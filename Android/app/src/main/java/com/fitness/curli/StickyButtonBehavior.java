package com.fitness.curli;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import static android.support.v4.view.ViewCompat.SCROLL_AXIS_VERTICAL;

public class StickyButtonBehavior extends CoordinatorLayout.Behavior {
    private Integer mNotStickyMargin;
    private int mAnchorId;

    public StickyButtonBehavior(int anchorId) {
        mAnchorId = anchorId;
        //mNotStickyMargin = notStickyMargin;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull
            View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return (axes == SCROLL_AXIS_VERTICAL);
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View
            child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);

        View anchor = coordinatorLayout.findViewById(mAnchorId);
        int[] anchorLocation = new int[2];
        anchor.getLocationOnScreen(anchorLocation);


        RelativeLayout stop = coordinatorLayout.findViewById(R.id.spinner_rl);
        int[] stopLocation = new int[2];
        stop.getLocationOnScreen(stopLocation);
        int coordStop = stopLocation[1];

        //vertical position, cannot scroll over the bottom of the coordinator layout
        child.setY(Math.min(anchorLocation[1], coordStop - child.getHeight() ));

        //Margins depend on the distance to the bottom
        //int diff = Math.max(coordStop - anchorLocation[1] - child.getHeight(), 0);
        //ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        //layoutParams.leftMargin = Math.min(diff, mNotStickyMargin);
        //layoutParams.rightMargin = Math.min(diff, mNotStickyMargin);
        //child.setLayoutParams(layoutParams);

    }

}