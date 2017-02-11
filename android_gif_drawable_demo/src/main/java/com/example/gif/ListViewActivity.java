package com.example.gif;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends Activity {

    private static final int NUMBER_CELLS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listview);

        List<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < NUMBER_CELLS; i++) {
            if (i >= 10 && i <= 20) {
                imageUrls.add("https://cloud.githubusercontent.com/assets/4410820/11539468/c4d62a9c-9959-11e5-908e-cf50a21ac0e9.gif");

            } else {
                imageUrls.add("http://cdn.duitang.com/uploads/item/201311/20/20131120213622_mJCUy.thumb.600_0.gif");

            }
        }

        CommonAdapter adapter = new CommonAdapter(this, imageUrls);
        ListView gridView = (ListView) findViewById(R.id.listview);
        gridView.setAdapter(adapter);
    }

}
