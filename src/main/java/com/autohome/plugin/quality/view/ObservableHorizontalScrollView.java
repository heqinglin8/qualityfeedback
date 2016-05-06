package com.autohome.plugin.quality.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class ObservableHorizontalScrollView extends HorizontalScrollView {

	private OnHorizontalScrollChangedListener listener;

	public ObservableHorizontalScrollView(Context context) {
		super(context);
	}

	public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ObservableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (listener != null) {
			listener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

	public void setOnHorizontalScrollChangedListener(OnHorizontalScrollChangedListener listener) {
		this.listener = listener;
	}

	public static interface OnHorizontalScrollChangedListener {

		public void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y, int oldx, int oldy);

	}

}
