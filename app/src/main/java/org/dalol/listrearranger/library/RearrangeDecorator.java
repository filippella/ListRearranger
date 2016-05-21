/*
 * Copyright (c) 2016.  Filippo Engidashet <filippo.eng@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package org.dalol.listrearranger.library;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 1/26/2016
 */
public class RearrangeDecorator extends RecyclerView.ItemDecoration implements RecyclerView.OnItemTouchListener {

    private int mDragHandleWidth = 0;

    private int mSelectedDragItemPos = -1;

    private int mFingerAnchorY;

    private int mFingerY;

    private int mFingerOffsetInViewY;

    private BitmapDrawable mFloatingItem;

    private Rect mFloatingItemStatingBounds;

    private Rect mFloatingItemBounds;

    private int mViewHandleId = -1;

    private OnItemMovedListener mMoveInterface;

    private boolean mIsDragging;

    @Nullable
    private OnDragStateChangedListener mDragStateChangedListener;


    public interface OnItemMovedListener {
        void onItemMoved(int from, int to);
    }

    public interface OnDragStateChangedListener {
        void onDragStart();

        void onDragStop();
    }

    public void setOnItemMovedListener(OnItemMovedListener mi) {
        mMoveInterface = mi;
    }

    public void setViewHandleId(int id) {
        mViewHandleId = id;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView rv, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, rv, state);

