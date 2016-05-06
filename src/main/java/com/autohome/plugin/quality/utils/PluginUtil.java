package com.autohome.plugin.quality.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.WebSettings;

import com.autohome.commonlib.tools.DES3Utils;
import com.autohome.mainlib.common.constant.AHClientConfig;
import com.autohome.mainlib.common.location.LocationHelper;
import com.autohome.mainlib.common.util.DeviceHelper;
import com.autohome.mainlib.common.util.IntentHelper;
import com.autohome.mainlib.common.util.LogUtil;
import com.autohome.mainlib.utils.SysUtil;
import com.loopj.android.http.Base64;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by heqinglin on 2016/4/29.
 * 这是一个插件工具类，本插件常用方法写在这这里
 */
public class PluginUtil {
    private static String TAG = "PluginUtil";
    private static final String UA_VERSION = "1.0";  //自定义UA协议版本

    /**
     * 给webview设置useragent
     * @param mWebSettings
     * @return
     */
    public  static String setUserAgent(WebSettings mWebSettings){
        StringBuilder userAgent = new StringBuilder(
                mWebSettings.getUserAgentString());
        userAgent.append(" ");
        userAgent.append("auto_android");
        userAgent.append("/");
        userAgent.append(SysUtil.getVersionCode());
        userAgent.append(" ");
        userAgent.append("nettype");
        userAgent.append("/");
        userAgent.append(DeviceHelper.getNetWorkMode().toLowerCase());
        userAgent.append(" ");
        userAgent.append("autohomeapp");
        userAgent.append("/");
//		String pluginfo = getPluginfo();
        userAgent.append(getAppinfoValue());
        mWebSettings.setUserAgentString(userAgent.toString());
        return userAgent.toString();
    }


    public static String secret(String originValue) {
        // TODO Auto-generated method stub
        LogUtil.d(TAG, "加密前  :　" + originValue);
        byte[] originValuebt = DES3Utils.encryptMode(originValue.getBytes());
        String mSecretStr = "";
        if (null != originValuebt) {
            mSecretStr = Base64.encodeToString(originValuebt,
                    Base64.NO_WRAP);
            if (!TextUtils.isEmpty(mSecretStr)) {
                mSecretStr = mSecretStr.replace("+", "-").replace("/", "_").replace("=", "");
            }
            LogUtil.d(TAG, "加密后  :　" + mSecretStr);
        }

        return mSecretStr;
    }



    private static String getAppinfoValue() {
        StringBuilder mSB = new StringBuilder(UA_VERSION);
        mSB.append("(auto_android;");
        mSB.append(AHClientConfig.getInstance().getAhClientVersion());
        mSB.append(";");
        StringBuilder mOrigSB = new StringBuilder();
        String pid = LocationHelper.getInstance().getChoseProvinceId();
        String cid = LocationHelper.getInstance().getChoseCityId();
        String gpspid = LocationHelper.getInstance().getCurrentProvinceId();
        String gpscid = LocationHelper.getInstance().getCurrentCityId();
        mOrigSB.append(TextUtils.isEmpty(pid) ? "0" : pid);
        mOrigSB.append(";");
        mOrigSB.append(TextUtils.isEmpty(cid) ? "0" : cid);
        mOrigSB.append(";");
        mOrigSB.append(TextUtils.isEmpty(gpspid) ? "0" : gpspid);
        mOrigSB.append(";");
        mOrigSB.append(TextUtils.isEmpty(gpscid) ? "0" : gpscid);
        mOrigSB.append(";");
        mOrigSB.append(DeviceHelper.getIMEI());
        mOrigSB.append(";0");
        mSB.append(secret(mOrigSB.toString())); // pid;cid;gpspid;gpscid;deviceid;0加密
        mSB.append(";");
        try {
            mSB.append(URLEncoder.encode(android.os.Build.VERSION.RELEASE,
                    "utf-8"));
            mSB.append(";");
            mSB.append(URLEncoder.encode(android.os.Build.MODEL, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String appInfo = mSB.toString();
        try {
            return URLEncoder.encode(appInfo, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 开启质量评价
     *
     * @param context
     * @param url
     *            webview加载的url
     * @param from
     *            来源
     * scheme：autohome://qualityfeedback?url=XXX&from=YYY
     *
     */
    public static void launchQualityFeedback(Context context,String from,String url) {
        Uri uri = Uri.parse("autohome://qualityfeedback/").buildUpon()
                .appendQueryParameter("from", from)
                .appendQueryParameter("url", url).build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        IntentHelper.startActivity(context, intent);
    }


    /**
     * 开启故障详情页
     *
     * @param context
     * @param seriesid
     *            webview加载的url
     * @param from
     *            来源
     *  * @param type
     *            故障类型
     * scheme：autohome://faultdetail?seriesid=XXX&from=YYY&type=ZZZ
     *
     */
    public static void launchQualityFeedback(Context context,String from,String seriesid,String type) {
        Uri uri = Uri.parse("autohome://faultdetail/").buildUpon()
                .appendQueryParameter("from", from)
                .appendQueryParameter("type", type)
                .appendQueryParameter("seriesid", seriesid).build();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        IntentHelper.startActivity(context, intent);
    }


}
