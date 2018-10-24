package com.yyz.cyuanw.activity.user_model;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyz.cyuanw.App;
import com.yyz.cyuanw.R;
import com.yyz.cyuanw.activity.BaseActivity;
import com.yyz.cyuanw.activity.LmDetailDetailEditActivity;
import com.yyz.cyuanw.activity.PpxzActivity;
import com.yyz.cyuanw.activity.fragment.CyFragment;
import com.yyz.cyuanw.apiClient.HttpData;
import com.yyz.cyuanw.bean.CyListData;
import com.yyz.cyuanw.bean.HttpCodeResult;
import com.yyz.cyuanw.bean.HttpResult;
import com.yyz.cyuanw.bean.PublicCarData;
import com.yyz.cyuanw.common.Constant;
import com.yyz.cyuanw.oss.Oss;
import com.yyz.cyuanw.tools.FileUtils;
import com.yyz.cyuanw.tools.LQRPhotoSelectUtils;
import com.yyz.cyuanw.tools.LogManager;
import com.yyz.cyuanw.tools.StringUtil;
import com.yyz.cyuanw.tools.ToastUtil;
import com.yyz.cyuanw.tools.Tools;
import com.yyz.cyuanw.view.CommonPopupDialog;
import com.yyz.cyuanw.view.CustomProgress;
import com.yyz.cyuanw.view.DatePickerDialog;
import com.yyz.cyuanw.view.ImageSelector.Pickture;
import com.yyz.cyuanw.view.ImageSelector.PicktureActivity;
import com.yyz.cyuanw.view.ImageSelector.interf.OperateListenerAdapter;
import com.yyz.cyuanw.view.ImageSelector.widget.PickRecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import rx.Observer;

public class SendCarActivity extends BaseActivity{

    @BindView(R.id.id_tv_title) TextView titleView;
    @BindView(R.id.title_right_text) TextView sendView;
    @BindView(R.id.__prv) PickRecyclerView mPickRecyclerView;
    @BindView(R.id.__prv2) PickRecyclerView mgxPickRecyclerView;
    @BindView(R.id.id_et_phone) EditText vinView;
    @BindView(R.id.id_tv_content) TextView cartypeView;
    @BindView(R.id.id_switch_view) TextView newcarView;
    @BindView(R.id.id_et_distance) EditText distanceView;
    @BindView(R.id.id_tv_type) TextView typeView;
    @BindView(R.id.id_tv_time) TextView timeView;
    @BindView(R.id.id_tv_color) TextView colorView;
    @BindView(R.id.id_tv_bsx) TextView bsxView;
    @BindView(R.id.id_tv_rylx) TextView rylxView;
    @BindView(R.id.id_tv_pfbz) TextView pfbzView;
    @BindView(R.id.id_et_pl) EditText plView;
    @BindView(R.id.id_tv_njdq) TextView njdqView;
    @BindView(R.id.id_tv_bxdq) TextView bxdqView;
    @BindView(R.id.id_et_kw) EditText kwView;
    @BindView(R.id.id_et_lsbj) EditText lsbjView;
    @BindView(R.id.id_et_pfbj) EditText pfbjView;
    @BindView(R.id.id_et_clms) EditText clmsView;
    @BindView(R.id.id_switch_gxcy) TextView gxcyView;
    @BindView(R.id.id_tv_more) TextView moreView;
    @BindView(R.id.id_linear_more) LinearLayout rootMoreView;
    @BindView(R.id.id_linear_gx) LinearLayout rootGxView;
    @BindView(R.id.id_et_gxdj) EditText gxdjView;
    @BindView(R.id.id_et_gxclms) EditText gxclmsView;

    private TextView nameView;
    private ListView listView;

    private View popupDialogView;
    private CommonPopupDialog mPopupDialog;
    private Dialog mDialog;

    private int newcarFlag;

    private final int COLUMN = 3, MAX = 24;
    private Pickture mPickture;
    private Pickture mgxPickture;

    private ArrayList<String> selectedList = new ArrayList<>();
    private ArrayList<String> tempList = new ArrayList<>();
    private ArrayList<String> tempImgPath = new ArrayList<>();

    private ArrayList<String> gxselectedList = new ArrayList<>();
    private ArrayList<String> gxtempList = new ArrayList<>();
    private ArrayList<String> gxtempImgPath = new ArrayList<>();

