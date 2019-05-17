// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class JsonReader
{
    private String json = "";
    public JsonReader(String url)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder(url);


            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("api_key", "fee26a6889234975a3b7eba5974aac13");


            // Request body
           /* StringEntity reqEntity = new StringEntity("{body}");
            request.setEntity(reqEntity);
*/
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
               json =  EntityUtils.toString(entity);
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public String getJSON()
    {
        return json;
    }
}