import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import org.json.*;


public class MetroGraph extends JPanel
{
    private List<Station> vertices = new ArrayList<Station>();
    private String start;
    private String end;
    private final String[] colors = {"RD","YL", "GR", "BL", "OR", "SV"};
    private JLabel path;
    private JComboBox<String> f;
    private JComboBox<String> t;
    private JButton search;
    //I moved all the Jbuttons and labels that my IDE said "could be converted to local variables" to the constructor

    public MetroGraph()
    {
        //initializes the graph with all the data on the stations from wmata API
        fillGraph();
        String [] verticesNames = new String [vertices.size()];
        int index = 0;
        for(Station s: vertices)
            verticesNames[index++] = s.getName();

        //creates the GUI aspects of this project
        setLayout(new BorderLayout());

        JPanel north = new JPanel();

        JPanel innerWest = new JPanel();
        innerWest.setLayout(new FlowLayout());
        JLabel from = new JLabel("Start: ");
        from.setFont(new Font("Times New Roman", Font.BOLD, 24));
        f = new JComboBox<String>(verticesNames);
        f.setMaximumSize(new Dimension(30, 30));
        f.setFont(new Font("Times New Roman", Font.BOLD, 24));
        innerWest.add(from);
        innerWest.add(f);
        north.add(innerWest, BorderLayout.WEST);

        JLabel title = new JLabel("Metro Map");
        title.setFont(new Font("Times New Roman", Font.BOLD, 50));
        north.add(title, BorderLayout.CENTER);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel innerEast = new JPanel();
        innerEast.setLayout(new FlowLayout());
        JLabel to = new JLabel("End: ");
        to.setFont(new Font("Times New Roman", Font.BOLD, 24));
        t = new JComboBox<String>(verticesNames);
        t.setMaximumSize(new Dimension(30, 30));
        t.setFont(new Font("Times New Roman", Font.BOLD, 24));
        innerEast.add(to);
        innerEast.add(t);
        north.add(innerEast, BorderLayout.EAST);

        add(north, BorderLayout.NORTH);

        ImageIcon pic1 = new ImageIcon("metromap.jpg");
        JLabel pic = new JLabel(pic1);
        add(pic, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new BorderLayout());
        search = new JButton("Search");
        search.setFont(new Font("Times New Roman", Font.BOLD, 16));
        search.addActionListener(new Listener());
        JButton reset = new JButton("Reset");
        reset.setFont(new Font("Times New Roman", Font.BOLD, 16));
        reset.addActionListener(new Listener());
        path = new JLabel("Path: ");
        path.setFont(new Font("Times New Roman", Font.BOLD, 20));
        south.add(search, BorderLayout.WEST);
        south.add(reset, BorderLayout.EAST);
        south.add(path, BorderLayout.CENTER);
        path.setHorizontalAlignment(SwingConstants.CENTER);
        add(south, BorderLayout.SOUTH);

    }

    //initializes the graph
    private void fillGraph()
    {
        JsonReader read;
        SequenceJson sequence = null;
        int i = 0, j = 0, k = 0, l = 0;
        //looping over all the color lines
        while (i < colors.length)
        {
            read = new JsonReader("https://api.wmata.com/Rail.svc/json/jStations", colors[i]);
            JSONObject obj = new JSONObject(read.getJSON());
            JSONArray stations = obj.getJSONArray("Stations");
          //  System.out.println(stations);
            //looping through each station of a given line
            while (j < stations.length())
            {
                JSONObject item = stations.getJSONObject(j);
                String name = item.getString("Name");

                //if the station is already in the list, update its colors to add the secondary, tertiary, etc. colors
                if(hasStation(name))
                {
                    if( getStation(name).getColors().size() == 1)
                        getStation(name).getColors().add(item.getString("LineCode1"));
                    else if(getStation(name).getColors().size() == 2)
                        getStation(name).getColors().add(item.getString("LineCode2"));
                    else
                        getStation(name).getColors().add(item.getString("LineCode3"));
                }

                //otherwise, add a new station to the list of stations and get all the necessary attributes from the JSON file
                else
                {
                    ArrayList<String> lines = new ArrayList<>();
                    lines.add(item.getString("LineCode1"));
                    vertices.add(new Station(item.getString("Code"), name, lines));
                }
                j++;
            }
            j = 0;

            //gets the list that contains both the starting and ending code on the current line

            CodeJson codes = new CodeJson("https://api.wmata.com/Rail.svc/json/jLines");
            JSONObject object = new JSONObject(codes.getJSON());
            JSONArray lines = object.getJSONArray("Lines");
            String startCode = "", endCode = "";
            for(int temp = 0; temp < lines.length(); temp++)
            {
                if(lines.getJSONObject(temp).getString("LineCode").equals(colors[i]))
                {
                    startCode = lines.getJSONObject(temp).getString("StartStationCode");
                    endCode = lines.getJSONObject(temp).getString("EndStationCode");
                }
            }

            //makes a SequenceJson object using the confirmed starting and ending station codes
            sequence = new SequenceJson("https://api.wmata.com/Rail.svc/json/jPath", startCode, endCode);
            JSONObject order = new JSONObject(sequence.getJSON());
            JSONArray finalPath = order.getJSONArray("Path");

            //calls the method to set the neighbors of each station thats in vertices
            setNeighbors(0, finalPath);

            i++;
        }

    }

