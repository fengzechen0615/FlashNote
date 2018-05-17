package com.example.wuke.flashnote.recyclerview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.example.wuke.flashnote.R;

public class RecycleItemTouchHelper extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mAdapter;

    private int SBposition;

    public RecycleItemTouchHelper(ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 上下滑动
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        int swipeFlags = ItemTouchHelper.LEFT;
//        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;//长按启用拖拽
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false; //启用拖拽删除
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(source, target);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // 移动删除回调
//        mAdapter.onItemDissmiss(viewHolder.getAdapterPosition());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            // 当滑动或者拖拽view的时候通过接口返回该ViewHolder
            mAdapter.onItemSelect(viewHolder);
        }
        if (actionState != ItemTouchHelper.ACTION_STATE_DRAG && actionState != ItemTouchHelper.ACTION_STATE_SWIPE) {
            mAdapter.onUpdate();
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        mAdapter.onItemClear(viewHolder);
    }

//    @Override
//    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            //滑动时改变Item的透明度
//            final float alpha = 1 - Math.abs(dX) / (float)viewHolder.itemView.getWidth();
//            viewHolder.itemView.setAlpha(alpha);
//            viewHolder.itemView.setTranslationX(dX);
//        }
//
//    }

}
