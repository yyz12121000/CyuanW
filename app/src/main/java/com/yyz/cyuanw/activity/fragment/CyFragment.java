package com.yyz.cyuanw.activity.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.CyDetailActivity;
import com.yyz.cyuanw.activity.GdsxActivity;
import com.yyz.cyuanw.activity.MainActivity;
import com.yyz.cyuanw.activity.PpxzActivity;
import com.yyz.cyuanw.activity.WdLmActivity;
import com.yyz.cyuanw.activity.user_model.LoginActivity;
import com.yyz.cyuanw.adapter.IOnListItemClickListenner;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.Data1;
import com.yyz.cyuanw.bean.Data2;
import com.yyz.cyuanw.bean.Data9;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.LoginData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.tools.Img;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.view.JgqjPopuwindow;
import com.yyz.cyuanw.view.ListPopuwindow;
import com.yyz.cyuanw.view.PullRV;
import com.yyz.cyuanw.view.choosecity.DBManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

public class CyFragment extends Fragment implements View.OnClickListener, PopupWindow.OnDismissListener {
    private RecyclerView list;
    private ListPopuwindow cyPopuwindow;
    private ListPopuwindow pxPopuwindow;
    private JgqjPopuwindow jgqjPopuwindow;
    private TextView tv_1, tv_2, tv_3, tv_4;
    private ListAdapter adapter;
    private View black;

    private int is_self = 0;//是否搜索自己的共享车源 0 否 1是
    private String key_word = "";//搜索关键词
    private int source = 0;//车源来源 0全部 1 批发 2 急售 3 新车
    private int order = 0;//排序 0默认排序 1价格最低 2价格最高 3 车龄最短 4里程最少

    private List<Integer> province_id = new ArrayList<>();//省份ID
    private List<Integer> city_id = new ArrayList<>();//城市ID
    private int region_id = 0;//地区ID

    private int brand_id = 0;//品牌ID
    private int series_id = 0;//品牌系列ID
    private int color = 0;//颜色ID
    private int gearbox = 0;//变速箱ID
    private int emission_standard = 0;//排放标准ID
    private int fuel_type = 0;//燃油类型ID
    private int max_price = 0;//最高价 (默认0)
    private int min_price = 0;//最低价 (默认0)
    private int max_mileage = 0;//最高里程 (默认0)
    private int min_mileage = 0;//最低里程 (默认0)
    private int max_year = 0;//最高车龄 (默认0)
    private int min_year = 0;//最低车龄 (默认0)
//    private int page = 1;//页码 从1开始 默认第一页

    private String selectCity;

    private DBManager dbManager;

    PullRV pullRV;

