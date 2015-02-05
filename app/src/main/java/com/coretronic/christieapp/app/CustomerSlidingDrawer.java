package com.coretronic.christieapp.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.SlidingDrawer;

/**
 * Created by Morris on 15/2/4.
 */
public class CustomerSlidingDrawer extends SlidingDrawer {
    public CustomerSlidingDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
