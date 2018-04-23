package com.example.lsnoussi.img_processing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Random;

import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.Allocation;

/**
 * Created by lsnoussi on 07/03/18.
 */

public class Effects {


    /** take a bitmap as a parameter.
     *  function to gray a bitmap using a tab of pixels
     *  @param bmp
     *   @return Bitmap
     */


    public static Bitmap toGray(Bitmap bmp ) {
        long start = System.currentTimeMillis();


        int w = bmp.getWidth();
        int h = bmp.getHeight();

        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());


        int[] pixels = new int[w * h];
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);

        for (int i = 0; i < pixels.length; ++i) {
            int r = Color.red(pixels[i]);
            int g = Color.green(pixels[i]);
            int b = Color.blue(pixels[i]);
            int gray = (int) (0.3 * r + 0.59 * g + b * 0.11);
            pixels[i] = Color.rgb(gray, gray, gray);
        }

        result.setPixels(pixels, 0, w, 0, 0, w, h);

        long end = System.currentTimeMillis();
        System.out.println(end - start);

        return result;

    }

    public static  void  toGreyRS (Bitmap  bmp, Context context) {

        long start = System.currentTimeMillis();
        RenderScript  rs = RenderScript.create(context);

        Allocation  input = Allocation.createFromBitmap(rs , bmp);
        Allocation  output = Allocation.createTyped(rs , input.getType());

        ScriptC_grey  greyScript = new  ScriptC_grey(rs);

        greyScript.forEach_toGrey(input , output);

        output.copyTo(bmp);

        input.destroy ();
        output.destroy ();
        greyScript.destroy ();
        rs.destroy ();
        long end = System.currentTimeMillis();
        System.out.println(end - start);

    }



    /**
     * take a bitmap as a parameter.
     *  function to put a random colored filter on a bitmap
     *  @param bmp
     * @return Bitmap

    */

    public static Bitmap colorize (Bitmap bmp) {
        long start = System.currentTimeMillis();

        int w = bmp.getWidth();
        int h = bmp.getHeight();
        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());

        int[] pixels = new int[w * h];

        Random ran = new Random();

        // possibility for hue [0..360]
        int nbr = ran.nextInt(360);
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);

        for (int i = 0; i < h * w; ++i) {
            int p = pixels[i];
            int r = Color.red(p);
            int g = Color.green(p);
            int b = Color.blue(p);

            float[] hsv = new float[3];


            Color.RGBToHSV(r, g, b, hsv);
            hsv[0] = nbr;
            hsv[1] = 1.0f;

            pixels[i] = Color.HSVToColor(hsv);
        }

        result.setPixels(pixels, 0, w, 0, 0, w, h);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return result;


    }



    /**function that calculates the histogram of a bitmap given
     *  @param bmp
     * take a bitmap as a parameter.
     *  @return a tab filled with the numb of pixels with gray level.
    */

    public static int[] histogram(Bitmap bmp) {
        long start = System.currentTimeMillis();

        int w = bmp.getWidth();
        int h = bmp.getHeight();


        int[] hist = new int[256];
        int[] pixels = new int[h * w];

        bmp.getPixels(pixels, 0, w, 0, 0, w, h);

        for (int x = 0; x < pixels.length; ++x) {
            int R = Color.red(pixels[x]);
            int G = Color.green(pixels[x]);
            int B = Color.blue(pixels[x]);

            int gray = (int) (0.3 * R + 0.59 * G + 0.11 * B);

            hist[gray] = hist[gray] + 1;
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        return hist;

    }


    /*** take a bitmap as a parameter.
     *  function that calculates a linear transformation between [min,max] over 256 level of gray
     *  @param bmp
     *  @return Bitmap
     * */

    public static Bitmap dynamicExtension(Bitmap bmp) {
        long start = System.currentTimeMillis();


        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] pixels = new int[h * w];

        bmp.getPixels(pixels, 0, w, 0, 0, w, h);

        int r_max = 0 ;
        int g_max = 0;
        int b_max = 0 ;

        int r_min = 0 ;
        int g_min = 0;
        int b_min = 0 ;

        int R0 = Color.red(pixels[0]);
        int G0 = Color.green(pixels[0]);
        int B0 = Color.blue(pixels[0]);

        for (int i = 0; i < pixels.length; ++i) {
            if (Color.red(pixels[i])> R0) {
                r_max= Color.red(pixels[i]);
            } else if ( Color.red(pixels[i]) < R0) {
                r_min = Color.red(pixels[i]);
            }


            if (Color.green(pixels[i])> G0) {
                g_max= Color.green(pixels[i]);
            } else if ( Color.green(pixels[i]) < G0) {
                g_min = Color.green(pixels[i]);
            }


            if (Color.blue(pixels[i])> B0) {
                b_max= Color.blue(pixels[i]);
            } else if ( Color.blue(pixels[i]) < B0) {
                b_min = Color.blue(pixels[i]);
            }


        }
        // Applies linear extension of dynamics to the bitmap

        for (int i = 0; i < pixels.length; ++i) {
            int R = 255 * ((Color.red(pixels[i])) - r_min) / (r_max - r_min);
            int G = 255 * ((Color.green(pixels[i])) - g_min) / (g_max - g_min);
            int B = 255 * ((Color.blue(pixels[i])) - b_min) / (b_max - b_min);
            pixels[i] = Color.rgb(R, G, B);
        }

        bmp.setPixels(pixels, 0, w, 0, 0, w, h);

        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return bmp;


    }



    /**  take a bitmap as a parameter.
     *  function that forces the levels of gray to be organized between 0 and 255
     *  @param bmp
     *  @return Bitmap
    */

    public static Bitmap histogramEqualizationGray(Bitmap bmp) {


        long start = System.currentTimeMillis();
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());


        int[] pixels = new int[h * w];

        int[] hist = histogram(bmp);

        int[] C = new int[hist.length];
        C[0] = hist[0];
        for (int i = 1; i < hist.length; ++i) {
            C[i] = C[i - 1] + hist[i];  // histogram's sum
        }
        //equalization:
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);
        for (int i = 0; i < pixels.length; ++i) {
            int R = Color.red(pixels[i]);  // transformation of the gray level
            R = C[R] * 255 / pixels.length;
            int G = Color.green(pixels[i]);
            G = C[G] * 255 / pixels.length;
            int B = Color.blue(pixels[i]);
            B = C[B] * 255 / pixels.length;

            pixels[i] = Color.rgb(R, G, B);
        }

        result.setPixels(pixels, 0, w, 0, 0, w, h);
        result = toGray(result);
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        return result;

    }
    /**take a bitmap as a parameter.
     *  same thing as histogramEqualization_gray but without graying the bitmap first
     *  @param bmp
     *  @return Bitmap
     * */


    public static Bitmap histogramEqualizationRGB(Bitmap bmp) {
        long start = System.currentTimeMillis();

        int w = bmp.getWidth();
        int h = bmp.getHeight();
        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());
        int[] pixels = new int[h * w];



        int[] hist = histogram(bmp);
        int[] C = new int[hist.length];
        C[0] = hist[0];
        for (int i = 1; i < hist.length; ++i) {
            C[i] = C[i - 1] + hist[i];
        }
        //equalization:
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);
        for (int i = 0; i < pixels.length; ++i) {
            int R = Color.red(pixels[i]);
            R = C[R] * 255 / pixels.length;
            int G = Color.green(pixels[i]);
            G = C[G] * 255 / pixels.length;
            int B = Color.blue(pixels[i]);
            B = C[B] * 255 / pixels.length;

            pixels[i] = Color.rgb(R, G, B);
        }


        result.setPixels(pixels, 0, w, 0, 0, w, h);
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        return result;


    }

    public static Bitmap histogramEqualization(Bitmap image, Context context) {

        long start = System.currentTimeMillis();
        //Get image size
        int width = image.getWidth();
        int height = image.getHeight();

        //Create new bitmap
        Bitmap res = image.copy(image.getConfig(), true);

        //Create renderscript
        RenderScript rs = RenderScript.create(context);

        //Create allocation from Bitmap
        Allocation allocationA = Allocation.createFromBitmap(rs, res);

        //Create allocation with same type
        Allocation allocationB = Allocation.createTyped(rs, allocationA.getType());

        //Create script from rs file.
        ScriptC_histEq histEqScript = new ScriptC_histEq(rs);

        //Set size in script
        histEqScript.set_size(width*height);

        //Call the first kernel.
        histEqScript.forEach_root(allocationA, allocationB);

        //Call the rs method to compute the remap array
        histEqScript.invoke_createRemapArray();

        //Call the second kernel
        histEqScript.forEach_remaptoRGB(allocationB, allocationA);

        //Copy script result into bitmap
        allocationA.copyTo(res);

        //Destroy everything to free memory
        allocationA.destroy();
        allocationB.destroy();
        histEqScript.destroy();
        rs.destroy();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return res;
    }


    /**
     * it increases the pixels of a color chosen and with a given percentage
     * @author https://xjaphx.wordpress.com/
     * @param bmp
     * @param type
     * @param percent
     * @return Bitmap
     */

    public static Bitmap boost(Bitmap bmp, int type, float percent) {

        long start = System.currentTimeMillis();
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());

        int A, R, G, B;
        int pixel;

        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = bmp.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                if(type == 1) {
                    R = (int)(R * (1 + percent));
                    if(R > 255) R = 255;
                }
                else if(type == 2) {
                    G = (int)(G * (1 + percent));
                    if(G > 255) G = 255;
                }
                else if(type == 3) {
                    B = (int)(B * (1 + percent));
                    if(B > 255) B = 255;
                }
                result.setPixel(x, y, Color.argb(A, R, G, B));

            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
      return result;
}

    /**function decreasing the color depth of the image giving it a cartoon effect by converting colors to standard values with 64 bit offset
     * @author https://xjaphx.wordpress.com/
     * @param bmp
     * @return Bitmap
     *
     */
        public static Bitmap decreaseColorDepth(Bitmap bmp) {

             long start = System.currentTimeMillis();
            // get image size
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
            result.setDensity(bmp.getDensity());
            // color information
            int R, G, B;
            int pixel;

            // scan through all pixels
            for(int x = 0; x < width; ++x) {
                for(int y = 0; y < height; ++y) {
                    // get pixel color
                    pixel = bmp.getPixel(x, y);
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);

                    // round-off color offset
                    R = ((R + (64 / 2)) - ((R + (64 / 2)) % 64) - 1);
                    if(R < 0) { R = 0; }
                    G = ((G + (64 / 2)) - ((G + (64/ 2)) % 64) - 1);
                    if(G < 0) { G = 0; }
                    B = ((B + (64 / 2)) - ((B + (64 / 2)) %64 - 1));
                    if(B < 0) { B = 0; }

                    // set pixel color to output bitmap
                    result.setPixel(x, y, Color.rgb( R, G, B));

                }
            }
            long end = System.currentTimeMillis();
            System.out.println(end - start);
            return result;
}

    /**
     * take a bitmap as a parameter.
     * function that brighten a picture using an histogram for every R,G,B
     * @param bmp
     * @return Bitmap
     *
     */

    public static Bitmap overExposure(Bitmap bmp) {

        long start = System.currentTimeMillis();
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());
        int[] pixels = new int[h * w];


        int[] hist_red = new int[256];
        int[] hist_green = new int[256];
        int[] hist_blue = new int[256];
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);
        int coeff = 10;

        for (int i = 0; i < pixels.length; ++i) {
            hist_red[(Color.red(pixels[i]) * 255 - coeff )/ 255] += 1;
            hist_green[(Color.green(pixels[i]) * 255 - coeff )/ 255] += 1;
            hist_blue[(Color.blue(pixels[i]) * 255 - coeff) / 255] += 1;
        }

        int[] C_red = new int[hist_red.length];
        int[] C_green = new int[hist_green.length];
        int[] C_blue = new int[hist_blue.length];


        C_red[0] = hist_red[0];
        C_green[0] = hist_green[0];
        C_blue[0] = hist_blue[0];


        for (int i = 1; i < hist_red.length; ++i) {
            C_red[i] = C_red[i - 1] + hist_red[i];
            C_green[i] = C_green[i - 1] + hist_green[i];
            C_blue[i] = C_blue[i - 1] + hist_blue[i];
        }
        //equalization:

        for (int i = 0; i < pixels.length; ++i) {
            int R = Color.red(pixels[i]);
            R = C_red[R] * 255 / pixels.length;
            int G = Color.green(pixels[i]);
            G = C_green[G] * 255 / pixels.length;
            int B = Color.blue(pixels[i]);
            B = C_blue[B] * 255 / pixels.length;

            pixels[i] = Color.rgb(R, G, B);
        }

        result.setPixels(pixels, 0, w, 0, 0, w, h);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return result;

    }

    /**
     * Function  sepia effect that changes the every canal to gray ,
     * then we add 94 to the red pixel , 38 to the green pixel and  18 to the blue pixel
     *
     * @param bmp
     * @return Bitmap
     */

    public static Bitmap sepia(Bitmap bmp) {

        long start = System.currentTimeMillis();
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());
        int R, G, B;

        int[] pixels = new int[h * w];
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);

        for (int i = 0; i < pixels.length; i++) {

            R = Color.red(pixels[i]);
            G = Color.green(pixels[i]);
            B = Color.blue(pixels[i]);
            B = G = R = (int) (0.3 * R + 0.59 * G + 0.11 * B);

            // Apply it for each canal's color
            R += 94;
            if (R > 255) {
                R = 255;
            }

            G += 38;
            if (G > 255) {
                G = 255;
            }

            B += 18;
            if (B > 255) {
                B = 255;
            }
            pixels[i] = Color.rgb(R, G, B);
        }

        result.setPixels(pixels, 0, w, 0, 0, w, h);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return result ;
    }

    /**
     *function that decreases/increases the brightness of a bitmap
     * @param bmp
     * @param value
     * @return Bitmap
     *  @author https://xjaphx.wordpress.com/
     */

    public static Bitmap brightness(Bitmap bmp, int value) {

        long start = System.currentTimeMillis();
        // image size
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = bmp.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // increase/decrease each channel
                R += value;
                if (R > 255) {
                    R = 255;
                } else if (R < 0) {
                    R = 0;
                }

                G += value;
                if (G > 255) {
                    G = 255;
                } else if (G < 0) {
                    G = 0;
                }

                B += value;
                if (B > 255) {
                    B = 255;
                } else if (B < 0) {
                    B = 0;
                }

                // apply new pixel color to output bitmap
                result.setPixel(x, y, Color.argb(A, R, G, B));

            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    return result;
    }

    /**
     * function that decreases/increases the contrast of a bitmap
     * @param bmp
     * @param value
     * @return Bitmap
     *  @author https://xjaphx.wordpress.com/
     */

      public static Bitmap contrast(Bitmap bmp,double value){
        long start = System.currentTimeMillis();

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());

        int []pixels = new int[width*height];

        double contrast = Math.pow((100+value)/100,2);

        bmp.getPixels(pixels,0,width,0,0,width,height);

        for (int i=0; i < pixels.length;i++) {

            int R = Color.red(pixels[i]);
            int G = Color.green(pixels[i]);
            int B = Color.blue(pixels[i]);

            R = (int) (((((R/255.0)-0.5)* contrast) + 0.5) * 255.0);
            if (R<0) { R=0;}
            else if (R>255) { R = 255;}


            G = (int) (((((G/255.0)-0.5)* contrast) + 0.5) * 255.0);
            if (G<0) { G=0;}
            else if (G>255) { G = 255;}

            B = (int) (((((B/255.0)-0.5)* contrast) + 0.5) * 255.0);
            if (B<0) { B=0;}
            else if (B>255) { B = 255;}

            pixels[i] = Color.rgb(R,G,B);

        }

        bmp.setPixels(pixels,0,width,0,0,width,height);

        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return bmp;
}
    /**
     * @param bmp
     * gives the bitmap a hand drawing effect
     * @return Bitmap
     * @author https://xjaphx.wordpress.com/
     *
     */

    public static Bitmap sketch(Bitmap bmp) {
        long start = System.currentTimeMillis();

        int type = 6;
        int threshold = 20;

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Bitmap bmp2 = toGray(bmp);
        Bitmap result = Bitmap.createBitmap(width, height, bmp.getConfig());
        result.setDensity(bmp.getDensity());
        int A, R, G, B;
        int sumR, sumG, sumB;
        int[][] pixels = new int[3][3];

        for(int y = 0; y < height - 2; ++y) {
            for(int x = 0; x < width - 2; ++x) {

                //      get pixel matrix
                for(int i = 0; i < 3; ++i) {
                    for(int j = 0; j < 3; ++j) {
                        pixels[i][j] = bmp2.getPixel(x + i, y + j);
                    }
                }

                // get alpha of center pixel
                A = Color.alpha(pixels[1][1]);

                // init color sum
                sumR = sumG = sumB = 0;
                sumR = (type*Color.red(pixels[1][1])) - Color.red(pixels[0][0]) - Color.red(pixels[0][2]) - Color.red(pixels[2][0]) - Color.red(pixels[2][2]);
                sumG = (type*Color.green(pixels[1][1])) - Color.green(pixels[0][0]) - Color.green(pixels[0][2]) - Color.green(pixels[2][0]) - Color.green(pixels[2][2]);
                sumB = (type*Color.blue(pixels[1][1])) - Color.blue(pixels[0][0]) - Color.blue(pixels[0][2]) - Color.blue(pixels[2][0]) - Color.blue(pixels[2][2]);

                // get final Red
                R = (int)(sumR  + threshold);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                // get final Green
                G = (int)(sumG  + threshold);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                // get final Blue
                B = (int)(sumB  + threshold);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B));
            }
        }

        long end = System.currentTimeMillis();
         System.out.println("sketch");
        System.out.println(end - start);

        return result;
    }


     /**
     * Function that invert a given bitmap's color
     * @param bmp
     * @return Bitmap
     *
     */
    public static Bitmap invert(Bitmap bmp) {
        long start = System.currentTimeMillis();

        int w = bmp.getWidth();
        int h = bmp.getHeight();


        int[] pixels = new int[h * w];
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);

        for (int i = 0; i < pixels.length; i++) {
            int R = 255 - Color.red(pixels[i]);
            int G = 255 - Color.green(pixels[i]);
            int B = 255 - Color.blue(pixels[i]);

            pixels[i] = Color.rgb(R, G, B);
        }

        bmp.setPixels(pixels, 0, w, 0, 0, w, h);

        long end = System.currentTimeMillis();
        System.out.println(end - start);

        return bmp;
    }

}








