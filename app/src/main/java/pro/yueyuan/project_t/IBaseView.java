package pro.yueyuan.project_t;

import pro.yueyuan.project_t.data.RankingBean;

/**
 * Created by Key on 2016/11/14.
 */

public interface IBaseView<T> {

    //把p存进v，或者说把p注入v
    void setPresenter(T presenter);

}
