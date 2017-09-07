package seatback.com.seatback.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

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
    private List<Integer> lastUsedColors = new ArrayList<>();

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

        height = getMeasuredHeight();
        width = getMeasuredWidth();
        circleRadius = ((width + height) / 36) * (int) Math.PI / 2;
//        circleRadius = (width + height) / 36 * 2;
        int rowCount = 0;
        int xDrawPosition = circleRadius;
        int yDrawPosition = circleRadius;
            for (int i = 0; i != 36; i++) {
                if (colors == null || colors.size() < 72) {
                    if (drawenOnce) {
                        int pressureLevel = lastUsedColors.get(isTop() ? i : i + 36);
                        paint.setColor(ColorUtils.getColor(pressureLevel));
                    } else {
                        paint.setColor(ColorUtils.BLUE_100);
                    }

//                paint.setColor(Color.RGBUtils.getColorForPressure(colors.get(i + (top ? 0 : 36))));
//                Log.d(TAG, "drawPressureMap: " + Utils.getColorForPressure(colors.get(i + (top ? 0 : 36))));
                } else {
                    int pressureLevel = colors.get(isTop() ? i : i + 36);
                    paint.setColor(ColorUtils.getColor(pressureLevel));
                    drawenOnce = true;
                    lastUsedColors = colors;
                }



                if (rowCount == 6) {
                    rowCount = 0;
                    xDrawPosition = circleRadius;
                    yDrawPosition += (circleRadius * 2);
                }
                canvas.drawCircle(xDrawPosition, yDrawPosition, circleRadius, paint);
                xDrawPosition += (circleRadius * 2);
                rowCount++;
            }



//        addCircles(Canvas canvas);
//        for (Circle circle : pressureMapArray) {
//            canvas.drawCircle(circle.getCx(), circle.getCy(), circle.getRadius(), circle.getPaint());
//        }
    }

    public void setColors(List<Integer> colors) {
        this.colors = colors;
        invalidate();
    }
}