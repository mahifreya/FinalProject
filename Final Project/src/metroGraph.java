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

    public metroGraph(String start, String end)
    {
        this.start = start;
        this.end = end;
        init();
    }

    public void init() //initializes the graph
    {
        JsonReader read = new JsonReader("https://api.wmata.com/Rail.svc/json/jLines");
        JSONObject obj = new JSONObject(read.getJSON());
    }
}
