package seatback.com.seatback.views;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

public class VerticalTextView extends AppCompatTextView {


   public VerticalTextView(Context context, AttributeSet attrs) {
      super(context, attrs);
      final int gravity = getGravity();
   }

   @Override
   protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      super.onMeasure(heightMeasureSpec, widthMeasureSpec);
      setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
   }

   @Override
   protected boolean setFrame(int l, int t, int r, int b) {
      return super.setFrame(l, t, l + (b - t), t + (r - l));
   }

   @Override
   public void draw(Canvas canvas) {
         canvas.translate(0, getWidth());
         canvas.rotate(-90);

      canvas.clipRect(0, 0, getWidth(), getHeight(), android.graphics.Region.Op.REPLACE);
      super.draw(canvas);
      invalidate();
   }
}