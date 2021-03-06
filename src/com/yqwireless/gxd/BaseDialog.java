package com.yqwireless.gxd;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class BaseDialog extends Dialog {

	private int MIN_WIDTH;
	private int MIN_HEIGHT;
	private Context context;
	private LayoutInflater inflater;
	private FrameLayout base;

	private static final String tag = BaseDialog.class.toString();

	public BaseDialog(Context context) {
		super(context, R.style.FullHeightDialog);
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initMin();
		initBase();
	}

	private void initMin() {
		DisplayMetrics dm = getMetrics();
		int height = dm.heightPixels;
		int width = dm.widthPixels;
		MIN_WIDTH = (int) (0.9f * (float) width);
		MIN_HEIGHT = (int) (0.1f * (float) height);
		Log.i(tag, "MIN_WIDTH:" + MIN_WIDTH + ", MIN_HEIGHT:" + MIN_HEIGHT);
	}

	private void initBase() {
		base = new FrameLayout(context);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		base.setLayoutParams(lp);
		base.setMinimumHeight(MIN_HEIGHT);
		base.setMinimumWidth(MIN_WIDTH);
		Drawable d = context.getResources().getDrawable(android.R.color.background_dark);
		d.setAlpha(180);
		base.setBackgroundDrawable(d);
		//base.setBackgroundColor(context.getResources().getColor(android.R.color.background_dark));
		base.setPadding(10, 10, 10, 15);
	}

	@Override
	public void show() {
		Window w = getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.y = -1;
		super.show();
	}

	private DisplayMetrics getMetrics() {
		DisplayMetrics dm = new DisplayMetrics();
		if (context instanceof Activity) {
			((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		} else {
			dm.widthPixels = 480;
			dm.heightPixels = 800;
		}
		return dm;
	}

	@Override
	public void setContentView(int layoutResID) {
		setContentView(inflater.inflate(layoutResID, null));
	}

	@Override
	public void setContentView(View view) {
		if (view instanceof LinearLayout) {
			((LinearLayout) view).setGravity(Gravity.CENTER);
		} else if (view instanceof RelativeLayout) {
			((RelativeLayout) view).setGravity(Gravity.CENTER);
		}
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		view.setLayoutParams(lp);
		view.setMinimumHeight(MIN_HEIGHT);
		view.setMinimumWidth(MIN_WIDTH);
		base.addView(view);
		super.setContentView(base);
	}

}
