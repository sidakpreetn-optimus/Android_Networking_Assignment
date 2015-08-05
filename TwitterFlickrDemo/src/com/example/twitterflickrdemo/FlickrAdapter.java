package com.example.twitterflickrdemo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * @author optimus158
 * 
 *         Adapter Class for setting the GridView
 */
public class FlickrAdapter extends BaseAdapter {

	private Context context;
	private List<FlickrModel> list = new ArrayList<FlickrModel>();

	/**
	 * @param context
	 * @param list
	 * 
	 *            This Constructor receives the Context and the an ArrayList of
	 *            FlickrModel class
	 */
	public FlickrAdapter(Context context, List<FlickrModel> list) {
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
		View view = arg1;
		FlickrModel model = list.get(arg0);
		ViewHolder viewHolder = new ViewHolder();
		if (view == null) {
			LayoutInflater inflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflator.inflate(R.layout.layout_gridview, null);
			viewHolder.pic = (ImageView) view.findViewById(R.id.imageViewGridView);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		// if object has null object set the default image else set the image
		// from FlickrModel object
		if (model.getImage() != null) {
			viewHolder.pic.setImageBitmap(model.getImage());
		} else {
			viewHolder.pic.setImageDrawable(context.getResources().getDrawable(
					R.drawable.loaderexperiment_teaser));
		}
		return view;
	}

	// Holder Class for holding the view
	static class ViewHolder {
		ImageView pic;
	}

}
