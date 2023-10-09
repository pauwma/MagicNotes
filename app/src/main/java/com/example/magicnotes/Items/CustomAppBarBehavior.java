package com.example.magicnotes.Items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

public class CustomAppBarBehavior extends AppBarLayout.Behavior {

    public CustomAppBarBehavior() {}

    public CustomAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        if (velocityY < 0 && !child.isActivated()) {
            child.setActivated(true);
            return true;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }
}
