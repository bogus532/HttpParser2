package com.androidtest.HttpParser2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HttpParser2 extends Activity {
	
	private static final String TAG = "HttpParser2";
	String address = "http://clien.career.co.kr/cs2/bbs/board.php?bo_table=news";
	String address_replace = "http://clien.career.co.kr/cs2/";
	
	private static final int MENU_LOGIN = Menu.FIRST;
	private static final int MENU_PREFERENCES = Menu.FIRST+1;
	
	ListView ArticleItemListView;
	
	ArticleItemAdapter ai;
		
	ArrayList<ArticleItem> ArticleItemArray = new ArrayList<ArticleItem>();
		
	ArticleItem selectedarticleitem;
	
	Intent intent;
	
	ProgressDialog mDialog;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setTitle(R.string.mainTitle);
        ArticleItemListView = (ListView)this.findViewById(R.id.ArticleItemListView);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        ArticleItemListView.setOnItemClickListener(new OnItemClickListener () {
        	
 			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				
 				selectedarticleitem = ArticleItemArray.get(index);
 
 				if(selectedarticleitem != null && !selectedarticleitem.getTitle().contentEquals("사진게시판"))
 				{
 					String uridata = selectedarticleitem.getLink();
 					String titleText = selectedarticleitem.getTitle();
 					if(uridata == null)
 					{
 						Toast.makeText(HttpParser2.this, R.string.data_null, Toast.LENGTH_SHORT).show();
 						return;
 					}
 					intent = new Intent(HttpParser2.this, HttpItemActivity.class); 
 					intent.putExtra("Link", uridata);
 					intent.putExtra("Title", titleText);
 					startActivity(intent);
 				}
 				else if(selectedarticleitem != null && selectedarticleitem.getTitle().contentEquals("사진게시판"))
 				{
 					String uridata = selectedarticleitem.getLink();
 					String titleText = selectedarticleitem.getTitle();
 					if(uridata == null)
 					{
 						Toast.makeText(HttpParser2.this, R.string.data_null, Toast.LENGTH_SHORT).show();
 						return;
 					}
 					intent = new Intent(HttpParser2.this, imageBulletinActivity.class); 
 					intent.putExtra("Link", uridata);
 					intent.putExtra("Title", titleText);
 					startActivity(intent);
 				}
				
			}
        	
        });
        
        /*
        // Non Thread
        try {
        	buildPostTagList();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
        //Thread
        setProgressDlg();
		new parseArticle().execute();
		
        ai = new ArticleItemAdapter(this,R.layout.article,ArticleItemArray);
        
 		ArticleItemListView.setAdapter(ai);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);

	    menu.add(0, MENU_LOGIN, Menu.NONE, "Login");
	    menu.add(0, MENU_PREFERENCES, Menu.NONE,"Pref.");
	    return true;
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    super.onOptionsItemSelected(item);

	    switch (item.getItemId()) {
	        case (MENU_LOGIN): {
	        	
	            return true;
	        }
	        case (MENU_PREFERENCES) : {
	        	
	            return true;
	        }
    
	    }
	    return false;
	}
    
    @Override
	public void onBackPressed() {
    	super.onBackPressed();
	}
     
    private void addArticleItemToArray(ArticleItem _articleitem)
    {
    	ArticleItemArray.add(_articleitem);
    }
    
    private int buildPostTagList() throws MalformedURLException,IOException {
		
		String title = null,link = null;
		int result = 0;
		
		Source source = new Source(new URL(address));

		source.fullSequentialParse();
		
		List<Element> ultags = source.getAllElements(HTMLElementName.UL);
		for (int i = 0; i < ultags.size(); i++) {

			Element ulElement = (Element) ultags.get(i);
			List<Element> liList = ulElement.getAllElements(HTMLElementName.LI);
			List<Element> aList = ulElement.getAllElements(HTMLElementName.A);
			List<Element> spanList = ulElement.getAllElements(HTMLElementName.SPAN);

			//Log.d(TAG, i + "li : " + liList.size() + " A : " + aList.size()	+ " Span : " + spanList.size());

			if (i == 3 || i == 4 || i==6) {
				for (int x = 0; x < liList.size(); x++) {
					try {
						Element e_title = (Element) liList.get(x);
						title = e_title.getTextExtractor().toString();
						Element e_link = (Element) aList.get(x);
						link = e_link.getAttributeValue("href");

						if (!title.equals("클리앙대화방")) {
							link = link.replace("../", "");
							link = address_replace + link;

							//Log.d(TAG, i + " Title : " + title);
							//Log.d(TAG, i + " Link : " + link);

							ArticleItem h = new ArticleItem(title, link);
							addArticleItemToArray(h);
						}
					} catch (Exception e) {
						// Log.e(TAG,e.getMessage());
					}
				}
			}
			
			result =1;

		}
		return result;
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
    
    private class parseArticle extends AsyncTask<Void, Integer, Integer> {    	
 	   
 		@Override
		protected void onPreExecute() {
				
			super.onPreExecute();
			mDialog.show();
		}

		@Override
		protected Integer doInBackground(Void... arg0) {

			int result = 0;
			//Log.d(TAG,"doInBackground");
			while(result == 0)
			{
				try {
					result = buildPostTagList();
				} catch (MalformedURLException e) {
					e.printStackTrace();
					result = 2;
				} catch (IOException e) {
					e.printStackTrace();
					result = 3;
				}
				catch (Exception e) {
					e.printStackTrace();
					result = 4;
				}
				Log.d(TAG,"result : "+result);
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
    			ArticleItemListView.setAdapter(ai);
    		}
    		else if(result > 1)
    		{
    			Toast.makeText(HttpParser2.this, "Error", Toast.LENGTH_SHORT).show();
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