package pro.yueyuan.project_t.chat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by xuq on 2017/3/15.
 */

public class DFragment extends Fragment {
    public static DFragment instance = null;
    public  static DFragment getInstance(){
        if (instance == null){
            instance = new DFragment();
        }
        return instance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setText("没感觉");
        return tv;
    }
}