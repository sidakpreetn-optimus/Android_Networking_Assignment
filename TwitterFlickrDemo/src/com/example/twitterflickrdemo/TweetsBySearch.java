package com.example.twitterflickrdemo;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * @author optimus158
 * 
 *         Fragment Class for Twitter tweets
 */
public class TweetsBySearch extends Fragment implements OnClickListener {

	private Button bSearch;
	private ListView listView;
	private EditText etSearch;
	private List<String> list;
	private TwitterAdapter adapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_twitter, container, false);
		bSearch = (Button) view.findViewById(R.id.buttonTwitter);
		etSearch = (EditText) view.findViewById(R.id.editTextTwitter);
		listView = (ListView) view.findViewById(R.id.listViewTwitter);
		bSearch.setOnClickListener(this);
		return view;
	}

	public void onClick(View v) {
		// calls the getTweets method and sets the result List to the adapter
		if (v.getId() == R.id.buttonTwitter) {
			String query = etSearch.getText().toString();
			list = MainActivity.getTweets(query);
			adapter = new TwitterAdapter(getActivity(), list);
			listView.setAdapter(adapter);
		}
	}
}
