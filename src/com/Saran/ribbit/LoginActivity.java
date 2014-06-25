package com.Saran.ribbit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	protected TextView mSignUpTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mSignUpTextView = (TextView) findViewById(R.id.signUpTextView);
		mSignUpTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent signupIntent = new Intent(LoginActivity.this,SignUpActivity.class);
				startActivity(signupIntent);
			}
		});
	}

}
