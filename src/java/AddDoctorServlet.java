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

@WebServlet("/AddDoctorServlet")
public class AddDoctorServlet extends HttpServlet {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/HospitalDB?useSSL=false";
    private static final String JDBC_USER = "root"; // Change this to your DB user
    private static final String JDBC_PASS = ""; // Change this to your DB password

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String specialty = request.getParameter("specialty");
        int experience = Integer.parseInt(request.getParameter("experience"));

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            String query = "INSERT INTO doctors (name, specialty, experience) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, specialty);
            pst.setInt(3, experience);
            pst.executeUpdate();

            out.println("<h2>Doctor Added Successfully!</h2>");
            out.println("<a href='add_doctor.html'>Add Another Doctor</a>");
            out.println("<br><a href='index.html'>Go to Home</a>");

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
        }
    }
}