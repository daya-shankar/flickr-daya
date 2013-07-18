package com.daya.flickr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.daya.flickr.tasks.LoadPhotostreamTask;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.people.User;

public class Tab1Fragment extends Fragment {

	public static final String CALLBACK_SCHEME = "flickrj-android-sample-oauth"; //$NON-NLS-1$
	public static final String PREFS_NAME = "flickrj-android-sample-pref"; //$NON-NLS-1$
	public static final String KEY_OAUTH_TOKEN = "flickrj-android-oauthToken"; //$NON-NLS-1$
	public static final String KEY_TOKEN_SECRET = "flickrj-android-tokenSecret"; //$NON-NLS-1$
	public static final String KEY_USER_NAME = "flickrj-android-userName"; //$NON-NLS-1$
	public static final String KEY_USER_ID = "flickrj-android-userId"; //$NON-NLS-1$

	private static final Logger logger = LoggerFactory
			.getLogger(MainActivity.class);

	View view;
	ListView lv;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		lv = (ListView) view.findViewById(R.id.listView1);

		load(getOAuthToken());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		System.out.println("-----tab 1 onResume called----");
		load(getOAuthToken());
	}

	public OAuth getOAuthToken() {
		// Restore preferences
		SharedPreferences settings = getActivity().getSharedPreferences(
				PREFS_NAME, Context.MODE_PRIVATE);
		String oauthTokenString = settings.getString(KEY_OAUTH_TOKEN, null);
		String tokenSecret = settings.getString(KEY_TOKEN_SECRET, null);
		if (oauthTokenString == null && tokenSecret == null) {
			logger.warn("No oauth token retrieved"); //$NON-NLS-1$
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
		logger.debug(
				"Retrieved token from preference store: oauth token={}, and token secret={}", oauthTokenString, tokenSecret); //$NON-NLS-1$
		return oauth;
	}

	private void load(OAuth oauth) {
		if (oauth != null) {
			new LoadPhotostreamTask(getActivity(), lv).execute(oauth);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		view = inflater.inflate(R.layout.tab_frag1_layout, container, false);
		return view;
	}
}