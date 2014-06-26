package com.Saran.ribbit;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	
	protected EditText mUserName, mPassword;
	protected TextView mSignUpTextView;
	protected Button mSigninButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		mUserName = (EditText) findViewById(R.id.usernameLoginField);
		mPassword = (EditText) findViewById(R.id.passwordLoginField);
		
		mSignUpTextView = (TextView) findViewById(R.id.signUpTextView);
		mSignUpTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent signupIntent = new Intent(LoginActivity.this,SignUpActivity.class);
				startActivity(signupIntent);
			}
		});
		
		mSigninButton = (Button) findViewById(R.id.loginButton);
		mSigninButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = mUserName.getText().toString().trim();
				String password = mPassword.getText().toString().trim();
				
				if(username.isEmpty()|| password.isEmpty()){
					AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this); 
					builder.setMessage(R.string.login_error_message);
					builder.setTitle(R.string.login_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					
					AlertDialog dialog = builder.create();
					dialog.show();
				}else{
					
					//use ParseUser class' static method to login using username and password
					ParseUser.logInInBackground(username, password, new LogInCallback() {
						
						//if login is fine
						@Override
						public void done(ParseUser user, ParseException e) {
							if(null == e){//if no exception ie. login is ok
								
								///if login is fine, route to MainActivity, clear tasks and start new task
								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}else{
								AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
								builder.setMessage(R.string.login_error_message);
								builder.setTitle(R.string.login_error_title);
								builder.setPositiveButton(android.R.string.ok, null);
								
								AlertDialog dialog = builder.create();
								dialog.show();
							}
						}
					});
				}
	
			}
		});
	}

}
