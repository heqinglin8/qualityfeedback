package com.autohome.plugin.quality.servant;

import android.text.TextUtils;

import com.autohome.mainlib.business.db.SpHelper;
import com.autohome.mainlib.common.net.BaseServant;
import com.autohome.mainlib.common.util.LogUtil;
import com.autohome.mainlib.common.util.URLFormatter;
import com.autohome.net.core.AHBaseServant;
import com.autohome.net.core.ResponseListener;
import com.autohome.plugin.quality.bean.FaultCategoryEntity;
import com.autohome.plugin.quality.constant.UrlConstant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by heqinglin on 2016/5/4.
 *
 */
public class FaultCategoryServant extends BaseServant<List<FaultCategoryEntity>>{

    private ReadCachePolicy mReadCachePolicy = ReadCachePolicy.ReadNetOnly;
    private boolean mIsAddCache;

    public FaultCategoryServant(ReadCachePolicy readCachePolicy, boolean isAddCache) {
        super();
        this.mReadCachePolicy = readCachePolicy;
        this.mIsAddCache = isAddCache;
    }

    public void getRequestParams(String type, String seriesId, ResponseListener<List<FaultCategoryEntity>> responseListener) {
        List<NameValuePair> params = new LinkedList<NameValuePair>();
        URLFormatter formatter = null;
        params.add(new BasicNameValuePair("sid", seriesId));
        params.add(new BasicNameValuePair("t", type));
        formatter = new URLFormatter(URLFormatter.EParamType.HTML_PARAM, URLFormatter.EParamInfo.NULL, params, UrlConstant.QUALITY_API_CATEGORYPERCENTLIST);
        LogUtil.i("url:",formatter.getFormatUrl());
        getData(formatter.getFormatUrl(), responseListener);
    }


//    @Override
//    public List<FaultCategoryEntity> parseData(String jsonText) throws Exception {
//        LogUtil.i("jsonText:",jsonText);
//        List<FaultCategoryEntity> list = null;
//        JSONObject root = new JSONObject(jsonText);
//        if (root.getInt("returncode") == 0 && root.has("result")) {
//            JSONArray resultArray = root.getJSONArray("result");
//            list = new ArrayList<FaultCategoryEntity>();
//            for(int i=0;i<resultArray.length();i++){
//                JSONObject object = resultArray.getJSONObject(i);
//                FaultCategoryEntity faultEntity = new FaultCategoryEntity();
//                faultEntity.setCategoryid(object.getString("categoryid"));
//                faultEntity.setCategoryname(object.getString("categoryname"));
//                faultEntity.setCategorypercent(object.getString("categorypercent"));
//                faultEntity.setUrl(object.getString("url"));
//                list.add(faultEntity);
//            }
//        }
//        return list;
//    }

    @Override
    public ParseResult<List<FaultCategoryEntity>> parseDataMakeCache(String jsonTxt) throws Exception {
        LogUtil.i("jsonText:",jsonTxt);
        List<FaultCategoryEntity> list = null;
        JSONObject root = new JSONObject(jsonTxt);
        if (root.getInt("returncode") == 0 && root.has("result")) {
            JSONArray resultArray = root.getJSONArray("result");
            list = new ArrayList<FaultCategoryEntity>();
            for(int i=0;i<resultArray.length();i++){
                JSONObject object = resultArray.getJSONObject(i);
                FaultCategoryEntity faultEntity = new FaultCategoryEntity();
                faultEntity.setCategoryid(object.getString("categoryid"));
                faultEntity.setCategoryname(object.getString("categoryname"));
                faultEntity.setCategorypercent(object.getString("categorypercent"));
                faultEntity.setUrl(object.getString("url"));
                list.add(faultEntity);
            }
        }
        boolean shouldWriteCache = false;
        if(list != null){
            if (list != null && list.size() > 0 && mIsAddCache) {
                shouldWriteCache = true;
            }
        }
//			boolean shouldWriteCache = ret != null && mIsAddCache;
        ParseResult<List<FaultCategoryEntity>> parseResult = new ParseResult<List<FaultCategoryEntity>>(list,
                shouldWriteCache);
        return parseResult;
    }

    @Override
    public ReadCachePolicy getReadPolicy() {
        return super.getReadPolicy();
    }

    @Override
    public String getCacheKey() {
        return TAG;
    }
    private final String TAG = "FaultCategoryServant0";

}
