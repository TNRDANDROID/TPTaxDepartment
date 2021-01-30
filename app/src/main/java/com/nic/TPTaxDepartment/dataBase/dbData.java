package com.nic.TPTaxDepartment.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.TPtaxModel;

import java.util.ArrayList;


public class dbData {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Context context;

    public dbData(Context context){
        this.dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if(dbHelper != null) {
            dbHelper.close();
        }
    }

    /****** DISTRICT TABLE *****/


    /****** VILLAGE TABLE *****/
//    public MITank insertVillage(MITank miTank) {
//
//        ContentValues values = new ContentValues();
//        values.put(AppConstant.DISTRICT_CODE, miTank.getDistictCode());
//        values.put(AppConstant.BLOCK_CODE, miTank.getBlockCode());
//        values.put(AppConstant.PV_CODE, miTank.getPvCode());
//        values.put(AppConstant.PV_NAME, miTank.getPvName());
//
//        long id = db.insert(DBHelper.VILLAGE_TABLE_NAME,null,values);
//        Log.d("Inserted_id_village", String.valueOf(id));
//
//        return miTank;
//    }
//    public ArrayList<MITank > getAll_Village(String dcode, String bcode) {
//
//        ArrayList<MITank > cards = new ArrayList<>();
//        Cursor cursor = null;
//
//        try {
//            cursor = db.rawQuery("select * from "+DBHelper.VILLAGE_TABLE_NAME+" where dcode = "+dcode+" and bcode = "+bcode+" order by pvname asc",null);
//            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
//            //       COLUMNS, null, null, null, null, null);
//            if (cursor.getCount() > 0) {
//                while (cursor.moveToNext()) {
//                    MITank  card = new MITank ();
//                    card.setDistictCode(cursor.getString(cursor
//                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
//                    card.setBlockCode(cursor.getString(cursor
//                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
//                    card.setPvCode(cursor.getString(cursor
//                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
//                    card.setPvName(cursor.getString(cursor
//                            .getColumnIndexOrThrow(AppConstant.PV_NAME)));
//
//                    cards.add(card);
//                }
//            }
//        } catch (Exception e){
//            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
//        } finally{
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return cards;
//    }

//    public MITank insertHabitation(MITank miTank) {
//
//        ContentValues values = new ContentValues();
//        values.put(AppConstant.DISTRICT_CODE, miTank.getDistictCode());
//        values.put(AppConstant.BLOCK_CODE, miTank.getBlockCode());
//        values.put(AppConstant.PV_CODE, miTank.getPvCode());
//        values.put(AppConstant.HABB_CODE, miTank.getHabCode());
//        values.put(AppConstant.HABITATION_NAME, miTank.getHabitationName());
//
//        long id = db.insert(DBHelper.HABITATION_TABLE_NAME,null,values);
//        Log.d("Inserted_id_habitation", String.valueOf(id));
//
//        return miTank;
//    }
//    public ArrayList<MITank > getAll_Habitation(String dcode, String bcode) {
//
//        ArrayList<MITank > cards = new ArrayList<>();
//        Cursor cursor = null;
//
//        try {
//            cursor = db.rawQuery("select * from "+DBHelper.HABITATION_TABLE_NAME+" where dcode = "+dcode+" and bcode = "+bcode+" order by habitation_name asc",null);
//            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
//            //       COLUMNS, null, null, null, null, null);
//            if (cursor.getCount() > 0) {
//                while (cursor.moveToNext()) {
//                    MITank  card = new MITank ();
//                    card.setDistictCode(cursor.getString(cursor
//                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
//                    card.setBlockCode(cursor.getString(cursor
//                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
//                    card.setPvCode(cursor.getString(cursor
//                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
//                    card.setHabCode(cursor.getString(cursor
//                            .getColumnIndexOrThrow(AppConstant.HABB_CODE)));
//                    card.setHabitationName(cursor.getString(cursor
//                            .getColumnIndexOrThrow(AppConstant.HABITATION_NAME)));
//
//                    cards.add(card);
//                }
//            }
//        } catch (Exception e){
//            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
//        } finally{
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//        return cards;
//    }




//    public void update_Track() {
//        String whereClause = "server_flag = server_flag";
//        Log.d("Update id is ", "id");
//        ContentValues values = new ContentValues();
//        values.put("server_flag", 1);
//        db.update(DBHelper.SAVE_TRACK_TABLE, values, whereClause, null);
//    }
//
//    public void deleteVillageTable() {
//        db.execSQL("delete from " + DBHelper.VILLAGE_TABLE_NAME);
//    }
//
//    public void deleteHabitationTable() {
//        db.execSQL("delete from " + DBHelper.HABITATION_TABLE_NAME);
//    }
//
//    public void deleteTankStructure() {
//        db.execSQL("delete from " + DBHelper.MI_TANK_STRUCTURE);
//    }
//
//    public void deleteMITankData() {
//        db.execSQL("delete from " + DBHelper.MI_TANK_DATA);
//    }
//
//    public void deleteStructures() {
//        db.execSQL("delete from " + DBHelper.MI_TANK_DATA_STRUCTURES);
//    }
//
//    public void deleteMITankCondition() {
//        db.execSQL("delete from " + DBHelper.MI_TANK_CONDITION);
//    }
//
//    public void deleteMITankImages() {
//        db.execSQL("delete from " + DBHelper.SAVE_MI_TANK_IMAGES);
//    }
//
//    public void deleteSaveTrackTable() {
//        db.execSQL("delete from " + DBHelper.SAVE_TRACK_TABLE);
//    }
//
//
    public void deleteAll() {

//        deleteVillageTable();
//        deleteHabitationTable();
//        deleteTankStructure();
//        deleteMITankData();
//        deleteStructures();
//        deleteMITankCondition();
//        deleteMITankImages();
//        deleteSaveTrackTable();
    }

