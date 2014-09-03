package com.droison.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
	private static final String TAG = "ImageUtils";
    private static int DeviceWidth;
    private static int DeviceHeight;

	/**
	 * Stores an image on the storage
	 * 
	 * @param image
	 *            the image to store.
	 * @param pictureFile
	 *            the file in which it must be stored
	 */
	public static void storeImage(Bitmap image, File pictureFile) {
		if (pictureFile == null) {
			Log.d(TAG, "Error creating media file, check storage permissions: ");
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			image.compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.close();
		} catch (FileNotFoundException e) {
			Log.d(TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			Log.d(TAG, "Error accessing file: " + e.getMessage());
		}
	}

	/**
	 * Get the screen height.
	 * 
	 * @param context
	 * @return the screen height
	 */
	public static int getScreenHeight(Activity context) {
        if (DeviceHeight == 0)
        {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            DeviceWidth = size.y;
        }
        return DeviceHeight;
	}

	/**
	 * Get the screen width.
	 * 
	 * @param context
	 * @return the screen width
	 */
	public static int getScreenWidth(Activity context) {
        if(DeviceWidth==0){
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            DeviceWidth = size.x;
        }
        return  DeviceWidth;
    }
}
