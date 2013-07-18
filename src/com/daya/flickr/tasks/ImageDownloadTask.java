package com.daya.flickr.tasks;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.daya.flickr.imageutils.ImageCache;
import com.daya.flickr.imageutils.ImageUtils;
import com.daya.flickr.imageutils.ImageUtils.DownloadedDrawable;

//takes URL as paramenter and sets bitmap associated with the imageview

public class ImageDownloadTask extends AsyncTask<String, Integer, Bitmap> {
	// private static final Logger logger =
	// LoggerFactory.getLogger(ImageDownloadTask.class);
	private WeakReference<ImageView> imgRef = null;
	private String mUrl;

	public ImageDownloadTask(ImageView imageView) {
		this.imgRef = new WeakReference<ImageView>(imageView);
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		mUrl = params[0];
		Bitmap image = ImageCache.getFromCache(mUrl);
		if (image != null) {
			return image;
		}
		return ImageUtils.downloadImage(mUrl);
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		if (this.isCancelled()) {
			result = null;
			return;
		}

		ImageCache.saveToCache(this.mUrl, result);
		if (imgRef != null) {
			ImageView imageView = imgRef.get();
			ImageDownloadTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

			if (this == bitmapDownloaderTask && bitmapDownloaderTask != null) {
				imageView.setImageBitmap(result);
			}
		}

	}

	public String getUrl() {
		return this.mUrl;
	}

	private ImageDownloadTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}
}
