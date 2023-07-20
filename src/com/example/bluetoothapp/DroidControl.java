package com.example.bluetoothapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DroidControl extends Activity {
	
	private static final String TAG = DroidControl.class.getSimpleName();
	private static final boolean D = true;
	// Intent request codes
  private static final int REQUEST_CONNECT_DEVICE = 1;
  private static final int REQUEST_ENABLE_BT = 2;
  public static final int button_id = 0;
  // Message types sent from the BluetoothChatService Handler
  public static final int MESSAGE_STATE_CHANGE = 1;
  public static final int MESSAGE_READ = 2;
  public static final int MESSAGE_WRITE = 3;
  public static final int MESSAGE_DEVICE_NAME = 4;
  public static final int MESSAGE_TOAST = 5;
  
  // Key names received from the BluetoothCommandService Handler
  public static final String DEVICE_NAME = "device_name";
  public static final String TOAST = "toast";
  private OutputStream outStream = null;
  private InputStream inStream = null;
  private BluetoothSocket btSocket = null;
  private BluetoothAdapter mBluetoothAdapter = null;
  private static final UUID MY_UUID =
  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
  private static String address = "00:06:66:44:FA:6E";
  private TextView textView;
  //    private BluetoothCommandService mCommandService = null;
  
  /*
    Handler handler1 = new Handler()
    {
    @Override public void handleMessage(Message msg)
    {
    if (D) Log.e(TAG, "+++ HANDLE MSG +++");
    
    }
    };
  */
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    if (D) Log.e(TAG, "+++ ON CREATE +++");
    
    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    
    if (mBluetoothAdapter == null) {
      Toast.makeText(this,"Bluetooth is not available.",Toast.LENGTH_LONG).show();
      finish();
      return;
    }
    
    if (!mBluetoothAdapter.isEnabled()) {
      Toast.makeText(this,"Please enable your BT and re-run this program.",Toast.LENGTH_LONG).show();
      finish();
      return;
    }
    textView = (TextView) findViewById(R.id.textView);
  }
  
  @Override
  public void onStart() {
    super.onStart();
    if (D) Log.e(TAG, "++ ON START ++");
    // if (mCommandService==null)
    // mCommandService = new BluetoothCommandService(this, handler1);
  }
  
  @Override
  public void onResume() {
    super.onResume();
    
    if (D) {Log.e(TAG, "+ ON RESUME +"); Log.e(TAG, "+ ABOUT TO ATTEMPT CLIENT CONNECT +");}
    //            
    //			mCommandService.start();
    
    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
    try {
      btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
    } catch (IOException e) {Log.e(TAG, "ON RESUME: Socket creation failed.", e);}
    
    mBluetoothAdapter.cancelDiscovery();
    
    try {
      btSocket.connect();
      textView.setText("BT connection established\ndata transfer link open");
      Log.e(TAG, "ON RESUME: BT connection established, data transfer link open.");
      } catch (IOException e) {
      try {
        btSocket.close();
      } catch (IOException e2) { Log.e(TAG, "ON RESUME: Unable to close socket during connection failure", e2);}
    }
    
    if (D) Log.e(TAG, "+ ABOUT TO SAY SOMETHING TO SERVER +");
    
    try {
      outStream = btSocket.getOutputStream();
      // inStream = btSocket.getInputStream();
      // mBluetoothThread = new BluetoothThread(this, mHandler, inStream);
      // new ReadInput().execute(inStream);
      } catch (IOException e) {
      textView.setText("ON RESUME: Output stream creation failed");
      Log.e(TAG, "ON RESUME: Output stream creation failed.", e);
    }
    // Thread thread = new Thread(commRunnable, TAG);
    // thread.start();
  }
  
  @Override
  public void onPause() {
    super.onPause();
    if (D) Log.e(TAG, "- ON PAUSE -");
    
    if (outStream != null) {
      try {
        outStream.flush();
      } catch (IOException e) {Log.d(TAG, "ON PAUSE: Couldn't flush output stream.", e);}
    }
    
    try     {
      btSocket.close();
    } catch (IOException e2) {Log.d(TAG, "ON PAUSE: Unable to close socket.", e2);}
  }
  
  @Override
  public void onStop() {
    super.onStop();
    if (D)
      Log.d(TAG, "-- ON STOP --");
  }
  
  @Override
	protected void onDestroy() {
		super.onDestroy();
    // if (mCommandService != null)
    //    mCommandService.stop();
  }
  
  Runnable commRunnable = new Runnable() {
    public void run() {
    
      /*   	int ret = 0;
	    	final byte[] buffer = new byte[255];
	    	while (ret >= 0) {
        try {
        ret = inStream.read(buffer);
        } catch (IOException e) {break;}
        
        runOnUiThread(new Runnable() {
        public void run() {
        String s = new String(buffer);
        textView.setText(s);
        }
        });
      }  
      */
    
    }
  };
  
}