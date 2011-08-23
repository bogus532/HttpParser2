package com.androidtest.HttpParser2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class imageBulletinActivity extends Activity {
	private static final String TAG = "imageBulletinActivity";
	
	ProgressDialog mDialog;
	imageBulletinItem imagebulletinitem;
	
	String address_replace = "http://clien.career.co.kr/cs2/";
	String address_replace_skin = "http://clien.career.co.kr/cs2/skin";
	String address_replace_data = "http://clien.career.co.kr/cs2/data";
	String address_replace_bbs = "http://clien.career.co.kr/cs2/bbs";
	
	String headtag ="<html xmlns=\"http://www.w3.org/1999/xhtml\">"+"\n"+
	"<head>"+"\n"+
	"<link href=\"http://clien.career.co.kr/cs2/css/style.css?v=20110712\" rel=\"stylesheet\" type=\"text/css\" />"+"\n"+
	"<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>"+ "\n"+
	"<meta http-equiv=\"Imagetoolbar\" content=\"no\" />"+"\n"+
	"<meta name=\"viewport\" content=\"user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width\" />"+"\n"+
	"<link rel=\"stylesheet\" href=\"http://clien.career.co.kr/cs2/style.css?v=20110712\" type=\"text/css\" />"+"\n"+	
	"</head> <html><body topmargin=\"0\" leftmargin=\"0\">"+"\n"+
	"<style>"+"\n"+ 
	".resContents      img { max-width:280; width: expression(this.width > 280 ? 280: true); }"+"\n"+
	".commentContents  img { max-width:280; width: expression(this.width > 280 ? 280: true); }"+"\n"+
	"</style>"+"\n"+	
	"<style type=\"text/css\">"+"\n"+
	".board_main{clear:both;padding-top:5px;width:280px;}"+"\n"+
	//".board_main table{width:100%;text-align:center;border-bottom:2px solid #ebebeb;}"+"\n"+
	//".board_main th{background-color:#dde1e6;border-bottom:2px solid #fff;padding:8px 0 6px 0;*padding:8px 0 3px 0;}"+"\n"+
	//".twl{border-left:1px solid #fff;border-right:1px solid #fff;}"+"\n"+
	//".board_main td{font:11px \"굴림\",Gulim,AppleGothic;color:#374273;border-bottom:1px solid #ebebeb;padding:4px 0 3px 0;}"+"\n"+
	//".board_main img{vertical-align:top;}"+"\n"+
	".view_head{background-color:#dde1e6;height:17px;color:#374273;padding:6px 0 2px 0;*padding:4px 0 3px 0;width:300px;}"+"\n"+
	".view_head a{color:#374273 !important;text-decoration:none !important;}"+"\n"+
	".user_info{float:left;padding-left:8px;}"+"\n"+
	".user_info span{font-size:11px;color:#808080;}"+"\n"+
	".post_info{float:right;padding-right:13px;}"+"\n"+
	".view_title{clear:both;border-bottom:5px solid #dde1e6;padding:10px 0 4px 18px;float:left;width:300px;word-break:break-all;}"+"\n"+
	".view_title div{float:left;width:280px;}"+"\n"+
	".title_btn{float:right;padding:20px 5px 0 0;}"+"\n"+
	".view_title h3{font-size:12px;font-weight:normal;}"+"\n"+
	".view_title h4{font:14px \"굴림\",Gulim,AppleGothic;font-weight:bold;}"+"\n"+
	".view_content{clear:both;font:12px \"굴림\",Gulim;line-height:19px;padding:0px 4px 0px 5px;word-break:break-all;}"+"\n"+
	".view_content_btn{text-align:right;margin-bottom:10px;}"+"\n"+
	".view_content_btn img{display:inline !important;}"+"\n"+
	".view_content_btn2{margin:0 auto;width:100px;overflow:hidden;}"+"\n"+
	".view_content_btn3{margin:0 auto;width:100px;}"+"\n"+
	".view_content_btn3 li{float:left;padding-right:6px;}"+"\n"+
	".view_content_btn3 img{display:inline !important;}"+"\n"+
	".view_content_btn2 li{float:left;padding-right:6px;}"+"\n"+
	".ad_area1{clear:both;text-align:center;padding:20px 0 20px 0;font-weight:normal;}"+"\n"+
	".reply_head{background-color:#f3f3f3;height:16px;padding:4px 14px 4px 9px;line-height:16px;margin-top:20px;width:280px;}"+"\n"+
	".repla_head a{color:#374273 !important;text-decoration:none !important;}"+"\n"+
	".reply_head1{background-color:#f3f3f3;height:16px;padding:4px 14px 4px 9px;line-height:16px;margin-top:20px;width:200px;}"+"\n"+
	".reply_info{float:left;color:#898989;font-size:11px;}"+"\n"+
	".reply_info .user_id{color:#374273;padding-right:2px;}"+"\n"+
	".reply_info .block{font-size:12px;padding:0 5px 0 13px;_line-height:19px;}"+"\n"+
	".reply_head li{float:left;}"+"\n"+
	".reply_btn{float:right;}"+"\n"+
	".reply_btn li{padding-left:3px;}"+"\n"+
	".reply_btn img{vertical-align:top;padding-top:1px;}"+"\n"+
	".reply_btn .report{padding:3px 0 0 7px;}"+"\n"+
	".reply_btn .ip{color:#b2b2b2;padding-right:7px;font-size:12px;font-weight:normal;}"+"\n"+
	".reply_content{clear:both;word-break:break-all;padding:4px 4px 0px 9px;margin-bottom:0px;font:12px \"굴림\",Gulim,AppleGothic;line-height:19px;color:#000;font-weight:normal;}"+"\n"+
	".ccl{width:280px;text-align:right;margin:0 auto;}"+"\n"+
	".ccl img{display:inline !important;}"+"\n"+
	".signature{color:#667e99;margin-top:20px;}"+"\n"+
	".signature img{display:inline !important;}"+"\n"+
	".signature dl{width:280px;overflow:hidden;margin:0 auto;}"+"\n"+
	".signature dt{border-bottom:2px solid #c8d0d9;height:18px;overflow:hidden;}"+"\n"+
	".signature dd{padding:7px 0 0 20px;}"+"\n"+
	"</style>";
	
	String endtag = " </body></html>";
	
	Intent intent;
	String intent_link;
	String originalLink;
	String intent_title;
	int page = 1;
	
	WebView webView;
	
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
        
        Button btnNext = (Button)findViewById(R.id.BtnNext);
        btnNext.setOnClickListener(new Button.OnClickListener(){
           public void onClick(View v) {
        	  // /*
        	page++;
			intent_link = originalLink + "&page="+page;
			Log.d(TAG,"link : "+intent_link);
			setProgressDlg();
			new parseHtml().execute();
			//*/
        	   //webView.clearView();
           }
        });
        
        setTitle(intent_title);
        
        setProgressDlg();
		new parseHtml().execute();
	}
	
	private int buildTagList() throws MalformedURLException,IOException {
		String content_str = "";

		int result=0;
		Log.d(TAG,"link : "+intent_link);
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
		int webwidth;
		int webheight;

		webView.getSettings().setJavaScriptEnabled(true);
		webView.clearView();
		webView.loadUrl("about:blank");
		webView.scrollTo(0, 0);
		
		html_str = headtag+imagebulletinitem.getContents()+endtag;
		try {
			webView.loadData(html_str, "text/html", "utf-8");
			webView.scrollTo(0, 0);
			webwidth = webView.getWidth();
			webheight = webView.getHeight();
			Log.d(TAG,"width = "+webwidth+", height = "+webheight);
			//webView.scrollTo(0, webheight);
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
		//mDialog.onKeyDown(keyCode, event)
    }
	
	private class parseHtml extends AsyncTask<Void, Integer, Integer> {    	
 	   
 		@Override
		protected void onPreExecute() {
				
			super.onPreExecute();
			//webView.clearFormData();
			
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
