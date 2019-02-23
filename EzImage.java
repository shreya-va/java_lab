
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.net.*;

/**
 * The EzImage class makes accessing and manipulating images straightforward.
 * Images can be loaded from files in GIF, JPEG, or PNG format and manipulated 
 * through a variety of array operations.
 * Both gray-scale and color images are supported. <br />
 * 
 * The ideas and much of the code are largely from the class EasyBufferedImage
 * by Kenny Hunt at UW-Lacrosse.
 * 
 * @author Kenny Hunt, University of Wisconin - Lacrosse
 * @author Dave Musicant, Carleton College
 * @author Jeff Ondich, Carleton College
 */
public class EzImage
{
    // Assign numeric values for each color. Enumerated types would be a
    // safer way to do this, but they make it more difficult for the user:
    // instead of being able to say EzImage.ALPHA, the user would have to
    // say EzImage.EzColor.ALPHA (or something like it). Alternatively,
    // EzColor (the enumerated type) could be represented as a non-nested class
    // to avoid this problem, but then the user would have to manipulate two
    // files. All in all, the even though enumerated types are safer, using
    // static final ints is cleaner for the user in this case. To help
    // safety, the constants used are negative and large in magnitude.
    // This helps assure that they won't be accidentally confused for color
    // band identifiers.
    public static final int RED = -1001;
    public static final int GREEN = -1002;
    public static final int BLUE = -1003;
    public static final int GRAY = -1004;
    public static final int ALPHA = -1005;
    
    // All image formats that Java knows how to write
    private static final String[] WRITER_TYPES = ImageIO.getWriterFormatNames();

    private static int windowCount = 0;

    private BufferedImage bufferedImage;
	
    /**
     * Constructs an EzImage object represented by the specified pixels.
     * The dimensions of the pixel array must be [height][width] and is assumed
     * to be gray-scale.
     * 
     * @param   pixels  an array of [height][width] pixels that represents the
     *                  image
     * @throws          IllegalArgumentException if pixels is null
     */
    public EzImage(int[][] pixels)
    {
        // Create the BufferedImage (doesn't include transparency)
        bufferedImage = new BufferedImage(pixels[0].length,pixels.length,
                                          BufferedImage.TYPE_BYTE_GRAY);

        if(pixels == null)
            throw new IllegalArgumentException("null pixels array");	
	   	
        setPixels(pixels, GRAY);
    }



    /**
     * Constructs an EzImage object represented by the specified
     * pixels.  The dimensions of the pixel arrays must be [height *
     * width] and layed out in row-major format.  The three pixel
     * arrays must be in red, green, blue order.
     *
     * @param redPixels an array of [height][width] pixels that
     *                  represents the red pixels in the image.
     * @param greenPixels an array of [height][width] pixels that
     *                  represents the green pixels in the image.
     * @param bluePixels an array of [height][width] pixels that
     *                  represents the blue pixels in the image.
     * @throws         IllegalArgumentException if pixels is null
     */
    public EzImage(int[][] redPixels, int[][] greenPixels, int[][] bluePixels)
    {
        // Create the BufferedImage (doesn't include transparency)
        bufferedImage = new BufferedImage(redPixels[0].length,redPixels.length,
                                          BufferedImage.TYPE_INT_RGB);

        if(redPixels == null || greenPixels == null || bluePixels == null)
            throw new IllegalArgumentException("null pixels array");
		
        setPixels(redPixels,RED);
        setPixels(greenPixels,GREEN);
        setPixels(bluePixels,BLUE);
    }

	
    /**
     * Constructs an EzImage object represented by the specified pixels.
     * The dimensions of the pixel array must be [height][width][3]
     * (red-green-blue).
     * 
     * @param  pixels  an array of [height][width][bands] pixels that
     *                 represents the image.
     * @throws         IllegalArgumentException if pixels is null 
     */
    public EzImage(int[][][] pixels)
    {
        // Create the BufferedImage (doesn't include transparency)
		bufferedImage = new BufferedImage(pixels[0].length,pixels.length,
                                          BufferedImage.TYPE_INT_RGB);

        if(pixels == null)
            throw new IllegalArgumentException("null pixels array");
		
        setPixels(pixels);
    }

