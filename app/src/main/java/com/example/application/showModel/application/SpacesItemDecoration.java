package com.example.application.showModel.application;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        // 如果想在每行的第一个item上方也添加间距，可以添加以下代码
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = space;
        }
    }
}
