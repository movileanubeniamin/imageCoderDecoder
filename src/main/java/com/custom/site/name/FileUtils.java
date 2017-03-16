/**
 * 
 */
package com.custom.site.name;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bmovileanu
 *
 */
public class FileUtils {
   
   /**
    * @param fileName
    * @return Image. On the first and second positions are the dimensions. The third value is the maximal value of an
    *         pixel
    */
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
}
