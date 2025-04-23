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

@WebServlet("/RegisterPatientServlet")
public class RegisterPatientServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/HospitalDB?useSSL=false";
    private static final String JDBC_USER = "root"; // Change to your DB user
    private static final String JDBC_PASS = "";     // Change to your DB password

    // Handle POST requests (form submission)
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String gender = request.getParameter("gender");
        String disease = request.getParameter("disease");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);

            String query = "INSERT INTO patients (name, age, gender, disease) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setInt(2, age);
            pst.setString(3, gender);
            pst.setString(4, disease);
            pst.executeUpdate();

            out.println("<h2>✅ Patient Registered Successfully!</h2>");
            out.println("<a href='register.html'>➕ Register Another Patient</a>");
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>❌ Error: " + e.getMessage() + "</h3>");
        }
    }

    // Handle GET requests (e.g., direct browser access)
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h2>⚠️ Access via GET not allowed. Please submit the form.</h2>");
        out.println("<a href='register.html'>Go to Patient Registration</a>");
    }
}
