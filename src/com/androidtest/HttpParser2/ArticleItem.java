package com.androidtest.HttpParser2;

import android.util.Log;


public class ArticleItem {
	
	private String title;
	private String link;
			
	public ArticleItem(String _title,String _link)
	{
		title = _title;
		link = _link;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getLink()
	{
		//Log.d("article","link : "+link);
		return link;
	}
	
	@Override
    public String toString() {

		return title;
    }

}
