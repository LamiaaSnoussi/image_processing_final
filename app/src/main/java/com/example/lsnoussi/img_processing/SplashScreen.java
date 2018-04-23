package com.example.lsnoussi.img_processing;

import android.content.Intent;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;


public class SplashScreen extends AppCompatActivity {


    /**
     * Showing splash screen with a timer.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_splash_screen);

       Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(4000);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        };
        thread.start();

    }

}
