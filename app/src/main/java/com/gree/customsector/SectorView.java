package com.gree.customsector;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.gree.customsector.Util.Tools;

/**
 * Created by JackHou on 2017/3/24.
 * 自定义控件用于空调扫风界面的使用，分为上下、左右扫风两种
 */

public class SectorView extends View implements GestureDetector.OnGestureListener
{
    private SelectedListener listener;

    //当圆直径大于控件的宽或者高的时候，minR是宽高小的哪个值
    private float minR;

    public void setSelectedListener(SelectedListener listener)
    {
        this.listener = listener;
    }

    //上下扫风还是左右扫风，true:左右  false:上下
    private boolean uDOrRF = true;
    //扇形个数
    private static final int childSize = 5;
    //每个扇形的角度
    private static final float childAngle = 100f / childSize;
    //圆的直径R
    private float diam = 1000;
    //圆的半径
    private float radius = diam / 2;
    //默认的扇形内部未选中时候的颜色
    private int sectorColor = Color.WHITE;
    //默认的扇形内部未选中时候的颜色
    private int sectorColor_selected = Color.LTGRAY;
    //选中的扇形
    private int selectedId = -1;
    private Context mContext;
    //画笔
    private Paint mPain;
    //画笔粗细
    private float strokeWidth = 1.5f;
    //画布
    private Canvas mCanvas;
    private Path mPath;
    //手势检测
    private GestureDetector mGestureDetector;

    public SectorView(Context context)
    {
        this(context, null);
    }