    private int itemId,from_type=1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cy, container, false);

        init(view);

        LogManager.d("===onCreateView");
        return view;
    }

    private void init(View view) {
        tv_1 = view.findViewById(R.id.tv_1);
        tv_1.setOnClickListener(this);
        tv_2 = view.findViewById(R.id.tv_2);
        tv_2.setOnClickListener(this);
        tv_3 = view.findViewById(R.id.tv_3);
        tv_3.setOnClickListener(this);
        tv_4 = view.findViewById(R.id.tv_4);
        tv_4.setOnClickListener(this);
        black = view.findViewById(R.id.black);
        view.findViewById(R.id.gdsx).setOnClickListener(this);

        List<String> cydatas = new ArrayList<>();
        cydatas.add("全部车源");
        cydatas.add("批发车源");
        cydatas.add("急售车源");
        cydatas.add("新车车源");
        List<String> pxDatas = new ArrayList<>();
        pxDatas.add("默认排序（发布时间降序）");
        pxDatas.add("价格最低");
        pxDatas.add("价格最高");
        pxDatas.add("车龄最短");
        pxDatas.add("里程最少");
        cyPopuwindow = new ListPopuwindow(getActivity());
        pxPopuwindow = new ListPopuwindow(getActivity());
        jgqjPopuwindow = new JgqjPopuwindow(getActivity());
        cyPopuwindow.setDatas(cydatas);
        pxPopuwindow.setDatas(pxDatas);
        list = view.findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CyFragment.ListAdapter();
        list.setAdapter(adapter);

        pullRV = view.findViewById(R.id.xrefreshview);

        pullRV.postDelayed(new Runnable() {
            @Override
            public void run() {
                //pullRV.startRefresh();
                doSearchByCity(App.get(Constant.KEY_LOCATION_CITY));
            }
        }, 500);

        pullRV.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
//                if (StringUtil.isNotNull(App.get(Constant.KEY_LOCATION_CITY))){
//                    Data9 sheng = dbManager.findLocationCity(App.get(Constant.KEY_LOCATION_CITY));
//                    province_id.clear();//省份ID
//                    city_id.clear();//城市ID
//                    //this.region_id = 0;//地区ID
//                    province_id.add(sheng.id);
//                    city_id.add(sheng.son.get(0).id);
//                }
                searchCy(true, pullRV.page = 1);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                searchCy(false, ++pullRV.page);
            }
        });

        cyPopuwindow.setItemListenner(new IOnListItemClickListenner() {
            @Override
            public void onItemClick(int position, String text,String car_style) {
//               source = 0;//车源来源 0全部 1 批发 2 急售 3 新车
                tv_1.setText(text);
                source = position;
                searchCy(true, pullRV.page = 1);
            }
        });
        pxPopuwindow.setItemListenner(new IOnListItemClickListenner() {
            @Override
            public void onItemClick(int position, String text,String car_style) {

                if(text.contains("默认排序")){
                    tv_2.setText("默认排序");
                }else{
                    tv_2.setText(text);
                }
//                order = 0;//排序 0默认排序 1价格最低 2价格最高 3 车龄最短 4里程最少
                order = position;
                searchCy(true, pullRV.page = 1);
            }
        });
        jgqjPopuwindow.setItemListenner(new IOnListItemClickListenner() {
            public void onItemClick(int position, String text, int a, int b) {
                tv_4.setText(text);
//                private int max_price = 0;//最高价 (默认0)
//                private int min_price = 0;//最低价 (默认0)
                min_price = a;
                max_price = b;
                searchCy(true, pullRV.page = 1);
            }
        });
        cyPopuwindow.setOnDismissListener(this);
        pxPopuwindow.setOnDismissListener(this);
        jgqjPopuwindow.setOnDismissListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                ((TextView) view).setTextColor(Color.parseColor("#EA6F5A"));
                if (!cyPopuwindow.isShowing()) {
                    cyPopuwindow.showAsDropDown(view, view.getLayoutParams().width / 2, 18);
                } else {
                    cyPopuwindow.dismiss();
                }
                break;
            case R.id.tv_2:
                ((TextView) view).setTextColor(Color.parseColor("#EA6F5A"));
                if (!pxPopuwindow.isShowing()) {
                    pxPopuwindow.showAsDropDown(view, view.getLayoutParams().width / 2, 18);
                } else {
                    pxPopuwindow.dismiss();
                }
                break;
            case R.id.tv_3:
                startActivityForResult(new Intent(CyFragment.this.getActivity(), PpxzActivity.class), 55);
                break;
            case R.id.tv_4:
                ((TextView) view).setTextColor(Color.parseColor("#EA6F5A"));
                if (!pxPopuwindow.isShowing()) {
                    jgqjPopuwindow.showAsDropDown(view, view.getLayoutParams().width / 2, 18);
                } else {
                    jgqjPopuwindow.dismiss();
                }
                break;
            case R.id.gdsx:
                Intent intent = new Intent(CyFragment.this.getActivity(), GdsxActivity.class);
                intent.putExtra("key_word", key_word);
                intent.putExtra("source", source);
                intent.putExtra("order", order);
                intent.putExtra("order", order);
                intent.putExtra("brand_id", brand_id);
                intent.putExtra("series_id", series_id);
                intent.putExtra("color", color);
                intent.putExtra("gearbox", gearbox);
                intent.putExtra("emission_standard", emission_standard);
                intent.putExtra("fuel_type", fuel_type);
                intent.putExtra("max_price", max_price);
                intent.putExtra("min_price", min_price);
                intent.putExtra("max_mileage", max_mileage);
                intent.putExtra("min_mileage", min_mileage);
                intent.putExtra("max_year", max_year);
                intent.putExtra("min_year", min_year);
                intent.putExtra("city", selectCity);

                startActivityForResult(intent, 66);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == 44){
                Intent intent = new Intent(CyFragment.this.getActivity(), CyDetailActivity.class);
                intent.putExtra("id", itemId);
                intent.putExtra("flag", 1);
                intent.putExtra("from_type",from_type);
                CyFragment.this.getActivity().startActivity(intent);
            } else if (requestCode == 55) {
                int pp_id = data.getIntExtra("pp_id", -1);
                int xl_id = data.getIntExtra("xl_id", -1);
                if (xl_id != -1 && pp_id != -1) {
                    brand_id = pp_id;
                    series_id = xl_id;
                    //tv_3.setText(data.getStringExtra("name"));
                    searchCy(true, pullRV.page = 1);
                }
            } else if (requestCode == 66) {
                color = data.getIntExtra("color", 0);//颜色ID
                gearbox = data.getIntExtra("gearbox", 0);//变速箱ID
                emission_standard = data.getIntExtra("emission_standard", 0);//排放标准ID
                fuel_type = data.getIntExtra("fuel_type", 0);//燃油类型ID

                min_year = data.getIntExtra("min_year", 0);//最低车龄 (默认0)
                max_year = data.getIntExtra("max_year", 0);//最高车龄 (默认0)

                min_mileage = data.getIntExtra("min_mileage", 0);//最低里程 (默认0)
                max_mileage = data.getIntExtra("max_mileage", 0);//最高里程 (默认0)

                searchCy(true, pullRV.page = 1);
            }
        }
    }

    @Override
    public void onDismiss() {
        tv_1.setTextColor(Color.parseColor("#666666"));
        tv_2.setTextColor(Color.parseColor("#666666"));
        tv_3.setTextColor(Color.parseColor("#666666"));
        tv_4.setTextColor(Color.parseColor("#666666"));
    }


    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Data2> data = new ArrayList<>();

        public void setData(List<Data2> data) {
            if (null != data) {
                this.data = data;

                if (this.data.size() == 0) {
                    black.setVisibility(View.VISIBLE);
                } else {
                    black.setVisibility(View.GONE);
                }

                notifyDataSetChanged();
            }
        }
        public void appendData(List<Data2> data) {
            if (null != data) {
                this.data.addAll(data);
                notifyDataSetChanged();
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_cy, viewGroup, false);
            CyFragment.ListAdapter.VHolder vHolder = new CyFragment.ListAdapter.VHolder(view);
            return vHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ((VHolder) viewHolder).setData();
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        private class VHolder extends RecyclerView.ViewHolder {
            private ImageView gx_iv, the_new, iv_j, iv_p;
            private TextView tv_title, tv_sp, tv_gl, tv_dd, tv_jg, gx_time;
            private SwipeMenuLayout swipemenulayout;

            public VHolder(@NonNull View itemView) {
                super(itemView);
                gx_iv = itemView.findViewById(R.id.gx_iv);
                the_new = itemView.findViewById(R.id.the_new);
                iv_j = itemView.findViewById(R.id.iv_j);
                iv_p = itemView.findViewById(R.id.iv_p);

                tv_title = itemView.findViewById(R.id.tv_title);
                tv_sp = itemView.findViewById(R.id.tv_sp);
                tv_gl = itemView.findViewById(R.id.tv_gl);
                tv_dd = itemView.findViewById(R.id.tv_dd);
                tv_jg = itemView.findViewById(R.id.tv_jg);
                gx_time = itemView.findViewById(R.id.gx_time);
                swipemenulayout = itemView.findViewById(R.id.swipemenulayout);
                swipemenulayout.setSwipeEnable(false);swipemenulayout.setClickable(true);
                itemView.findViewById(R.id.layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = getAdapterPosition();
                        itemId = data.get(position).id;

                        if (StringUtil.isNotNull(App.get(Constant.KEY_USER_TOKEN))) {
                            Intent intent = new Intent(CyFragment.this.getActivity(), CyDetailActivity.class);
                            intent.putExtra("id", itemId);
                            intent.putExtra("flag", 1);
                            intent.putExtra("from_type",from_type);
                            CyFragment.this.getActivity().startActivity(intent);
                        } else {
                            //startActivity(new Intent(CyFragment.this.getActivity(), LoginActivity.class));
                            startActivityForResult(new Intent(CyFragment.this.getActivity(), LoginActivity.class), 44);
                        }
                    }
                });

            }

            public void setData() {
                int position = getAdapterPosition();
                Data2 data2 = data.get(position);

                Img.loadD(gx_iv, data2.cover,R.mipmap.ic_cy);
                tv_title.setText(data2.name);
                tv_sp.setText(data2.license_plate_time);
                tv_gl.setText(data2.mileage + "公里");
                tv_dd.setText(data2.location);
                tv_jg.setText(data2.retail_offer + "万");
                gx_time.setText(data2.publish_time);

                if (data2.is_new_car == 1)//是否新车 0否 1是
                {
                    the_new.setVisibility(View.VISIBLE);
                } else {
                    the_new.setVisibility(View.GONE);
                }
                if (data2.urgent == 1)//急售 0否 1是
                {
                    iv_j.setVisibility(View.VISIBLE);
                } else {
                    iv_j.setVisibility(View.GONE);
                }
                if (data2.wholesale == 1)//批发 0否 1是
                {
                    iv_p.setVisibility(View.VISIBLE);
                } else {
                    iv_p.setVisibility(View.GONE);
                }
            }
        }
    }

    public void doSearch(String key_word) {
        this.key_word = key_word;
        searchCy(true, pullRV.page = 1);
    }

    public void doSearchByCity(String city) {
        dbManager = new DBManager(App.context);
        if (StringUtil.isNotNull(city)){
            selectCity = city;
            Data9 sheng = dbManager.findLocationCity(city);
            province_id.clear();//省份ID
            city_id.clear();//城市ID
            province_id.add(sheng.id);
            city_id.add(sheng.son.get(0).id);
        }
        searchCy(true, 1);
    }

    public void doSearchByAdress(int province,int city) {
        this.province_id.clear();//省份ID
        this.city_id.clear();//城市ID
//        this.region_id = 0;//地区ID
        this.province_id.add(province);
        this.city_id.add(city);
        //this.province_id.add(1111);
        //this.city_id.add(2222);
        searchCy(true, pullRV.page = 1);
    }

    public void searchCy(boolean isRefresh,int page) {
        HttpData.getInstance().searchCy(page,province_id,city_id,region_id,source, order, min_price, max_price, brand_id, series_id, color, gearbox, emission_standard, fuel_type, min_year, max_year, min_mileage, max_mileage, key_word, new Observer<HttpResult<Data1>>() {
            @Override
            public void onCompleted() {
//                App.showToast("999");
            }

            @Override
            public void onError(Throwable e) {
//                App.showToast("服务器请求超时");
                pullRV.stopRefresh();
                LogManager.e("解析出错" + e.getMessage());
            }

            @Override
            public void onNext(HttpResult<Data1> result) {
                if (result.status == 200) {
                    if (isRefresh){
                        adapter.setData(result.data.info);
                        pullRV.stopRefresh();
                    }else {
                        adapter.appendData(result.data.info);
                        pullRV.checkhasMore(result.data.info.size());
                    }

//                    adapter.setData(result.data.info);
//                    adapter.startBanner(result.data.ads);
                } else {
//                    App.showToast(result.message);
                }
            }
        });
    }


}
