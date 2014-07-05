package com.Saran.ribbit;

import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewActivity extends Activity {
	
	ImageView imageview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		
		imageview = (ImageView) findViewById(R.id.imageView);
		Uri uri = getIntent().getData();
		Picasso.with(ImageViewActivity.this).load(uri.toString()).into(imageview);
		
	}
}
