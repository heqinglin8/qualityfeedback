package com.autohome.plugin.quality.constant;

import com.autohome.mainlib.business.analysis.UmsParams;


public class QualityFeedbackUmsParam extends UmsParams{
	public QualityFeedbackUmsParam(){
		super();
		put("pluginname", Constant.packageName, 9);
		put("pluginversion", Constant.versionName, 10);
	}

}
