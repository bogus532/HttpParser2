package com.androidtest.HttpParser2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContentsActivity extends Activity {
	private static final String TAG = "ContentsActivity";
	String address_replace = "http://clien.career.co.kr/cs2/";
	
	BitmapFactory.Options options = new BitmapFactory.Options();
	
	ContentsItem contentitem;
	
	ProgressDialog mDialog;
	Intent intent;
	String intent_link;
	String intent_title;
	
	private final int DYNAMIC_VIEW_ID = 0x8000;
	private LinearLayout dynamicLayout;
	TextView textTitle;
	TextView textId;
	TextView textDate;
	TextView textContents;
	
	Display display;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contents);
        
        textTitle = (TextView)this.findViewById(R.id.textTitle);
        textId = (TextView)this.findViewById(R.id.textId);
        textDate = (TextView)this.findViewById(R.id.textDate);
        textContents = (TextView)this.findViewById(R.id.textContents);
        textContents.setAutoLinkMask(Linkify.WEB_URLS);
        textContents.setLinksClickable(true);
        
        dynamicLayout = (LinearLayout)this.findViewById(R.id.dynamicArea);
        
        intent = getIntent();
        intent_link = intent.getExtras().getString("Link").toString();
        intent_title = intent.getExtras().getString("Title").toString();
        setTitle(intent_title);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        Context context = getApplicationContext();
        display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        
        Log.d(TAG,"Link URL : "+intent_link);
        
        /*
        // Non Thread
        try {
        	buildTagList();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//*/        
      //Thread
        setProgressDlg();
		new parseHtml().execute();
	}
	
	private void setProgressDlg()
    {
    	mDialog = new ProgressDialog(this);
		mDialog.setMax(100);
		mDialog.setMessage("Please wait....");
		mDialog.setIndeterminate(false);
		//mDialog.onKeyDown(keyCode, event)
    }
	
	private int buildTagList() throws MalformedURLException,IOException {
		
		String title = "",author = "";
		String content_str = "";
		String strDate = "";
		String h3Str = "",h4Str = "";
		String temp ="";
		Date date = null;
		
		int result=0;
		int index = 0;
		
		//Log.d(TAG,"buildTagList"+",Link : "+intent_link);
		
		Source source = new Source(new URL(intent_link));
		
		source.fullSequentialParse();
		
		//Title
		List<Element> h3tags = source.getAllElements(HTMLElementName.H3);
		Log.d(TAG,"h3tags.size : "+h3tags.size());
		for (int i = 0; i < h3tags.size(); i++) {

			Element h3Element = (Element) h3tags.get(i);
			h3Str = h3Element.getTextExtractor().toString();
			Log.d(TAG,i+" - H3 Tag : "+h3Element.getTextExtractor().toString());
			result += 1;
		}
		
		List<Element> h4tags = source.getAllElements(HTMLElementName.H4);
		
		for (int i = 0; i < h4tags.size(); i++) {

			Element h4Element = (Element) h4tags.get(i);
			h4Str = h4Element.getTextExtractor().toString();
			Log.d(TAG,i+" - H4 Tag : "+h4Element.getTextExtractor().toString());
			result += 1;
		}
		
		title = h3Str + h4Str;
		
		//Contents
		List<Element> contenttags = source.getAllElementsByClass("view_content");
		
		for (int i = 0; i < contenttags.size(); i++) {

			Element contentElement = (Element) contenttags.get(i);
			String cStr = contentElement.toString();
			Log.d(TAG,i + " : cStr : "+cStr);
			
			List<Element> ptags = contentElement.getAllElements(HTMLElementName.P);
			List<Element> divtags = contentElement.getAllElements(HTMLElementName.DIV);
			List<Element> spantags = contentElement.getAllElements(HTMLElementName.SPAN);
			List<Element> attachimgtags = contentElement.getAllElementsByClass("attachedImage");
			
			Log.d(TAG,i+": ptags : "+ptags.size()+", divtags : "+divtags.size()+", imgtags : "+attachimgtags.size());
			
			
			if(attachimgtags.size() > 0)
			{
				for(int x = 0; x < attachimgtags.size();x++)
				{
					Element attachimgElement = (Element)attachimgtags.get(x);
					List<Element> imgtags = attachimgElement.getAllElements(HTMLElementName.IMG);
					
					for(int z = 0; z < imgtags.size(); z++)
					{
						Element imgElement = (Element) imgtags.get(z);
						temp = imgElement.getAttributeValue("src");
						temp = temp.replace("../", "");
						temp = "<img src=\""+address_replace + temp+"\"/>";
						//temp = address_replace + temp;
						Log.d(TAG,x + " : imgtags : "+temp);
						content_str += temp;
						content_str += "\n";
						//z = imgtags.size();
					}
				}
			}
			
			 
			if(ptags.size()>1)
			{
				for(int x = 0; x < ptags.size();x++)
				{
					Element pElement = (Element) ptags.get(x);
					temp = pElement.getTextExtractor().toString();
					//temp = pElement.toString();
					content_str += temp;
					content_str += "\n";
					Log.d(TAG,x + " : p contents : "+temp);
				}
			}
			else
			{
				for(int x = 0; x < spantags.size();x++)
				{
					Element spanElement = (Element) spantags.get(x);
					temp = spanElement.getTextExtractor().toString();
					//temp = spanElement.toString();
					content_str += temp;
					Log.d(TAG,x + " : span contents : "+temp);
				}
			}
			//*/
			
		}

		/*
		
		List<Element> spantags = source.getAllElementsByClass("view_content");
		
		Log.d(TAG," - SPAN Tag size : "+spantags.size());

		for(int x = 0; x < spantags.size();x++)
		{
			Element spanElement = (Element) spantags.get(x);
			String spanStr = spanElement.toString();
			Log.d(TAG,x+" - spanStr : "+spanStr);
						
			content_str += spanElement.getContent().toString();
			
			content_str = content_str.replaceAll("<span (.*?)>","");
			content_str = content_str.replaceAll("<div (.*?)>","");
			content_str = content_str.replaceAll("<p (.*?)>","");
			content_str = content_str.replaceAll("<font (.*?)>","");
			content_str = content_str.replaceAll("<div>","");
			content_str = content_str.replaceAll("</div>","");
			content_str = content_str.replaceAll("</span>","");
			content_str = content_str.replaceAll("</font>","");
			content_str = content_str.replaceAll("<p>","");
			content_str = content_str.replaceAll("&nbsp;","");
			content_str = content_str.replaceAll("</p>","\n");
			content_str = content_str.replaceAll("<br />","");
			content_str = content_str.replaceAll("<a (.*?)>","");
			content_str = content_str.replaceAll("<a href=\"(.*?)\">","");
			content_str = content_str.replaceAll("<A HREF=\"(.*?)\" TARGET='(.*?)'>","");
			content_str = content_str.replaceAll("</A>","");
			content_str = content_str.replaceAll("</a>","");
			content_str = content_str.replaceAll("<img (.*?)>","");
			content_str = content_str.replaceAll("&gt;",">");
			content_str = content_str.replaceAll("<!-- (.*?) -->","");
				
				//content_str += spanElement.getTextExtractor().toString();
				
			//	x = spantags.size();
			
		}
		//*/
				
		//author
		List<Element> divtags = source.getAllElements(HTMLElementName.DIV);
		for (int i = 0; i < divtags.size(); i++) {
			Element divElement = (Element) divtags.get(i);
			List<Element> ptags2 = divElement.getAllElements(HTMLElementName.P);
			for(int x = 0; x < ptags2.size();x++)
			{
				Element pElement2 = (Element) ptags2.get(x);
				String pStr = pElement2.toString();
				
				if(pStr.contains("user_info"))
				{
					author = pElement2.getTextExtractor().toString();
					x = ptags2.size();
					i = divtags.size();
					result +=1;
				}
				//Log.d(TAG,i+" : "+x+" - Div Tag author : "+author);
			}

		}
		
		//Date
		for (int i = 0; i < divtags.size(); i++) {
			Element divElement = (Element) divtags.get(i);
			List<Element> ptags3 = divElement.getAllElements(HTMLElementName.P);
			for(int x = 0; x < ptags3.size();x++)
			{
				Element pElement3 = (Element) ptags3.get(x);
				String pStr = pElement3.toString();
				
				if(pStr.contains("post_info"))
				{
					temp = pElement3.getTextExtractor().toString();
					temp.replaceAll(", Hit : [0-9]* , Vote : [0-9]*", "");
					//Log.d(TAG,temp);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					try {
						date = sdf.parse(temp);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					strDate = sdf.format(date);
					x = ptags3.size();
					i = divtags.size();
					result +=1;
				}
				
				//Log.d(TAG,i+" : "+x+" - Div Tag date : "+strDate);
			}

		}
		
		ContentsItem ci  = new ContentsItem(title,author,strDate,content_str);
		
		setContentsItem(ci);
		Log.d(TAG,"Last Title : "+content_str);
		Log.d(TAG,"result : "+result);
		return result;
		
	}
	
	public class ImageGetter implements Html.ImageGetter {
		public Drawable getDrawable(String source) {
			Bitmap imgBitmap = null;
	        Bitmap resized = null;
	        Drawable drawable = null;
	        
	        options.inSampleSize =16;
	                
	        try
	        {
	        	URL url = new URL(source);
	        	URLConnection conn = url.openConnection();
	        	conn.connect();
	        	
	        	int nSize = conn.getContentLength();
	        	Log.d("HttpItemParser","nSize : "+nSize);
	        	BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
	        	
	        	FileOutputStream fileout = new FileOutputStream(new File(
                        Environment.getExternalStorageDirectory()
                                        .getAbsolutePath()
                                        + "/test.jpg"));
	        	
	        	imgBitmap = BitmapFactory.decodeStream(bis);
	        	int imgheight = imgBitmap.getHeight();
	        	int imgwidth = imgBitmap.getWidth();
	        	Log.d(TAG,"height : "+imgheight+", width : "+imgwidth);
	        	resized = Bitmap.createScaledBitmap(imgBitmap, display.getWidth(), display.getHeight()/2, true);
	        	
	        	resized.compress(CompressFormat.JPEG, 100, fileout);
	        	
	        	fileout.flush();
                fileout.close();
                drawable = Drawable.createFromPath(Environment
                                .getExternalStorageDirectory().getAbsolutePath()
                                + "/test.jpg");
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                                .getIntrinsicHeight());	        	
	        	
	        	bis.close();
	        }
	        catch (Exception e)
	        {
	        	e.printStackTrace();
	        } finally {
	        	
	        }
            return drawable;
    }
	}

	public ContentsItem setContentsItem(ContentsItem ci)
	{
		contentitem = ci;
		return contentitem;
	}
	
	private void setLayout()
	{
		textTitle.setText(contentitem._Title);
		textId.setText(contentitem._Id);
		textDate.setText(contentitem._Date);
		//textContents.setText(contentitem._Contents);
		textContents.setText(Html.fromHtml(contentitem._Contents,new ImageGetter(), null));
	}
    
    private class parseHtml extends AsyncTask<Void, Integer, Integer> {    	
    	   
 		@Override
		protected void onPreExecute() {
				
			super.onPreExecute();
			
			mDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... arg0) {

			int result = 0,count=0;

			while(result == 0)
			{
				try {
					result = buildTagList();
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
					result = 6;
				} catch (IOException e) {
					e.printStackTrace();
					result = 7;
				} catch (Exception e) {
					e.printStackTrace();
					result = 8;
				}
				Log.d(TAG,"result : "+result+", count : "+count);
				count++;
				if(count > 5)
				{
					return result = 0;
				}
			}
			return result;
		}  
    	
     	@Override
    	protected void onProgressUpdate(Integer... progress) {
 
    	}
  
    	@Override
    	protected void onPostExecute(Integer result) {
    		super.onPostExecute(result);
    		if(result > 0 && result <= 5)
    		{
    			setLayout();
    		}
    		else if(result >= 6 || result == 0)
    		{
    			Toast.makeText(ContentsActivity.this, "Error", Toast.LENGTH_SHORT).show();
    			onBackPressed();
    		}
			mDialog.dismiss(); 
			
    	}
  
    	@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		  	
    }

}