        if (mSelectedDragItemPos != -1) {
            int itemPos = rv.getChildPosition(view);

            if (itemPos == mSelectedDragItemPos) {
                view.setVisibility(View.INVISIBLE);
            } else {
                view.setVisibility(View.VISIBLE);
                float floatMiddleY = mFloatingItemBounds.top + mFloatingItemBounds.height() / 2;

                if ((itemPos > mSelectedDragItemPos) && (view.getTop() < floatMiddleY)) {
                    float amountUp = (floatMiddleY - view.getTop()) / (float) view.getHeight();
                    if (amountUp > 1)
                        amountUp = 1;

                    outRect.top = -(int) (mFloatingItemBounds.height() * amountUp);
                    outRect.bottom = (int) (mFloatingItemBounds.height() * amountUp);
                }
                if ((itemPos < mSelectedDragItemPos) && (view.getBottom() > floatMiddleY)) {
                    float amountDown = ((float) view.getBottom() - floatMiddleY) / (float) view.getHeight();
                    if (amountDown > 1)
                        amountDown = 1;

                    outRect.top = (int) (mFloatingItemBounds.height() * amountDown);
                    outRect.bottom = -(int) (mFloatingItemBounds.height() * amountDown);
                }
            }
        } else {
            outRect.top = 0;
            outRect.bottom = 0;
            view.setVisibility(View.VISIBLE);
        }
    }

    private int getNewPosition(RecyclerView rv) {
        int itemsOnScreen = rv.getLayoutManager().getChildCount();

        float floatMiddleY = mFloatingItemBounds.top + mFloatingItemBounds.height() / 2;
        int above = 0;
        int below = Integer.MAX_VALUE;
        for (int n = 0; n < itemsOnScreen; n++) {
            if (n != 0) {
                View view = rv.getLayoutManager().getChildAt(n);
                if (view.getVisibility() != View.VISIBLE)
                    continue;
                int itemPos = rv.getChildPosition(view);
                if (itemPos == mSelectedDragItemPos)
                    continue;
                float viewMiddleY = view.getTop() + view.getHeight() / 2;
                if (floatMiddleY > viewMiddleY) {
                    if (itemPos > above)
                        above = itemPos;
                } else if (floatMiddleY <= viewMiddleY) {
                    if (itemPos < below)
                        below = itemPos;
                }
            }
        }

        if (below != Integer.MAX_VALUE) {
            if (below < mSelectedDragItemPos)
                below++;
            return below - 1;
        } else {
            if (above < mSelectedDragItemPos)
                above++;
            return above;
        }
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        if (e.getAction() == MotionEvent.ACTION_UP) {
            mViewHandleId = -1;
        }
        View itemView = rv.findChildViewUnder(e.getX(), e.getY());

        if (itemView == null)
            return false;
        boolean dragging = false;
        if ((mDragHandleWidth > 0) && (e.getX() < mDragHandleWidth)) {
            dragging = true;
        } else if (mViewHandleId != -1) {
            View handleView = itemView.findViewById(mViewHandleId);
            if (handleView == null) {
                return false;
            }
            if (handleView.getVisibility() != View.VISIBLE) {
                return false;
            }
            int[] parentItemPos = new int[2];
            itemView.getLocationInWindow(parentItemPos);

            int[] handlePos = new int[2];
            handleView.getLocationInWindow(handlePos);

            int xRel = handlePos[0] - parentItemPos[0];
            int yRel = handlePos[1] - parentItemPos[1];

            Rect touchBounds = new Rect(itemView.getLeft() + xRel, itemView.getTop() + yRel,
                    itemView.getLeft() + xRel + handleView.getWidth(),
                    itemView.getTop() + yRel + handleView.getHeight()
            );
            if (touchBounds.contains((int) e.getX(), (int) e.getY()))
                dragging = true;
        }

        if (dragging) {
            setIsDragging(true);
            mFloatingItem = createFloatingBitmap(itemView);
            mFingerAnchorY = (int) e.getY();
            mFingerOffsetInViewY = mFingerAnchorY - itemView.getTop();
            mFingerY = mFingerAnchorY;
            mSelectedDragItemPos = rv.getChildPosition(itemView);
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        if (e.getAction() == MotionEvent.ACTION_UP) {
            mViewHandleId = -1;
        }
        if ((e.getAction() == MotionEvent.ACTION_UP) || (e.getAction() == MotionEvent.ACTION_CANCEL)) {
            if ((e.getAction() == MotionEvent.ACTION_UP) && mSelectedDragItemPos != -1) {
                int newPos = getNewPosition(rv);
                if (mMoveInterface != null) {
                    mMoveInterface.onItemMoved(mSelectedDragItemPos, newPos);
                }
            }
            setIsDragging(false);
            mSelectedDragItemPos = -1;
            mFloatingItem = null;
            rv.invalidateItemDecorations();
            return;
        }
        mFingerY = (int) e.getY();
        if (mFloatingItem != null) {
            mFloatingItemBounds.top = mFingerY - mFingerOffsetInViewY;
            if (mFloatingItemBounds.top < -mFloatingItemStatingBounds.height() / 2) {
                mFloatingItemBounds.top = -mFloatingItemStatingBounds.height() / 2;
            }
            mFloatingItemBounds.bottom = mFloatingItemBounds.top + mFloatingItemStatingBounds.height();
            mFloatingItem.setBounds(mFloatingItemBounds);
        }
        float scrollAmount = 0;
        if (mFingerY > (rv.getHeight() * (1 - 0.2f))) {
            scrollAmount = (mFingerY - (rv.getHeight() * (1 - 0.2f)));
        } else if (mFingerY < (rv.getHeight() * 0.2f)) {
            scrollAmount = (mFingerY - (rv.getHeight() * 0.2f));
        }
        scrollAmount *= 0.2f;
        rv.scrollBy(0, (int) scrollAmount);
        rv.invalidateItemDecorations();
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private void setIsDragging(final boolean dragging) {
        if (dragging != mIsDragging) {
            mIsDragging = dragging;
            if (mDragStateChangedListener != null) {
                if (mIsDragging) {
                    mDragStateChangedListener.onDragStart();
                } else {
                    mDragStateChangedListener.onDragStop();
                }
            }
        }
    }

    public void setOnDragStateChangedListener(final OnDragStateChangedListener dragStateChangedListener) {
        this.mDragStateChangedListener = dragStateChangedListener;
    }


    Paint bgColor = new Paint();

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mFloatingItem != null) {
            bgColor.setShadowLayer(4f, 2f, 2f, Color.BLACK);
            bgColor.setColor(Color.RED);
            bgColor.setStrokeWidth(1.5f);
            bgColor.setStyle(Paint.Style.FILL_AND_STROKE);
            c.drawRect(mFloatingItemBounds, bgColor);
            mFloatingItem.draw(c);
        }
    }

    private BitmapDrawable createFloatingBitmap(View v) {
        mFloatingItemStatingBounds = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        mFloatingItemBounds = new Rect(mFloatingItemStatingBounds);

        Bitmap bitmap = Bitmap.createBitmap(mFloatingItemStatingBounds.width(), mFloatingItemStatingBounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        BitmapDrawable retDrawable = new BitmapDrawable(v.getResources(), bitmap);
        retDrawable.setBounds(mFloatingItemBounds);
        return retDrawable;
    }
}
