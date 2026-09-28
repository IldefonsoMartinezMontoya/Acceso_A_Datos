# Memoria explicativa: Práctica 1

## Prácticas Clase File y Streams

## Ejercicio 1: contenido de un directorio
Ejercicio_1 toma una ruta desde los argumentos de ejecución. Comprueba la existencia de la ruta y muestra por consola los nombres de sus elementos.
## Ejercicio 2: lectura de texto
Ejercicio_2 abre la ruta recibida como argumento o, FichTexto.txt. Lee el fichero línea a línea con FileReader y BufferedReader e imprime su contenido en consola.
## Ejercicio 3: formulario y almacenamiento
Ejercicio_3 presenta una ventana Swing con un menú vertical. «Crear Fichero» abre un formulario con campos para nombre, apellidos y ciudad natal. «Terminar» agrega los datos a src/Practica1/Ejercicio_3_texto.txt, vacía los campos e informa del resultado. «Volver» retorna al menú; «Mostrar» abre una ventana con el texto guardado; «Cerrar programa» termina la aplicación. Se utilizan JFrame, JPanel, GridLayout, botones, campos de texto y listeners de acción.

## Ejercicio 4: escritura y lectura de números
Ejercicio_4 ofrece botones para generar, mostrar y cerrar. Al generar, añade al fichero src/Practica1/Ejercicio_4_texto.txt los números pares desde 0 hasta 100, separados por espacios y en una nueva línea. El botón «Mostrar» presenta el contenido en una ventana. Se utilizan BufferedWriter, FileWriter, BufferedReader y FileReader.

## Ejercicio 5: escritura y transformación de texto
Ejercicio_5 solicita por consola la ruta del fichero y una línea de contenido. Añade esa línea al final del fichero y después lee el fichero completo. Muestra el texto por consola cambiando las letras minúsculas a mayúsculas y las mayúsculas a minúsculas. Conserva espacios, números y signos, y mantiene los saltos entre líneas.

## Ejercicio 6: suma de números de un fichero
Ejercicio_6 lee el fichero indicado por la constante ARCHIVO. Interpreta cada línea como un número entero, suma los valores y muestra el total por consola. El fichero debe contener un entero por línea.

## Ficheros de datos
- Ejercicio_3_texto.txt: registros introducidos mediante el formulario del ejercicio 3.
- Ejercicio_4_texto.txt: secuencias de números pares generadas por el ejercicio 4.
- Ejercicio 5: el fichero que se solicita por consola; se crea si no existe y se amplía en cada ejecución.
- Ejercicio 6: el fichero indicado en ARCHIVO, con un número entero en cada línea.