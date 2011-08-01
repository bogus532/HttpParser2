package com.androidtest.HttpParser2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContentsActivity extends Activity {
	private static final String TAG = "ContentsActivity";
	String address_replace = "http://clien.career.co.kr/cs2/";
	
	ContentsItem contentitem;
	
	ProgressDialog mDialog;
	Intent intent;
	String intent_link;
	
	private final int DYNAMIC_VIEW_ID = 0x8000;
	private LinearLayout dynamicLayout;
	TextView textTitle;
	TextView textId;
	TextView textDate;
	TextView textContents;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contents);
        
        textTitle = (TextView)this.findViewById(R.id.textTitle);
        textId = (TextView)this.findViewById(R.id.textId);
        textDate = (TextView)this.findViewById(R.id.textDate);
        textContents = (TextView)this.findViewById(R.id.textContents);
        
        dynamicLayout = (LinearLayout)this.findViewById(R.id.dynamicArea);
        
        intent = getIntent();
        intent_link = intent.getExtras().getString("Link").toString();
        
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
		String strDate = "",strImg = "";
		String h3Str = "",h4Str = "";
		String temp ="";
		Date date = null;
		
		int result=0;
		
		Log.d(TAG,"buildTagList"+",Link : "+intent_link);
		
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
		/*
		List<Element> contenttags = source.getAllElements(HTMLElementName.DIV);
		
		for (int i = 0; i < contenttags.size(); i++) {

			Element contentElement = (Element) contenttags.get(i);
			String cStr = contentElement.toString();
			Log.d(TAG,i + " : cStr : "+cStr);
			if(cStr.contains("resContents"))
			{
				List<Element> ptags = contentElement.getAllElements(HTMLElementName.P);
				Log.d(TAG,i+" - P Tag size : "+ptags.size());
				
				for(int x = 0; x < ptags.size();x++)
				{
					Element pElement = (Element) ptags.get(x);
					String pStr = pElement.toString();
					Log.d(TAG,x+" - pStr : "+pStr);
					if(pStr.contains("<p")&&!pStr.contains("<p class="))
					{
						content_str += pElement.getTextExtractor().toString();
						content_str +="\n";
					}
				}
				i = contenttags.size();
			}
		}

		//*/
		///*
		List<Element> contenttags = source.getAllElements(HTMLElementName.DIV);
		
		for (int i = 0; i < contenttags.size(); i++) {

			Element contentElement = (Element) contenttags.get(i);
			String cStr = contentElement.toString();
			Log.d(TAG,i + " : cStr : "+cStr);
			if(cStr.contains("resContents"))
			{
				List<Element> spantags = contentElement.getAllElements(HTMLElementName.SPAN);
				Log.d(TAG,i+" - SPAN Tag size : "+spantags.size());
				
				for(int x = 0; x < spantags.size();x++)
				{
					Element spanElement = (Element) spantags.get(x);
					String spanStr = spanElement.toString();
					Log.d(TAG,x+" - spanStr : "+spanStr);
					if(spanStr.contains("writeContents"))
					{
						
						content_str += spanElement.getContent().toString();

						content_str = content_str.replaceAll("<p>","");
						content_str = content_str.replaceAll("&nbsp;","\n");
						content_str = content_str.replaceAll("</p>","");
						content_str = content_str.replaceAll("<br />","");
						content_str = content_str.replaceAll("<a href=\"*\">","");
						x = spantags.size();
					}
				}
				i = contenttags.size();
			}
		}

		//*/
		/*
		List<Element> contenttags = source.getAllElements(HTMLElementName.DIV);
		for (int i = 0; i < contenttags.size(); i++) {
			Element contentElement = (Element) contenttags.get(i);
			String cStr = contentElement.toString();
			Log.d(TAG,i+" : "+cStr);
			if(cStr.contains("resContents"))
			{
				content_str = contentElement.getTextExtractor().toString();
				i=contenttags.size();
			}
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
				Log.d(TAG,i+" : "+x+" - Div Tag author : "+author);
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
					Log.d(TAG,temp);
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
				
				Log.d(TAG,i+" : "+x+" - Div Tag date : "+strDate);
			}

		}
		
		ContentsItem ci  = new ContentsItem(title,author,strDate,content_str);
		
		setContentsItem(ci);
		Log.d(TAG,"result : "+result);
		return result;
		
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
		textContents.setText(contentitem._Contents);
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
			//Log.d(TAG,"doInBackground");
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
