package com.autohome.plugin.quality.activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.autohome.commonlib.view.drawer.AHMainDrawer;
import com.autohome.mainlib.common.bean.ChooseEntity;
import com.autohome.mainlib.common.util.LogUtil;
import com.autohome.mainlib.common.util.ResUtil;
import com.autohome.mainlib.common.util.WebLoadProgressHelper;
import com.autohome.mainlib.common.view.AHErrorLayout;
import com.autohome.mainlib.common.view.AHLoadProgressWebView;
import com.autohome.mainlib.core.BaseActivity;
import com.autohome.mainlib.utils.NetUtil;
import com.autohome.net.core.AHBaseServant;
import com.autohome.net.core.EDataFrom;
import com.autohome.net.core.ResponseListener;
import com.autohome.net.error.AHError;
import com.autohome.plugin.quality.R;
import com.autohome.plugin.quality.bean.FaultCategoryEntity;
import com.autohome.plugin.quality.servant.FaultCategoryServant;
import com.autohome.plugin.quality.view.GuZhangDrawer;
import com.autohome.plugin.quality.view.ObservableHorizontalScrollView;
import com.autohome.plugin.quality.utils.PluginUtil;
import com.autohome.webview.util.ToastUtils;
import com.autohome.webview.view.AHWebChromeClient;
import com.autohome.webview.view.AHWebViewClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 故障详情页
 */
