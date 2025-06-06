import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ViewDoctorServlet")
public class ViewDoctorServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/HospitalDB?useSSL=false";
    private static final String JDBC_USER = "root"; // Change this to your DB user
    private static final String JDBC_PASS = ""; // Change this to your DB password

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            String query = "SELECT * FROM doctors";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            out.println("<h2>Doctor List</h2>");
            out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Specialty</th><th>Experience (Years)</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" + rs.getString("name") + "</td><td>" + rs.getString("specialty") + "</td><td>" + rs.getInt("experience") + "</td></tr>");
            }
            out.println("</table>");
            out.println("<br><a href='index.html'>Go to Home</a>");

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}