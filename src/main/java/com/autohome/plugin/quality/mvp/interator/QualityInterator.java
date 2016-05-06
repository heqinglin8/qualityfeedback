package com.autohome.plugin.quality.mvp.interator;

import com.autohome.mainlib.common.bean.ChooseEntity;
import com.autohome.net.core.AHBaseServant;

import java.util.List;

/**
 * Created by heqinglin on 2016/5/4.
 * 数据源的接口 model
 */
public interface QualityInterator {
    List<ChooseEntity> getFaultCategoryData(String type, String seriesId, AHBaseServant.ReadCachePolicy readCacheAndNet, boolean isWriteCache);
}
