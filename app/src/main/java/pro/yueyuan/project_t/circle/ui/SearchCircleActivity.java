package pro.yueyuan.project_t.circle.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        PTApplication.getRequestService().searchCircle(searchKeyWord,0,10)
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
                        if (searchCircleBean.getData().size()!=0){
                            reflushAdapter(searchCircleBean.getData());
                        }else{
                            Logger.e("没有数据啊啊啊啊啊啊");
                        }
                    }
                });
    }

    /**
     * 刷新listview
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
