package pro.yueyuan.project_t.home.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.zaaach.citypicker.CityPickerActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pro.yueyuan.project_t.BaseFragment;
import pro.yueyuan.project_t.R;
import pro.yueyuan.project_t.data.ShowGameListBean;
import pro.yueyuan.project_t.home.IHomeContract;

import static dagger.internal.Preconditions.checkNotNull;

/**
 * Created by Key on 2017/2/17 20:17
 * email: MrKey.K@gmail.com
 * description:
 */

public class HomeFragment extends BaseFragment implements IHomeContract.View {
    private static final int REQUEST_CODE_PICK_CITY = 233;

    private List<ShowGameListBean.DataBean> mGameListDatas;

    public PopupWindow popupWindow;

    public BottomNavigationView bottomNavigationView;

    @BindView(R.id.tv_home_cityname_fmt)
    TextView tv_home_cityname_fmt;
    @BindView(R.id.ll_home_chosecity_fmt)
    LinearLayout ll_home_chosecity_fmt;
    @BindView(R.id.tv_home_select_fmt)
    TextView tv_home_select_fmt;
    @BindView(R.id.iv_home_addroom_fmt)
    ImageView iv_home_addroom_fmt;


    /**
     * 创建事务管理器
     */
    FragmentTransaction transaction;
    HomeActivity meActivity;
    /**
     * 通过重写第一级基类IBaseView接口的setPresenter()赋值
     */
    private IHomeContract.Presenter mPresenter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
//        mPresenter.start();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }



    /**
     * @return 布局文件ID
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_homebak;
    }
    /**
     * 点击时间的处理
     */
    @OnClick({
            R.id.ll_home_chosecity_fmt,
            R.id.tv_home_select_fmt,
            R.id.iv_home_addroom_fmt
    })
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_home_chosecity_fmt:
                startActivityForResult(new Intent(getActivity(), CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
                break;
            case R.id.tv_home_select_fmt:
                mPresenter.loadGameList("secret","app.yueyuan.pro");
                break;
            case R.id.iv_home_addroom_fmt:
                transaction.replace(R.id.fl_content_home_activity,meActivity.mFragmentList.get(1));
                // 然后将该事务添加到返回堆栈，以便用户可以向后导航
                transaction.addToBackStack(null);
                // 执行事务
                transaction.commit();
                break;
        }
    }

    /**
     * 重写onActivityResult
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == getActivity().RESULT_OK){
            if (data != null){
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                tv_home_cityname_fmt.setText(city);
            }
        }
    }

    /**
     * TODO 初始化布局文件
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(Bundle savedInstanceState) {
        /**
         * 获取当前activity
         */
        meActivity = (HomeActivity) getActivity();
        transaction = meActivity.getSupportFragmentManager().beginTransaction();
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.navigation_bottom);
        if (bottomNavigationView.getVisibility() == View.GONE) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
       /* loginQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QQlogin();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });*/

    }



    @Override
    public void setPresenter(@NonNull IHomeContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    /**
     * 显示我的头像
     */
    @Override
    public void showMyAvatar() {
       /* Glide.with(this)
                .load("http://oss.yueyuan.pro/user/888888/1111.jpg?x-oss-process=image/resize,m_lfit,w_100,h_100")
                // 圆形裁剪
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(iv_home_fragment);*/
    }

    @Override
    public void initGameList(List<ShowGameListBean.DataBean> data) {
        mGameListDatas = data;
        initPopupWindow();
        Logger.e(mGameListDatas.size()+"");
    }

    /*@OnClick(R.id.b_home_fragment)
    public void onClick() {
        mPresenter.loadMyAvatar();
    }*/




    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     */
    class popupDismissListener implements PopupWindow.OnDismissListener{
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }
    protected void initPopupWindow(){
        View popupWindowView = getActivity().getLayoutInflater().inflate(R.layout.firstchose, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        RecyclerView mRecycle = (RecyclerView) popupWindowView.findViewById(R.id.rcv_pop_item);
        mRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycle.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        if(mGameListDatas != null && mGameListDatas.size()>0){
            Logger.e(mGameListDatas.size()+"");
            mRecycle.setAdapter(new CommonAdapter<ShowGameListBean.DataBean>(getContext(),R.layout.item_firsetchose,mGameListDatas) {
                @Override
                protected void convert(ViewHolder holder, ShowGameListBean.DataBean dataBean, int position) {
                    holder.itemView.setTag(dataBean.getName());
                    holder.setText(R.id.tv_item_gamename,dataBean.getName());
                }
                @Override
                public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
                    super.setOnItemClickListener(onItemClickListener);
                    Logger.d("setOnItemClickListener: " + getTag());
                }
            });
        }



        //动画效果
        popupWindow.setAnimationStyle(R.style.AnimationRightFade);

        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //宽度
        //popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
        //高度
        //popupWindow.setHeight(LayoutParams.FILL_PARENT);
        //显示位置
        popupWindow.showAtLocation(getActivity().getLayoutInflater().inflate(R.layout.activity_home, null), Gravity.RIGHT, 0, 1000);
        //设置背景半透明
        backgroundAlpha(0.5f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return true;
            }
        });

        Button open = (Button)popupWindowView.findViewById(R.id.cancel);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Open", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
}
