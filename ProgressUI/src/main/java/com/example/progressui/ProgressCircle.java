package com.example.progressui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by db538 on 7/19/13.
 */
public class ProgressCircle extends View {

    private Typeface ttfRobotoLight;
    private Typeface ttfRobotoThin;

    private int value = 42;

    private Paint paintText;
    private int colorText = 0xff000000;

    private Paint paintCircle;
    private int colorCircle = 0xff000000;

    private float factorOuter = 0.7f;
    private float factorInner = 0.8f;
    private float factorText = 0.8f;
    private float factorPercent = 0.6f;

    private float radiusOuter;
    private float radiusInner;
    private float radiusText;
    private float sizeText;

    private FloatPoint posCenter;
    private FloatPoint posText_Value;
    private FloatPoint posText_Percent;

    public ProgressCircle(Context context) {
        super(context);

        init();
    }

    public ProgressCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        if (!isInEditMode()) {
            ttfRobotoLight = Typeface.createFromAsset(this.getContext().getAssets(), "Roboto-Light.ttf");
            ttfRobotoThin = Typeface.createFromAsset(this.getContext().getAssets(), "Roboto-Thin.ttf");
        }

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(colorText);
        if (!isInEditMode())
            paintText.setTypeface(ttfRobotoThin);

        paintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintCircle.setColor(colorCircle);
        paintCircle.setStyle(Paint.Style.STROKE);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        computeTextPosition();

        this.invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        radiusOuter = (Math.min(w, h) >> 1) * factorOuter;
        radiusInner = radiusOuter * factorInner;
        radiusText = radiusInner * factorText;

        float posxCenter = w >> 1;
        float posyCenter = h >> 1;
        posCenter = new FloatPoint(posxCenter, posyCenter);

        // compute the desired font height
        float const_height = 1000.0f;
        float width_over_height = computeValueTextWidth(const_height, 100) / const_height;
        sizeText = (float) Math.sqrt(4 * radiusText * radiusText / (1 + width_over_height * width_over_height));

        computeTextPosition();
    }

    private Rect getStringRect(String str) {
        Rect rect = new Rect();
        paintText.getTextBounds(str, 0, str.length(), rect);
        return rect;
    }

    private float computeValueTextWidth() {
        return computeValueTextWidth(sizeText, value);
    }

    private float computeValueTextWidth(float height, int value) {
        paintText.setTextSize(height);
        float widthNumber = getStringRect(Integer.toString(value)).right;
        paintText.setTextSize(height * factorPercent);
        float widthPercent = getStringRect("%").right;
        return widthNumber + widthPercent;
    }

    private void computeTextPosition() {
        String strNumber = Integer.toString(value);
        String strPercent = "%";

        paintText.setTextSize(sizeText);
        Rect rectNumber = getStringRect(strNumber);
        paintText.setTextSize(sizeText * factorPercent);
        Rect rectPercent = getStringRect(strPercent);

        float widthTotal = rectNumber.right + rectPercent.right;
        float heightTotal = rectNumber.top;

        float startxNumber = posCenter.x - widthTotal / 2;
        float startyNumber = posCenter.y - heightTotal / 2;

        float startxPercent = startxNumber + rectNumber.right;
        float startyPercent = startyNumber + heightTotal - rectPercent.top;

        posText_Value = new FloatPoint(startxNumber, startyNumber);
        posText_Percent = new FloatPoint(startxPercent, startyPercent);
    }

    private void drawValueText(Canvas canvas) {
        String strNumber = Integer.toString(value);
        String strPercent = "%";

        paintText.setTextSize(sizeText);
        canvas.drawText(strNumber, posText_Value.x, posText_Value.y, paintText);
        paintText.setTextSize(sizeText * factorPercent);
        canvas.drawText(strPercent, posText_Percent.x, posText_Percent.y, paintText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(posCenter.x, posCenter.y, radiusOuter, paintCircle);
        canvas.drawCircle(posCenter.x, posCenter.y, radiusInner, paintCircle);

        drawValueText(canvas);
    }

    private static class FloatPoint {
        float x, y;

        public FloatPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}
