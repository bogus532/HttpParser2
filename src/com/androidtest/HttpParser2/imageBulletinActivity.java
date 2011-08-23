package com.androidtest.HttpParser2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

public class imageBulletinActivity extends Activity {
	private static final String TAG = "imageBulletinActivity";
	
	ProgressDialog mDialog;
	imageBulletinItem imagebulletinitem;
	
	String address_replace = "http://clien.career.co.kr/cs2/";
	String address_replace_skin = "http://clien.career.co.kr/cs2/skin";
	String address_replace_data = "http://clien.career.co.kr/cs2/data";
	String address_replace_bbs = "http://clien.career.co.kr/cs2/bbs";
	
	String headtag = "";
	
	String endtag = "";
	
	Intent intent;
	String intent_link;
	String originalLink;
	String intent_title;
	int page = 1;
	
	WebView webView;
	ScrollView scrView;
	
	Display display;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagebulletinactivity);
        
        intent = getIntent();
        originalLink = intent.getExtras().getString("Link").toString();
        intent_link = originalLink;
        intent_title = intent.getExtras().getString("Title").toString();
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        webView = (WebView)this.findViewById(R.id.imgbulletinwebview);
        webView.getSettings().setPluginsEnabled(true);
        
        scrView = (ScrollView)this.findViewById(R.id.item_list);
        
        Context context = getApplicationContext();
        display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        
        //Log.d(TAG,"LCD Width : "+display.getWidth()+" , Height : "+display.getHeight());
        
        tagItem ti  = new tagItem(display.getWidth());
        
        headtag = ti.getIBHeadtag();
        endtag = ti.getTailtag();
        
        Button btnNext = (Button)findViewById(R.id.BtnNext);
        btnNext.setOnClickListener(new Button.OnClickListener(){
           public void onClick(View v) {
        	page++;
			intent_link = originalLink + "&page="+page;
			//Log.d(TAG,"link : "+intent_link);
			setProgressDlg();
			new parseHtml().execute();
           }
        });
        
        setTitle(intent_title);
        
        setProgressDlg();
		new parseHtml().execute();
	}
	
	private int buildTagList() throws MalformedURLException,IOException {
		String content_str = "";

		int result=0;
		
		Source source = new Source(new URL(intent_link));
		
		source.fullSequentialParse();
		
		//contents
		List<Element> boardmaintags = source.getAllElementsByClass("board_main");
		
		content_str = "";
		
		for(int z = 0; z < boardmaintags.size(); z++)
		{
			Element boardmainElement = (Element) boardmaintags.get(z);
			String cStr = boardmainElement.toString();
			
			content_str += cStr;

			result = 1;
		}
		
		if(content_str != null)
		{
			content_str = content_str.replaceAll(",  Hit : [0-9]* ,","");
			content_str = content_str.replaceAll("Vote : [0-9]*","");
			content_str = content_str.replaceAll("%","%25");
			content_str = content_str.replaceAll("\\.\\./skin",address_replace_skin);
			content_str = content_str.replaceAll("\\.\\./data",address_replace_data);
			content_str = content_str.replaceAll("\\.\\./bbs",address_replace_bbs);
			content_str = content_str.replaceAll("<!-- 게시판 하단 영역 -->","");
			content_str = content_str.replaceAll("<!-- 게시판 검색 -->","");
			content_str = content_str.replaceAll("&gt;","");
			content_str = content_str.replaceAll("<!--a class='page_text' href='./board.php?bo_table=image&page=[0-9]*'>맨끝 </a-->","");
			content_str = content_str.replaceAll("<!-- 목록, 글쓰기 버튼-->","");
			content_str = content_str.replaceAll("<!-- 관리자 버튼 -->","");
			content_str = content_str.replaceAll("<div class=\"board_foot\">","<!-- <div class=\"board_foot\">");
			content_str = content_str.replaceAll("<div class=\"list_button\">","<!-- <div class=\"list_button\">");
			content_str = content_str+" -->";
		}
		
		imageBulletinItem ci  = new imageBulletinItem(content_str);
		
		setimageBulletinItem(ci);

		return result;
		
	}
	
	public imageBulletinItem setimageBulletinItem(imageBulletinItem ci)
	{
		imagebulletinitem = ci;
		return imagebulletinitem;
	}
	
	private void setLayout()
	{
		String html_str;

		webView.getSettings().setJavaScriptEnabled(true);
		webView.clearView();
		webView.loadUrl("about:blank");
		scrView.scrollTo(0, 0);
		
		html_str = headtag+imagebulletinitem.getContents()+endtag;
		
		try {
			webView.loadData(html_str, "text/html", "utf-8");
			scrView.scrollTo(0, 0);
			setTitle(intent_title+" : "+page+"Page");
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
		//-------------------------------------------------------------------------------------------------------//
		/*
		FileOutputStream fileout = null;
		try {
			fileout = new FileOutputStream(new File(
			        Environment.getExternalStorageDirectory()
			                        .getAbsolutePath()
			                        + "/test.html"));
			fileout.write(html_str.getBytes());
			fileout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//*/
		//-------------------------------------------------------------------------------------------------------//
	}
	
	private void setProgressDlg()
    {
    	mDialog = new ProgressDialog(this);
		mDialog.setMax(100);
		mDialog.setMessage("Please wait....");
		mDialog.setIndeterminate(false);
		mDialog.setCancelable(false);
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
    			Toast.makeText(imageBulletinActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
