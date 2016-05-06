package com.autohome.plugin.quality.mvp.interator.impl;

import com.autohome.mainlib.common.bean.ChooseEntity;
import com.autohome.mainlib.common.util.LogUtil;
import com.autohome.mainlib.common.view.AHErrorLayout;
import com.autohome.net.core.AHBaseServant;
import com.autohome.net.core.EDataFrom;
import com.autohome.net.core.ResponseListener;
import com.autohome.net.error.AHError;
import com.autohome.plugin.quality.bean.FaultCategoryEntity;
import com.autohome.plugin.quality.mvp.interator.QualityInterator;
import com.autohome.plugin.quality.servant.FaultCategoryServant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heqinglin on 2016/5/4.
 */
public class QualityInteratorImpl implements QualityInterator {
    /**
     * 获取故障栏目信息
     * @param type
     * @param seriesId
     * @param readCacheAndNet
     * @param isWriteCache
     * @return
     */
    @Override
    public List<ChooseEntity> getFaultCategoryData(String type, String seriesId, AHBaseServant.ReadCachePolicy readCacheAndNet, boolean isWriteCache) {
        final List<ChooseEntity> chooseEntitys = null;
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
                    chooseEntitys.add(cn);
                }
            }

            @Override
            public void onFailure(AHError error, Object tag) {
                LogUtil.e("error:",error.errorMsg+""+error.toString());
            }
        });
        return chooseEntitys;
    }
}
