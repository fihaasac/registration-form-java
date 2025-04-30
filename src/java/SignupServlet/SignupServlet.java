package SignupServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("username");
        String email = request.getParameter("email");
        String psw = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // 1. Username validation (only a-z, A-Z, 0-9)
        if (!name.matches("^[a-zA-Z0-9]+$")) {
            out.println("<script>alert('Invalid username! Only a-z, A-Z, and 0-9 allowed.'); window.history.back();</script>");
            return;
        }

        // 2. Password should contain at least one special character
        if (!psw.matches(".*[!@#$%^&*()].*")) {
            out.println("<script>alert('Password must contain at least one special character.'); window.history.back();</script>");
            return;
        }

        // 3. Passwords must match
        if (!psw.equals(confirmPassword)) {
            out.println("<script>alert('Passwords do not match.'); window.history.back();</script>");
            return;
        }

        try {
            // Connect to MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/userdb1", "root", "");

            // Insert into database
            String sql = "INSERT INTO users(username, email, password) VALUES(?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, psw);
            stmt.executeUpdate();

            // Close connections
            stmt.close();
            conn.close();

            // Success message
            out.println("<h2>Registration successful!</h2>");
            out.println("<p>Username: " + name + "</p>");
            out.println("<p>Email: " + email + "</p>");

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
        }
    }
}
