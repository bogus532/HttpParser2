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

import com.androidtest.HttpParser2.util.NetworkBase;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HttpItemActivity extends Activity {
	private static final String TAG = "HttpItemActivity";
	
	String address_replace = "http://clien.career.co.kr/cs2/";
	
	private static final int MENU_UPDATE = Menu.FIRST;
			
	ListView httpItemListView;
	ProgressDialog mDialog;
	
	HttpItemAdapter aa;
	ArrayList<HttpItem> HttpItemArray = new ArrayList<HttpItem>();
	HttpItem selectedhttpitem;
	private LayoutInflater mInflater;
	
	Intent intent;
	String intent_link;
	String originalLink;
	String intent_title;
	
	int page = 1;
	int scroll_index = 0;
	int scroll_skip_index = 0;
	boolean bUpdate = false;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httpitemactivity);
        
        intent = getIntent();
        originalLink = intent.getExtras().getString("Link").toString();
        intent_link = originalLink;
        intent_title = intent.getExtras().getString("Title").toString();
        
        setTitle(intent_title);
        
        //Log.d(TAG,intent_link);
        
        httpItemListView = (ListView)this.findViewById(R.id.httpItemListView);
        
        mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        httpItemListView.addFooterView(mInflater.inflate(R.layout.articlefooter, null));
        httpItemListView.setVisibility(View.INVISIBLE);
        //httpItemListView.setOnScrollListener(this);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        httpItemListView.setOnItemClickListener(new OnItemClickListener () {
        	
 			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
 				
 				int total_index = httpItemListView.getCount();
 				boolean bnextPage = false;
 				
 				//Log.d("setOnItemClickListener","index : "+index+", total : "+total_index);
 				 				
 				if(index == total_index -1 || index > total_index)
 				{
 					selectedhttpitem = null;
 					bnextPage = true;
 				}
 				else
 				{
 					selectedhttpitem = HttpItemArray.get(index);
 				}
 				
 				if(selectedhttpitem != null && 
 						(selectedhttpitem.getTitle().contains("관리자에 의해 삭제된 글입니다.")
 						|| selectedhttpitem.getTitle().contains("다수의 신고에 의해 삭제된 글입니다.")))
 				{
 					Toast.makeText(HttpItemActivity.this, "삭제된 글입니다.", 1000).show();
 					return;
 				}
  
 				if(selectedhttpitem != null && bnextPage == false)
 				{
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
 				else if(selectedhttpitem == null && bnextPage == true)
 				{
 					page++;
 					intent_link = originalLink + "&page="+page;
 					scroll_index = total_index;
 					setProgressDlg();
 					new parseHtml().execute();
 					Log.d("setOnItemClickListener","page : "+page);
 				}
 				else 
 				{
 					Toast.makeText(HttpItemActivity.this, R.string.data_null, Toast.LENGTH_SHORT).show();
					return;
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
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);

	    menu.add(0, MENU_UPDATE, Menu.NONE, "Update");

	    return true;
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    super.onOptionsItemSelected(item);

	    switch (item.getItemId()) {
	        case (MENU_UPDATE): {
	        	
	        	page = 1;
	        	bUpdate = true;
	        	setProgressDlg();
	    		new parseHtml().execute();
	    		
	            return true;
	        }
	    }
	    return false;
	}
    
    /*
    @Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		int count = totalItemCount - visibleItemCount;
		
		Log.d(TAG,"onScroll - count : "+count+", visible : "+visibleItemCount+", total : "+totalItemCount);

		if(firstVisibleItem >= count && totalItemCount != 0)
		{
			intent_link = intent_link + "&page=2";
			new parseHtml().execute();
		}	
	}
 
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
	}
	*/
    
    private void addHttpItemToArray(HttpItem _httpitem)
    {
		HttpItemArray.add(_httpitem);
    }
    
    private void updateListView()
    {
      	//aa.notifyDataSetChanged();
    	httpItemListView.setVisibility(View.VISIBLE);
    }
    
    private void setProgressDlg()
    {
    	mDialog = new ProgressDialog(this);
		mDialog.setMax(100);
		//mDialog.setTitle("Loading...");
		mDialog.setMessage("Please wait....");
		//mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
		mDialog.setIndeterminate(false);
		mDialog.setCancelable(false);
    }
    
    
	private int buildTagList() throws MalformedURLException,IOException {
		
		String title = null,link = null,author = null;
		String strDate = null,strImg = null,strReturnURL = null;
		Date date = null;
		int result=0;
		int skip_count = 0;
		int check_count = 0;
		int post_notice_color = 0;
		//Log.d(TAG,"buildTagList");
		String readhtml = NetworkBase.getHtml(intent_link);
		Source source = new Source(readhtml);
		//Source source = new Source(new URL(intent_link));
		String temp[]= new String[6];

		source.fullSequentialParse();
		
		List<Element> tbodytags = source.getAllElements(HTMLElementName.TBODY);
		
		for (int i = 0; i < tbodytags.size(); i++) {

			Element trElement = (Element) tbodytags.get(i);
			List<Element> trList = trElement.getAllElements(HTMLElementName.TR);
			List<Element> postnoticeList = trElement.getAllElementsByClass("post_notice");

			//Log.d(TAG, "Tbody SIZE : " + tbodytags.size() + ", TR : " + trList.size());
			Log.d(TAG,"post notice index: "+postnoticeList.size());
			if(page > 1)
			{
				skip_count = postnoticeList.size();
				scroll_skip_index = postnoticeList.size();
			}
			
			check_count = postnoticeList.size();
								
			for(int x = skip_count; x < trList.size(); x++)
			{
				Element tdElement = (Element) trList.get(x);
				List<Element> tdList = tdElement.getAllElements(HTMLElementName.TD);
				
				for(int y=0; y < tdList.size();y++)
				{
					Element e_title = (Element) tdList.get(y);
					temp[y] = e_title.getTextExtractor().toString();
					//Log.d(TAG, x+" : "+y + " temp string : " + temp[y]);
				}
				
				if(tdElement.getTextExtractor().toString().contentEquals(""))
				{
					check_count++;
					//Log.d(TAG,"check_count = "+check_count);
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
					if(page == 1 && x < check_count)
					{
						post_notice_color = 1;
						if(!title.contains("[공지]"))
						{
							title = "[공지]"+title;
						}
						Log.d(TAG,x+", post_notice");
					}
					else
					{
						post_notice_color = 0;
					}

					HttpItem h = new HttpItem(title, link, date, author,strReturnURL,post_notice_color);
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
			if(bUpdate == true)
			{
				intent_link = originalLink;
				HttpItemArray.clear();
			}
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
				//Log.d(TAG,"result : "+result+", count : "+count);
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
				if(bUpdate == true)
				{
					httpItemListView.setSelection(0);
				}
				else
				{
					httpItemListView.setSelection(scroll_index-scroll_skip_index);
				}
				updateListView();
			}
			else if(result == 2)
			{
				Toast.makeText(HttpItemActivity.this, "네트워크 연결 에러 입니다.\\n 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
    			onBackPressed();
			}
			else if(result == 3)
			{
				Toast.makeText(HttpItemActivity.this, "Error", Toast.LENGTH_SHORT).show();
    			onBackPressed();
			}
			else if(result == 4)
			{
				Toast.makeText(HttpItemActivity.this, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
    			onBackPressed();
			}
			bUpdate = false;
			mDialog.dismiss(); 
    	}
  
    	@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		  	
    }
	
    

}
