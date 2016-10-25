package com.xy.hashmaker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xy.hashmaker.BuildConfig;
import com.xy.hashmaker.R;
import com.xy.hashmaker.activities.base.BaseActivity;
import com.xy.hashmaker.algorithm.Converter;
import com.xy.hashmaker.algorithm.HashMaker;
import com.xy.hashmaker.constants.ExtraKey;

import java.util.ArrayList;

import static com.xy.hashmaker.constants.ExtraKey.EXTRA_KEY_CONTENT;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Converter converter = new Converter();
    private HashMaker hashMaker = new HashMaker();

    private ViewGroup mainLayout;
    private Toolbar toolbar;
    private ProgressBar progressBar;

    private EditText contentEdit;
    private Button clearButton;
    private Button sendButton;

    private HashMakerHandler hashMakerHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void findUI() {
        this.mainLayout = (ViewGroup) this.findViewById(R.id.content_main);
        this.toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.progressBar = (ProgressBar) this.findViewById(R.id.progress_bar);

        this.contentEdit = (EditText) this.findViewById(R.id.edit_content);
        this.clearButton = (Button) this.findViewById(R.id.button_clear);
        this.sendButton = (Button) this.findViewById(R.id.button_send);
    }

    @Override
    protected void initUI() {
        this.toolbar.setLogo(R.drawable.pic_sharp);
        this.toolbar.setTitle(this.getTitle() + " " + "v" + BuildConfig.VERSION_NAME);
        this.toolbar.setTitleTextColor(this.getResources().getColor(R.color.color_general_white_FFFFFF));
        this.setSupportActionBar(toolbar);
    }

    @Override
    protected void registerEvent() {
        this.clearButton.setOnClickListener(this);
        this.sendButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_clear:
                this.contentEdit.clearFocus();
                this.contentEdit.setText("");
                this.mainLayout.requestFocus();
                break;

            case R.id.button_send:
                String currentContent = this.contentEdit.getText().toString();

                if (TextUtils.isEmpty(currentContent)) {
                    Toast.makeText(this, this.getString(R.string.custom_string_toast_empty_message), Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);

                    String convertedContent = this.converter.convertToSimplifiedChinese(currentContent);
                    this.hashMakerHandler = new HashMakerHandler();
                    HashMakerThread hashMakerThread = new HashMakerThread(convertedContent);
                    hashMakerThread.start();
                }
                break;
        }
    }

    // Inner Classes
    private class HashMakerHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            progressBar.setVisibility(View.GONE);

            Bundle bundle = msg.getData();
            ArrayList<String> hashtagResult = bundle.getStringArrayList(ExtraKey.EXTRA_KEY_CONTENT);

            if (hashtagResult.size() > 0) {
                Intent intent = new Intent(MainActivity.this, HashtagResultActivity.class);
                intent.putExtra(EXTRA_KEY_CONTENT, hashtagResult);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, getString(R.string.custom_string_toast_short_message), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class HashMakerThread extends Thread {
        private String convertedContent;

        public HashMakerThread(String convertedContent) {
            this.convertedContent = convertedContent;
        }

        @Override
        public void run() {
            super.run();

            ArrayList<String> hashtagResultList = (ArrayList<String>) hashMaker.makeHashTag(this.convertedContent);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(ExtraKey.EXTRA_KEY_CONTENT, hashtagResultList);
            message.setData(bundle);

            MainActivity.this.hashMakerHandler.sendMessage(message);
        }
    }
}
