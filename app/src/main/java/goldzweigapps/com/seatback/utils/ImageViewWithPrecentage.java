package goldzweigapps.com.seatback.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import goldzweigapps.com.seatback.R;

/**
 * Created by naji on 8/30/2017.
 */

public class ImageViewWithPrecentage extends AppCompatImageView {
    public float precentage = 0.9f;

    public ImageViewWithPrecentage(Context context) {
        super(context);
    }

    public ImageViewWithPrecentage(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ImageViewWithPrecentage);
        CharSequence foo_cs = arr.getString(R.styleable.ImageViewWithPrecentage_precentage);
        if (foo_cs != null) {
            // Do something with foo_cs.toString()
            precentage = Float.parseFloat(foo_cs.toString());
        }
        arr.recycle();  // Do this when done.
    }

    public ImageViewWithPrecentage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ImageViewWithPrecentage);
        CharSequence foo_cs = arr.getString(R.styleable.ImageViewWithPrecentage_precentage);
        if (foo_cs != null) {
            // Do something with foo_cs.toString()
            precentage = Float.parseFloat(foo_cs.toString());
        }
        arr.recycle();  // Do this when done.
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidthHalf = getDefaultSize(getSuggestedMinimumWidth(),
                widthMeasureSpec); //50% of the width of its parent
        int measuredHeightHalf = (int)((float)getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec) * precentage); //50% of the Height of its parent
        setMeasuredDimension(measuredWidthHalf, measuredHeightHalf);
    }
}
