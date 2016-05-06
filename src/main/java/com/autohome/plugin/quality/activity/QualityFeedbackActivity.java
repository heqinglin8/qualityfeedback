package com.autohome.plugin.quality.activity;
import android.os.Bundle;
import com.autohome.mainlib.common.view.BaseFragment;
import com.autohome.mainlib.core.BaseFragmentActivity;
import com.autohome.plugin.quality.R;
import com.autohome.plugin.quality.fragment.QualityFeedbackFragment;

/**
 * 质量评价首页
 */
public class QualityFeedbackActivity extends BaseFragmentActivity {
    /**
     * 主题皮肤切换的页面
     */
    @Override
    public void onSkinChangedFragmentActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    /**
     * 布局初始化
     */
    private void initViews() {
        //初始化fragment添加
       String mUrl = "http://app.k.autohome.com.cn/2137/appquality?type=1";
        QualityFeedbackFragment mFragment = new QualityFeedbackFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("mLoadUrl",mLoadUrl);
//        mFragment.setArguments(bundle);
        mFragment.setLoadUrl(mUrl);
        replaceFragment(mFragment);
    }

    /**
     * 切换fragment 以replace的方式，将销毁之前的fragment
     * @param fragment
     */
    public void replaceFragment(BaseFragment fragment){
                getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_root_layout, fragment)
                .commit();
    }


}
