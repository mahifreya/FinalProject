// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SequenceJson
{
    private String json = "";
    private Timer timer = new Timer(0.1);

    public SequenceJson(String url, String fromCode, String toCode)
    {
        HttpClient httpclient = HttpClients.createDefault();
        timer.waitFor();
        try
        {
            URIBuilder builder = new URIBuilder(url);

            builder.setParameter("FromStationCode", fromCode);
            builder.setParameter("ToStationCode", toCode);

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
                //System.out.println(EntityUtils.toString(entity));
                json = EntityUtils.toString(entity);
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

