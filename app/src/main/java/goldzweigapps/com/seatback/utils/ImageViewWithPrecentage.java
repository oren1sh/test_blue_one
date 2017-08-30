package goldzweigapps.com.seatback.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by naji on 8/30/2017.
 */

public class ImageViewWithPrecentage extends AppCompatImageView {
    public ImageViewWithPrecentage(Context context) {
        super(context);
    }

    public ImageViewWithPrecentage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewWithPrecentage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidthHalf = getDefaultSize(getSuggestedMinimumWidth(),
                widthMeasureSpec); //50% of the width of its parent
        int measuredHeightHalf = (int)((float)getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec) * 0.9f); //50% of the Height of its parent
        setMeasuredDimension(measuredWidthHalf, measuredHeightHalf);
    }
}
