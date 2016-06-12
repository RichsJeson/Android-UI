package com.android.richsjeson.ui.tablayout.ripple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.richsjeson.ui.tablayout.ripple.animation.ValueAnimatorCompat;


/**
 * @author richsjeson
 */
public class SlidingTabStrip extends LinearLayout {

    private int mSelectedIndicatorHeight;
    private final Paint mSelectedIndicatorPaint;

    private int mSelectedPosition = -1;
    private float mSelectionOffset;

    private int mIndicatorLeft = -1;
    private int mIndicatorRight = -1;

    private ValueAnimatorCompat mCurrentAnimator;

    //添加的，用于记录高度偏移
    private float mGussionHeightOffset;
    private int mCurrentPosition;

    private RippleTabLayout mParent;

    public void setGussionHeightOffset(int position, float heightOffset){
        mCurrentPosition = position;
        mGussionHeightOffset = heightOffset;
    }

    SlidingTabStrip(Context context,RippleTabLayout rippleTabLayout) {
        super(context);
        mParent=rippleTabLayout;
        setWillNotDraw(false);
        mSelectedIndicatorPaint = new Paint();
        mSelectedIndicatorPaint.setStrokeWidth(3);
        mSelectedIndicatorPaint.setAntiAlias(true);
        mSelectedIndicatorPaint.setStyle(Paint.Style.STROKE);
    }

