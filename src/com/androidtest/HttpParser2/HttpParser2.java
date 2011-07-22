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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class HttpParser2 extends Activity {
	
	private static final String TAG = "HttpParser2";
	String address = "http://clien.career.co.kr/cs2/bbs/board.php?bo_table=news";
	String address_replace = "http://clien.career.co.kr/cs2/";
	
	ListView ArticleItemListView;
	
	ArticleItemAdapter ai;
		
	ArrayList<ArticleItem> ArticleItemArray = new ArrayList<ArticleItem>();
		
	ArticleItem selectedarticleitem;
	
	Intent intent;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ArticleItemListView = (ListView)this.findViewById(R.id.ArticleItemListView);
        
        ArticleItemListView.setOnItemClickListener(new OnItemClickListener () {
        	
 			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				
 				selectedarticleitem = ArticleItemArray.get(index);
 
 				if(selectedarticleitem != null)
 				{
 					/*
 					
 					String uridata = selectedarticleitem.getLink();
 					if(uridata == null)
 					{
 						Toast.makeText(HttpParser2.this, R.string.data_null, Toast.LENGTH_SHORT).show();
 						return;
 					}
 				
 					Log.d(TAG,"Link URL : "+uridata);
 					
	 				Uri uri = Uri.parse(uridata);
	 				Intent intent  = new Intent(Intent.ACTION_VIEW,uri);
	 				startActivity(intent);
	 				*/
 					String uridata = selectedarticleitem.getLink();
 					if(uridata == null)
 					{
 						Toast.makeText(HttpParser2.this, R.string.data_null, Toast.LENGTH_SHORT).show();
 						return;
 					}
 					intent = new Intent(HttpParser2.this, HttpItemActivity.class); 
 					intent.putExtra("Link", uridata);
 					startActivity(intent);
 				}
				
			}
        	
        });
        
        try {
        	buildPostTagList();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        ai = new ArticleItemAdapter(this,R.layout.article,ArticleItemArray);
        
 		ArticleItemListView.setAdapter(ai);
    }
     
    private void addArticleItemToArray(ArticleItem _articleitem)
    {
    	ArticleItemArray.add(_articleitem);
    }
    
    private void buildPostTagList() throws MalformedURLException,IOException {
		
		String href=null,label=null;
		String title = null,link = null;
		
		Source source = new Source(new URL(address));

		source.fullSequentialParse();
		
		List trtags = source.getAllElements(HTMLElementName.UL);
		for(int i=0; i < trtags.size(); i++)
		{
			 
		     Element trElement = (Element) trtags.get(i);
		     List liList = trElement.getAllElements(HTMLElementName.LI);
		     List aList = trElement.getAllElements(HTMLElementName.A);
		     List spanList = trElement.getAllElements(HTMLElementName.SPAN);
		     
		     Log.d(TAG,i+"li : "+liList.size()+" A : "+aList.size()+" Span : "+spanList.size());
		     
		     //Title
		     
		     //Element e_title = (Element)liList.get(3);
		     //title = e_title.getTextExtractor().toString(); 
			if (i == 3 || i == 4) {
				for (int x = 0; x < liList.size(); x++) {
					try {
						Element e_title = (Element) liList.get(x);
						title = e_title.getTextExtractor().toString();
						Element e_link = (Element) aList.get(x);
						link = e_link.getAttributeValue("href");
						
						link = link.replace("../","");
						link = address_replace + link;
												
						Log.d(TAG, i + " Title : " + title);
						Log.d(TAG, i + " Link : " + link);

						ArticleItem h = new ArticleItem(title, link);
						addArticleItemToArray(h);
					} catch (Exception e) {
						// Log.e(TAG,e.getMessage());
					}
				}
			}
		     		     
		     
		}
		

	}
    
}