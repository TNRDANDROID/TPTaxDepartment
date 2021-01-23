package com.nic.TPTaxDepartment.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nic.TPTaxDepartment.constant.AppConstant;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TPTaxDepartment";
    private static final int DATABASE_VERSION = 1;


    public static final String SAVE_TRADE_IMAGE = "trade_image";
    public static final String CAPTURED_PHOTO = "captured_photo";
    public static final String SAVE_FIELD_VISIT = "field_visit";

    public static final String DISTRICT_LIST = "district_list";
    public static final String LOCAL_LIST = "local_list";
    public static final String WARD_LIST = "ward_list";
    public static final String STREET_LIST = "street_list";
    public static final String GENDER_LIST = "gender_list";
    public static final String TAX_TYPE_LIST = "tax_type_list";
    public static final String TAX_TYPE_FIELD_VISIT_LIST = "tax_type_field_visit_list";
    public static final String LICENCE_VALIDITY_LIST = "licence_validity_list";
    public static final String SAVE_NEW_TRADER_DETAILS="save_new_trader_details";
    public static final String TRADE_CODE_LIST="trade_code_list";
    public static final String FIELD_VISIT_STATUS="field_visit_status";


    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + DISTRICT_LIST + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "state_code INTEGER," +
                "dcode INTEGER," +
                "district_name_en TEXT)");
        db.execSQL("CREATE TABLE " + LOCAL_LIST + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "state_code INTEGER," +
                "dcode INTEGER," +
                "lbcode INTEGER," +
                "lbody_name_en TEXT)");
        db.execSQL("CREATE TABLE " + WARD_LIST + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "state_code INTEGER," +
                "dcode INTEGER," +
                "lbcode INTEGER," +
                "ward_id INTEGER," +
                "ward_code TEXT," +
                "ward_name_en TEXT,"+
                "ward_name_ta TEXT)");
        db.execSQL("CREATE TABLE " + STREET_LIST + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "state_code INTEGER," +
                "dcode INTEGER," +
                "lbcode INTEGER," +
                "ward_id INTEGER," +
                "ward_code TEXT," +
                "streetid INTEGER," +
                "street_code TEXT," +
                "street_name_en TEXT,"+
                "street_name_ta TEXT,"+
                "building_zone TEXT)");
        db.execSQL("CREATE TABLE " + GENDER_LIST + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "gender_code TEXT," +
                "gender_name_en TEXT," +
                "gender_name_ta TEXT)");

        db.execSQL("CREATE TABLE " + SAVE_TRADE_IMAGE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "tradecode INTEGER," +
                "trade_image BLOB," +
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
        db.execSQL("CREATE TABLE " + TAX_TYPE_LIST + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "taxtypeid INTEGER," +
                "taxtypedesc_en TEXT," +
                "taxtypedesc_ta TEXT," +
                "taxcollection_methodlogy TEXT," +
                "installmenttypeid INTEGER)");
        db.execSQL("CREATE TABLE " + TAX_TYPE_FIELD_VISIT_LIST + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "taxtypeid INTEGER," +
                "taxtypedesc_en TEXT," +
                "taxtypedesc_ta TEXT," +
                "taxcollection_methodlogy TEXT," +
                "installmenttypeid INTEGER)");
        db.execSQL("CREATE TABLE " + LICENCE_VALIDITY_LIST + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fin_yearid INTEGER," +
                "fin_year TEXT," +
                "from_fin_year INTEGER," +
                "from_fin_mon INTEGER," +
                "to_fin_year INTEGER," +
                "to_fin_mon INTEGER)");

        db.execSQL("CREATE TABLE " + SAVE_NEW_TRADER_DETAILS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                AppConstant.MODE+" TEXT ," +
                AppConstant.TRADE_CODE_ID+" INTEGER," +
                AppConstant.TRADE_CODE+" TEXT," +
                AppConstant.DATE+" TEXT," +
                "description_en TEXT," +
                "description_ta TEXT," +
                AppConstant.LICENCE_TYPE_ID+" INTEGER," +
                AppConstant.LICENCE_TYPE+" TEXT," +
                AppConstant.TRADE_DESCRIPTION+" TEXT," +
                AppConstant.APPLICANT_NAME_EN+" TEXT," +
                AppConstant.APPLICANT_NAME_TA+" TEXT," +
                AppConstant.GENDER+" TEXT," +
                AppConstant.GENDER_CODE+" TEXT," +
                AppConstant.AGE+" INTEGER," +
                AppConstant.FATHER_HUSBAND_NAME_EN+" TEXT," +
                AppConstant.FATHER_HUSBAND_NAME_TA+" TEXT," +
                AppConstant.MOBILE+" INTEGER," +
                AppConstant.E_MAIL+" TEXT," +
                AppConstant.ESTABLISHMENT_NAME_EN+" TEXT," +
                AppConstant.ESTABLISHMENT_NAME_TA+" TEXT," +
                AppConstant.WARD_ID+" TEXT," +
                AppConstant.WARD_NAME_EN+" TEXT," +
                AppConstant.STREET_ID+" TEXT," +
                AppConstant.STREET_NAME_EN+" TEXT," +
                AppConstant.DOOR_NO+" TEXT," +
                AppConstant.LICENCE_VALIDITY+" TEXT," +
                AppConstant.LICENCE_VALIDITY_ID+" TEXT," +
                AppConstant.LATITUDE+" TEXT," +
                AppConstant.TRADE_IMAGE+" BLOB,"+
                AppConstant.PAYMENT_STATUS+" TEXT, "+
                AppConstant.LONGITUDE+" TEXT)");

        db.execSQL("CREATE TABLE " + TRADE_CODE_LIST + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tradedetails_id INTEGER," +
                "lb_tradecode INTEGER," +
                "description_en TEXT," +
                "description_ta TEXT," +
                "date TEXT," +
                "finyear TEXT," +
                "traderate TEXT," +
                "licencetypeid TEXT,"+
                "lbcode INTEGER,"+
                "statecode INTEGER,"+
                "dcode INTEGER)");

        db.execSQL("CREATE TABLE " + FIELD_VISIT_STATUS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "field_visit_status_id INTEGER," +
                "field_visit_status TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_TRADE_IMAGE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_FIELD_VISIT);
            db.execSQL("DROP TABLE IF EXISTS " + CAPTURED_PHOTO);
            db.execSQL("DROP TABLE IF EXISTS " + DISTRICT_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + LOCAL_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + WARD_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + STREET_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + GENDER_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TAX_TYPE_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TAX_TYPE_FIELD_VISIT_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + LICENCE_VALIDITY_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_NEW_TRADER_DETAILS);
            db.execSQL("DROP TABLE IF EXISTS " + TRADE_CODE_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + FIELD_VISIT_STATUS);

            onCreate(db);
        }
    }


}
