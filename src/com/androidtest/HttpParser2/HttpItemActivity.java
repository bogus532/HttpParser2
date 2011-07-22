package com.androidtest.HttpParser2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HttpItemActivity extends Activity {
	private static final String TAG = "HttpItemActivity";
	
	ListView httpItemListView;
	ProgressDialog mDialog;
	
	HttpItemAdapter aa;
	ArrayList<HttpItem> HttpItemArray = new ArrayList<HttpItem>();
	HttpItem selectedhttpitem;
	
	Intent intent;
	String intent_link;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httpitemactivity);
        
        intent = getIntent();
        intent_link = intent.getExtras().getString("Link").toString();
        
        Log.d(TAG,intent_link);
        
        httpItemListView = (ListView)this.findViewById(R.id.httpItemListView);
        
        httpItemListView.setOnItemClickListener(new OnItemClickListener () {
        	
 			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				
 				selectedhttpitem = HttpItemArray.get(index);
  
 				if(selectedhttpitem != null)
 				{
 					
 					String uridata = selectedhttpitem.getLink();
 					
 					if(uridata == null)
 					{
 						Toast.makeText(HttpItemActivity.this, R.string.data_null, Toast.LENGTH_SHORT).show();
 						return;
 					}
 					
  					Log.d(TAG,"Link URL : "+uridata);
 					
	 				Uri uri = Uri.parse(uridata);
	 				Intent intent  = new Intent(Intent.ACTION_VIEW,uri);
	 				startActivity(intent);
	 				
  				}
				
			}
        	
        });
       
        /*
        try {
        	buildTagList();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
        
        //setProgressDlg();
		
		//new parseHtml().execute();
        
        aa = new HttpItemAdapter(this,R.layout.row,HttpItemArray);
		        
        httpItemListView.setAdapter(aa);
		
    }
    
    private void addHttpItemToArray(HttpItem _httpitem)
    {
		HttpItemArray.add(_httpitem);
    }
    
    private void setProgressDlg()
    {
    	mDialog = new ProgressDialog(this);
		mDialog.setMax(100);
		//mDialog.setTitle("Loading...");
		mDialog.setMessage("Please wait....");
		//mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
		mDialog.setIndeterminate(false);
    }
    
    
    private int buildTagList() throws MalformedURLException,IOException {
		
		String href=null,label=null;
		String title = null,link = null,author = null;
		Date date = null;
		int result=0;
		Log.d(TAG,"buildTagList");
		Source source = new Source(new URL(intent_link));

		source.fullSequentialParse();
		
		List trtags = source.getAllElements(HTMLElementName.TR);
		
		for(int i=0; i < trtags.size(); i++)
		{
			
		     Element trElement = (Element) trtags.get(i);
		     List tdList = trElement.getAllElements(HTMLElementName.TD);
		     //List aList = trElement.getAllElements(HTMLElementName.A);
		     //List spanList = trElement.getAllElements(HTMLElementName.SPAN);
		     
		     //Log.d(TAG,"TR SIZE : "+trtags.size()+", TD : "+tdList.size()+", A : "+aList.size()+", Span : "+spanList.size());
		     Log.d(TAG,"TR SIZE : "+trtags.size()+", TD : "+tdList.size());
		/*     
		     //Title
		     Element e_title = (Element)tdList.get(1);
		     title = e_title.getTextExtractor().toString();
		     
		     //Link
		     Element e_link = (Element)aList.get(0);
		     link = e_link.getAttributeValue("href");
		     
		     //Author 
		     Element e_author = (Element)tdList.get(2);
		     //author = e_author.getAttributeValue("title");
		     author = e_author.getContent().getChildElements().toString();
		     
		     //Date
		     Element e_date = (Element)spanList.get(1);
		     String sdate = e_date.getAttributeValue("title");
		     
		     Log.d(TAG,i+" Title : "+title);
		     Log.d(TAG,i+" Link : "+link);
		     Log.d(TAG,i+" Author : "+author);
		     Log.d(TAG,i+" Date : "+sdate);
		    
		     if(href!=null && label!=null && href!="")
		     {
		      
		      HttpItem h = new HttpItem(title,link,date,author);
		      addHttpItemToArray(h);
		     }
		     */ 
		     result = 1;
		}
		
		return result;
		
	}
    
    private class parseHtml extends AsyncTask<Void, Integer, Integer> {    	
    	   
 		@Override
		protected void onPreExecute() {
				
			super.onPreExecute();
			Toast.makeText(HttpItemActivity.this, "작업을 시작합니다.", Toast.LENGTH_SHORT);
			mDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... arg0) {
			/*
			for(int i=0; i<mDialog.getMax(); i++)
			{
				try
				{
					Thread.sleep(500);
				}catch(Exception ex){}
				this.publishProgress(i);
			}
			*/
			int result = 0;
			Log.d(TAG,"doInBackground");
			while(result > 0)
			{
				try {
					result = buildTagList();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Log.d(TAG,"result : "+result);
			}
			return null;
		}  
    	
     	@Override
    	protected void onProgressUpdate(Integer... progress) {
     		super.onProgressUpdate(progress);
    	}
  
    	@Override
    	protected void onPostExecute(Integer result) {
    		super.onPostExecute(result);
			Toast.makeText(HttpItemActivity.this, "작업이 끝났습니다.", Toast.LENGTH_SHORT);
			mDialog.dismiss(); 
    	}
  
    	@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		  	
    }
	
    

}
