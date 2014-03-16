package akari.jp.base;

import android.os.Handler;
import android.os.SystemClock;

public class CountUpTimer {
	private Handler myHandler = new Handler();
	long startTime = 0L;
	long timeInMillies = 0L;
	long timeSwap = 0L;
	long finalTime = 0L;
	int minutes;
	int seconds;
	
	private Runnable updateTimerMethod = new Runnable() {
		
		@Override
		public void run() {
			
			timeInMillies = SystemClock.uptimeMillis() - startTime;
			finalTime = timeSwap + timeInMillies;

			seconds = (int) (finalTime / 1000);
			minutes = seconds / 60;
		   	seconds = seconds % 60;
//		   	int milliseconds = (int) (finalTime % 1000);
//			   textTimer.setText("" + minutes + ":"
//			     + String.format("%02d", seconds) + ":"
//			     + String.format("%03d", milliseconds));
		   	myHandler.postDelayed(this, 0);			
		}
	};
	
	public void startTimer(){
		startTime = SystemClock.uptimeMillis();
		myHandler.postDelayed(updateTimerMethod, 0);
	}
	
	public void pauseTimer(){
		timeSwap += timeInMillies;
	    myHandler.removeCallbacks(updateTimerMethod);
	}
	
	public int getMinutes() {
		return minutes;
	}


	public int getSeconds() {
		return seconds;
	}


	
}
