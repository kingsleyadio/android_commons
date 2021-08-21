package com.kingsleyadio.appcommons.datetime.time;

import android.content.Context;
import android.util.AttributeSet;

import com.kingsleyadio.appcommons.datetime.WheelPicker;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * MinutePicker
 * Created by ycuwq on 2018/1/22.
 */
public class MinutePicker extends WheelPicker<Integer> {
    private OnMinuteSelectedListener mOnMinuteSelectedListener;

    public MinutePicker(Context context) {
        this(context, null);
    }

    public MinutePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MinutePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setItemMaximumWidthText("00");
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMinimumIntegerDigits(2);
        setDataFormat(numberFormat::format);
        updateMinute();
        setOnWheelChangeListener((item, position) -> {
            if (mOnMinuteSelectedListener != null) {
                mOnMinuteSelectedListener.onMinuteSelected(item);
            }
        });
    }

    private void updateMinute() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            list.add(i);
        }
        setDataList(list);
    }

    public void setSelectedMinute(int hour) {
        setSelectedMinute(hour, true);
    }

    public void setSelectedMinute(int hour, boolean smoothScroll) {
        setCurrentPosition(hour, smoothScroll);
    }

    public void setOnMinuteSelectedListener(OnMinuteSelectedListener onMinuteSelectedListener) {
        mOnMinuteSelectedListener = onMinuteSelectedListener;
    }

    public interface OnMinuteSelectedListener {
        void onMinuteSelected(int hour);
    }
}