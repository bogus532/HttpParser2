package com.androidtest.HttpParser2;

import java.util.ArrayList;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HttpItemAdapter extends ArrayAdapter<HttpItem> {
	Context context_this;
	
    private ArrayList<HttpItem> items;

    public HttpItemAdapter(Context context, int textViewResourceId, ArrayList<HttpItem> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            context_this = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
                        
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)context_this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
            }
            HttpItem b = items.get(position);
            if (b != null) {
	            	TextView tvContents = (TextView) v.findViewById(R.id.tvContents);
	                TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
	                TextView tvAuthor = (TextView) v.findViewById(R.id.tvAuthor);
                    
	                tvContents.setTextSize(15);
                    tvContents.setText(this.getItem(position).toString());
                    tvContents.setAutoLinkMask(Linkify.WEB_URLS);
                    tvContents.setLinksClickable(true);
    				
                    if (tvContents != null){
                    	tvContents.setText(b.getTitle());                            
                    }
                    
                    if(tvDate != null){
                    	tvDate.setText(b.getDateToString());
                    }
                    
                    if(tvAuthor != null){
                    	tvAuthor.setText(b.getAuthor());
                    }
            }
            return v;
    }
    
 }
