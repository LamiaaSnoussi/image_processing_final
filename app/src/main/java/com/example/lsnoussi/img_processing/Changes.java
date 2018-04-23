package com.example.lsnoussi.img_processing;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by lsnoussi on 19/04/18.
 */

public class Changes {


    public static ArrayList<Bitmap> images = new ArrayList<>(4);
    public static int change = -1;
    int recentChange = 0;

	/**
	 * Add the filtered bitmap to images ( a bitmap array)
	 * @param bmp
	 */

	public void setChange(Bitmap bmp){
		if(change< images.size())
			change++;
			recentChange= change;
			images.add(change, bmp);
	}

	public Bitmap getLastChange(){
		if(change>0)
			change--;
		return images.get(change);
	}
	public Bitmap getNextChange(){
		if(change < recentChange)
			change++;
		return images.get(change);
	}
}
