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
        try{
            Scanner console = new Scanner(new File("redline.txt"));
            String js = "";
            while(console.hasNextLine());
                js+= console.nextLine();
            JSONObject obj = new JSONObject(js);
            JSONArray stations = obj.getJSONArray("Stations");
            for(int i = 0; i < stations.length(); i++)
            {
                JSONObject item = stations.getJSONObject(i);
                ArrayList<String> color = new ArrayList<>();
                color.add("red");
                vertices.add(new Station(item.getString("Name"),color , null));
            }
          /*  for(int i = 0; i < vertices.size(); i++)
            {
                if(i == 0) {
                    vertices.get(i).setPrevious(null);
                    vertices.get(i).setNext(vertices.get(i + 1));
                }
                else if(i == vertices.size() - 1)
                {
                    vertices.get(i).setPrevious(vertices.get(i - 1));
                    vertices.get(i).setNext(null);
                }
                else {
                    vertices.get(i).setPrevious(vertices.get(i - 1));
                    vertices.get(i).setNext(vertices.get(i + 1));
                }
            }
*/
        }
        catch (FileNotFoundException e) {
            System.exit(0);
        }
    }
}
