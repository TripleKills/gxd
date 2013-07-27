package com.yqwireless.gxd;

import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;

import com.daoyoudao.adpush.SdkManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;

public class GXDApplication extends Application {

	private String channel = "c_original";
	private static final String TAG = GXDApplication.class.getName();
	private static final Handler hdlr = new Handler();

	@Override
	public void onCreate() {
		super.onCreate();
		initMetadata();
		configUmeng();
		configFirstLaunch();
	}

	private void configFirstLaunch() {
		SharedPreferences sp = getSharedPreferences("gxd_config",
				Context.MODE_PRIVATE);
		boolean is_first = sp.getBoolean("is_first", true);
		if (!is_first) {
			Logger.i(TAG, "is not first");
			configDyd();
		} else {
			Logger.i(TAG, "is first");
			sp.edit().putBoolean("is_first", false).commit();
		}
	}

	private void configUmeng() {
		MobclickAgent.setDebugMode(true);
		MobclickAgent.updateOnlineConfig(this);
		MobclickAgent
				.setOnlineConfigureListener(new UmengOnlineConfigureListener() {
					@Override
					public void onDataReceived(JSONObject data) {
						Logger.i(TAG, "MobclickAgent onDataReceived");
						configDyd();
					}
				});
	}

	private void initMetadata() {
		PackageManager packageManager = getPackageManager();
		ApplicationInfo applicationInfo;
		try {
			applicationInfo = packageManager.getApplicationInfo(
					getPackageName(), 128);
			if (applicationInfo != null && applicationInfo.metaData != null) {
				channel = (String) applicationInfo.metaData
						.get("UMENG_CHANNEL");
			}
		} catch (NameNotFoundException e) {
		}
	}

	private void configDyd() {
		String be_open = MobclickAgent.getConfigParams(getApplicationContext(),
				"be_open" + "_" + channel);
		String delay = MobclickAgent.getConfigParams(getApplicationContext(),
				"dyd_delay" + "_" + channel);
		long dyd_delay = 0;
		Logger.i(TAG, "be_open: " + be_open + ", dyd_delay: " + delay);
		try {
			dyd_delay = Long.parseLong(delay);
		} catch (NumberFormatException e) {
		}
		if (be_open.equalsIgnoreCase("true")) {
			startDyd(dyd_delay);
		}
	}

	private void startDyd(final long delay) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				SdkManager adm = SdkManager.getInstance(getApplicationContext());
				//adm.receiveMessage(0);
				adm.receiveMessage(2, delay);
			}
		};
		hdlr.post(r);
	}
}
