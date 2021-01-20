package com.nic.TPTaxDepartment.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.model.CommonModel;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TPTaxDepartment";
    private static final int DATABASE_VERSION = 1;


    public static final String SAVE_TRADE_IMAGE = "trade_image";
    public static final String CAPTURED_PHOTO = "captured_photo";
    public static final String SAVE_FIELD_VISIT = "field_visit";

    public static final String DISTRICT_TABLE_NAME="district_table";
    public static final String LOCAL_BODY_NAME="local_body_name";
    public static final String WARD_TABLE_NAME="ward_table_name";
    public static final String STREET_TABLE_NAME="street_table_name";
    public static final String SAVE_NEW_TRADE_DETAILS="save_new_trade_details";

    public static final String FIN_YEAR_LIST="fin_year_list";



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

        db.execSQL("CREATE TABLE " + DISTRICT_TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "scode INTEGER," +
                "dcode INTEGER," +
                "dname TEXT)");

        db.execSQL("CREATE TABLE " + LOCAL_BODY_NAME + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "scode INTEGER," +
                "dcode INTEGER," +
                "lpcode TEXT," +
                "lpname TEXT)");

        db.execSQL("CREATE TABLE " + WARD_TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "scode INTEGER," +
                "dcode INTEGER," +
                "lpcode INTEGER," +
                "wardcode INTEGER," +
                "wardid INTEGER,"+
                "ward_name_en TEXT ,"+
                "ward_name_ta TEXT )");

        db.execSQL("CREATE TABLE " + STREET_TABLE_NAME + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "scode INTEGER," +
                "dcode INTEGER," +
                "lpcode INTEGER," +
                "wardcode INTEGER," +
                "wardid INTEGER,"+
                "streetid INTEGER,"+
                "streetcode INTEGER,"+
                "street_name_en TEXT,"+
                "street_name_ta TEXT)");


        db.execSQL("CREATE TABLE " + SAVE_NEW_TRADE_DETAILS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tradercode INTEGER," +
                "tradedate INTEGER," +
                "licenseType TEXT," +
                "tradeDescription TEXT," +
                "traderName TEXT," +
                "traderNameTa TEXT," +
                "tradeImage BLOB," +
                "tradergender TEXT," +
                "traderage INTEGER," +
                "fathername TEXT," +
                "fathernameta TEXT," +
                "mobileno INTEGER," +
                "email TEXT," +
                "establishmentname TEXT," +
                "ward TEXT," +
                "street TEXT," +
                "doorno TEXT," +
                "licencevalidity INTEGER," +
                "loc_lat TEXT," +
                "loc_lan TEXT," +
                "paymentstatus TEXT)");

        db.execSQL("CREATE TABLE " + FIN_YEAR_LIST + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fin_yearid TEXT," +
                "fin_year TEXT," +
                "from_fin_year TEXT," +
                "from_fin_mon TEXT," +
                "to_fin_year TEXT," +
                "to_fin_mon TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_TRADE_IMAGE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_FIELD_VISIT);
            db.execSQL("DROP TABLE IF EXISTS " + CAPTURED_PHOTO);
            db.execSQL("DROP TABLE IF EXISTS " + DISTRICT_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + LOCAL_BODY_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + WARD_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + STREET_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + FIN_YEAR_LIST);


            onCreate(db);
        }
    }

    public void insertWARD(CommonModel commonModel) {
        SQLiteDatabase db = this .getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, commonModel.getD_code());
        values.put(AppConstant.STATE_CODE, commonModel.getState_code());
        values.put(AppConstant.LP_CODE,commonModel.getLocal_pan_code());
        values.put(AppConstant.WARD_CODE,commonModel.getWard_code());
        values.put(AppConstant.WARD_ID,commonModel.getWardID());
        //values.put(AppConstant.WARD_NAME,commonModel.getWard_name());
        values.put(AppConstant.WARD_NAME_EN,commonModel.getWard_name_english());
        values.put(AppConstant.WARD_NAME_TA,commonModel.getWard_name_tamil());

        long id = db.insert(DBHelper.WARD_TABLE_NAME,null,values);
        Log.d("Insert_WARD_ID", String.valueOf(id));
        //Toast.makeText(context, "Insertede Successfully", Toast.LENGTH_SHORT).show();
        //db.close();
    }

    /****** WARD  TABLE *****/
    public void insertStreet(CommonModel commonModel) {
        SQLiteDatabase db = this .getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, commonModel.getD_code());
        values.put(AppConstant.STATE_CODE, commonModel.getState_code());
        values.put(AppConstant.LP_CODE,commonModel.getLocal_pan_code());
        values.put(AppConstant.WARD_CODE,commonModel.getWard_code());
        values.put(AppConstant.WARD_ID,commonModel.getWardID());
        values.put(AppConstant.STREET_CODE,commonModel.getStreet_code());
        values.put(AppConstant.STREET_ID,commonModel.getStreet_id());
        values.put(AppConstant.STREET_NAME_EN,commonModel.getStreet_name_english());
        values.put(AppConstant.STREET_NAME_TA,commonModel.getStreet_name_tamil());

        long id = db.insert(DBHelper.STREET_TABLE_NAME,null,values);
       Log.d("Inserted_id_STREET", String.valueOf(id));
        //Toast.makeText(context, "Insertede Successfully", Toast.LENGTH_SHORT).show();
        //db.close();
    }

    public void insertFinYear(CommonModel commonModel) {
        SQLiteDatabase db = this .getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AppConstant.FIN_YEAR_ID, commonModel.getFIN_YEAR_ID());
        values.put(AppConstant.FIN_YEAR, commonModel.getFIN_YEAR());
        values.put(AppConstant.FROM_FIN_YEAR,commonModel.getFROM_FIN_YEAR());
        values.put(AppConstant.TO_FIN_YEAR,commonModel.getTO_FIN_YEAR());
        values.put(AppConstant.FROM_FIN_MONTH,commonModel.getFROM_FIN_MONTH());
        values.put(AppConstant.TO_FIN_MONTH,commonModel.getTO_FIN_MONTH());

        long id = db.insert(DBHelper.FIN_YEAR_LIST,null,values);
        Log.d("Inserted_id_STREET", String.valueOf(id));
        //Toast.makeText(context, "Insertede Successfully", Toast.LENGTH_SHORT).show();
        //db.close();
    }

    public ArrayList<CommonModel> getAllWard() {
        SQLiteDatabase db = this .getWritableDatabase();
        ArrayList<CommonModel> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+WARD_TABLE_NAME +" ORDER BY ward_name_ta",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0)
            {
                if(cursor.moveToFirst()) {
                do {
                    CommonModel card = new CommonModel();
                    card.setD_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setState_code(cursor.getInt(cursor
                            .getColumnIndexOrThrow(AppConstant.STATE_CODE)));
                    card.setLocal_pan_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LP_CODE)));
                    card.setWard_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_CODE)));
                    card.setWardID(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.WARD_ID)));
                    card.setWard_name_english(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_NAME_EN)));
                    card.setWard_name_tamil(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_NAME_TA)));

                    cards.add(card);
                }while (cursor.moveToNext());
            }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } /*finally{
            if (cursor != null) {
                cursor.close();
            }
        }*/
        return cards;
    }

    public ArrayList<CommonModel> getAllStreet() {
        SQLiteDatabase db = this .getWritableDatabase();
        ArrayList<CommonModel> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.FIN_YEAR_LIST,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                   do {
                        CommonModel card = new CommonModel();
                        card.setFIN_YEAR_ID(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.FIN_YEAR_ID)));
                        card.setFIN_YEAR(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.FIN_YEAR)));
                        card.setFROM_FIN_YEAR(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FROM_FIN_YEAR)));
                        card.setTO_FIN_YEAR(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TO_FIN_YEAR)));
                        card.setFROM_FIN_MONTH(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FROM_FIN_MONTH)));
                        card.setTO_FIN_MONTH(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TO_FIN_MONTH)));

                        cards.add(card);
                    } while (cursor.moveToNext());
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        }/* finally{
            if (cursor != null) {
                cursor.close();
            }
        }*/
        return cards;
    }


}
