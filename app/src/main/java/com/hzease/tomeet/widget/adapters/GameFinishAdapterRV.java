package com.hzease.tomeet.widget.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hzease.tomeet.AppConstants;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.GameFinishBean;
import com.hzease.tomeet.data.HomeActivityBean;
import com.hzease.tomeet.utils.TimeUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xuq on 2017/10/10.
 */

public class GameFinishAdapterRV extends RecyclerView.Adapter {
    /**
     * 三种类型item
     */
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    final int TYPE_3 = 2;
    private boolean isShowQRCode;
    int[] gameType = {R.drawable.one_0, R.drawable.one_1, R.drawable.one_2, R.drawable.one_3, R.drawable.one_4, R.drawable.others_icon, R.drawable.two_one1_1, R.drawable.two_one1_2, R.drawable.two_one1_3, R.drawable.two_one1_4, R.drawable.two_one1_5, R.drawable.two_one1_6,
            R.drawable.two_one2_1, R.drawable.two_one2_2, R.drawable.two_one2_3, R.drawable.two_one2_4, R.drawable.two_one2_5, R.drawable.two_one2_6,
            R.drawable.two_one3_1, R.drawable.two_one3_2, R.drawable.two_one3_3, R.drawable.two_one3_4, R.drawable.two_one3_5, R.drawable.two_one3_6, R.drawable.two_one3_7,
            R.drawable.two_one4_1, R.drawable.two_one4_2, R.drawable.two_one4_3, R.drawable.two_one4_4, R.drawable.two_one4_5};
    private List<GameFinishBean.DataBean.MembersBean> mDatas;
    private Context context;
    private GameFinishBean.DataBean.RoomBean roomData;
    private FileInputStream fis;

