import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import org.json.*;


public class metroGraph extends JPanel
{
    private List<Station> vertices = new ArrayList<Station>();
    private String start;
    private String end;
    private final double speed = 33;
    private final String[] colors = {"RD","YL", "GR", "BL", "OR", "SV"};
    private JLabel title;
    private JLabel pic;
    private JLabel from;
    private JLabel to;
    private JLabel path;
    private JTextField f;
    private JTextField t;
    private ImageIcon pic1;
    private JButton search;
    private JButton reset;

    public metroGraph()
    {
        init();

        setLayout(new BorderLayout());

        pic1 = new ImageIcon("metromap.jpg");
        pic = new JLabel(pic1);
        add(pic, BorderLayout.CENTER);

        title = new JLabel("Metro Map");
        title.setFont(new Font("Times New Roman", Font.BOLD, 50));
        add(title, BorderLayout.NORTH);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel west = new JPanel();
        west.setLayout(new FlowLayout());
        from = new JLabel("Start: ");
        from.setFont(new Font("Times New Roman", Font.BOLD, 24));
        f = new JTextField(10);
        f.setFont(new Font("Times New Roman", Font.BOLD, 24));
        west.add(from);
        west.add(f);
        add(west, BorderLayout.WEST);

        JPanel east = new JPanel();
        east.setLayout(new FlowLayout());
        to = new JLabel("End: ");
        to.setFont(new Font("Times New Roman", Font.BOLD, 24));
        t = new JTextField(10);
        t.setFont(new Font("Times New Roman", Font.BOLD, 24));
        east.add(to);
        east.add(t);
        add(east, BorderLayout.EAST);

        JPanel south = new JPanel();
        south.setLayout(new BorderLayout());
        search = new JButton("Search");
        search.setFont(new Font("Times New Roman", Font.BOLD, 16));
        search.addActionListener(new Listener());
        reset = new JButton("Reset");
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

    public void init() //initializes the graph
    {
        JsonReader read;
        List<Station> temp = new ArrayList<Station>();
        int i = 0, j = 0;
        while (i < colors.length)
        {
            read = new JsonReader("https://api.wmata.com/Rail.svc/json/jStations", colors[i]);
            JSONObject obj = new JSONObject(read.getJSON());
            JSONArray stations = obj.getJSONArray("Stations");
            while (j < stations.length())
            {
                JSONObject item = stations.getJSONObject(j);
                String name = item.getString("Name");
           /*     if(hasStation(name))
                {
                    getStation(name).getColors().add(item.getString("LineCode1"));
                   // getStation(name).getNeighbors().
                }
                else
                {*/
                    ArrayList<String> lines = new ArrayList<>();
                    lines.add(item.getString("LineCode1"));
                    temp.add(new Station(item.getString("Code"), name, lines));
                    j++;
              //  }
            }
            i++;
        }
            for(int h = 0; h < temp.size()-1; h++)
            {
                if(h == 0) {
                    temp.get(h).addNeighbor(temp.get(h+1), actualTime(temp.get(h), temp.get(h + 1)));
                }
                else if(h == temp.size() - 1)
                {
                    temp.get(h).addNeighbor(temp.get(h-1), actualTime(temp.get(h), temp.get(h-1)));
                    temp.get(h).addNeighbor(temp.get(h+1), actualTime(temp.get(h), temp.get(h + 1)));
                }
                else
                {
                    temp.get(h).addNeighbor(temp.get(h-1), actualTime(temp.get(h), temp.get(h - 1)));
                }
            }

           boolean flag = false;
           for(int k = 0; k < temp.size(); k++)
           {
               for(int m = 0; m < vertices.size(); m++)
               {
                   if (vertices.get(m).getName().equals(temp.get(k).getName()))
                   {
                       vertices.get(m).addNeighbors(temp.get(k).getNeighbors());
                       flag = true;
                       break;
                   }
               }
               if (!flag)
                   vertices.add(temp.get(k));
               flag = false;
           }

        }
        //JsonReader read = new JsonReader("https://api.wmata.com/Rail.svc/json/jLines");

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

    public void printStations()
    {
        for(Station s: vertices)
            System.out.print(s.getName() + " ");
    }
    public List<String> path()
    {
        printStations();
        Station startNode = getStation(start);
        System.out.println(start);
        System.out.println(startNode.getName());
        Station endNode = getStation(end);
        System.out.println(end);
        System.out.println(endNode.getName());

        // setup for A*
        HashMap<Station,Station> parentMap = new HashMap<Station, Station>();
        HashSet<Station> visited = new HashSet<Station>();
        Map<Station, Double> distances = initializeAllToInfinity();

        Queue<Station> priorityQueue = new PriorityQueue<>();

        //  enque StartNode, with time 0
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
        DistanceTimeJson distance = new DistanceTimeJson("https://api.wmata.com/Rail.svc/json/jSrcStationToDstStationInfo", start.getStationCode(), end.getStationCode());
        JSONObject obj = new JSONObject(distance.getJSON());
       try {
           JSONArray item = obj.getJSONArray("StationToStationInfos");
           JSONObject mile = item.getJSONObject(0);
           double miles = mile.getDouble("CompositeMiles");
           return miles / speed * 60;
       }
       catch(org.json.JSONException e)
       {
           System.out.println("this is the one that failed");
           return -10;
       }
    }

    private int actualTime(Station start, Station end)
    {
        DistanceTimeJson time = new DistanceTimeJson("https://api.wmata.com/Rail.svc/json/jSrcStationToDstStationInfo", start.getStationCode(), end.getStationCode());
        JSONObject obj = new JSONObject(time.getJSON());
        try {
            JSONArray item = obj.getJSONArray("StationToStationInfos");
            JSONObject rail = item.getJSONObject(0);
            return rail.getInt("RailTime");
        }
        catch(org.json.JSONException e)
        {
            System.out.println("this is the one that failed");
            return -10;
        }
    }

    private class Listener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == search)
            {
                if(f.getText().equals("") || t.getText().equals(""))
                    path.setText("Invalid station entered. Please check the start and end stations.");
                else
                {
                    start = f.getText();
                    end = t.getText();
                    String p = "";
                    List<String> list = path();
                    for(int i = 0; i < list.size(); i++)
                    {
                        p+=list.get(i) + " ";
                    }
                    path.setText(p);
                }
            }
            else
            {
                f.setText("");
                path.setText("Path: ");
                t.setText("");
            }
        }
    }
}


