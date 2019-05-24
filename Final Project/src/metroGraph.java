import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.security.acl.Group;
import java.util.*;
import java.util.List;

import org.json.*;


public class MetroGraph extends JPanel
{
    private List<Station> vertices = new ArrayList<Station>();
    private String start;
    private String end;
    private final String[] colors = {"RD","YL", "GR", "BL", "OR", "SV"};
    private JPanel p1;
    private JPanel p2;
    private JLabel path;
    private JLabel from;
    private JLabel to;
    private JComboBox<String> f;
    private JComboBox<String> t;
    private JButton search;
    private JButton reset;
    //I moved all the Jbuttons and labels that my IDE said "could be converted to local variables" to the constructor

    public MetroGraph()
    {
        //initializes the graph with all the data on the stations from wmata API
        fillGraph();
        String [] verticesNames = new String [vertices.size()];
        int index = 0;
        for(Station s: vertices)
            verticesNames[index++] = s.getName();

        /*// creates the GUI aspect of this project
        JPanel panel = new JPanel();
        p1 = new JPanel();
        p2 = new JPanel();
        GroupLayout g = new GroupLayout(panel);
        g.setAutoCreateGaps(true);
        g.setAutoCreateContainerGaps(true);
        GroupLayout g1 = new GroupLayout(p1);
        //g1.setAutoCreateGaps(true);
        //g1.setAutoCreateContainerGaps(true);
        GroupLayout g2 = new GroupLayout(p2);
        //g2.setAutoCreateGaps(true);
        //g2.setAutoCreateContainerGaps(true);
        panel.setLayout(g);
        p1.setLayout(g1);
        p2.setLayout(g2);

        // initializes all the GUI components
        JLabel title = new JLabel("Metro Map");
        title.setFont(new Font("Times New Roman", Font.BOLD, 50));
        ImageIcon pic1 = new ImageIcon("metromap");
        JLabel pic = new JLabel(pic1);
        Font f1 = new Font("Times New Roman", Font.BOLD, 24);
        JLabel from = new JLabel("Start: ");
        from.setFont(f1);
        f = new JComboBox<String>(verticesNames);
        f.setMaximumSize(new Dimension(30, 30));
        f.setFont(f1);
        JLabel to = new JLabel("End: ");
        to.setFont(f1);
        t = new JComboBox<String>(verticesNames);
        t.setMaximumSize(new Dimension(30, 30));
        t.setFont(f1);
        search = new JButton("Search");
        search.setFont(f1);
        search.addActionListener(new Listener());
        path = new JLabel("");
        path.setFont(f1);
        reset = new JButton("Reset");
        reset.setFont(f1);
        reset.addActionListener(new Listener());

        // creates the first inner panel
        g1.setHorizontalGroup(g1.createSequentialGroup()
            .addGroup(g1.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(from)
                .addComponent(to))
            .addGroup(g1.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(f)
                .addComponent(t))
            .addGroup(g1.createParallelGroup(GroupLayout.Alignment.CENTER)));
        g1.setVerticalGroup(g1.createSequentialGroup()
            .addGroup(g1.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(from)
                .addComponent(f))
            .addGroup(g1.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(to)
                .addComponent(t))
            .addComponent(search));

        // creates second inner panel
        g2.setHorizontalGroup(g1.createSequentialGroup()
            .addGroup(g2.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(path)
                .addComponent(reset)));
        g2.setVerticalGroup(g1.createSequentialGroup()
            .addComponent(path)
            .addComponent(reset));

        // creates overall panel
        g.setHorizontalGroup(g.createSequentialGroup()
            .addGroup(g.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(title)
                .addComponent(pic)
                .addComponent(p1)
                .addComponent(p2)));
        g.setVerticalGroup(g.createSequentialGroup()
            .addComponent(title)
            .addComponent(pic)
            .addComponent(p1)
            .addComponent(p2));

        // sets up start screen
        p2.setVisible(false);
        add(panel);
        */
        // creates the GUI aspect of this project
        p1 = new JPanel();
        GroupLayout layout = new GroupLayout(p1);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        p1.setLayout(layout);
        p2 = new JPanel();
        GroupLayout layout1 = new GroupLayout(p2);
        layout1.setAutoCreateGaps(true);
        layout1.setAutoCreateContainerGaps(true);
        p2.setLayout(layout1);

        // initializes all the GUI components
        JLabel title = new JLabel("Metro Map");
        title.setFont(new Font("Times New Roman", Font.BOLD, 50));
        JLabel title1 = new JLabel("Metro Map");
        title1.setFont(new Font("Times New Roman", Font.BOLD, 50));
        ImageIcon p = new ImageIcon("metromap.jpg");
        JLabel pic = new JLabel(p);
        JLabel pic1 = new JLabel(p);
        Font f1 = new Font("Times New Roman", Font.BOLD, 24);
        from = new JLabel("Start: ");
        from.setFont(f1);
        f = new JComboBox<String>(verticesNames);
        f.setMaximumSize(new Dimension(30, 30));
        f.setFont(f1);
        to  = new JLabel("End: ");
        to.setFont(f1);
        t = new JComboBox<String>(verticesNames);
        t.setMaximumSize(new Dimension(30, 30));
        t.setFont(f1);
        search = new JButton("Search");
        search.setFont(f1);
        search.addActionListener(new Listener());
        path = new JLabel("");
        path.setFont(f1);
        //path.setMaximumSize(new Dimension(1500, 100));
        reset = new JButton("Reset");
        reset.setFont(f1);
        reset.addActionListener(new Listener());

        // adds components to the first panel
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(title)
                .addComponent(pic)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(from)
                        .addComponent(to))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(f)
                        .addComponent(t)))
                .addComponent(search)));
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(title)
            .addComponent(pic)
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(from)
                .addComponent(f))
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(to)
                .addComponent(t))
            .addComponent(search));

        // adds components to the second panel
        layout1.setHorizontalGroup(layout1.createSequentialGroup()
            .addGroup(layout1.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(title1)
                .addComponent(pic1)
                .addComponent(path)
                .addComponent(reset)));
        layout1.setVerticalGroup(layout1.createSequentialGroup()
            .addComponent(title1)
            .addComponent(pic1)
            .addComponent(path)
            .addComponent(reset));

        // adds the first panel to the frame
        add(p1);
        add(p2);
        p2.setVisible(false);

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

            CodeJson codes = new CodeJson("https://api.wmata.com/Rail.svc/json/jLines");
            JSONObject object = new JSONObject(codes.getJSON());
            JSONArray lines = object.getJSONArray("Lines");
            String startCode = "", endCode = "";

            //gets the starting and ending code on the current line
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
            setNeighbors(finalPath);

            i++;
        }

    }

    private void setNeighbors(JSONArray finalPath)
    {
        //sets the neighbors of each station in the list
        int l = 0;
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
            if (e.getSource() == search)
            {
                start = (String) f.getSelectedItem();
                end = (String) t.getSelectedItem();
                String p = "";
                List<String> list = path();
                if (list != null)
                {

                    for (int i = 0; i < list.size(); i++)
                    {
                        p += list.get(i) + " ";
                        if (i%5 == 0)
                            p += '\n';
                    }
                    path.setText(p);
                }
                else
                    path.setText("No path available");
                p1.setVisible(false);
                p2.setVisible(true);
            }
            else
            {
                p2.setVisible(false);
                p1.setVisible(true);
            }

            /*//the path search button was clicked
            if (e.getSource() == search)
            {
                ///*
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
                path.setText("Path: ");
            }
            */
        }
    }
}


