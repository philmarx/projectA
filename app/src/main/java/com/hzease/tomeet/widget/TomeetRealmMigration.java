package com.hzease.tomeet.widget;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by Key on 2017/8/9 06:50
 * email: MrKey.K@gmail.com
 * description:
 */

public class TomeetRealmMigration implements RealmMigration {
    /**
     * This method will be called if a migration is needed. The entire method is wrapped in a
     * write transaction so it is possible to create, update or delete any existing objects
     * without wrapping it in your own transaction.
     *
     * @param realm      the Realm schema on which to perform the migration.
     * @param oldVersion the schema version of the Realm at the start of the migration.
     * @param newVersion the schema version of the Realm after executing the migration.
     */
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {
            schema.get("RealmFriendBean").addField("vip", boolean.class);
            oldVersion++;
        }
        if (oldVersion == 2) {
            schema.get("RealmFriendBean").addField("isChoose", boolean.class);
            oldVersion++;
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof TomeetRealmMigration;
    }
}
