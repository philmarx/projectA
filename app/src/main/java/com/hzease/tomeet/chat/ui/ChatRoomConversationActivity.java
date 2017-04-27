package com.hzease.tomeet.chat.ui;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hzease.tomeet.ModitfyRoomInfoActivity;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;

/**
 * Created by xuq on 2017/3/15.
 */

public class ChatRoomConversationActivity extends FragmentActivity {
    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.icon)
    ListView icon;
    List<String> datas = new ArrayList<>();
    @BindView(R.id.roomsinfos)
    ImageView roomsinfos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroomconversation);
        ButterKnife.bind(this);
        for (int i = 0; i < 10; i++) {
            datas.add(String.valueOf(i));
        }
        icon.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return datas.size();
            }

            @Override
            public Object getItem(int position) {
                return datas.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;
                if (convertView == null) {
                    convertView = View.inflate(PTApplication.getInstance(), R.layout.item_chatroom_icon, null);
                    viewHolder = new ViewHolder();
                    viewHolder.ready = (TextView) convertView.findViewById(R.id.ready);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                if (position % 2 == 0) {
                    viewHolder.ready.setText("已准备");
                } else {
                    viewHolder.ready.setVisibility(View.GONE);
                }
                return convertView;
            }

        });
    }

    public class ViewHolder {
        TextView ready;
    }

    @OnClick({
            R.id.imageview,
            R.id.roomsinfos
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview:
                Logger.e("imageview");
                break;
            case R.id.roomsinfos:
                initPopupWindos(view);
                break;
        }
    }

    private void initPopupWindos(View v) {
        Logger.e("initPopupWindows");
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_roominfo, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        getWindow().setAttributes(wlBackground);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                getWindow().setAttributes(wlBackground);
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Button cancel = (Button) contentView.findViewById(R.id.cancelforowner);
        Button modtify = (Button) contentView.findViewById(R.id.moditfyroominfo);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        modtify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatRoomConversationActivity.this, ModitfyRoomInfoActivity.class));
            }
        });
    }
}
