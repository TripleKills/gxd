package com.yqwireless.gxd;

import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;

import android.app.Application;

public class GXDApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		MobclickAgent.setDebugMode(true);
		MobclickAgent.updateOnlineConfig(this);
		MobclickAgent
				.setOnlineConfigureListener(new UmengOnlineConfigureListener() {
					@Override
					public void onDataReceived(JSONObject data) {
						String be_open = MobclickAgent.getConfigParams(
								getApplicationContext(), "be_open");
						if (be_open.equalsIgnoreCase("true")) {
							// open be

						}
					}
				});
	}
}
