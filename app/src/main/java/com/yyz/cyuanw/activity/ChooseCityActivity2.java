package com.yyz.cyuanw.activity;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.bean.Data9;
import com.yyz.cyuanw.view.sortrecyclerview.ClearEditText;
import com.yyz.cyuanw.view.sortrecyclerview.PinyinComparator;
import com.yyz.cyuanw.view.sortrecyclerview.PinyinComparator2;
import com.yyz.cyuanw.view.sortrecyclerview.PinyinUtils;
import com.yyz.cyuanw.view.sortrecyclerview.SideBar;
import com.yyz.cyuanw.view.sortrecyclerview.SortAdapter2;
import com.yyz.cyuanw.view.sortrecyclerview.SortModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChooseCityActivity2 extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private SideBar sideBar;
    private TextView dialog, tv_sd_input;
    private SortAdapter2 adapter;
    private ClearEditText mClearEditText;
    LinearLayoutManager manager;
    private RelativeLayout rl_title;
    private View zzc;

    private List<Data9> SourceDateList;

       /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator2 pinyinComparator;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ppxz;
    }

    @Override
    public void initView() {
        findViewById(R.id.cancel).setOnClickListener(this);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        zzc = findViewById(R.id.zzc);
        tv_sd_input = (TextView) findViewById(R.id.tv_sd_input);

        pinyinComparator = new PinyinComparator2();

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
        adapter = new SortAdapter2(this);
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

//        adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                pp_id = position;
//            }
//        });

    }

    private int pp_id = -1;

    @Override
    public void initData() {
        setData1();
    }

    private void setData1() {
        try {
            String jsonStr = getJson("address.txt", this);
            JSONArray array = new JSONArray(jsonStr);
            SourceDateList =  parse(array);
            adapter.setData(SourceDateList);
        } catch (Exception e) {

        }

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);

       ;


    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<Data9> filterDateList = new ArrayList<>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (Data9 sortModel : SourceDateList) {
                String name = sortModel.name;
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
            case R.id.cancel:
                finish();
                break;
        }
    }
    private List<Data9> parse(JSONArray array) {
        List<Data9> list = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject sheng = array.optJSONObject(i);
                Data9 sheng_data = new Data9();
                sheng_data.id = sheng.optInt("id");
                sheng_data.name = sheng.optString("name");
                sheng_data.initials = sheng.optString("initials");
                sheng_data.pinyin = sheng.optString("pinyin");

                sheng_data.letters =  sheng_data.pinyin.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if ( sheng_data.letters.matches("[A-Z]")) {
                    sheng_data.letters =  sheng_data.letters.toUpperCase();
                } else {
                    sheng_data.letters = "#";
                }

                sheng_data.son = new ArrayList<>();

                JSONArray sheng_son = sheng.optJSONArray("son");
                if (null == sheng_son || sheng_son.length() == 0) {
                    continue;
                }
                for (int j = 0; j < sheng_son.length(); j++) {
                    JSONObject shi = array.optJSONObject(i);
                    Data9 shi_data = new Data9();
                    shi_data.id = shi.optInt("id");
                    shi_data.name = shi.optString("name");
                    shi_data.initials = shi.optString("initials");
                    shi_data.pinyin = shi.optString("pinyin");
                    shi_data.son = new ArrayList<>();
                    JSONArray shi_son = shi.optJSONArray("son");
                    if (null == shi_son || shi_son.length() == 0) {
                        continue;
                    }
                    for (int k = 0; k < sheng_son.length(); k++) {
                        JSONObject qu = array.optJSONObject(i);
                        Data9 qu_data = new Data9();
                        qu_data.id = shi.optInt("id");
                        qu_data.name = shi.optString("name");
                        qu_data.initials = shi.optString("initials");
                        qu_data.pinyin = shi.optString("pinyin");
                        shi_data.son.add(qu_data);
                    }
                    sheng_data.son.add(shi_data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


}
