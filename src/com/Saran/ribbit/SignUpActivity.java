package com.Saran.ribbit;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {
	
	protected EditText mUserName, mPassword, mEmail;
	protected Button mSignupButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//To display progressbar spinner. It must be done before setContentView()
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
		
		setContentView(R.layout.activity_sign_up);
		
		//hide ActionBar from signup screen
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		mUserName = (EditText) findViewById(R.id.UsernameSignupField);
		mPassword = (EditText) findViewById(R.id.PasswordSignupField);
		mEmail = (EditText) findViewById(R.id.EmailSignupField);
		
		mSignupButton = (Button) findViewById(R.id.SignupButton);
		mSignupButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = mUserName.getText().toString().trim();
				String password = mPassword.getText().toString().trim();
				String email = mEmail.getText().toString().trim();
				
				if(username.isEmpty()|| password.isEmpty() ||email.isEmpty()){
					AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this); 
					builder.setMessage(R.string.signup_error_message);
					builder.setTitle(R.string.sign_up_error_title);
					builder.setPositiveButton(android.R.string.ok, null);
					
					AlertDialog dialog = builder.create();
					dialog.show();
				}else{
					//Show ProgressBar spinner
					setProgressBarIndeterminateVisibility(true);
					
					//create a new parse user if user input is fine
					ParseUser newUser = new ParseUser();
					newUser.setUsername(username);
					newUser.setPassword(password);
					newUser.setEmail(email);
					
					newUser.signUpInBackground(new SignUpCallback() {
						
						@Override
						public void done(ParseException e) {
							
							//Remove ProgressBar spinner
							setProgressBarIndeterminateVisibility(false);
							
							if(null==e){ //if no exception ie. signup is ok
								
								///if login is fine, route to MainActivity, clear tasks and start new task
								Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}else{
								AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
								builder.setMessage(R.string.signup_error_message);
								builder.setTitle(R.string.sign_up_error_title);
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
