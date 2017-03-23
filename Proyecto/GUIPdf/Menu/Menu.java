package Menu;
import GUI.Marco;
import java.awt.Dimension;
import javax.swing.JFrame;
//Clase main, ejecuta el Frame principal
public class Menu {
    public static void main(String [] args){
        Marco gui=new Marco();
        gui.setTitle("PDF view");
        gui.pack();
        gui.setMinimumSize(new Dimension(600,600));
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true);
        gui.setLocationRelativeTo(null);
    }
}
