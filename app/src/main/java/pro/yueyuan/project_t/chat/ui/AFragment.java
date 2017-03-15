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

public class AFragment extends Fragment {
    public static AFragment instance = null;
    public  static AFragment getInstance(){
        if (instance == null){
            instance = new AFragment();
        }
        return instance;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setText("非常喜欢");
        return tv;
    }
}
