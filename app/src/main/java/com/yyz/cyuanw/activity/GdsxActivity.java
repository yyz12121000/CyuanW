package com.yyz.cyuanw.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.R;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data3;
import com.yyz.cyuanw.bean.Data4;
import com.yyz.cyuanw.bean.Data5;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.tools.LogManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;


public class GdsxActivity extends BaseActivity {
    @BindView(R.id.id_tv_title)
    TextView titleView;
    @BindView(R.id.hint)
    TextView hint;
    @BindView(R.id.item_1)
    RelativeLayout item_1;
    @BindView(R.id.item_2)
    RelativeLayout item_2;
    @BindView(R.id.item_3)
    RelativeLayout item_3;
    @BindView(R.id.item_4)
    RelativeLayout item_4;
    @BindView(R.id.item_5)
    RelativeLayout item_5;
    @BindView(R.id.item_6)
    RelativeLayout item_6;
    @BindView(R.id.item_7)
    RelativeLayout item_7;

    private boolean hasData = false;
    private Data3 data3;

    private int color = 0;//颜色ID
    private int gearbox = 0;//变速箱ID
    private int emission_standard = 0;//排放标准ID
    private int fuel_type = 0;//燃油类型ID

    private int min_year = 0;//最低车龄 (默认0)
    private int max_year = 0;//最高车龄 (默认0)

    private int min_mileage = 0;//最低里程 (默认0)
    private int max_mileage = 0;//最高里程 (默认0)

