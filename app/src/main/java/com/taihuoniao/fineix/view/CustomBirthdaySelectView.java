package com.taihuoniao.fineix.view;

import android.content.Context;
import android.text.TextUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class CustomBirthdaySelectView extends LinearLayout {
    private static final int DURING_YEAR = 100;
    private static final int MONTHS_OF_YEAR = 12;
    private boolean isCurrentYear = true;
    private boolean isCurrentMonth = true;
    public List<String> months;
    public List<String> years;
    public List<String> days;
    private Calendar calendar;
    private WheelView wv_left;
    private WheelView wv_center;
    private WheelView wv_right;

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
        TextView tv_left = (TextView) view.findViewById(R.id.tv_center);
        TextView tv_center = (TextView) view.findViewById(R.id.tv_left);
        TextView tv_right = (TextView) view.findViewById(R.id.tv_right);
        boolean isCyclic = false;
        wv_left.setCyclic(isCyclic);
        wv_center.setCyclic(isCyclic);
        wv_right.setCyclic(isCyclic);
        int visibleItems = 5;
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

    private void setCalendarCurrentTime() {
        calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(1394380800*1000l);// 测试代码
    }

    private void initYears() {
        years = new ArrayList<>();
        int curYear = calendar.get(Calendar.YEAR);
        for (int i = curYear - DURING_YEAR + 1; i <= curYear; i++) {
            years.add(String.valueOf(i));
        }
        LogUtil.e("size", years.size() + "");
    }

    private void initMonths() {
        months = new ArrayList<>();
        int monthLen = 0;
        if (isCurrentYear) {
            monthLen = calendar.get(Calendar.MONTH) + 1;
            if (monthLen == 1) {
                isCurrentMonth = true;
            }
        } else {
            monthLen = MONTHS_OF_YEAR;
        }
        for (int i = 1; i <= monthLen; i++) {
            if (i < 10) {
                months.add(String.format("0%s", String.valueOf(i)));
            } else {
                months.add(String.valueOf(i));
            }
        }
    }

    private void initDays() {
        days = new ArrayList<>();
        int dayLen = 0;
        if (isCurrentMonth) {
            dayLen = calendar.get(Calendar.DAY_OF_MONTH);
        } else {
            dayLen = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        for (int i = 1; i <= dayLen; i++) {
            if (i < 10) {
                days.add(String.format("0%s", String.valueOf(i)));
            } else {
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
                    isCurrentMonth = false;
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
                if (isCurrentYear && newValue == months.size() - 1) {//是今年的当前月
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
    private void setCalendarCurrentSelected() {
        try {
            LogUtil.e("getBithday", TimeUtil.getMillonsecond(getBithday(), "yyyy-MM-dd") + "");
            calendar.setTimeInMillis(TimeUtil.getMillonsecond(getBithday(), "yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置当前选中日期
     *
     * @param date
     */
    public void setCurrentDate(String date, String pattern) throws ParseException {
        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(pattern)) return;
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        Date parse = simpleDateFormat.parse(date);
        calendar.setTime(parse);
        int curYear = calendar.get(Calendar.YEAR);
        for (int i = 0; i < years.size(); i++) {
            if (TextUtils.equals(String.valueOf(curYear), years.get(i))) {
                wv_left.setCurrentItem(i);
                break;
            }
        }

        int curMonth = calendar.get(Calendar.MONTH);
        String monthStr;
        for (int j = 0; j < months.size(); j++) {
            if (curMonth < 10) {
                monthStr = "0" + (curMonth + 1);
            } else {
                monthStr = String.valueOf(curMonth + 1);
            }
            if (TextUtils.equals(monthStr, months.get(j))) {
                wv_center.setCurrentItem(j);
                break;
            }
        }

        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr;
        for (int k = 0; k < days.size(); k++) {
            if (curDay < 10) {
                dayStr = "0" + curDay;
            } else {
                dayStr = String.valueOf(curDay);
            }
            if (TextUtils.equals(dayStr, days.get(k))) {
                wv_right.setCurrentItem(k);
                break;
            }
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