    void setSelectedIndicatorColor(int color) {
        if (mSelectedIndicatorPaint.getColor() != color) {
            mSelectedIndicatorPaint.setColor(color);
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void setSelectedIndicatorHeight(int height) {
        if (mSelectedIndicatorHeight != height) {
            mSelectedIndicatorHeight = height;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    boolean childrenNeedLayout() {
        for (int i = 0, z = getChildCount(); i < z; i++) {
            final View child = getChildAt(i);
            if (child.getWidth() <= 0) {
                return true;
            }
        }
        return false;
    }

    void setIndicatorPositionFromTabPosition(int position, float positionOffset) {
        //Log.i("SlidingTabStrip", "positionOffset = " + positionOffset);
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        mGussionHeightOffset = positionOffset;
        updateIndicatorPosition();
    }

    float getIndicatorPosition() {
        return mSelectedPosition + mSelectionOffset;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
            // HorizontalScrollView will first measure use with UNSPECIFIED, and then with
            // EXACTLY. Ignore the first call since anything we do will be overwritten anyway
            return;
        }

        if (mParent.mMode == RippleTabLayout.MODE_FIXED && mParent.mTabGravity == RippleTabLayout.GRAVITY_CENTER) {
            final int count = getChildCount();

            // First we'll find the widest tab
            int largestTabWidth = 0;
            for (int i = 0, z = count; i < z; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == VISIBLE) {
                    largestTabWidth = Math.max(largestTabWidth, child.getMeasuredWidth());
                }
            }

            if (largestTabWidth <= 0) {
                // If we don't have a largest child yet, skip until the next measure pass
                return;
            }

            final int gutter = mParent.dpToPx(RippleTabLayout.FIXED_WRAP_GUTTER_MIN);
            boolean remeasure = false;

            if (largestTabWidth * count <= getMeasuredWidth() - gutter * 2) {
                // If the tabs fit within our width minus gutters, we will set all tabs to have
                // the same width
                for (int i = 0; i < count; i++) {
                    final LayoutParams lp =
                            (LayoutParams) getChildAt(i).getLayoutParams();
                    if (lp.width != largestTabWidth || lp.weight != 0) {
                        lp.width = largestTabWidth;
                        lp.weight = 0;
                        remeasure = true;
                    }
                }
            } else {
                // If the tabs will wrap to be larger than the width minus gutters, we need
                // to switch to GRAVITY_FILL
                mParent.mTabGravity = RippleTabLayout.GRAVITY_FILL;
                mParent.updateTabViews(false);
                remeasure = true;
            }

            if (remeasure) {
                // Now re-measure after our changes
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (mCurrentAnimator != null && mCurrentAnimator.isRunning()) {
            // If we're currently running an animation, lets cancel it and start a
            // new animation with the remaining duration
            mCurrentAnimator.cancel();
            final long duration = mCurrentAnimator.getDuration();
            animateIndicatorToPosition(mSelectedPosition,
                    Math.round((1f - mCurrentAnimator.getAnimatedFraction()) * duration));
        } else {
            // If we've been layed out, update the indicator position
            updateIndicatorPosition();
        }
    }

    private void updateIndicatorPosition() {
        final View selectedTitle = getChildAt(mSelectedPosition);
        int left, right;

        if (selectedTitle != null && selectedTitle.getWidth() > 0) {
            left = selectedTitle.getLeft();
            right = selectedTitle.getRight();

            if (mSelectionOffset > 0f && mSelectedPosition < getChildCount() - 1) {
                // Draw the selection partway between the tabs
                View nextTitle = getChildAt(mSelectedPosition + 1);
                left = (int) (mSelectionOffset * nextTitle.getLeft() +
                        (1.0f - mSelectionOffset) * left);
                right = (int) (mSelectionOffset * nextTitle.getRight() +
                        (1.0f - mSelectionOffset) * right);
            }
        } else {
            left = right = -1;
        }

        setIndicatorPosition(left, right);
    }

    private void setIndicatorPosition(int left, int right) {
        if (left != mIndicatorLeft || right != mIndicatorRight) {
            // If the indicator's left/right has changed, invalidate
            mIndicatorLeft = left;
            mIndicatorRight = right;
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    void animateIndicatorToPosition(final int position, int duration) {
        final boolean isRtl = ViewCompat.getLayoutDirection(this)
                == ViewCompat.LAYOUT_DIRECTION_RTL;

        final View targetView = getChildAt(position);
        final int targetLeft = targetView.getLeft();
        final int targetRight = targetView.getRight();
        final int startLeft;
        final int startRight;

        if (Math.abs(position - mSelectedPosition) <= 1) {
            // If the views are adjacent, we'll animate from edge-to-edge
            startLeft = mIndicatorLeft;
            startRight = mIndicatorRight;
        } else {
            // Else, we'll just grow from the nearest edge
            final int offset = mParent.dpToPx(RippleTabLayout.MOTION_NON_ADJACENT_OFFSET);
            if (position < mSelectedPosition) {
                // We're going end-to-start
                if (isRtl) {
                    startLeft = startRight = targetLeft - offset;
                } else {
                    startLeft = startRight = targetRight + offset;
                }
            } else {
                // We're going start-to-end
                if (isRtl) {
                    startLeft = startRight = targetRight + offset;
                } else {
                    startLeft = startRight = targetLeft - offset;
                }
            }
        }

        if (startLeft != targetLeft || startRight != targetRight) {
            ValueAnimatorCompat animator = mParent.mIndicatorAnimator = ViewUtils.createAnimator();
            animator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            animator.setDuration(duration);
            animator.setFloatValues(0, 1);
            animator.setUpdateListener(new ValueAnimatorCompat.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimatorCompat animator) {
                    final float fraction = animator.getAnimatedFraction();
                    setIndicatorPosition(
                            AnimationUtils.lerp(startLeft, targetLeft, fraction),
                            AnimationUtils.lerp(startRight, targetRight, fraction));
                }
            });
            animator.setListener(new ValueAnimatorCompat.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(ValueAnimatorCompat animator) {
                    mSelectedPosition = position;
                    mSelectionOffset = 0f;
                    mGussionHeightOffset = 1;
                }

                @Override
                public void onAnimationCancel(ValueAnimatorCompat animator) {
                    mSelectedPosition = position;
                    mSelectionOffset = 0f;
                    mGussionHeightOffset = 1;
                }
            });
            animator.start();
            mCurrentAnimator = animator;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("onDraw", "mGussionHeightOffset start= " + mGussionHeightOffset);
        if (mIndicatorLeft >= 0 && mIndicatorRight > mIndicatorLeft) {
            Path path = new Path();
            path.moveTo(0, getHeight());
            for (int i = 0; i < getWidth(); i++) {
                double y1 = getGussion(i, dpToPx(15) * Math.abs(mGussionHeightOffset - 0.5), (mIndicatorRight + mIndicatorLeft) / 2);
                double y2 = getGussion(i + 1, dpToPx(15) * Math.abs(mGussionHeightOffset - 0.5), (mIndicatorRight + mIndicatorLeft) / 2);
                path.quadTo(i, (float) y1, i + 1, (float) y2);
            }
            canvas.drawPath(path, mSelectedIndicatorPaint);
            // Thick colored underline below the current selection
        }
    }

    //画高斯函数
    private double getGussion(double lineX, double GussionHight, double GussionMu){
        double result = 0;
        result = -GussionHight * ( Math.pow(Math.E, -(lineX - GussionMu) * (lineX - GussionMu) / (2 * dpToPx(8) * dpToPx(8)))) + getHeight() - 3 ;
        return result;
    }

    public int dpToPx(int dps) {
        return Math.round(getResources().getDisplayMetrics().density * dps);
    }
}
