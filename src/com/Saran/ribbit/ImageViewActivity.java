package com.Saran.ribbit;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageViewActivity extends Activity {
	
	ImageView imageview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_view);
		
		imageview = (ImageView) findViewById(R.id.imageView);
		Uri uri = getIntent().getData();
		Picasso.with(ImageViewActivity.this).load(uri.toString()).into(imageview);
		
		//Timer to close the activity after 10s, if it is still open
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				finish();		
			}
		}, 10*1000);
	}
}
