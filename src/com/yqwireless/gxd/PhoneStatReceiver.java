package com.yqwireless.gxd;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class PhoneStatReceiver extends BroadcastReceiver {

	private static final String TAG = "PhoneStatReceiver";
	private static WindowManager wm;
	private static DBHelper helper;
	private static TextView tv;
	private boolean view_area;
	private boolean view_area_call_in;
	private boolean view_area_call_out;
	private static boolean incomingFlag = false;

	private static String incoming_number = null;

	@Override
	public void onReceive(Context context, Intent intent) {

		// 显示归属地
		view_area = PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("view_area", true);
		// 去电时显示归属地
		view_area_call_out = PreferenceManager.getDefaultSharedPreferences(
				context).getBoolean("view_area_call_out", true);
		// 来电时显示归属地
		view_area_call_in = PreferenceManager.getDefaultSharedPreferences(
				context).getBoolean("view_area_call_in", true);
		if (view_area && (view_area_call_in || view_area_call_out)) {
			// 拨打电话
			if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
				incomingFlag = false;
				String phoneNumber = intent
						.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				Log.i(TAG, "CALL OUT: " + phoneNumber);
				if (view_area_call_out)
					new ShowArea(context).execute(phoneNumber);
			} else {
				// 来电
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Service.TELEPHONY_SERVICE);

				switch (tm.getCallState()) {
				// 电话等待接听
				case TelephonyManager.CALL_STATE_RINGING:
					incomingFlag = true;
					incoming_number = intent.getStringExtra("incoming_number");
					Log.i(TAG, "CALL IN RINGING :" + incoming_number);
					if (view_area_call_in)
						new ShowArea(context).execute(incoming_number);
					break;
				// 电话摘机
				case TelephonyManager.CALL_STATE_OFFHOOK:
					if (incomingFlag) {
						Log.i(TAG, "CALL IN ACCEPT :" + incoming_number);
					}
					break;
				// 电话挂机
				case TelephonyManager.CALL_STATE_IDLE:
					Log.i(TAG, "CALL IDLE");
					try {
						if (wm != null && tv.getParent() != null)
							wm.removeView(tv);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}

	/**
	 * 异步任务
	 * 
	 * @author hy511
	 * 
	 */
	class ShowArea extends AsyncTask<String, Void, TextView> {

		private Context context;
		WindowManager.LayoutParams params;
		private float mTouchStartX;
		private float mTouchStartY;
		private float x;
		private float y;

		public ShowArea(Context context) {
			this.context = context;
		}

		@Override
		protected TextView doInBackground(String... param) {
			// 构建显示内容
			tv = new TextView(context);
			tv.setTextSize(25);
			tv.setTextColor(context.getResources().getColor(
					android.R.color.white));
			tv.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.call_frame));
			// 得到连接
			helper = DBHelper.getInstance(context);
			String incomingNumber = param[0];
			if (checkOperators(incomingNumber))
				return tv;
			PhoneArea phoneArea;
			if ((incomingNumber != null && incomingNumber.length() >= 7)
					&& ((phoneArea = helper.findPhoneArea((incomingNumber)
							.substring(0, 7))) != null)) {
				tv.setText(phoneArea.getArea());
			} else {
				tv.setText(R.string.none_area);
			}
			return tv;
		}

		private boolean checkOperators(String incoming) {
			if (null == incoming || incoming.length() != 5)
				return false;
			String area = null;
			if ("10086".equals(incoming)) {
				area = "中国移动客服";
			} else if ("10010".equals(incoming)) {
				area = "中国联通客服";
			} else if ("10000".equals(incoming)) {
				area = "中国电信客服";
			}
			if (null == area) {
				return false;
			}
			tv.setText(area);
			return true;
		}

		@Override
		protected void onPostExecute(TextView textView) {
			wm = (WindowManager) context.getApplicationContext()
					.getSystemService(Context.WINDOW_SERVICE);

			params = new WindowManager.LayoutParams();
			params.type = LayoutParams.TYPE_PHONE;
			params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
					| LayoutParams.FLAG_NOT_FOCUSABLE;
			params.gravity = Gravity.TOP | Gravity.LEFT;
			params.x = 20;
			params.y = 5;
			params.width = WindowManager.LayoutParams.WRAP_CONTENT;
			params.height = WindowManager.LayoutParams.WRAP_CONTENT;
			params.format = PixelFormat.RGBA_8888;

			tv.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					x = event.getRawX();
					y = event.getRawY() - 25;
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mTouchStartX = event.getX();
						mTouchStartY = event.getY();

						break;
					case MotionEvent.ACTION_MOVE:
						updateViewPosition();
						break;
					case MotionEvent.ACTION_UP:
						updateViewPosition();
						mTouchStartX = mTouchStartY = 0;
						break;
					}
					return true;
				}
			});

			// 显示
			wm.addView(tv, params);

			int screenWidth = wm.getDefaultDisplay().getWidth();
			int screenHeight = wm.getDefaultDisplay().getHeight();
			x = screenWidth / 4;
			y = screenHeight / 3;
			Log.i(TAG, "x " + x + ", y " + y);
			updateViewPosition();
			textView.removeCallbacks(dismiss);
			textView.postDelayed(dismiss, 10000);

		}

		private void updateViewPosition() {
			// 更新浮动窗口位置参数
			params.x = (int) (x - mTouchStartX);
			params.y = (int) (y - mTouchStartY);
			wm.updateViewLayout(tv, params);

		}

		private Runnable dismiss = new Runnable() {

			@Override
			public void run() {
				if (wm != null && null != tv && tv.getParent() != null)
					wm.removeView(tv);
			}
		};
	}

}
