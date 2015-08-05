package com.example.twitterflickrdemo;

import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;

/**
 * @author optimus158
 * 
 *         Model Class for each Image from Flickr, it would have an url, bitmap
 *         and an adapter reference for updating the GridView
 */
public class FlickrModel {

	private String imageUrl;
	private Bitmap image;
	private FlickrAdapter adapter;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Bitmap getImage() {
		return image;
	}

	public FlickrAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(FlickrAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * @param adapter
	 * 
	 *            Executes the URL for fetching the image from Flickr and stores
	 *            in this FlickrModel Object
	 */
	public void loadImage(FlickrAdapter adapter) {

		this.adapter = adapter;
		if (imageUrl != null && !imageUrl.equals("")) {
			new getImageFromURL().execute(imageUrl);
		}
	}

	/**
	 * @author optimus158
	 * 
	 *         AsynTask for this Model Class for providing Lazy Loading of
	 *         GridView
	 */
	private class getImageFromURL extends AsyncTask<String, String, Bitmap> {

		protected Bitmap doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection()
						.getInputStream());
				// Extracting the thumbnail for displaying it in GridView
				return ThumbnailUtils.extractThumbnail(bitmap, 200, 150);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		protected void onPostExecute(Bitmap result) {
			image = result;
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
		}

	}
}
