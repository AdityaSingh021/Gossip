package com.example.gossip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

@SuppressLint("AppCompatCustomView")
public class EarthView extends ImageView implements Animation.AnimationListener,SensorEventListener {
    private Animation mRollAnimation;
    private Animation mJumpAnimation;
    private float mRollAngle;
    private float mJumpHeight;
    private boolean mIsJumping;
    private static final int JUMP_DURATION = 300;
    private boolean isJumping = false;
    public EarthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Define the roll animation
//        mRollAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        mRollAnimation.setDuration(1000);
//        mRollAnimation.setRepeatCount(Animation.INFINITE);
//        mRollAnimation.setInterpolator(new LinearInterpolator());
//        mRollAnimation.setAnimationListener(this);

        // Define the jump animation
        mJumpAnimation = new TranslateAnimation(0, 0, 0, -50);
        mJumpAnimation.setDuration(500);
        mJumpAnimation.setInterpolator(new DecelerateInterpolator());
        mJumpAnimation.setAnimationListener(this);
    }
//    public void startRollAnimation() {
//        // Create a RotateAnimation to rotate the view around its center
//        RotateAnimation rotate = new RotateAnimation(0, 360,
//                Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        rotate.setDuration(15000);
//        rotate.setInterpolator(new LinearInterpolator());
//        rotate.setRepeatCount(Animation.INFINITE);
//
//        // Start the animation
//        startAnimation(rotate);
//    }
    public void startJumpAnimation() {
        // Ignore jump events while the view is already jumping
        if (isJumping) {
            return;
        }
        isJumping = true;

        // Create a TranslateAnimation to move the view up and down
        TranslateAnimation translate = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, -0.1f);
        translate.setDuration(JUMP_DURATION / 2);
        translate.setInterpolator(new DecelerateInterpolator());
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // Create a second TranslateAnimation to move the view back down
                TranslateAnimation translate = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, -0.2f,
                        Animation.RELATIVE_TO_SELF, 0.1f);
                translate.setDuration(300);
                translate.setInterpolator(new DecelerateInterpolator());
                translate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Reset the jumping flag
                        isJumping = false;

                        // Restart the rotating animation
//                        startRollAnimation();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                // Start the second animation
                startAnimation(translate);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        // Start the first animation
        startAnimation(translate);
    }
    @Override
    public void onAnimationStart(Animation animation) {
        // No-op
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == mJumpAnimation) {
            mIsJumping = false;
            mJumpHeight = 5;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // No-op
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Apply the roll animation
        canvas.rotate(mRollAngle, getWidth() / 2, getHeight() / 2);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && !mIsJumping) {
            // Start the jump animation
            mIsJumping = true;
            startJumpAnimation();
//            mJumpAnimation.reset();
//            startAnimation(mJumpAnimation);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Calculate the roll angle based on the device's orientation
            mRollAngle = -event.values[0] * 10;
//            startRollAnimation();
            invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

