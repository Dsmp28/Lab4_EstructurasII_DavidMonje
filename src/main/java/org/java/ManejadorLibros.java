package org.java;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class ManejadorLibros {
    private HashMap<String, Libro> libros; //HashMap para almacenar los libros
    private HashMap<String, String> mapaNombreIsbn; //HashMap para almacenar el nombre del libro y su isbn
    List<Libro> resultados; //Lista para almacenar los resultados de la busqueda


    //Constructor de la clase
    public ManejadorLibros() {
        libros = new HashMap<>();
        mapaNombreIsbn = new HashMap<>();
        resultados = new ArrayList<>();
    }

    // Método para procesar un archivo CSV y procesar las acciones de inserción, actualización, eliminación y búsqueda de libros
    public void processFile(String filePath) throws IOException, CsvValidationException {
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                // El primer elemento contiene la acción (INSERT, PATCH, DELETE, etc.)
                String action = line[0].split(";")[0].trim();

                // Reconstruir el contenido JSON a partir del resto de las columnas
                StringBuilder jsonStringBuilder = new StringBuilder(line[0].split(";", 2)[1]);
                for (int i = 1; i < line.length; i++) {
                    jsonStringBuilder.append("\",\"").append(line[i]);
                }
                String jsonString = jsonStringBuilder.toString();

                // Procesar la acción
                switch (action) {
                    case "INSERT":
                        insertBook(jsonString);
                        break;
                    case "PATCH":
                        patchBook(jsonString);
                        break;
                    case "DELETE":
                        deleteBook(jsonString);
                        break;
                    case "SEARCH":
                        searchBook(jsonString);
                        break;
                }
            }

            // Escribir los resultados en un archivo de texto si hay resultados
            if (!resultados.isEmpty()) {
                File archivoSalida = new File("src/main/resources/resultados.enc");

                try (FileWriter writer = new FileWriter(archivoSalida)) {
                    StringBuilder sb = new StringBuilder();

                    for (Libro libro : resultados) {
                        sb.append(libro.toString()).append("\r\n");
                    }
                    File archivoPrueba = new File("src/main/resources/salida.txt");
                    try (FileWriter pruebaW = new FileWriter(archivoPrueba)) {
                        pruebaW.write(sb.toString());
                    }

                    DESManual des = new DESManual(new String(Files.readAllBytes(archivoPrueba.toPath())));

                    // Escribir los bytes encriptados en el archivo
                    System.out.println("Escribiendo resultados encriptados en el archivo resultados.enc, por favor espere...\n");

                    Files.write(archivoSalida.toPath(), des.getEncriptado());
                    System.out.println("El archivo fue cargado correctamente. Los resultados se mostrarán en ../resources/resultados.enc\n");
                }
            } else {
                System.out.println("No hay resultados para mostrar. El archivo fue cargado correctamente.\n");
            }
        }
    }

    // Método para insertar un libro utilizando una cadena JSON como texto
    private void insertBook(String jsonString) {
        String isbn = extractValue(jsonString, "isbn");
        String name = extractValue(jsonString, "name");
        String author = extractValue(jsonString, "author");
        String category = extractValue(jsonString, "category");
        String price = extractValue(jsonString, "price");
        String quantity = extractValue(jsonString, "quantity");

        Libro libro = new Libro(isbn, name, author, category, price, quantity);
        libros.put(isbn, libro);
        mapaNombreIsbn.put(name, isbn);
    }

    // Método para actualizar un libro (PATCH) utilizando una cadena JSON como texto
    private void patchBook(String jsonString) {
        String isbn = extractValue(jsonString, "isbn");
        if (libros.containsKey(isbn)) {
            Libro libro = libros.get(isbn);
            String newName = extractValue(jsonString, "name");
            String newAuthor = extractValue(jsonString, "author");
            String newCategory = extractValue(jsonString, "category");
            String newPrice = extractValue(jsonString, "price");
            String newQuantity = extractValue(jsonString, "quantity");

            if (!newName.isEmpty()) {
                mapaNombreIsbn.remove(libro.getName());
                libro.setName(newName);
                mapaNombreIsbn.put(newName, isbn);
            }
            if (!newAuthor.isEmpty()) {
                libro.setAuthor(newAuthor);
            }
            if (!newCategory.isEmpty()) {
                libro.setCategory(newCategory);
            }
            if (!newPrice.isEmpty()) {
                libro.setPrice(newPrice);
            }
            if (!newQuantity.isEmpty()) {
                libro.setQuantity(newQuantity);
            }
        }
    }


    // Método para eliminar un libro (DELETE) utilizando una cadena JSON como texto
    private void deleteBook(String jsonString) {
        String isbn = extractValue(jsonString, "isbn");
        if (libros.containsKey(isbn)) {
            Libro libro = libros.remove(isbn);
            mapaNombreIsbn.remove(libro.getName());
        }
    }

    // Método para buscar un libro (SEARCH) utilizando una cadena JSON como texto
    private void searchBook(String jsonString) {
        String name = extractValue(jsonString, "name");
        if (mapaNombreIsbn.containsKey(name)) {
            String isbn = mapaNombreIsbn.get(name);
            Libro libro = libros.get(isbn);
            resultados.add(libro);
        }
    }

    // Método auxiliar para extraer valores de las cadenas JSON simuladas
    private String extractValue(String jsonString, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = jsonString.indexOf(searchKey);

        if (startIndex == -1) {
            // Si no encuentra la clave en el JSON, retorna una cadena vacía
            return "";
        }

        startIndex += searchKey.length();

        // Identificar si el valor empieza con una comilla
        if (jsonString.charAt(startIndex) == '"') {
            // Si empieza con comillas, buscar la comilla de cierre
            startIndex++; // Avanzar más allá de la primera comilla
            int endIndex = jsonString.indexOf("\"", startIndex);
            // Extraer el valor entre las comillas
            return jsonString.substring(startIndex + 1, endIndex - 1).trim();
        } else {
            // Si no hay comillas, el valor es un número o texto sin comillas
            int endIndex = jsonString.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = jsonString.indexOf("}", startIndex);
            }
            return jsonString.substring(startIndex + 2, endIndex  - 1).trim();
        }
    }


}