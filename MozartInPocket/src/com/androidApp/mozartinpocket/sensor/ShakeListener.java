package com.androidApp.mozartinpocket.sensor;

import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.util.Log;

public class ShakeListener implements SensorEventListener
{

	private static final int UPTATE_INTERVAL_TIME = 70;
	private SensorManager sensorManager;
	private Sensor sensor;
	private Vibrator vibrator;
	private OnShakeListener onShakeListener;	
	private Context mContext;
	private long lastUpdateTime;


	public ShakeListener(Context c)
	{
		mContext = c;
		start();
	}

	public void start()
	{

		sensorManager = (SensorManager) mContext
				.getSystemService(Context.SENSOR_SERVICE);
		vibrator = (Vibrator) mContext
				.getSystemService(Service.VIBRATOR_SERVICE);
		if (sensorManager != null)
		{

			sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		}

		if (sensor != null)
		{
			sensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}


	public void stop()
	{
		sensorManager.unregisterListener(this);
	}

	public void setOnShakeListener(OnShakeListener listener)
	{
		onShakeListener = listener;
	}

	public void onSensorChanged(SensorEvent event)
	{
		long currentUpdateTime = System.currentTimeMillis();	
		long timeInterval = currentUpdateTime - lastUpdateTime;
		
		float[] values=event.values;
		if(timeInterval>UPTATE_INTERVAL_TIME){
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)  
			{  
				lastUpdateTime = currentUpdateTime;
				if ((Math.abs(values[0]) > 19 || Math.abs(values[1]) > 19 || Math  
						.abs(values[2]) > 19))  
				{  
					Log.d("x",String.valueOf(values[0]));
					Log.d("y",String.valueOf(values[1]));
					Log.d("z",String.valueOf(values[2]));
					onShakeListener.onShake();
					vibrator.vibrate(500);
				}  
			}  
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{

	}

	public interface OnShakeListener
	{
		public void onShake();
	}
}