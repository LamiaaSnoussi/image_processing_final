package com.example.lsnoussi.img_processing;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lsnoussi on 07/03/18.
 */

public class Convolution {

    /**
     * take a bitmap as a parameter.
     *  function to blur using kernel matrix
     * @param bmp
     * @return Bitmap
     */

    public static Bitmap moyenneur(Bitmap bmp) {
        long start = System.currentTimeMillis();

        int[][] Matrix = new int[3][3];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                Matrix[i][j] = 1;
            }
        }
        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());

        int width = bmp.getWidth();
        int height = bmp.getHeight();



        int sumR, sumG, sumB = 0;
        int[] pixels = new int [width*height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);



        for (int x = 1; x < width - 1; ++x) {
            for (int y = 1; y < height - 1; ++y) {

                sumR = sumG = sumB = 0;

                int index=0;

                for (int u = -1; u <= 1; ++u) {
                    for (int v = -1; v <= 1; ++v) {
                        index = (y+v)*width +(x+u);
                        sumR += Color.red(pixels[index]) * Matrix[u + 1][v + 1];
                        sumG += Color.green(pixels[index]) * Matrix[u + 1][v + 1];
                        sumB += Color.blue(pixels[index]) * Matrix[u + 1][v + 1];
                    }
                }

                sumR = sumR / 9;

                sumG = sumG / 9;

                sumB = sumB / 9;

                pixels[index] =  Color.rgb(sumR, sumG, sumB);

            }
        }

        result.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return result;


    }

       /**
     *  @param bmp
        *  @return Bitmap
     * take a bitmap as a parameter.
     *  function to blur using gauss matrix */


    public static Bitmap gaussConvolution(Bitmap bmp) {
        long start = System.currentTimeMillis();


        int[][] Matrix = new int[][]{
                {1, 2, 1},
                {2, 4, 2},
                {1, 2, 1}
        };

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());

        int sumR, sumG, sumB = 0;

        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);


        for (int x = 1; x < width - 1; ++x) {
            for (int y = 1; y < height - 1; ++y) {

                sumR = sumG = sumB = 0;
                int index = 0;


                for (int u = -1; u <= 1; ++u) {
                    for (int v = -1; v <= 1; ++v) {

                        index = (y + v) * width + (x + u);
                        sumR += Color.red(pixels[index]) * Matrix[u + 1][v + 1];
                        sumG += Color.green(pixels[index]) * Matrix[u + 1][v + 1];
                        sumB += Color.blue(pixels[index]) * Matrix[u + 1][v + 1];
                    }
                }


                sumR = sumR / 16;

                sumG = sumG / 16;

                sumB = sumB / 16;


                pixels[index] = Color.rgb(sumR, sumG, sumB);

            }
        }

        result.setPixels(pixels, 0, width, 0, 0, width, height);

        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return result;

    }

    /**
     * Sobel
     * @param bmp
     * @return
     */

    public static Bitmap edgeDetection(Bitmap bmp){
        long start = System.currentTimeMillis();

        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());
        int h = bmp.getHeight();
        int w = bmp.getWidth();
        int[] pixels1 = new  int [w*h];
        Effects.toGray(bmp);
        int[] pixels2 = new int [w*h];
        bmp.getPixels(pixels1, 0, w,  0, 0, w, h);
        bmp.getPixels(pixels2, 0, w,  0, 0, w, h);
        int[] X = {-1,-1,-1,0,0,0,1,1,1};
        int[] Y = {-1,0,1,-1,0,1,-1,0,1};
        int[] currentPixel = new  int [9];
        for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j< h - 1; j++) {
                int sX = 0;
                int sY = 0;
                for (int m = 0; m < 3; m++) {
                    currentPixel[3*m] = Color.red(pixels1[i - 1 + (j+m-1)*w]);
                    currentPixel[3*m+1] = Color.red(pixels1[i + (m-1+j)*w]);
                    currentPixel[3*m+2] = Color.red(pixels1[i + 1 + (m+j-1)*w]);
                }
                for (int n = 0; n < 9; n++) {
                    sX += currentPixel[n] * X[n];
                    sY += currentPixel[n] * Y[n];
                }
                int norm = (int) Math.min(Math.sqrt(sX*sX + sY*sY), 255);
                pixels2[i + j*w] = Color.rgb(norm, norm, norm);
            }
        }
        result.setPixels(pixels2, 0, w,  0, 0, w, h);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return result;
    }

    /**
     *
     * @param bmp
     * @return
     */

    public static Bitmap laplaceFilter(Bitmap bmp){

        Bitmap result = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        result.setDensity(bmp.getDensity());
        int[] pixel = new int[bmp.getWidth()*bmp.getHeight()];
        int  masque [] []= new int[][]{{-2, -1,0}, {-1, 1, 1}, {0, 1, 2}};
        bmp.getPixels(pixel,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        int[] tab = pixel;
        int n = masque.length/2;
        for(int i = n; i<bmp.getWidth() - n; i++){
            for(int j = n; j<bmp.getHeight() - n; j++){
                tab=Laplace(masque,pixel,i,j,bmp.getWidth(), tab);
            }
        }
        result.setPixels(tab,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
        return result;
    }

    public static int[] Laplace(int[][] masque, int[] p,int i, int j, int largeur, int[] tab){
        int n = masque.length/2;
        int rr=0,gg=0,bb=0;
        for(int k = -n; k<=n; k++) {
            for (int l = -n; l <= n; l++) {
                int pp = p[(i + k)  + (j + l) * largeur];
                int r = Color.red(pp);
                int g = Color.green(pp);
                int b = Color.blue(pp);
                rr = rr + masque[k + n][l + n] * r;
                gg = gg + masque[k + n][l + n] * g;
                bb = bb + masque[k + n][l + n] * b;
            }
        }
        if(rr>0){
            rr = (int)(rr/16);
            rr = 128 + rr;
        }
        if(rr<=0){
            rr = rr + 2040;
            rr = (int) rr/16;
        }
        if(gg>0){
            gg = (int) gg/16;
            gg = 128 + gg;
        }
        if(gg<=0){
            gg = gg + 2040;
            gg = (int) gg/16;
        }
        if(bb>0){
            bb = (int) bb/16;
            bb = 128 + bb;
        }
        if(bb<=0){
            bb = bb + 2040;
            bb = (int) bb/16;
        }
        int gris = (int) (rr * 0.3 + gg * 0.59 + bb * 0.11);
        int encode = Color.rgb(gris, gris, gris);
        tab[i + j * largeur] = encode;
        return tab;



    }





}
