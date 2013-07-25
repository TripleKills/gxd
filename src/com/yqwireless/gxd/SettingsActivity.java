package com.yqwireless.gxd;

import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * 设置界面Activity
 * @author hy511
 *
 */
public class SettingsActivity extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

	}
	
	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		MobclickAgent.onPause(this);
		super.onPause();
	}
}
