package com.example.car.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class CarDbHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "cars.db";
    private static int DATABASE_VERSION = 1;

    CarDbHelper(Context context) {
        super(context ,DATABASE_NAME ,null ,DATABASE_VERSION);
    }


}
