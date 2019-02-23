
/**
 * PhotoLab.java
 *
 * @author shreya-va
*/
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
public class PhotoLab
{

 public EzImage onlyRed(EzImage image)
   {
     int[][][] pixels = image.getPixels3D();

     for(int i=0; i<pixels.length; i++){
        
            for (int j=0;j<pixels[0].length;j++)
            {    
                pixels[i][j][1] = 0;
                pixels[i][j][2] = 0;
            }
        }
      
     EzImage fixed = new EzImage(pixels);
     return fixed;
    }
   public EzImage negate(EzImage image)
   {
     int[][][] pixels = image.getPixels3D();
     for (int i=0;i<pixels.length; i++){
            
       for (int j=0;j<pixels[0].length;j++){
          pixels[i][j][1] = 255-pixels[i][j][1];
          pixels[i][j][2] = 255-pixels[i][j][2];
          pixels[i][j][0] = 255-pixels[i][j][0];
        }
       
     }
     EzImage fixed = new EzImage(pixels);
     return fixed;
 }
 
 public static void insertionSort (int [] numbers){
        for(int index = 1; index < numbers.length; index++){
            int key = numbers[index];
            int position = index;
        
            while(position > 0 && numbers [position - 1] > key){
                numbers[position] = numbers[position - 1];
                position--;
            }
            numbers[position] = key;
        }
    }
    
 public EzImage inOrder(EzImage image)
 {
     EzImage newImage = image.copyToGrayScale();
     int [] grayArray = newImage.getPixels1D();
     insertionSort(grayArray);
     newImage.setPixels(grayArray);
     return newImage;
    }

 public EzImage colourful(EzImage image)
 {
     int[][][] pixels = image.getPixels3D();
     Random random = new Random();
     int red = random.nextInt(256);
     int blue = random.nextInt(256);
     int green = random.nextInt(256);
     for (int i=0;i<pixels.length; i++){
       int imageWidth = image.getWidth();
       for (int j=0;j<pixels[0].length;j++){
          if (j % 5  == 0){
           pixels[i][j][1] = red;
           pixels[i][j][2] = blue;
           pixels[i][j][0] = green;
          if(i % 10 == 0){
          pixels[i][j][1] = 0;
          pixels[i][j][0] = 0;
        }
       }
        }
       
      
     }
     EzImage fixed = new EzImage(pixels);
     return fixed;
}
}
