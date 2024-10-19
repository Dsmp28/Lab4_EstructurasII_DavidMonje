package org.java;

import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws CsvValidationException, IOException {
        Menu menu = new Menu();
        menu.showMenu();
    }

}
