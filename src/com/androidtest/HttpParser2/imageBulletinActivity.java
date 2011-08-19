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
import android.webkit.WebView;
import android.widget.Toast;

public class imageBulletinActivity extends Activity {
	private static final String TAG = "imageBulletinActivity";
	
	ProgressDialog mDialog;
	imageBulletinItem imagebulletinitem;
	
	String address_replace = "http://clien.career.co.kr/cs2/";
	String address_replace_skin = "http://clien.career.co.kr/cs2/skin";
	String address_replace_data = "http://clien.career.co.kr/cs2/data";
	
	String headtag ="<html xmlns=\"http://www.w3.org/1999/xhtml\">"+
	"<head>"+
	"<link href=\"http://clien.career.co.kr/cs2/css/style.css?v=20110712\" rel=\"stylesheet\" type=\"text/css\" />"+
	"<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>"+ 
	"<meta http-equiv=\"Imagetoolbar\" content=\"no\" />"+
	"<meta name=\"viewport\" content=\"user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width\" />"+
	"<link rel=\"stylesheet\" href=\"http://clien.career.co.kr/cs2/style.css?v=20110712\" type=\"text/css\" />"+	
	"</head> <html><body topmargin=\"0\" leftmargin=\"0\">"+
	"<style>"+ 
	".resContents      img { max-width:280; width: expression(this.width > 280 ? 280: true); }"+
	".commentContents  img { max-width:280; width: expression(this.width > 280 ? 280: true); }"+
	"</style>"+	
	"<style type=\"text/css\">"+
	".board_main{clear:both;padding-top:5px;}"+
	".view_content{clear:both;font:12px \"굴림\",Gulim;line-height:19px;padding:0px 4px 0px 5px;word-break:break-all;}"+
	".view_content_btn{text-align:right;margin-bottom:10px;}"+
	".view_content_btn img{display:inline !important;}"+
	".view_content_btn2{margin:0 auto;width:300px;overflow:hidden;}"+
	".view_content_btn3{margin:0 auto;width:300px;}"+
	".view_content_btn3 li{float:left;padding-right:6px;}"+
	".view_content_btn3 img{display:inline !important;}"+
	".view_content_btn2 li{float:left;padding-right:6px;}"+
	".ad_area1{clear:both;text-align:center;padding:20px 0 20px 0;font-weight:normal;}"+
	".reply_head{background-color:#f3f3f3;height:16px;padding:4px 14px 4px 9px;line-height:16px;margin-top:20px;}"+
	".repla_head a{color:#374273 !important;text-decoration:none !important;}"+
	".reply_head1{background-color:#f3f3f3;height:16px;padding:4px 14px 4px 9px;line-height:16px;margin-top:20px;}"+
	".reply_info{float:left;color:#898989;font-size:11px;}"+
	".reply_info .user_id{color:#374273;padding-right:2px;}"+
	".reply_info .block{font-size:12px;padding:0 5px 0 13px;_line-height:19px;}"+
	".reply_head li{float:left;}"+
	".reply_btn{float:right;}"+
	".reply_btn li{padding-left:3px;}"+
	".reply_btn img{vertical-align:top;padding-top:1px;}"+
	".reply_btn .report{padding:3px 0 0 7px;}"+
	".reply_btn .ip{color:#b2b2b2;padding-right:7px;font-size:12px;font-weight:normal;}"+
	".reply_content{clear:both;word-break:break-all;padding:4px 4px 0px 9px;margin-bottom:0px;font:12px \"굴림\",Gulim,AppleGothic;line-height:19px;color:#000;font-weight:normal;}"+
	".ccl{width:280px;text-align:right;margin:0 auto;}"+
	".ccl img{display:inline !important;}"+
	".signature{color:#667e99;margin-top:20px;}"+
	".signature img{display:inline !important;}"+
	".signature dl{width:280px;overflow:hidden;margin:0 auto;}"+
	".signature dt{border-bottom:2px solid #c8d0d9;height:18px;overflow:hidden;}"+
	".signature dd{padding:7px 0 0 20px;}"+
	"</style>";
	
	String endtag = " </body></html>";
	
	Intent intent;
	String intent_link;
	String originalLink;
	String intent_title;
	
	WebView webView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagebulletinactivity);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        webView = (WebView)this.findViewById(R.id.imgbulletinwebview);
        webView.getSettings().setPluginsEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(1);
        
        intent = getIntent();
        originalLink = intent.getExtras().getString("Link").toString();
        intent_link = originalLink;
        intent_title = intent.getExtras().getString("Title").toString();
        
        setTitle(intent_title);
        
        Log.d(TAG,"link : "+intent_link);
        
        //webView.loadUrl(intent_link);
        
        setProgressDlg();
		new parseHtml().execute();
	}
	
	private int buildTagList2() throws MalformedURLException,IOException {
		String content_str = "";
		String reply_str = "";
	
		int result=0;
		
		Source source = new Source(new URL(intent_link));
		
		source.fullSequentialParse();
		
		//contents
		List<Element> contenttags = source.getAllElementsByClass("view_content");
		
		for (int i = 0; i < contenttags.size(); i++) {

			Element contentElement = (Element) contenttags.get(i);
			String cStr = contentElement.toString();

			if(cStr.contains("<object"))
			{
				cStr = cStr.replaceAll("height=\"[0-9]*\"","height=\"150\"");
				cStr = cStr.replaceAll("width=\"[0-9]*\"","width=\"200\"");
			}
			content_str += cStr;
			
			result = 1;
		}
		
		//contents - reply
		List<Element> replytags = source.getAllElementsByClass("reply_base");
		
		for (int i = 0; i < replytags.size(); i++) {

			Element replyElement = (Element) replytags.get(i);
			String cStr = replyElement.toString();
			//Log.d("replytags",i + " : cStr : "+cStr);
			reply_str += cStr;
		}
		
		content_str = content_str.replaceAll("<div class=\"signature\"","<!-- <div class=\"signature\"");
		content_str = content_str.replaceAll("</dd></dl></div>","</dd></dl></div> -->");
		//content_str = content_str.replaceAll("<div class=\"ccl\"","<!-- <div class=\"ccl\"");
		//content_str = content_str.replaceAll("<div class=\"signature\"><dl><dt>(.*?)</dd></dl></div>","aaaa");
		
		content_str = content_str.replaceAll("<div class=\"ExifInfo\" style=\"width:[0-9]*px;\">"
				,"<div class=\"ExifInfo\" style=\"width:280px;\">");
		content_str = content_str.replaceAll("%","%25");
		content_str = content_str.replaceAll("\\.\\./skin",address_replace_skin);
		content_str = content_str.replaceAll("\\.\\./data",address_replace_data);
		//Log.d("content_str",content_str);
		
		reply_str = reply_str.replaceAll("%","%25");
		reply_str = reply_str.replaceAll("\\.\\./skin",address_replace_skin);
		reply_str = reply_str.replaceAll("\\.\\./data",address_replace_data);
		reply_str = reply_str.replaceAll("</textarea>(.*?)</div>","</textarea></div>");
		//Log.d("replytags",reply_str);
		content_str += reply_str;		
				
		imageBulletinItem ci  = new imageBulletinItem(content_str);
		
		setimageBulletinItem(ci);
		
		Log.d(TAG,"result : "+result);
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
		webView.setVerticalScrollBarEnabled(false);
		html_str = headtag+imagebulletinitem._Contents+endtag;
		try {
			webView.loadData(html_str, "text/html", "utf-8");
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
		//mDialog.onKeyDown(keyCode, event)
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
					result = buildTagList2();
					
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
