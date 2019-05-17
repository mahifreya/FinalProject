import java.util.ArrayList;
import java.util.List;

public class Station
{
    private final String name;
    private List<String> colors;
    private List<Station> neighbors;

    public Station(String name, ArrayList<String> colors, ArrayList<Station>neighbors)
    {
        this.name = name;
        this.colors = colors;
        this.neighbors = neighbors;
    }

    public String getName() {
        return name;
    }

    public List<String> getColors() {
        return colors;
    }

    public List<Station> getNeighbors(){return neighbors;};

    public void addNeighbor(Station s){neighbors.add(s);}

}
