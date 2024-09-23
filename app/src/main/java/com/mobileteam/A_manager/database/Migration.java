package com.mobileteam.A_manager.database;

import java.util.UUID;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if(oldVersion == 0){
            RealmObjectSchema mStudentSchema = schema.get("Student");
            mStudentSchema.addField("std_id", Integer.class, FieldAttribute.REQUIRED).transform(new RealmObjectSchema.Function() {
                @Override
                public void apply(DynamicRealmObject obj) {
                    obj.set("std_id", UUID.randomUUID().toString());
                }
            });
        }
        oldVersion++;
    }
}
