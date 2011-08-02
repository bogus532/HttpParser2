package com.androidtest.HttpParser2;

import java.io.BufferedInputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class HttpItemImageDownloader {
	private static final String TAG = "HttpItemImageDownloader";
	private HashMap<String,Bitmap> bitmapHashMap = new HashMap<String,Bitmap>();
	
	BitmapFactory.Options options = new BitmapFactory.Options();
	
	Bitmap bm;
	
	public void download(String url,ImageView imageView)
	{
		if (url == null) {
			return;
		}
		if(bitmapHashMap.get(url) == null)
		{
			Log.d(TAG,"Thread - Task execute");
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
			task.execute(url);
		}
		else
		{
			Log.d(TAG,"Cache - URL : "+url);
			bm = bitmapHashMap.get(url);
			imageView.setImageBitmap(bm);
		}
	}
	
	public Bitmap getImage(String url)
	{
		Bitmap getBM = null;
		if(bitmapHashMap.get(url) != null)
		{
			getBM = bitmapHashMap.get(url);
		}
		return getBM;
	}
	
	private Bitmap GetImageFromURL(String strImageURL) 
    {
        Bitmap imgBitmap = null;
        Bitmap resized = null;
        
        options.inSampleSize =16;
                
        try
        {
        	URL url = new URL(strImageURL);
        	URLConnection conn = url.openConnection();
        	conn.connect();
        	
        	int nSize = conn.getContentLength();
        	Log.d("HttpItemParser","nSize : "+nSize);
        	BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
        	
        	imgBitmap = BitmapFactory.decodeStream(bis);
        	resized = Bitmap.createScaledBitmap(imgBitmap, 100, 100, true);
        	        	
        	bis.close();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        } finally {
        	
        }
        
        bitmapHashMap.put(strImageURL, resized);
        
        return resized;
    }
	
	
	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        ImageView imgV;
                
        public BitmapDownloaderTask(ImageView _imageView) {
        	imgV = _imageView;
        }
       
        /**
         * Actual download method.
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            return GetImageFromURL(url);
        }

        /**
         * Once the image is downloaded, associates it to the imageView
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
        	imgV.setImageBitmap(bitmap);
        }
        
        @Override
    	protected void onProgressUpdate(Void... progress) {
 
    	}
	}
}