    private void setNeighbors(int l, JSONArray finalPath)
    {
        //sets the neighbors of each station in the list
        while(l < finalPath.length())
        {
            Station current =  getStation(finalPath.getJSONObject(l).getString("StationName"));
            //if it's the "start" it only has a neighbor in one direction
            if(l == 0)
            {
                Station next = getStation(finalPath.getJSONObject(l+1).getString("StationName"));
                current.addNeighbor(next, actualTime(current, next));
            }
            //if it's the "end" it only has a neighbor in one direction
            else if(l == finalPath.length()-1)
            {
                Station previous = getStation(finalPath.getJSONObject(l-1).getString("StationName"));
                current.addNeighbor(previous, actualTime(current, previous));
            }
            else
            {
                Station next = getStation(finalPath.getJSONObject(l+1).getString("StationName"));
                Station previous = getStation(finalPath.getJSONObject(l-1).getString("StationName"));
                current.addNeighbor(next, actualTime(current, next));
                current.addNeighbor(previous, actualTime(current, previous));
            }
            l++;
        }
    }

    //checks if the list of stations contains the station with the given name
    private boolean hasStation(String name)
    {
        for(Station s : vertices)
        {
            if(s.getName().equals(name))
                return true;
        }
        return false;
    }

    //returns the station with the same name as the input
    private Station getStation(String name)
    {
        for(Station s: vertices)
        {
            if(s.getName().equals(name))
                return s;
        }
        return null;
    }

    //prints out all the stations in vertices
    private void printStations()
    {
        for(Station s: vertices)
            System.out.print(s.getName() + " ");
        System.out.println();
    }

    private List<String> path()
    {
        //test statement to make sure that all the stations have been added to the graph
        printStations();
        Station startNode = getStation(start);
        Station endNode = getStation(end);

        //local variables for A*
        HashMap<Station,Station> parentMap = new HashMap<Station, Station>();
        HashSet<Station> visited = new HashSet<Station>();
        Map<Station, Double> distances = initializeAllToInfinity();
        Queue<Station> priorityQueue = new PriorityQueue<>();

        //enqueue StartNode, with time 0
        startNode.setTimeToStart(0);
        distances.put(startNode,0.0);
        priorityQueue.add(startNode);
        Station current = null;

        while (!priorityQueue.isEmpty())
        {
            current = priorityQueue.remove();

            if (!visited.contains(current) )
            {
                visited.add(current);
                //if the endNode is reached
                if (current.getName().equals(endNode.getName()))
                    return reconstructPath(startNode, endNode, parentMap);

                Set<Station> neighbors = current.getNeighbors().keySet();
                for (Station neighbor : neighbors)
                {
                    if (!visited.contains(neighbor) )
                    {

                        //calculate predicted time to the end node
                        //using a predicted time is what makes A* different from Dijkstra's algorithm
                        double predictedTime = predictTime(neighbor, endNode);
                        System.out.println(predictedTime);
                        //calculate time to neighbor
                        double neighborTime = current.getNeighbors().get(neighbor);

                        //calculate the time to the startNode
                        double totalTime = current.getTimeToStart() + neighborTime + predictedTime;

                        // check if time smaller
                        if(totalTime < distances.get(neighbor) )
                        {
                            // update neighbor's time
                            distances.put(neighbor, totalTime);
                            //used for PriorityQueue, to compare the stations
                            neighbor.setTimeToStart(totalTime);
                            neighbor.setPredictedTime(predictedTime);
                            //set parent, for getting the final path output
                            parentMap.put(neighbor, current);
                            //enqueue
                            priorityQueue.add(neighbor);
                        }
                    }
                }
            }
        }
        throw new RuntimeException(("Cannot find path!"));
        //return null;
    }

    private Map<Station, Double> initializeAllToInfinity()
    {
        //initializes the distance from every station to start as the maximum value possible
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
        // construct final path list
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
        //time prediction is based on distance and average speed of the trains
        DistanceTimeJson distance = new DistanceTimeJson("https://api.wmata.com/Rail.svc/json/jSrcStationToDstStationInfo", start.getStationCode(), end.getStationCode());
        JSONObject obj = new JSONObject(distance.getJSON());
        try
        {
            JSONArray item = obj.getJSONArray("StationToStationInfos");
            JSONObject mile = item.getJSONObject(0);
            double miles = mile.getDouble("CompositeMiles");
            return miles / 33 * 60;
        }
        catch(org.json.JSONException e)
        {
           // System.out.println(e.getMessage());
            return -10;
        }
    }

    private int actualTime(Station start, Station end)
    {
        //actual time is based on the data the API provides us for the time it takes to go from startStation to endStation
        DistanceTimeJson time = new DistanceTimeJson("https://api.wmata.com/Rail.svc/json/jSrcStationToDstStationInfo", start.getStationCode(), end.getStationCode());
        JSONObject obj = new JSONObject(time.getJSON());
        try
        {
            JSONArray item = obj.getJSONArray("StationToStationInfos");
            JSONObject rail = item.getJSONObject(0);
            return rail.getInt("RailTime");
        }
        catch(org.json.JSONException e)
        {
         //   System.out.println(e.getMessage());
            return -10;
        }
    }

    private class Listener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            //the path search button was clicked
            if (e.getSource() == search)
            {
                //makes sure that both the start and end station have been selected
                if(f.getSelectedItem().equals("") ||t.getSelectedItem().equals(""))
                    path.setText("Invalid station entered. Please check the start and end stations.");
                else
                {
                    start = (String)f.getSelectedItem();
                    end = (String)t.getSelectedItem();
                    String p = "";
                    List<String> list = path();
                    if(list!= null)
                    {
                        for (int i = 0; i < list.size(); i++) {
                            p += list.get(i) + " ";
                        }
                        path.setText(p);
                    }
                    else
                        path.setText("No path available");
                }
            }
            //the reset button was clicked
            else
            {
              //  f.setText("");
                path.setText("Path: ");
               // t.setText("");
            }
        }
    }
}


