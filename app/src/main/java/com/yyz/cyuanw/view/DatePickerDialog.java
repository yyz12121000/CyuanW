package com.yyz.cyuanw.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.yyz.cyuanw.R;
import com.yyz.cyuanw.view.weel.NumericWheelAdapter;
import com.yyz.cyuanw.view.weel.OnWheelScrollListener;
import com.yyz.cyuanw.view.weel.WheelView;

import java.util.Calendar;

/**
 * @author pythoner
 */
public class DatePickerDialog extends Dialog {

    private Button btn_left, btn_right;

    private WheelView year, month, day;
    private DatePickerListener listener;
    private String date;//初始化显示的日期，默认为当日
    private TextView noticeTv;
    private String title;

    public DatePickerDialog(Context context, String date, String title) {
        super(context);
        this.title = title;
        this.date = date;
        init();
    }

    public DatePickerDialog(Context context, int theme, String date, String title) {
        super(context, theme);
        this.date = date;
        this.title = title;
        init();
    }

    public DatePickerDialog(Context context, int theme, String date, String title, DatePickerListener listener) {
        super(context, theme);
        this.date = date;
        this.title = title;
        this.listener = listener;
        init();
    }

    private void init() {
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_date_choice);
        initViews();
        initValues();
    }

    private void initViews() {
        noticeTv = (TextView) findViewById(R.id.noticeTv);
        noticeTv.setText(title);
        btn_left = (Button) findViewById(R.id.user_detail_birthday_dialog_cancel_btn);
        btn_left.setOnClickListener(clickListener);
        btn_right = (Button) findViewById(R.id.user_detail_birthday_dialog_true_btn);
        btn_right.setOnClickListener(clickListener);

        String icurYear, icurMonth, icurDay;
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        if (date == null || date.length() < 10) {// 格式不正确
            int curMonth = c.get(Calendar.MONTH);
            int curDay = c.get(Calendar.DAY_OF_MONTH);
            icurYear = String.valueOf(curYear);
            icurMonth = String.valueOf(curMonth + 1);
            icurDay = String.valueOf(curDay);
        } else {// 年月日
            icurYear = date.substring(0, 4);
            icurMonth = date.substring(5, 7);
            icurDay = date.substring(8, 10);
        }

        year = (WheelView) findViewById(R.id.year_wheel);
        year.setAdapter(new NumericWheelAdapter(curYear - 60, curYear));
        // year.setLabel("年");
        year.setCurrentItem(Integer.parseInt(icurYear) - Integer.parseInt(year.getAdapter().getItem(0)));

        month = (WheelView) findViewById(R.id.month_wheel);
        month.setAdapter(new NumericWheelAdapter(1, 12));// "%02d"
        // month.setLabel("月");
        month.setCyclic(true);
        month.setCurrentItem(Integer.parseInt(icurMonth) - 1);

        int daysOfMounth = getDaysOfMounth();
        day = (WheelView) findViewById(R.id.day_wheel);
        day.setAdapter(new NumericWheelAdapter(1, daysOfMounth));
        // day.setLabel("日");
        day.setCyclic(true);
        day.setCurrentItem(Integer.parseInt(icurDay) - 1);

        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
            public void onScrollingStarted(WheelView wheel) {
            }

            public void onScrollingFinished(WheelView wheel) {
                int daysOfMounth = getDaysOfMounth();
                day.setAdapter(new NumericWheelAdapter(1, daysOfMounth));
            }
        };

        year.addScrollingListener(scrollListener);
        month.addScrollingListener(scrollListener);
        // day.addScrollingListener(scrollListener);

    }

    private int getDaysOfMounth() {
        int mMonth = Integer.parseInt(month.getAdapter().getItem(month.getCurrentItem()));
        switch (mMonth) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                int mYear = Integer.parseInt(year.getAdapter().getItem(year.getCurrentItem()));
                if (mYear % 4 == 0 && mYear % 100 != 0 || mYear % 400 == 0) {
                    return 29;
                } else {
                    return 28;
                }
        }
        return -1;
    }

    private void initValues() {

    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (v.getId() == R.id.user_detail_birthday_dialog_cancel_btn) {
                dismiss();
            } else if (v.getId() == R.id.user_detail_birthday_dialog_true_btn) {
                String mYear = year.getAdapter().getItem(year.getCurrentItem());
                String mMonth = month.getAdapter().getItem(month.getCurrentItem());
                String mDay = day.getAdapter().getItem(day.getCurrentItem());
                if (mDay == null || mDay.length() == 0) {
                    mDay = "1";
                }
                if (Integer.parseInt(mMonth) < 10) {
                    mMonth = "0" + mMonth;
                }
                if (Integer.parseInt(mDay) < 10) {
                    mDay = "0" + mDay;
                }
                if (listener != null) {
                    listener.onOKClick(mYear, mMonth, mDay);
                }
                dismiss();

            }
        }
    };


    public interface DatePickerListener {
        void onOKClick(String year, String month, String date);
    }
}
