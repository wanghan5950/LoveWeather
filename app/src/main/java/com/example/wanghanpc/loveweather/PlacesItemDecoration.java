package com.example.wanghanpc.loveweather;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PlacesItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private Drawable placeDrawable;
    private int orientation;
    private boolean hasBottomPlace = true;

    public PlacesItemDecoration(Context context, int orientation) {
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        placeDrawable = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);
    }

    public PlacesItemDecoration(Context context, int orientation, boolean hasBottomPlace){
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        placeDrawable = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);
        this.hasBottomPlace = hasBottomPlace;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state){
        if (orientation == VERTICAL_LIST){
            drawVertical(canvas,parent);
        }else {
            drawHorizontal(canvas,parent);
        }
    }

    public void setPlaceDrawable(Drawable drawable){
        this.placeDrawable = drawable;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        if (orientation == VERTICAL_LIST){
            outRect.set(0,0,0,placeDrawable.getIntrinsicHeight());
        }else {
            outRect.set(0,0,placeDrawable.getIntrinsicWidth(),0);
        }
    }

    public void setOrientation(int orientation){
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw new IllegalArgumentException("invalid orientation");
        }
        this.orientation = orientation;
    }

    private void drawVertical(Canvas canvas, RecyclerView parent){
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = hasBottomPlace ? parent.getChildCount() : parent.getChildCount() - 1;
        for (int i  = 0; i < childCount; i ++){
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + placeDrawable.getIntrinsicHeight();
            placeDrawable.setBounds(left,top,right,bottom);
            placeDrawable.draw(canvas);
        }
    }
    private void drawHorizontal(Canvas canvas, RecyclerView parent){
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = hasBottomPlace ? parent.getChildCount() : parent.getChildCount() - 1;
        for (int i = 0; i < childCount; i ++){
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + placeDrawable.getIntrinsicHeight();
            placeDrawable.setBounds(left,top,right,bottom);
            placeDrawable.draw(canvas);
        }
    }
}
