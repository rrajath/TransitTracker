package com.rrajath.transittracker.di.component;

import com.rrajath.transittracker.di.module.OneBusAwayApiModule;
import com.rrajath.transittracker.di.scope.UserScope;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.network.interfaces.OneBusAwayApiService;

import dagger.Subcomponent;

@UserScope
@Subcomponent (
        modules = {
                OneBusAwayApiModule.class
        }
)
public interface OneBusAwayApiComponent {
    StopsManager inject(StopsManager stopsManager);

    OneBusAwayApiService service();
}