public class QualityDetailActivity extends BaseActivity implements View.OnClickListener,AHMainDrawer.IMainDrawerListener, GuZhangDrawer.onItemSelectListener, CompoundButton.OnCheckedChangeListener {
    //数据变量
    private boolean isLoadError = false;
    public static final String BLANK_PAGE = "about:blank";
    private List<ChooseEntity> categoryList;
    private boolean isInit = false;
    private final static String TAG = "QualityDetailActivity";
    //数据变量
    private static final String SERIES_ID_KEY = "seriesid";  //车系id
    private static final String FROM_KEY = "from";
    private static final String TYPE_KEY = "type";  //故障类型
    private String from = "-1";  //代表页面来源,-1代表内部跳转
    private String type = "";  //代表故障类型
    private String seriesId = "-1";  //车系id
    private String mUrl = "";  //代表浏览器渲染的url，先默认这个
    private final static String mTitle  = "故障详情";
    private final static int CATEGORY_MIN_SIZE = 4;
    /**
     * 控件变量
     */
    private View barContainer;
    private ObservableHorizontalScrollView scrollView;
    private RadioGroup radioGroup;
    private AHLoadProgressWebView mBrowserWebView;
    private TextView mBackTV;
    private TextView mNextTV;
    private TextView mTitleTV;
    private AHErrorLayout mErrorLayout;
    private ImageView guzhangAlbum;
    private GuZhangDrawer categoryDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_quality_detail);
        handleInitData();
        initView();
        initWebView();
        setListener();
        loadData();
    }

    @Override
    public void onSkinChangedActivity() {
//        mainView.setBackgroundColor(ResUtil.getColor(this,ResUtil.BG_COLOR_01));
        barContainer.setBackgroundColor(ResUtil.getColor(this,
                ResUtil.BG_COLOR_35));
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            ((RadioButton) radioGroup.getChildAt(0)).setTextColor(ResUtil
                    .getColorStateList(this, ResUtil.BAR_COLOR));

        }
    }

    /**
     * 初始化数据
     */
    private void handleInitData() {
        Intent mIntent = getIntent();
        Uri mUri =mIntent.getData();
        if (null == mUri) {
            //来自质量反馈插件app内部跳转
            type = mIntent.getStringExtra(TYPE_KEY);
            if(!TextUtils.isEmpty(mIntent.getStringExtra(SERIES_ID_KEY)))
                seriesId = mIntent.getStringExtra(SERIES_ID_KEY);
        } else {
            // <!-- autohome://faultdetail/?from=XX&&type=XX&&seriesId=XX -->
            from = mUri.getQueryParameter(FROM_KEY);
            type = mUri.getQueryParameter(TYPE_KEY);
            seriesId = mUri.getQueryParameter(SERIES_ID_KEY);
        }
    }

    public void getDataFromServer(boolean isNeedLoading){
        if(isNeedLoading){
            mErrorLayout
                    .setErrorType(AHErrorLayout.NETWORK_LOADING);
        }
        if (!NetUtil.CheckNetState()) {
            ToastUtils.showMessage(this, getString(R.string.network_error_info),
                    false);
        }
        getFaultCategoryData(type, seriesId, AHBaseServant.ReadCachePolicy.ReadCacheAndNet, true);
    }

    private void getFaultCategoryData(String type, String seriesId, AHBaseServant.ReadCachePolicy readCacheAndNet, boolean isWriteCache) {
        FaultCategoryServant servant = new FaultCategoryServant(readCacheAndNet,isWriteCache);
        servant.getRequestParams(type,seriesId,new ResponseListener<List<FaultCategoryEntity>>(){

            @Override
            public void onReceiveData(List<FaultCategoryEntity> data, EDataFrom eDataFrom, Object o2) {
                List<FaultCategoryEntity> faultCategoryEntitys = data;
                            for(int i = 0; i< faultCategoryEntitys.size(); i++){
                         FaultCategoryEntity entity = faultCategoryEntitys.get(i);
                        ChooseEntity cn = new ChooseEntity(entity.getCategoryid(),entity.getCategoryname());
                        cn.setParam1(entity.getCategorypercent());
                        cn.setParam2(entity.getUrl());
                        categoryList.add(cn);
                    }
                //填充导航栏
                fillUI();
            }

            @Override
            public void onFailure(AHError error, Object tag) {
                LogUtil.e("error:",error.errorMsg+""+error.toString());
                mErrorLayout
                        .setErrorType(AHErrorLayout.NETWORK_ERROR);
            }
        });
    }

    private void initView() {
        mBackTV = (TextView) findViewById(R.id.ahlib_common_back_TextView);
        mNextTV = (TextView) findViewById(R.id.ahlib_right_function_TextView);
        mTitleTV = (TextView) findViewById(R.id.ahlib_navigation_title_TextView);
//        mTitleTV.setText(mTitle);
        mNextTV.setVisibility(View.GONE);

        barContainer = findViewById(R.id.guzhang_bar_container);
        guzhangAlbum = (ImageView) findViewById(R.id.guzhang_album);
        categoryList = new ArrayList<>();
        categoryDrawer = new GuZhangDrawer(this,categoryList);
        barContainer.setBackgroundColor(ResUtil.getColor(this,
                ResUtil.BG_COLOR_05));
        scrollView = (ObservableHorizontalScrollView)findViewById(R.id.guzhang_bar_scrollview);
        radioGroup = (RadioGroup) findViewById(R.id.guzhang_radiogroup);

        mBrowserWebView = (AHLoadProgressWebView) findViewById(R.id.common_brower_WebView);
        mErrorLayout = (AHErrorLayout) findViewById(R.id.topic_page_loading_layout);
        mErrorLayout.setNoLoadingAnim(true);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        mBackTV.setOnClickListener(this);
        mNextTV.setOnClickListener(this);
        guzhangAlbum.setOnClickListener(this);
        categoryDrawer.setOnDrawerListener(this);
        categoryDrawer.setOnItemClickListener(this);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mErrorLayout.setVisibility(View.GONE);
                if(!(URLUtil.isHttpUrl(mUrl) || URLUtil.isHttpsUrl(mUrl))){
                    loadData();
                }else{
                    mBrowserWebView.loadUrl(mUrl);
                }
            }
        });
    }

    /**
     * 初始化浏览器设置
     */
    private void initWebView() {
        WebSettings mWebSettings = mBrowserWebView.getSettings();

        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setDatabaseEnabled(true);
        //设置webview UserAgent协议
        PluginUtil.setUserAgent(mWebSettings);
//        LogUtil.i("userAgent",userAgent);
        //设置加载进度监听
        mBrowserWebView.setWebChromeClient(new CommonWebViewChromeClient());
        mBrowserWebView.setWebViewClient(new CommonWebViewClient(mBrowserWebView));
//        mBrowserWebView.loadUrl(mUrl);
    }

    /**
     * 初始化选项卡
     */
    private void initRadioButtons() {
        if (categoryList != null && categoryList.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(this);
            RadioButton rbtn;
            for (int i = 0; i < categoryList.size(); i++) {
                rbtn =  (RadioButton)inflater.inflate(
                        R.layout.club_guzhang_bar_item, null);
                rbtn.setTextColor(ResUtil.getColorStateList(this,
                        ResUtil.BAR_COLOR));
                rbtn.setTag(i);
                rbtn.setText(Html.fromHtml(categoryList.get(i).getName()+"<br><small>"+categoryList.get(i).getParam1()+"</small>"));
                rbtn.setOnCheckedChangeListener(this);
                radioGroup.addView(rbtn);
            }
//            isInit = true;
            ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
//            isInit = false;
        }
    }

    /**
     * 加载数据
     * 当openThreadInActivity = false的时候，
     * 这个方法不会再父类被调用，需要子类主动调用
     */
    @Override
    public void loadData(){
        getDataFromServer(true);
    }
    /**
     * 填充数据
     * 当openThreadInActivity = false的时候，
     * 这个方法不会再父类被调用，需要子类主动调用
     */
    @Override
    public void fillUI() {
        //渲染故障分类四口入口
        guzhangAlbum.setVisibility(categoryList.size()<CATEGORY_MIN_SIZE ? View.GONE : View.VISIBLE);
        //渲染top导航栏
        initRadioButtons();
        //渲染侧边栏
        categoryDrawer.setList(categoryList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ahlib_common_back_TextView:
                 finish();
                break;
            case R.id.guzhang_album:
                categoryDrawer.openDrawer();
                break;
            case R.id.ahlib_right_function_TextView:
                PluginUtil.launchQualityFeedback(QualityDetailActivity.this,"1","http://app.k.autohome.com.cn/2137/appquality?type=1");
                break;
        }
    }

    @Override
    public void endPvInDrawer() {

    }

    @Override
    public void beginPvInDrawer() {

    }

    @Override
    public void choose(ChooseEntity entity, int position) {
        ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
    }

    /**
     * 监听title
     */
    private final class CommonWebViewChromeClient extends AHWebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mBrowserWebView.getWebLoadProgressHelper().onProgressChanged(newProgress);
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            // TODO Auto-generated method stub
            super.onReceivedTitle(view, title);

            if (TextUtils.equals("找不到网页", title)
                    || TextUtils.equals("Webpage not available", title)) {
                isLoadError = true;
            }

            if (isLoadError) {
                title = "";
            }
            // 回传标题
            if (null != mTitleTV) {
                mTitleTV.setText(title);
            }
        }

    }

    /**
     * 选项卡切换
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        RadioButton rbtn = (RadioButton) buttonView;
        if (isChecked && !isInit) {
            int position = (int) rbtn.getTag();
            ChooseEntity ce = categoryList.get(position);
            //Param2 設置為url
            mUrl = ce.getParam2();
            mBrowserWebView.loadUrl(mUrl);
            scrollView.requestChildFocus(rbtn, rbtn);
//            ce.setChecked(true);
            categoryDrawer.setListIndexSelector(ce);
            LogUtil.i(TAG,mUrl);
        }
    }

    private final class CommonWebViewClient extends AHWebViewClient {

        public CommonWebViewClient(WebView webView) {
            super(webView);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            LogUtil.d(TAG, "开始加载....");

            isLoadError = false;

            mErrorLayout.setVisibility(View.GONE);

            mBrowserWebView
                    .getWebLoadProgressHelper()
                    .onPageStarted(
                            url,
                            false,
                            new WebLoadProgressHelper.Callback() {

                                @Override
                                public void onCallback(boolean initiativeFinish) {
                                    if (isLoadError || initiativeFinish) {
                                        mErrorLayout
                                                .setVisibility(View.VISIBLE);
                                        mErrorLayout
                                                .setErrorType(AHErrorLayout.NETWORK_ERROR);
                                        isLoadError = false;
                                    }
                                }
                            });
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            LogUtil.d(TAG, "页面加载出错");
            if (!NetUtil.CheckNetState()) {
                ToastUtils
                        .showMessage(QualityDetailActivity.this, QualityDetailActivity.this
                                .getString(com.autohome.mainlib.R.string.network_error_info), false);
            }

            isLoadError = true;

            mBrowserWebView.getWebLoadProgressHelper().onReceivedError(
                    failingUrl);
            mBrowserWebView.stopLoading();
            /*
			 * if (isLoadError) {
			 * mErrorLayout.setErrorType(AHErrorLayout.NETWORK_ERROR); }
			 *
			 * mErrorLayout.setVisibility(isLoadError ? View.VISIBLE :
			 * View.GONE); mBrowserWebView.setVisibility(isLoadError ? View.GONE
			 * : View.VISIBLE);
			 */
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            mBrowserWebView
                    .getWebLoadProgressHelper()
                    .onPageFinished(
                            new WebLoadProgressHelper.Callback() {

                                @Override
                                public void onCallback(boolean initiativeFinish) {
                                    if (isLoadError || initiativeFinish) {
                                        mErrorLayout
                                                .setVisibility(View.VISIBLE);
                                        mErrorLayout
                                                .setErrorType(AHErrorLayout.NETWORK_ERROR);
                                        isLoadError = false;
                                    }
                                }
                            });

            if (BLANK_PAGE.equals(url)) {
                isLoadError = true;
                mErrorLayout
                        .setErrorType(AHErrorLayout.NETWORK_ERROR);
            }
        }

    }


}
