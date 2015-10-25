package cn.iwgang.countdownview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * 倒计时View
 * Created by iWgang on 15/9/16.
 * https://github.com/iwgang/CountdownView
 */
public class CountdownView extends View {
    private Context mContext;
    private int mDay, mHour, mMinute, mSecond, mMillisecond;
    private OnCountdownEndListener mOnCountdownEndListener;
    private CountDownTimer mCountDownTimer;

    private boolean isShowDay;
    private boolean isShowHour;
    private boolean isShowMinute;
    private boolean isShowMillisecond;
    private boolean isHideTimeBackground;
    private boolean isShowTimeBgDivisionLine;
    private boolean isTimeTextBold;
    private boolean isSuffixTextBold;

    private Paint mTimeTextPaint;
    private Paint mSuffixTextPaint;
    private Paint mTimeTextBgPaint;
    private Paint mTimeTextBgDivisionLinePaint;

    private RectF mDayBgRectF;
    private RectF mHourBgRectF;
    private RectF mMinuteBgRectF;
    private RectF mSecondBgRectF;
    private RectF mMillisecondBgRectF;

    private float mTimeTextWidth;
    private float mTimeTextHeight;
    private float mTimeTextSize;
    private float mTimeBgSize;
    private int mTimeTextColor;
    private int mTimeBgColor;
    private float mTimeBgRadius;
    private int mTimeBgDivisionLineColor;
    private float mTimeTextBaseY;
    private float mTimeBgDivisionLineYPos;
    private int mTimeBgDivisionLineSize;

    // 后缀
    private float mSuffixTextHeight;
    private String mSuffix;
    private String mSuffixDay;
    private String mSuffixHour;
    private String mSuffixMinute;
    private String mSuffixSecond;
    private String mSuffixMillisecond;
    private int mSuffixTextColor;
    private float mSuffixTextSize;
    private float mSuffixDayTextWidth;
    private float mSuffixHourTextWidth;
    private float mSuffixMinuteTextWidth;
    private float mSuffixSecondTextWidth;
    private float mSuffixMillisecondTextWidth;
    private int mSuffixGravity;
    private float mSuffixLRMargin;
    private float mSuffixDayLeftMargin;
    private float mSuffixDayRightMargin;
    private float mSuffixHourLeftMargin;
    private float mSuffixHourRightMargin;
    private float mSuffixMinuteLeftMargin;
    private float mSuffixMinuteRightMargin;
    private float mSuffixSecondLeftMargin;
    private float mSuffixSecondRightMargin;
    private float mSuffixMillisecondLeftMargin;

    private float mSuffixDayTextBaseline;
    private float mSuffixHourTextBaseline;
    private float mSuffixMinuteTextBaseline;
    private float mSuffixSecondTextBaseline;
    private float mSuffixMillisecondTextBaseline;

