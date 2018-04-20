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



        /*new android.os.Handler().postDelayed(new Runnable() {


       /* @Override
        public void run() {
            // This method will be executed once the timer is over
            // Start your app main activity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
           startActivity(i);


        finish();
        }
    }, 4000);*/

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
