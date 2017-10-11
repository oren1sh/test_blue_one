package seatback.com.seatback.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import seatback.com.seatback.utils.ColorUtils;

/**
 * Created by gilgoldzweig on 24/03/2017.
 */

public class PressureMap extends View {
    private static final String TAG = PressureMap.class.getSimpleName();
    private Paint paint;
    private int height;
    private int width;
    private int circleRadius;
    private boolean top;
    private boolean drawenOnce = false;
    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    private List<Integer> colors;
    static private List<Integer> lastUsedColors = new ArrayList<>();

    public PressureMap(Context context) {
        super(context);
        init();
    }
    public PressureMap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public PressureMap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        colors = new ArrayList<>();
        paint = new Paint();
        if( lastUsedColors.size() == 0){
            for(int i = 0; i < 72; i++)
                lastUsedColors.add(i, 0);
        }
//        addCircles();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPressureMap(canvas);
        invalidate();
    }

    /**
     * draw circles in a set of 6 each row
     * move to new line.
     * set yDrawPosition one line lower.
     * xDrawPosition back to zero
     * line count go back to zero
     */
    private void drawPressureMap(Canvas canvas) {

        height = getMeasuredHeight()/2;
        width = getMeasuredWidth();
        circleRadius = ((width + height) / 36) * (int) Math.PI / 2;
//        circleRadius = (width + height) / 36 * 2;
        int rowCount = 0;
        int xDrawPosition = circleRadius;
        int yDrawPosition = circleRadius;
        if ((colors == null || colors.size() < 72) && lastUsedColors.size() > 0)
            colors = lastUsedColors;
        for (int i = 0; i < 36; i++) {
            int pressureLevel = colors.get(isTop() ? i : i + 36);
            paint.setColor(ColorUtils.getColor(pressureLevel));

            if (rowCount == 6) {
                rowCount = 0;
                xDrawPosition = circleRadius;
                yDrawPosition += (circleRadius * 2);
            }
            canvas.drawCircle(xDrawPosition, yDrawPosition, circleRadius, paint);
            xDrawPosition += (circleRadius * 2);
            rowCount++;
        }
        if( colors != null)
            lastUsedColors = colors;
    }

    public void setColors(List<Integer> colors) {
//        if( colors != null) {
//            Random rand = new Random();
//
//            for (int index = 0; index < colors.size(); index++) {
//                colors.set(index, rand.nextInt(500) + 1);
//            }
//        }
//
        this.colors = colors;
        invalidate();
    }
}