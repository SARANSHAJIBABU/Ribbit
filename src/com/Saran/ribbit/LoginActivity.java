package com.Saran.ribbit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends Activity {
	
	protected EditText mUserName, mPassword;
	protected TextView mSignUpTextView;
	protected Button mSigninButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//To display progressbar spinner. It must be done before setContentView()
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_login);
		
		//hide ActionBar from login screen
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
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
					//Show ProgressBar spinner
					setProgressBarIndeterminateVisibility(true);
					
					//use ParseUser class' static method to login using username and password
					ParseUser.logInInBackground(username, password, new LogInCallback() {
						
						//if login is fine
						@Override
						public void done(ParseUser user, ParseException e) {
							
							//Remove ProgressBar spinner
							setProgressBarIndeterminateVisibility(false);

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
