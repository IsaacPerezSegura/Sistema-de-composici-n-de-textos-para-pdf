package GUI;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
//Clase que crea mensajes emergentes para indicar que un proceso esta activo
public class MensajeEmergente extends JFrame{
    private final String m;
    private final JLabel mensaje;
  public  MensajeEmergente(String m){
      setUndecorated(true);
      toFront();
      setVisible(true);
      setLocationRelativeTo(null);
      setSize(200,100);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.m=m;
      mensaje=new JLabel(m);
      mensaje.setForeground(Color.red);
      mensaje.setFont(new Font("Arial",Font.BOLD,20));
      mensaje.setSize(200,70);
      add(mensaje);
  }
}
