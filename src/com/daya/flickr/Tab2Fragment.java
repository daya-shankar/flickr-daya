package com.daya.flickr;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daya.flickr.flickrhelpers.FlickrjActivity;

public class Tab2Fragment extends Fragment {

	private static final String API_KEY = "274e1d3e1b7f14ab86b219d152a63755"; //$NON-NLS-1$
	public static final String API_SEC = "658cd23cf393b53d"; //$NON-NLS-1$
	View view;
	static final int CHOOSE_GALLERY_REQUEST = 102;
	static final int CAMERA_PIC_REQUEST = 103;
	Uri mCapturedImageURI;

	File fileUri;
	private EditText et_ImageName;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Button btnFlickr = (Button) view.findViewById(R.id.button_upload);
		btnFlickr.setOnClickListener(mFlickrClickListener);

		Button btnPick = (Button) view.findViewById(R.id.button_pick);
		btnPick.setOnClickListener(mPickClickListener);

		et_ImageName = (EditText) view.findViewById(R.id.editText_imageName);
	}

	View.OnClickListener mFlickrClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (fileUri == null) {
				Toast.makeText(getActivity().getApplicationContext(),
						"Please pick photo", Toast.LENGTH_SHORT).show();

				return;
			}

			Intent intent = new Intent(getActivity().getApplicationContext(),
					FlickrjActivity.class);
			intent.putExtra("flickImagePath", fileUri.getAbsolutePath());
			intent.putExtra("flickrImageName", et_ImageName.getText()
					.toString());
			startActivity(intent);
		}
	};

	View.OnClickListener mPickClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Choose photo source");
			builder.setMessage("Choose your photo from?");
			builder.setCancelable(true);

			builder.setPositiveButton("From Gallery",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
							startActivityForResult(intent,
									CHOOSE_GALLERY_REQUEST);
						}
					});

			builder.setNegativeButton("From Camera",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							ContentValues values = new ContentValues();
							values.put(MediaColumns.TITLE, "ImageTemp");
							mCapturedImageURI = getActivity()
									.getContentResolver()
									.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
											values);
							Intent intentPicture = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							intentPicture.putExtra(MediaStore.EXTRA_OUTPUT,
									mCapturedImageURI);
							startActivityForResult(intentPicture,
									CAMERA_PIC_REQUEST);
						}
					});

			Dialog chooseDialog = builder.create();
			chooseDialog.show();
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}

		view = inflater.inflate(R.layout.tab_frag2_layout, container, false);
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CHOOSE_GALLERY_REQUEST
				|| requestCode == CAMERA_PIC_REQUEST) {

			if (resultCode == Activity.RESULT_OK && data != null) {

				Uri tmp_fileUri = data.getData();

				if (tmp_fileUri == null) {
					tmp_fileUri = mCapturedImageURI;
				}

				System.out.println("-----uri is----" + tmp_fileUri);

				((ImageView) view.findViewById(R.id.imageView1))
						.setImageURI(tmp_fileUri);

				String selectedImagePath = getPath(tmp_fileUri);
				fileUri = new File(selectedImagePath);
				Log.e("", "fileUri : " + fileUri.getAbsolutePath());
			}

			else if (resultCode == Activity.RESULT_OK && data == null) {

				Uri tmp_fileUri = mCapturedImageURI;
				System.out.println("-----uri is----" + tmp_fileUri);

				((ImageView) view.findViewById(R.id.imageView1))
						.setImageURI(tmp_fileUri);

				String selectedImagePath = getPath(tmp_fileUri);
				fileUri = new File(selectedImagePath);
				Log.e("", "fileUri : " + fileUri.getAbsolutePath());
			}
		}
	}

	public String getPath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = getActivity().managedQuery(uri, projection, null, null,
				null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	};
}