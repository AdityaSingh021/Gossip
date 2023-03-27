package com.example.gossip;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class OvalShape extends ViewOutlineProvider {
    private float mRadiusX;
    private float mRadiusY;

    public OvalShape(float radiusX, float radiusY) {
        mRadiusX = radiusX;
        mRadiusY = radiusY;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        outline.setOval(0, 0, (int) (mRadiusX * 2), (int) (mRadiusY * 2));
    }

}
