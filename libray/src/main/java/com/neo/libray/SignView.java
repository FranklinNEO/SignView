package com.neo.libray;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Neo on 16/4/8.
 */
public class SignView extends View {
    private static final int SignViewWitdh = 290;
    private static final int SignViewHeight = 150;
    private static final float lineWidth = 1;
    private static final int MaxSize = 15;

    private Canvas canvas = null;

    private Paint deflinePaint;
    private Paint linePaint;
    private Paint picPaint;
    private Paint greyPaint;
    private Paint redPaint;
    private Paint whitePaint;

    private Path defline;
    private Path signedline;

    private ValueAnimator lineAnimator = null;
    private ValueAnimator signAnimator = null;

    private Bitmap bitmap_sign_icon = null;

    private int signedDays = 0;

    private int unsignColor = 0xD4D4D4;
    private int signedColor = 0xFF3A3F;
    private int backgroundColor = 0xFFFFFF;

    private int bgSpPic = R.drawable.ic_sp_trans;

    private int line_progress = 0;
    private int circle_progress = 0;

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
        backgroundColor = a.getColor(R.styleable.SignView_backgroundColor, backgroundColor);

        bgSpPic = a.getResourceId(R.styleable.SignView_bgSpPic, bgSpPic);
        a.recycle();
        setSignedDays(signedDays);

        bitmap_sign_icon = BitmapFactory.decodeResource(getResources(), bgSpPic);

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

