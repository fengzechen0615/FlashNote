package com.example.wuke.flashnote.friends;

import android.support.v7.widget.RecyclerView;

public interface ItemTouchHelperAdapterFriend {
    // 数据交换
    void onItemMove(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target);

    // 数据删除
    void onItemDissmiss(int position_swipe);

    // drag或者swipe选中
    void onItemSelect(RecyclerView.ViewHolder source);

    // 状态清除
    void onItemClear(RecyclerView.ViewHolder source);

}
