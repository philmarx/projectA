package pro.yueyuan.project_t.circle;


import java.util.List;

import pro.yueyuan.project_t.IBasePresenter;
import pro.yueyuan.project_t.IBaseView;
import pro.yueyuan.project_t.data.HomeRoomsBean;
import pro.yueyuan.project_t.data.ShowGameListBean;

/**
 * Created by Key on 2016/11/25 01:13
 * email: MrKey.K@gmail.com
 * description: 用来写View接口和Presenter接口的契约
 */

public interface ICircleContract {

    interface View extends IBaseView<Presenter> {


    }

    interface Presenter extends IBasePresenter {

    }
}
