package com.hzease.tomeet.chat.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hzease.tomeet.BaseFragment;
import com.hzease.tomeet.PTApplication;
import com.hzease.tomeet.R;
import com.hzease.tomeet.data.PhoneContactBean;
import com.hzease.tomeet.utils.ChineseToEnglish;
import com.hzease.tomeet.utils.ToastUtils;
import com.hzease.tomeet.widget.adapters.AddPhoneContactAdapter;
import com.orhanobut.logger.Logger;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Key on 2017/3/24 17:10
 * email: MrKey.K@gmail.com
 * description:
 */

public class AddPhoneContactsFragment extends BaseFragment {

    @BindView(R.id.rl_load_View)
    RelativeLayout rl_load_View;
    @BindView(R.id.lv_addPhone_contact)
    ListView lv_addPhone_contact;

    public AddPhoneContactsFragment() {
    }

    public static AddPhoneContactsFragment newInstance() {
        return new AddPhoneContactsFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_add_phone_contacts;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        try {
            Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = mContext.getContentResolver().query(contactUri,
                    new String[]{"display_name", "sort_key", "contact_id", "data1"},
                    null, null, "sort_key");
            String contactName;
            String contactNumber;
            String contactSortKey;
            final Map<String, String> phoneMap = new HashMap<>();
            int contactId;
            while (cursor.moveToNext()) {
                // 通讯录名字
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                // 通讯录号码
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace("-", "").replace(" ", "");
                if (contactNumber.startsWith("86")) {
                    contactNumber = contactNumber.replaceFirst("86", "");
                } else if (contactNumber.startsWith("+86")) {
                    contactNumber = contactNumber.replaceFirst("\\+86", "");
                } else if (contactNumber.startsWith("0086")) {
                    contactNumber = contactNumber.replaceFirst("0086", "");
                }
                //contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                // 首字母拼音
                //contactSortKey = getSortkey (cursor.getString(1));

                if (contactNumber.length() == 11 && !contactNumber.startsWith("0")) {
                    phoneMap.put(contactNumber, contactName);
                }
            }
            cursor.close();//使用完后一定要将cursor关闭，不然会造成内存泄露等问题

            //移除自己的号码
            phoneMap.remove(PTApplication.myInfomation.getData().getPhone());

            Logger.e(new JSONArray(phoneMap.keySet()).toString());

            PTApplication.getRequestService().getPhoneContactFriends(PTApplication.userToken, PTApplication.userId, new JSONArray(phoneMap.keySet()).toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterTerminate(new Action0() {
                        @Override
                        public void call() {
                            if (rl_load_View.getVisibility() == View.VISIBLE) {
                                rl_load_View.setVisibility(View.GONE);
                            }
                        }
                    })
                    .subscribe(new Subscriber<PhoneContactBean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.e(e.getMessage());
                        }

                        @Override
                        public void onNext(PhoneContactBean phoneContactBean) {
                            if (phoneContactBean.isSuccess()) {
                                for (PhoneContactBean.DataBean bean : phoneContactBean.getData()) {
                                    bean.setContactName(phoneMap.get(bean.getPhone()));
                                    String contactName = ChineseToEnglish.getFirstSpell(bean.getContactName());
                                    bean.setLetter(getSortkey(contactName));
                                }
                                // TODO: 2017/6/20 数据填充
                                initAdapter(phoneContactBean.getData());
                                Logger.e(phoneContactBean.toString());
                            } else {
                                // todo 失败占位图和提示
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            if (rl_load_View.getVisibility() == View.VISIBLE) {
                rl_load_View.setVisibility(View.GONE);
            }
            ToastUtils.getToast(mContext, "获取联系人失败");
            // TODO: 2017/6/19 放一张占位图
        }
    }

    private void initAdapter(List<PhoneContactBean.DataBean> data) {
        lv_addPhone_contact.setAdapter(new AddPhoneContactAdapter(mContext,data));
    }


    private static String getSortkey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        } else
            return "#";   //获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
    }
}
