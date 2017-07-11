package com.hzease.tomeet.widget.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.NoDataBean;
import com.hzease.tomeet.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xuq on 2017/6/28.
 */

public class ClearLabelsAdapter extends BaseAdapter {
    private int mResources[] = {R.drawable.flowlayout_one, R.drawable.flowlayout_two, R.drawable.flowlayout_three, R.drawable.flowlayout_four, R.drawable.flowlayout_five};
    List<String> labels = PTApplication.myInfomation.getData().getLabels();
    Context context;
    Activity activity;
    private PopupWindow popupWindow;

    public ClearLabelsAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Object getItem(int position) {
        return labels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context,R.layout.item_clearlabels,null);
            viewHolder.labels = (TextView) convertView.findViewById(R.id.tv_clearlabels_labels_item);
            viewHolder.clear = (Button) convertView.findViewById(R.id.tv_clearlabels_clear_item);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.labels.setText(labels.get(position));
        viewHolder.labels.setBackgroundResource(mResources[position % 5]);
        viewHolder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupClear(v,labels.get(position));
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView labels;
        Button clear;
    }

    private void initPopupClear(View v, final String label) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_isclear, null);
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置PopupWindow以外部分的背景颜色  有一种变暗的效果
        final WindowManager.LayoutParams wlBackground = activity.getWindow().getAttributes();
        wlBackground.alpha = 0.5f;      // 0.0 完全不透明,1.0完全透明
        activity.getWindow().setAttributes(wlBackground);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // 当PopupWindow消失时,恢复其为原来的颜色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                wlBackground.alpha = 1.0f;
                activity.getWindow().setAttributes(wlBackground);
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                labels.remove(label);
                notifyDataSetChanged();
            }
        });
        Button clear = (Button) contentView.findViewById(R.id.bt_clearlabels_clear_fmt);
        Button cancel = (Button) contentView.findViewById(R.id.bt_clearlabels_cancel_fmt);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearLabel(label);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //设置PopupWindow进入和退出动画
        popupWindow.setAnimationStyle(R.style.anim_popup_centerbar);
        // 设置PopupWindow显示在中间
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    private void clearLabel(String label) {
        PTApplication.getRequestService().clearLabels(label,PTApplication.userToken,PTApplication.userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError" + e.getMessage());
                    }

                    @Override
                    public void onNext(NoDataBean noDataBean) {
                        if (noDataBean.isSuccess()){
                            ToastUtils.getToast(context,"消除成功");
                            popupWindow.dismiss();
                        }else{
                            ToastUtils.getToast(context,noDataBean.getMsg());
                        }
                    }
                });
    }
}
