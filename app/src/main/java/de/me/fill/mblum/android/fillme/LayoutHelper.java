package de.me.fill.mblum.android.fillme;

import android.view.View;
import android.view.ViewGroup;

class LayoutHelper {
    /**
     * Set height of a view to given value
     *
     * @param layout View to change to
     * @param height Height
     */
    void setHeightOf(View layout, int height) {
        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.height = height;
        layout.setLayoutParams(layoutParams);
    }

    /**
     * Set width of a view to given value
     *
     * @param layout View to change to
     * @param width  Width
     */
    static void setWidthOf(View layout, int width) {
        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.width = width;
        layout.setLayoutParams(layoutParams);
    }
}
