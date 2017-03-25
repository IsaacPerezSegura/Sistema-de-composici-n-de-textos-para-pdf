Dentro de la carpeta "Muestra" se incluye un archivo PDF el cual fue generado
a base del archivo "prueba.txt" junto con las imagenes usadas para el mismo, con el fin
de mostrar la funcionalidad del programa.

Compilado en Windows
Linea de comando usados:

- Compilando 
GUIPdf> javac -d compilacion -cp lib\iText-5_0_5.jar;lib\PDFRenderer.jar;src s
rc\Menu\Menu.java

- Creando jar
compilacion> jar -cvfm GUIPDF.jar manifest.txt Etiquetas/*.class Menu/*
.class GUI/*.class

Donde Manifest:
Class-Path: lib/iText-5_0_5.jar lib/PDFRenderer.jar
Main-Class: Menu.Menu

Nota: itext 5.0.5 no visualiza archivos PDF solo es capaz de extraer texto, por eso se ha incluido una libreria extra :D.
