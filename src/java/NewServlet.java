import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital";
    private static final String USER = "root";
    private static final String PASS = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
            String action = request.getParameter("action");

            if (action != null) {
                switch (action) {
                    case "doctorLogin":
                        doctorLogin(request, con, out);
                        break;
                    case "addDoctor":
                        addDoctor(request, con, out);
                        break;
                    case "addPatient":
                        addPatient(request, con, out);
                        break;
                    case "scheduleAppointment":
                        scheduleAppointment(request, con, out);
                        break;
                    case "viewDoctors":
                        viewDoctors(con, out);
                        break;
                    case "viewPatients":
                        viewPatients(con, out);
                        break;
                    case "viewAppointments":
                        viewAppointments(con, out);
                        break;
                    default:
                        out.println("<h3>Invalid action.</h3>");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }
    }

    private void doctorLogin(HttpServletRequest request, Connection con, PrintWriter out) throws Exception {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM doctors WHERE email = ? AND password = ?");
        ps.setString(1, email);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            out.println("<h3>Login successful! Welcome, Dr. " + rs.getString("name") + "</h3>");
        } else {
            out.println("<h3>Invalid credentials.</h3>");
        }
    }

    private void addDoctor(HttpServletRequest request, Connection con, PrintWriter out) throws Exception {
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
            out.println("<h3>Error adding doctor.</h3>");
        }
    }

    private void addPatient(HttpServletRequest request, Connection con, PrintWriter out) throws Exception {
        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String disease = request.getParameter("disease");
        int doctor_assigned = Integer.parseInt(request.getParameter("doctor_assigned"));

        PreparedStatement ps = con.prepareStatement("INSERT INTO patients (name, age, disease, doctor_assigned) VALUES (?, ?, ?, ?)");
        ps.setString(1, name);
        ps.setInt(2, age);
        ps.setString(3, disease);
        ps.setInt(4, doctor_assigned);
        int result = ps.executeUpdate();

        if (result > 0) {
            out.println("<h3>Patient added successfully!</h3>");
        } else {
            out.println("<h3>Error adding patient.</h3>");
        }
    }

    private void scheduleAppointment(HttpServletRequest request, Connection con, PrintWriter out) throws Exception {
        int patient_id = Integer.parseInt(request.getParameter("patient_id"));
        int doctor_id = Integer.parseInt(request.getParameter("doctor_id"));
        String appointment_date = request.getParameter("appointment_date");

        PreparedStatement ps = con.prepareStatement("INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)");
        ps.setInt(1, patient_id);
        ps.setInt(2, doctor_id);
        ps.setString(3, appointment_date);
        int result = ps.executeUpdate();

        if (result > 0) {
            out.println("<h3>Appointment scheduled successfully!</h3>");
        } else {
            out.println("<h3>Error scheduling appointment.</h3>");
        }
    }

    private void viewDoctors(Connection con, PrintWriter out) throws Exception {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM doctors");
        ResultSet rs = ps.executeQuery();

        out.println("<h3>Doctors List:</h3>");
        while (rs.next()) {
            out.println("Dr. " + rs.getString("name") + " - " + rs.getString("specialization") + "<br>");
        }
    }

    private void viewPatients(Connection con, PrintWriter out) throws Exception {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM patients");
        ResultSet rs = ps.executeQuery();

        out.println("<h3>Patients List:</h3>");
        while (rs.next()) {
            out.println(rs.getString("name") + " - " + rs.getString("disease") + "<br>");
        }
    }

    private void viewAppointments(Connection con, PrintWriter out) throws Exception {
        PreparedStatement ps = con.prepareStatement("SELECT * FROM appointments");
        ResultSet rs = ps.executeQuery();

        out.println("<h3>Appointments:</h3>");
        while (rs.next()) {
            out.println("Appointment ID: " + rs.getInt("appointment_id") + " - Date: " + rs.getString("appointment_date") + "<br>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}