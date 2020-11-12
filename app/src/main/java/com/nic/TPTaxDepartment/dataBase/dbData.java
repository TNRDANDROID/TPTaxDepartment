package com.nic.TPTaxDepartment.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


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



}
