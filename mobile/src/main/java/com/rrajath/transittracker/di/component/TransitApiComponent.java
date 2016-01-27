package com.rrajath.transittracker.di.component;

import com.rrajath.transittracker.di.module.TransitApiModule;
import com.rrajath.transittracker.di.scope.UserScope;
import com.rrajath.transittracker.network.StopsManager;
import com.rrajath.transittracker.network.interfaces.TransitApiService;

import dagger.Subcomponent;

@UserScope
@Subcomponent (
        modules = {
                TransitApiModule.class
        }
)
public interface TransitApiComponent {
    StopsManager inject(StopsManager stopsManager);

    TransitApiService service();
}
