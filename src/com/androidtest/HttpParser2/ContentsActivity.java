package com.androidtest.HttpParser2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ContentsActivity extends Activity {
	private static final String TAG = "ContentsActivity";
	String address_replace = "http://clien.career.co.kr/cs2/";
	String address_replace_skin = "http://clien.career.co.kr/cs2/skin";
	String address_replace_data = "http://clien.career.co.kr/cs2/data";
	
	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	//int lcd_width = 480;
	
	String headtag ="<html xmlns=\"http://www.w3.org/1999/xhtml\">"+
	"<head>"+
	"<link href=\"http://clien.career.co.kr/cs2/css/style.css?v=20110712\" rel=\"stylesheet\" type=\"text/css\" />"+
	"<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>"+ 
	"<meta http-equiv=\"Imagetoolbar\" content=\"no\" />"+
	"<meta name=\"viewport\" content=\"user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width\" />"+
	"<link rel=\"stylesheet\" href=\"http://clien.career.co.kr/cs2/style.css?v=20110712\" type=\"text/css\" />"+	
	//"<script type='text/javascript' src='http://ad.clien.net/ad/www/delivery/spcjs.php?id=1&amp;blockcampaign=1&amp;charset=UTF-8'></script>"+
	"</head> <html><body topmargin=\"0\" leftmargin=\"0\">"+
	"<style>"+ 
	".resContents      img { max-width:280; width: expression(this.width > 280 ? 280: true); }"+
	//".resContents      img { max-width:"+Integer.toString(lcd_width)+"; width: expression(this.width > "+Integer.toString(lcd_width)+" ? "+Integer.toString(lcd_width)+": true); }"+
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
	
	String imgstarttag = "<html><body><p><img src=\"";
	String imgendtag = "\" width='46' height='16' align='right' border='0'></p></body></html>";
	
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
	WebView webView;
	
	ImageView imgView;
	
	Display display;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contents);
        
        textTitle = (TextView)this.findViewById(R.id.textTitle);
        textId = (TextView)this.findViewById(R.id.textId);
        textDate = (TextView)this.findViewById(R.id.textDate);
        
        textTitle.setBackgroundColor(Color.WHITE);
        textTitle.setTextColor(Color.BLACK);
        
        textId.setBackgroundColor(Color.WHITE);
        textId.setTextColor(Color.BLACK);
        
        textDate.setBackgroundColor(Color.WHITE);
        textDate.setTextColor(Color.BLACK);
        
        webView = (WebView)this.findViewById(R.id.webview);
        //webView.setBackgroundColor(Color.BLACK);
        webView.getSettings().setPluginsEnabled(true);
        //webView.getSettings().setLoadsImagesAutomatically(true);	
        //below two line, web 화면 전체가 디바이스 엘시디 사이즈에 최적화
        //webView.getSettings().setUseWideViewPort(true);
        //webView.setInitialScale(1);
        
        imgView = (ImageView)this.findViewById(R.id.imgview);
        
        intent = getIntent();
        intent_link = intent.getExtras().getString("Link").toString();
        intent_title = intent.getExtras().getString("Title").toString();
        setTitle(intent_title);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        Context context = getApplicationContext();
        display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        
        Log.d(TAG,"LCD Width : "+display.getWidth()+" , Height : "+display.getHeight());
        //lcd_width = display.getWidth();
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
	
	//by webview & jericho parser
	private int buildTagList2() throws MalformedURLException,IOException {
		String title = "",author = "";
		String author_link = "";
		String content_str = "";
		String reply_str = "";
		String strDate = "";
		String h3Str = "",h4Str = "";
		String temp ="";
		Date date = null;
		
		int result=0;
		
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
		Log.d("replytags",reply_str);
		content_str += reply_str;		
				
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
		
		if(author.equals("님"))
		{
			author = "";
			List<Element> viewheadtags = source.getAllElementsByClass("view_head");
			for (int i = 0; i < viewheadtags.size(); i++) {
				Element viewheadElement = (Element) viewheadtags.get(i);
				List<Element> imgtags = viewheadElement.getAllElements(HTMLElementName.IMG);
				for(int x = 0; x < imgtags.size();x++)
				{
					Element imgElement = (Element) imgtags.get(x);
					author_link = imgElement.getAttributeValue("src");
					if(author_link != null)
					{
						author_link = author_link.replace("../", "");
						author_link = address_replace+author_link;
					}
					Log.d(TAG,"author link : "+author_link);
				}
				
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

		
		ContentsItem ci  = new ContentsItem(title,author,strDate,content_str,author_link);
		
		setContentsItem(ci);
		//Log.d(TAG,"Last Title : "+content_str);
		Log.d(TAG,"result : "+result);
		return result;
		
	}
	
	//by jericho parser
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
			List<Element> ahreftags = contentElement.getAllElements(HTMLElementName.A);
			List<Element> attachimgtags = contentElement.getAllElementsByClass("attachedImage");
			List<Element> viewcontenttags = contentElement.getAllElementsByClass("view_content");
			
			Log.d(TAG,i+": ptags : "+ptags.size()+", divtags : "+divtags.size()+", imgtags : "+attachimgtags.size()
					+", ahreftags : "+ahreftags.size());
			
			
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
			
			 
			if(ptags.size()>2)
			{
				for(int x = 0; x < ptags.size();x++)
				{
					Element pElement = (Element) ptags.get(x);
					temp = pElement.getTextExtractor().toString();
					temp = "<p>"+temp+"</p>";
					Log.d(TAG,x + " : temp contents : "+temp);
					content_str += temp;
					//content_str += "";
					
					//Log.d(TAG,x + " : p contents : "+content_str);
				}
			}
			else
			{
				for(int x = 0; x < viewcontenttags.size();x++)
				{
					Element spanElement = (Element) spantags.get(x);
					//temp = spanElement.getTextExtractor().toString();
					temp = spanElement.toString();
					temp = temp.replaceAll("&nbsp;", "");
					content_str += temp;
					Log.d(TAG,x + " : span contents : "+temp);
				}
			}
			//*/
			
		}

		//reply
		List<Element> replytags = source.getAllElementsByClass("reply_base");
		
		for (int i = 0; i < replytags.size(); i++) {

			Element contentElement = (Element) replytags.get(i);
			String cStr = contentElement.toString();
			Log.d(TAG,i + " : reply : "+cStr);
			//content_str += cStr;
		}
		
				
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
		
		if(author.equals("님"))
		{
			//Log.d(TAG,"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
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
		
		//ContentsItem ci  = new ContentsItem(title,author,strDate,content_str,);
		
		//setContentsItem(ci);
		//Log.d(TAG,"Last Title : "+content_str);
		Log.d(TAG,"result : "+result);
		return result;
		
	}
	
	public class ImageGetter implements Html.ImageGetter {
		public Drawable getDrawable(String source) {
			Bitmap imgBitmap = null;
	        Bitmap resized = null;
	        Drawable drawable = null;
	        
	        options.inSampleSize =16;
	        
	        Log.d(TAG,"source : "+source);
	                
	        try
	        {
	        	URL url = new URL(source);
	        	URLConnection conn = url.openConnection();
	        	conn.connect();
	        	
	        	int nSize = conn.getContentLength();
	        	//Log.d(TAG,"nSize : "+nSize);
	        	BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
	        	
	        	FileOutputStream fileout = new FileOutputStream(new File(
                        Environment.getExternalStorageDirectory()
                                        .getAbsolutePath()
                                        + "/test.jpg"));
	        	
	        	imgBitmap = BitmapFactory.decodeStream(bis);
	        	int imgheight = imgBitmap.getHeight();
	        	int imgwidth = imgBitmap.getWidth();
	        	Log.d(TAG,"height : "+imgheight+", width : "+imgwidth);
	        	resized = Bitmap.createScaledBitmap(imgBitmap, imgwidth/2, imgheight/2, true);
	        	
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
		String html_str;
		textTitle.setText(contentitem._Title);
		textId.setText(contentitem._Id);
		textDate.setText(contentitem._Date);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setVerticalScrollBarEnabled(false);
		html_str = headtag+contentitem._Contents+endtag;
		try {
			if(contentitem._Id_link != null)
			{
				//imgwebView.loadData(imgstarttag+contentitem._Id_link+imgendtag,"text/html", "utf-8");
				imgView.setImageBitmap(null);
				imageDownloader.download(contentitem._Id_link, imgView);
			}

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
					//result = buildTagList();
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
