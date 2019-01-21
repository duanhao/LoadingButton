package com.app.loadbtnlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Author：duanhao         Mail：duanhao@bmsoft.com.cn
 * CreateDate：2019/1/17 上午10:31     CreateVersion：1.0.0
 * CreateDesc：
 * UpdateDate：2019/1/17 上午10:31     CreateVersion：
 * UpdateDesc：
 */
public class LoadingButton extends FrameLayout {

    // 开始和结束Loading时的回调
    private OnLoadingListener mLoadingListener;
    // Loading动画旋转周期
    private int mRotateDuration = 1000;
    // 按钮缩成Loading动画的时间
    private int mReduceDuration = 350;
    private int width;
    private int height;
    // 是否在Loading中
    private boolean isLoading = false;
    private Context mContext;
    private TextView mLoadTxt;
    private ProgressBar mProgressBar;
    private int mCircleColor;
    private String mTxtTitle;
    private Drawable mBgDrawable;


    public LoadingButton(Context context) {
        this(context, null);
    }

    public LoadingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton);
        mCircleColor = typedArray.getColor(R.styleable.LoadingButton_circleBgColor, ContextCompat.getColor(context, R.color.loadingbtn_normal_bg_color));
        mTxtTitle = typedArray.getString(R.styleable.LoadingButton_txt);
        float txtSize = typedArray.getFloat(R.styleable.LoadingButton_txtsize, 17);
        int txtColor = typedArray.getColor(R.styleable.LoadingButton_txtcolor, ContextCompat.getColor(mContext, R.color.loadingbtn_txt_color));
        typedArray.recycle();
        mLoadTxt = new TextView(mContext);
        mLoadTxt.setText(mTxtTitle);
        mLoadTxt.setTextSize(txtSize);
        mLoadTxt.setTextColor(txtColor);
        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mLoadTxt.setLayoutParams(params);
        addView(mLoadTxt);
        init();
    }

    /**
     * 初始化 属性
     */
    private void init() {
        mBgDrawable = getBackground();
        mProgressBar = new ProgressBar(mContext);
        mProgressBar.setIndeterminateTintList(ColorStateList.valueOf(mCircleColor));
        mProgressBar.setIndeterminateTintMode(PorterDuff.Mode.SRC_ATOP);
        mProgressBar.setVisibility(INVISIBLE);
        FrameLayout.LayoutParams params = new LayoutParams(dp2px(45), dp2px(45));
        mProgressBar.setLayoutParams(params);
        addView(mProgressBar);

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (width == 0) width = getMeasuredWidth();
        if (height == 0) height = getMeasuredHeight();

    }


    /**
     * 按钮缩成Loading的动画
     */
    private void showStartLoadAnimation() {
        ValueAnimator animator = new ValueAnimator().ofInt(width, height);
        animator.setDuration(mReduceDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getLayoutParams().width = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setBackground(mBgDrawable);
                setEnabled(false);
                mLoadTxt.setText("");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showLoadingAnimation();
            }
        });
        animator.start();
    }

    private void showLoadingAnimation() {
//        RotateAnimation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setDuration(mRotateDuration);
//        animation.setInterpolator(new LinearInterpolator());
//        animation.setRepeatCount(-1);
//        startAnimation(animation);
        if (mLoadingListener != null) {
            mLoadingListener.onStart();
        }
        setBackground(null);
        mProgressBar.setVisibility(VISIBLE);
        isLoading = true;
    }

    /**
     * 播放Loading拉伸成按钮的动画
     */
    private void showFinishLoadAnimation() {
        mProgressBar.setVisibility(INVISIBLE);
        ValueAnimator animator = new ValueAnimator().ofInt(height, width);
        animator.setDuration(mReduceDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getLayoutParams().width = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                setBackground(mBgDrawable);
                setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLoadTxt.setText(mTxtTitle);
                setEnabled(true);
                if (mLoadingListener != null) {
                    mLoadingListener.onFinish();
                }
            }
        });
        animator.start();
        isLoading = false;
    }

    /**
     * dp转px
     *
     * @param dpVal
     * @return
     */
    private int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, mContext.getResources().getDisplayMetrics());
    }

    private int sp2px(float spValue) {
        float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 开始Loading
     */
    public void startLoading() {
        if (!isLoading) {
            clearAnimation();
            showStartLoadAnimation();
        }
    }

    /**
     * 开始Loading
     *
     * @param listener Loading开始时的回调
     */
    public void startLoading(OnLoadingListener listener) {
        if (!isLoading) {
            mLoadingListener = listener;
            clearAnimation();
            showStartLoadAnimation();
        }
    }

    /**
     * 结束Loading
     */
    public void finishLoading() {
        if (isLoading) {
            clearAnimation();
            showFinishLoadAnimation();
        }
    }

    /**
     * 结束Loading
     *
     * @param listener Loading结束时的回调
     */
    public void finishLoading(OnLoadingListener listener) {
        if (isLoading) {
            mLoadingListener = listener;
            clearAnimation();
            showFinishLoadAnimation();
        }
    }


    /**
     * Loading开始和结束时的回调接口
     */
    public interface OnLoadingListener {
        void onStart();

        void onFinish();
    }
}