import javax.swing.*;

import static java.awt.Component.CENTER_ALIGNMENT;

public class Main
{
    public static void main(String [] args)
    {
        MetroGraph m = new MetroGraph();

        JFrame frame = new JFrame("Metro routes");
        frame.setSize(1500,1000);
        frame.setLocation((int)CENTER_ALIGNMENT, (int)CENTER_ALIGNMENT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(m);
        frame.setVisible(true);
    }
}
