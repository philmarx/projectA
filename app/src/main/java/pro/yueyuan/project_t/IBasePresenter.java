package pro.yueyuan.project_t;

/**
 * Created by Key on 2016/11/14.
 */

public interface IBasePresenter {

    // 同步生命周期，做一些初始化操作
    // 将数据有关的放入 P，将显示有关的留在 V
    // 还有一点要强调的是，P 需要绑定生命周期，即在 P 中创建同名方法，在 V 的生命周期中调用，这样保证 P 中的数据源响应生命周期，能及时更新状态。
    void start();
}
