package br.com.silver.dybapp.utils;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {
            schema.create("Counter")
                    .addField("value", int.class);
        }

        if(oldVersion == 2) {
            schema.get("Counter")
                    .addField("id", int.class);
        }

        if(oldVersion == 3) {
            schema.get("Delivery")
                    .addField("imei", String.class);
        }

    }
}
