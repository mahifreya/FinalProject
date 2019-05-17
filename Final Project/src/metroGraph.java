import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import org.json.*;


public class metroGraph //extends JPanel
{
    private List<Station> vertices = new ArrayList<Station>();
    private String start;
    private String end;
    private final String[] colors = {"RD","YL", "GR", "BL", "OR", "SV"};

    public metroGraph(String start, String end)
    {
        this.start = start;
        this.end = end;
        init();
    }

    public void init() //initializes the graph
    {
        JsonReader read;
        for (int i = 0; i < colors.length; i++)
        {
            read = new JsonReader("https://api.wmata.com/Rail.svc/json/jStations[?" + colors[i] + "]");
            JSONObject obj = new JSONObject(read.getJSON());
            JSONArray stations = obj.getJSONArray("Stations");
            for(int k = 0; k < stations.length(); k++)
            {
                JSONObject item = stations.getJSONObject(i);
                String name = item.getString("Name");
                if(hasStation(name))
                {
                    getStation(name).getColors().add(item.getString("LineCode1"));
                }
                else
                {
                    ArrayList<String> lines = new ArrayList<>();
                    lines.add(item.getString("LineCode1"));
                    ArrayList<Station> neighbors = new ArrayList<>();
                    vertices.add(new Station(name, lines, neighbors));
                }
            }

        }
             //JsonReader read = new JsonReader("https://api.wmata.com/Rail.svc/json/jLines");

    }

    public boolean hasStation(String name)
    {
        for(Station s : vertices)
        {
            if(s.getName().equals(name))
                return true;
        }
        return false;
    }

    public Station getStation(String name)
    {
        for(Station s: vertices)
        {
            if(s.getName().equals(name))
                return s;
        }
        return null;
    }
}
//hi