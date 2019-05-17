import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import org.json.*;


public class metroGraph extends JPanel
{
    private List<Station> vertices = new ArrayList<Station>();
    private String start;
    private String end;
    private final String[] colors = {"RD","YL", "GR", "BL", "OR", "SV"};
    private final int speed = 33;

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
                    vertices.add(new Station(item.getString("Code"), name, lines, null));
                }
            }
            for(int h = 0; h < vertices.size(); h++)
            {
                if(i == 0) {
                    vertices.get(i).addNeighbor(vertices.get(i+1), 0);
                }
                else if(i == vertices.size() - 1)
                {
                    vertices.get(i).addNeighbor(vertices.get(i-1), 0);
                    vertices.get(i).addNeighbor(vertices.get(i+1), 0);
                }
                else
                {
                    vertices.get(i).addNeighbor(vertices.get(i-1), 0);
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

    public List<String> path()
    {

            Station startNode = getStation(start);
            Station endNode = getStation(end);

            // setup for A*
            HashMap<Station,Station> parentMap = new HashMap<Station, Station>();
            HashSet<Station> visited = new HashSet<Station>();
            Map<Station, Double> distances = initializeAllToInfinity();

            Queue<Station> priorityQueue = new PriorityQueue<>();

            //  enque StartNode, with distance 0
            startNode.setTimeToStart(0);
            distances.put(startNode,0.0);
            priorityQueue.add(startNode);
            Station current = null;

            while (!priorityQueue.isEmpty()) {
                current = priorityQueue.remove();

                if (!visited.contains(current) ){
                    visited.add(current);
                    // if last element in PQ reached
                    if (current.equals(endNode)) return reconstructPath(startNode, endNode, parentMap);

                    Set<Station> neighbors = current.getNeighbors().keySet();
                    for (Station neighbor : neighbors) {
                        if (!visited.contains(neighbor) ){

                            // calculate predicted time to the end node
                            double predictedTime = predictTime(neighbor, endNode);

                            // 1. calculate time to neighbor. 2. calculate time from start node
                            double neighborTime = current.getNeighbors().get(neighbor);
                            double totalTime = current.getTimeToStart() + neighborTime + predictedTime;

                            // check if time smaller
                            if(totalTime < distances.get(neighbor) ){
                                // update n's time
                                distances.put(neighbor, totalTime);
                                // used for PriorityQueue
                                neighbor.setTimeToStart(totalTime);
                                neighbor.setPredictedTime(predictedTime);
                                // set parent
                                parentMap.put(neighbor, current);
                                // enqueue
                                priorityQueue.add(neighbor);
                            }
                        }
                    }
                }
            }
            return null;
    }

    private Map<Station, Double> initializeAllToInfinity() {
        Map<Station,Double> distances = new HashMap<>();

        Iterator<Station> iter = vertices.iterator();
        while (iter.hasNext()) {
            Station node = iter.next();
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        return distances;
    }

    private List<String> reconstructPath(Station start, Station goal,
                                                 Map<Station, Station> parentMap) {
        // construct output list
        LinkedList<String> path = new LinkedList<>();
        Station currNode = goal;
        while(!currNode.equals(start)){
            path.addFirst(currNode.getName());
            currNode = parentMap.get(currNode);
        }
        path.addFirst(start.getName());
        return path;
    }

    private double predictTime(Station start, Station end)
    {
        JsonReader distance = new JsonReader("https://api.wmata.com/Rail.svc/json/jSrcStationToDstStationInfo[?" + start.getStationCode() + "][&" + end.getStationCode()+ "]");
        JSONObject obj = new JSONObject(distance.getJSON());
        JSONObject item = obj.getJSONObject("StationToStationInfos");
        double miles = Double.parseDouble(item.getString("CompositeMiles"));
        return miles / speed * 60;
    }
}