package com.lzj.slidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class QuickSearchView extends View {

    private static final String[] SECTIONS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private Paint paint;
    private float cellWidth;
    private float cellHeight;
    private Rect rect;
    private int currentIndex = -1;
    private Context context;

    public QuickSearchView(Context context) {
        this(context,null);
    }

    public QuickSearchView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public QuickSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    private void init() {
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.common_text_color_light_black));
        paint.setAntiAlias(true);//抗锯齿
        paint.setTextSize(dp2px(context, 10));//此方法设置的单位是像素

        //矩形对象
        rect = new Rect();
    }

    //onMeasure  onLayout  onDraw


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //getMeasuredWidth()
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //getMeasuredHeight()
    }

    //宽度0-->真实的宽度
    //此方法在onMeasure之后被系统调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cellWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        //提高精度
        cellHeight = measuredHeight*1.0f / SECTIONS.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //因为绘制文字和绘制图片的起点是不一样的，绘制文字的起点是从左下角
        for (int i = 0; i < SECTIONS.length; i++) {
            //获取某一个字符串的矩形边框
            paint.getTextBounds(SECTIONS[i],0,1,rect);
            int textWidth = rect.width();
            int textHeight = rect.height();
            float x = cellWidth/2-textWidth/2;
            float y = cellHeight/2+textHeight/2 + cellHeight*i;

            if(currentIndex == i) {
                paint.setColor(Color.WHITE);
            } else {
                paint.setColor(context.getResources().getColor(R.color.common_text_color_light_black));
            }

            canvas.drawText(SECTIONS[i],x,y,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();//触摸点的y轴坐标
                //A  0~10    索引0
                //B  11~20   索引1
                //C 21~30  索引2
                int oldIndex = currentIndex;
                currentIndex = (int) (y/cellHeight);
                if(currentIndex > SECTIONS.length - 1) {
                    currentIndex = SECTIONS.length - 1;
                } else if (currentIndex < 0) {
                    currentIndex = 0;
                }
                if(oldIndex != currentIndex) {
                    notifyLetterChanged(SECTIONS[currentIndex], (float) (cellHeight * (currentIndex + 0.5)));
                    invalidate();//请求重新绘制
                }
                break;
            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    listener.onTouchStop();
                }
                currentIndex = -1;
                invalidate();
                break;
        }
        return true;
    }

    public interface OnLetterChangedListener {

        void onLetterChanged(String letter, float y);

        void onTouchStop();
    }

    private OnLetterChangedListener listener;

    public void setOnLetterChangedListener(OnLetterChangedListener listener) {
        this.listener = listener;
    }

    private void notifyLetterChanged(String letter, float y) {
        if(listener != null) {
            listener.onLetterChanged(letter, y);
        }
    }

    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