    /**
     * Constructs an EzImage object represented by the specified
     * pixels.  The dimensions of the pixel arrays must be [height *
     * width] and layed out in row-major format.  The three pixel
     * arrays must be in red, green, blue order.
     *
     * @param redPixels an array of [height * width] pixels that
     *                  represents the red pixels in the image.
     * @param greenPixels an array of [height * width] pixels that
     *                  represents the green pixels in the image.
     * @param bluePixels an array of [height * width] pixels that
     *                  represents the blue pixels in the image.
     * @param  width   the width (in pixels) of the image.
     * @param  height  the height (in pixels) of the image.
     * @throws         IllegalArgumentException if pixels is null or the
     *                 length is not width * height.
     */
    public EzImage(int[] redPixels, int[] greenPixels, int[] bluePixels,
                   int height, int width)
    {
        if(redPixels.length != greenPixels.length ||
           redPixels.length != bluePixels.length)
            throw new IllegalArgumentException("pixel array sizes do not match");

        // Create the BufferedImage (doesn't include transparency)
        bufferedImage = new BufferedImage(width,height,
                                          BufferedImage.TYPE_INT_RGB);

        if(redPixels == null || greenPixels == null || bluePixels == null)
            throw new IllegalArgumentException("null pixels array");
		
        setPixels(redPixels,RED);
        setPixels(greenPixels,GREEN);
        setPixels(bluePixels,BLUE);
    }


    /**
     * Constructs an EzImage object represented by the specified pixels.
     * The dimensions of the pixel array must be [height * width] and layed out
     * in row-major format.  The image is assumed to be gray-scale.
     *
     * @param  pixels  an array of [height * width] pixels that represents the
     *                 image.
     * @param  width   the width (in pixels) of the image.
     * @param  height  the height (in pixels) of the image.
     * @throws         IllegalArgumentException if pixels is null or the
     *                 length is not width * height.
     */
    public EzImage(int[] pixels, int height, int width)
    {
        // Create the BufferedImage (doesn't include transparency)
        bufferedImage = new BufferedImage(width,height,
                                          BufferedImage.TYPE_BYTE_GRAY);

        if(pixels == null)
        {
            throw new IllegalArgumentException("null pixels array");
        }
        else if((width * height) != pixels.length)
        {
            throw new IllegalArgumentException(
                    "pixels dimensions doesn't match width/height parameters");
        }
        
        setPixels(pixels, GRAY);
    }

    /**
     * Constructs an EzImage object by wrapping around a BufferedImage object.
     *
     * @param  image  a BufferedImage object
     */
    public EzImage(BufferedImage image)
    {
        bufferedImage = image;
    }

    /**
     * Constructs an EzImage object by reading the image file
     * specified by the filename.  The format of the file may be any
     * one of the formats that Java has readers for, such as GIF, PNG, or JPEG.
     * 
     * @param  filename  the name of the file to load
     * @throws           FileNotFoundException
     */
    public EzImage(String filename) throws FileNotFoundException, IOException
    {
        File file = new File(filename);
        if(!file.exists())
            throw new FileNotFoundException(filename);
        
        bufferedImage = ImageIO.read(file);
    }
    
    /**
     * Constructs an EzImage object from the File object indicated.
     * The format of the file may be any one of the formats that Java has
     * readers for, such as GIF, PNG, or JPEG.
     * 
     * @param  file  the File object to load
     * @throws       IOException,FileNotFoundException
     */
    public EzImage(File file) throws IOException, FileNotFoundException
    {
        if(!file.exists())
            throw new FileNotFoundException(file.getName());

        bufferedImage = ImageIO.read(file);
    }

