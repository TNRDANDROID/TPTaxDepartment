package com.nic.TPTaxDepartment.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TPTaxDepartment";
    private static final int DATABASE_VERSION = 1;


    public static final String SAVE_TRADE_IMAGE = "trade_image";
    public static final String CAPTURED_PHOTO = "captured_photo";
    public static final String SAVE_FIELD_VISIT = "field_visit";


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
                "screen_status TEXT," +
                "loc_lat TEXT," +
                "loc_long TEXT)");

        db.execSQL("CREATE TABLE "+ CAPTURED_PHOTO + "("
                + "id INTEGER," +
                "loc_lat TEXT," +
                "loc_long TEXT," +
                "taxtypeid TEXT," +
                "field_image blob,"+
                "pending_flag INTEGER,"+
                "description TEXT)");

        db.execSQL("CREATE TABLE " + SAVE_FIELD_VISIT + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "taxtypeid TEXT," +
                "assessment_id TEXT," +
                "build_type TEXT," +
                "current_status TEXT," +
                "remarks TEXT," +
                "delete_flag TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_TRADE_IMAGE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_FIELD_VISIT);
            db.execSQL("DROP TABLE IF EXISTS " + CAPTURED_PHOTO);

            onCreate(db);
        }
    }


}
