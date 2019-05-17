import javax.swing.*;

import static java.awt.Component.CENTER_ALIGNMENT;

public class Main
{
    public static void main(String [] args)
    {
        String start = JOptionPane.showInputDialog("Enter your start station");
        String end = JOptionPane.showInputDialog("Enter your end station");

        metroGraph m = new metroGraph(start, end);
     /*   JFrame frame = new JFrame();
        frame.setContentPane(m);
        frame.setSize(1500,1500);
        frame.setLocation((int)CENTER_ALIGNMENT, (int)CENTER_ALIGNMENT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
*/
    }
}
