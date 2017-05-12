package com.hzease.tomeet.circle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.NetActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.PersonOrderInfoActivity;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.CircleMemberBean;
import com.hzease.tomeet.widget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MemberListActivity extends NetActivity {

    @BindView(R.id.lv_memberlist_aty)
    ListView lv_memberlist_aty;
    @BindView(R.id.tv_memberlist_membercount)
    TextView tv_memberlist_membercount;
    private long circleId;
    private long ownerId;
    private List<CircleMemberBean.DataBean> mDatas;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void netInit(Bundle savedInstanceState) {

    }

    /**
     * @return 返回布局文件ID
     */
    @Override
    protected int getContentViewId() {
        return R.layout.activity_member_list;
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void initLayout(Bundle savedInstanceState) {
        Bundle bundle = this.getIntent().getExtras();
        circleId = bundle.getLong("circleId");
        ownerId = bundle.getLong("ownerId");
        PTApplication.getRequestService().findMembers(circleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CircleMemberBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CircleMemberBean circleMemberBean) {
                        if (circleMemberBean.isSuccess()) {
                            mDatas = circleMemberBean.getData();
                            initAdapter(circleMemberBean.getData());
                            tv_memberlist_membercount.setText("成员("+circleMemberBean.getData().size()+")");
                        }
                    }
                });
        lv_memberlist_aty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long userId = mDatas.get(position).getId();
                Intent intent = new Intent(MemberListActivity.this, PersonOrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("userId",userId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initAdapter(final List<CircleMemberBean.DataBean> data) {
        lv_memberlist_aty.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public Object getItem(int position) {
                return data.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;
                if (convertView == null){
                    convertView = View.inflate(MemberListActivity.this,R.layout.item_memberlist,null);
                    viewHolder = new ViewHolder();
                    viewHolder.icon = (CircleImageView) convertView.findViewById(R.id.civ_memberlist_icon_item);
                    viewHolder.name = (TextView) convertView.findViewById(R.id.tv_memberlist_name_item);
                    viewHolder.isAdmin = (ImageView) convertView.findViewById(R.id.iv_memberlist_isadmin_item);
                    viewHolder.level = (ImageView) convertView.findViewById(R.id.iv_memberlist_lv_item);
                    convertView.setTag(viewHolder);
                }else{
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.name.setText(data.get(position).getNickname());
                if (ownerId == data.get(position).getId()){
                    viewHolder.isAdmin.setVisibility(View.VISIBLE);
                }
                int exprences = data.get(position).getExperience();
                if (exprences>=0 && exprences <100){
                    viewHolder.level.setImageResource(R.drawable.lv_1);
                }
                if (exprences>=100 && exprences <200){
                    viewHolder.level.setImageResource(R.drawable.lv_2);
                }
                if (exprences>=200 && exprences <300){
                    viewHolder.level.setImageResource(R.drawable.lv_3);
                }
                if (exprences>=300 && exprences <400){
                    viewHolder.level.setImageResource(R.drawable.lv_4);
                }
                if (exprences>=400 && exprences <500){
                    viewHolder.level.setImageResource(R.drawable.lv_5);
                }
                Glide.with(MemberListActivity.this)
                        .load(AppConstants.YY_PT_OSS_USER_PATH + data.get(position).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                        .placeholder(R.drawable.person_default_icon)
                        .error(R.drawable.person_default_icon)
                        .bitmapTransform(new CropCircleTransformation(MemberListActivity.this))
                        .signature(new StringSignature(data.get(position).getAvatarSignature()))
                        .into(viewHolder.icon);
                return convertView;
            }
        });
    }
    class ViewHolder{
        CircleImageView icon;
        TextView name;
        ImageView isAdmin;
        ImageView level;
    }
}
