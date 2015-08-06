package remote.phone.model;

import android.os.CountDownTimer;

public abstract class PausableCountDownTimer {
	private long timeToEnd;
	private CountDownTimer cdt;
	private boolean running;

	public PausableCountDownTimer(int milis) {
		running = false;
		cdt = newCDT(milis);
		timeToEnd = milis;
	}

	public void pause() {
		setTime(timeToEnd);
	}

	public void resume() {
		running = true;
		cdt.start();
	}

	public void setTime(long milis) {
		running = false;
		cdt.cancel();
		timeToEnd = milis;
		cdt = newCDT(milis);
	}

	private CountDownTimer newCDT(long milis) {
		return new CountDownTimer(milis, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				timeToEnd = millisUntilFinished;
				onCountDownTick(millisUntilFinished);
			}

			@Override
			public void onFinish() {
				onCountDownFinish();
			}
		};
	}

	protected abstract void onCountDownFinish();

	protected abstract void onCountDownTick(long millisUntilFinished);

	public boolean isRunning() {
		return running;
	}

}
