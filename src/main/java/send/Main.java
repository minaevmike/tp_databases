package send;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by Mike on 17.03.14.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost request = new HttpPost("http://localhost:8083/user/create");
            StringEntity params =new StringEntity("{'username': 'user1', 'about': 'hello im user1', 'isAnonymous': False, 'name': 'John', 'email': 'exam2135ple@mail.ru'} ");
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            System.out.println(response.toString());
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }
}
