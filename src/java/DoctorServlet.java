import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/DoctorServlet")


public class DoctorServlet extends HttpServlet {
    private final String DB_URL = "jdbc:mysql://localhost:3306/hospital";
    private final String USER = "root";
    private final String PASS = "";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");

        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            switch (action) {
                case "addDoctor":
                    String name = request.getParameter("name");
                    String specialization = request.getParameter("specialization");
                    String email = request.getParameter("email");
                    String password = request.getParameter("password");

                    PreparedStatement ps = con.prepareStatement("INSERT INTO doctors (name, specialization, email, password) VALUES (?, ?, ?, ?)");
                    ps.setString(1, name);
                    ps.setString(2, specialization);
                    ps.setString(3, email);
                    ps.setString(4, password);
                    int result = ps.executeUpdate();

                    if (result > 0) {
                        out.println("<h3>Doctor added successfully!</h3>");
                    } else {
                        out.println("<h3>Failed to add doctor.</h3>");
                    }
                    break;

                default:
                    out.println("<h3>Unknown action: " + action + "</h3>");
            }
        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}