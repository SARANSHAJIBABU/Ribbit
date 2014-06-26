package com.Saran.ribbit;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class RibbitApplication extends Application {
	
	@Override
	public void onCreate() {
		//initialize parse communication
		  Parse.initialize(this, "xDwjfruBpZnZXJ6D1xwE9UkYGvgl5Uok4ZLVd33Q", "W4CQ5pvgEnXQQ2mQW3AsOX89XUKktxTU1vco9Fr5");
		}

}