    private String[] colorArr = new String[]{"黑色","红色","蓝色","白色","绿色","黄色","银灰","灰色","橙色","香槟色","咖啡色","紫色","其它"};
    private String[] speedArr = new String[]{"手动","自动"};
    private String[] rylxArr = new String[]{"汽油","柴油","电动","混动"};
    private String[] pfbzArr = new String[]{"国一","国二","国三","国四","国五","国六"};
    private String[] cllxArr = new String[]{"轿车","跑车","越野车","商务车","皮卡","面包车","客车","货车","工程车"};

    private int selectType;

    private Oss oss;

    private List<String> images = new ArrayList<>();
    private List<String> gximages = new ArrayList<>();

    private int is_new,is_share;

    private int brand_id = 0;//品牌ID
    private int series_id = 0;//品牌系列ID
    private int style_id = 0;

    private int flag;
    private int id;

    private PublicCarData carData;

    private int pickType;

    private boolean shareClicked = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sendcar;
    }

    @Override
    public void initView() {
        setTitle(titleView,"发布车源");
        sendView.setVisibility(View.VISIBLE);

        mPickture = Pickture.with(this).column(COLUMN).max(MAX).hasCamera(true).selected(tempList);
        mgxPickture = Pickture.with(this).column(COLUMN).max(MAX).hasCamera(true).selected(gxtempList);

        mPickture.showOn(mPickRecyclerView);
        mgxPickture.showOn(mgxPickRecyclerView);

        mPickRecyclerView.setOnOperateListener(new OperateListenerAdapter() {

            @Override
            public void onClickAdd() {
                //checkPermission();
                //mPickture.selected(tempList).create();
                pickType = 0;
                PermissionGen.needPermission(SendCarActivity.this,
                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                );
            }

        });

        mgxPickRecyclerView.setOnOperateListener(new OperateListenerAdapter() {

            @Override
            public void onClickAdd() {
                //checkPermission();
                //mPickture.selected(tempList).create();

                pickType = 1;
                PermissionGen.needPermission(SendCarActivity.this,
                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                );
            }

        });

        plView.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(".") && dest.toString().length() == 0){
                    return "0.";
                }
                if(dest.toString().contains(".")){
                    int index = dest.toString().indexOf(".");
                    int length = dest.toString().substring(index).length();
                    if(length == 2){
                        return "";
                    }
                }
                return null;
            }
        }});

    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {

        if (pickType == 0){
            mPickture.selected(tempList).create();
        }else{
            mgxPickture.selected(gxtempList).create();
        }
    }

    @Override
    public void initData() {
        oss = new Oss();

        flag = getIntent().getIntExtra("flag",0);
        id = getIntent().getIntExtra("id",0);

        if (flag == 1){
            getCarInfo();
        }
    }

    @OnClick({R.id.title_right_text,R.id.id_tv_content,R.id.id_switch_view,R.id.id_tv_type,R.id.id_tv_bsx,R.id.id_tv_color,R.id.id_tv_more,
            R.id.id_tv_rylx,R.id.id_tv_bxdq,R.id.id_tv_njdq,R.id.id_tv_time,R.id.id_tv_pfbz,R.id.id_switch_gxcy})
    public void onClickEvent(View view){
        switch (view.getId()){
            case R.id.title_right_text:

                String carInfo = cartypeView.getText().toString();
                if (!StringUtil.isNotNull(carInfo)){
                    App.showToast("请选择品牌型号");
                    return;
                }

                String timeInfo = timeView.getText().toString();
                if (is_new == 0){
                    if (!StringUtil.isNotNull(timeInfo)){
                        App.showToast("请选择上牌时间");
                        return;
                    }
                }

                String lsbj = lsbjView.getText().toString();
                if (!StringUtil.isNotNull(lsbj)){
                    App.showToast("请输入零售报价");
                    return;
                }

                if (flag == 1){
                    editCarSource(carInfo,timeInfo,lsbj);
                }else{
                    CustomProgress.show(this, "车源发布中...", true, null);

                    if (tempImgPath.size() > 0){
                        uploadPic(carInfo,timeInfo,lsbj,0);
                    }else{
                        if (gxtempImgPath.size() > 0){
                            uploadgxPic(carInfo,timeInfo,lsbj,0);
                        }
                    }
                }
                //sendCarSource(carInfo,timeInfo,lsbj);
                break;
            case R.id.id_tv_content:

                Intent intent = new Intent(this, SelectCarActivity.class);
                intent.putExtra("name",cartypeView.getText().toString());
                startActivityForResult(intent, 10);
                break;
            case R.id.id_switch_view:

                if (newcarFlag == 0){
                    is_new=newcarFlag = 1;
                    newcarView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.switch_on,0);
                    timeView.setText("新车未上牌");
                    njdqView.setText("新车");
                    bxdqView.setText("新车");
                    distanceView.setEnabled(false);
                    njdqView.setEnabled(false);
                    bxdqView.setEnabled(false);
                }else{
                    is_new=newcarFlag = 0;
                    newcarView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.switch_off,0);
                    timeView.setText("");
                    njdqView.setText("");
                    bxdqView.setText("");
                    distanceView.setEnabled(true);
                    njdqView.setEnabled(true);
                    bxdqView.setEnabled(true);
                }
                break;
            case R.id.id_tv_color:

                selectType = 0;
                showPopupDialog(selectType);
                break;
            case R.id.id_tv_bsx:

                selectType = 1;
                showPopupDialog(selectType);
                break;
            case R.id.id_tv_rylx:

                selectType = 2;
                showPopupDialog(selectType);
                break;
            case R.id.id_tv_pfbz:

                selectType = 3;
                showPopupDialog(selectType);
                break;
            case R.id.id_tv_type:

                selectType = 4;
                showPopupDialog(selectType);
                break;
            case R.id.id_tv_njdq:

                createDataDialog(njdqView);
                break;
            case R.id.id_tv_bxdq:

                createDataDialog(bxdqView);
                break;
            case R.id.id_tv_time:

                createDataDialog(timeView);
                break;
            case R.id.id_switch_gxcy:

                if (is_share == 0){
                    is_share = 1;
                    gxcyView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.switch_on,0);
                    rootGxView.setVisibility(View.VISIBLE);

                    if (flag == 0){
                        if (shareClicked == false){
                            shareClicked = true;

                            if (selectedList.size() > 0){
                                gxselectedList.addAll(selectedList);
                                gxtempList.addAll(tempList);
                                gxtempImgPath.addAll(tempImgPath);

                                mgxPickRecyclerView.bind(gxtempList);
                            }

                            gxclmsView.setText(clmsView.getText().toString());
                            gxclmsView.setSelection(gxclmsView.getText().toString().length());

                        }

                    }

                }else{
                    is_share = 0;
                    gxcyView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.switch_off,0);
                    rootGxView.setVisibility(View.GONE);
                }
                break;
            case R.id.id_tv_more:

                if (rootMoreView.getVisibility() == View.GONE){
                    moreView.setText("收起");
                    rootMoreView.setVisibility(View.VISIBLE);
                }else{
                    moreView.setText("更多信息");
                    rootMoreView.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void setCarInfo(){

        if (carData != null){

            vinView.setText(carData.vin_code);
            cartypeView.setText(carData.name);
            if (carData.is_new == 1){
                is_new=newcarFlag = 1;
                newcarView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.switch_on,0);
                rootGxView.setVisibility(View.VISIBLE);
            }
            typeView.setText(carData.car_style);
            timeView.setText(carData.license_plate_time);
            distanceView.setText(carData.mileage);
            colorView.setText(carData.color);
            bsxView.setText(carData.gearbox);
            rylxView.setText(carData.fuel_type);
            pfbzView.setText(carData.emission_standard);
            plView.setText(carData.displacement);
            njdqView.setText(carData.annual_inspection_expiry_time);
            bxdqView.setText(carData.insurance_expiry_time);
            kwView.setText(carData.position_number);
            lsbjView.setText(carData.retail_offer);
            pfbjView.setText(carData.wholesale_offer);
            clmsView.setText(carData.describe);
            gxclmsView.setText(carData.share_describe);
            gxdjView.setText(carData.retail_bottom_price);

            if (carData.is_share == 1){
                is_share = 1;
                gxcyView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.switch_on,0);
                rootGxView.setVisibility(View.VISIBLE);
            }

            images.addAll(carData.old_images);
            gximages.addAll(carData.old_share_images);

            if (carData.images.size() > 0){
                mPickRecyclerView.bind((ArrayList<String>) carData.images);
            }
            if (carData.share_images.size() > 0){
                mgxPickRecyclerView.bind((ArrayList<String>) carData.share_images);
            }
        }
    }

    int number;
    public void uploadPic(String name,String time,String price,int i){
        //CustomProgress.show(this, "车源发布中...", true, null);

        number++;

        if (number > tempImgPath.size()){
            if (gxtempImgPath.size() > 0){
                uploadgxPic(name,time,price,0);
            }else{
                sendCarSource(name,time,price);
                return;
            }

        }

        String logo_qz = "img/" + Tools.getNYR() + "/" ;
        oss.uploadImage(logo_qz,tempImgPath.get(i), new Oss.IOnFinishListenner() {
            @Override
            public void onSuccess(String imageName) {

                images.add(imageName);

                if (number <= tempImgPath.size()) {
                    uploadPic(name,time,price,number);
                }
                //CustomProgress.dismis();

                //sendCarSource(name,time,price);

                LogManager.e("xxx-images" + images.toString());
            }

            @Override
            public void onFailure() {
                LogManager.e("xxx-failure");
                CustomProgress.dismis();
            }
        });

    }

    int gxnumber;
    public void uploadgxPic(String name,String time,String price,int i){
        //CustomProgress.show(this, "车源发布中...", true, null);

        gxnumber++;

        if (gxnumber > gxtempImgPath.size()){
            sendCarSource(name,time,price);
            return;
        }

        String logo_qz = "img/" + Tools.getNYR() + "/" ;
        oss.uploadImage(logo_qz,gxtempImgPath.get(i), new Oss.IOnFinishListenner() {
            @Override
            public void onSuccess(String imageName) {

                gximages.add(imageName);

                if (gxnumber <= gxtempImgPath.size()) {
                    uploadgxPic(name,time,price,gxnumber);
                }
                //CustomProgress.dismis();

                //sendCarSource(name,time,price);

                LogManager.e("xxx-images" + gximages.toString());
            }

            @Override
            public void onFailure() {
                LogManager.e("xxx-failure");
                CustomProgress.dismis();
            }
        });

    }

    public void getCarInfo(){
        HttpData.getInstance().getCarInfo(id,App.get(Constant.KEY_USER_TOKEN),
                new Observer<HttpResult<PublicCarData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogManager.e(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult<PublicCarData> result) {
                        if (result.status == 200) {
                            carData = result.data;
                            setCarInfo();
                        }

                    }
                });
    }

    public void sendCarSource(String name,String time,String price){
        String cover = "";
        if (images.size() > 0){
            cover = images.get(0);
        }
        String gxcover = "";
        if (gximages.size() > 0){
            gxcover = gximages.get(0);
        }
        Double pfbj = 0.0;
        if (StringUtil.isNotNull(pfbjView.getText().toString()))
            pfbj = Double.parseDouble(pfbjView.getText().toString());

        Double lc = 0.0;
        if (StringUtil.isNotNull(distanceView.getText().toString()))
            lc = Double.parseDouble(distanceView.getText().toString());

        Double gxdj = 0.0;
        if (StringUtil.isNotNull(gxdjView.getText().toString()))
            gxdj = Double.parseDouble(gxdjView.getText().toString());

        //CustomProgress.show(this, "发布中...", false, null);

        HttpData.getInstance().publishCar(App.get(Constant.KEY_USER_TOKEN),is_share,is_new,name,kwView.getText().toString(),cover,images,
                vinView.getText().toString(),brand_id,series_id,style_id,time,lc,colorView.getText().toString(),
                bsxView.getText().toString(),rylxView.getText().toString(),pfbzView.getText().toString(),plView.getText().toString(),typeView.getText().toString(),njdqView.getText().toString(),
                bxdqView.getText().toString(),Double.parseDouble(price),pfbj,gxdj,clmsView.getText().toString(),gxcover,gxclmsView.getText().toString(),gximages,
                new Observer<HttpCodeResult>() {
                    @Override
                    public void onCompleted() {
//                App.showToast("999");
                        CustomProgress.dismis();
                    }

                    @Override
                    public void onError(Throwable e) {
                        CustomProgress.dismis();
                        App.showToast("服务器请求超时");
                        LogManager.e(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpCodeResult result) {
                        if (result.status == 200) {
                            finish();
                        }
                        App.showToast(result.message);
                    }
                });
    }

    public void editCarSource(String name,String time,String price){
        String cover = "";
        if (images.size() > 0){
            cover = images.get(0);
        }
        Double pfbj = 0.0;
        if (StringUtil.isNotNull(pfbjView.getText().toString()))
            pfbj = Double.parseDouble(pfbjView.getText().toString());

        Double lc = 0.0;
        if (StringUtil.isNotNull(distanceView.getText().toString()))
            pfbj = Double.parseDouble(distanceView.getText().toString());

        Double gxdj = 0.0;
        if (StringUtil.isNotNull(gxdjView.getText().toString()))
            gxdj = Double.parseDouble(gxdjView.getText().toString());

        CustomProgress.show(this, "编辑中...", false, null);

        HttpData.getInstance().editCarInfo(App.get(Constant.KEY_USER_TOKEN),id,is_share,is_new,name,kwView.getText().toString(),cover,images,
                vinView.getText().toString(),brand_id,series_id,style_id,time,lc,colorView.getText().toString(),
                bsxView.getText().toString(),rylxView.getText().toString(),pfbjView.getText().toString(),plView.getText().toString(),typeView.getText().toString(),njdqView.getText().toString(),
                bxdqView.getText().toString(),Double.parseDouble(price),pfbj,gxdj,clmsView.getText().toString(),cover,gxclmsView.getText().toString(),images,
                new Observer<HttpCodeResult>() {
                    @Override
                    public void onCompleted() {
//                App.showToast("999");
                        CustomProgress.dismis();
                    }

                    @Override
                    public void onError(Throwable e) {
                        CustomProgress.dismis();
                        App.showToast("服务器请求超时");
                        LogManager.e(e.getMessage());
                    }

                    @Override
                    public void onNext(HttpCodeResult result) {
                        if (result.status == 200) {
                            finish();
                        }
                        App.showToast(result.message);
                    }
                });
    }

    private void createDataDialog(final TextView tv) {
        String date = tv.getText().toString()+"-10";
        if (!StringUtil.isNotNull(date)) {
            date = "";
        }
        DatePickerDialog dialog = new DatePickerDialog(this, R.style.Theme_Dialog_NoTitle, date, "请选择日期", new DatePickerDialog.DatePickerListener() {
            @Override
            public void onOKClick(String year, String month, String date) {
                tv.setText(year + "-" + month);
            }
        });
        Tools.setDialog(this, dialog, Gravity.CENTER, 0, 1.0, -1);

    }

    public void showPopupDialog(int showType) {
        if (popupDialogView == null) {
            popupDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_cyfb, null);

            popupDialogView.findViewById(R.id.id_root_view).setOnClickListener(view -> mPopupDialog.dismiss());
            popupDialogView.findViewById(R.id.id_btn_cancel).setOnClickListener(view -> mPopupDialog.dismiss());

            nameView = popupDialogView.findViewById(R.id.id_tv_type);
            listView = popupDialogView.findViewById(R.id.id_list_view);

        }

        switch (showType){
            case 0:
                nameView.setText("车身颜色选择");
                listView.setAdapter(new ListViewAdapter(this,colorArr));
                break;
            case 1:
                nameView.setText("选择变速箱");
                listView.setAdapter(new ListViewAdapter(this,speedArr));
                break;
            case 2:
                nameView.setText("燃油类型");
                listView.setAdapter(new ListViewAdapter(this,rylxArr));
                break;
            case 3:
                nameView.setText("排放标准");
                listView.setAdapter(new ListViewAdapter(this,pfbzArr));
                break;
            case 4:
                nameView.setText("车辆类型");
                listView.setAdapter(new ListViewAdapter(this,cllxArr));
                break;
        }

        if (mPopupDialog == null) {
            mPopupDialog = new CommonPopupDialog(this,android.R.style.Theme_Panel);
            mPopupDialog.setCanceledOnTouchOutside(true);
            mPopupDialog.setContentView(popupDialogView);
        }

        mPopupDialog.showAtLocation(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 10){
                int pp_id = data.getIntExtra("pp_id", -1);
                int xl_id = data.getIntExtra("xl_id", -1);
                int se_id = data.getIntExtra("series_id", -1);
                String brand_name = data.getStringExtra("pp_name");
                String series_name = data.getStringExtra("series_name");
                String car_style = data.getStringExtra("car_style");

                typeView.setText(car_style);

                if (xl_id != -1 && pp_id != -1 && se_id != -1) {
                    brand_id = pp_id;
                    series_id = xl_id;
                    style_id = se_id;
                }

                String[] arr = series_name.substring(series_name.indexOf("%")+1).split("-");
                pfbzView.setText(arr[0]);
                bsxView.setText(arr[1]);
                plView.setText(arr[2]);
                //宝马M4 vv 国五-自动-3.0
                LogManager.e(brand_name.substring(0,brand_name.indexOf("%")) + " vv "+series_name.substring(series_name.indexOf("%")+1));
                cartypeView.setText(brand_name.substring(0,brand_name.indexOf("%")) +" "+series_name.substring(0,series_name.indexOf("%")));
            }else{
                CustomProgress.show(this, "图片处理中...", false, null);

                if (pickType == 0){
                    selectedList = data.getStringArrayListExtra(Pickture.PARAM_PICKRESULT);

                    //mPickRecyclerView.bind(selectedList);

                    LogManager.e("xxx-selectedList" + selectedList.toString());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tempImgPath.clear();
                                tempList.clear();

                                for (int i = 0;i< selectedList.size();i++){

                                    String path = FileUtils.compressImage(selectedList.get(i),SendCarActivity.this);
                                    if (StringUtil.isNotNull(path)){
                                        tempImgPath.add(path);
                                        tempList.add(selectedList.get(i));
                                    }else{
                                        selectedList.remove(selectedList.get(i));
                                    }
                                }

                                handler.sendEmptyMessage(1);

                            }catch (Exception e){
                                LogManager.e("----fffffff"+e.getMessage());

                            }
                        }
                    }).start();
                }else{
                    gxselectedList = data.getStringArrayListExtra(Pickture.PARAM_PICKRESULT);

                    //mPickRecyclerView.bind(selectedList);

                    LogManager.e("xxx-gxselectedList" + selectedList.toString());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                gxtempImgPath.clear();
                                gxtempList.clear();

                                for (int i = 0;i< gxselectedList.size();i++){

                                    String path = FileUtils.compressImage(gxselectedList.get(i),SendCarActivity.this);
                                    if (StringUtil.isNotNull(path)){
                                        gxtempImgPath.add(path);
                                        gxtempList.add(gxselectedList.get(i));
                                    }else{
                                        gxselectedList.remove(gxselectedList.get(i));
                                    }
                                }

                                handler.sendEmptyMessage(2);

                            }catch (Exception e){
                                LogManager.e("----fffffff"+e.getMessage());

                            }
                        }
                    }).start();
                }

            }

        }else if(resultCode == 10){
            String brand_name = data.getStringExtra("pp_name");
            cartypeView.setText(brand_name);
        }

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            CustomProgress.dismis();
            if (msg.what == 1){
                if (tempList != null && tempList.size() > 0)
                    mPickRecyclerView.bind(tempList);
            }else{
                if (gxtempList != null && gxtempList.size() > 0)
                    mgxPickRecyclerView.bind(gxtempList);
            }
        }
    };

    class ListViewAdapter extends BaseAdapter {
        private Context context;
        private String[] itemList;

        public ListViewAdapter(Context context, String[] itemList) {
            super();
            this.context = context;
            this.itemList = itemList;
        }

        @Override
        public int getCount() {
            return itemList.length;
        }

        @Override
        public Object getItem(int position) {
            return itemList[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.listitem_cyfb, null);
                holder.typeView = (TextView) convertView.findViewById(R.id.id_item_type);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.typeView.setText(itemList[position]);

            holder.typeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (selectType){
                        case 0:

                            colorView.setText(itemList[position]);
                            break;
                        case 1:

                            bsxView.setText(itemList[position]);
                            break;
                        case 2:

                            rylxView.setText(itemList[position]);
                            break;
                        case 3:

                            pfbzView.setText(itemList[position]);
                            break;
                        case 4:

                            typeView.setText(itemList[position]);
                            break;
                    }

                    mPopupDialog.dismiss();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            private TextView typeView;
        }
    }

}


