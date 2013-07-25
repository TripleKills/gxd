package com.yqwireless.gxd;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yqwireless.gxd.db.DBHelper;
import com.yqwireless.gxd.entity.PhoneArea;

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
			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			final View dialog_view = inflater.inflate(R.layout.dialog_view,
					null);
			new AlertDialog.Builder(this).setTitle(R.string.action_about)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(dialog_view).setPositiveButton("确定", null).show();
			break;
		}
		return true;
	}

	public void onBtnClick(View view) {
		switch (view.getId()) {
		case R.id.deleteButton:
			delChar();
			break;
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

		} else
			show_text.setText(R.string.none_area);

	}
}