    public SectorView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SectorView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SectorView);
        diam = array.getDimension(R.styleable.SectorView_diam, 1000);
        radius = diam / 2;
        sectorColor = array.getColor(R.styleable.SectorView_sectorColor, Color.WHITE);
        sectorColor_selected = array.getColor(R.styleable.SectorView_sectorColor_selected, Color.LTGRAY);
        uDOrRF = array.getBoolean(R.styleable.SectorView_uDOrRl, true);
        //dp转换成px
        strokeWidth = Tools.dp2px(context, strokeWidth);
        mGestureDetector = new GestureDetector(this);
        mGestureDetector.setIsLongpressEnabled(false);
        mPain = new Paint();
        //抗锯齿
        mPain.setAntiAlias(true);
        mPain.setStyle(Paint.Style.STROKE);//设置画笔格式
        mPain.setStrokeWidth(strokeWidth);//设置画笔宽度
        mPain.setColor(sectorColor);//设置画笔颜色
        mCanvas = new Canvas();
        //canvas加上抗锯齿
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        mPath = new Path();
        array.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST)
        {
            setMeasuredDimension((int) diam, (int) diam);
        }
        else if(widthSpecMode == MeasureSpec.AT_MOST)
        {
            setMeasuredDimension((int) diam, heightSpecSize);
        }
        else if(heightSpecMode == MeasureSpec.AT_MOST)
        {
            setMeasuredDimension(widthSpecSize, (int) diam);
        }
    }

    /**
     * 画扇形
     *
     * @param canvas
     * @param rectF
     */
    private void drawArc(Canvas canvas, RectF rectF)
    {
        if(uDOrRF)
        {
            Tools.ShowLog("zxxxx", "drawArc: 上下扫风");
            RectF rect = new RectF(rectF.left - strokeWidth / 2, rectF.top - strokeWidth / 2, rectF.right + strokeWidth / 2, rectF.bottom + strokeWidth / 2);
            for(int i = 2; i < childSize + 2; i++)
            {
                if(i <= selectedId)
                {
                    //选中的扇形画实心，否则画空心
                    mPain.setColor(sectorColor_selected);
                    mPain.setStyle(Paint.Style.FILL);
                    canvas.drawArc(rect, i * childAngle, childAngle, true, mPain);
                }
                else
                {
                    mPain.setColor(Color.LTGRAY);
                    mPain.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(rectF, i * childAngle, childAngle, true, mPain);
                }
            }
        }
        else
        {
            RectF rect = new RectF(rectF.left - strokeWidth / 2, rectF.top - strokeWidth / 2, rectF.right + strokeWidth / 2, rectF.bottom + strokeWidth / 2);
            for(int i = 6; i < childSize + 6; i++)
            {
                if(i <= selectedId)
                {
                    //选中的扇形画实心，否则画空心
                    mPain.setColor(sectorColor_selected);
                    mPain.setStyle(Paint.Style.FILL);
                    canvas.drawArc(rect, i * childAngle, childAngle, true, mPain);
                }
                else
                {
                    mPain.setColor(Color.LTGRAY);
                    mPain.setStyle(Paint.Style.STROKE);
                    canvas.drawArc(rectF, i * childAngle, childAngle, true, mPain);
                }
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas)
    {

        /*绘制外圆*/
        mPain.setStyle(Paint.Style.FILL);
        mPain.setColor(sectorColor);
        //左右扫风
        if(uDOrRF)
        {
            //如果圆的直径大于控件的宽或者高，则直径等于宽高小的哪个属性
            if(diam > getHeight() || diam > getWidth())
            {
                minR = getHeight() > getWidth() ? getWidth() / 2 : getHeight() / 2;
                canvas.drawCircle(minR, minR / 2, minR, mPain);
                //绘制最内侧圆
                mPain.setColor(sectorColor);
                canvas.drawCircle(minR, minR / 2, minR - strokeWidth, mPain);
                //绘制扇形
                drawArc(canvas, new RectF(strokeWidth, strokeWidth - minR / 2, minR * 2 - strokeWidth, minR * 2 - strokeWidth - minR / 2));
            }
            else
            {
                Tools.ShowLog("zxxxxx", "radius:" + radius);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2 - radius / 2, radius, mPain);
                //绘制最内侧圆
                mPain.setColor(sectorColor);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2 - radius / 2, radius - strokeWidth, mPain);
                //绘制扇形
                drawArc(canvas, new RectF(getWidth() / 2 - radius, getHeight() / 2 - radius- radius / 2,
                        getWidth() / 2 + radius, getHeight() / 2 + radius - radius / 2));
            }
        }//上下扫风
        else
        {
            //如果圆的直径大于控件的宽或者高，则直径等于宽高小的哪个属性
            if(diam > getHeight() || diam > getWidth())
            {
                minR = getHeight() > getWidth() ? getWidth() / 2 : getHeight() / 2;
                canvas.drawCircle(minR, minR, minR, mPain);
                //绘制最内侧圆
                mPain.setColor(sectorColor);
                canvas.drawCircle(minR, minR, minR - strokeWidth, mPain);
                //绘制扇形
                drawArc(canvas, new RectF(getWidth() / 2 - minR + minR / 2, getHeight() / 2 - minR,
                        getWidth() / 2 + minR + minR / 2, getHeight() / 2 + minR));
            }
            else
            {
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPain);
                //绘制最内侧圆
                mPain.setColor(sectorColor);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius - strokeWidth, mPain);
                //绘制扇形
                drawArc(canvas, new RectF(getWidth() / 2 - radius + radius / 2, getHeight() / 2 - radius,
                        getWidth() / 2 + radius + radius / 2, getHeight() / 2 + radius));
            }
        }



    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //左右扫风
                if(uDOrRF)
                {
                    //如果圆的直径大于控件的宽或者高，则直径等于宽高小的哪个属性
                    if(diam > getHeight() || diam > getWidth())
                    {
                        selectedId = whichSector(event.getX() - minR, event.getY() - minR / 2, minR);
                    }
                    else
                    {
                        selectedId = whichSector(event.getX() - getWidth() / 2, event.getY() - getHeight() / 2 + radius / 2, radius);
                    }
                }//上下扫风
                else
                {
                    //如果圆的直径大于控件的宽或者高，则直径等于宽高小的哪个属性
                    if(diam > getHeight() || diam > getWidth())
                    {
                        selectedId = whichSector(event.getX() - getWidth() / 2 - minR / 2, event.getY() - getHeight() / 2, minR);
                    }
                    else
                    {
                        selectedId = whichSector(event.getX() - getWidth() / 2  - radius / 2, event.getY() - getHeight() / 2, radius);
                    }
                }


                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
            default:

                break;
        }
        return true;
    }

    /**
     * 计算点在哪个扇形区域
     *
     * @param x
     * @param y
     * @param r 半径
     * @return
     */
    private int whichSector(double x, double y, double r)
    {
        double mod;
        mod = Math.sqrt(x * x + y * y);
        double arg;

        /*夹角*/
        arg = Math.round(Math.atan2(y, x) / Math.PI * 180);
        arg = arg < 0 ? arg + 360 : arg;

        if(mod > r)
        {
            return -2;
        }
        else
        {
            //左右扫风
            if(uDOrRF)
            {
                Tools.ShowLog("zxxxx", "whichSector:左右扫风");
                /*根据幅角来判断改点落在哪个区域*/
                for(int i = 2; i < childSize + 2; i++)
                {
                    if(isSelected(arg, i) || isSelected(360 + arg, i))
                    {
                        if(listener != null)
                        {
                            listener.onSelectedListener(i - 1);
                        }
                        return i;
                    }
                }
            }//上下扫风
            else
            {
                /*根据幅角来判断改点落在哪个区域*/
                for(int i = 6; i < childSize + 6; i++)
                {
                    if(isSelected(arg, i) || isSelected(360 + arg, i))
                    {
                        if(listener != null)
                        {
                            listener.onSelectedListener(i - 6);
                        }
                        return i;
                    }
                }
            }

        }

        return -1;
    }

    /**
     * 判断该区域是否被选中
     *
     * @param arg 角度
     * @param i   第几个扇形
     * @return
     */
    private boolean isSelected(double arg, int i)
    {
        return arg > (i * childAngle) && arg < ((i + 1) * childAngle);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
    {
        float tpx = 0;
        float tpy = 0;
        //左右扫风
        if(uDOrRF)
        {
            //如果圆的直径大于控件的宽或者高，则直径等于宽高小的哪个属性
            if(diam > getHeight() || diam > getWidth())
            {
                tpx = e2.getX() - minR;
                tpy = e2.getY() - minR / 2;
            }
            else
            {
                tpx = e2.getX() - getWidth() / 2;
                tpy = e2.getY() - getHeight() / 2 + radius / 2;
            }

        }//上下扫风
        else
        {
            //如果圆的直径大于控件的宽或者高，则直径等于宽高小的哪个属性
            if(diam > getHeight() || diam > getWidth())
            {
                tpx = e2.getX() - getWidth() / 2 - minR / 2;
                tpy = e2.getY() - getHeight() / 2;
            }
            else
            {
                tpx = e2.getX() - getWidth() / 2 - radius / 2;
                tpy = e2.getY() - getHeight() / 2;
            }
        }
        selectedId = whichSector(tpx, tpy, radius);
        invalidate();
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e)
    {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e)
    {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        return false;
    }

    /**
     * 定义一个回调接口方法，用于返回当前选定的是哪个扇形
     */
    public interface SelectedListener
    {
        void onSelectedListener(int i);
    }
}
