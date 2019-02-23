
/**
 * PhotoLabTester.java
 *
 * @author Dave Musicant
 * @author Jeff Ondich
 * @author Amy Csizmar Dalal
 * @author shreya-va
 * This class provides a test of your PhotoLab class.
 * Modify it as you see fit.
 */

import java.io.*;
import java.awt.*;

class PhotoLabTester
{
    public static void main( String[] args ) throws IOException
    {
        PhotoLab photoLab = new PhotoLab();
        Sorting sortArray = new Sorting();
        EzImage imageOriginal = new EzImage("background3.jpg");
        EzImage imageSecond = new EzImage("background2.jpg");
        imageOriginal.show("Original", 0, 0);

        EzImage imageNew = photoLab.onlyRed(imageOriginal);
        imageNew.show("Only red", 0, 300);

        imageNew = photoLab.negate(imageOriginal);
        imageNew.show("Negative", 0, 500);
        
       //imageNew = photoLab.inOrder(imageOriginal);
        //imageNew.show("In Order", 0, 700);
        
        imageNew = photoLab.colourful(imageSecond);
        imageNew.show("Colourful", 0, 700);
    }
}