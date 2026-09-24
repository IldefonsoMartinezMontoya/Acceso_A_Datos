# Memoria explicativa — Práctica 1

## Introducción
La práctica reúne ejercicios de Java sobre manejo de ficheros y creación de interfaces gráficas con Swing. Las clases pertenecen al paquete `Practica1` del proyecto `JavaClase2`.

## Ejercicio 1: contenido de un directorio
`Ejercicio_1` toma una ruta opcional desde los argumentos de ejecución. Si no recibe ninguna, utiliza `src/EjemplosClaseFileStreams`. Comprueba la existencia de la ruta y muestra por consola los nombres de sus elementos. Emplea la clase `File` y practica el uso de argumentos de línea de comandos.

## Ejercicio 2: lectura de texto
`Ejercicio_2` abre la ruta recibida como argumento o, por defecto, `FichTexto.txt`. Lee el fichero línea a línea con `FileReader` y `BufferedReader` e imprime su contenido en consola. Usa `try-with-resources` para cerrar el lector y maneja errores de entrada/salida.

## Ejercicio 3: formulario y almacenamiento
`Ejercicio_3` presenta una ventana Swing con un menú vertical. «Crear Fichero» abre un formulario con campos para nombre, apellidos y ciudad natal. «Terminar» agrega los datos a `src/Practica1/Ejercicio_3_texto.txt`, vacía los campos e informa del resultado. «Volver» retorna al menú; «Mostrar» abre una ventana con el texto guardado; «Cerrar programa» termina la aplicación. Se utilizan `JFrame`, `JPanel`, `GridLayout`, `CardLayout`, botones, campos de texto y listeners de acción.

## Ejercicio 4: escritura y lectura de números
`Ejercicio_4` ofrece botones para generar, mostrar y cerrar. Al generar, añade al fichero `src/Practica1/Ejercicio_4_texto.txt` los números pares desde 0 hasta 100, separados por espacios y en una nueva línea. El botón «Mostrar» presenta el contenido en una ventana. Se utilizan `BufferedWriter`, `FileWriter`, `BufferedReader` y `FileReader`.

## Ficheros de datos
- `Ejercicio_3_texto.txt`: registros introducidos mediante el formulario del ejercicio 3.
- `Ejercicio_4_texto.txt`: secuencias de pares generadas por el ejercicio 4.

## Conceptos trabajados
Clases, métodos, argumentos de ejecución, rutas, lectura y escritura de ficheros de texto, excepciones `IOException`, cierre automático de recursos, componentes Swing, distribución con layouts y respuesta a eventos.

## Para completar posteriormente
Esta memoria resume el código revisado el 24 de septiembre de 2026. Más adelante se pueden añadir el enunciado de la práctica, capturas, pruebas, incidencias y conclusiones personales.