    public ArrayList<TPtaxModel> selectImage(String mobileNo, String status) {
        db.isOpen();
        ArrayList<TPtaxModel> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = "mobileno = ? and screen_status = ?";
        String[] selectionArgs = new String[]{mobileNo,status}; ;
//        if (status.equalsIgnoreCase("new")) {
//            selection = "tradecode = ? and screen_status = ?";
//            selectionArgs = new String[]{tradecode,status};
//        }else {
//            selection = "dcode = ? and bcode = ? and pvcode = ? and work_id = ? and type_of_work = ?";
//            selectionArgs = new String[]{dcode,bcode,pvcode,work_id,type_of_work};
//        }

        try {
            cursor = db.query(DBHelper.SAVE_TRADE_IMAGE,
                    new String[]{"*"}, selection,selectionArgs, null, null, null);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        cards = new ArrayList<>();

                        byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.TRADE_IMAGE));
                        byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        TPtaxModel card = new TPtaxModel();
                        card.setTradecode(cursor.getInt(cursor
                                .getColumnIndexOrThrow(AppConstant.TRADE_CODE)));
                        card.setLatitude(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.LATITUDE)));
                        card.setLongitude(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.LONGITUDE)));
                        card.setImage(decodedByte);

                        cards.add(card);
                    }while(cursor.moveToNext());
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<CommonModel> selectPendingImage(String request_id) {
        db.isOpen();
        ArrayList<CommonModel> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = "request_id = ?";
        String[] selectionArgs = new String[]{request_id}; ;
//        if (status.equalsIgnoreCase("new")) {
//            selection = "tradecode = ? and screen_status = ?";
//            selectionArgs = new String[]{tradecode,status};
//        }else {
//            selection = "dcode = ? and bcode = ? and pvcode = ? and work_id = ? and type_of_work = ?";
//            selectionArgs = new String[]{dcode,bcode,pvcode,work_id,type_of_work};
//        }

        try {
            cursor = db.query(DBHelper.CAPTURED_PHOTO,
                    new String[]{"*"}, selection,selectionArgs, null, null, null);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {

                        byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(AppConstant.FIELD_IMAGE));
                        byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        CommonModel card = new CommonModel();
                        card.setRequest_id(cursor.getString(cursor
                                .getColumnIndexOrThrow("request_id")));
                        card.setLatitude(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.LATITUDE)));
                        card.setLongitude(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.LONGITUDE)));
                        card.setImage(decodedByte);

                        cards.add(card);
                    }while(cursor.moveToNext());
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<TPtaxModel> getPendingActivity() {
        db.isOpen();
        ArrayList<TPtaxModel> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = null;
        String[] selectionArgs = null;


        try {
            cursor = db.query(DBHelper.SAVE_FIELD_VISIT,
                    new String[]{"*"}, selection,selectionArgs, null, null, null);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        TPtaxModel card = new TPtaxModel();
                        card.setTaxTypeId(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.TAX_TYPE_ID)));
                        card.setAssessmentId(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.ASSESSMENT_ID)));
                        card.setCurrentStatus(cursor.getString(cursor
                                .getColumnIndexOrThrow(AppConstant.CURRENT_STATUS)));

                        cards.add(card);
                    }while(cursor.moveToNext());
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }




}