    private String key_word = "";
    private int order = 0;//排序 0默认排序 1价格最低 2价格最高 3 车龄最短 4里程最少
    private int source = 0;
    private int brand_id = 0;
    private int series_id = 0;
    private int max_price = 0;
    private int min_price = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_gdsx;
    }

    @Override
    public void initView() {
        setTitle(titleView, "更多筛选");
        ((TextView) item_1.getChildAt(0)).setText("地区");
        ((TextView) item_2.getChildAt(0)).setText("车龄");
        ((TextView) item_3.getChildAt(0)).setText("里程");
        ((TextView) item_4.getChildAt(0)).setText("排放标准");
        ((TextView) item_5.getChildAt(0)).setText("变速箱");
        ((TextView) item_6.getChildAt(0)).setText("燃油类型");
        ((TextView) item_7.getChildAt(0)).setText("颜色");

        ((TextView) item_1.getChildAt(1)).setHint("请选择");
        ((TextView) item_2.getChildAt(1)).setHint("请选择");
        ((TextView) item_3.getChildAt(1)).setHint("请选择");
        ((TextView) item_4.getChildAt(1)).setHint("请选择");
        ((TextView) item_5.getChildAt(1)).setHint("请选择");
        ((TextView) item_6.getChildAt(1)).setHint("请选择");
        ((TextView) item_7.getChildAt(1)).setHint("请选择");
    }

    @Override
    public void initData() {
        Intent intent = getIntent();

        this.key_word = intent.getStringExtra("key_word");
        this.source = intent.getIntExtra("source", 0);
        this.order = intent.getIntExtra("order", 0);
        this.brand_id = intent.getIntExtra("brand_id", 0);
        this.series_id = intent.getIntExtra("series_id", 0);
        this.color = intent.getIntExtra("color", 0);
        this.gearbox = intent.getIntExtra("gearbox", 0);
        this.emission_standard = intent.getIntExtra("emission_standard", 0);
        this.fuel_type = intent.getIntExtra("fuel_type", 0);
        this.max_price = intent.getIntExtra("max_price", 0);
        this.min_price = intent.getIntExtra("min_price", 0);
        this.max_mileage = intent.getIntExtra("max_mileage", 0);
        this.min_mileage = intent.getIntExtra("min_mileage", 0);
        this.max_year = intent.getIntExtra("max_year", 0);
        this.min_year = intent.getIntExtra("min_year", 0);

        carScreeningCount();
        dictionary();
    }


    private void initRight() {
        for (int i = 0; i < data3.car_years.size(); i++) {
            if (data3.car_years.get(i).min == min_year && data3.car_years.get(i).max == max_year) {
                ((TextView) item_2.getChildAt(1)).setText(data3.car_years.get(i).name);
            }
        }

        for (int i = 0; i < data3.car_years.size(); i++) {
            if (data3.mileage_interval.get(i).min == min_mileage && data3.mileage_interval.get(i).max == max_mileage) {
                ((TextView) item_3.getChildAt(1)).setText(data3.mileage_interval.get(i).name);
            }
        }

        ((TextView) item_4.getChildAt(1)).setText(getName(data3.emission_standard, emission_standard));
        ((TextView) item_5.getChildAt(1)).setText(getName(data3.gearbox, gearbox));
        ((TextView) item_6.getChildAt(1)).setText(getName(data3.fuel_type, fuel_type));
        ((TextView) item_7.getChildAt(1)).setText(getName(data3.color, color));


    }

    @OnClick({R.id.reset, R.id.sure, R.id.item_1, R.id.item_2, R.id.item_3, R.id.item_4, R.id.item_5, R.id.item_6, R.id.item_7})
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.item_2:
                Intent intent2 = new Intent(this, ListChooseActivity.class);
                intent2.putStringArrayListExtra("list", getNames(data3.car_years));
                intent2.putExtra("title", "选择排放标准");
                startActivityForResult(intent2, 2);
                break;
            case R.id.item_3:
                Intent intent3 = new Intent(this, ListChooseActivity.class);
                intent3.putStringArrayListExtra("list", getNames(data3.mileage_interval));
                intent3.putExtra("title", "选择排放标准");
                startActivityForResult(intent3, 3);
                break;
            case R.id.item_4:
                Intent intent4 = new Intent(this, ListChooseActivity.class);
                intent4.putStringArrayListExtra("list", getNames(data3.emission_standard));
                intent4.putExtra("title", "选择排放标准");
                startActivityForResult(intent4, 4);
                break;
            case R.id.item_5:
                Intent intent5 = new Intent(this, ListChooseActivity.class);
                intent5.putStringArrayListExtra("list", getNames(data3.gearbox));
                intent5.putExtra("title", "选择变速箱");
                startActivityForResult(intent5, 5);
                break;
            case R.id.item_6:
                Intent intent6 = new Intent(this, ListChooseActivity.class);
                intent6.putStringArrayListExtra("list", getNames(data3.fuel_type));
                intent6.putExtra("title", "选择燃油类型");
                startActivityForResult(intent6, 6);
                break;
            case R.id.item_7:
                Intent intent7 = new Intent(this, ListChooseActivity.class);
                intent7.putStringArrayListExtra("list", getNames(data3.color));
                intent7.putExtra("title", "选择颜色");
                startActivityForResult(intent7, 7);
                break;
            case R.id.reset:
                color = 0;//颜色ID
                gearbox = 0;//变速箱ID
                emission_standard = 0;//排放标准ID
                fuel_type = 0;//燃油类型ID

                min_year = 0;//最低车龄 (默认0)
                max_year = 0;//最高车龄 (默认0)

                min_mileage = 0;//最低里程 (默认0)
                max_mileage = 0;//最高里程 (默认0)

                ((TextView) item_2.getChildAt(1)).setText("");
                ((TextView) item_3.getChildAt(1)).setText("");
                ((TextView) item_4.getChildAt(1)).setText("");
                ((TextView) item_5.getChildAt(1)).setText("");
                ((TextView) item_6.getChildAt(1)).setText("");
                ((TextView) item_7.getChildAt(1)).setText("");
                carScreeningCount();
                break;
            case R.id.sure:
                Intent intent = new Intent();
                intent.putExtra("color", color);
                intent.putExtra("gearbox", gearbox);
                intent.putExtra("emission_standard", emission_standard);
                intent.putExtra("fuel_type", fuel_type);
                intent.putExtra("min_year", min_year);
                intent.putExtra("max_year", max_year);
                intent.putExtra("min_mileage", min_mileage);
                intent.putExtra("max_mileage", max_mileage);
                setResult(RESULT_OK, intent);

                finish();
                break;
        }
    }

    private ArrayList<String> getNames(List<Data4> data) {
        ArrayList<String> list = new ArrayList<>();
        if (null == data) return list;
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i).name);
        }
        return list;
    }

    private String getName(List<Data4> data4s, int id) {
        for (int i = 0; i < data4s.size(); i++) {
            if (data4s.get(i).id == id) {
                return data4s.get(i).name;
            }
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        int index = data.getIntExtra("index", -1);
        String text = data.getStringExtra("text");
        switch (requestCode) {
            case 2:
                ((TextView) item_2.getChildAt(1)).setText(text);
                min_year = data3.car_years.get(index).min;
                max_year = data3.car_years.get(index).max;
                break;
            case 3:
                ((TextView) item_3.getChildAt(1)).setText(text);
                min_mileage = data3.mileage_interval.get(index).min;
                max_mileage = data3.mileage_interval.get(index).max;
                break;
            case 4:
                ((TextView) item_4.getChildAt(1)).setText(text);
                emission_standard = data3.emission_standard.get(index).id;
                break;
            case 5:
                ((TextView) item_5.getChildAt(1)).setText(text);
                gearbox = data3.gearbox.get(index).id;
                break;
            case 6:
                ((TextView) item_6.getChildAt(1)).setText(text);
                fuel_type = data3.fuel_type.get(index).id;
                break;
            case 7:
                ((TextView) item_7.getChildAt(1)).setText(text);
                color = data3.color.get(index).id;
                break;
        }
        carScreeningCount();
    }

    public void dictionary() {
        HttpData.getInstance().search_criteria(new Observer<HttpResult<Data3>>() {
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
            public void onNext(HttpResult<Data3> result) {
                if (result.status == 200) {
                    data3 = result.data;
                    hasData = true;
                    initRight();
//                    adapter.setData(result.data.info);
//                    adapter.setData(result.data.info);
//                    adapter.startBanner(result.data.ads);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }

    public void carScreeningCount() {
        HttpData.getInstance().carScreeningCount(source, order, min_price, max_price, brand_id, series_id, color, gearbox, emission_standard, fuel_type, min_year, max_year, min_mileage, max_mileage, key_word, new Observer<HttpResult<Data5>>() {
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
            public void onNext(HttpResult<Data5> result) {
                if (result.status == 200) {
                    hint.setText("共找到" + result.data.count + "个车源");
                }
            }
        });
    }
}
