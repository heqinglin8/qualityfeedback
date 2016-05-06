package com.autohome.plugin.quality;

import android.text.TextUtils;

import com.autohome.framework.core.PluginContext;
import com.autohome.mainlib.common.constant.AHClientConfig;
import com.autohome.mainlib.common.constant.AHConstant;
import com.autohome.mainlib.common.location.LocationHelper;
import com.autohome.mainlib.common.net.NetConstant;
import com.autohome.mainlib.common.stroage.InitDataHelper;
import com.autohome.mainlib.common.util.LogUtil;
import com.autohome.mainlib.core.AHBaseApplication;
import com.autohome.mainlib.utils.DiskUtil;
import com.autohome.net.AHNetConfigs;
import com.autohome.ums.common.CommonUtil;
import com.autohome.webview.config.WebViewConfig;
import com.cubic.autohome.logsystem.AHLogSystem;
import com.cubic.autohome.logsystem.crash.CrashHandler;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.net.URLEncoder;

public class QualityApplication extends AHBaseApplication {


	@Override
	public void onCreate() {
		super.onCreate();
// 设置日志系统

		// 设置是否开启调试日志
		AHLogSystem.getInstance().enableDebugLog(AHClientConfig.getInstance().isDebug());

		// 日志系统初始化
		String channel = QualityApplication.getInstance().getUMSChannelValue();
		String imei = CommonUtil.getDeviceID(this);
		String provinceId = LocationHelper.getInstance().getCurrentProvinceId();
		provinceId = !TextUtils.isEmpty(provinceId) ? provinceId : "0";
		String cityId = LocationHelper.getInstance().getCurrentCityId();
		cityId = !TextUtils.isEmpty(cityId) ? cityId : "0";
		AHLogSystem.getInstance().init(getApplicationContext(), channel, URLEncoder.encode(NetConstant.USER_AGENT),
				imei, provinceId, cityId, "0", Integer.parseInt(AHConstant.PM), null, "2");

		// 向友盟上报崩溃日志
		AHLogSystem.getInstance().setCrashCallback(new CrashHandler.CrashCallback() {

			@Override
			public void onCrash(Throwable ex) {
				LogUtil.e("ahcrash", "#crash 2 umeng#", ex);
				MobclickAgent.reportError(QualityApplication.getContext(), ex);
			}
		});
	}


}
