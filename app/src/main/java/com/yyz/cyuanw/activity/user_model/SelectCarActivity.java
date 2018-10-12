package com.yyz.cyuanw.activity.user_model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.activity.MainActivity;
import com.yyz.cyuanw.adapter.IOnListItemClickListenner;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.view.PpxzSecondPopuwindow;
import com.yyz.cyuanw.view.sortrecyclerview.ClearEditText;
import com.yyz.cyuanw.view.sortrecyclerview.PinyinComparator;
import com.yyz.cyuanw.view.sortrecyclerview.PinyinUtils;
import com.yyz.cyuanw.view.sortrecyclerview.SideBar;
import com.yyz.cyuanw.view.sortrecyclerview.SortAdapter;
import com.yyz.cyuanw.view.sortrecyclerview.SortModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import rx.Observer;

public class SelectCarActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private SideBar sideBar;
    private TextView dialog, tv_sd_input;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    LinearLayoutManager manager;
    private View zzc;

    private List<SortModel> SourceDateList;

    private PpxzSecondPopuwindow popuwindow;

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private int level;

    private int xl_id;
    private String xl_name;
    private String xl_carstyle;

    private String name;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_selectcar;
    }

    @Override
    public void initView() {
        findViewById(R.id.id_iv_back).setOnClickListener(this);
        zzc = findViewById(R.id.zzc);
        tv_sd_input = (TextView) findViewById(R.id.tv_sd_input);
        tv_sd_input.setOnClickListener(this);

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sideBar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧SideBar触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //RecyclerView社置manager
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new SortAdapter(this,1);
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //item点击事件
        /*adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, ((SortModel)adapter.getItem(position)).getName(),Toast.LENGTH_SHORT).show();
            }
        });*/
        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        popuwindow = new PpxzSecondPopuwindow(this);

        adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,String name) {
                pp_id = position;
                pp_name = name;
                brandNext(position);
            }
        });
        popuwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                zzc.setVisibility(View.GONE);
            }
        });
        popuwindow.setItemListenner(new IOnListItemClickListenner() {
            @Override
            public void onItemClick(int position, String text,String car_style) {
                if (level == 1){
                    xl_id = position;
                    xl_name = pp_name+text;
                    xl_carstyle = car_style;
                    seriesNext(position);
                }else if(level == 2){
                    Intent intent = new Intent();
                    intent.putExtra("pp_id", pp_id);
                    intent.putExtra("xl_id", xl_id);
                    intent.putExtra("series_id", position);
                    intent.putExtra("series_name",text);
                    intent.putExtra("pp_name",xl_name);
                    intent.putExtra("car_style",xl_carstyle);
                    setResult(RESULT_OK,intent);
                    SelectCarActivity.this.finish();
                }


            }
        });
    }

    private int pp_id = -1;
    private String pp_name;

    @Override
    public void initData() {

        name = getIntent().getStringExtra("name");
        dictionary2();
        top_brand();
    }

    private void showSecond(List<SortModel> list) {
        level = 1;
        popuwindow.setDatas(list);
        if (!popuwindow.isShowing()) {
            popuwindow.showAsDropDown(tv_sd_input, 0, 3);
            zzc.setVisibility(View.VISIBLE);
        } else {
            popuwindow.dismiss();
        }
    }

    private void showThird(List<SortModel> list) {
        level = 2;
        popuwindow.setDatas(list);
        if (!popuwindow.isShowing()) {
            popuwindow.showAsDropDown(tv_sd_input, 0, 3);
            zzc.setVisibility(View.VISIBLE);
        } else {
            popuwindow.dismiss();
        }
    }

    private void setData1(List<SortModel> sortModels) {
        SourceDateList = sortModels;
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);

        adapter.setData(SourceDateList);
    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                        //不区分大小写
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                        ) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateList(filterDateList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_back:
                finish();
                break;
            case R.id.tv_sd_input:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View v = LayoutInflater.from(this).inflate(R.layout.dialog_brandinput,null);
                final EditText et = v.findViewById(R.id.input_text);
                et.setText(name);
                if (StringUtil.isNotNull(name)){
                    et.setSelection(et.getText().toString().length());
                }
                builder.setView(v);
                builder.setTitle("车辆名称");
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                Intent intent = new Intent();
                                intent.putExtra("pp_name",et.getText().toString());
                                setResult(10,intent);
                                SelectCarActivity.this.finish();
                            }
                        });
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
                break;
        }
    }

    public void dictionary2() {
        HttpData.getInstance().dictionary2(new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                LogManager.e("解析出错" + e.getMessage());
            }

            @Override
            public void onNext(ResponseBody result) {
                try {
                    String body = result.string();
                    JSONObject obj = new JSONObject(body);

                    if (obj.getInt("status") == 200) {
                        JSONObject jsonObject = obj.getJSONObject("data").optJSONObject("brand");
                        List<SortModel> sortModels = jx(jsonObject);
                        setData1(sortModels);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void top_brand() {
        HttpData.getInstance().top_brand(new Observer<HttpListResult<SortModel>>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                LogManager.e("解析出错" + e.getMessage());
            }

            @Override
            public void onNext(HttpListResult<SortModel> result) {
                if (result.status == 200) {
                    adapter.setTopData(result.data);
//                    adapter.setData(result.data.info);
//                    adapter.setData(result.data.info);
//                    adapter.startBanner(result.data.ads);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }

    public void brandNext(int id) {
        HttpData.getInstance().brandNext(id, new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                LogManager.e("解析出错" + e.getMessage());
            }

            @Override
            public void onNext(ResponseBody result) {
                try {
                    String body = result.string();
                    JSONObject object = new JSONObject(body);
                    if (object.optInt("status") == 200) {
                        List<SortModel> list = bl(object.optJSONObject("data"));
                        showSecond(list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void seriesNext(int id) {
        HttpData.getInstance().seriesNext(id, new Observer<ResponseBody>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                LogManager.e("解析出错" + e.getMessage());
            }

            @Override
            public void onNext(ResponseBody result) {
                try {
                    String body = result.string();
                    JSONObject object = new JSONObject(body);
                    if (object.optInt("status") == 200) {
                        List<SortModel> list = cl(object.optJSONObject("data"));
                        showThird(list);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static List<String> copyIterator(Iterator<String> iter) {
        List<String> copy = new ArrayList<>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }


    private List<SortModel> bl(JSONObject object) {
        Iterator it = object.keys();
        List<SortModel> listdata = new ArrayList<>();
        while (it.hasNext()) {
            String key = (String) it.next();
            JSONArray array = object.optJSONArray(key);

            SortModel sortModel = new SortModel();
            sortModel.setType(1);
            sortModel.setName(key);

            listdata.add(sortModel);


            List<SortModel> list = jx2(array);
            listdata.addAll(list);
        }
        return listdata;
    }

    private List<SortModel> cl(JSONObject object) {
        Iterator it = object.keys();
        List<String> copy = copyIterator(it);
        Collections.reverse(copy);

        List<SortModel> listdata = new ArrayList<>();
        for (int i=0;i<copy.size();i++){
            //String key = (String) it.next();
            JSONArray array = object.optJSONArray(copy.get(i));

            SortModel sortModel = new SortModel();
            sortModel.setType(1);
            sortModel.setName(copy.get(i));

            listdata.add(sortModel);

            List<SortModel> list = jx3(array);
            listdata.addAll(list);
        }
//        while (it.hasNext()) {
//            String key = (String) it.next();
//            JSONArray array = object.optJSONArray(key);
//
//            SortModel sortModel = new SortModel();
//            sortModel.setType(1);
//            sortModel.setName(key);
//
//            listdata.add(sortModel);
//
//
//            List<SortModel> list = jx3(array);
//            listdata.addAll(list);
//        }
        return listdata;
    }

    private List<SortModel> jx3(JSONArray array) {
        List<SortModel> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.optJSONObject(i);
            SortModel sortModel = new SortModel();
            sortModel.setType(2);
            sortModel.setId(object.optInt("id"));
            sortModel.setName(object.optString("name"));
            sortModel.setEmission_standard(object.optString("emission_standard"));
            sortModel.setGearbox(object.optString("gearbox"));
            sortModel.setPower(object.optString("power"));
            list.add(sortModel);
        }
        return list;
    }

    private List<SortModel> jx2(JSONArray array) {
        List<SortModel> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.optJSONObject(i);
            SortModel sortModel = new SortModel();
            sortModel.setType(2);
            sortModel.setId(object.optInt("id"));
            sortModel.setName(object.optString("name"));
            sortModel.setCar_style(object.optString("car_style"));
            list.add(sortModel);
        }
        return list;
    }

    private List<SortModel> jx(JSONObject jsonObject) {
        String[] arr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        List<SortModel> sortModels = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {

            if (!jsonObject.has(arr[i])) {
                continue;
            }
            JSONArray aa = jsonObject.optJSONArray(arr[i]);
            for (int j = 0; j < aa.length(); j++) {
                JSONObject oo = aa.optJSONObject(j);
                SortModel sortModel = zf(oo);
                sortModels.add(sortModel);
            }
        }

        return sortModels;
    }

    private SortModel zf(JSONObject oo) {
        SortModel sortModel = new SortModel();
        sortModel.setId(oo.optInt("id"));
        sortModel.setName(oo.optString("name"));
        sortModel.setLogo(oo.optString("logo"));
        //汉字转换成拼音
        String pinyin = PinyinUtils.getPingYin(oo.optString("name"));
        String sortString = pinyin.substring(0, 1).toUpperCase();

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            sortModel.setLetters(sortString.toUpperCase());
        } else {
            sortModel.setLetters("#");
        }
        return sortModel;
    }
}
