package com.androidtest.HttpParser2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
	
	String address_replace = "http://clien.career.co.kr/cs2/";
	
	ListView httpItemListView;
	ProgressDialog mDialog;
	
	HttpItemAdapter aa;
	ArrayList<HttpItem> HttpItemArray = new ArrayList<HttpItem>();
	HttpItem selectedhttpitem;
	
	Intent intent;
	String intent_link;
	String intent_title;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httpitemactivity);
        
        intent = getIntent();
        intent_link = intent.getExtras().getString("Link").toString();
        intent_title = intent.getExtras().getString("Title").toString();
        
        setTitle(intent_title);
        
        //Log.d(TAG,intent_link);
        
        httpItemListView = (ListView)this.findViewById(R.id.httpItemListView);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        httpItemListView.setOnItemClickListener(new OnItemClickListener () {
        	
 			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				
 				selectedhttpitem = HttpItemArray.get(index);
  
 				if(selectedhttpitem != null)
 				{
 					/*
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
	 				*/
 					String uridata = selectedhttpitem.getLink();
 					if(uridata == null)
 					{
 						Toast.makeText(HttpItemActivity.this, R.string.data_null, Toast.LENGTH_SHORT).show();
 						return;
 					}
 					intent = new Intent(HttpItemActivity.this, ContentsActivity.class); 
 					intent.putExtra("Link", uridata);
 					intent.putExtra("Title", selectedhttpitem.getTitle());
 					startActivity(intent);
	 				
  				}
			}
        });
       
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
        
        aa = new HttpItemAdapter(this,R.layout.row,HttpItemArray);
		        
        httpItemListView.setAdapter(aa);
		
    }
    
    @Override
	public void onBackPressed() {
    	super.onBackPressed();
	}
    
    private void addHttpItemToArray(HttpItem _httpitem)
    {
		HttpItemArray.add(_httpitem);
    }
    
    private void updateListView()
    {
      	//aa.notifyDataSetChanged();
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
		
		String title = null,link = null,author = null;
		String strDate = null,strImg = null,strReturnURL = null;
		Date date = null;
		int result=0;
		//Log.d(TAG,"buildTagList");
		Source source = new Source(new URL(intent_link));
		String temp[]= new String[6];

		source.fullSequentialParse();
		
		List<Element> tbodytags = source.getAllElements(HTMLElementName.TBODY);
		
		for (int i = 0; i < tbodytags.size(); i++) {

			Element trElement = (Element) tbodytags.get(i);
			List<Element> trList = trElement.getAllElements(HTMLElementName.TR);

			//Log.d(TAG, "Tbody SIZE : " + tbodytags.size() + ", TR : " + trList.size());
			
			for(int x = 0; x < trList.size(); x++)
			{
				Element tdElement = (Element) trList.get(x);
				List<Element> tdList = tdElement.getAllElements(HTMLElementName.TD);
				
				
				for(int y=0; y < tdList.size();y++)
				{
					Element e_title = (Element) tdList.get(y);
					temp[y] = e_title.getTextExtractor().toString();
					//Log.d(TAG, x+" : "+y + " temp string : " + temp[y]);
				}
				
				if(tdList.size() == 5)
				{
					title = temp[1];
					author = temp[2];
				}
				else if(tdList.size() == 1)
				{
					continue;
				}
				else
				{
					title = temp[1] + temp[2];
					author = temp[3];
				}
				
				
				List<Element> aList = tdElement.getAllElements(HTMLElementName.A);
		
				for(int z=0; z < aList.size();z++)
				{
					Element e_link = (Element) aList.get(z);
					if(!e_link.getAttributeValue("href").contains("javascript"))
					{
						link = e_link.getAttributeValue("href");
					}
					//Log.d(TAG,x+" : "+z + " Link string : " + link);
				}
				
				if(link != null)
				{
					link = link.replace("../", "");
					link = address_replace + link;
				}
				
			
				for(int z=0; z < tdList.size();z++)
				{
					Element e_date = (Element) tdList.get(z);
					strDate = e_date.toString();
					strDate = strDate.replaceAll("<td><span title=\"","");
					strDate = strDate.replaceAll("</a></td>","");
					strDate = strDate.replaceAll("\">[0-9]*-[0-9]*","");
					strDate = strDate.replaceAll("\">[0-9]*:[0-9]*","");
					
					if(!strDate.contains("<td"))
					{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                    date = new GregorianCalendar(0,0,0).getTime();
	                    try {
	                        date = sdf.parse(strDate);
	                    } catch (ParseException e) {
	                        e.printStackTrace();
	                    }
					}
					
					//Log.d(TAG,x+" : "+z + " date string : " + strDate);
				}
				
				strReturnURL = null;
				
				for(int z=0; z < tdList.size();z++)
				{
					Element e_img = (Element) tdList.get(z);
					strImg = e_img.toString();
					
					//if(strImg.contains("<img"))
					if(strImg.contains("<td class=\"post_name\"><img"))
					{
						strImg = e_img.toString();
						strImg = strImg.replaceAll("<td class=\"post_name\"><img src='","");
						strImg = strImg.replaceAll("></a></td>","");
						strImg = strImg.replaceAll("></td>","");
						strImg = strImg.replaceAll("' width='[0-9]*' height='[0-9]*' align='[a-zA-Z]*' border='[0-9]*'","");
						strImg = strImg.replace("../", "");
						strReturnURL = address_replace + strImg;
					}
					
					//Log.d(TAG,x+" : "+z + " img string : " + strReturnURL);
				}

				if (trList != null) {

					HttpItem h = new HttpItem(title, link, date, author,strReturnURL);
					addHttpItemToArray(h);
				}
			}
			
			result = 1;
		}
		
		return result;
		
	}
    
    private class parseHtml extends AsyncTask<Void, Integer, Integer> {    	
    	   
 		@Override
		protected void onPreExecute() {
				
			super.onPreExecute();
			mDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... arg0) {

			int result = 0;
			int count=0;
			while(result == 0)
			{
				try {
					result = buildTagList();
				} catch (MalformedURLException e) {
					e.printStackTrace();
					result = 2;
				} catch (IOException e) {
					e.printStackTrace();
					result =3;
				}
				catch (Exception e) {
					e.printStackTrace();
					result =4;
				}
				Log.d(TAG,"result : "+result+", count : "+count);
				count++;
				if(count>5)
				{
					break;
				}
			}
			return result;
		}  
    	
     	@Override
    	protected void onProgressUpdate(Integer... progress) {
     		//super.onProgressUpdate(progress);
    	}
  
    	@Override
    	protected void onPostExecute(Integer result) {
    		super.onPostExecute(result);
			if(result == 1)
			{
				httpItemListView.setAdapter(aa);
				updateListView();
			}
			else if(result > 1)
			{
				Toast.makeText(HttpItemActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
