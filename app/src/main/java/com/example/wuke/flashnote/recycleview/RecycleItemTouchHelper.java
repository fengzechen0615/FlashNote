package com.example.wuke.flashnote.recycleview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class RecycleItemTouchHelper extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mAdapter;

    public RecycleItemTouchHelper(ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 上下滑动
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        int swipeFlags = ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;//长按启用拖拽
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true; //启用拖拽删除
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(source, target);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // 移动删除回调
        mAdapter.onItemDissmiss(viewHolder);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            // 当滑动或者拖拽view的时候通过接口返回该ViewHolder
            mAdapter.onItemSelect(viewHolder);
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (!recyclerView.isComputingLayout()) {
            // 当需要清除之前在onSelectedChanged或者onChildDraw,onChildDrawOver设置的状态或者动画时通过接口返回该ViewHolder
            mAdapter.onItemClear(viewHolder);
        }
    }
}
