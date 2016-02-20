package com.daimajia.androidanimations;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Bruce Too
 * On 2/19/16.
 * At 23:20
 */
public class FixedGridLayout extends ViewGroup {
    int mCellWidth;
    int mCellHeight;

    public FixedGridLayout(Context context) {
        super(context);
    }

    public void setCellWidth(int px) {
        mCellWidth = px;
        requestLayout();
    }

    public void setCellHeight(int px) {
        mCellHeight = px;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //每一次addView的都会执行 onMeasure
        //子view的 绘制规则 是 AT_MOST 因此父视图无法获取到子view的绘制大小
        int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth,
                MeasureSpec.AT_MOST);
        int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight,
                MeasureSpec.AT_MOST);

        int count = getChildCount();
        for (int index = 0; index < count; index++) {
            final View child = getChildAt(index);
            //所有只能通过子view 根据自己的绘制规则 cellWidthSpec cellHeightSpec
            //调用 measure方法 在调用 view内部的 onMeasure方法去绘制view的大小
            /**
             protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
             setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
             getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
             }
             */
            child.measure(cellWidthSpec, cellHeightSpec);
        }
        // Use the size our parents gave us, but default to a minimum size to
        // avoid
        // clipping transitioning children
        int minCount = count > 3 ? count : 3;
        //该方法必须在onMeasure回调中调用 -- 绘制父视图的显示区域大小
        setMeasuredDimension(
                resolveSize(mCellWidth * minCount, widthMeasureSpec),
                resolveSize(mCellHeight * minCount, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cellWidth = mCellWidth;
        int cellHeight = mCellHeight;
        int columns = (r - l) / cellWidth;
        if (columns < 0) {
            columns = 1;
        }
        int x = 0;
        int y = 0;
        int i = 0;
        int count = getChildCount();
        for (int index = 0; index < count; index++) {
            final View child = getChildAt(index);

            int w = child.getMeasuredWidth();
            int h = child.getMeasuredHeight();

            int left = x + ((cellWidth - w) / 2);
            int top = y + ((cellHeight - h) / 2);

            child.layout(left, top, left + w, top + h);
            if (i >= (columns - 1)) {
                // advance to next row
                i = 0;
                x = 0;
                y += cellHeight;
            } else {
                i++;
                x += cellWidth;
            }
        }
    }
}