
package com.autohome.plugin.quality.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.autohome.mainlib.business.db.AppConfigDb;
import com.autohome.mainlib.common.bean.ChooseEntity;
import com.autohome.mainlib.common.constant.AHConstant;
import com.autohome.mainlib.common.util.ResUtil;
import com.autohome.mainlib.common.view.drawerview.AHSkinMainDrawer;
import com.autohome.plugin.quality.R;
import com.autohome.plugin.quality.adapter.SingleMultipleListDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class GuZhangDrawer extends AHSkinMainDrawer {

    private ListView listview;
    private onItemSelectListener listener;
    private List<ChooseEntity> list;
    private SingleMultipleListDataAdapter adapter;


    public GuZhangDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initize();
    }

    public GuZhangDrawer(Context context,List<ChooseEntity> list) {
        super(context);
        this.list = list;
        initize();
    }

    public void setList(List<ChooseEntity> list) {
        this.list = list;
        adapter.setList(list);
    }

    private void initize() {
        listview = (ListView) findMainViewById(R.id.drawer_list);
        listview.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        adapter = new SingleMultipleListDataAdapter(context,false);
//        loadData();
        if(list != null && list.size() > 0){
        	ChooseEntity en = list.get(0);
        	en.setChecked(true);
        }
        adapter.setList(list);
        adapter.setListView(listview);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChooseEntity entity = list.get(position);
                String name=entity.getName();
                closeDrawer();
                setListIndexSelector(entity);
                if(listener!=null)
                    listener.choose(entity, position);
            }
        });
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        onSkinChanged();
    }

    public void notifyDataSetChanged(){
        adapter.notifyDataSetChanged();
    }
    /**
     * 加载数据
     */
//    private void loadData() {
//        //TODO 记得冲网上获取分类信息
////        list = AppConfigDb.getInstance().getConfig(AHConstant.JingXuan, AHConstant.APP_CONFIG_TYPE_META_DATA);
//        if(list == null)
//            list = new ArrayList<ChooseEntity>();
//        for(int i=0;i<10;i++){
//            ChooseEntity entity = new ChooseEntity();
//            entity.setSid(i+"");
//            entity.setName("故障"+i);
//            list.add(entity);
//        }
//    }

    /**
     * 设定点击之后选中项目 遍历
     * @param entity 当前选中的对像
     */
	public void setListIndexSelector(ChooseEntity entity) {
		if (list != null) {
			for (ChooseEntity ce : list) {
				if (ce.getName().equals(entity.getName())) {
					ce.setChecked(true);
				} else {
					ce.setChecked(false);
				}
			}
			((SingleMultipleListDataAdapter) listview.getAdapter()).notifyDataSetChanged();
		}
	}
	
	@Override
	public void openDrawer() {
		super.openDrawer();
		((SingleMultipleListDataAdapter) listview.getAdapter()).notifyDataSetChanged();
	}

    @Override
    public View getMainView() {
        View view = View.inflate(context, R.layout.price_drawer, null);
        return view;
    }

    public interface onItemSelectListener
    {
        public void choose(ChooseEntity entity, int position);
    }
    public void setOnItemClickListener(onItemSelectListener listener)
    {
        
        this.listener=listener;
    }
    //TODO liyunfei
    
    /**
    @Override
    public void findMainViews() {
        // TODO Auto-generated method stub
       

//        String json = "[{\"value\":\"0\",\"type\":\"1\",\"name\":\"全部\"},{\"value\":\"19\",\"type\":\"1\",\"name\":\"精挑细选\"},{\"value\":\"1\",\"type\":\"1\",\"name\":\"媳妇当车模\"},{\"value\":\"24\",\"type\":\"1\",\"name\":\"超级试驾员\"},{\"value\":\"16\",\"type\":\"1\",\"name\":\"高端阵地\"},{\"value\":\"3\",\"type\":\"1\",\"name\":\"现身说法\"},{\"value\":\"7\",\"type\":\"1\",\"name\":\"美人“记”\"},{\"value\":\"17\",\"type\":\"1\",\"name\":\"首发阵营\"},{\"value\":\"12\",\"type\":\"1\",\"name\":\"藏地之旅\"},{\"value\":\"11\",\"type\":\"1\",\"name\":\"海外购车\"},{\"value\":\"5\",\"type\":\"1\",\"name\":\"蜜月之旅\"},{\"value\":\"2\",\"type\":\"1\",\"name\":\"顶配风采\"},{\"value\":\"22\",\"type\":\"1\",\"name\":\"80后婚礼\"},{\"value\":\"4\",\"type\":\"1\",\"name\":\"原创大片\"},{\"value\":\"15\",\"type\":\"1\",\"name\":\"走遍全球\"},{\"value\":\"6\",\"type\":\"1\",\"name\":\"经典老车\"},{\"value\":\"13\",\"type\":\"1\",\"name\":\"彩云之南\"},{\"value\":\"20\",\"type\":\"1\",\"name\":\"改装有理\"},{\"value\":\"14\",\"type\":\"1\",\"name\":\"海南风情\"},{\"value\":\"9\",\"type\":\"1\",\"name\":\"即时直播\"},{\"value\":\"21\",\"type\":\"1\",\"name\":\"摄影课堂\"},{\"value\":\"8\",\"type\":\"1\",\"name\":\"小鬼当家\"},{\"value\":\"18\",\"type\":\"1\",\"name\":\"探访海外4S\"}]";
//        try {
//            JSONArray root = new JSONArray(json);
//            for (int i = 0; i < root.length(); i++) {
//                JSONObject obj = root.getJSONObject(i);
//                ChooseEntity entity = new ChooseEntity();
//                entity.setName(obj.getString("name"));
//                entity.setSid(obj.getString("value"));
//                entity.setType(obj.getString("type"));
//                list.add(entity);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
       
    }
*/
    @Override
    public View getSubView() {
       return null;
    }

    //TODO liyunfei
//    @Override
//    public void findSubViews() {
//        // TODO Auto-generated method stub
//
//    }

    @Override
    public void onClickFinish() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClickCancel() {
        // TODO Auto-generated method stub

    }

    @Override
    public String getTitleName() {
        // TODO Auto-generated method stub
        return "故障分类";
    }
    
	@Override
	public void onSkinChanged() {
		super.onSkinChanged();
        listview.setDivider(new ColorDrawable(ResUtil.getColor(context, ResUtil.BG_COLOR_04)));
        listview.setDividerHeight(1);
        ((SingleMultipleListDataAdapter) listview.getAdapter()).notifyDataSetChanged();
	}



}
