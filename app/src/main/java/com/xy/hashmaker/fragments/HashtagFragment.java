package com.xy.hashmaker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xy.hashmaker.R;

/**
 * Created by Xavier Yin on 10/26/16.
 */

public class HashtagFragment extends Fragment {
    private TextView testingText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hashtag, container, false);
        this.testingText = (TextView) view.findViewById(R.id.text_testing);
        this.testingText.setText("HashtagFragment");

        return view;
    }
}
