package br.com.silver.dybapp;

import android.app.Application;

import br.com.silver.dybapp.utils.RealmMigrations;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        final RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("silver.realm")
                .schemaVersion(3)
                .migration(new RealmMigrations())
                .build();
        Realm.setDefaultConfiguration(configuration);
        Realm.getInstance(configuration);
    }

}
