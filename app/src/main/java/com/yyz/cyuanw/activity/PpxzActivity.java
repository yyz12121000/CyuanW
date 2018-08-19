package com.yyz.cyuanw.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.HttpListResult;
import com.yyz.cyuanw.tools.LogManager;
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
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observer;

public class PpxzActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private SideBar sideBar;
    private TextView dialog, tv_sd_input;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    LinearLayoutManager manager;
    private RelativeLayout rl_title;
    private View zzc;

    private List<SortModel> SourceDateList;

    private PpxzSecondPopuwindow popuwindow;

    /**
     * 根据拼音来排列RecyclerView里面的数据类
     */
    private PinyinComparator pinyinComparator;

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
        adapter = new SortAdapter(this);
        mRecyclerView.setAdapter(adapter);adapter.notifyDataSetChanged();
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
        List<String> pxDatas = new ArrayList<>();
        pxDatas.add("默认排序（发布时间降序）");
        pxDatas.add("价格最低");
        pxDatas.add("价格最高");
        pxDatas.add("车龄最短");
        pxDatas.add("里程最少");
        popuwindow.setDatas(pxDatas);
        adapter.setOnItemClickListener(new SortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!popuwindow.isShowing()) {
                    popuwindow.showAsDropDown(tv_sd_input, 0, 3);
                    zzc.setVisibility(View.VISIBLE);
                } else {
                    popuwindow.dismiss();
                }
            }
        });
        popuwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                zzc.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void initData() {

        dictionary2();
        top_brand();
    }

    private void setData1(List<SortModel> sortModels) {
        SourceDateList = sortModels;
        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);

        adapter.setData(SourceDateList);
    }

    /**
     * 为RecyclerView填充数据
     *
     * @param date
     * @return
     */
//    private List<SortModel> filledData(String[] date) {
//        List<SortModel> mSortList = new ArrayList<>();
//
//        for (int i = 0; i < date.length; i++) {
//            SortModel sortModel = new SortModel();
//            sortModel.setName(date[i]);
//            //汉字转换成拼音
//            String pinyin = PinyinUtils.getPingYin(date[i]);
//            String sortString = pinyin.substring(0, 1).toUpperCase();
//
//            // 正则表达式，判断首字母是否是英文字母
//            if (sortString.matches("[A-Z]")) {
//                sortModel.setLetters(sortString.toUpperCase());
//            } else {
//                sortModel.setLetters("#");
//            }
//
//            mSortList.add(sortModel);
//        }
//        return mSortList;
//
//    }

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
            case R.id.cancel:
                finish();
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
