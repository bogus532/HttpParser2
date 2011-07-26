package com.androidtest.HttpParser2;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HttpItemAdapter extends ArrayAdapter<HttpItem> {
	private static final String TAG = "HttpItemAdapter";
	
	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	Context context_this;
	
	BitmapFactory.Options options = new BitmapFactory.Options();
	
    private ArrayList<HttpItem> items;

    public HttpItemAdapter(Context context, int textViewResourceId, ArrayList<HttpItem> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            context_this = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
                        
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)context_this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
            }
            HttpItem b = items.get(position);
            if (b != null) {
	            	TextView tvContents = (TextView) v.findViewById(R.id.tvContents);
	                TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
	                TextView tvAuthor = (TextView) v.findViewById(R.id.tvAuthor);
	                ImageView ivGiflink = (ImageView) v.findViewById(R.id.ivDec);
                    
	                tvContents.setTextSize(15);
                    tvContents.setText(this.getItem(position).toString());
                    tvContents.setAutoLinkMask(Linkify.WEB_URLS);
                    tvContents.setLinksClickable(true);
    				
                    if (tvContents != null){
                    	tvContents.setText(b.getTitle());                            
                    }
                    
                    if(tvDate != null){
                    	tvDate.setTextColor(Color.GRAY);
                    	tvDate.setText(b.getDateToString());
                    }
                    
                    if(tvAuthor != null){
                    	tvAuthor.setTextColor(Color.GRAY);
                    	tvAuthor.setText(b.getAuthor());
                    }
                    
                    if(ivGiflink != null)
                    {
                    	if(b.getGiflink()!=null)
                    	{
                    		Log.d(TAG, "Position : "+position +", URL : " + b.getGiflink());
							Bitmap bm = GetImageFromURL(b.getGiflink());
							ivGiflink.setImageBitmap(bm);
							//Log.d(TAG, "URL : " + b.getGiflink());
							// imageDownloader.download(b.getGiflink(), ivGiflink);
							// httpItemImageDownloader.download(b.getGiflink(),
							// ivDec,tvImagLink);
                    	}
                    }
            }
            return v;
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
        	//Log.d(TAG,"nSize : "+nSize);
        	BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
        	
        	imgBitmap = BitmapFactory.decodeStream(bis);
        	resized = Bitmap.createScaledBitmap(imgBitmap, 40, 10, true);
        	        	
        	bis.close();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
        } finally {
        	
        }
        
        return resized;
    }
    
 }