    /**
     * Constructs an EzImage object by reading the image
     * specified by the url.  The format of the file may be any
     * one of the formats that Java has readers for, such as GIF, PNG, or JPEG.
     * 
     * @param  url  the URL of an image file to load.
     */
    public EzImage(URL url) throws IOException
    {
        bufferedImage = ImageIO.read(url);
    }


    /**
     * Returns the value of v clamped to the range 0-255.
     * This is a convenience method for working with image pixel values.
     *
     * @param  v  the value of a pixel to be clamped.
     * @return    the value v clamped to the range 0-255.
     */
    private static int clamp(double v)
    {
        if(v < 0) 
            return 0;
        else if(v > 255)
            return 255;
        else
            return (int)v;
    }

    /**
     * Returns an EzImage object that is a gray-scale copy.
     * 
     * @return      an EzImage that is a gray-scale copy
     */
    public EzImage copyToGrayScale() {
        BufferedImage result = new BufferedImage(bufferedImage.getWidth(),
                bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster input = bufferedImage.getRaster();
        WritableRaster output = result.getRaster();
        for(int row=0; row < input.getHeight(); row++)
        {
            for(int col=0; col < input.getWidth(); col++)
            {
                int red = input.getSample(col, row, getBandId(RED));
                int green = input.getSample(col, row, getBandId(BLUE));
                int blue = input.getSample(col, row, getBandId(GREEN));
                output.setSample(col, row, 0, clamp((red+green+blue)/3.0));
            }
        }
        return new EzImage(result);
    }

    /**
     * Returns a 3D array of pixel values.  The dimensions of the array
     * correspond to the [height][width][bands] of the EzImage.  The number of
     * bands will be 1 for a gray-scale or binary image, 3 for a color image
     * without transparency, and 4 for a color image with transparency.  The
     * bands are (in order) RED (or GRAY), GREEN, BLUE, ALPHA.  Pixel values
     * are in the range 0-255.
     * 
     * @return     an array of pixels
     */
    public int[][][] getPixels3D()
    {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int bands = bufferedImage.getSampleModel().getNumBands();
        int[][][] pixels = new int[height][width][bands];
        WritableRaster raster = bufferedImage.getRaster();
        for(int i=0; i<height; i++)
        {
            for(int j=0; j<width; j++)
            {
                for(int k=0; k<bands; k++)
                {
                    pixels[i][j][k] = raster.getSample(j,i,k);                 
                }
            }
        }
        return pixels;
    }

    /**
     * Returns a 2D array of pixel values corresponding to the specified band.
     * The dimensions of the array correspond to the [height][width] of the
     * EzImage. The number of bands will be 1 for a gray-scale or binary image,
     * 3 for a color image without transparency, and 4 for a color image with
     * transparency.  The bands are (in order) RED (or GRAY), GREEN, BLUE,
     * ALPHA.  Pixel values are in the range 0-255.
     * 
     * @param  band  is either RED, GREEN, BLUE, ALPHA, or GRAY
     * @return       an array of pixels.
     * @throws       IllegalArgumentException if gray band is requested from
     *               color image, or vice-versa
     */
    public int[][] getPixels2D(int band) {

        if (band == GRAY && isColor())
            throw new IllegalArgumentException(
                    "Requested gray band of color image.");

        if (band != GRAY && !isColor())
            throw new IllegalArgumentException(
                    "Requested color band of gray image.");

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[][] pixels = new int[height][width];
        WritableRaster raster = bufferedImage.getRaster();
        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                pixels[i][j] = raster.getSample(j,i,getBandId(band));
            }
        }

