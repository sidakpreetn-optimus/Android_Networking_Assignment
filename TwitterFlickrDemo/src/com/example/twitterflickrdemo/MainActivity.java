package com.example.twitterflickrdemo;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements TabListener {

	private static final String TWITTER_CALLBACK_URL = "x-oauthflow-twitter://callback";
	private static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	private static final String CONSUMER_KEY = "BKJ5DkkfzDIS5ERJMVMfikCyv";
	private static final String CONSUMER_SECRET = "LuNpJSUipBw39gIP8HHEvS0DZ5dn3UT5uDn3S9u15JXrtoeJ0d";

	private static String AUTH_TOKEN = "";
	private static String AUTH_TOKEN_SECRET = "";
	private static Twitter twitter;
	private static RequestToken requestToken;
	private static AccessToken accessToken;

	private ActionBar actionBar;
	private ViewPager viewPager;

	private static List<String> listOfTweets;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		// Getting the twitter object if null
		if (twitter == null) {
			twitter = new TwitterFactory().getInstance();
		}
		if (accessToken == null) {
			new LoginTwitter().execute("TwitterLogin");
		}
		// Setting up the UI Components
		setupTabsAndViewPager();
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// Syncing the ViewPager with current selected Tab
		viewPager.setCurrentItem(arg0.getPosition());
	}

	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}

	private void setupTabsAndViewPager() {

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int arg0) {
				// Syncing the ActionBar with the ViewPager
				actionBar.setSelectedNavigationItem(arg0);
			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {
			}
		});
		// Setting the animation to the ViewPager
		viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

		Tab twitterTab = actionBar.newTab();
		twitterTab.setText("Twitter");
		twitterTab.setTabListener(this);

		Tab flickrTab = actionBar.newTab();
		flickrTab.setText("Flickr");
		flickrTab.setTabListener(this);

		actionBar.addTab(twitterTab);
		actionBar.addTab(flickrTab);
	}

	/*
	 * This method is called when Activity is loaded from the stack, it requests
	 * the accesstoken from twitter with verifier and requesttoken
	 */
	protected void onNewIntent(Intent intent) {
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
			final String verifier = uri
					.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
			try {
				AccessToken accessToken = twitter.getOAuthAccessToken(
						requestToken, verifier);
				AUTH_TOKEN = accessToken.getToken();
				AUTH_TOKEN_SECRET = accessToken.getTokenSecret();
			} catch (Exception ex) {
				Log.d("SN", ex.getLocalizedMessage() + "");
			}
			super.onNewIntent(intent);
		}
	}

	/**
	 * @param search
	 * @return
	 * 
	 *         This method fetches Tweets for a String query
	 */
	public static List<String> getTweets(String search) {
		listOfTweets = new ArrayList<String>();
		try {
			// Authenticating the twitter instance with consumer key, consumer
			// secret, accesstoken, accesstoken secret
			TwitterFactory twitterFactory = new TwitterFactory();
			Twitter twitter = twitterFactory.getInstance();
			twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
			twitter.setOAuthAccessToken(new AccessToken(AUTH_TOKEN,
					AUTH_TOKEN_SECRET));
			Query query = new Query(search);
			query.count(100);
			// Getting the tweets in a list of Status Object
			List<Status> list = twitter.search(query).getTweets();
			for (int i = 0; i < list.size(); i++) {
				Status temp = list.get(i);
				listOfTweets.add("@" + temp.getUser().getName() + "\n\n"
						+ temp.getText());
			}
		} catch (Exception ex) {
			Log.e("SN", ex.getLocalizedMessage());
		}

		return listOfTweets;
	}

	/**
	 * @author optimus158
	 * 
	 *         This class performs the authentication function Sends the API key
	 *         and gets the request token
	 */
	private class LoginTwitter extends AsyncTask<String, String, String> {

		protected String doInBackground(String... params) {
			try {
				twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
				requestToken = twitter
						.getOAuthRequestToken(TWITTER_CALLBACK_URL);
				MainActivity.this.startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse(requestToken.getAuthenticationURL())));
			} catch (Exception e) {
				Log.e("SN", e.getMessage());
			}
			return null;
		}

	}

}