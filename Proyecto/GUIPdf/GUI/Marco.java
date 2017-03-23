package GUI;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
//Clase Frame principal (Contiene frames internos)
public class Marco extends JFrame implements ActionListener{
   private final JDesktopPane paneInterno;
   private Panel [] panel=new Panel[50];
   private Panel abrir;
   private int cont=0,posicion,guardar;
   private boolean v=false;
   private final JMenuBar menu;
   private final JMenu subMenu,inf;
   private final JMenuItem [] items;
   private JInternalFrame frameInterno;
   private internalFrameListener frameListener;
   //construccion
public Marco(){ 
    //Construccion del JDesktopPane que contendra los JInternalFrame
paneInterno=new JDesktopPane();
paneInterno.setBackground(new Color(234,234,234));
add(paneInterno);
//Construccion de items para el JMenuBar
items=new JMenuItem [4];
items[0]=new JMenuItem("Nuevo");
items[0].setBackground(new Color(78,117,209));
items[0].setForeground(Color.white);
items[1]=new JMenuItem("Abrir");
items[1].setBackground(new Color(78,117,209));
items[1].setForeground(Color.white);
items[2]=new JMenuItem("Guardar");
items[2].setBackground(new Color(78,117,209));
items[2].setForeground(Color.white);
items[2].setEnabled(false);
items[0].addActionListener(this);
items[1].addActionListener(this);
items[2].addActionListener(this);
items[3]=new JMenuItem("Integrantes");
items[3].setBackground(new Color(78,117,209));
items[3].setForeground(Color.white);
items[3].addActionListener(this);
// Se anexan los items en el JMenu
subMenu=new JMenu("Archivo");
subMenu.setFont(new Font("Arial",Font.BOLD,15));
subMenu.setForeground(Color.white);
subMenu.add(items[0]);
subMenu.add(items[1]);
subMenu.add(items[2]);
//Construccion de JMenu donde alamacena un item que muestra informacion de los integrantes
inf=new JMenu("Info.");
inf.setFont(new Font("Arial",Font.BOLD,15));
inf.setForeground(Color.white);
inf.add(items[3]);
//Construccion del JMenuBar para añadir el JMenu y sus derivados (items)
menu =new JMenuBar();
menu.setBackground(new Color(78,117,209));
menu.add(subMenu);
menu.add(inf);
setJMenuBar(menu);

}
//Metodo escucha (Contiene acciones de creacion, guardado y abrir documento)
@Override
public void actionPerformed(ActionEvent event){   
frameListener=new internalFrameListener();
//Se crea nuevo documento
if(event.getSource()==items[0]){
    /*Condicionales que nos ayudan a mantener un orden entre JInternalFrame y el arreglo panel
    Ejemplo: el primer JInternalFrame creado se le añade setName(0) y el panel se contiene en el arreglo casilla 0*/
    if(posicion>0 || v==true){
     frameInterno=new JInternalFrame("Nuevo ",true,true,true,true);
     panel[posicion]= new Panel();
     panel[posicion].nuevo();
     frameInterno.setName(String.valueOf(posicion));
     añadirFrameInterno(panel[posicion]); 
     posicion=0;
     v=false;
    }else if(panel[cont]==null){
     frameInterno=new JInternalFrame("Nuevo ",true,true,true,true);
     panel[cont]= new Panel();
     panel[cont].nuevo();
     frameInterno.setName(String.valueOf(cont));
     añadirFrameInterno(panel[cont]);
    }
    cont=cont+1;
}else //Se abre un documento
    if(event.getSource()==items[1]){
        abrir=new Panel();
        String titulo;
        abrir.nuevo();
        titulo=abrir.abrir();
        if(!titulo.isEmpty()){
        frameInterno=new JInternalFrame(titulo,true,true,true,true);
        añadirFrameInterno(abrir);
        }
}else //Se guarda un documento (Solo se puede implementar si ya se ha creado o abierto alguno)
        if(event.getSource()==items[2]){
//Guardar documento mediante Item
    panel[guardar].guardar();
}else //Informacion de los integrantes
     if(event.getSource()==items[3]){
    JOptionPane.showMessageDialog(null, "Isaac Perez Segura\n" +
"Mauricio Vázquez Valdivia\n" +
"María Fernanda Escobedo Rodríguez");
}
}
//Metodo que añade JInternalFrame (Segun sea el caso, abrir o crear uno)
public void añadirFrameInterno(Panel p){
 frameInterno.addInternalFrameListener(frameListener);
 frameInterno.add(p);
 frameInterno.setSize(this.getWidth()-100,this.getHeight()-100);
 frameInterno.setVisible(true);
 paneInterno.add(frameInterno);
}
//Clase que contiene metodos escucha para JinternalFrame
private class internalFrameListener implements InternalFrameListener {

        @Override
        public void internalFrameOpened(InternalFrameEvent e) {
            items[2].setEnabled(true);
        }
        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
        }
        @Override
        /*Metodo que nos ayuda a identificar que panel debe desaparecer dentro del arreglo
        al momento de cerrar algun JInternalFrame (ya que no es necesario contenerlo si no esta abierto)*/
        public void internalFrameClosed(InternalFrameEvent e) {
            items[2].setEnabled(false);
            try{
            if(Integer.parseInt(e.getInternalFrame().getName())<(cont-1)){
            posicion=Integer.parseInt(e.getInternalFrame().getName());
            v=true;
            }
            cont=cont-1;
            }catch(Exception i){}
            //Se borra del arreglo el panel que contenia el JInternalFrame que se acaba de cerrar
            panel[posicion]=null;
        }
        @Override
        public void internalFrameIconified(InternalFrameEvent e) {
            items[2].setEnabled(false);
        }
        @Override
        public void internalFrameDeiconified(InternalFrameEvent e) {
            items[2].setEnabled(true);
        }
        @Override
        //Metodo que indica el Frame seleccionado, para poder guardar
        public void internalFrameActivated(InternalFrameEvent e) {
            items[2].setEnabled(true);
            try{
            /*se almacena el nombre del frame (valor numerico) en "guardar" para indicar
                con esta mismo para indicar en el arreglo de que panel provendra la informacion*/
            guardar=Integer.parseInt(e.getInternalFrame().getName());
            }catch(Exception i){}
        }
        @Override
        public void internalFrameDeactivated(InternalFrameEvent e) {
            items[2].setEnabled(false);
        }
}
}
