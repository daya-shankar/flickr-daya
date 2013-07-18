package com.daya.flickr.flickrhelpers;

import javax.xml.parsers.ParserConfigurationException;

import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.RequestContext;
import com.googlecode.flickrjandroid.interestingness.InterestingnessInterface;
import com.googlecode.flickrjandroid.oauth.OAuth;
import com.googlecode.flickrjandroid.oauth.OAuthToken;
import com.googlecode.flickrjandroid.photos.PhotosInterface;

public final class FlickrHelper {

	private static FlickrHelper instance = null;
	private static final String API_KEY = "274e1d3e1b7f14ab86b219d152a63755";
	public static final String API_SEC = "658cd23cf393b53d";

	private FlickrHelper() {

	}

	public static FlickrHelper getInstance() {
		if (instance == null) {
			instance = new FlickrHelper();
		}

		return instance;
	}

	public Flickr getFlickr() {
		try {
			Flickr f = new Flickr(API_KEY, API_SEC, new REST());
			return f;
		} catch (ParserConfigurationException e) {
			return null;
		}
	}

	public Flickr getFlickrAuthed(String token, String secret) {
		Flickr f = getFlickr();
		RequestContext requestContext = RequestContext.getRequestContext();
		OAuth auth = new OAuth();
		auth.setToken(new OAuthToken(token, secret));
		requestContext.setOAuth(auth);
		return f;
	}

	public InterestingnessInterface getInterestingInterface() {
		Flickr f = getFlickr();
		if (f != null) {
			return f.getInterestingnessInterface();
		} else {
			return null;
		}
	}

	public PhotosInterface getPhotosInterface() {
		Flickr f = getFlickr();
		if (f != null) {
			return f.getPhotosInterface();
		} else {
			return null;
		}
	}

}
