import java.util.*;

public class Station
{
    private final String stationCode;
    private final String name;
    private List<String> colors;
    private Map<Station, Integer> neighbors = new HashMap<>();
    private double timeToStart;
    private double predictedTime;

    public Station(String stationCode, String name, ArrayList<String> colors, Map<Station, Integer>neighbors)
    {
        this.stationCode = stationCode;
        this.name = name;
        this.colors = colors;
        this.neighbors = neighbors;
    }

    public String getStationCode() { return stationCode; }

    public String getName() { return name; }

    public List<String> getColors() { return colors; }

    public Map<Station, Integer> getNeighbors(){return neighbors;};

    public void addNeighbor(Station s, int time){neighbors.put(s, time);}

    public void setNeighbors(Map<Station, Integer> s) {neighbors = s;}

    public void setTimeToStart(double time) { timeToStart = time;}

    public double getTimeToStart(){return timeToStart;}

    public void setPredictedTime(double time) { predictedTime = time;}

    public double getPredictedTime(){return predictedTime;}
}
