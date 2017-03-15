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

public class BFragment extends Fragment {
    public static BFragment instance = null;
    public  static BFragment getInstance(){
        if (instance == null){
            instance = new BFragment();
        }
        return instance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setText("有好感");
        return tv;
    }
}
