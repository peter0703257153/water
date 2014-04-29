package com.example.work;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;


public class TakeAPicture extends Activity implements SurfaceHolder.Callback,OnTouchListener{
	 Camera camera;
	ShutterCallback shutter;
	PictureCallback raw;
	
	Mat imgToProcess;
	String t;
    TextView ts;
	
	private static final String TAG = "CameraActivity";
	
	private Socket client;
	private PrintWriter printwriter;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		   
		SurfaceView surface = (SurfaceView)findViewById(R.id.surfaceView1);
		SurfaceHolder holder = surface.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.setFixedSize(400, 300);
		 camera = Camera.open();
		 surface.setOnTouchListener(TakeAPicture.this);
		
		ts=(TextView) findViewById(R.id.textView1);
	    ts.setTextColor(Color.RED);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
	

		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "IO Exception", e);
		}
		
		
	}



	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		takePicture();
		return false;
	}


	
	private void takePicture() {
		camera.takePicture(shutterCallback, null,callback);
		
        
		}
	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// TODO Do something when the shutter closes.
			}
	};
	PictureCallback callback = new PictureCallback() {
       @Override
       public void onPictureTaken(byte[] data, Camera camera) {
           Log.i(TAG, "Saving a bitmap to file");
           
          
           if (OpenCVLoader.initDebug()) { 
           	 Log.d("work", "work");
           	Bitmap picture = BitmapFactory.decodeByteArray(data, 0, data.length);
               Log.i("camera open", "n");
              
               imgToProcess=new Mat();
               
                 Utils.bitmapToMat(picture, imgToProcess);
                 Log.d("work", "work");

               Imgproc.cvtColor(imgToProcess, imgToProcess, Imgproc.COLOR_RGB2GRAY);
               /*int c=imgToProcess.rows()*imgToProcess.cols();
               int zero=c-Core.countNonZero(imgToProcess);
               int f=Core.countNonZero(imgToProcess);
              int b=(f/zero);
              String vv;
              vv=Integer.toString(b);
              ts.setText(vv);*/
               int erosion_size = 2;
               
               Mat element  = Imgproc.getStructuringElement(
                   Imgproc.MORPH_RECT, new Size(2 * erosion_size + 1, 2 * erosion_size + 1), 
                   new Point(erosion_size, erosion_size)
               );
               
               Imgproc.erode(imgToProcess, imgToProcess, element);
               
                t = Core.mean(imgToProcess).toString();
                float v=Float.parseFloat(t);
                if(v<65.99){
                	Toast.makeText(TakeAPicture.this, "water is not safe", Toast.LENGTH_LONG).show();
                }
                 ts.setText(t);
                 
                 Utils.matToBitmap(imgToProcess, picture);
                 String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                 File file = new File(extStorageDirectory, "peter.PNG");
                 FileOutputStream outStream = null;
       		try {
       			outStream = new FileOutputStream(file);
       		} catch (FileNotFoundException e) {
       			// TODO Auto-generated catch block
       			e.printStackTrace();
       		}
       		picture.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                 
                 try {
       			outStream.flush();
       		} catch (IOException e) {
       			// TODO Auto-generated catch block
       			e.printStackTrace();
       		}
                 try {
       			outStream.close();
       		} catch (IOException e) {
       			// TODO Auto-generated catch block
       			e.printStackTrace();
       		}
                 
           }
           
        /*   try {
    			  
   		     client = new Socket("10.10.6.105", 4444); //connect to server
   		      
   		     printwriter = new PrintWriter(client.getOutputStream(),true);
   		     printwriter.write(t);  //write the message to output stream
   		 
   		     printwriter.flush();
   		     printwriter.close();
   		     client.close();   //closing the connection
   		 
   		    } catch (UnknownHostException e) {
   		     e.printStackTrace();
   		    } catch (IOException e) {
   		     e.printStackTrace();
   		    }*/
           
           
           camera.stopPreview();
           camera.startPreview();
          
}
      
	
	
	
	};
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	
		camera.setPreviewCallback(null);
		camera.stopPreview();
		
		camera.release();
		
	}

	protected void onPause() {
		super.onPause();
		
		finish();	
	}
	

}