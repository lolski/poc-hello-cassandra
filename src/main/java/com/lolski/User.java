package com.lolski;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class User {
    public static User create(String id, int age) {
        return new AutoValue_User(id, age);
    }

    public abstract String getId();
    public abstract int getAge();
}
