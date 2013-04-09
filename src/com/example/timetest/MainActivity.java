package com.example.timetest;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import at.abraxas.amarino.Amarino;
import at.abraxas.amarino.AmarinoIntent;

public class MainActivity extends Activity {

	private TextView value;
	private ArduinoReceiver arduinoReceiver = new ArduinoReceiver();
	private static final String DEVICE_ADDRESS =  "00:12:12:04:32:51";
	private int x = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		value = (TextView) findViewById(R.id.value);
		value.setText(Integer.toString(x));
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(arduinoReceiver, new IntentFilter(AmarinoIntent.ACTION_RECEIVED));
		Amarino.connect(this, DEVICE_ADDRESS);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Amarino.disconnect(this, DEVICE_ADDRESS);
		unregisterReceiver(arduinoReceiver);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class ArduinoReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String data = null;
			final int dataType = intent.getIntExtra(AmarinoIntent.EXTRA_DATA_TYPE, -1);
			if (dataType == AmarinoIntent.STRING_EXTRA){
					data = intent.getStringExtra(AmarinoIntent.EXTRA_DATA);
					x = x + 1;
					if (data != null){
						value.setText(Integer.toString(x));
					}
			}
		}
	}
}
