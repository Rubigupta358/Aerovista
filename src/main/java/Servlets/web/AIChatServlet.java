 package Servlets.web;

import Servlets.dao.ProductDAO;
import Servlets.model.Product;
import com.google.gson.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@WebServlet("/AIChatServlet")
public class AIChatServlet extends HttpServlet {

    private String geminiApiKey;

    @Override
    public void init() throws ServletException {
        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            geminiApiKey = prop.getProperty("GEMINI_API_KEY");
            if (geminiApiKey == null || geminiApiKey.isEmpty()) {
                throw new ServletException("GEMINI_API_KEY not found in config.properties");
            }
        } catch (IOException e) {
            throw new ServletException("Failed to load config.properties", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String userMessage = request.getParameter("message");
        if (userMessage == null) userMessage = "";

        HttpSession session = request.getSession(true);
        String prompt = buildJsonOnlyPrompt(userMessage);

        String modelRaw;
        try {
            modelRaw = callGeminiFlash(prompt);
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = new JsonObject();
            err.addProperty("reply", "⚠️ Error contacting AI service.");
            response.getWriter().write(err.toString());
            return;
        }

        JsonObject modelJson;
        System.out.println("Prompt sent to Gemini:\n" + prompt);
        System.out.println("Gemini raw response:\n" + modelRaw);

        try {
            modelJson = parseGeminiJson(modelRaw);
        } catch (Exception e) {
            JsonObject out = new JsonObject();
            out.addProperty("reply", modelRaw);
            response.getWriter().write(out.toString());
            return;
        }

        String intent = modelJson.has("intent") ? modelJson.get("intent").getAsString() : "fallback";
        String replyText = modelJson.has("reply_text") ? modelJson.get("reply_text").getAsString() : "";
        JsonObject params = modelJson.has("params") ? modelJson.getAsJsonObject("params") : new JsonObject();
        boolean complete = modelJson.has("complete") && modelJson.get("complete").getAsBoolean();

        JsonObject out = new JsonObject();

        if ("add_product".equalsIgnoreCase(intent)) {
            processAddProductFlow(session, params, replyText, complete, out);
        } else if ("view_product".equalsIgnoreCase(intent)) {
            out.addProperty("reply", (replyText == null || replyText.isEmpty())
                    ? "You can view all products here: <a href='productForm.jsp' target='_blank'>View Products</a>"
                    : replyText + " <a href='productForm.jsp' target='_blank'>View Products</a>");
        } else {
            out.addProperty("reply", (replyText != null && !replyText.isEmpty()) ? replyText : "I can help with 'add product' and 'view product'.");
        }

        response.getWriter().write(out.toString());
    }

    private String buildJsonOnlyPrompt(String userMessage) {
        String system = "You are an assistant that MUST reply with ONLY valid JSON (no surrounding text). " +
                "Valid JSON structure: " +
                "{\"intent\":\"<one of: add_product, view_product, fallback>\", " +
                "\"reply_text\":\"<text to show to user (may contain HTML links)>\", " +
                "\"params\":{...}, \"complete\":<true|false> }.\n\n" +
                "If user intent is add_product, try to extract as many fields as possible from user's message into params. " +
                "Allowed params: name (string), category (string), price (number), launch_date (yyyy-mm-dd). " +
                "Set complete=true only when all four fields are present and valid. " +
                "If some fields are missing, set complete=false and in reply_text clearly request the missing fields (comma separated). " +
                "Do not add any explanation outside the JSON. ";
        return system + "\n\nUser: " + userMessage;
    }

    private String callGeminiFlash(String prompt) throws IOException {
        if (geminiApiKey == null || geminiApiKey.isEmpty())
            throw new IOException("GEMINI_API_KEY not set in config.properties");

        String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + geminiApiKey;
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        JsonObject part = new JsonObject();
        part.addProperty("text", prompt);

        JsonObject contentsItem = new JsonObject();
        JsonArray parts = new JsonArray();
        parts.add(part);
        contentsItem.add("parts", parts);
        JsonArray contents = new JsonArray();
        contents.add(contentsItem);
        JsonObject body = new JsonObject();
        body.add("contents", contents);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes(StandardCharsets.UTF_8));
        }

