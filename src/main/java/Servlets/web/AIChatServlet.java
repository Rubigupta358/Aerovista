package Servlets.web;

import Servlets.dao.FeedbackDAO;
import Servlets.dao.InvestmentDAO;
import Servlets.dao.ProductDAO;
import Servlets.dao.SalesDAO;
import Servlets.model.Feedback;
import Servlets.model.Investment;
import Servlets.model.Product;
import Servlets.model.Sale;
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
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
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
        } else if ("add_sales".equalsIgnoreCase(intent)) {
            processAddSaleaFlow(session, params, replyText, complete, out);
        } else if ("view_sales".equalsIgnoreCase(intent)) {
            out.addProperty("reply", (replyText == null || replyText.isEmpty())
                    ? "You can view all sales here: <a href='sales.jsp' target='_blank'>View Sales</a>"
                    : replyText + " <a href='sales.jsp' target='_blank'>View Sales</a>");
        }else if ("add_investment".equalsIgnoreCase(intent)) {
        	processAddInvestmentFlow(session, params, replyText, complete, out);
        } else if ("view_investment".equalsIgnoreCase(intent)) {
            out.addProperty("reply", (replyText == null || replyText.isEmpty())
                    ? "You can view all investment here: <a href='investment.jsp' target='_blank'>View Investment</a>"
                    : replyText + " <a href='investment.jsp' target='_blank'>View Investment</a>");
        } else if ("add_feedback".equalsIgnoreCase(intent)) {

        	processAddFeedbackFlow(session, params, replyText, complete, out);
        } else if ("view_feedback".equalsIgnoreCase(intent)) {
            out.addProperty("reply", (replyText == null || replyText.isEmpty())
                    ? "You can view all feedback here: <a href='feedback.jsp' target='_blank'>View feedback</a>"
                    : replyText + " <a href='feedback.jsp' target='_blank'>View feedback</a>");
        } else {
            out.addProperty("reply", (replyText != null && !replyText.isEmpty())
                    ? replyText
                    : "I can help with 'add product', 'view product', 'add sales', 'view sales','add investment','view investment','add feedback' and 'view feedback'.");
        }

        response.getWriter().write(out.toString());
    }

    private String buildJsonOnlyPrompt(String userMessage) {
    	String system = "You are an assistant that MUST reply with ONLY valid JSON (no surrounding text). " +
    		    "Valid JSON structure: " +
    		    "{\"intent\":\"<one of: add_product, view_product, add_sales, view_sales, add_investment, view_investment, add_feedback, view_feedback, fallback>\", " +
    		    "\"reply_text\":\"<text to show to user>\", " +
    		    "\"params\":{...}, \"complete\":<true|false> }.\n\n" +

    		    "For add_product: params = name, category, price, launch_date (yyyy-mm-dd).\n" +
    		    "For add_sales: params = product_id (number), quantity (number), revenue (number), sale_date (yyyy-mm-dd).\n" +
    		    "For add_investment: params = product_id (number), month, investment_type, amount.\n" +
    		    "For add_feedback: params = product_id (number), feature_name (string), rating (number), feedback_text (string).\n" +

    		    "Set complete=true only when all required fields are present and valid. " +
    		    "If fields missing, set complete=false and in reply_text clearly request missing fields. " +
    		    "Do not add any explanation outside the JSON.";

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

    private void processAddProductFlow(HttpSession session, JsonObject params, String modelReplyText,
                                       boolean modelSaysComplete, JsonObject out) {
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

    
    private void processAddSaleaFlow(HttpSession session, JsonObject params, String modelReplyText,
            boolean modelSaysComplete, JsonObject out) {
Sale temp = (Sale) session.getAttribute("tempSale");
if (temp == null) {
temp = new Sale();
session.setAttribute("tempSale", temp);
}

if (params != null) {
if (params.has("product_id") && !params.get("product_id").isJsonNull()) temp.setProductId(params.get("product_id").getAsInt());
if (params.has("quantity") && !params.get("quantity").isJsonNull()) temp.setQuantity(params.get("quantity").getAsInt());
if (params.has("revenue") && !params.get("revenue").isJsonNull()) {
try { temp.setRevenue(params.get("revenue").getAsDouble()); } catch (Exception ignored) {}
}
if (params.has("sale_date") && !params.get("sale_date").isJsonNull()) {
try { temp.setSaleDate(java.sql.Date.valueOf(params.get("sale_date").getAsString().trim())); } catch (Exception ignored) {}
}
}
    
    
    
        StringBuilder missing = new StringBuilder();
        if (temp.getProductId() <= 0) missing.append("product_id, ");
        if (temp.getQuantity() <= 0) missing.append("quantity, ");
        if (temp.getRevenue() <= 0) missing.append("revenue, ");
        if (temp.getSaleDate() == null) missing.append("sale_date, ");
        
         

        
        if (missing.length() > 0) {
            out.addProperty("reply", (modelReplyText != null && !modelReplyText.trim().isEmpty())
                    ? modelReplyText
                    : "Please provide these fields: " + missing.toString().replaceAll(", $", ""));
            out.addProperty("complete", false);
            session.setAttribute("tempSale", temp);
        } else {
            try {
                new SalesDAO().addSale(temp);
                String success = "✅ Sale added successfully! Product ID: \" + temp.getProductId()"
                		+ ", Quantity: " + temp.getQuantity()
                        + ", Revenue: " + temp.getRevenue()
                        + ", Sale Date: " + temp.getSaleDate();
                out.addProperty("reply", success);
                out.addProperty("complete", true);
                session.removeAttribute("tempSale");
            }  catch (Exception ex) {
                ex.printStackTrace();
                out.addProperty("reply", "⚠️ Unable to save sale. Please try again later.");
                out.addProperty("complete", false);
            }
        }
    } 
			        
				    private void processAddInvestmentFlow(HttpSession session, JsonObject params, String modelReplyText,
				            boolean modelSaysComplete, JsonObject out) {
				Investment temp = (Investment) session.getAttribute("tempInvestment");
				if (temp == null) {
				temp = new Investment();
				session.setAttribute("tempInvestment", temp);
				}
				
				if (params != null) {
				if (params.has("product_id") && !params.get("product_id").isJsonNull()) {
				temp.setProductId(params.get("product_id").getAsInt());
				}
				if (params.has("month") && !params.get("month").isJsonNull()) {
				temp.setMonth(params.get("month").getAsString().trim());
				}
				if (params.has("investment_type") && !params.get("investment_type").isJsonNull()) {
				temp.setInvestmentType(params.get("investment_type").getAsString().trim());
				}
				if (params.has("amount") && !params.get("amount").isJsonNull()) {
				try { temp.setAmount(params.get("amount").getAsDouble()); } catch (Exception ignored) {}
				}
				}
				
				StringBuilder missing = new StringBuilder();
				if (temp.getProductId() <= 0) missing.append("product_id, ");
				if (temp.getMonth() == null || temp.getMonth().trim().isEmpty()) missing.append("month, ");
				if (temp.getInvestmentType() == null || temp.getInvestmentType().trim().isEmpty()) missing.append("investment_type, ");
				if (temp.getAmount() <= 0) missing.append("amount, ");
				
				if (missing.length() > 0) {
				out.addProperty("reply", (modelReplyText != null && !modelReplyText.trim().isEmpty())
				? modelReplyText
				: "Please provide these fields: " + missing.toString().replaceAll(", $", ""));
				out.addProperty("complete", false);
				session.setAttribute("tempInvestment", temp);
				} else {
				try {
				new InvestmentDAO().addInvestment(temp);
				String success = "✅ Investment added successfully! Product ID: " + temp.getProductId()
				+ ", Month: " + temp.getMonth()
				+ ", Investment Type: " + temp.getInvestmentType()
				+ ", Amount: " + temp.getAmount();
				out.addProperty("reply", success);
				out.addProperty("complete", true);
				
				// Send new record to frontend for instant table update
				out.add("newInvestment", new Gson().toJsonTree(temp));
				
				session.removeAttribute("tempInvestment");
				} catch (Exception ex) {
				ex.printStackTrace();
				out.addProperty("reply", "⚠️ Unable to save investment. Please try again later.");
				out.addProperty("complete", false);
				}
				}
				}
				
				    
    
    private void processAddFeedbackFlow(HttpSession session, JsonObject params, String modelReplyText,
            boolean modelSaysComplete, JsonObject out) {

        // Retrieve or create temp Feedback object
        Feedback temp = (Feedback) session.getAttribute("tempFeedback");
        
        if (temp == null) {
            temp = new Feedback();
            session.setAttribute("tempFeedback", temp);
            System.out.println("Here is temp " + temp.toString());
        }

    	//System.out.println("Product ID :" +temp.getProductId() +"feature :" + temp.getFeatureName()+ "Rating:" +temp.getRating()+ "temp:" +temp.getFeedbackText());
        System.out.println("printing params line 382"+params.toString());

        // Update temp with incoming params
        if (params != null) {
            if (params.has("product_id") && !params.get("product_id").isJsonNull()) {
            	System.out.println("Param - Product ID"+params.get("product_id").getAsInt());
                temp.setProductId(params.get("product_id").getAsInt());
            }
            if (params.has("feature_name") && !params.get("feature_name").isJsonNull()) {
            	System.out.println("Param - Feature: "+params.get("feature_name").toString());
            	temp.setFeatureName(params.get("feature_name").getAsString().trim()); // ✅ changed
            }
            if (params.has("rating") && !params.get("rating").isJsonNull()) {
            	System.out.println("Param - Rating: "+params.get("rating").getAsInt());
                temp.setRating(params.get("rating").getAsInt());
            }
            if (params.has("feedback_text") && !params.get("feedback_text").isJsonNull()) {
            	System.out.println("Param - Feedback: "+params.get("feedback_text").getAsString().trim());
                temp.setFeedbackText(params.get("feedback_text").getAsString().trim()); // ✅ changed
            }
        }

    	System.out.println("HERE------------FEEDBACK-395");
        // Check for missing fields
        StringBuilder missing = new StringBuilder();
        if (temp.getProductId() <= 0) missing.append("product_id, ");
        if (temp.getFeatureName() == null || temp.getFeatureName().trim().isEmpty()) missing.append("Feature, ");
        if (temp.getRating() <= 0) missing.append("Rating, ");
        if (temp.getFeedbackText() == null || temp.getFeedbackText().trim().isEmpty()) missing.append("feedback, ");
    // Respond based on completeness
        if (missing.length() > 0) {
            out.addProperty("reply", (modelReplyText != null && !modelReplyText.trim().isEmpty())
                ? modelReplyText
                : "Please provide these fields: " + missing.toString().replaceAll(", $", ""));
            out.addProperty("complete", false);
            session.setAttribute("tempFeedback", temp);

        	System.out.println("Inside IF HERE------------FEEDBACK-412");
        } else {
            try {
            	System.out.println("----------Trying to save-------------");
                new FeedbackDAO().addFeedback(temp);  // Save to DB
                System.out.println("-------------After saving--------------");
                String success = "✅ Record added successfully! "
                    + "Product ID: " + temp.getProductId()
                    + ", Feature: " + temp.getFeatureName()
                    + ", Rating: " + temp.getRating()
                    + ", Feedback: " + temp.getFeedbackText();
                out.addProperty("reply", success);
                out.addProperty("complete", true);
                session.removeAttribute("tempFeedback");

            	System.out.println("Inside else HERE------------FEEDBACK-4277");
            } catch (Exception ex) {
                ex.printStackTrace();
                out.addProperty("reply", "⚠️ Unable to save record. Please try again later.");
                out.addProperty("complete", false);
            }
        }
    }

}
 