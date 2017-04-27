package com.hzease.tomeet.circle;


import dagger.Component;
import com.hzease.tomeet.circle.ui.CircleActivity;
import com.hzease.tomeet.data.source.IPTRepositoryComponent;
import com.hzease.tomeet.utils.FragmentScoped;

/**
 * Created by Key on 2016/12/3 16:31
 * email: MrKey.K@gmail.com
 * description:
 */
@FragmentScoped
@Component(dependencies = IPTRepositoryComponent.class, modules = CirclePresenterModule.class)
public interface ICircleComponent {

    void inject(CircleActivity circleActivity);

}
