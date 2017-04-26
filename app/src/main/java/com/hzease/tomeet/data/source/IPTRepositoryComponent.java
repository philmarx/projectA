package com.hzease.tomeet.data.source;

import javax.inject.Singleton;

import dagger.Component;
import com.hzease.tomeet.PTApplicationModule;

/**
 * Created by Key on 2016/11/28 22:47
 * email: MrKey.K@gmail.com
 * description:
 */
@Singleton
@Component(modules = {PTRepositoryModule.class, PTApplicationModule.class})
public interface IPTRepositoryComponent {

    PTRepository getPTRepository();

}
