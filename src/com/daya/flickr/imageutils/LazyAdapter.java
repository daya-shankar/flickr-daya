/**
 * 
 */
package com.daya.flickr.imageutils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daya.flickr.R;
import com.daya.flickr.imageutils.ImageUtils.DownloadedDrawable;
import com.daya.flickr.tasks.ImageDownloadTask;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;

//for listview in first fragment
public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private PhotoList photos;
	private static LayoutInflater inflater = null;

	public LazyAdapter(Activity a, PhotoList d) {
		activity = a;
		photos = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.row, null);

		TextView text = (TextView) vi.findViewById(R.id.imageTitle);
		;
		ImageView image = (ImageView) vi.findViewById(R.id.imageIcon);
		Photo photo = photos.get(position);
		text.setText(photo.getTitle());
		if (image != null) {
			ImageDownloadTask task = new ImageDownloadTask(image);
			Drawable drawable = new DownloadedDrawable(task);
			image.setImageDrawable(drawable);
			task.execute(photo.getSmallSquareUrl());
		}

		return vi;
	}
}
