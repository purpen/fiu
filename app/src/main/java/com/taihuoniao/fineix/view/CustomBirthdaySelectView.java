package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.TimeUtil;
import com.taihuoniao.fineix.view.wheelview.OnWheelChangedListener;
import com.taihuoniao.fineix.view.wheelview.StringWheelAdapter;
import com.taihuoniao.fineix.view.wheelview.WheelView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomBirthdaySelectView extends LinearLayout {
    private static final int DURING_YEAR = 150;
    private static final int MONTHS_OF_YEAR = 12;
    private boolean isCurrentYear = true;
    private boolean isCurrentMonth = true;
    private boolean isCyclic = false;
    private int visibleItems = 5;
    public List<String> months;
    public List<String> years;
    public List<String> days;
    private Calendar calendar;
    private WheelView wv_left;
    private WheelView wv_center;
    private WheelView wv_right;
    private TextView tv_left;
    private TextView tv_center;
    private TextView tv_right;

    public CustomBirthdaySelectView(Context context) {
        super(context);
        initData();
        initViews(context);
        installListeners();
    }

    public CustomBirthdaySelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
        initViews(context);
        installListeners();
    }

    public CustomBirthdaySelectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData();
        initViews(context);
        installListeners();
    }

    private void initViews(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_bithday_select_view, this);
        wv_left = (WheelView) view.findViewById(R.id.wv_left);
        wv_center = (WheelView) view.findViewById(R.id.wv_center);
        wv_right = (WheelView) view.findViewById(R.id.wv_right);
        tv_left = (TextView) view.findViewById(R.id.tv_center);
        tv_center = (TextView) view.findViewById(R.id.tv_left);
        tv_right = (TextView) view.findViewById(R.id.tv_right);
        wv_left.setCyclic(isCyclic);
        wv_center.setCyclic(isCyclic);
        wv_right.setCyclic(isCyclic);
        wv_left.setVisibleItems(visibleItems);
        wv_center.setVisibleItems(visibleItems);
        wv_right.setVisibleItems(visibleItems);
        if (years != null && years.size() > 0) {
            wv_left.setAdapter(new StringWheelAdapter(years));
            wv_left.setCurrentItem(DURING_YEAR - 1);
            isCurrentYear = true;
        }
        if (months != null && months.size() > 0) {
            wv_center.setAdapter(new StringWheelAdapter(months));
            LogUtil.e("CurMonth", calendar.get(Calendar.MONTH) + "");
            wv_center.setCurrentItem(months.size() - 1);
            isCurrentMonth = true;
        }

        if (days != null && days.size() > 0) {
            wv_right.setAdapter(new StringWheelAdapter(days));
            wv_right.setCurrentItem(days.size() - 1);
        }

    }

    private void initData() {
        setCalendarCurrentTime();
        initYears();
        initMonths();
        initDays();
    }

    private void setCalendarCurrentTime(){
        calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(1394380800*1000l);// 测试代码
    }

    private void initYears() {
        years = new ArrayList<String>();
        LogUtil.e("initYears", calendar.get(Calendar.YEAR) + "");
        int curYear = calendar.get(Calendar.YEAR);
        for (int i = curYear - DURING_YEAR + 1; i <= curYear; i++) {
            years.add(String.valueOf(i));
        }
        LogUtil.e("size", years.size() + "");
    }

    private void initMonths() {
        months = new ArrayList<String>();
        int monthLen = 0;
        if (isCurrentYear) {
            monthLen = calendar.get(Calendar.MONTH) + 1;
            if (monthLen==1){
                isCurrentMonth=true;
            }
        } else {
            monthLen = MONTHS_OF_YEAR;
        }
        LogUtil.e("monthLen", monthLen + "");
        for (int i = 1; i <= monthLen; i++) {
            if (i<10){
                months.add(String.format("0%s", String.valueOf(i)));
            }else {
                months.add(String.valueOf(i));
            }
        }
    }

    private void initDays() {
        days = new ArrayList<String>();
        int dayLen = 0;
        if (isCurrentMonth) {
            dayLen = calendar.get(Calendar.DAY_OF_MONTH);
            LogUtil.e("isCurrentMonthdayLen", dayLen + "");
        } else {
            dayLen = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            LogUtil.e("dayLen", dayLen + "");
        }

        for (int i = 1; i <= dayLen; i++) {
            if (i<10){
                days.add(String.format("0%s", String.valueOf(i)));
            }else {
                days.add(String.valueOf(i));
            }
        }
    }

    private void installListeners() {
        wv_left.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (newValue == years.size() - 1) {//年的最后一项
                    isCurrentYear = true;
                    setCalendarCurrentTime(); //设置为当前日期
                    initMonths();
                    initDays();
                } else {
                    isCurrentYear = false;
                    isCurrentMonth=false;
                    setCalendarCurrentSelected();
                    initMonths();
                    initDays();
                }
                wv_center.setAdapter(new StringWheelAdapter(months));
                wv_center.setCurrentItem(0);
                wv_right.setAdapter(new StringWheelAdapter(days));
                wv_right.setCurrentItem(0);
            }
        });
        wv_center.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (isCurrentYear&&newValue == months.size() - 1) {//是今年的当前月
                    isCurrentMonth = true;
                    setCalendarCurrentTime();
                } else {
                    isCurrentMonth = false;
                    setCalendarCurrentSelected();
                }
                initDays();
                wv_right.setAdapter(new StringWheelAdapter(days));
                wv_right.setCurrentItem(0);
            }
        });
    }

    /**
     * 设置为当前选择日期
     */
    private void setCalendarCurrentSelected(){
        try {
            LogUtil.e("getBithday", TimeUtil.getMillonsecond(getBithday(),"yyyy-MM-dd")+"");
            calendar.setTimeInMillis(TimeUtil.getMillonsecond(getBithday(),"yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getBithday() {
        if (years == null || years.size() == 0 || months == null || months.size() == 0 || days == null || days.size() == 0) {
            return "";
        }
        String year = years.get(wv_left.getCurrentItem());
        String month = months.get(wv_center.getCurrentItem());
        String day = days.get(wv_right.getCurrentItem());
        return String.format("%s-%s-%s", year, month, day);
    }
}
