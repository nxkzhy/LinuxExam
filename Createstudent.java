import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import com.google.gson.reflect.TypeToken;
import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/Createstudent")
public class Createstudent extends HttpServlet {
   final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
   final static String URL = "jdbc:mysql://42.192.183.84/linux_final";
   final static String USER = "root";
   final static String PASS = "201014zhy@NXK";
   final static String SQL_INSERT_STUDENT = "INSERT INTO student(name,age) VALUES (?, ?)";

   public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");

      student req = getRequestBody(request);

      PrintWriter out = response.getWriter();

      out.println(Createstudent(req));
      out.flush();
      out.close();
   }

   private student getRequestBody(HttpServletRequest request) throws IOException {
      student note = new student();
      StringBuffer bodyJ = new StringBuffer();
      String line = null;
      BufferedReader reader = request.getReader();
      while ((line = reader.readLine()) != null)
         bodyJ.append(line);
      Gson gson = new Gson();
      note = gson.fromJson(bodyJ.toString(), new TypeToken<student>() {
      }.getType());
      return note;
   }

   private int Createstudent(student req) {
      Connection conn = null;
      PreparedStatement stmt = null;
      int retcode = -1;
      try {
         Class.forName(JDBC_DRIVER);
         conn = DriverManager.getConnection(URL, USER, PASS);
         stmt = conn.prepareStatement(SQL_INSERT_STUDENT);

         stmt.setString(1, req.name);
         stmt.setString(2, req.age);
         int row = stmt.executeUpdate();
         if (row > 0)
            retcode = 1;

         stmt.close();
         conn.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (stmt != null)
               stmt.close();
            if (conn != null)
               conn.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
      return retcode;
   }
}
