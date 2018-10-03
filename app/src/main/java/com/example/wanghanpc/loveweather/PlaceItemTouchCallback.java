package com.example.wanghanpc.loveweather;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class PlaceItemTouchCallback extends ItemTouchHelper.Callback {

    private static final int PLACE_EDIT_CHECK = 0;
    private int editMode = PLACE_EDIT_CHECK;

    private ItemTouchAdapter itemTouchAdapter;
    public PlaceItemTouchCallback(ItemTouchAdapter itemTouchAdapter) {
        this.itemTouchAdapter = itemTouchAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = 0,swipeFlags = 0;
        if (editMode == 1){
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
        }
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        itemTouchAdapter.onMove(fromPosition,toPosition);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        itemTouchAdapter.onSwiped(position);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
//        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG){
////            viewHolder.itemView.getBackground().setAlpha(100);
//        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
//        viewHolder.itemView.getBackground().setAlpha(255);
    }

    public interface ItemTouchAdapter {
        void onMove(int fromPosition, int toPosition);
        void onSwiped(int position);
        void saveWeatherList();
    }

    public void setEditMode(int editMode){
        this.editMode = editMode;
    }
}
