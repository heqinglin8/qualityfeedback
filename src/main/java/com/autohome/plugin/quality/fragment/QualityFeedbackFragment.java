package com.autohome.plugin.quality.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.autohome.mainlib.business.analysis.AHUMSContants;
import com.autohome.mainlib.business.analysis.UmsAnalytics;
import com.autohome.mainlib.business.analysis.UmsParams;
import com.autohome.mainlib.common.util.IntentHelper;
import com.autohome.mainlib.common.util.LogUtil;
import com.autohome.mainlib.common.view.AHErrorLayout;
import com.autohome.mainlib.common.view.AHLoadProgressWebView;
import com.autohome.mainlib.common.view.BaseFragment;
import com.autohome.mainlib.utils.NetUtil;
import com.autohome.plugin.quality.R;
import com.autohome.plugin.quality.activity.QualityDetailActivity;
import com.autohome.plugin.quality.utils.PluginUtil;
import com.autohome.webview.util.ToastUtils;
import com.autohome.webview.view.AHWebChromeClient;
import com.autohome.webview.view.AHWebViewClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 质量评价页面
 */
public class QualityFeedbackFragment extends BaseFragment implements View.OnClickListener {
    //数据变量
    private boolean isLoadError = false;
    public static final String BLANK_PAGE = "about:blank";
    private View mRootView;
    private final static String TAG  = "QualityFeedbackFragment";
    private final static String mTitle  = "质量评价";
    //数据变量
    private static final String M_URL_KEY = "url";
    private static final String FROM_KEY = "from";
    private static final String SERIES_ID_KEY = "seriesid";  //车系id
    private static final String TYPE_KEY = "type";  //故障类型
    private String from = "-1";  //代表页面来源，-1代表内部跳转
    private String mUrl = "";  //代表浏览器渲染的url
    private String title;
    protected UmsParams mPvParams = null;
    //布局变量
    private AHLoadProgressWebView mBrowserWebView;
    private Activity mActivity;
    private TextView mBackTV;
    private TextView mNextTV;
    private TextView mTitleTV;

    /**
     * 初始化webView
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

        //设置加载进度监听
        mBrowserWebView.setWebChromeClient(new CommonWebViewChromeClient());
        mBrowserWebView.setWebViewClient(new CommonWebViewClient(mBrowserWebView));
        LogUtil.i(TAG, "待加载的URL = " + mUrl);
        if (!(URLUtil.isHttpUrl(mUrl) || URLUtil.isHttpsUrl(mUrl))) {
            mActivity.finish();
            return;
        }
        bindJsCallback();
        mBrowserWebView.loadUrl(mUrl);

        //设置javascript监听
//        mBrowserWebView.addJavascriptInterface(new ClsAccessor(), "accessor");
    }

    /**
     * 绑定对故障详情页的跳转事件监听，这个回调是在子线程，如果想要操作ui需要post回到ui线程去操作
     */
    private void bindJsCallback() {

        mBrowserWebView.bindMethod("jumpToNewUrlWithType",
                new com.autohome.webview.view.AHWebViewClient.Method() {

                    @Override
                    public JSONObject execute(JSONObject args) {
                        try {
                            String type  = args.optString("type");
                            String seriesid  = args.optString("seriesid");
                            Intent intent = new Intent(mActivity,QualityDetailActivity.class);
                            intent.putExtra(SERIES_ID_KEY,seriesid);
                            intent.putExtra(TYPE_KEY,type);
                            IntentHelper.startActivity(mActivity,intent);
                            return new JSONObject("{\"result\":1}");
                        } catch (JSONException e) {
                            LogUtil.e(e.toString());
                            return null;
                        }
                    }

                });
    }


    private void handleInitData() {
        mActivity = getActivity();
        if(mActivity!=null){
            Intent mIntent = mActivity.getIntent();
            Uri mUri = mIntent.getData();
            if (null == mUri) {
                //来自质量反馈插件app内部跳转
                if(!TextUtils.isEmpty(mActivity.getIntent().getStringExtra(M_URL_KEY)))
                                                                   mUrl = mIntent.getStringExtra(M_URL_KEY);
            } else {
                from = mUri.getQueryParameter(FROM_KEY);
                mUrl = mUri.getQueryParameter(M_URL_KEY);
            }
        }
    }

//    public static QualityFeedbackFragment newInstance(String param1, String param2) {
//        QualityFeedbackFragment fragment = new QualityFeedbackFragment();
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void createPvParamsForQualityFeedback(int userId) {
        userId = UmsAnalytics.getUserId();
        UmsParams params = new UmsParams();
        params.put(AHUMSContants.USER_ID, String.valueOf(userId), 1);
        params.put(AHUMSContants.SERIES_ID, String.valueOf(userId), 1);
        params.put("typeid", String.valueOf(userId), 1);
        mPvParams = params;
        setPvLabel(AHUMSContants.CLUB_HOME_PAGE_IN_SERIES_AREA_TOPIC);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quality_feedback, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleInitData();
        mRootView = view;
        initView();
        initWebView();
        setListener();
    }

    /**
     * 设置监听
     */
    private void setListener() {
        mBackTV.setOnClickListener(this);

        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mErrorLayout.setVisibility(View.GONE);
                mBrowserWebView.loadUrl(mUrl);
            }
        });


    }

    /**
     * 初始化并渲染布局
     */
    private void initView() {
        mBackTV = (TextView) mRootView.findViewById(R.id.ahlib_common_back_TextView);
        mNextTV = (TextView) mRootView.findViewById(R.id.ahlib_right_function_TextView);
        mTitleTV = (TextView) mRootView.findViewById(R.id.ahlib_navigation_title_TextView);
//        mTitleTV.setText(mTitle);
        //TODO 测试跳转质量详情页
        mNextTV.setVisibility(View.GONE);

        mBrowserWebView = (AHLoadProgressWebView) mRootView
                .findViewById(R.id.common_brower_WebView);
        mErrorLayout = (AHErrorLayout) mRootView
                .findViewById(R.id.topic_page_loading_layout);
        mErrorLayout.setNoLoadingAnim(true);

    }

    /**
     * 返回错误控件给父类
     * @return
     */
    @Override
    protected AHErrorLayout getErrorLayout() {
        return mErrorLayout;
    }

    /**
     * 修改皮肤的回调
     */
    @Override
    public void onSkinChangedFragment() {

    }

    /**
     * 设置web浏览器链接
     * @param url
     */
    public void setLoadUrl(String url){
       this.mUrl = url;
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.ahlib_common_back_TextView:
                mActivity.finish();
                break;
        }
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
                            new com.autohome.mainlib.common.util.WebLoadProgressHelper.Callback() {

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
                        .showMessage(mActivity, mActivity
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
                            new com.autohome.mainlib.common.util.WebLoadProgressHelper.Callback() {

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
            }
        }

    }

}
