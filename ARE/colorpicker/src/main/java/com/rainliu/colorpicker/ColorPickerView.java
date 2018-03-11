package com.rainliu.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import org.w3c.dom.Attr;

/**
 * Created by wliu on 2018/3/6.
 */

public class ColorPickerView extends HorizontalScrollView {

    private Context mContext;

    private ColorPickerListener mColorPickerListener;

    private AttributeSet mAttributeSet;

    private Bundle mAttributeBundle = new Bundle();

    private int mColorViewWidth = 0;

    private int mColorViewHeight = 0;

    private int mColorViewMarginLeft = 0;

    private int mColorViewMarginRight = 0;

    private int[] mColors = null;

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        this.mContext = context;
        this.mAttributeSet = attributeSet;

        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.ColorPickerView);
        mColorViewWidth = ta.getDimensionPixelSize(R.styleable.ColorPickerView_colorViewWidth, 40);
        mColorViewHeight = ta.getDimensionPixelSize(R.styleable.ColorPickerView_colorViewHeight, 40);
        mColorViewMarginLeft = ta.getDimensionPixelSize(R.styleable.ColorPickerView_colorViewMarginLeft, 5);
        mColorViewMarginRight = ta.getDimensionPixelSize(R.styleable.ColorPickerView_colorViewMarginRight, 5);
        int colorsId = ta.getResourceId(R.styleable.ColorPickerView_colors, R.array.colors);
        mColors = ta.getResources().getIntArray(colorsId);
        ta.recycle();

        mAttributeBundle.putInt(ColorView.ATTR_VIEW_WIDTH, mColorViewWidth);
        mAttributeBundle.putInt(ColorView.ATTR_VIEW_HEIGHT, mColorViewWidth);
        mAttributeBundle.putInt(ColorView.ATTR_MARGIN_LEFT, mColorViewMarginLeft);
        mAttributeBundle.putInt(ColorView.ATTR_MARGIN_RIGHT, mColorViewMarginRight);

        initView();
    }

    private void initView() {
        final LinearLayout colorsContainer = new LinearLayout(mContext);
        LinearLayout.LayoutParams containerLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        colorsContainer.setLayoutParams(containerLayoutParams);

        for (final int color : mColors) {
            final ColorView colorView = new ColorView(mContext, color, mAttributeBundle);
            colorsContainer.addView(colorView);

            colorView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isCheckedNow = colorView.getChecked();
                    if (isCheckedNow) {
                        return;
                    }

                    int childCount = colorsContainer.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View childView = colorsContainer.getChildAt(i);
                        if (childView instanceof ColorView) {
                            boolean isThisColorChecked = ((ColorView) childView).getChecked();
                            if (isThisColorChecked) {
                                ((ColorView) childView).setChecked(false);
                            }
                        }
                    }

                    colorView.setChecked(true);
                    if (mColorPickerListener != null) {
                        mColorPickerListener.onPickColor(colorView.getColor());
                    }
                }
            });
        }

        this.addView(colorsContainer);
    }

    public void setColorPickerListener(ColorPickerListener listener) {
        this.mColorPickerListener = listener;
    }
}
