package com.neo.libray;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Neo on 16/4/8.
 */
public class SignView extends View implements ValueAnimator.AnimatorUpdateListener {
    private static final int SignViewWitdh = 290;
    private static final int SignViewHeight = 150;
    private static final float lineWidth = 1;
    private static final int MaxSize = 15;

    private Paint deflinePaint;
    private Paint linePaint;
    private Paint picPaint;

    private Path defline;
    private Path signedline;

    private Bitmap bitmap_normal_unsign = null;
    private Bitmap bitmap_5_unsign = null;
    private Bitmap bitmap_10_unsign = null;
    private Bitmap bitmap_15_unsign = null;
    private Bitmap bitmap_normal_signed = null;
    private Bitmap bitmap_5_signed = null;
    private Bitmap bitmap_10_signed = null;
    private Bitmap bitmap_15_signed = null;

    private int signedDays = 0;
    private int unsignColor = 0xD4D4D4;
    private int signedColor = 0xFF3A3F;
    private int unSignNormalPic = R.drawable.ic_normal_unsign;
    private int unSignSpPic = R.drawable.ic_sp_unsign;
    private int signedNormalPic= R.drawable.ic_normal_signed;
    private int signedSpPic=R.drawable.ic_sp_signed;

    private int progress = 0;
    private List<CoordinateInfo> coordinateInfos = Arrays.asList(new CoordinateInfo(15, 15), new CoordinateInfo(80, 15), new CoordinateInfo(145, 15), new CoordinateInfo(210, 15), new CoordinateInfo(275, 15),
            new CoordinateInfo(275, 75), new CoordinateInfo(210, 75), new CoordinateInfo(145, 75), new CoordinateInfo(80, 75), new CoordinateInfo(15, 75),
            new CoordinateInfo(15, 135), new CoordinateInfo(80, 135), new CoordinateInfo(145, 135), new CoordinateInfo(210, 135), new CoordinateInfo(275, 135));
    private CoordinateInfo coordinateInfo_now = null;

    public SignView(Context context) {
        this(context, null);
    }

