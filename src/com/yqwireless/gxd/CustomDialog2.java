package com.yqwireless.gxd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 这个类是自定义类型的对话框，不能删
 * 
 * @author qy
 *
 */
public class CustomDialog2 extends BaseDialog {

	private FrameLayout content;
	private TextView title;
	private Button yes;
	private Button no;
	private Button ret;
	private LinearLayout yesno;
	private LayoutInflater inflater;

	public CustomDialog2(Context context) {
		super(context);
		inflater = LayoutInflater.from(context);
		LinearLayout view = (LinearLayout) inflater.inflate(
				R.layout.custom_dialog, null);
		title = (TextView) view.findViewById(R.id.custom_dialog_title);
		yes = (Button) view.findViewById(R.id.custom_dialog_yes_btn);
		no = (Button) view.findViewById(R.id.custom_dialog_no_btn);
		ret = (Button) view.findViewById(R.id.custom_dialog_single_btn);
		content = (FrameLayout) view.findViewById(R.id.custom_dialog_content);
		yesno = (LinearLayout) view.findViewById(R.id.custom_dialog_yesno);
		initButtons();
		super.setContentView(view);
	}

	private void initButtons() {
		View.OnClickListener lsnr = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		};
		yes.setOnClickListener(lsnr);
		no.setOnClickListener(lsnr);
		ret.setOnClickListener(lsnr);
	}

	public void setMessage(String msg) {
		LinearLayout view = (LinearLayout) inflater.inflate(
				R.layout.custom_dialog_message, null);
		TextView message = (TextView) view.getChildAt(0);
		message.setText(msg);
		content.addView(view);
	}

	public void setTitle(String title) {
		this.title.setText(title);
		this.title.setVisibility(View.VISIBLE);
	}

	public void enableYesNoButton() {
		yesno.setVisibility(View.VISIBLE);
	}

	public void setYesButton(String text) {
		if (text != null)
			yes.setText(text);
	}

	public void setYesButton(final View.OnClickListener lsnr) {
		if (lsnr == null)
			return;
		View.OnClickListener dismissLsnr = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//dismiss();
				lsnr.onClick(v);
			}
		};
		yes.setOnClickListener(dismissLsnr);
	}

	public void setYesButton(boolean autoDismiss, View.OnClickListener lsnr) {
		if (!autoDismiss) {
			if (lsnr != null) {
				yes.setOnClickListener(lsnr);
			}
		} else {
			setYesButton(lsnr);
		}
	}

	public void setYesButton(String text, View.OnClickListener lsnr) {
		setYesButton(text);
		setYesButton(lsnr);
	}

	public void setYesButton(boolean autoDismiss, String text,
			View.OnClickListener lsnr) {
		setYesButton(text);
		setYesButton(autoDismiss, lsnr);
	}

	public void setNoButton(String text) {
		if (text != null)
			yes.setText(text);
	}

	public void setNoButton(final View.OnClickListener lsnr) {
		if (lsnr == null)
			return;
		View.OnClickListener dismissLsnr = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				lsnr.onClick(v);
			}
		};
		no.setOnClickListener(dismissLsnr);
	}

	public void setNoButton(boolean autoDismiss, View.OnClickListener lsnr) {
		if (!autoDismiss) {
			if (lsnr != null) {
				no.setOnClickListener(lsnr);
			}
		} else {
			setNoButton(lsnr);
		}
	}

	public void setNoButton(String text, View.OnClickListener lsnr) {
		setNoButton(text);
		setNoButton(lsnr);
	}

	public void setNoButton(boolean autoDismiss, String text,
			View.OnClickListener lsnr) {
		setNoButton(text);
		setNoButton(autoDismiss, lsnr);
	}

	public void enableRetButton() {
		ret.setVisibility(View.VISIBLE);
	}

	public void setRetButton(String text) {
		if (text != null)
			ret.setText(text);
	}

	public void setRetButton(final View.OnClickListener lsnr) {
		if (lsnr == null)
			return;
		View.OnClickListener dismissLsnr = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				lsnr.onClick(v);
			}
		};
		ret.setOnClickListener(dismissLsnr);
	}

	public void setRetButton(boolean autoDismiss, View.OnClickListener lsnr) {
		if (!autoDismiss) {
			if (lsnr != null) {
				ret.setOnClickListener(lsnr);
			}
		} else {
			setRetButton(lsnr);
		}
	}

	public void setRetButton(String text, View.OnClickListener lsnr) {
		setRetButton(text);
		setRetButton(lsnr);
	}

	public void setRetButton(boolean autoDismiss, String text,
			View.OnClickListener lsnr) {
		setRetButton(text);
		setRetButton(autoDismiss, lsnr);
	}

	@Override
	public void setContentView(int id) {
		View view = inflater.inflate(id, null);
		setContentView(view);
	}

	@Override
	public void setContentView(View view) {
		content.addView(view);
	}

}
