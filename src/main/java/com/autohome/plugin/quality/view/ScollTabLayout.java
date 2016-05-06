package com.autohome.plugin.quality.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autohome.plugin.quality.R;

/**
 * Created by heqinglin8 on 16/5/2.
 */
public class ScollTabLayout extends LinearLayout implements View.OnClickListener {
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private View lastCheckView;
    private int bgColor = 0xFFFFFF;  //选项卡背景
    private int normallColor = R.color.bgcolor12;  //常规项字体颜色
    private int selectColor = R.color.bgcolorred;  //选项卡选中字体颜色

    /**
     * {@inheritDoc}
     */
    public ScollTabLayout(Context context) {
        super(context);
        setOrientation(VERTICAL);
        init();
    }
    int i = 0;
    @Override
    public void addView(View child) {
        if(this.getChildCount()<=0){
            i = 0;
        }
        child.setOnClickListener(this);
        child.setTag(i++);
        child.setBackgroundColor(bgColor);
        super.addView(child);
    }

    /**
     * {@inheritDoc}
     */
    public ScollTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        // retrieve selected radio button as requested by the user in the
        // XML layout file
//        TypedArray attributes = context.obtainStyledAttributes(
//                attrs, com.android.internal.R.styleable.RadioGroup, com.android.internal.R.attr.radioButtonStyle, 0);

//        int value = attributes.getResourceId(R.styleable.RadioGroup_checkedButton, View.NO_ID);
//        if (value != View.NO_ID) {
//            mCheckedId = value;
//        }
          //TODO暂且默认为横向
//        final int index = attributes.getInt(com.android.internal.R.styleable.RadioGroup_orientation, LinearLayout.VERTICAL);
        final int index = HORIZONTAL;
        setOrientation(index);

//        attributes.recycle();
        init();
    }

    private void init() {

    }

    @Override
    public void onClick(View view) {
        initSelectedColor(view,selectColor);
        initSelectedColor(lastCheckView,normallColor);
        lastCheckView = view;
        mOnCheckedChangeListener.onCheckedChanged(this, (Integer) view.getTag());
    }


    public void initSelectedColor(View view,int ColorId){
        if(view instanceof ViewGroup){
            ViewGroup vg = (ViewGroup) view;
            for(int i = 0;i<vg.getChildCount();i++){
                if(vg.getChildAt(i) instanceof TextView || vg.getChildAt(i) instanceof Button){
                    TextView tv = (TextView) vg.getChildAt(i);
                    tv.setTextColor(getResources().getColor(ColorId));
                }
            }
        }else if(view instanceof TextView || view instanceof Button){
            TextView tv = (TextView) view;
            tv.setTextColor(ColorId);
        }
    }
    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * radio button changed in this group.</p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>Called when the checked radio button has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *
         * @param group the group in which the checked radio button has changed
         * @param checkedId the unique identifier of the newly checked radio button
         */
        public void onCheckedChanged(ScollTabLayout group, int checkedId);
    }

    /**
     * <p>Register a callback to be invoked when the checked radio button
     * changes in this group.</p>
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    public void setCurrentItem(int index){
        View view = getChildAt(index);
        onClick(view);
    }

    public int getNormallColor() {
        return normallColor;
    }

    public void setNormallColor(int normallColor) {
        this.normallColor = normallColor;
    }

    public int getSelectColor() {
        return selectColor;
    }

    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }


}
