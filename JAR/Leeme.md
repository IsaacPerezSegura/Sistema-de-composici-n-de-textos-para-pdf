Dentro de la carpeta "Muestra" se incluye un archivo PDF el cual fue generado
a base del archivo "prueba.txt" junto con las imagenes usadas para el mismo, con el fin
de mostrar la funcionalidad del programa.

Compilado en Windows
<br>
Lineas de comandos usadas:<br>

- Compilando :<br>
GUIPdf> javac -d compilacion -cp lib\iText-5_0_5.jar;lib\PDFRenderer.jar;src s
rc\Menu\Menu.java

- Creando jar:<br>
compilacion> jar -cvfm GUIPDF.jar manifest.txt Etiquetas/*.class Menu/*
.class GUI/*.class
<br>
Donde Manifest:<br>
Class-Path: lib/iText-5_0_5.jar lib/PDFRenderer.jar
Main-Class: Menu.Menu
<br>
<color.red>Nota: itext 5.0.5 no visualiza archivos PDF solo es capaz de extraer texto, por eso se ha incluido una libreria extra :D.
