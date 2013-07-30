package com.yqwireless.gxd;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class MainActivity extends Activity {
	private TextView mInput, show_text;
	private static DBHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mInput = (TextView) findViewById(R.id.digits);
		show_text = (TextView) findViewById(R.id.show_text);

		// 初次使用将准备好的数据库文件考入到系统目录共程序使用
		DBHelper.copyDB(getBaseContext());
		// 获得数据库连接
		helper = DBHelper.getInstance(this);

		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();

		UmengUpdateAgent.update(this);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// 设置菜单
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		// 关于菜单
		case R.id.action_about:
			/*LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			final View dialog_view = inflater.inflate(R.layout.dialog_view,
					null);
			TextView about_text_versoin = (TextView) dialog_view
					.findViewById(R.id.about_text_version);
			String about_version = getString(R.string.about_version);
			about_version = about_version.replace("1.0", "   "+ getVersionName());
			about_text_versoin.setText(about_version);
			new AlertDialog.Builder(this).setTitle(R.string.action_about)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(dialog_view).setPositiveButton("确定", null).show();*/
			showAbout();
			break;
		case R.id.action_feedback:
			FeedbackAgent agent = new FeedbackAgent(this);
			agent.startFeedbackActivity();
			break;
		case R.id.action_check_update:
			UmengUpdateListener updateListener = new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus,
						UpdateResponse updateInfo) {
					Log.i("--->", "callback result =>" + updateStatus);
					switch (updateStatus) {

					case 0: // has update
						UmengUpdateAgent.showUpdateDialog(MainActivity.this,
								updateInfo);
						break;
					case 1: // has no update
						Toast.makeText(MainActivity.this, "已是最新版本",
								Toast.LENGTH_SHORT).show();
						break;
					case 2: // none wifi
						Toast.makeText(MainActivity.this,
								"没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT)
								.show();
						break;
					case 3: // time out
						Toast.makeText(MainActivity.this, "检查更新超时",
								Toast.LENGTH_SHORT).show();
						break;
					}

				}
			};
			UmengUpdateAgent.update(this);
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(updateListener);
			Toast.makeText(this, "正在检查更新...", Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}
	
	private void showAbout() {
		BaseDialog dialog = new CustomDialog2(MainActivity.this);
		
		View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.about, null);
		String version = getString(R.string.version) + ": " + getVersionName();
		String weibo = getString(R.string.weibo) + ": " + getString(R.string.weibo_content);
		String email = getString(R.string.email) + ": " + getString(R.string.email_content);
		String title = getString(R.string.about);
		TextView versionT = (TextView) v.findViewById(R.id.about_version);
		TextView weiboT = (TextView) v.findViewById(R.id.about_weibo);
		TextView emailT = (TextView) v.findViewById(R.id.about_email);
		TextView titleT = (TextView) v.findViewById(R.id.about_title);
		versionT.setText(version);
		weiboT.setText(weibo);
		emailT.setText(email);
		titleT.setText(title);
		dialog.setContentView(v);
		dialog.show();
	}

	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			return "1.0";
		}

	}

	public void onBtnClick(View view) {
		switch (view.getId()) {
		/*case R.id.deleteButton:
			delChar();
			break;*/
		case R.id.btn_del:
			delChar();
			break;
		case R.id.btn_clear:
			clearChar();
			break;
		default:
			addChar(((TextView) view).getText().toString());
		}
		String searchText = mInput.getText().toString();
		if (TextUtils.isEmpty(searchText)) {
		}
	}

	private void delChar() {
		String text = mInput.getText().toString();
		if (text.length() > 0) {
			text = text.substring(0, text.length() - 1);
			mInput.setText(text);
		}
		if (mInput.getText().toString().length() < 7) {
			show_text.setText(R.string.input_notify);
		}
	}

	private void clearChar() {
		mInput.setText(null);
		show_text.setText(R.string.input_notify);
	}

	private void addChar(String c) {
		if (mInput.getText().toString().length() == 11) {
			return;
		}
		c = c.toLowerCase(Locale.CHINA);
		mInput.setText(mInput.getText() + String.valueOf(c.charAt(0)));
		if (mInput.getText().toString().length() == 7) {
			query(mInput.getText().toString());
		}
	}

	/**
	 * 查询按钮触发事件
	 * 
	 * @param view
	 */
	public void query(String phoneNumber) {

		// 截取前面7个数字
		phoneNumber = phoneNumber.substring(0, 7);

		// 查询数据库
		PhoneArea phoneArea;
		if ((phoneArea = helper.findPhoneArea(new String[] { phoneNumber
				.toString() })) != null) {
			show_text.setText(phoneArea.getArea());
		} else {
			show_text.setText(R.string.none_area);
		}
	}
}
