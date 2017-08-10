package com.hzease.tomeet.circle.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.SearchCircleBean;
import com.hzease.tomeet.widget.adapters.SearchCircleAdapter;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchCircleActivity extends NetActivity {
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.search_circle)
    AutoLinearLayout searchCircle;
    @BindView(R.id.lv_circle_search_act)
    ListView lv_circle_search_act;

    String searchKeyWord;
    @BindView(R.id.bt_circle_create_aty)
    Button bt_circle_create_aty;
    @BindView(R.id.all_circle_nosearch_aty)
    AutoLinearLayout all_circle_nosearch_aty;


    @OnClick({
            R.id.bt_circle_create_aty
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_circle_create_aty:
                Intent intent = new Intent();
                intent.putExtra("temp",4);
                this.setResult(0,intent);
                finish();
                break;
        }
    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_searchcircle;
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Timer timer = new Timer(); //设置定时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() { //弹出软键盘的代码
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(et_search, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 300); //设置300毫秒的时长
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                searchKeyWord = et_search.getText().toString().trim();
                if (!(searchKeyWord.isEmpty())) {
                    loadSearchInfo(searchKeyWord);
                }
            }
        });
    }

    private void loadSearchInfo(String searchKeyWord) {
        PTApplication.getRequestService().searchCircle(searchKeyWord, 0, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SearchCircleBean>() {
                    @Override
                    public void onCompleted() {
                        Logger.e("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(SearchCircleBean searchCircleBean) {
                        if (searchCircleBean.getData().size() != 0) {
                            reflushAdapter(searchCircleBean.getData());
                        } else {
                            all_circle_nosearch_aty.setVisibility(View.VISIBLE);
                            lv_circle_search_act.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * 刷新listview
     *
     * @param data
     */
    private void reflushAdapter(final List<SearchCircleBean.DataBean> data) {
        SearchCircleAdapter adapter = new SearchCircleAdapter(data);
        lv_circle_search_act.setAdapter(adapter);
        lv_circle_search_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchCircleActivity.this, CircleInfoActivity.class);
                intent.putExtra("circleId", data.get(position).getId());
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * TODO 调用 mRequestService 获取网络参数去初始化布局
     *
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {
    }
}
