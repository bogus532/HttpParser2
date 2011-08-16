package com.androidtest.HttpParser2;

import java.util.ArrayList;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArticleItemAdapter extends ArrayAdapter<ArticleItem> {
	Context context_this;
	
    private ArrayList<ArticleItem> items;

    public ArticleItemAdapter(Context context, int textViewResourceId, ArrayList<ArticleItem> items) {
            super(context, textViewResourceId, items);
            this.items = items;
            context_this = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
                        
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)context_this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.article, null);
            }
            ArticleItem b = items.get(position);
            if (b != null) {
	            	TextView tvArticle = (TextView) v.findViewById(R.id.tvArticle);
	                                    
	            	tvArticle.setTextSize(35);
	            	tvArticle.setText(this.getItem(position).toString());
	            	tvArticle.setAutoLinkMask(Linkify.WEB_URLS);
	            	tvArticle.setLinksClickable(true);
    				
                    if (tvArticle != null){
                    	tvArticle.setText(b.getTitle());                            
                    }
            }
            return v;
    }
    
 }