    private float mTimeTextBaseline;
    private float mLeftPaddingSize;
    private float mTopPaddingSize;
    private int mContentAllWidth;
    private int mContentAllHeight;
    private Rect mComRect = new Rect();

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountdownView);
        mTimeBgColor = ta.getColor(R.styleable.CountdownView_timeBgColor, 0xFF444444);
        mTimeBgRadius = ta.getDimension(R.styleable.CountdownView_timeBgRadius, 0);
        isShowTimeBgDivisionLine = ta.getBoolean(R.styleable.CountdownView_isShowTimeBgDivisionLine, true);
        mTimeBgDivisionLineColor = ta.getColor(R.styleable.CountdownView_timeBgDivisionLineColor, Color.parseColor("#30FFFFFF"));

        isTimeTextBold = ta.getBoolean(R.styleable.CountdownView_isTimeTextBold, false);
        mTimeTextSize = ta.getDimension(R.styleable.CountdownView_timeTextSize, sp2px(12));
        mTimeTextColor = ta.getColor(R.styleable.CountdownView_timeTextColor, 0xFFFFFFFF);
        isHideTimeBackground = ta.getBoolean(R.styleable.CountdownView_isHideTimeBackground, false);
        isShowDay = ta.getBoolean(R.styleable.CountdownView_isShowDay, false);
        isShowHour = ta.getBoolean(R.styleable.CountdownView_isShowHour, true);
        isShowMinute = ta.getBoolean(R.styleable.CountdownView_isShowMinute, true);
        isShowMillisecond = ta.getBoolean(R.styleable.CountdownView_isShowMillisecond, false);

        isSuffixTextBold = ta.getBoolean(R.styleable.CountdownView_isSuffixTextBold, false);
        mSuffixTextSize = ta.getDimension(R.styleable.CountdownView_suffixTextSize, sp2px(12));
        mSuffixTextColor = ta.getColor(R.styleable.CountdownView_suffixTextColor, 0xFF000000);
        mSuffix = ta.getString(R.styleable.CountdownView_suffix);
        mSuffixDay = ta.getString(R.styleable.CountdownView_suffixDay);
        mSuffixHour = ta.getString(R.styleable.CountdownView_suffixHour);
        mSuffixMinute = ta.getString(R.styleable.CountdownView_suffixMinute);
        mSuffixSecond = ta.getString(R.styleable.CountdownView_suffixSecond);
        mSuffixMillisecond = ta.getString(R.styleable.CountdownView_suffixMillisecond);
        mSuffixGravity = ta.getInt(R.styleable.CountdownView_suffixGravity, 1);
        mSuffixLRMargin = ta.getDimension(R.styleable.CountdownView_suffixLRMargin, -1);
        mSuffixDayLeftMargin = ta.getDimension(R.styleable.CountdownView_suffixDayLeftMargin, -1);
        mSuffixDayRightMargin = ta.getDimension(R.styleable.CountdownView_suffixDayRightMargin, -1);
        mSuffixHourLeftMargin = ta.getDimension(R.styleable.CountdownView_suffixHourLeftMargin, -1);
        mSuffixHourRightMargin = ta.getDimension(R.styleable.CountdownView_suffixHourRightMargin, -1);
        mSuffixMinuteLeftMargin = ta.getDimension(R.styleable.CountdownView_suffixMinuteLeftMargin, -1);
        mSuffixMinuteRightMargin = ta.getDimension(R.styleable.CountdownView_suffixMinuteRightMargin, -1);
        mSuffixSecondLeftMargin = ta.getDimension(R.styleable.CountdownView_suffixSecondLeftMargin, -1);
        mSuffixSecondRightMargin = ta.getDimension(R.styleable.CountdownView_suffixSecondRightMargin, -1);
        mSuffixMillisecondLeftMargin = ta.getDimension(R.styleable.CountdownView_suffixMillisecondLeftMargin, -1);
        ta.recycle();

        // 初始化画笔
        initPaint();
        // 初始化后缀
        initSuffix();
        // 初始化后缀边距
        initSuffixMargin();

        // 测量时间文字高度
        Rect rect = new Rect();
        mTimeTextPaint.getTextBounds("00", 0, 2, rect);
        mTimeTextWidth = rect.width();
        mTimeTextHeight = rect.height();

        if (!isHideTimeBackground) {
            mTimeBgSize = mTimeTextWidth + (dp2px(2) * 4);
        }

        mContentAllWidth = getAllContentWidth();
        mContentAllHeight = isHideTimeBackground ? (int) mTimeTextHeight : (int) mTimeBgSize;
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        // 初始化时间文字画笔
        mTimeTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimeTextPaint.setColor(mTimeTextColor);
        mTimeTextPaint.setTextAlign(Paint.Align.CENTER);
        mTimeTextPaint.setTextSize(mTimeTextSize);
        if (isTimeTextBold) {
            mTimeTextPaint.setFakeBoldText(true);
        }

        // 初始化时间后缀文字画笔
        mSuffixTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSuffixTextPaint.setColor(mSuffixTextColor);
        mSuffixTextPaint.setTextSize(mSuffixTextSize);
        if (isSuffixTextBold) {
            mSuffixTextPaint.setFakeBoldText(true);
        }

        // 初始化时间背景画笔
        mTimeTextBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimeTextBgPaint.setStyle(Paint.Style.FILL);
        mTimeTextBgPaint.setColor(mTimeBgColor);

        // 初始化时间背景中间的分割线画笔
        mTimeTextBgDivisionLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimeTextBgDivisionLinePaint.setColor(mTimeBgDivisionLineColor);
        mTimeBgDivisionLineSize = dp2px(0.5f);
        mTimeTextBgDivisionLinePaint.setStrokeWidth(mTimeBgDivisionLineSize);
    }

    private void initSuffix() {
        boolean isSuffixNull = true;
        float mSuffixTextWidth = 0;

        if (!TextUtils.isEmpty(mSuffix)) {
            isSuffixNull = false;
            mSuffixTextWidth = mSuffixTextPaint.measureText(mSuffix);
        }

        if (isShowDay) {
            if (!TextUtils.isEmpty(mSuffixDay)) {
                mSuffixDayTextWidth = mSuffixTextPaint.measureText(mSuffixDay);
            } else {
                if (!isSuffixNull) {
                    mSuffixDay = mSuffix;
                    mSuffixDayTextWidth = mSuffixTextWidth;
                }
            }
        }

        if (isShowHour) {
            if (!TextUtils.isEmpty(mSuffixHour)) {
                mSuffixHourTextWidth = mSuffixTextPaint.measureText(mSuffixHour);
            } else {
                if (!isSuffixNull) {
                    mSuffixHour = mSuffix;
                    mSuffixHourTextWidth = mSuffixTextWidth;
                }
            }
        }

        if (isShowMinute) {
            if (!TextUtils.isEmpty(mSuffixMinute)) {
                mSuffixMinuteTextWidth = mSuffixTextPaint.measureText(mSuffixMinute);
            } else if (!isSuffixNull) {
                mSuffixMinute = mSuffix;
                mSuffixMinuteTextWidth = mSuffixTextWidth;
            }
        }

        if (!TextUtils.isEmpty(mSuffixSecond)) {
            mSuffixSecondTextWidth = mSuffixTextPaint.measureText(mSuffixSecond);
        } else if (isShowMillisecond && !isSuffixNull) {
            mSuffixSecond = mSuffix;
            mSuffixSecondTextWidth = mSuffixTextWidth;
        }

        if (isShowMillisecond && isSuffixNull && !TextUtils.isEmpty(mSuffixMillisecond)) {
            mSuffixMillisecondTextWidth = mSuffixTextPaint.measureText(mSuffixMillisecond);
        }
    }

    private void initSuffixMargin() {
        boolean isSuffixLRMarginNull = true;

        if (mSuffixLRMargin > 0) {
            isSuffixLRMarginNull = false;
        }

        if (isShowDay) {
            if (mSuffixDayLeftMargin < 0) {
                if (!isSuffixLRMarginNull) {
                    mSuffixDayLeftMargin = mSuffixLRMargin;
                } else {
                    mSuffixDayLeftMargin = 0;
                }
            }

            if (mSuffixDayRightMargin < 0) {
                if (!isSuffixLRMarginNull) {
                    mSuffixDayRightMargin = mSuffixLRMargin;
                } else {
                    mSuffixDayRightMargin = 0;
                }
            }
        } else {
            mSuffixDayLeftMargin = 0;
            mSuffixDayRightMargin = 0;
        }

        if (isShowHour) {
            if (mSuffixHourLeftMargin < 0) {
                if (!isSuffixLRMarginNull) {
                    mSuffixHourLeftMargin = mSuffixLRMargin;
                } else {
                    mSuffixHourLeftMargin = 0;
                }
            }

            if (mSuffixHourRightMargin < 0) {
                if (!isSuffixLRMarginNull) {
                    mSuffixHourRightMargin = mSuffixLRMargin;
                } else {
                    mSuffixHourRightMargin = 0;
                }
            }
        } else {
            mSuffixHourLeftMargin = 0;
            mSuffixHourRightMargin = 0;
        }

        if (isShowMinute) {
            if (mSuffixMinuteLeftMargin < 0) {
                if (!isSuffixLRMarginNull) {
                    mSuffixMinuteLeftMargin = mSuffixLRMargin;
                } else {
                    mSuffixMinuteLeftMargin = 0;
                }
            }

            if (mSuffixMinuteRightMargin < 0) {
                if (!isSuffixLRMarginNull) {
                    mSuffixMinuteRightMargin = mSuffixLRMargin;
                } else {
                    mSuffixMinuteRightMargin = 0;
                }
            }
        } else {
            mSuffixMinuteLeftMargin = 0;
            mSuffixMinuteRightMargin = 0;
        }

        if (mSuffixSecondLeftMargin < 0) {
            if (!isSuffixLRMarginNull) {
                mSuffixSecondLeftMargin = mSuffixLRMargin;
            } else {
                mSuffixSecondLeftMargin = 0;
            }
        }
        if (mSuffixSecondRightMargin < 0) {
            if (!isSuffixLRMarginNull) {
                mSuffixSecondRightMargin = mSuffixLRMargin;
            } else {
                mSuffixSecondRightMargin = 0;
            }
        }

        if (isShowMillisecond) {
            if (mSuffixMillisecondLeftMargin < 0) {
                if (!isSuffixLRMarginNull && mSuffixMillisecondTextWidth > 0) {
                    mSuffixMillisecondLeftMargin = mSuffixLRMargin;
                } else {
                    mSuffixMillisecondLeftMargin = 0;
                }
            }
        } else {
            mSuffixMillisecondLeftMargin = 0;
        }
    }

    /**
     * 初始化时间背景的RectF对象
     */
    private void initTimeBgRect() {
        if (!isHideTimeBackground) {
            float mHourLeft;
            float mMinuteLeft;
            float mSecondLeft;

            if (isShowDay) {
                // 显示天
                // 初始化小时背景RectF
                mDayBgRectF = new RectF(mLeftPaddingSize, mTopPaddingSize, mLeftPaddingSize + mTimeBgSize, mTopPaddingSize + mTimeBgSize);
                // 计算分钟x轴
                mHourLeft = mLeftPaddingSize + mTimeBgSize + mSuffixDayTextWidth + mSuffixDayLeftMargin + mSuffixDayRightMargin;
            } else {
                mHourLeft = mLeftPaddingSize;
            }

            if (isShowHour) {
                // 显示小时
                // 初始化小时背景RectF
                mHourBgRectF = new RectF(mHourLeft, mTopPaddingSize, mHourLeft + mTimeBgSize, mTopPaddingSize + mTimeBgSize);
                // 计算分钟x轴
                mMinuteLeft = mHourLeft + mTimeBgSize + mSuffixHourTextWidth + mSuffixHourLeftMargin + mSuffixHourRightMargin;
            } else {
                mMinuteLeft = mHourLeft;
            }

            if (isShowMinute) {
                // 显示分钟
                // 初始化分钟背景RectF
                mMinuteBgRectF = new RectF(mMinuteLeft, mTopPaddingSize, mMinuteLeft + mTimeBgSize, mTopPaddingSize + mTimeBgSize);
                // 计算秒钟x轴
                mSecondLeft = mMinuteLeft + mTimeBgSize + mSuffixMinuteTextWidth + mSuffixMinuteLeftMargin + mSuffixMinuteRightMargin;
            } else {
                mSecondLeft = mMinuteLeft;
            }

            // 初始化秒钟背景RectF
            mSecondBgRectF = new RectF(mSecondLeft, mTopPaddingSize, mSecondLeft + mTimeBgSize, mTopPaddingSize + mTimeBgSize);

            if (isShowMillisecond) {
                // 计算毫秒x轴
                float mMillisecondLeft = mSecondLeft + mTimeBgSize + mSuffixSecondTextWidth + mSuffixSecondLeftMargin + mSuffixSecondRightMargin;

                // 初始化毫秒背景RectF
                mMillisecondBgRectF = new RectF(mMillisecondLeft, mTopPaddingSize, mMillisecondLeft + mTimeBgSize, mTopPaddingSize + mTimeBgSize);
            }

            Paint.FontMetrics timeFontMetrics = mTimeTextPaint.getFontMetrics();
            mTimeTextBaseY = mSecondBgRectF.top + (mSecondBgRectF.bottom - mSecondBgRectF.top - timeFontMetrics.bottom + timeFontMetrics.top) / 2 - timeFontMetrics.top;

            // 背景分割线Y坐标
            mTimeBgDivisionLineYPos = mSecondBgRectF.centerY() + mTimeBgDivisionLineSize;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(1, mContentAllWidth, widthMeasureSpec), measureSize(2, mContentAllHeight, heightMeasureSpec));

        if (getHeight() > 0 && getWidth() > 0) {
            // 初始化时间文字baseline和文字背景top padding值
            initTimeTextBaselineAndTimeBgTopPadding();
            // 初始化内容view left padding值
            initLeftPaddingSize();
            // 初始化时间背景的RectF对象
            initTimeBgRect();
        }
    }

    private void initTimeTextBaselineAndTimeBgTopPadding() {
        if (getPaddingTop() == getPaddingBottom()) {
            // 居中
            mTimeTextBaseline = getHeight() / 2 + mTimeTextHeight / 2;

            mTopPaddingSize = (getHeight() - mContentAllHeight) / 2;
        } else {
            // 计算上边距
            mTimeTextBaseline = getHeight() - (getHeight() - getPaddingTop()) + mTimeTextHeight;

            mTopPaddingSize = getPaddingTop();
        }

        if (isShowDay && mSuffixDayTextWidth > 0) {
            mSuffixDayTextBaseline = getSuffixTextBaseLine(mSuffixDay);
        }

        if (isShowHour && mSuffixHourTextWidth > 0) {
            mSuffixHourTextBaseline = getSuffixTextBaseLine(mSuffixHour);
        }

        if (isShowMinute && mSuffixMinuteTextWidth > 0) {
            mSuffixMinuteTextBaseline = getSuffixTextBaseLine(mSuffixMinute);
        }

        if (mSuffixSecondTextWidth > 0) {
            mSuffixSecondTextBaseline = getSuffixTextBaseLine(mSuffixSecond);
        }

        if (isShowMillisecond && mSuffixMillisecondTextWidth > 0) {
            mSuffixMillisecondTextBaseline = getSuffixTextBaseLine(mSuffixMillisecond);
        }
    }

    private float getSuffixTextBaseLine(String suffixText) {
        Rect tempRect = new Rect();
        mSuffixTextPaint.getTextBounds(suffixText, 0, suffixText.length(), tempRect);

        float ret;
        switch (mSuffixGravity) {
            case 0:
                // top
                if (isHideTimeBackground) {
                    ret = mTimeTextBaseline - mTimeTextHeight - tempRect.top;
                } else {
                    ret = mTopPaddingSize - tempRect.top;
                }
                break;
            default:
            case 1:
                // center
                if (isHideTimeBackground) {
                    ret = mTimeTextBaseline - mTimeTextHeight / 2 + tempRect.height() / 2;
                } else {
                    ret = mTopPaddingSize + mTimeBgSize - mTimeBgSize / 2  + tempRect.height() / 2;
                }
                break;
            case 2:
                // bottom
                if (isHideTimeBackground) {
                    ret = mTimeTextBaseline - tempRect.bottom;
                } else {
                    ret = mTopPaddingSize + mTimeBgSize - tempRect.bottom;
                }
                break;
        }

        return ret;
    }

    private void initLeftPaddingSize() {
        if (getPaddingLeft() == getPaddingRight()) {
            // center
            mLeftPaddingSize = (getWidth() - mContentAllWidth) / 2;
        } else {
            // padding left
            mLeftPaddingSize = getPaddingLeft();
        }
    }

    /**
     * measure view Size
     * @param specType    1 width 2 height
     * @param contentSize all content view size
     * @param measureSpec spec
     * @return
     */
    private int measureSize(int specType, int contentSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = Math.max(contentSize, specSize);
        } else {
            result = contentSize;

            if (specType == 1) {
                // width
                result += (getPaddingLeft() + getPaddingRight());
            } else {
                // height
                result += (getPaddingTop() + getPaddingBottom());
            }
        }

        return result;
    }

    /**
     * get all view width
     * @return
     */
    private int getAllContentWidth() {
        float timeWidth = isHideTimeBackground ? mTimeTextWidth : mTimeBgSize;
        float width = timeWidth;
        width += (mSuffixDayTextWidth + mSuffixHourTextWidth + mSuffixMinuteTextWidth + mSuffixSecondTextWidth + mSuffixMillisecondTextWidth);
        width += (mSuffixDayLeftMargin + mSuffixDayRightMargin + mSuffixHourLeftMargin + mSuffixHourRightMargin
                + mSuffixMinuteLeftMargin + mSuffixMinuteRightMargin + mSuffixSecondLeftMargin + mSuffixSecondRightMargin + mSuffixMillisecondLeftMargin);

        if (isShowDay) {
            width += timeWidth;
        }

        if (isShowHour) {
            width += timeWidth;
        }

        if (isShowMinute) {
            width += timeWidth;
        }

        if (isShowMillisecond) {
            width += timeWidth;
        }

        return (int)Math.ceil(width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float mHourLeft;
        float mMinuteLeft;
        float mSecondLeft;

        if (isHideTimeBackground) {
            // 无背景
            // 判断显示天
            if (isShowDay) {
                // 画天文字
                canvas.drawText(formatNum(mDay), mLeftPaddingSize + mTimeTextWidth / 2, mTimeTextBaseline, mTimeTextPaint);
                if (mSuffixDayTextWidth > 0) {
                    // 画天后缀
                    mSuffixTextPaint.getTextBounds(mSuffixDay, 0, mSuffixDay.length(), mComRect);
                    canvas.drawText(mSuffixDay, mLeftPaddingSize + mTimeTextWidth + mSuffixDayLeftMargin, mSuffixDayTextBaseline, mSuffixTextPaint);
                }

                // 计算小时x轴
                mHourLeft = mLeftPaddingSize + mTimeTextWidth + mSuffixDayTextWidth + mSuffixDayLeftMargin + mSuffixDayRightMargin;
            } else {
                mHourLeft = mLeftPaddingSize;
            }

            // 判断显示小时
            if (isShowHour) {
                // 画小时文字
                canvas.drawText(formatNum(mHour), mHourLeft + mTimeTextWidth / 2, mTimeTextBaseline, mTimeTextPaint);
                if (mSuffixHourTextWidth > 0) {
                    // 画小时后缀
                    canvas.drawText(mSuffixHour, mHourLeft + mTimeTextWidth + mSuffixHourLeftMargin, mSuffixHourTextBaseline, mSuffixTextPaint);
                }

                // 计算分钟x轴
                mMinuteLeft = mHourLeft + mTimeTextWidth + mSuffixHourTextWidth + mSuffixHourLeftMargin + mSuffixHourRightMargin;
            } else {
                mMinuteLeft = mHourLeft;
            }

            // 判断显示分钟
            if (isShowMinute) {
                // 画分钟文字
                canvas.drawText(formatNum(mMinute), mMinuteLeft + mTimeTextWidth / 2 , mTimeTextBaseline, mTimeTextPaint);
                if (mSuffixMinuteTextWidth > 0) {
                    // 画分钟后缀
                    canvas.drawText(mSuffixMinute, mMinuteLeft + mTimeTextWidth + mSuffixMinuteLeftMargin, mSuffixMinuteTextBaseline, mSuffixTextPaint);
                }

                // 计算秒钟x轴
                mSecondLeft = mMinuteLeft + mTimeTextWidth + mSuffixMinuteTextWidth + mSuffixMinuteLeftMargin + mSuffixMinuteRightMargin;
            } else {
                mSecondLeft = mMinuteLeft;
            }

            // 画秒钟文字
            canvas.drawText(formatNum(mSecond), mSecondLeft + mTimeTextWidth / 2, mTimeTextBaseline, mTimeTextPaint);
            if (mSuffixSecondTextWidth > 0) {
                // 画秒钟后缀
                canvas.drawText(mSuffixSecond, mSecondLeft + mTimeTextWidth + mSuffixSecondLeftMargin, mSuffixSecondTextBaseline, mSuffixTextPaint);
            }

            if (isShowMillisecond) {
                // 计算毫秒x轴
                float mMillisecondLeft = mSecondLeft + mTimeTextWidth + mSuffixSecondTextWidth + mSuffixSecondLeftMargin + mSuffixSecondRightMargin;
                // 画毫秒文字
                canvas.drawText(formatMillisecond(), mMillisecondLeft + mTimeTextWidth / 2, mTimeTextBaseline, mTimeTextPaint);
                if (mSuffixMillisecondTextWidth > 0) {
                    // 画毫秒后缀
                    canvas.drawText(mSuffixMillisecond, mMillisecondLeft + mTimeTextWidth + mSuffixMillisecondLeftMargin, mSuffixMillisecondTextBaseline, mSuffixTextPaint);
                }
            }
        } else {
            // 有背景

            // 判断显示天
            if (isShowDay) {
                // 画天背景
                canvas.drawRoundRect(mDayBgRectF, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
                if (isShowTimeBgDivisionLine) {
                    // 画天背景中间的横线
                    canvas.drawLine(mLeftPaddingSize, mTimeBgDivisionLineYPos, mLeftPaddingSize + mTimeBgSize, mTimeBgDivisionLineYPos, mTimeTextBgDivisionLinePaint);
                }
                // 画天文字
                canvas.drawText(formatNum(mDay), mDayBgRectF.centerX(), mTimeTextBaseY, mTimeTextPaint);
                if (mSuffixDayTextWidth > 0) {
                    // 画天后缀
                    canvas.drawText(mSuffixDay, mLeftPaddingSize + mTimeBgSize + mSuffixDayLeftMargin, mSuffixDayTextBaseline, mSuffixTextPaint);
                }

                // 计算小时x轴
                mHourLeft = mLeftPaddingSize + mTimeBgSize + mSuffixDayTextWidth + mSuffixDayLeftMargin + mSuffixDayRightMargin;
            } else {
                mHourLeft = mLeftPaddingSize;
            }

            // 判断显示小时
            if (isShowHour) {
                // 画小时背景
                canvas.drawRoundRect(mHourBgRectF, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
                if (isShowTimeBgDivisionLine) {
                    // 画小时背景中间的横线
                    canvas.drawLine(mHourLeft, mTimeBgDivisionLineYPos, mTimeBgSize + mHourLeft, mTimeBgDivisionLineYPos, mTimeTextBgDivisionLinePaint);
                }
                // 画小时文字
                canvas.drawText(formatNum(mHour), mHourBgRectF.centerX(), mTimeTextBaseY, mTimeTextPaint);
                if (mSuffixHourTextWidth > 0) {
                    // 画小时后缀
                    canvas.drawText(mSuffixHour, mHourLeft + mTimeBgSize + mSuffixHourLeftMargin, mSuffixHourTextBaseline, mSuffixTextPaint);
                }

                // 计算分钟x轴
                mMinuteLeft = mHourLeft + mTimeBgSize + mSuffixHourTextWidth + mSuffixHourLeftMargin + mSuffixHourRightMargin;
            } else {
                mMinuteLeft = mHourLeft;
            }

            // 判断显示分钟
            if (isShowMinute) {
                // 画分钟背景
                canvas.drawRoundRect(mMinuteBgRectF, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
                if (isShowTimeBgDivisionLine) {
                    // 画分钟背景中间的横线
                    canvas.drawLine(mMinuteLeft, mTimeBgDivisionLineYPos, mTimeBgSize + mMinuteLeft, mTimeBgDivisionLineYPos, mTimeTextBgDivisionLinePaint);
                }
                // 画分钟文字
                canvas.drawText(formatNum(mMinute), mMinuteBgRectF.centerX(), mTimeTextBaseY, mTimeTextPaint);
                if (mSuffixMinuteTextWidth > 0) {
                    // 画分钟后缀
                    canvas.drawText(mSuffixMinute, mMinuteLeft + mTimeBgSize + mSuffixMinuteLeftMargin, mSuffixMinuteTextBaseline, mSuffixTextPaint);
                }

                // 计算秒钟x轴
                mSecondLeft = mMinuteLeft + mTimeBgSize + mSuffixMinuteTextWidth + mSuffixMinuteLeftMargin + mSuffixMinuteRightMargin;
            } else {
                mSecondLeft = mMinuteLeft;
            }

            // 画秒钟背景
            canvas.drawRoundRect(mSecondBgRectF, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
            if (isShowTimeBgDivisionLine) {
                // 画秒钟背景中间的横线
                canvas.drawLine(mSecondLeft, mTimeBgDivisionLineYPos, mTimeBgSize + mSecondLeft, mTimeBgDivisionLineYPos, mTimeTextBgDivisionLinePaint);
            }
            // 画秒钟文字
            canvas.drawText(formatNum(mSecond), mSecondBgRectF.centerX(), mTimeTextBaseY, mTimeTextPaint);
            if (mSuffixSecondTextWidth > 0) {
                // 画秒钟后缀
                canvas.drawText(mSuffixSecond, mSecondLeft + mTimeBgSize + mSuffixSecondLeftMargin, mSuffixSecondTextBaseline, mSuffixTextPaint);
            }

            if (isShowMillisecond) {
                // 计算毫秒x轴
                float mMillisecondLeft = mSecondLeft + mTimeBgSize + mSuffixSecondTextWidth + mSuffixSecondLeftMargin + mSuffixSecondRightMargin;
                // 画毫秒背景
                canvas.drawRoundRect(mMillisecondBgRectF, mTimeBgRadius, mTimeBgRadius, mTimeTextBgPaint);
                if (isShowTimeBgDivisionLine) {
                    // 画毫秒背景中间的横线
                    canvas.drawLine(mMillisecondLeft, mTimeBgDivisionLineYPos, mTimeBgSize + mMillisecondLeft, mTimeBgDivisionLineYPos, mTimeTextBgDivisionLinePaint);
                }
                // 画毫秒文字
                canvas.drawText(formatMillisecond(), mMillisecondBgRectF.centerX(), mTimeTextBaseY, mTimeTextPaint);
                if (mSuffixMillisecondTextWidth > 0) {
                    // 画毫秒后缀
                    canvas.drawText(mSuffixMillisecond, mMillisecondLeft + mTimeBgSize + mSuffixMillisecondLeftMargin, mSuffixMillisecondTextBaseline, mSuffixTextPaint);
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    /**
     * 启动倒计时
     * @param millisecond 毫秒数
     */
    public void start(long millisecond) {
        if (millisecond <= 0) {
            return ;
        }

        if (null != mCountDownTimer) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }

        long countDownInterval;
        if (isShowMillisecond) {
            countDownInterval = 10;
            updateShow(millisecond);
        } else {
            countDownInterval = 1000;
        }

        mCountDownTimer = new CountDownTimer(millisecond, countDownInterval) {
            @Override
            public void onFinish() {
                // 倒计时结束
                allShowZero();
                // 回调
                if (null != mOnCountdownEndListener) {
                    mOnCountdownEndListener.onEnd(CountdownView.this);
                }
            }

            @Override
            public void onTick(long millisUntilFinished) {
                updateShow(millisUntilFinished);
            }
        };
        mCountDownTimer.start();
    }

    public void stop() {
        if (null != mCountDownTimer) mCountDownTimer.cancel();
    }

    public void setShowHourView(boolean isShowHour) {
        this.isShowHour = isShowHour;
        invalidate();
    }

    public void setShowMillisecondView(boolean isShowMillisecond) {
        this.isShowMillisecond = isShowMillisecond;
        invalidate();
    }

    public void allShowZero() {
        mHour = 0;
        mMinute = 0;
        mSecond = 0;
        mMillisecond = 0;

        invalidate();
    }

    public void setOnCountdownEndListener(OnCountdownEndListener onCountdownEndListener) {
        this.mOnCountdownEndListener = onCountdownEndListener;
    }

    private void updateShow(long ms) {
        mDay = (int)(ms / (1000 * 60 * 60 * 24));
        mHour = (int)((ms % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        mMinute = (int)((ms % (1000 * 60 * 60)) / (1000 * 60));
        mSecond = (int)((ms % (1000 * 60)) / 1000);
        mMillisecond = (int)(ms % 1000);

        invalidate();
    }

    private String formatNum(int time) {
        return time < 10 ? "0"+time : String.valueOf(time);
    }

    private String formatMillisecond() {
        String retMillisecondStr;

        if (mMillisecond > 99) {
            retMillisecondStr = String.valueOf(mMillisecond / 10);
        } else if (mMillisecond <= 9) {
            retMillisecondStr = "0" + mMillisecond;
        } else {
            retMillisecondStr = String.valueOf(mMillisecond);
        }

        return retMillisecondStr;
    }

    public interface OnCountdownEndListener {
        void onEnd(CountdownView cv);
    }

    private int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private float sp2px(float spValue) {
        final float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return spValue * scale;
    }

}
