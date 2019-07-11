package com.example.car.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.car.Car;

import java.util.ArrayList;
import java.util.List;

public class CarContract {
    public static class CarTable implements BaseColumns {
        public static final String TABLE_NAME = "car";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_MODEL = "model";
        public static final String COLUMN_COLOR = "color";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_DPI = "dpl";
    }

    private SQLiteDatabase db;
    private CarDbHelper dbHelper;
    private static CarContract instance;

    private CarContract(Context context) {
        dbHelper = new CarDbHelper(context);
    }

    public static CarContract getInstance(Context context) {
        if (instance == null) {
            synchronized (CarContract.class) {
                if (instance == null) {
                    instance = new CarContract(context);
                }
            }
        }
        return instance;
    }


    public Boolean insertCar(Car car) {
        ContentValues values = new ContentValues();
        values.put(CarTable.COLUMN_MODEL, car.getModel());
        values.put(CarTable.COLUMN_COLOR, car.getColor());
        values.put(CarTable.COLUMN_DESCRIPTION, car.getDescription());
        values.put(CarTable.COLUMN_IMAGE, car.getImage());
        values.put(CarTable.COLUMN_DPI, car.getDpl());

        long result = db.insert(CarTable.TABLE_NAME, null, values);

        return result != -1;
    }

    public Boolean updateCar(Car car) {
        ContentValues values = new ContentValues();
        values.put(CarTable.COLUMN_MODEL, car.getModel());
        values.put(CarTable.COLUMN_COLOR, car.getColor());
        values.put(CarTable.COLUMN_DESCRIPTION, car.getDescription());
        values.put(CarTable.COLUMN_IMAGE, car.getImage());
        values.put(CarTable.COLUMN_DPI, car.getDpl());

        String[] args = {String.valueOf(car.getId())};
        long result = db.update(CarTable.TABLE_NAME, values, "id=?", args);

        return result > 0;
    }

    public Boolean deleteCar(int id) {

        String[] args = {String.valueOf(id)};
        long result = db.delete(CarTable.TABLE_NAME, "id=?", args);

        return result > 0;
    }

    public Car getCar(int id) {
        Cursor cursor = db.rawQuery(" SELECT * FROM " + CarTable.TABLE_NAME + " WHERE id=? ",
                new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            int columnId = cursor.getInt(cursor.getColumnIndex(CarTable.COLUMN_ID));
            String model = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_MODEL));
            String color = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_COLOR));
            String description = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_DESCRIPTION));
            String image = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_IMAGE));
            double dpl = cursor.getDouble(cursor.getColumnIndex(CarTable.COLUMN_DPI));
            Car car = new Car(model, color, image, description, dpl);

            car.setId(columnId);
            return car;
        }
        return null;

    }

    public List<Car> getAllCars() {
        Cursor cursor = db.rawQuery(" SELECT * FROM " + CarTable.TABLE_NAME, null);
        List<Car> carList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(CarTable.COLUMN_ID));
                String model = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_COLOR));
                String description = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_DESCRIPTION));
                String image = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_IMAGE));
                double dpl = cursor.getDouble(cursor.getColumnIndex(CarTable.COLUMN_DPI));

                Car car = new Car(model, color, image, description, dpl);
                car.setId(id);
                carList.add(car);

            } while (cursor.moveToNext());
        }
        return carList;
    }

    public List<Car> getAllCars(String query) {
        Cursor cursor = db.rawQuery(" SELECT * FROM " + CarTable.TABLE_NAME  + " WHERE " +
               CarTable.COLUMN_MODEL + " =? " , new String[]{query});
        List<Car> carList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(CarTable.COLUMN_ID));
                String model = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_COLOR));
                String description = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_DESCRIPTION));
                String image = cursor.getString(cursor.getColumnIndex(CarTable.COLUMN_IMAGE));
                double dpl = cursor.getDouble(cursor.getColumnIndex(CarTable.COLUMN_DPI));

                Car car = new Car(model, color, image, description, dpl);
                car.setId(id);
                carList.add(car);

            } while (cursor.moveToNext());
        }
        return carList;
    }

    public long getCarCount() {
        return DatabaseUtils.queryNumEntries(db, CarTable.TABLE_NAME);
    }


    public void openData() {
        db = dbHelper.getWritableDatabase();
    }

    public void closeData() {
        db.close();
    }
}