        greyPaint = new Paint();
        greyPaint.setAntiAlias(true);
        greyPaint.setColor(getResources().getColor(R.color.unsign_color));
        redPaint = new Paint();
        redPaint.setAntiAlias(true);
        redPaint.setColor(getResources().getColor(R.color.signed_color));
        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(getResources().getColor(R.color.sign_background_color));

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
        this.canvas = canvas;
        initView();
        updateLineView();
        updateDotView();
    }

    private void initView() {
        //default
        canvas.drawPath(defline, deflinePaint);
        for (int i = 0; i < coordinateInfos.size(); i++) {
            drawUnsignImage(i);
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
            drawSignedImage(i);
        }
    }


    private void updateLineView() {
        if (line_progress == 0)
            return;
        if (signedDays == 0) {
            drawSignedImage(0);
            return;
        }
        if (signedDays == 4) {
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() + (91 * line_progress / 200)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays == 9) {
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() - (89 * line_progress / 200)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays == 14) {
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() + (87 * line_progress / 200)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays % 10 == 0) {//向下 81
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX()), dp2px((coordinateInfo_now.getY() + (80 * line_progress / 200))), linePaint);
        } else if (signedDays % 10 == 5) {//向下 77
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX()), dp2px((coordinateInfo_now.getY() + (83 * line_progress / 200))), linePaint);
        } else if (signedDays % 10 < 5) {//向右 100
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() + (102 * line_progress / 200)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays % 10 > 5) {//向左 100
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() - (102 * line_progress / 200)), dp2px(coordinateInfo_now.getY()), linePaint);
        }
        if (line_progress == 100) {
            line_progress = 0;
            signAnimator();
        }
    }

    private void updateDotView() {
        if (circle_progress == 0)
            return;
        if (signedDays == 0) {

        } else if (signedDays == 4) {
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() + (91 / 2)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays == 9) {
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() - (89 / 2)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays == 14) {
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() + (87 / 2)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays % 10 == 0) {//向下 81
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX()), dp2px((coordinateInfo_now.getY() + (80 / 2))), linePaint);
        } else if (signedDays % 10 == 5) {//向下 77
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX()), dp2px((coordinateInfo_now.getY() + (83 / 2))), linePaint);
        } else if (signedDays % 10 < 5) {//向右 100
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() + (102 / 2)), dp2px(coordinateInfo_now.getY()), linePaint);
        } else if (signedDays % 10 > 5) {//向左 100
            canvas.drawLine(dp2px(coordinateInfo_now.getX()), dp2px(coordinateInfo_now.getY()), dp2px(coordinateInfo_now.getX() - (102 / 2)), dp2px(coordinateInfo_now.getY()), linePaint);
        }
        drawNextDaysView(signedDays);
        if (circle_progress == 100) {
            signedDays++;
            circle_progress = 0;
            setSignedDays(signedDays);
        }
    }

    private void drawUnsignImage(int poistion) {
        CoordinateInfo coordinateInfo = coordinateInfos.get(poistion);
        switch (poistion) {
            case 4:
                int px_5 = dp2px(24);
                int x_5 = dp2px(coordinateInfo.getX()) - px_5 / 2;
                int y_5 = dp2px(coordinateInfo.getY()) - px_5 / 2;
                canvas.drawCircle(dp2px(coordinateInfo.getX()), dp2px(coordinateInfo.getY()), px_5 / 2, greyPaint);
                canvas.drawBitmap(BitmapUtil.scale(bitmap_sign_icon, (double) px_5, (double) px_5), x_5, y_5, picPaint);
                break;
            case 9:
                int px_10 = dp2px(28);
                int x_10 = dp2px(coordinateInfo.getX()) - px_10 / 2;
                int y_10 = dp2px(coordinateInfo.getY()) - px_10 / 2;
                canvas.drawCircle(dp2px(coordinateInfo.getX()), dp2px(coordinateInfo.getY()), px_10 / 2, greyPaint);
                canvas.drawBitmap(BitmapUtil.scale(bitmap_sign_icon, (double) px_10, (double) px_10), x_10, y_10, picPaint);
                break;
            case 14:
                int px_15 = dp2px(30);
                int x_15 = dp2px(coordinateInfo.getX()) - px_15 / 2;
                int y_15 = dp2px(coordinateInfo.getY()) - px_15 / 2;
                canvas.drawCircle(dp2px(coordinateInfo.getX()), dp2px(coordinateInfo.getY()), px_15 / 2, greyPaint);
                canvas.drawBitmap(BitmapUtil.scale(bitmap_sign_icon, (double) px_15, (double) px_15), x_15, y_15, picPaint);
                break;
            default:
                float px_normal = (float) (dp2px(15) / 2);
                int x_normal = dp2px(coordinateInfo.getX());
                int y_normal = dp2px(coordinateInfo.getY());
                canvas.drawCircle(x_normal, y_normal, px_normal, greyPaint);
                canvas.drawCircle(x_normal, y_normal, dp2px(10) / 2, whitePaint);
                break;
        }
    }

    private void drawSignedImage(int poistion) {
        CoordinateInfo coordinateInfo = coordinateInfos.get(poistion);
        switch (poistion) {
            case 4:
                int px_5 = dp2px(24);
                int x_5 = dp2px(coordinateInfo.getX()) - px_5 / 2;
                int y_5 = dp2px(coordinateInfo.getY()) - px_5 / 2;
                canvas.drawCircle(dp2px(coordinateInfo.getX()), dp2px(coordinateInfo.getY()), px_5 / 2, redPaint);
                canvas.drawBitmap(BitmapUtil.scale(bitmap_sign_icon, (double) px_5, (double) px_5), x_5, y_5, picPaint);
                break;
            case 9:
                int px_10 = dp2px(28);
                int x_10 = dp2px(coordinateInfo.getX()) - px_10 / 2;
                int y_10 = dp2px(coordinateInfo.getY()) - px_10 / 2;
                canvas.drawCircle(dp2px(coordinateInfo.getX()), dp2px(coordinateInfo.getY()), px_10 / 2, redPaint);
                canvas.drawBitmap(BitmapUtil.scale(bitmap_sign_icon, (double) px_10, (double) px_10), x_10, y_10, picPaint);
                break;
            case 14:
                int px_15 = dp2px(30);
                int x_15 = dp2px(coordinateInfo.getX()) - px_15 / 2;
                int y_15 = dp2px(coordinateInfo.getY()) - px_15 / 2;
                canvas.drawCircle(dp2px(coordinateInfo.getX()), dp2px(coordinateInfo.getY()), px_15 / 2, redPaint);
                canvas.drawBitmap(BitmapUtil.scale(bitmap_sign_icon, (double) px_15, (double) px_15), x_15, y_15, picPaint);
                break;
            default:
                float px_normal = (float) (dp2px(15) / 2);
                int x_normal = dp2px(coordinateInfo.getX());
                int y_normal = dp2px(coordinateInfo.getY());
                canvas.drawCircle(x_normal, y_normal, px_normal, redPaint);
                canvas.drawCircle(x_normal, y_normal, dp2px(10) / 2, whitePaint);
                break;
        }
    }

    private void drawNextDaysView(int poistion) {
        CoordinateInfo coordinateInfo = coordinateInfos.get(poistion);
        float startAngle = 0;
        if (poistion == 0) {
            startAngle = 0f;
        } else if (poistion % 10 == 0 || poistion % 10 == 5) {//向下
            startAngle = 270f;
        } else if (poistion % 10 < 5) {//向右
            startAngle = 180f;
        } else if (poistion % 10 > 5) {//向左
            startAngle = 0f;
        }
        switch (poistion) {
            case 4:
                int px_5 = dp2px(24)/2;
                int x_5 = dp2px(coordinateInfo.getX());
                int y_5 = dp2px(coordinateInfo.getY()) ;
                RectF f_5 = new RectF(x_5-px_5, y_5-px_5, x_5 + px_5 , y_5 + px_5);
                canvas.drawCircle(x_5, y_5, px_5 , greyPaint);
                canvas.drawArc(f_5, startAngle, ((float) circle_progress * 360) / 100, true, redPaint);
                canvas.drawBitmap(BitmapUtil.scale(bitmap_sign_icon, (double) px_5*2, (double) px_5*2), x_5-px_5, y_5-px_5, picPaint);
                break;
            case 9:
                int px_10 = dp2px(28)/2;
                int x_10 = dp2px(coordinateInfo.getX());
                int y_10 = dp2px(coordinateInfo.getY()) ;
                RectF f_10 = new RectF(x_10-px_10, y_10-px_10, x_10 + px_10 , y_10 + px_10);
                canvas.drawCircle(x_10, y_10, px_10 , greyPaint);
                canvas.drawArc(f_10, startAngle, ((float) circle_progress * 360) / 100, true, redPaint);
                canvas.drawBitmap(BitmapUtil.scale(bitmap_sign_icon, (double) px_10*2, (double) px_10*2), x_10-px_10, y_10-px_10, picPaint);
                break;
            case 14:
                int px_15 = dp2px(30)/2;
                int x_15 = dp2px(coordinateInfo.getX());
                int y_15 = dp2px(coordinateInfo.getY()) ;
                RectF f_15 = new RectF(x_15-px_15, y_15-px_15, x_15 + px_15 , y_15 + px_15);
                canvas.drawCircle(x_15, y_15, px_15 , greyPaint);
                canvas.drawArc(f_15, startAngle, ((float) circle_progress * 360) / 100, true, redPaint);
                canvas.drawBitmap(BitmapUtil.scale(bitmap_sign_icon, (double) px_15*2, (double) px_15*2), x_15-px_15, y_15-px_15, picPaint);
                break;
            default:
                float px_normal = (float) (dp2px(15) / 2);
                int x_normal = dp2px(coordinateInfo.getX());
                int y_normal = dp2px(coordinateInfo.getY());
                RectF f = new RectF(x_normal - px_normal, y_normal - px_normal, x_normal + px_normal, y_normal + px_normal);
                canvas.drawCircle(x_normal, y_normal, px_normal, greyPaint);
                canvas.drawArc(f, startAngle, ((float) circle_progress * 360) / 100, true, redPaint);
                canvas.drawCircle(x_normal, y_normal, dp2px(10) / 2, whitePaint);
                break;
        }
    }

    private void setNowCoordinateInfo(int mSignedDays) {
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

    public void setSignedDays(int mSignedDays) {
        if (mSignedDays > MaxSize)
            return;
        this.signedDays = mSignedDays;
        postInvalidate();
        setNowCoordinateInfo(mSignedDays);
    }

    public int getSignedDays() {
        return signedDays;
    }

    public void MoveToNext() {
        if (signedDays == MaxSize) {
            Toast.makeText(getContext(), "已是签到最后一天!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (signedDays == 0) {
            signAnimator();
        } else {
            lineAnimator();
        }

    }

    private void lineAnimator() {
        line_progress = 0;
        lineAnimator = ValueAnimator.ofInt(0, 100);
        lineAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                line_progress = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        lineAnimator.setTarget(this);
        lineAnimator.setDuration(800).start();
    }

    private void signAnimator() {
        circle_progress = 0;
        signAnimator = ValueAnimator.ofInt(0, 100);
        signAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                circle_progress = (int) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        signAnimator.setTarget(this);
        signAnimator.setDuration(800).start();
    }

    public int dp2px(float dp) {
        return DensityUtil.dip2px(getContext(), dp);
    }
}
