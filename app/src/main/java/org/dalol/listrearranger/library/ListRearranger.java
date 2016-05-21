/*
 *
 *  * Copyright (c) 2016.  Filippo Engidashet <filippo.eng@gmail.com>
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to
 *  * deal in the Software without restriction, including without limitation the
 *  * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 *  * sell copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *  * IN THE SOFTWARE.
 *
 */

package org.dalol.listrearranger.library;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.dalol.listrearranger.sample.Film;

import java.util.List;

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 1/26/2016
 */
public class ListRearranger<T extends RowItem> implements HolderClickListener {

    private static final String TAG = ListRearranger.class.getSimpleName();
    private final RecyclerView mRecyclerView;
    private final List<T> mItems;
    private CreateListListener<T> mListListener;
    private ListRearrangeListener<T> mRearrangeListener;
    private ListRearrangerAdapter mRearrangerAdapter;
    private RearrangeDecorator mRearrangeDecorator;

    public ListRearranger(RecyclerView recyclerView, CreateListListener listListener, List<T> items) {
        mRecyclerView = recyclerView;
        mListListener = listListener;
        mItems = items;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
    }

    public void addIntoList(int position, T item) {
        mItems.add(position, item);
    }

    public void addIntoList(T item) {
        mItems.add(item);
    }


    public void addRearrangeListener(ListRearrangeListener<T> rearrangeListener) {
        mRearrangeListener = rearrangeListener;
    }

    public void refresh() {
        if (mRecyclerView == null) {
            new IllegalStateException("RecyclerView does not have to be null!");
        }
        mRearrangerAdapter = new ListRearrangerAdapter(mListListener, mItems, this);
        mRecyclerView.setAdapter(mRearrangerAdapter);

        mRearrangeDecorator = new RearrangeDecorator();

//        mRearrangeDecorator.setFloatingAlpha(0.6f);
//        mRearrangeDecorator.setAutoScrollSpeed(0.3f);
//        mRearrangeDecorator.setAutoScrollWindow(0.1f);

        mRearrangeDecorator.setOnItemMovedListener(new RearrangeDecorator.OnItemMovedListener() {
            @Override
            public void onItemMoved(int from, int to) {
                Log.d(TAG, "Moved from " + from + " to " + to);
            }
        });
        mRearrangeDecorator.setOnDragStateChangedListener(new RearrangeDecorator.OnDragStateChangedListener() {
            @Override
            public void onDragStart() {

            }

            @Override
            public void onDragStop() {

            }
        });

        mRecyclerView.addItemDecoration(mRearrangeDecorator);
        mRecyclerView.addOnItemTouchListener(mRearrangeDecorator);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void onLongClickListener(int resourceId) {
        mRearrangeDecorator.setViewHandleId(resourceId);
    }
}
