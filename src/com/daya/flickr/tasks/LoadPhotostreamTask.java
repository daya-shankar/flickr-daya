/**
 * 
 */
package com.daya.flickr.tasks;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ListView;

import com.daya.flickr.flickrhelpers.FlickrHelper;
import com.daya.flickr.flickrhelpers.FlickrjActivity;
import com.daya.flickr.imageutils.LazyAdapter;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.PhotoList;

public class LoadPhotostreamTask extends AsyncTask<OAuth, Void, PhotoList> {

	public static final String CALLBACK_SCHEME = "flickrj-android-sample-oauth";
	public static final String PREFS_NAME = "flickrj-android-sample-pref";
	public static final String KEY_OAUTH_TOKEN = "flickrj-android-oauthToken";
	public static final String KEY_TOKEN_SECRET = "flickrj-android-tokenSecret";
	public static final String KEY_USER_NAME = "flickrj-android-userName";
	public static final String KEY_USER_ID = "flickrj-android-userId";

	private ListView listView;
	private Activity activity;

	public LoadPhotostreamTask(Activity activity, ListView listView) {
		this.activity = activity;
		this.listView = listView;
	}

	@Override
	protected PhotoList doInBackground(OAuth... arg0) {
		OAuthToken token = arg0[0].getToken();

		// System.out.println("-----token is-----"+token.getOauthToken());

		if (token.getOauthToken() == null) {

			Intent intent = new Intent(activity, FlickrjActivity.class);
			activity.startActivity(intent);
		}

		if (token.getOauthToken() != null) {

			Flickr f = FlickrHelper.getInstance().getFlickrAuthed(
					token.getOauthToken(), token.getOauthTokenSecret());
			Set<String> extras = new HashSet<String>();
			extras.add("url_sq");
			extras.add("url_l");
			extras.add("views");
			User user = arg0[0].getUser();
			try {
				return f.getPeopleInterface().getPhotos(user.getId(), extras,
						20, 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public OAuth getOAuthToken() {
		// Restore preferences
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		String oauthTokenString = settings.getString(KEY_OAUTH_TOKEN, null);
		String tokenSecret = settings.getString(KEY_TOKEN_SECRET, null);
		if (oauthTokenString == null && tokenSecret == null) {
			//logger.warn("No oauth token retrieved"); //$NON-NLS-1$
			return null;
		}
		OAuth oauth = new OAuth();
		String userName = settings.getString(KEY_USER_NAME, null);
		String userId = settings.getString(KEY_USER_ID, null);
		if (userId != null) {
			User user = new User();
			user.setUsername(userName);
			user.setId(userId);
			oauth.setUser(user);
		}
		OAuthToken oauthToken = new OAuthToken();
		oauth.setToken(oauthToken);
		oauthToken.setOauthToken(oauthTokenString);
		oauthToken.setOauthTokenSecret(tokenSecret);
		// logger.debug(
		//				"Retrieved token from preference store: oauth token={}, and token secret={}", oauthTokenString, tokenSecret); //$NON-NLS-1$
		return oauth;
	}

	@Override
	protected void onPostExecute(PhotoList result) {
		if (result != null) {
			LazyAdapter adapter = new LazyAdapter(this.activity, result);
			this.listView.setAdapter(adapter);
		}
	}

}