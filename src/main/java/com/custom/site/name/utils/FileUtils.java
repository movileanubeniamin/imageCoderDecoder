package com.custom.site.name.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class FileUtils {


    public static List<Integer> readPpm(String fileName) {
        List<Integer> image = new ArrayList<Integer>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            // Fromat
            bufferedReader.readLine();
            // Comment
            bufferedReader.readLine();
            // Dimenstions
            line = bufferedReader.readLine();
            String[] dimensions = line.split(" ");
            image.add(Integer.valueOf(dimensions[0]));
            image.add(Integer.valueOf(dimensions[1]));
            // read RGB
            while ((line = bufferedReader.readLine()) != null)
                image.add(Integer.valueOf(line));
            bufferedReader.close();
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("File not found: " + fileNotFoundException);
        } catch (IOException ioException) {
            System.err.println(ioException);
        }
        return image;
    }


    public static void writePpm(String fileName, List<Integer> image) {
        BufferedWriter outputWriter;
        try {
            outputWriter = new BufferedWriter(new FileWriter(fileName));
            outputWriter.write("P3");
            outputWriter.newLine();
            outputWriter.write("# CREATOR: GIMP PNM Filter Version 1.1");
            outputWriter.newLine();
            outputWriter.write("800 600");
            outputWriter.newLine();
            outputWriter.write("255");
            outputWriter.newLine();
            for (Integer anImage : image) {
                outputWriter.write(Integer.toString(anImage));
                outputWriter.newLine();
            }
            outputWriter.flush();
            outputWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
