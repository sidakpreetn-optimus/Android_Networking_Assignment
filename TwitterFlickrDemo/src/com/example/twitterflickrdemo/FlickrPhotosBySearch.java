package com.example.twitterflickrdemo;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class FlickrPhotosBySearch extends Fragment implements OnClickListener,
		OnItemClickListener {

	// The Flickr API Key from Flickr
	private static final String API_KEY = "70d186cc5ed6bbd1dfc59cf5c4b2ab2e";
	private static final String EXT_STORAGE = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	private static String DATA_URL;

	private EditText etSearch;
	private Button bSearch;
	private GridView gridView;
	private String query;
	private List<FlickrModel> listURL;
	private FlickrAdapter adapter;
	private ProgressDialog dialog;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_flickr, container, false);
		etSearch = (EditText) view.findViewById(R.id.editTextFlickr);
		bSearch = (Button) view.findViewById(R.id.buttonFlickr);
		bSearch.setOnClickListener(this);
		gridView = (GridView) view.findViewById(R.id.gridViewFlickr);
		gridView.setOnItemClickListener(this);

		setupDialog();
		return view;
	}

	/**
	 * @param query
	 * 
	 *            Constructs the URL for fetching JSON from Flickr
	 */
	private void makeURL(String query) {
		DATA_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key="
				+ API_KEY
				+ "&text="
				+ query
				+ "&per_page=20&format=json&nojsoncallback=1";
	}

	/**
	 * Private method for accessing ProgressDialog object
	 */
	private void setupDialog() {
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage("Loading");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
	}

	/*
	 * Called when any button that has registered itself with the listener in
	 * fragment is clicked
	 */
	public void onClick(View v) {
		if (v.getId() == R.id.buttonFlickr) {
			query = etSearch.getText().toString().trim();
			try {
				makeURL(query);
				// Passing the URL to AsyncTask Class for fetching JSONObject
				new fetchData().execute(DATA_URL);
			} catch (Exception ex) {
				Toast.makeText(getActivity(), "searchFlickrMethod",
						Toast.LENGTH_SHORT).show();
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Setting the adapter to gridview and also setting the image through
	 * Model's AsynTask
	 */
	private void populateFlickrPhotosToView() {
		adapter = new FlickrAdapter(getActivity(), listURL);
		gridView.setAdapter(adapter);
		for (FlickrModel model : listURL) {
			model.loadImage(adapter);
		}
	}

	/**
	 * @author optimus158
	 * 
	 *         Class for performing the Networking operation i.e. fetching JSON
	 *         from Flickr
	 */
	private class fetchData extends AsyncTask<String, Long, String> {

		protected void onPreExecute() {
			dialog.show();
		}

		protected String doInBackground(String... arg0) {
			String jsonData = "";
			try {
				// Opens connection the Internet and fetches a JSON String
				Http http = new Http();
				jsonData = http.read(arg0[0]);

			} catch (Exception ex) {
				Toast.makeText(getActivity(), "asynTaskdoInBackground",
						Toast.LENGTH_SHORT).show();
				ex.printStackTrace();
			}
			return jsonData;
		}

		protected void onPostExecute(String result) {
			List<FlickrJSONImage> list = null;
			try {
				JSONObject jsonObject = new JSONObject(result);
				JSONObject jsonData = jsonObject.getJSONObject("photos");
				JSONArray jsonArray = jsonData.getJSONArray("photo");
				Gson gson = new Gson();
				// Extracts the array of JSON Objects from the JSON String
				list = gson.fromJson(jsonArray.toString(),
						new TypeToken<ArrayList<FlickrJSONImage>>() {
						}.getType());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			// Creating FlickrModel List from the FlickrJSONImage list extracted
			// from JSON String
			listURL = new ArrayList<FlickrModel>();
			FlickrModel model = null;
			for (FlickrJSONImage obj : list) {
				model = new FlickrModel();
				model.setImageUrl("http://farm" + obj.getFarm()
						+ ".static.flickr.com/" + obj.getServer() + "/"
						+ obj.getId() + "_" + obj.getSecret() + "_m.jpg");
				listURL.add(model);
			}
			populateFlickrPhotosToView();
			dialog.dismiss();
		}
	}

	/**
	 * @param file
	 * 
	 *            Method for refreshing the gallery so that the wrote image file
	 *            to storage can be viewed when clicked second time
	 */
	private void refreshGallery(File file) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri contentUri = Uri.fromFile(file);
		mediaScanIntent.setData(contentUri);
		getActivity().sendBroadcast(mediaScanIntent);
	}

	/*
	 * Handles the clicking of images in Gridview When clicked first image is
	 * saved to the specified storage path, when same image is clicked second
	 * time gallery app of android displays the clicked image
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		FlickrModel model = listURL.get(arg2);
		try {
			// Creating the directory
			File flickrDir = new File(EXT_STORAGE + "/FlickrPhotos");
			// if doesnot exists then only create
			if (!flickrDir.exists()) {
				flickrDir.mkdirs();
			}
			// Each image is saved by name of its URL
			File flickrPhoto = new File(EXT_STORAGE + "/FlickrPhotos/"
					+ model.getImageUrl().replace('/', '_'));
			// If image file already exists then start gallery else save it to
			// storage
			if (flickrPhoto.exists()) {
				Intent intent = new Intent(
						Intent.ACTION_VIEW,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setDataAndType(
						Uri.parse("file://" + flickrPhoto.getAbsolutePath()),
						"image/*");
				startActivity(intent);
			} else {
				FileOutputStream fOut = new FileOutputStream(flickrPhoto);
				model.getImage()
						.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.close();
				Toast.makeText(getActivity(), "Saved To Gallery",
						Toast.LENGTH_SHORT).show();
				refreshGallery(flickrPhoto);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(getActivity(), "Write to Storage Error",
					Toast.LENGTH_SHORT).show();
		}
	}
}