        int code = conn.getResponseCode();
        InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        StringBuilder resp = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) resp.append(line).append("\n");
        }
        conn.disconnect();
        return resp.toString().trim();
    }

    private JsonObject parseGeminiJson(String raw) throws JsonParseException {
        JsonObject root = JsonParser.parseString(raw).getAsJsonObject();

        if (root.has("candidates")) {
            JsonArray candidates = root.getAsJsonArray("candidates");
            if (candidates.size() > 0) {
                JsonObject first = candidates.get(0).getAsJsonObject();
                if (first.has("content")) {
                    JsonObject content = first.getAsJsonObject("content");
                    if (content.has("parts")) {
                        JsonArray parts = content.getAsJsonArray("parts");
                        if (parts.size() > 0) {
                            JsonObject part = parts.get(0).getAsJsonObject();
                            if (part.has("text")) {
                                String text = part.get("text").getAsString().trim();
                                if (text.startsWith("```")) {
                                    int firstLineEnd = text.indexOf("\n");
                                    if (firstLineEnd != -1) text = text.substring(firstLineEnd + 1);
                                    if (text.endsWith("```")) text = text.substring(0, text.length() - 3);
                                }
                                return JsonParser.parseString(text.trim()).getAsJsonObject();
                            }
                        }
                    }
                }
            }
        }
        throw new JsonParseException("Could not parse Gemini JSON");
    }

    private void processAddProductFlow(HttpSession session, JsonObject params, String modelReplyText, boolean modelSaysComplete, JsonObject out) {
        Product temp = (Product) session.getAttribute("tempProduct");
        if (temp == null) {
            temp = new Product();
            session.setAttribute("tempProduct", temp);
        }

        if (params != null) {
            if (params.has("name") && !params.get("name").isJsonNull()) temp.setName(params.get("name").getAsString().trim());
            if (params.has("category") && !params.get("category").isJsonNull()) temp.setCategory(params.get("category").getAsString().trim());
            if (params.has("price") && !params.get("price").isJsonNull()) {
                try { temp.setPrice(params.get("price").getAsDouble()); } catch (Exception ignored) {}
            }
            if (params.has("launch_date") && !params.get("launch_date").isJsonNull()) {
                try { temp.setLaunchDate(java.sql.Date.valueOf(params.get("launch_date").getAsString().trim())); } catch (Exception ignored) {}
            }
        }

        StringBuilder missing = new StringBuilder();
        if (temp.getName() == null || temp.getName().trim().isEmpty()) missing.append("name, ");
        if (temp.getCategory() == null || temp.getCategory().trim().isEmpty()) missing.append("category, ");
        if (temp.getPrice() <= 0) missing.append("price, ");
        if (temp.getLaunchDate() == null) missing.append("launch_date, ");

        if (missing.length() > 0) {
            out.addProperty("reply", (modelReplyText != null && !modelReplyText.trim().isEmpty())
                    ? modelReplyText
                    : "Please provide these fields: " + missing.toString().replaceAll(", $", ""));
            out.addProperty("complete", false);
            session.setAttribute("tempProduct", temp);
        } else {
            try {
                new ProductDAO().addProduct(temp);
                String success = "✅ Product added successfully! Name: " + temp.getName()
                        + ", Category: " + temp.getCategory()
                        + ", Price: " + temp.getPrice()
                        + ", Launch Date: " + temp.getLaunchDate();
                out.addProperty("reply", success);
                out.addProperty("complete", true);
                session.removeAttribute("tempProduct");
            } catch (Exception ex) {
                ex.printStackTrace();
                out.addProperty("reply", "⚠️ Unable to save product. Please try again later.");
                out.addProperty("complete", false);
            }
        }
    }
}
