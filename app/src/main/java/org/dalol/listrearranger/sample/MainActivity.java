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

package org.dalol.listrearranger.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.dalol.listrearranger.R;
import org.dalol.listrearranger.library.CreateListListener;
import org.dalol.listrearranger.library.Helper;
import org.dalol.listrearranger.library.ListRearrangeListener;
import org.dalol.listrearranger.library.ListRearranger;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        CreateListListener<Film> createListListener = new CreateListListener<Film>() {
            @Override
            public View onCreateRowItem(ViewGroup parent, int viewType) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
            }

            @Override
            public void onBindRowItem(View itemView, Film film, int position) {
                Log.d(TAG, "Film Position -> " + position + " || Film Title -> " + film.mFilmTitle);
                TextView filmTitle = (TextView) itemView.findViewById(R.id.film_title);
                filmTitle.setText(film.mFilmTitle);
            }
        };

        ListRearranger listRearranger = new ListRearranger(mRecyclerView, createListListener, Helper.generateFilmList());
        listRearranger.addIntoList(new Film());
        listRearranger.addIntoList(3, new Film());

        ListRearrangeListener<Film> filmRearrangerListener = new ListRearrangeListener<Film>() {
            @Override
            public void onRowClick(View view, int position) {
                Log.d(TAG, "Row clicked on Position " + position);
            }

            @Override
            public void onRowLongClick(View view, int position) {

            }

            @Override
            public void onRowAddRemoveFavourite(View view, int oldPosition) {

            }

            @Override
            public void onStartRearranging(View view, int position) {

            }

            @Override
            public void onRowRearrangeProgress(int currentPosition, int oldPosition) {

            }

            @Override
            public void onRowRearrangeCompleted(View view, int newPosition, int oldPostion, List<Film> itemList) {

            }
        };

        listRearranger.addRearrangeListener(filmRearrangerListener);
        listRearranger.refresh();

    }
}
