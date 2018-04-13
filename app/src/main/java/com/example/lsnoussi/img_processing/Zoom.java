package com.example.lsnoussi.img_processing;

import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by lsnoussi on 06/04/18.
 */

public class Zoom {

     /**
  * function that calculates the space between two fingers on the screen
  * @param event
  * @return a float
  */

 public static float spacing(MotionEvent event) {
     float x = event.getX(0) - event.getY(1);
     float y = event.getY(0) - event.getY(1);
     float F = (x * x + y * y) * (x * x + y * y);
     return F;
 }


 /**
  * function that calculates the point in the middle between two fingers on the screen
  * @param point
  * @param event
  */
 public static void midPoint(PointF point, MotionEvent event) {
     float x = event.getX(0) + event.getY(1);
     float y = event.getY(0) + event.getY(1);
     point.set(x / 2, y / 2);
 }


 /**
  * function that shows an event in the LogCat view, for debugging
  * @param event
  */

 public static void dumpEvent(MotionEvent event) {
     String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
     StringBuilder sb = new StringBuilder();
     int action = event.getAction();
     int actionCode = action & MotionEvent.ACTION_MASK;
     sb.append("event ACTION_").append(names[actionCode]);

     if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
         sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
         sb.append(")");
     }

     sb.append("[");
     for (int i = 0; i < event.getPointerCount(); i++) {
         sb.append("#").append(i);
         sb.append("(pid ").append(event.getPointerId(i));
         sb.append(")=").append((int) event.getX());
         sb.append(",").append((int) event.getY(i));
         if (i + 1 < event.getPointerCount())
             sb.append(";");
     }

     sb.append("]");
     Log.d("Touch Events ---------", sb.toString());
 }


}
