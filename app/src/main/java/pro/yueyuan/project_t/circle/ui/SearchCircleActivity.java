package pro.yueyuan.project_t.circle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pro.yueyuan.project_t.NetActivity;
import pro.yueyuan.project_t.PTApplication;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.SearchCircleBean;
import pro.yueyuan.project_t.widget.adapters.SearchCircleAdapter;
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
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_circle_create_aty:
                Intent intent = new Intent(SearchCircleActivity.this,CircleActivity.class);
                intent.putExtra("flag",4);
                startActivity(intent);
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
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchKeyWord = et_search.getText().toString().trim();
                loadSearchInfo(searchKeyWord);
            }


            @Override
            public void afterTextChanged(Editable s) {

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
                    }
                    @Override
                    public void onError(Throwable e) {
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
    private void reflushAdapter(List<SearchCircleBean.DataBean> data) {
        SearchCircleAdapter adapter = new SearchCircleAdapter(data);
        lv_circle_search_act.setAdapter(adapter);
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
