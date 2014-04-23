package com.example.work;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class splash extends Activity{
protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
	   
		Thread timer = new Thread(){
			public void run(){
				
				try{
					sleep(5000);
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent intent = new Intent("com.example.work.menu");
					startActivity(intent);
				}
				
			}
		};
		timer.start();
	}

	
	@Override
	protected void onPause() {
		
		super.onPause();
	
	}

}
