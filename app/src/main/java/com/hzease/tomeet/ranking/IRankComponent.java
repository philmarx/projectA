package com.hzease.tomeet.ranking;


import dagger.Component;
import com.hzease.tomeet.data.source.IPTRepositoryComponent;
import com.hzease.tomeet.ranking.ui.RankingActivity;
import com.hzease.tomeet.utils.FragmentScoped;

/**
 * Created by Key on 2016/12/3 16:31
 * email: MrKey.K@gmail.com
 * description:
 */
@FragmentScoped
@Component(dependencies = IPTRepositoryComponent.class, modules = RankPresenterModule.class)
public interface IRankComponent {

    void inject(RankingActivity rankActivity);

}
