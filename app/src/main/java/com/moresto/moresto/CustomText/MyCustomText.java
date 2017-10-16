package com.moresto.moresto.CustomText;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Gangsterz on 10/15/2017.
 */

public class MyCustomText extends TextView {
    public MyCustomText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public MyCustomText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyCustomText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
            setTypeface(tf);
        }
    }
}
