package frontend;



import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import database.DataService;
import database.User;
import database.UserDAO;
import database.executor.TransactionExecutor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.text.ParseException;

public class Frontend extends HttpServlet {
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String param = request.getPathInfo();
        String [] params = param.split("/");
        Connection connection = DataService.getConnection();
        switch (params[1]){
            case "user":
                switch (params[2]){
                    case "create":
                        String json = getBody(request);
                        JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
                        TransactionExecutor exec = new TransactionExecutor();
                        try {
                            StringBuilder query = new StringBuilder();
                            query.
                                    append("INSERT INTO user (username, email, name, about, isAnonymous) VALUES (").
                                    append(escape(object.getString("username")) + " ,").
                                    append(escape(object.getString("email")) + " ,").
                                    append(escape(object.getString("name")) + " ,").
                                    append(escape(object.getString("about")) + " ,").
                                    append(object.getBoolean("isAnonymous") + ");");

                            exec.execUpdate(connection, query.toString());
                        }
                        catch (MySQLIntegrityConstraintViolationException e){

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        finally {
                            try{
                                User user = UserDAO.getByEmail(connection, object.getString("email"));
                                JSONObject result = new JSONObject();
                                result.put("code", 0);
                                JSONObject responseJSON = new JSONObject();
                                responseJSON.put("about", user.getAbout());
                                responseJSON.put("email", user.getEmail());
                                responseJSON.put("id", user.getId());
                                responseJSON.put("isAnonymous", user.getIsAnonymous());
                                responseJSON.put("name", user.getName());
                                responseJSON.put("username", user.getUsername());
                                result.put("response", responseJSON);
                                System.out.println(result.toString());
                                response.getWriter().println(result.toString());
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        break;
                    case "details":
                        String email = request.getParameter("user");
                }
            break;
        }

    }

    private String escape(String str)
    {
        return "'" + str + "'";
    }

    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        body = stringBuilder.toString();
        return body;
    }
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println("Nice");

    }
}