    public SignView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SignView, defStyleAttr, 0);
        signedDays = a.getInteger(R.styleable.SignView_signedDays, signedDays);
        unsignColor = a.getColor(R.styleable.SignView_unsignColor, unsignColor);
        signedColor = a.getColor(R.styleable.SignView_signedColor, signedColor);
        unSignNormalPic=a.getResourceId(R.styleable.SignView_unSignNormalPic,unSignNormalPic);
        unSignSpPic=a.getResourceId(R.styleable.SignView_unSignNormalPic,unSignSpPic);
        signedNormalPic=a.getResourceId(R.styleable.SignView_unSignNormalPic,signedNormalPic);
        signedSpPic=a.getResourceId(R.styleable.SignView_unSignNormalPic,signedSpPic);
        a.recycle();
        setSignedDays(signedDays);

        bitmap_normal_unsign = BitmapFactory.decodeResource(getResources(), unSignNormalPic);
        bitmap_5_unsign = BitmapFactory.decodeResource(getResources(), unSignSpPic);
        bitmap_10_unsign = BitmapFactory.decodeResource(getResources(), unSignSpPic);
        bitmap_15_unsign = BitmapFactory.decodeResource(getResources(), unSignSpPic);
        bitmap_normal_signed = BitmapFactory.decodeResource(getResources(), signedNormalPic);
        bitmap_5_signed = BitmapFactory.decodeResource(getResources(), signedSpPic);
        bitmap_10_signed = BitmapFactory.decodeResource(getResources(), signedSpPic);
        bitmap_15_signed = BitmapFactory.decodeResource(getResources(), signedSpPic);

        deflinePaint = new Paint();
        deflinePaint.setAntiAlias(true);
        deflinePaint.setStrokeWidth(lineWidth);
        deflinePaint.setColor(getResources().getColor(R.color.unsign_color));
        deflinePaint.setStyle(Paint.Style.STROKE);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(getResources().getColor(R.color.signed_color));
        linePaint.setStyle(Paint.Style.STROKE);

        picPaint = new Paint();
        picPaint.setAntiAlias(true);

        defline = new Path();
        defline.moveTo(dp2px(15), dp2px(15));
        defline.lineTo(dp2px(275), dp2px(15));
        defline.lineTo(dp2px(275), dp2px(75));
        defline.lineTo(dp2px(15), dp2px(75));
        defline.lineTo(dp2px(15), dp2px(135));
        defline.lineTo(dp2px(275), dp2px(135));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(dp2px(SignViewWitdh), dp2px(SignViewHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initView(canvas);
        updateView(canvas);
    }

    private void updateView(Canvas canvas) {
        if (progress == 0)
            return;
        if (signedDays == 0) {
            drawSignedImage(canvas, 0);
            return;
        }
        if (signedDays == 4) {
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() + (91 * progress / 200)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays == 9) {
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() - (89 * progress / 200)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays == 14) {
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() + (87 * progress / 200)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays % 10 == 0) {//向下 81
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX()), dp2px((coordinateInfo_now.getY() + (80 * progress / 200))), linePaint);
        } else if (signedDays % 10 == 5) {//向下 77
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX()), dp2px((coordinateInfo_now.getY() + (83 * progress / 200))), linePaint);
        } else if (signedDays % 10 < 5) {//向右 100
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() + (102 * progress / 200)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays % 10 > 5) {//向左 100
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() - (102 * progress / 200)), dp2px(coordinateInfo_now.getY()), linePaint);
        }

        if (progress == 100) {
            signedDays++;
            progress = 0;
            setSignedDays(signedDays);
        }
    }

    private void initView(Canvas canvas) {
        //default
        canvas.drawPath(defline, deflinePaint);
        for (int i = 0; i < coordinateInfos.size(); i++) {
            drawUnsignImage(canvas, i);
        }
        //signedline
        signedline = new Path();
        signedline.moveTo(dp2px(15), dp2px(15));
        for (int i = 0; i < signedDays; i++) {
            CoordinateInfo coordinateInfo = coordinateInfos.get(i);
            signedline.lineTo(dp2px(coordinateInfo.getX()), dp2px(coordinateInfo.getY()));
        }
        canvas.drawPath(signedline, linePaint);
        //signedImg
        for (int i = 0; i < signedDays; i++) {
            drawSignedImage(canvas, i);
        }
    }

    private void drawUnsignImage(Canvas canvas, int poistion) {
        CoordinateInfo coordinateInfo = coordinateInfos.get(poistion);
        switch (poistion) {
            case 4:
                int px_5 = dp2px(24);
                int x_5 = dp2px(coordinateInfo.getX()) - px_5 / 2;
                int y_5 = dp2px(coordinateInfo.getY()) - px_5 / 2;
                canvas.drawBitmap(BitmapUtil.scale(bitmap_5_unsign, (double) px_5, (double) px_5), x_5, y_5, picPaint);
                break;
            case 9:
                int px_10 = dp2px(28);
                int x_10 = dp2px(coordinateInfo.getX()) - px_10 / 2;
                int y_10 = dp2px(coordinateInfo.getY()) - px_10 / 2;
                canvas.drawBitmap(BitmapUtil.scale(bitmap_10_unsign, (double) px_10, (double) px_10), x_10, y_10, picPaint);
                break;
            case 14:
                int px_15 = dp2px(30);
                int x_15 = dp2px(coordinateInfo.getX()) - px_15 / 2;
                int y_15 = dp2px(coordinateInfo.getY()) - px_15 / 2;
                canvas.drawBitmap(BitmapUtil.scale(bitmap_15_unsign, (double) px_15, (double) px_15), x_15, y_15, picPaint);
                break;
            default:
                int px_normal = dp2px(15);
                int x_normal = dp2px(coordinateInfo.getX()) - px_normal / 2;
                int y_normal = dp2px(coordinateInfo.getY()) - px_normal / 2;
                canvas.drawBitmap(BitmapUtil.scale(bitmap_normal_unsign, (double) px_normal, (double) px_normal), x_normal, y_normal, picPaint);
                break;
        }
    }

    private void drawSignedImage(Canvas canvas, int poistion) {
        CoordinateInfo coordinateInfo = coordinateInfos.get(poistion);
        switch (poistion) {
            case 4:
                int px_5 = dp2px(24);
                int x_5 = dp2px(coordinateInfo.getX()) - px_5 / 2;
                int y_5 = dp2px(coordinateInfo.getY()) - px_5 / 2;
                canvas.drawBitmap(BitmapUtil.scale(bitmap_5_signed, (double) px_5, (double) px_5), x_5, y_5, picPaint);
                break;
            case 9:
                int px_10 = dp2px(28);
                int x_10 = dp2px(coordinateInfo.getX()) - px_10 / 2;
                int y_10 = dp2px(coordinateInfo.getY()) - px_10 / 2;
                canvas.drawBitmap(BitmapUtil.scale(bitmap_10_signed, (double) px_10, (double) px_10), x_10, y_10, picPaint);
                break;
            case 14:
                int px_15 = dp2px(30);
                int x_15 = dp2px(coordinateInfo.getX()) - px_15 / 2;
                int y_15 = dp2px(coordinateInfo.getY()) - px_15 / 2;
                canvas.drawBitmap(BitmapUtil.scale(bitmap_15_signed, (double) px_15, (double) px_15), x_15, y_15, picPaint);
                break;
            default:
                int px_normal = dp2px(15);
                int x_normal = dp2px(coordinateInfo.getX()) - px_normal / 2;
                int y_normal = dp2px(coordinateInfo.getY()) - px_normal / 2;
                canvas.drawBitmap(BitmapUtil.scale(bitmap_normal_signed, (double) px_normal, (double) px_normal), x_normal, y_normal, picPaint);
                break;
        }
    }

    public void setSignedDays(int mSignedDays) {
        if (mSignedDays > MaxSize)
            return;
        this.signedDays = mSignedDays;
        postInvalidate();
        if (mSignedDays >= 15 || mSignedDays == 0) {
            return;
        }
        CoordinateInfo coordinateInfo = coordinateInfos.get(mSignedDays - 1);
        if (mSignedDays % 10 == 0) {
            coordinateInfo_now = new CoordinateInfo(coordinateInfo.getX(), coordinateInfo.getY() + 14);
        } else if (mSignedDays % 10 == 5) {
            coordinateInfo_now = new CoordinateInfo(coordinateInfo.getX(), coordinateInfo.getY() + 12);
        } else if (mSignedDays % 10 < 5) {
            coordinateInfo_now = new CoordinateInfo((float) (coordinateInfo.getX() + 7.5), coordinateInfo.getY());
        } else if (mSignedDays % 10 > 5) {
            coordinateInfo_now = new CoordinateInfo((float) (coordinateInfo.getX() - 7.5), coordinateInfo.getY());
        }
    }

    public int getSignedDays() {
        return signedDays;
    }

    public void MoveToNext() {
        if (signedDays == MaxSize) {
            Toast.makeText(getContext(), "已是签到最后一天!", Toast.LENGTH_SHORT).show();
            return;
        }
        progress = 0;
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.addUpdateListener(this);
        animator.setTarget(this);
        animator.setDuration(1000).start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        progress = (int) animation.getAnimatedValue();
        postInvalidate();
    }

    public int dp2px(float dp) {
        return DensityUtil.dip2px(getContext(), dp);
    }
}
