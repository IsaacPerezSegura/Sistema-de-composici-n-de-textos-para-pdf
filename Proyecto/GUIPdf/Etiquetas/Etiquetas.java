package Etiquetas;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;

public class Etiquetas {
    //variable "texto" contiene todo el texto a etiquetar
    private final String texto;
    //variable "f" contiene el archivo seleccionado (una ruta y el archivo nuevo)
    private final File f;
    //variable "documento" representa el documento...
    private Document documento;
    //se construye la clase
    public Etiquetas(String hoja,File f){
        this.texto=hoja;
        this.f=f;
    }
    //Este metodo analiza el texto en busca de etiquetas
    public  void etiquetas(){
        try{
            
        FileOutputStream archivo=new FileOutputStream(f.toString()+".pdf");
        documento=new Document(PageSize.A4);
        PdfWriter.getInstance(documento, archivo);
        //se abre el documento para comenzar a escribir
        documento.open();
        //variable "caracter" nos ayuda a analizar el texto
        char caracter;
        //variable "valida" nos ayuda a saber cuando abre un etiqueta y cuando cierra
        boolean valida=false;
        //variable "ruta" nos ayuda a saber cuando se abre una etiqueta tipo imagen
        boolean ruta=false;
        //variable "residuo" contiene todo el texto que este fuera de una etiqueta
        String residuo="";
        //variable "aux" contiene todo el texto dentro de una etiqueta
        String aux="";
        //Ciclo for que recorre los caracteres para detectar etiquetas
        for(int i=0;i<texto.length();i++){
            caracter=texto.charAt(i);
            //Condicionales IF que nos ayudan a detectar la etiqueta
            if(caracter== '{'&& texto.charAt(i+1)=='T' && texto.charAt(i+2)=='}'){
               i=i+3;
                caracter=texto.charAt(i);
                valida=true;
            }else if(caracter== '{'&& texto.charAt(i+1)=='#' && texto.charAt(i+2)=='T'&& texto.charAt(i+3)=='}'){
                caracter=' ';
                i=i+3;
                aux=aux+caracter;
                Font fuente=new Font();
                fuente.setSize(25);
                fuente.setFamily(FontFamily.COURIER.toString());
                fuente.setStyle(Font.BOLD);
                fuente.setColor(BaseColor.RED);
                Paragraph titulos=new Paragraph(aux,fuente);
                titulos.setAlignment(Paragraph.ALIGN_CENTER);
                documento.add(titulos);
                aux="";
                valida=false;
            }if(caracter== '{'&& texto.charAt(i+1)=='C' && texto.charAt(i+2)=='}'){
               i=i+3;
                caracter=texto.charAt(i);
                valida=true;
            }else if(caracter== '{'&& texto.charAt(i+1)=='#' && texto.charAt(i+2)=='C'&& texto.charAt(i+3)=='}'){
                caracter=' ';
                i=i+3;
                aux=aux+caracter;
                Font fuente=new Font();
                fuente.setSize(15);
                fuente.setFamily(FontFamily.TIMES_ROMAN.toString());
                fuente.setStyle(Font.BOLD);
                Paragraph capitulo=new Paragraph("\n");
                capitulo.setFont(new Font(fuente));
                capitulo.add(aux);
                capitulo.setAlignment(Paragraph.ALIGN_LEFT);
                documento.add(capitulo);
                aux="";
                valida=false;
            }if(caracter== '{'&& texto.charAt(i+1)=='S' && texto.charAt(i+2)=='}'){
               i=i+3;
                caracter=texto.charAt(i);
                valida=true;
            }else if(caracter== '{'&& texto.charAt(i+1)=='#' && texto.charAt(i+2)=='S'&& texto.charAt(i+3)=='}'){
                caracter=' ';
                i=i+3;
                aux=aux+caracter;
                Font fuente=new Font();
                fuente.setSize(14);
                fuente.setFamily(FontFamily.TIMES_ROMAN.toString());
                Paragraph capitulo=new Paragraph(aux,fuente);
                capitulo.setAlignment(Paragraph.ALIGN_LEFT);
                documento.add(capitulo);
                aux="";
                valida=false;
            }else if(caracter== '{'&& texto.charAt(i+1)=='P' && texto.charAt(i+2)=='}'){
                i=i+3;
                caracter=texto.charAt(i);
                valida=true;
            }else if(caracter== '{'&& texto.charAt(i+1)=='#' && texto.charAt(i+2)=='P'&& texto.charAt(i+3)=='}'){
                caracter=' ';
                i=i+3;
                aux=aux+caracter;
                Paragraph parrafo=new Paragraph();
                parrafo.setAlignment(Paragraph.ALIGN_LEFT);
                /*Se llama al metodo "etiquetasForma" para analizar si el parrafo tiene dentro etiquetas que dan estilo
                y se regresa el residuo (texto que no contuvo estilo como negrita,subrayado etc)
                */
                aux=etiquetasForma(aux,parrafo);
                parrafo.setFont(new Font());
                parrafo.add(aux);
                documento.add(parrafo);
                aux="";
                valida=false;
            }else if(caracter== '{'&& texto.charAt(i+1)=='I' && texto.charAt(i+2)=='}'){
                i=i+3;
                caracter=texto.charAt(i);
                ruta=true;
            }else if(caracter== '{'&& texto.charAt(i+1)=='#' && texto.charAt(i+2)=='I'&& texto.charAt(i+3)=='}'){
                i=i+3;
                Paragraph imagenes=new Paragraph();
                Image imagen= Image.getInstance(aux);
                imagen.setAlignment(Image.ALIGN_CENTER);
                if(imagen.getScaledHeight()>=1000 && imagen.getScaledWidth()>=1000){
                    imagen.scaleAbsolute( imagen.getScaledWidth()/8,imagen.getScaledHeight()/8);
                }
                imagenes.add(imagen);
                documento.add(imagenes);
                aux="";
                ruta=false;
            }
            if(valida){
                aux=aux+caracter;
            }else{
                residuo=residuo+caracter;
            }
            if(ruta && caracter!=' '){
               aux=aux+caracter; 
            }
        }
        documento.close();
        }catch(Exception e){JOptionPane.showMessageDialog(null,e);}
    }
    //Metodo que analiza si una etiqueta principal contiene otras que dan estilo al texto (Mismo proceso de arriba)
    public String etiquetasForma(String texto, Paragraph secuencia) throws DocumentException{
        char caracter;
        boolean v=false;
        String residuo="";
        String aux="";
        for(int i=0;i<texto.length();i++){
            caracter=texto.charAt(i);
            if(caracter== '{'&& texto.charAt(i+1)=='b' && texto.charAt(i+2)=='}'){
               i=i+3;
                caracter=texto.charAt(i);
                secuencia.setFont(new Font());
                //Se añade el residuo que esta antes de la etiqueta
                secuencia.add(residuo);
                residuo="";
                v=true;
            }else if(caracter== '{'&& texto.charAt(i+1)=='#' && texto.charAt(i+2)=='b'&& texto.charAt(i+3)=='}'){
                caracter=' ';
                i=i+3;
                aux=aux+caracter;
                Font fuente=new Font();
                fuente.setStyle(Font.BOLD);
                secuencia.setFont(new Font(fuente));
                secuencia.add(aux);
                aux="";
                v=false;
            }else if(caracter== '{'&& texto.charAt(i+1)=='i' && texto.charAt(i+2)=='}'){
               i=i+3;
                caracter=texto.charAt(i);
                secuencia.setFont(new Font());
                secuencia.add(residuo);
                residuo="";
                v=true;
            }else if(caracter== '{'&& texto.charAt(i+1)=='#' && texto.charAt(i+2)=='i'&& texto.charAt(i+3)=='}'){
                caracter=' ';
                i=i+3;
                aux=aux+caracter;
                Font fuente=new Font();
                fuente.setStyle(Font.ITALIC);
                secuencia.setFont(new Font(fuente));
                secuencia.add(aux);
                aux="";
                v=false;
            }else if(caracter== '{'&& texto.charAt(i+1)=='u' && texto.charAt(i+2)=='}'){
               i=i+3;
                caracter=texto.charAt(i);
                secuencia.setFont(new Font());
                secuencia.add(residuo);
                residuo="";
                v=true;
            }else if(caracter== '{'&& texto.charAt(i+1)=='#' && texto.charAt(i+2)=='u'&& texto.charAt(i+3)=='}'){
                caracter=' ';
                i=i+3;
                aux=aux+caracter;
                Font fuente=new Font();
                fuente.setStyle(Font.UNDERLINE);
                secuencia.setFont(new Font(fuente));
                secuencia.add(aux);
                aux="";
                v=false;
            }else if(caracter== '{'&& texto.charAt(i+1)=='n' && texto.charAt(i+2)=='}'){
                i=i+2;
                caracter=' ';
                if(v){
                aux=aux+"\n ";
                }else {residuo=residuo+"\n ";}
            }
            if(v){
              aux=aux+caracter;  
            }else { residuo=residuo+caracter;}
        }
        //Se regresa el ultimo fragmento de texto que no estuvo dentro de ninguno etiqueta
        return residuo;
    }
}
