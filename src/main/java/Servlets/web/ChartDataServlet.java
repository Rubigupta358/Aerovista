package Servlets.web;

import com.google.gson.Gson;
import Servlets.ConnectDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

@WebServlet("/ChartData")
public class ChartDataServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String table = request.getParameter("table"); // table name from JSP

		List<Map<String, Object>> dataList = new ArrayList<>();

		try {
			Connection con = ConnectDB.getConnectDB();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + table);

			ResultSetMetaData meta = rs.getMetaData();
			int colCount = meta.getColumnCount();

			while (rs.next()) {
				Map<String, Object> row = new HashMap<>();
				for (int i = 1; i <= colCount; i++) {
					row.put(meta.getColumnName(i), rs.getObject(i));
				}
				dataList.add(row);
			}

			rs.close();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		Gson gson = new Gson();
		PrintWriter out = response.getWriter();
		out.print(gson.toJson(dataList));
		out.flush();
	}
}
