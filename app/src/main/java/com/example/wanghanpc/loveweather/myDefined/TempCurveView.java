package com.example.wanghanpc.loveweather.myDefined;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.wanghanpc.loveweather.R;
import com.example.wanghanpc.loveweather.tools.PxAndDp;

public class TempCurveView extends View {

    private static final int defaultMinWidth = 50;
    private static final int defaultMinHeight = 60;
    private int minTextSize = 40;
    private int textColor = Color.WHITE;
    private int lineWidth = 5;
    private int dotRadius = 10;
    private int textDotDistance = 10;
    private int position;
    private int size;
    private TextPaint textPaint;
    private Paint.FontMetrics textFontMetrics;
    private Paint dotPaint;
    private Paint linePaint;
    private int minTemp;
    private int maxTemp;
    private float[] tempDataArray;

    public TempCurveView(Context context) {
        super(context);
        initPaint();
    }

    public TempCurveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public TempCurveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
        initPaint();
    }

    /**
     * 设置画笔信息
     */
    private void initPaint() {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(minTextSize);
        textPaint.setColor(textColor);
        textFontMetrics = textPaint.getFontMetrics();

        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.FILL);
        dotPaint.setColor(textColor);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(textColor);
    }

    /**
     * 设置自定义属性并赋初始值
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TempCurveView,defStyleAttr,0);
        minTextSize = (int) array.getDimension(R.styleable.TempCurveView_textSize, PxAndDp.dip2px(minTextSize));
        textColor = array.getColor(R.styleable.TempCurveView_textColor,Color.parseColor("#fff"));
        lineWidth = (int) array.getDimension(R.styleable.TempCurveView_lineWidth,PxAndDp.dip2px(lineWidth));
        dotRadius = (int) array.getDimension(R.styleable.TempCurveView_dotRadius,PxAndDp.dip2px(dotRadius));
        textDotDistance = (int) array.getDimension(R.styleable.TempCurveView_textDotDistance,PxAndDp.dip2px(textDotDistance));
        array.recycle();
    }

    /**
     * 传入总的最高气温和最低气温
     * @param minTemp
     * @param maxTemp
     */
    public void setMaxMinTemp(int minTemp, int maxTemp){
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        Log.d("TempCurveView","minTemp = " + minTemp + " maxTemp = " + maxTemp);
        invalidate();
    }

    /**
     * 传入左、中、右三个气温值(左气温值为本item和上一个item的气温值的平均值，右为本item和下一个item的平均值)
     * @param tempData
     */
    public void setTempDataArray(float[] tempData){
        this.tempDataArray = tempData;
        invalidate();
    }

    public void setPosition(int position, int size){
        this.position = position;
        this.size = size;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = getSize(widthMode,widthSize,0);
        int height = getSize(heightMode,heightSize,1);
        setMeasuredDimension(width,height);
    }

    /**
     * 计算测量的宽高值
     * @param mode
     * @param size
     * @param type 0表示宽度，1表示高度
     * @return 宽度或高度的计算结果
     */
    private int getSize(int mode, int size, int type) {
        int result;
        if (mode == MeasureSpec.EXACTLY){
            result = size;
        }else {
            if (type == 0){
                //最小不能低于最小的宽度
                result = PxAndDp.dip2px(defaultMinWidth) + getPaddingLeft() + getPaddingRight();
            }else {
                //最小不能低于最小宽度加上一些数据
                int textHeight = (int) (textFontMetrics.bottom - textFontMetrics.top);
                //加上文字的高度(需要加上文字和圆点的间距)
                result = PxAndDp.dip2px(defaultMinHeight) + textHeight + getPaddingTop() + getPaddingBottom() + textDotDistance;
            }
            if (mode == MeasureSpec.AT_MOST){
                result = Math.min(result,size);
            }
        }
        return result;
    }

    private int calculateHeight(float temp){
        //最高最低温度差
        float tempRange = maxTemp - minTemp;
        int textHeight = (int) (textFontMetrics.bottom - textFontMetrics.top);
        //view的最高和最低差，减去文字高度和文字和圆点之间的间隙
        int viewDistance = getHeight() - textHeight - textDotDistance;
        //当前温度和最低温度的差
        float currentTempDistance = temp - minTemp;
        return (int) (currentTempDistance * viewDistance / tempRange - textDotDistance);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (tempDataArray == null || minTemp == 0 || maxTemp == 0){
            return;
        }

        //文本的高度
        int textHeight = (int) (textFontMetrics.bottom - textFontMetrics.top);

        //为文字和圆点预留最低的高度
        int baseHeight = getHeight() - textHeight - textDotDistance;
        int calMinMiddle = baseHeight - calculateHeight(tempDataArray[1]);
        canvas.drawCircle(getWidth() / 2, calMinMiddle, dotRadius,dotPaint);

        //画温度文字
        String text = String.valueOf((int)tempDataArray[1]) + "°";
        int baseX = (int) (canvas.getWidth() / 2 - textPaint.measureText(text) / 2);
        int baseY = (int) (calMinMiddle - textFontMetrics.top) + textDotDistance;
        canvas.drawText(text,baseX,baseY,textPaint);

        //画线
        if (position != 0){
            int calMinLeft = baseHeight - calculateHeight(tempDataArray[0]);
            canvas.drawLine(0,calMinLeft,getWidth() / 2, calMinMiddle, linePaint);
        }
        if (position != size - 1){
            int calMinRight = baseHeight - calculateHeight(tempDataArray[2]);
            canvas.drawLine(getWidth() / 2, calMinMiddle, getWidth(), calMinRight ,linePaint);
        }
    }
}
