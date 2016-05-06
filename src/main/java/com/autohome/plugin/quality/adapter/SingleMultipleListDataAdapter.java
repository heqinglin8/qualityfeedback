package com.autohome.plugin.quality.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.autohome.mainlib.common.bean.ChooseEntity;
import com.autohome.plugin.quality.view.AHSingleORMultipleView;

import java.util.List;

/**
 * 默认单选多选Adapter(抽屉list)
 * 
 * @date:2014-3-26
 */
public class SingleMultipleListDataAdapter extends BaseAdapter {

	private Context mContext;
	private boolean mIsLeft;
//	public boolean ismIsLeft() {
//        return mIsLeft;
//    }

    public void setmIsLeft(boolean mIsLeft) {
        this.mIsLeft = mIsLeft;
    }

    private ListView mListView;

	List<ChooseEntity> list;

	public SingleMultipleListDataAdapter(Context context, boolean isLeft) {
		super();
		mContext = context;
		mIsLeft = isLeft;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout v;

		if (convertView == null) {
			ViewHolder holder = new ViewHolder();
			v = new AHSingleORMultipleView(mContext, mIsLeft);
			holder.singleMultipleView = v;
			convertView = v;
		} else {
			v = (AHSingleORMultipleView) convertView;
			AHSingleORMultipleView vv=(AHSingleORMultipleView) v;
			vv.setIsLeft(mIsLeft);
		}

		((AHSingleORMultipleView) v).setText(list.get(position).getName());
//		((AHSingleORMultipleView) v).setTextColor(ResUtil.getColor(mContext,
//				ResUtil.TEXT_COLOR_06));
		((AHSingleORMultipleView) v).setChangeModeForBg(mContext);
		getListView().setItemChecked(position, list.get(position).isChecked());
		return v;
	}

	public void setList(List<ChooseEntity> list) {
		this.list = list;

	}

	public ListView getListView() {
		return mListView;
	}

	public void setListView(ListView listView) {
		mListView = listView;
	}

	public class ViewHolder {
		public RelativeLayout singleMultipleView;
	}

}
