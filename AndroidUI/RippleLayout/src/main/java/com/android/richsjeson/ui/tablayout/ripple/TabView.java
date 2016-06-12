package com.android.richsjeson.ui.tablayout.ripple;/**
 * Created by Administrator on 2016/6/3.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.internal.widget.TintManager;
import android.text.Layout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.richsjeson.ui.tablayout.ripple.bean.Tab;

/**
 * @author richsjeson
 * @date 2016/6/3 11:22
 */
public class TabView extends LinearLayout implements View.OnLongClickListener {

    private final Tab mTab;
    private TextView mTextView;
    private ImageView mIconView;

    private View mCustomView;
    private TextView mCustomTextView;
    private ImageView mCustomIconView;

    private int mDefaultMaxLines = 2;
    private RippleTabLayout mParent;

    public TabView(Context context, Tab tab, RippleTabLayout rippleTabLayout) {
        super(context);
        mTab = tab;
        mParent=rippleTabLayout;
        if (mParent.mTabBackgroundResId != 0) {
            setBackgroundDrawable(TintManager.getDrawable(context, mParent.mTabBackgroundResId));

        }
        ViewCompat.setPaddingRelative(this, mParent.mTabPaddingStart, mParent.mTabPaddingTop,
                mParent.mTabPaddingEnd, mParent.mTabPaddingBottom);
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        update();
    }

