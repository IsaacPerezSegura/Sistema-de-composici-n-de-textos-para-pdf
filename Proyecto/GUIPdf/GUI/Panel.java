package GUI;
import Etiquetas.Etiquetas;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import com.sun.pdfview.PagePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Scanner;
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
    private JButton guardar,guardarComo,abrir,siguiente,anterior;
    private final JPanel menu;
    private String nombre="";
    private String [] nRutanombre;
    private MensajeEmergente me;
    //variable "dialogo" que nos ayuda navegar para abrir/guardar archivos
    private JFileChooser dialogo;
    //Variable que guarda la ruta donde un archivo se guarda
    private File ruta,rutaT;
    /*Cuando se escribe una serie de informacion con etiquetado para generar un PDF en la aplicacion
    lo unico que se guarda es el PDF mas no la serie de informacion con etiquetado, si se cierra toda esta informacion
    se pierde, por lo que es conveniente poder guardarlo al igual que el archivo PDF para poder editar el contenido
    del PDF, con esta variable FileWriter crearemos un archivo txt el cual contendra toda la informacion de etiquetado con el que se genera el PDF*/
    private FileWriter aEscribir;
    private Scanner leeTxt;
    /*Visualiza*/
    private PagePanel panelpdf;
    private PDFFile pdffile;
    private int indice = 0,tamano;
    //Construccion
    public Panel(){
        BorderLayout layout=new BorderLayout(10,10);
        this.setLayout(layout);
        //Construccion del panel que contiene el JTextArea
        areaTrabajo=new JPanel();
        areaTrabajo.setLayout(new GridLayout(1,1));
        //Panel que contiene botones de abrir y guardar
        menu=new JPanel();
        menu.setLayout(new FlowLayout());
        menu.setBackground(new Color(78,117,209));
        guardar=new JButton("Guardar");
        guardar.addActionListener(this);
        guardarComo=new JButton("Guardar como...");
        guardarComo.setEnabled(false);
        guardarComo.addActionListener(this);
        this.add(menu,BorderLayout.PAGE_END);
        this.setBackground(Color.WHITE);
    }
    //Metodo que crea una nueva area de trabajo (Se implementa en clase: Marco)
    public void nuevo(){
        abrir=new JButton("Abrir");
        abrir.addActionListener(this);
        hoja=new JTextArea();
        scroll=new JScrollPane(hoja,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        hoja.setLineWrap(true);
        hoja.setWrapStyleWord(true);
        hoja.setFont(new Font("Arial",Font.BOLD,15));
    }
    //Metodo que abre archivos existentes PDF (Se implementa en esta misma clase y clase Marco).
    public String abrir(){
        //Variable "dialogo" nos ayuda a realizar una seleccion de archivo.
        dialogo=new JFileChooser();
        //Variable "nombre" nos ayuda a publicar en el titlo del JInternalFrame
        //Si se realizo una seleccion...
        if(dialogo.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            //Se crea un mensaje que notifica al usuario sobr el proceso y finaliza automaticamente
            MensajeEmergente abrirm=new MensajeEmergente("Abriendo...");
            abrirm.setAlwaysOnTop(true);
            ruta=dialogo.getSelectedFile();
        try{
            //Si se reconoce extension PDF
            if(String.valueOf(ruta).endsWith(".pdf")){
                if(abrir!=null){
                    remove(areaTrabajo);
                    menu.removeAll();
                }
                abrirPdf();
                nombre=dialogo.getSelectedFile().getName();
                
                validate();
            }else //Si se reconoce extension TXT
               if (String.valueOf(ruta).endsWith(".txt")){
                   if(panelpdf!=null){
                       remove(panelpdf);
                       menu.removeAll();
                   }
                    abrirTxt();
                    nombre=dialogo.getSelectedFile().getName();
                    validate();
          }
            guardarComo.setEnabled(true);
        }catch(Exception e){
          abrirm.dispose();
            JOptionPane.showMessageDialog(null, e);
        }
          abrirm.dispose();
        }
       return nombre;
    }
    public void abrirPdf(){
        if(panelpdf==null){
            abrir=new JButton("Abrir");
            abrir.addActionListener(this);
            siguiente = new JButton("Siguiente");
            siguiente.addActionListener(this);
            anterior = new JButton("Anterior");
            anterior.addActionListener(this);
            panelpdf = new PagePanel();
        }
        menu.add(abrir);
        menu.add(anterior);
        menu.add(siguiente);
        try{
        File file = dialogo.getSelectedFile();
        file.createNewFile();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY,0, channel.size());
        pdffile = new PDFFile(buf);
        tamano=pdffile.getNumPages();
        PDFPage page = pdffile.getPage(indice++);
        panelpdf.showPage(page);
        panelpdf.repaint();
        add(panelpdf);
        }catch(Exception e){}
    }
    public void abrirTxt(){
        if(hoja==null){
         nuevo();
        }
        menu.add(guardar);
        menu.add(abrir);
        menu.add(guardarComo);
        areaTrabajo.add(scroll);
        add(areaTrabajo,BorderLayout.CENTER);
        repaint();
        hoja.setText("");
        if(ruta!=null){
        try{
          leeTxt=new Scanner(new FileInputStream(ruta),"latin1");
          while(leeTxt.hasNext()){
               hoja.setText(hoja.getText()+""+leeTxt.nextLine()+"\n");
          }
        }catch(Exception e){}
        }
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
           nRutanombre=String.valueOf(ruta).split(".txt");
           //Se implementa la clase Etiquetas para poder darle forma al texto
           guardarContenido(String.valueOf(hoja.getText()));
           Etiquetas guardar=new Etiquetas(String.valueOf(hoja.getText()),ruta);
           me.setAlwaysOnTop(true);
           guardar.etiquetas();
           guardarComo.setEnabled(true);
           me.dispose();
           JOptionPane.showMessageDialog(null,"Documento Guardado");
           }else{
               me.dispose();
           }
       }else if(ruta!=null){
           guardarContenido(String.valueOf(hoja.getText()));
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
    //metodo que guarda en archivo Txt la etiquetas redactadas, para asi brindar la posibilidad de seguir editando el documento
    public void guardarContenido(String texto){
        try{
        aEscribir =new FileWriter(nRutanombre[0]+".txt",false);
        aEscribir.write(texto);
        aEscribir.close();
        }catch(Exception e){}
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
        }else if(event.getSource()==siguiente){
            if(indice<=tamano){
                indice=indice+1;
                PDFPage page = pdffile.getPage(indice);
                panelpdf.showPage(page);
            }
        }else if(event.getSource()==anterior){
            if(indice>=tamano){
                indice=indice-1;
                PDFPage page = pdffile.getPage(indice);
                panelpdf.showPage(page);
            }
        }
    }
}
