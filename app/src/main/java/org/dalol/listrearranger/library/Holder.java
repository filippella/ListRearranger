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

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.dalol.listrearranger.R;

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 1/26/2016
 */
public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private HolderClickListener mListener;
    private Runnable mCallback;
    private Handler mHandler;

    public Holder(View itemView, HolderClickListener listener) {
        super(itemView);
        mListener = listener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        mHandler = new Handler();

        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:

                        mCallback = new Runnable() {

                            @Override
                            public void run() {
                                mListener.onLongClickListener(R.id.move_handle);
                            }
                        };
                        mHandler.postDelayed(mCallback, 60);

                        break;

                    case MotionEvent.ACTION_UP:
                        mHandler.removeCallbacks(mCallback);
                        mListener.onLongClickListener(-1);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

        Toast.makeText(v.getContext(), "You clicked on a row at position " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onLongClick(View v) {

        return true;
    }
}
