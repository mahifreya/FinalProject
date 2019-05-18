// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
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

    public DistanceTimeJson(String url, String startCode, String endCode)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder("https://api.wmata.com/Rail.svc/json/jSrcStationToDstStationInfo");

            builder.setParameter("FromStationCode", startCode);
            builder.setParameter("ToStationCode", endCode);

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("api_key", "44444444444");


            // Request body
            /*StringEntity reqEntity = new StringEntity("{body}");
            request.setEntity(reqEntity);
*/
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                json =  EntityUtils.toString(entity);
                System.out.println(json);
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
