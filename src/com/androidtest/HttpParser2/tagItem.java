package com.androidtest.HttpParser2;

public class tagItem {
	
	private String ib_head_tag1 =
		"<html xmlns=\"http://www.w3.org/1999/xhtml\">"+"\n"+
		"<head>"+"\n"+
		"<link href=\"http://clien.career.co.kr/cs2/css/style.css?v=20110712\" rel=\"stylesheet\" type=\"text/css\" />"+"\n"+
		"<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>"+ "\n"+
		"<meta http-equiv=\"Imagetoolbar\" content=\"no\" />"+"\n"+
		"<meta name=\"viewport\" content=\"user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width\" />"+"\n"+
		"<link rel=\"stylesheet\" href=\"http://clien.career.co.kr/cs2/style.css?v=20110712\" type=\"text/css\" />"+"\n"+	
		"</head> <html><body topmargin=\"0\" leftmargin=\"0\">"+"\n"+
		"<style>"+"\n"; 
		//".resContents      img { max-width:280; width: expression(this.width > 280 ? 280: true); }"+"\n"+
		//".commentContents  img { max-width:280; width: expression(this.width > 280 ? 280: true); }"+"\n"+
	private String ib_head_tag2 =
		"</style>"+"\n"+	
		"<style type=\"text/css\">"+"\n"+
		".board_main{clear:both;padding-top:5px;width:280px;}"+"\n"+
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
		"</style>"+"\n";
	
	private String content_headtag1 ="<html xmlns=\"http://www.w3.org/1999/xhtml\">"+"\n"+
		"<head>"+"\n"+
		"<link href=\"http://clien.career.co.kr/cs2/css/style.css?v=20110712\" rel=\"stylesheet\" type=\"text/css\" />"+"\n"+
		"<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>"+"\n"+
		"<meta http-equiv=\"Imagetoolbar\" content=\"no\" />"+"\n"+
		"<meta name=\"viewport\" content=\"user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width\" />"+"\n"+
		"<link rel=\"stylesheet\" href=\"http://clien.career.co.kr/cs2/style.css?v=20110712\" type=\"text/css\" />"+"\n"+	
		"</head> <html><body topmargin=\"0\" leftmargin=\"0\">"+"\n"+
		"<style>"+"\n"; 
		//".resContents      img { max-width:280; width: expression(this.width > 280 ? 280: true); }"+
		//".commentContents  img { max-width:280; width: expression(this.width > 280 ? 280: true); }"+
	private String content_headtag2 =
		"</style>"+"\n"+
		"<style type=\"text/css\">"+"\n"+
		".board_main{clear:both;padding-top:5px;}"+"\n"+
		".view_content{clear:both;font:12px \"굴림\",Gulim;line-height:19px;padding:0px 4px 0px 5px;word-break:break-all;}"+"\n"+
		".view_content_btn{text-align:right;margin-bottom:10px;}"+"\n"+
		".view_content_btn img{display:inline !important;}"+"\n"+
		".view_content_btn2{margin:0 auto;width:300px;overflow:hidden;}"+"\n"+
		".view_content_btn3{margin:0 auto;width:300px;}"+"\n"+
		".view_content_btn3 li{float:left;padding-right:6px;}"+"\n"+
		".view_content_btn3 img{display:inline !important;}"+"\n"+
		".view_content_btn2 li{float:left;padding-right:6px;}"+"\n"+
		".ad_area1{clear:both;text-align:center;padding:20px 0 20px 0;font-weight:normal;}"+"\n"+
		".reply_head{background-color:#f3f3f3;height:16px;padding:4px 14px 4px 9px;line-height:16px;margin-top:20px;}"+"\n"+
		".repla_head a{color:#374273 !important;text-decoration:none !important;}"+"\n"+
		".reply_head1{background-color:#f3f3f3;height:16px;padding:4px 14px 4px 9px;line-height:16px;margin-top:20px;}"+"\n"+
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
		"</style>"+"\n";
	
	private String empty_headtag =
		"<html xmlns=\"http://www.w3.org/1999/xhtml\">"+"\n"+
		"<html><body>"+"\n";
	
	private String tail_tag = " </body></html>";
	
	public String ibhead_tag="";
	public String Content_headtag="";
	
	int lcd_width = 280;
	
	public tagItem(int _lcd_width)
	{
		lcd_width = _lcd_width;
		lcd_width = lcd_width/2;
		
		ibhead_tag = ib_head_tag1+
		".resContents      img { max-width:"+lcd_width+"; width: expression(this.width > "+lcd_width+" ? "+lcd_width+": true); }"+"\n"+
		".commentContents  img { max-width:"+lcd_width+"; width: expression(this.width > "+lcd_width+" ? "+lcd_width+": true); }"+"\n"+
		ib_head_tag2;
		
		Content_headtag = content_headtag1+
		".resContents      img { max-width:"+lcd_width+"; width: expression(this.width > "+lcd_width+" ? "+lcd_width+": true); }"+"\n"+
		".commentContents  img { max-width:"+lcd_width+"; width: expression(this.width > "+lcd_width+" ? "+lcd_width+": true); }"+"\n"+
		content_headtag2;
		
	}
	
	public String getIBHeadtag(){
		return ibhead_tag;
	}
	
	public String getContentHeadtag(){
		return Content_headtag;
	}
	
	public String getEmptyHeadtag(){
		return empty_headtag;
	}
	
	public String getTailtag(){
		return tail_tag;
	}

}