    @Override
    public void setSelected(boolean selected) {
        final boolean changed = (isSelected() != selected);
        super.setSelected(selected);
        if (changed && selected) {
            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);

            if (mTextView != null) {
                mTextView.setSelected(selected);
            }
            if (mIconView != null) {
                mIconView.setSelected(selected);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        // This view masquerades as an action bar tab.
        event.setClassName(ActionBar.Tab.class.getName());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        // This view masquerades as an action bar tab.
        info.setClassName(ActionBar.Tab.class.getName());
    }

    @Override
    public void onMeasure(final int origWidthMeasureSpec, final int origHeightMeasureSpec) {
        final int specWidthSize = MeasureSpec.getSize(origWidthMeasureSpec);
        final int specWidthMode = MeasureSpec.getMode(origWidthMeasureSpec);
        final int maxWidth = mParent.getTabMaxWidth();

        final int widthMeasureSpec;
        final int heightMeasureSpec = origHeightMeasureSpec;

        if (maxWidth > 0 && (specWidthMode == MeasureSpec.UNSPECIFIED
                || specWidthSize > maxWidth)) {
            // If we have a max width and a given spec which is either unspecified or
            // larger than the max width, update the width spec using the same mode
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(mParent.mTabMaxWidth, specWidthMode);
        } else {
            // Else, use the original width spec
            widthMeasureSpec = origWidthMeasureSpec;
        }

        // Now lets measure
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // We need to switch the text size based on whether the text is spanning 2 lines or not
        if (mTextView != null) {
            final Resources res = getResources();
            float textSize = mParent.mTabTextSize;
            int maxLines = mDefaultMaxLines;

            if (mIconView != null && mIconView.getVisibility() == VISIBLE) {
                // If the icon view is being displayed, we limit the text to 1 line
                maxLines = 1;
            } else if (mTextView != null && mTextView.getLineCount() > 1) {
                // Otherwise when we have text which wraps we reduce the text size
                textSize = mParent.mTabTextMultiLineSize;
            }

            final float curTextSize = mTextView.getTextSize();
            final int curLineCount = mTextView.getLineCount();
            final int curMaxLines = mTextView.getMaxLines();

            if (textSize != curTextSize || (curMaxLines >= 0 && maxLines != curMaxLines)) {
                // We've got a new text size and/or max lines...
                boolean updateTextView = true;

                if (mParent.mMode == RippleTabLayout.MODE_FIXED && textSize > curTextSize && curLineCount == 1) {
                    // If we're in fixed mode, going up in text size and currently have 1 line
                    // then it's very easy to get into an infinite recursion.
                    // To combat that we check to see if the change in text size
                    // will cause a line count change. If so, abort the size change.
                    final Layout layout = mTextView.getLayout();
                    if (layout == null
                            || approximateLineWidth(layout, 0, textSize) > layout.getWidth()) {
                        updateTextView = false;
                    }
                }

                if (updateTextView) {
                    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    mTextView.setMaxLines(maxLines);
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            }
        }
    }

    final void update() {
        final Tab tab = mTab;
        final View custom = tab.getCustomView();
        if (custom != null) {
            final ViewParent customParent = custom.getParent();
            if (customParent != this) {
                if (customParent != null) {
                    ((ViewGroup) customParent).removeView(custom);
                }
                addView(custom);
            }
            mCustomView = custom;
            if (mTextView != null) {
                mTextView.setVisibility(GONE);
            }
            if (mIconView != null) {
                mIconView.setVisibility(GONE);
                mIconView.setImageDrawable(null);
            }

            mCustomTextView = (TextView) custom.findViewById(android.R.id.text1);
            if (mCustomTextView != null) {
                mDefaultMaxLines = mTextView.getMaxLines();
            }
            mCustomIconView = (ImageView) custom.findViewById(android.R.id.icon);
        } else {
            // We do not have a custom view. Remove one if it already exists
            if (mCustomView != null) {
                removeView(mCustomView);
                mCustomView = null;
            }
            mCustomTextView = null;
            mCustomIconView = null;
        }

        if (mCustomView == null) {
            // If there isn't a custom view, we'll us our own in-built layouts
            if (mIconView == null) {
                ImageView iconView = (ImageView) LayoutInflater.from(getContext())
                        .inflate(R.layout.ripple_tab_icon, this, false);
                addView(iconView, 0);
                mIconView = iconView;
            }
            if (mTextView == null) {
                TextView textView = (TextView) LayoutInflater.from(getContext())
                        .inflate(R.layout.ripple_tab_text, this, false);
                addView(textView);
                mTextView = textView;
                mDefaultMaxLines = mTextView.getMaxLines();
            }
            mTextView.setTextAppearance(getContext(), mParent.mTabTextAppearance);
            if (mParent.mTabTextColors != null) {
                mTextView.setTextColor(mParent.mTabTextColors);
            }
            updateTextAndIcon(tab, mTextView, mIconView);
        } else {
            // Else, we'll see if there is a TextView or ImageView present and update them
            if (mCustomTextView != null || mCustomIconView != null) {
                updateTextAndIcon(tab, mCustomTextView, mCustomIconView);
            }
        }
    }

    private void updateTextAndIcon(Tab tab, TextView textView, ImageView iconView) {
        final Drawable icon = tab.getIcon();
        final CharSequence text = tab.getText();

        if (iconView != null) {
            if (icon != null) {
                iconView.setImageDrawable(icon);
                iconView.setVisibility(VISIBLE);
                setVisibility(VISIBLE);
            } else {
                iconView.setVisibility(GONE);
                iconView.setImageDrawable(null);
            }
            iconView.setContentDescription(tab.getContentDescription());
        }

        final boolean hasText = !TextUtils.isEmpty(text);
        if (textView != null) {
            if (hasText) {
                textView.setText(text);
                textView.setContentDescription(tab.getContentDescription());
                textView.setVisibility(VISIBLE);
                setVisibility(VISIBLE);
            } else {
                textView.setVisibility(GONE);
                textView.setText(null);
            }
        }

        if (iconView != null) {
            MarginLayoutParams lp = ((MarginLayoutParams) iconView.getLayoutParams());
            int bottomMargin = 0;
            if (hasText && iconView.getVisibility() == VISIBLE) {
                // If we're showing both text and icon, add some margin bottom to the icon
                bottomMargin = mParent.dpToPx(RippleTabLayout.DEFAULT_GAP_TEXT_ICON);
            }
            if (bottomMargin != lp.bottomMargin) {
                lp.bottomMargin = bottomMargin;
                iconView.requestLayout();
            }
        }

        if (!hasText && !TextUtils.isEmpty(tab.getContentDescription())) {
            setOnLongClickListener(this);
        } else {
            setOnLongClickListener(null);
            setLongClickable(false);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        final int[] screenPos = new int[2];
        getLocationOnScreen(screenPos);

        final Context context = getContext();
        final int width = getWidth();
        final int height = getHeight();
        final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        Toast cheatSheet = Toast.makeText(context, mTab.getContentDescription(),
                Toast.LENGTH_SHORT);
        // Show under the tab
        cheatSheet.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                (screenPos[0] + width / 2) - screenWidth / 2, height);

        cheatSheet.show();
        return true;
    }

    public Tab getTab() {
        return mTab;
    }

    /**
     * Approximates a given lines width with the new provided text size.
     */
    private float approximateLineWidth(Layout layout, int line, float textSize) {
        return layout.getLineWidth(line) * (textSize / layout.getPaint().getTextSize());
    }
}
