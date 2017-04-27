package com.hzease.tomeet.login;


import dagger.Component;
import com.hzease.tomeet.data.source.IPTRepositoryComponent;
import com.hzease.tomeet.login.ui.LoginActivity;
import com.hzease.tomeet.utils.FragmentScoped;

/**
 * Created by Key on 2016/12/3 16:31
 * email: MrKey.K@gmail.com
 * description:
 */
@FragmentScoped
@Component(dependencies = IPTRepositoryComponent.class, modules = LoginPresenterModule.class)
public interface ILoginComponent {

    void inject(LoginActivity loginActivity);

}
