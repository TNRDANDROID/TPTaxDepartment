package com.nic.TPTaxDepartment.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TPTaxDepartment";
    private static final int DATABASE_VERSION = 1;


    public static final String SAVE_TRADE_IMAGE = "trade_image";


    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + SAVE_TRADE_IMAGE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "tradecode INTEGER," +
                "loc_trade_image BLOB," +
                "loc_lat TEXT," +
                "loc_long TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_TRADE_IMAGE);

            onCreate(db);
        }
    }


}
