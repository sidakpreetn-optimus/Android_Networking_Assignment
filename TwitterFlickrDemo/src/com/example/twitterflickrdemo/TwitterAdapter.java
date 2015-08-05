package com.example.twitterflickrdemo;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author optimus158
 * 
 *         Adapter Class for providing data to the ListView
 */
public class TwitterAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;

	public TwitterAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View arg1, ViewGroup arg2) {
		LayoutInflater inflator = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		arg1 = inflator.inflate(R.layout.layout_listview, arg2, false);
		TextView textView = (TextView) arg1.findViewById(R.id.textViewTwitter);
		textView.setText(list.get(arg0));
		return arg1;
	}

}
