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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 1/26/2016
 */
public class ListRearrangerAdapter<RI extends RowItem> extends RecyclerView.Adapter<Holder> {

    private final CreateListListener<RI> mListListener;
    private final HolderClickListener mHolderClickListener;
    private List<RI> mItems;

    public ListRearrangerAdapter(CreateListListener listener, List<RI> items, HolderClickListener holderClickListener) {
        mListListener = listener;
        mItems = items;
        mHolderClickListener = holderClickListener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mListListener.onCreateRowItem(parent, viewType);
        return new Holder(view, mHolderClickListener);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        mListListener.onBindRowItem(holder.itemView, mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
