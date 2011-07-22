package com.androidtest.HttpParser2;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HttpItem {
	
	private String title;
	private String link;
	private String author;
	private Date date;
		
	public HttpItem(String _title,String _link,Date _date,String _author)
	{
		title = _title;
		link = _link;
		author = _author;
		date = _date;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getLink()
	{
		return link;
	}
	
	public String getAuthor()
	{
		return author;
	}
	
	public Date getDate()
	{
		return date;
	}
	
	public String getDateToString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = sdf.format(date);
        return dateString;
	}
	
	@Override
    public String toString() {

		return title;
    }
	
}
