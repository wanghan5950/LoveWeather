package com.example.wanghanpc.loveweather.adapters;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class PlaceItemTouchCallback extends ItemTouchHelper.Callback {

    private static final int PLACE_MODE_CHECK = 0;
    private static final int PLACE_MODE_EDIT = 1;
    private int editMode = PLACE_MODE_CHECK;

    private ItemTouchAdapterInterface itemTouchAdapter;

    public PlaceItemTouchCallback(ItemTouchAdapterInterface itemTouchAdapter) {
        this.itemTouchAdapter = itemTouchAdapter;
    }

    /**
     * 是否长按后才拖动
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    /**
     * 是否支持左右滑动操作
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * 返回可以滑动的方向
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0,swipeFlags = 0;
        if (editMode == PLACE_MODE_EDIT){
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }else {
            swipeFlags = ItemTouchHelper.LEFT;
        }
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    /**
     * item在拖拽过程中不断回调的方法
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        itemTouchAdapter.onMove(fromPosition,toPosition);
        return true;
    }

    /**
     * item被侧滑时调用
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        itemTouchAdapter.onSwiped(position);
    }

    /**
     * 从静止状态变为拖拽时调用
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

    }

    /**
     * 操作某个item和动画完成时调用
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        int position = viewHolder.getAdapterPosition();
        itemTouchAdapter.onClearView(position);
    }

    /**
     * 实现自定义交互或动画
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public interface ItemTouchAdapterInterface {
        void onMove(int fromPosition, int toPosition);
        void onSwiped(int position);
        void onClearView(int position);
    }

    public void setEditMode(int editMode){
        this.editMode = editMode;
    }
}