        return pixels;
    }

    
    /**
     * Returns a 2D array of pixel values corresponding to gray values.
     * This method is only valid if the image is gray.
     * The dimensions of the array correspond to the [height][width] of the
     * EzImage. Pixel values are in the range 0-255.
     * 
     * @return       an array of pixels.
     * @throws       UnsupportedOperationException if image is color
     */
    public int[][] getPixels2D() {
        if (isColor())
            throw new UnsupportedOperationException("Requested pixels from a " +
                    "color image without specifying band.");
        else
            return getPixels2D(GRAY);
    }
    
    
    /**
     * Returns a 1D array of pixel values corresponding to the specified band.
     * The array is layed out in row-major format and contains height*width
     * pixel values. The number of bands will be 1 for a gray-scale or binary
     * image, 3 for a color image without transparency, and 4 for a color image
     * with transparency. The bands are (in order) RED (or GRAY), GREEN, BLUE,
     * ALPHA.  Pixel values are in the range 0-255.
     * 
     * @param  band  is either RED, GREEN, BLUE, ALPHA, or GRAY
     * @return       an array of pixels.
     * @throws       IllegalArgumentException if gray band is requested from
     *               color image, or vice-versa
     */
    public int[] getPixels1D(int band)
    {
        if (band == GRAY && isColor())
            throw new IllegalArgumentException(
                    "Requested gray band of color image.");

        if (band != GRAY && !isColor())
            throw new IllegalArgumentException(
                    "Requested color band of gray image.");

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[]pixels = new int[height * width];
        WritableRaster raster = bufferedImage.getRaster();
        for(int i=0; i<height; i++)
        {
            for(int j=0; j<width; j++)
            {
                pixels[i*width + j] = raster.getSample(j,i,getBandId(band));
            }
        }

        return pixels;
    }

    /**
     * Returns a 1D array of pixel values corresponding to gray values.
     * This method is only valid if the image is gray.
     * The array is layed out in row-major format and contains height*width
     * pixel values. Pixel values are in the range 0-255.
     * 
     * @return       an array of pixels.
     * @throws       UnsupportedOperationException if image is color
     */
    public int[] getPixels1D() {
        if (isColor())
            throw new UnsupportedOperationException("Requested pixels from a " +
                    "color image without specifying band.");
        else
            return getPixels1D(GRAY);
    }
    
    
    
    /**
     * Returns an integer band identifier associated with the color provided.
     * In storing gray images, the Java BufferedImage stores all data in band 0.
     * In storing color images, Java stores red in band 0, green in band 1,
     * blue in band 2, and alpha (if it exists) in band 3).
     * 
     * @param  band  an appropriate EzColor band
     * @return       an integer band identifier
     * @throws       IllegalArgumentException if an unaccounted for band is
     *               input
     */
    private int getBandId(int band)
    {
        int bandId = -1;
        switch (band)
        {
            case GRAY:
            case RED:
                bandId = 0;
                break;
            case GREEN:
                bandId = 1;
                break;
            case BLUE:
                bandId = 2;
                break;
            case ALPHA:
                bandId = 3;
                break;
            default:
                throw new IllegalArgumentException("Invalid band.");
        }
        return bandId;
    }
    
    /**
     * Sets all of the EzImage pixels.
     * This method is only valid if the image is gray.
     * The array is layed out in row-major format and contains height*width
     * pixel values. Pixel values are in the range 0-255.
     * 
     * @param  pixels   an array of pixels.
     * @throws           UnsupportedOperationException if image is color
     */
    public void setPixels(int[] pixels) {
        if (isColor())
            throw new UnsupportedOperationException("Input pixels for a " +
                    "color image without specifying band.");
        else
            setPixels(pixels,GRAY);
    }
    

    /**
     * Sets all of the EzImage pixels in the specified band.  The number of
     * elements in the array must be equal to height*width of the EzImage.
     * The band must be GRAY if the image is gray-scale, or RED, GREEN, BLUE,
     * or ALPHA is the image is color.  Pixel values not in the range 0-255
     * will be stripped of their higher-order bits.
     * 
     * @param  pixels  an array of the "new" image for the specified band.
     * @param  band    one of GRAY, RED, GREEN, BLUE, or ALPHA
     * @throws         IllegalArgumentException if the pixels array is not
     *                 compatible with the image, or if an illegal color band
     *                 is specified.
     */
    public void setPixels(int[] pixels, int band) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        if(pixels == null || width * height != pixels.length)
            throw new IllegalArgumentException(
                    "pixel array doesn't match the image size");

        if (band == GRAY && isColor())
            throw new IllegalArgumentException(
                    "Indicated gray band for color image.");

        if (band != GRAY && !isColor())
            throw new IllegalArgumentException(
                    "Indicated color band for gray image.");

        WritableRaster raster = bufferedImage.getRaster();
        for(int i=0; i<height; i++)
        {
            for(int j=0; j<width; j++)
            {
                raster.setSample(j, i, getBandId(band), pixels[i*width + j]);
            }
        }
    }

    /**
     * Sets all of the EzImage pixels.
     * This method is only valid if the image is gray.
     * The array is layed out in row-major format and contains height*width
     * pixel values. Pixel values are in the range 0-255.
     * 
     * @param  pixels   an array of pixels.
     * @throws           UnsupportedOperationException if image is color
     */
    public void setPixels(int[][] pixels) {
        if (isColor())
            throw new UnsupportedOperationException("Input pixels for a " +
                    "color image without specifying band.");
        else
            setPixels(pixels,GRAY);
    }
    

    /**
     * Sets all of the EzImage pixels in the specified band.  The number of
     * elements in the array must be equal to height*width of the EzImage.
     * The band must be GRAY if the image is gray-scale, or RED, GREEN, BLUE,
     * or ALPHA is the image is color.  Pixel values not in the range 0-255
     * will be stripped of their higher-order bits.
     * 
     * @param  pixels  an array of the "new" image for the specified band.
     * @param  band    one of GRAY, RED, GREEN, BLUE, or ALPHA
     * @throws         IllegalArgumentException if the pixels array is not
     *                 compatible with the image, or if an illegal color band
     *                 is specified.
     */
    public void setPixels(int[][] pixels, int band)
    {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if(pixels == null || pixels[0] == null ||
           width != pixels[0].length || height != pixels.length)
            throw new IllegalArgumentException(
                    "pixel array doesn't match the image size");

        if (band == GRAY && isColor())
            throw new IllegalArgumentException(
                    "Indicated gray band for color image.");

        if (band != GRAY && !isColor())
            throw new IllegalArgumentException(
                    "Indicated color band for gray image.");
        
        WritableRaster raster = bufferedImage.getRaster();
        for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
                raster.setSample(j, i, getBandId(band), pixels[i][j]);
            }
        }
    }
    
    /**
     * Sets all of the EzImage pixels.  The dimensions of the pixels array
     * must correspond to [height][width][bands] of the EzImage.  The number of
     * bands will be 1 for a gray-scale or binary image, 3 for a color image
     * without transparency, and 4 for a color image with transparency.  The
     * bands are (in order) RED (or GRAY), GREEN, BLUE, ALPHA.  Pixel values
     * not in the range 0-255 will be stripped of their higher-order bits.
     * 
     * @param  pixels   a 3D array of HEIGHT by WIDTH by DEPTH pixels of the
     *                  image
     * @throws          IllegalArgumentException if the pixels array is not
     *                  compatible with the image.
     */
    public void setPixels(int[][][] pixels) 
    {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int bands = bufferedImage.getSampleModel().getNumBands();

        if(pixels == null || pixels[0] == null || pixels[0][0] == null ||
           width != pixels[0].length || height != pixels.length ||
           bands != pixels[0][0].length)
            throw new IllegalArgumentException(
                    "pixel array doesn't match the image size");

        WritableRaster raster = bufferedImage.getRaster();
        for(int i=0; i<height; i++)
        {
            for(int j=0; j<width; j++)
            {
                for(int k=0; k<bands; k++)
                {
                    raster.setSample(j, i, k, pixels[i][j][k]);
                }
            }
        }
    }

       
    /**
     * Returns true if the image is a color image and false otherwise.
     *    
     * @return     true if the image is a color image and false otherwise.
     * @throws     IllegalStateException if the number of bands is nonsensical.
     */
    public boolean isColor()
    {
        int numBands = bufferedImage.getSampleModel().getNumBands();
        // gray
        if (numBands == 1)           
            return false;
        // color, possibly with transparency
        else if (numBands == 3 || numBands == 4)
            return true;
        else
            throw new IllegalStateException("Invalid number of bands.");
    }

    /**
     * Returns the number of color bands present in the image.
     *    
     * @return     the number of color bands in this image
     */
    public int numColorBands()
    {
        return bufferedImage.getSampleModel().getNumBands();
    }
    
    
    /** 
     * Returns a String array listing the file formats supported by this class
     * for writing.
     * @return   an array of strings listing the supported file formats.
     */
    public String[] getSupportedWritingFormats()
    {
        return WRITER_TYPES;
    }
    
    /**
     * Returns the image in BufferedImage format.
     * @return   the image in BufferedImage format
     */
    public BufferedImage getBufferedImage()
    {
        return bufferedImage;
    }

    /**
     * Returns the height of the image.
     * @return   the height of this image
     */
    public int getHeight()
    {
        return bufferedImage.getHeight();
    }

    /**
     * Returns the width of the image.
     * @return   the height of this image
     */
    public int getWidth()
    {
        return bufferedImage.getWidth();
    }

    /**
     * Returns a copy of the calling EzImage.
     * @return     A copy of the EzImage.
     */
    public EzImage copy()
    {
        // create the BufferedImage (doesn't include transparency!)
        BufferedImage result = new BufferedImage(getWidth(),getHeight(),
                bufferedImage.getType());

        // Draw the input onto the BufferedImage and return
        Graphics g = result.createGraphics();
        g.drawImage(bufferedImage, 0, 0, null);
        g.dispose();
        return new EzImage(result);
    }

    private static class ImagePanel extends JPanel
    {
        private static final long serialVersionUID = 123L;
        private BufferedImage image;

        public ImagePanel(EzImage im)
        {
            image = im.bufferedImage;
            setMinimumSize(new Dimension(im.getWidth(), im.getHeight()));
        }

        public void paintComponent(Graphics g)
        {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());

            // center the image
            int dx = (getWidth() - image.getWidth()) / 2;
            if(dx < 0)
                dx = 0;

            int dy = (getHeight() - image.getHeight()) / 2;
            if( dy < 0 )
                dy = 0;
            g.drawImage(image, dx, dy, this);
        }
    }
    
    /**
     * Creates a window that will display this EzImage. EzImage maintains a
     * count of all windows shown and will terminate the application if
     * <b>all</b> such windows are closed.
     * @param      title    the title of the window
     */
    public void show(String title)
    {
        show(title,0,0);
    }

    /**
     * Creates a window that will display this EzImage. EzImage maintains a
     * count of all windows shown and will terminate the application if
     * <b>all</b> such windows are closed.
     * @param  title   the title of the window
     * @param  row     the row coordinate of the upper left corner of the
     *                 window
     * @param  column  the column coordinate of the upper left corner of the
     *                 window
     */
    public void show(String title, int row, int column)
    {
        JFrame window = new JFrame(title);
        window.getContentPane().add(new ImagePanel(this.copy()));
        window.getContentPane().setPreferredSize
            (new Dimension(getWidth(),getHeight()));
        window.pack();
        window.setLocation(column,row);
        windowCount++;
        window.addWindowListener(
                    new WindowAdapter()
                    {
                        public void windowClosing(WindowEvent e)
                        {
                            if(--windowCount == 0)
                                System.exit(0);
                            else
                                e.getWindow().dispose();
                        }
                    }
                );

        window.setVisible( true );
    }

    /**
     * Creates an image file having the specified name and of the specified format.
     * @param   filename  the name of the file to be saved
     * @param   format    String containing one of the supported Java file
     *                    types
     * @throws            IOException if the file cannot be created,
     *                    IllegalArgumentException if the file type is not
     *                    supported
     */
    public void save(String filename, String format) throws IOException {
        boolean validType = false;
        for (int i=0; i < WRITER_TYPES.length; i++)
            if (format.equals(WRITER_TYPES[i]))
            {
                validType = true;
                break;
            }
        
        if (!validType)
            throw new IllegalArgumentException("File type is not valid.");

        ImageIO.write(bufferedImage, format, new File(filename));
    }


    /**
     * The main method for this class.  Used for testing purposes only!  
     * It is highly recommended that you write your own main method in a 
     * separate class file to use the EzImage class.
     */
    public static void main(String[] args) throws IOException
    {
        // Test 2D array constructor
        int[][] test1 = new int[200][255];
        int[][] test2 = new int[200][255];
        for (int i=0; i < test1.length; i++)
            for (int j=0; j < test1[0].length; j++) {
                test1[i][j] = j;
                test2[i][j] = i;
            }
        EzImage image1 = new EzImage(test1);
        image1.show("Black on left, white on right, shade in between");
        EzImage image2 = new EzImage(test2);
        image2.show("Black on top, almost white on bottom, shade in between",
                0,270);
        System.out.println("Image 1 color bands (should be 1) = "
                + image1.numColorBands());
        System.out.println("Image 2 color bands (should be 1) = "
                + image2.numColorBands());

        // Test 3D array constructor gray      
        int[][][] test3 = new int[200][255][3];
        int[][][] test4 = new int[200][255][3];
        int[][][] test5 = new int[200][255][3];
        for (int i=0; i < test3.length; i++)
            for (int j=0; j < test3[0].length; j++) {
                test3[i][j][0] = j;
                test4[i][j][1] = i;
                test5[i][j][2] = 255-j;
            }
        EzImage image3 = new EzImage(test3);
        image3.show("Black on left, red on right, shade in between",250,0);
        EzImage image4 = new EzImage(test4);
        image4.show("Black on top, almost green on bottom, shade in between",
                250,270);
        EzImage image5 = new EzImage(test5);
        image5.show("Blue on left, black on right, shade in between",
                250,550);
        System.out.println("Image 3 color bands (should be 3) = " 
                + image3.numColorBands());
        System.out.println("Image 4 color bands (should be 3) = " 
                + image4.numColorBands());
        System.out.println("Image 5 color bands (should be 3) = " 
                + image5.numColorBands());
        

        // Test 1D array constructor
        int height = 200;
        int width = 255;
        int[] test6 = new int[height*width];
        int[] test7 = new int[height*width];
        for (int i=0; i < height; i++)
            for (int j=0; j < width; j++) {
                test6[i*width+j] = j;
                test7[i*width+j] = i;
            }
        EzImage image6 = new EzImage(test6,height,width);
        image6.show("Black on left, white on right, shade in between",100,100);
        EzImage image7 = new EzImage(test7,height,width);
        image7.show("Black on top, almost white on bottom, shade in between",
                100,370);
        System.out.println("Image 6 color bands (should be 1) = "
                + image6.numColorBands());
        System.out.println("Image 7 color bands (should be 1) = "
                + image7.numColorBands());
        
        // Test String and file constructors
        EzImage image8 = new EzImage(new File("jackets.jpg"));
        image8.show("Jackets",400,100);
        EzImage image9 = new EzImage("jackets.jpg");
        image9.show("Jackets",400,500);
        
        // Test URL constructor
        EzImage image10 = new EzImage(new URL(
                "http://www.mathcs.carleton.edu/faculty/dmusican/dave3.jpg"));
        image10.show("Dave",400,900);
        
        // Test copy to gray and copy, verify that change to original doesn't
        // mess up copy
        EzImage image11 = new EzImage(new File("jackets.jpg"));
        EzImage image12 = image11.copyToGrayScale();
        EzImage image13 = image11.copy();
        EzImage image14 = image12.copy();
        int[][][] pixels = image11.getPixels3D();
        for (int i=100; i < 200; i++)
            for (int j=100; j < 200; j++)
                for (int k=0; k < 3; k++)
                    pixels[i][j][k] = 0;
        image11.setPixels(pixels);
        image11.show("Jackets with hole",0,0);
        image12.show("Pristine gray jackets",0,400);
        image13.show("Pristine color jackets",0,800);
        System.out.println("Image 11 color bands (should be 3) = " +
                image11.numColorBands());
        System.out.println("Image 12 color bands (should be 1) = " +
                image12.numColorBands());
        System.out.println("Image 13 color bands (should be 3) = " +
                image13.numColorBands());
        System.out.println("Image 14 color bands (should be 1) = " +
                image14.numColorBands());
        
        // Get and set 1D pixels
        EzImage image15 = new EzImage("jackets.jpg");
        EzImage image16 = image15.copyToGrayScale();
        int[] pixels1d = image15.getPixels1D(BLUE);
        for (int i=0; i < pixels1d.length; i++)
            pixels1d[i] = 255;
        image15.setPixels(pixels1d,BLUE);
        image15.show("Jackets with blue way up",0,0);
        pixels1d = image16.getPixels1D();
        for (int i=0; i < pixels1d.length; i+=5)
            pixels1d[i] = 255;
        image16.setPixels(pixels1d);
        image16.show("Gray jackets with every fifth pixel white",0,400);
        
        // Get and set 2D pixels
        EzImage image17 = new EzImage("jackets.jpg");
        EzImage image18 = image15.copyToGrayScale();
        int[][] pixels2d = image17.getPixels2D(BLUE);
        for (int i=100; i < pixels2d.length; i++)
            for (int j=300; j < pixels2d[0].length; j++)
                pixels2d[i][j] = 255;
        image17.setPixels(pixels2d,BLUE);
        image17.show(
            "Jackets with blue way up in rectangle more right than down",300,0);
        pixels2d = image18.getPixels2D();
        for (int i=100; i < pixels2d.length; i++)
            for (int j=300; j < pixels2d[0].length; j++)
                pixels2d[i][j] = 255;
        image18.setPixels(pixels2d);
        image18.show("Gray jackets with white rectangle more right than down"
                ,300,400);
        
        // Get and set 3D pixels
        EzImage image19 = new EzImage("jackets.jpg");
        EzImage image20 = image19.copyToGrayScale();
        int[][][] pixels3d = image19.getPixels3D();
        System.out.println("Image 19: num bands returned (should be 3) = " +
                pixels3d[0][0].length);
        for (int i=100; i < pixels3d.length; i++)
            for (int j=300; j < pixels3d[0].length; j++)
                pixels3d[i][j][2] = 255;
        image19.setPixels(pixels3d);
        image19.show(
            "Jackets with blue way up in rectangle more right than down",0,0);
        pixels3d = image20.getPixels3D();
        System.out.println("Image 20: num bands returned (should be 1) = " +
                pixels3d[0][0].length);
        for (int i=100; i < pixels3d.length; i++)
            for (int j=300; j < pixels3d[0].length; j++)
                pixels3d[i][j][0] = 255;
        image20.setPixels(pixels3d);
        image20.show("Gray jackets with white rectangle more right than down"
                ,0,400);
        
        // Try saving   
        image20.save("boxjackets.jpg","JPEG");
        
    }
}