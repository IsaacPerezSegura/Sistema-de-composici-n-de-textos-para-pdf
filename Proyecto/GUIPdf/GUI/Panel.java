package GUI;
import Etiquetas.Etiquetas;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
public class Panel extends JPanel implements ActionListener{
    //Panel que contiene Scrolls
    private final JPanel areaTrabajo;
    //Area de escritura
    private JTextArea hoja;
    private JScrollPane scroll;
    private final JButton guardar,guardarComo,abrir;
    private final JPanel menu;
    private MensajeEmergente me;
    //variable "dialogo" que nos ayuda navegar para abrir/guardar archivos
    private JFileChooser dialogo;
    //Variable que guarda la ruta donde un archivo se guarda
    //private String ruta;
    private File ruta,rutaT;
    //Construccion
    public Panel(){
        BorderLayout layout=new BorderLayout(10,10);
        this.setLayout(layout);
        //Construccion del panel que contiene el JTextArea
        areaTrabajo=new JPanel();
        areaTrabajo.setLayout(new GridLayout(1,1));
        this.add(areaTrabajo,BorderLayout.CENTER);
        //Panel que contiene botones de abrir y guardar
        menu=new JPanel();
        menu.setLayout(new FlowLayout());
        menu.setBackground(new Color(78,117,209));
        guardar=new JButton("Guardar");
        guardar.addActionListener(this);
        abrir=new JButton("Abrir");
        abrir.addActionListener(this);
        guardarComo=new JButton("Guardar como...");
        guardarComo.setEnabled(false);
        guardarComo.addActionListener(this);
        menu.add(guardar);
        menu.add(abrir);
        menu.add(guardarComo);
        this.add(menu,BorderLayout.PAGE_END);
        this.setBackground(Color.WHITE);
    }
    //Metodo que crea una nueva area de trabajo (Se implementa en clase: Marco)
    public void nuevo(){
    hoja=new JTextArea();
    scroll=new JScrollPane(hoja,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    hoja.setLineWrap(true);
    hoja.setWrapStyleWord(true);
    hoja.setFont(new Font("Arial",Font.BOLD,15));
    areaTrabajo.add(scroll);
    }
    //Metodo que abre archivos existentes PDF (Se implementa en esta misma clase y clase Marco).
    public String abrir(){
        //Variable "nombre" nos ayuda a publicar en el titlo del JInternalFrame
        String nombre="";
        //Variable "dialogo" nos ayuda a realizar una seleccion de archivo.
        dialogo=new JFileChooser();
        //Si se realizo una seleccion...
        if(dialogo.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            //Se crea un mensaje que notifica al usuario sobr el proceso y finaliza automaticamente
            MensajeEmergente abrirm=new MensajeEmergente("Abriendo...");
            abrirm.setAlwaysOnTop(true);
        try{
           /*Variable "texto" contiene todo el texto dentro del PDF
            Implementando "PdfTextExtractor" y "PdfReader" en la libreria iText
            */
           String texto=PdfTextExtractor.getTextFromPage(new PdfReader(dialogo.getSelectedFile().toString()),1);
           //Se publica en el JTextArea
           hoja.setText(String.valueOf(texto));
           nombre=dialogo.getSelectedFile().getName();
        }catch(Exception e){
          abrirm.dispose();
            JOptionPane.showMessageDialog(null, e);
        }
          abrirm.dispose();
        }
       return nombre;
    }
    //Metodo que guarda el area de trabajo a archivo PDF(Se implementa en esta misma clase y clase Marco).
    public void guardar(){
       try{
       dialogo=new JFileChooser();
       /*Se inicia nuevamente variable "dialogo" para realizar
       una seleccion de ruta, donde sera almacenado nuestro archivo*/
       me=new MensajeEmergente("Guardando...");
       /*Estas condicionales nos ayudan a saber si ya se establecio un 
       directorio, de ser asi no es necesario solicitar el showsaveDialog*/
       if(ruta==null){
           if(dialogo.showSaveDialog(this)==JFileChooser.APPROVE_OPTION ){
           ruta=dialogo.getSelectedFile();
           //Se implementa la clase Etiquetas para poder darle forma al texto
           Etiquetas guardar=new Etiquetas(String.valueOf(hoja.getText()),ruta);
           me.setAlwaysOnTop(true);
           guardar.etiquetas();
           guardarComo.setEnabled(true);
           me.dispose();
           JOptionPane.showMessageDialog(null,"Documento Guardado");
           }else{
               ruta=rutaT;
               me.dispose();
           }
       }else if(ruta!=null){
           Etiquetas guardar=new Etiquetas(String.valueOf(hoja.getText()),ruta);
           guardar.etiquetas();
           me.dispose();
           JOptionPane.showMessageDialog(null,"Documento Guardado");
       }
       }catch(Exception e){
           JOptionPane.showMessageDialog(null,e);
           me.dispose();
       }
}
    //Metodo escucha para botones
    @Override
    public void actionPerformed(ActionEvent event){
        if(event.getSource()==guardar){
           guardar(); 
        }else if(event.getSource()==abrir){
           abrir(); 
        }else if(event.getSource()==guardarComo){
            rutaT=ruta;
            ruta=null;
            guardar();
        }
    }
}
