import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DistanceTimeJson
{
    private String json = "";
    private Timer timer = new Timer(0.1);

    public DistanceTimeJson(String url, String startCode, String endCode)
    {
        HttpClient httpclient = HttpClients.createDefault();
        timer.waitFor();
        try
        {
            URIBuilder builder = new URIBuilder("https://api.wmata.com/Rail.svc/json/jSrcStationToDstStationInfo");

            builder.setParameter("FromStationCode", startCode);
            builder.setParameter("ToStationCode", endCode);

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("api_key", "fee26a6889234975a3b7eba5974aac13");


            // Request body
            /*StringEntity reqEntity = new StringEntity("{body}");
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
