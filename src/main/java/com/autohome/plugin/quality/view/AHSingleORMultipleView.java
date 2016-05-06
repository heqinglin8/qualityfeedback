package com.autohome.plugin.quality.view;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autohome.commonlib.tools.ScreenUtils;
import com.autohome.mainlib.common.util.ResUtil;
import com.autohome.plugin.quality.R;

/**
 * 默认单选多选Adapter(Item)
 * 
 * @date:2014-3-26
 */
public class AHSingleORMultipleView extends RelativeLayout implements Checkable {

	private boolean mChecked = false;

	private Context mContext;
	private RadioButton mRadioButton;
	private TextView mTextView;
	private Boolean misLeft = true;

	public AHSingleORMultipleView(Context context, boolean isLeft) {
		super(context);
		misLeft = isLeft;
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mRadioButton = new RadioButton(context);

		mTextView = new TextView(context);
		mTextView.setGravity(Gravity.CENTER_VERTICAL);
		mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

		mRadioButton.setFocusable(false);
		mRadioButton.setClickable(false);
		LayoutParams paramsBtn = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		LayoutParams paramsTxt = new LayoutParams(
				LayoutParams.WRAP_CONTENT, ScreenUtils.dpToPxInt(mContext, 50));
		if (misLeft) {
			mRadioButton.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.checkbox_selector));
//			mRadioButton.setButtonDrawable(R.drawable.checkbox_selector);
			paramsBtn.leftMargin = ScreenUtils.dpToPxInt(mContext, 15);
			paramsBtn.addRule(RelativeLayout.CENTER_VERTICAL);
			addView(mRadioButton, paramsBtn);
			paramsTxt.leftMargin = ScreenUtils.dpToPxInt(context, 50);
			addView(mTextView, paramsTxt);
		} else {
			mRadioButton.setButtonDrawable(getResources().getDrawable(R.drawable.forms_icon_select_selector));
			mRadioButton.setId(1023);
			paramsBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			paramsBtn.addRule(RelativeLayout.CENTER_VERTICAL);
			addView(mRadioButton, paramsBtn);
			paramsTxt.leftMargin = ScreenUtils.dpToPxInt(mContext, 15);
			paramsTxt.rightMargin = ScreenUtils.dpToPxInt(mContext, 5);
			paramsTxt.addRule(RelativeLayout.CENTER_VERTICAL);
			paramsTxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			paramsTxt.addRule(RelativeLayout.LEFT_OF, 1023);
			addView(mTextView, paramsTxt);
		}
		setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
				AbsListView.LayoutParams.WRAP_CONTENT));
		setPadding(0, 0, ScreenUtils.dpToPxInt(context, 15), 0);

		setGravity(Gravity.CENTER_VERTICAL);
		setChangeModeForBg(context);
	}

	@SuppressWarnings("deprecation")
	public void setChangeModeForBg(Context context) {
		if (misLeft) {
//			mRadioButton.setButtonDrawable(R.drawable.checkbox_selector);//(ResUtil.getDrawable(mContext, ResUtil.BG_CHECKBOX_SELC));
			mRadioButton.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.checkbox_selector));
//			mRadioButton.setButtonDrawable(ResUtil.getDrawable(mContext, ResUtil.BG_CHECKBOX_SELC));

		}
		if (mRadioButton.isChecked()) {
			mTextView.setTextColor(ResUtil.getColor(context, ResUtil.TEXT_COLOR_02));
		} else {
			mTextView.setTextColor(ResUtil.getColorStateList(context, ResUtil.TEXT_COLOR_DRAWER));
		}
		setBackgroundColor(ResUtil.getColor(context, ResUtil.BG_COLOR_01));
		setBackgroundDrawable(ResUtil.getDrawable(context, ResUtil.LIST_ITEM_SELECTOR));
	}

	public void setIsLeft(boolean isLeft) {
		if (isLeft != misLeft) {
			misLeft = isLeft;
			removeAllViews();
			LayoutParams paramsBtn = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			LayoutParams paramsTxt = new LayoutParams(
					LayoutParams.WRAP_CONTENT, ScreenUtils.dpToPxInt(mContext, 44));
			if (isLeft) {
				mRadioButton.setButtonDrawable(mContext.getResources().getDrawable(R.drawable.checkbox_selector));
//				mRadioButton.setButtonDrawable(ResUtil.getDrawable(mContext, ResUtil.BG_CHECKBOX_SELC));
//				mRadioButton.setButtonDrawable(R.drawable.checkbox_selector);
				paramsBtn.leftMargin = ScreenUtils.dpToPxInt(mContext, 15);
				paramsBtn.addRule(RelativeLayout.CENTER_VERTICAL);
				addView(mRadioButton, paramsBtn);
				paramsTxt.leftMargin = (int) ScreenUtils.dpToPxInt(mContext, 50.0f);
				addView(mTextView, paramsTxt);
			} else {
				mRadioButton.setButtonDrawable(getResources().getDrawable(R.drawable.forms_icon_select_selector));
				mRadioButton.setId(1023);
				paramsBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				paramsBtn.addRule(RelativeLayout.CENTER_VERTICAL);
				addView(mRadioButton, paramsBtn);
				paramsTxt.leftMargin = ScreenUtils.dpToPxInt(mContext, 15);
				paramsTxt.rightMargin = ScreenUtils.dpToPxInt(mContext, 5);
				paramsTxt.addRule(RelativeLayout.CENTER_VERTICAL);
				paramsTxt.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				paramsTxt.addRule(RelativeLayout.LEFT_OF, 1023);
				addView(mTextView, paramsTxt);
			}
		}
	}

	public void setText(String txt) {
		mTextView.setText(txt);
	}

	public void setTextColor(int color) {
		mTextView.setTextColor(color);
	}

	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mChecked = checked;
			mRadioButton.setChecked(checked);
			if (checked) {
				mTextView.setTextColor(ResUtil.getColor(mContext, ResUtil.TEXT_COLOR_02));
			} else {
				mTextView.setTextColor(ResUtil.getColorStateList(mContext, ResUtil.TEXT_COLOR_DRAWER));
			}
		}
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void toggle() {
		setChecked(!mChecked);
	}

}
