package com.example.sherif.skullmp3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Sherif on 9/21/2017.
 */
public class RecyclerDivider extends RecyclerView.ItemDecoration {
    private int mOrientation;
    private Drawable mDivider;


    public RecyclerDivider(Context context, int orientation) {
        mDivider= ContextCompat.getDrawable(context, R.drawable.recycler_divider);
        if(orientation!= LinearLayoutManager.VERTICAL){
            throw new IllegalArgumentException("This class can only be used in the case of a vertical linear orientation");
        }
        mOrientation=orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if(mOrientation==LinearLayoutManager.VERTICAL){
            drawHorizontalDivider(c,parent,state);
        }
    }

    private void drawHorizontalDivider(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left,top,right,bottom;
        left=parent.getPaddingLeft();
        right=parent.getWidth()-parent.getPaddingRight();
        int itemCount=parent.getChildCount();

        for(int i=0;i<itemCount;i++){
            View v=parent.getChildAt(i);
                RecyclerView.LayoutParams mParams= (RecyclerView.LayoutParams) v.getLayoutParams();
                top=v.getTop()-mParams.topMargin;
                bottom=top+mDivider.getIntrinsicHeight();
                mDivider.setBounds(left,top,right,bottom);
                mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }

}