    public GameFinishAdapterRV(List<GameFinishBean.DataBean.MembersBean> mDatas, Context context, GameFinishBean.DataBean.RoomBean roomData) {
        this.mDatas = mDatas;
        this.context = context;
        this.roomData = roomData;
        mDatas.get(0).setShowInfo(true);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_1) {
            View view = LayoutInflater.from(context).inflate(R.layout.share_type_head, parent, false);
            return new HeadHolder(view);
        } else if (viewType == TYPE_2) {
            View view = LayoutInflater.from(context).inflate(R.layout.share_type_info, parent, false);
            return new InfoHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.share_type_foot, parent, false);
            return new FootHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof InfoHolder) {
            final InfoHolder holder1 = (InfoHolder) holder;
            //头像
           /* Glide.with(context)
                    .load(AppConstants.YY_PT_OSS_USER_PATH + mDatas.get(position - 1).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .dontAnimate()
                    //.placeholder(R.drawable.person_default_icon)
                    //.error(R.drawable.person_default_icon)
                    .signature(new StringSignature(mDatas.get(position - 1).getAvatarSignature()))
                    .into(holder1.iv_avatar);*/
            /*PTApplication.getRequestService().downloadPicFromNet(AppConstants.YY_PT_OSS_USER_PATH + mDatas.get(position - 1).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            boolean isScuccess = writeResponseBodyToDisk(response.body(), position,holder1.iv_avatar);
                            Logger.e("downloadSuccess" + isScuccess);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Logger.e("downLoadActivityPic error: " + t);
                        }
                    });*/
           /* try {
                fis = new FileInputStream("./sdcard/DCIM/Camera/" + position + ".jpg");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            //holder1.iv_avatar.setImageBitmap(BitmapFactory.decodeStream(fis));

            holder1.iv_avatar.setImageURI(AppConstants.YY_PT_OSS_USER_PATH + mDatas.get(position - 1).getId() + AppConstants.YY_PT_OSS_AVATAR_THUMBNAIL);
            //名字
            holder1.tv_name.setText(mDatas.get(position - 1).getNickname());
            //获得叶子
            holder1.tv_currency_number.setText("X" + mDatas.get(position - 1).getBadge());
            //获得分数
            holder1.tv_getpoint.setText("+" + mDatas.get(position - 1).getPoint());
            //名次
            holder1.tv_number_fmt.setText(position + "");
            //名次
            holder1.tv_room_number.setText("第" + position + "名");
            //总名次
            holder1.tv_all_number.setText("第" + mDatas.get(position - 1).getGlobalRanking() + "名");
            if (mDatas.get(position - 1).isShowInfo()) {
                holder1.ll_game_info.setVisibility(View.VISIBLE);
                holder1.iv_isshow_gameinfo.setImageResource(R.drawable.arrow_down);
            } else {
                holder1.ll_game_info.setVisibility(View.GONE);
                holder1.iv_isshow_gameinfo.setImageResource(R.drawable.arrow_up);
            }
            holder1.iv_isshow_gameinfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mDatas.get(position - 1).isShowInfo()) {
                        closeAnimator(holder1.iv_isshow_gameinfo, holder1.ll_game_info, position - 1);
                    } else {
                        openAnimator(holder1.iv_isshow_gameinfo, holder1.ll_game_info, position - 1);
                    }
                }
            });
        } else if (holder instanceof HeadHolder) {
            HeadHolder holder1 = (HeadHolder) holder;
            //房间名字
            holder1.tv_room_title.setText(roomData.getName());
            //时间
            holder1.tv_room_starttime.setText(roomData.getBeginTime());
            //活动类型
            holder1.iv_game_type.setImageResource(gameType[roomData.getGame().getId()]);
        } else {
            FootHolder holder1 = (FootHolder) holder;
            if (isShowQRCode) {
                holder1.iv_QR_Code.setVisibility(View.VISIBLE);
            } else {
                holder1.iv_QR_Code.setVisibility(View.GONE);
            }
        }
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, int position, final ImageView avatar) {
        try {
            if (!PTApplication.imageLocalCachePath.exists()) {
                PTApplication.imageLocalCachePath.mkdirs();
            }
            File futureStudioIconFile;
            futureStudioIconFile = new File("./sdcard/DCIM/Camera/" + position + ".jpg");
            /*if (futureStudioIconFile.exists()) {
                Logger.e("图片已经存在 无需下载");
                return false;
            }*/
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                Logger.e(futureStudioIconFile.getAbsolutePath());
                final Bitmap bitmap = BitmapFactory.decodeFile(futureStudioIconFile.getAbsolutePath());
                if (bitmap != null){
                    avatar.setImageBitmap(bitmap);
                }
                return true;
            } catch (IOException e) {
                Logger.e("exception" + e.getMessage());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            Logger.e("exception" + e.getMessage());
            return false;
        }
    }

    //关闭详情
    private void closeAnimator(ImageView imageView, LinearLayout linearLayout, int postion) {
        ObjectAnimator rotation;
        if (postion == 0) {
            rotation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 180f);
        } else {
            rotation = ObjectAnimator.ofFloat(imageView, "rotation", 180f, 0f);
        }
        rotation.setDuration(150);
        rotation.start();
        linearLayout.setVisibility(View.GONE);
        mDatas.get(postion).setShowInfo(false);
        //imageView.setImageResource(R.drawable.arrow_up);
    }

    //打开详情
    private void openAnimator(ImageView imageView, LinearLayout linearLayout, int postion) {
        ObjectAnimator rotation;
        if (postion == 0) {
            rotation = ObjectAnimator.ofFloat(imageView, "rotation", 180f, 0f);
        } else {
            rotation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 180f);
        }
        rotation.setDuration(150);
        rotation.start();
        linearLayout.setVisibility(View.VISIBLE);
        mDatas.get(postion).setShowInfo(true);
        //imageView.setImageResource(R.drawable.arrow_down);
    }

    @Override
    public int getItemCount() {
        return mDatas.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_1;
        } else if (position == mDatas.size() + 1) {
            return TYPE_3;
        } else {
            return TYPE_2;
        }
    }

    public class InfoHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        SimpleDraweeView iv_avatar;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_currency_number)
        TextView tv_currency_number;
        @BindView(R.id.tv_getpoint)
        TextView tv_getpoint;
        @BindView(R.id.tv_number_fmt)
        TextView tv_number_fmt;
        @BindView(R.id.iv_isshow_gameinfo)
        ImageView iv_isshow_gameinfo;
        @BindView(R.id.tv_room_number)
        TextView tv_room_number;
        @BindView(R.id.tv_all_number)
        TextView tv_all_number;
        @BindView(R.id.ll_game_info)
        LinearLayout ll_game_info;

        public InfoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FootHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_qr_code)
        ImageView iv_QR_Code;

        public FootHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class HeadHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_room_title)
        TextView tv_room_title;
        @BindView(R.id.tv_room_starttime)
        TextView tv_room_starttime;
        @BindView(R.id.iv_game_type)
        ImageView iv_game_type;

        public HeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void changeIsShowQRCode(boolean isShowQRCode) {
        this.isShowQRCode = isShowQRCode;
        notifyItemChanged(getItemCount() - 1);
    }
}
