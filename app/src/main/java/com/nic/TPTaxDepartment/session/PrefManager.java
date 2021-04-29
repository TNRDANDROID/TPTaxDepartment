package com.nic.TPTaxDepartment.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.nic.TPTaxDepartment.constant.AppConstant;

import org.json.JSONArray;


/**
 * Created by AchanthiSudan on 11/01/19.
 */
public class PrefManager {

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    private static final String APP_KEY = "AppKey";
    private static final String KEY_USER_AUTH_KEY = "auth_key";
    private static final String KEY_USER_PASS_KEY = "pass_key";
    private static final String KEY_ENCRYPT_PASS = "pass";
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_USER_PASSWORD = "UserPassword";
    private static final String KEY_DISTRICT_CODE = "District_Code";
    private static final String KEY_BLOCK_CODE = "Block_Code";
    private static final String KEY_PV_CODE = "Pv_Code";
    private static final String KEY_HAB_CODE = "Hab_Code";
    private static final String KEY_DISTRICT_NAME = "District_Name";
    private static final String KEY_DESIGNATION = "Designation";
    private static final String KEY_NAME = "Name";
    private static final String KEY_BLOCK_NAME = "Block_Name";
    private static final String KEY_SPINNER_SELECTED_PVCODE = "spinner_selected_pv_code";
    private static final String KEY_SPINNER_SELECTED_BLOCKCODE = "spinner_selected_block_code";

    private static final String KEY_BLOCK_CODE_JSON = "block_code_json";
    private static final String KEY_VILLAGE_CODE_JSON = "village_code_json";
    private static final String KEY_SCHEME_NAME = "Scheme_Name";
    private static final String KEY_FINANCIALYEAR_NAME = "FinancialYear_Name";
    private static final String KEY_VILLAGE_LIST_PV_NAME = "Village_List_Pv_Name";



    private static final String IMEI = "imei";
    private static final String MOTIVATOR_ID = "motivator_id";
    private static final String SCHEDULE_MASTER_ID = "schedule_master_id";
    private static final String KEY_DELETE_ID = "deleteId";
    private static final String KEY_DELETE_POSITION = "deletePosition";
    private static final String CHECK_BOX_CLICKED = "checkboxvalue";
    private static final String MI_TANK_SURVEY_ID = "mi_tank_survey_id";


    private static final String WARD_LIST = "WardList";
    private static final String STREET_LIST = "StreetList";
    private static final String TAX_TYPE_LIST = "TaxTypeList";
    private static final String TRADER_LICENSE_TYPE_LIST = "TraderLicenseTypeList";
    private static final String TRADERS_LIST = "TradersList";
    private static final String DAILY_COLLECTION_LIST = "DailyCollectionList";
    private static final String STATE_CODE = "StateCode";
    private static final String LB_TYPE = "lbtype";
    private static final String TP_CODE = "tpcode";
    private static final String LBODY_NAME_EN = "lbody_name_en";
    private static final String ROLE_CODE = "role_code";
    private static final String ROLE_NAME = "role_name";
    private static final String USER_FNAME = "user_first_name";
    private static final String USER_LNAME = "user_last_name";
    private static final String TRADER_IMAGE_LIST = "trader_image_list";
    private static final String FIELD_VISIT_HISTORY_LIST = "FieldVisitHistory_list";
    private static final String PROPERTY_IMAGE = "PROPERTY_IMAGE";
    private static final String PROPERTY_IMAGE_LAT = "PROPERTY_IMAGE_LAT";
    private static final String PROPERTY_IMAGE_LONG = "PROPERTY_IMAGE_LONG";




    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(AppConstant.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public Integer getScheduleMasterId() {
        return pref.getInt(SCHEDULE_MASTER_ID,0);
    }

    public void setScheduleMasterId(Integer scheduleMasterId) {
        editor.putInt(SCHEDULE_MASTER_ID,scheduleMasterId);
        editor.commit();
    }

    public String getIMEI() {
        return pref.getString(IMEI,null);
    }

    public void setImei(String imei) {
        editor.putString(IMEI,imei);
        editor.commit();
    }
    public String getTraderImageList() {
        return pref.getString(TRADER_IMAGE_LIST,null);
    }

    public void setTraderImageList(String traderImageList) {
        editor.putString(TRADER_IMAGE_LIST,traderImageList);
        editor.commit();
    }

    public void setAppKey(String appKey) {
        editor.putString(APP_KEY, appKey);
        editor.commit();
    }

    public String getAppKey() {
        return pref.getString(APP_KEY, null);
    }

    public void setWardList(String ward) {
        editor.putString(WARD_LIST, ward);
        editor.commit();
    }

    public String getWardList() {
        return pref.getString(WARD_LIST, null);
    }

    public void setPropertyImage(String propertyImage) {
        editor.putString(PROPERTY_IMAGE, propertyImage);
        editor.commit();
    }

    public String getPropertyImage() {
        return pref.getString(PROPERTY_IMAGE, null);
    }
    public void setPropertyImageLong(String propertyImageLong) {
        editor.putString(PROPERTY_IMAGE_LONG, propertyImageLong);
        editor.commit();
    }

    public String getPropertyImageLong() {
        return pref.getString(PROPERTY_IMAGE_LONG, null);
    }
    public void setPropertyImageLat(String propertyImageLat) {
        editor.putString(PROPERTY_IMAGE_LAT, propertyImageLat);
        editor.commit();
    }

    public String getPropertyImageLat() {
        return pref.getString(PROPERTY_IMAGE_LAT, null);
    }
    public void setUserFname(String userFname) {
        editor.putString(USER_FNAME, userFname);
        editor.commit();
    }

    public String getUserFname() {
        return pref.getString(USER_FNAME, null);
    }
    public void setUserLname(String userLname) {
        editor.putString(USER_LNAME, userLname);
        editor.commit();
    }

    public String getUserLname() {
        return pref.getString(USER_LNAME, null);
    }
    public void setRoleName(String roleName) {
        editor.putString(ROLE_NAME, roleName);
        editor.commit();
    }

    public String getRoleName() {
        return pref.getString(ROLE_NAME, null);
    }
    public void setRoleCode(String roleCode) {
        editor.putString(ROLE_CODE, roleCode);
        editor.commit();
    }

    public String getRoleCode() {
        return pref.getString(ROLE_CODE, null);
    }
    public void setLbodyNameEn(String lbodyNameEn) {
        editor.putString(LBODY_NAME_EN, lbodyNameEn);
        editor.commit();
    }

    public String getLbodyNameEn() {
        return pref.getString(LBODY_NAME_EN, null);
    }
    public void setLbType(String lbType) {
        editor.putString(LB_TYPE, lbType);
        editor.commit();
    }

    public String getLbType() {
        return pref.getString(LB_TYPE, null);
    }
    public void setStateCode(String stateCode) {
        editor.putString(STATE_CODE, stateCode);
        editor.commit();
    }

    public String getStateCode() {
        return pref.getString(STATE_CODE, null);
    }

    public void setStreetList(String street) {
        editor.putString(STREET_LIST, street);
        editor.commit();
    }

    public String getStreetList() {
        return pref.getString(STREET_LIST, null);
    }
    public void setTpCode(String tpCode) {
        editor.putString(TP_CODE, tpCode);
        editor.commit();
    }

    public String getTpCode() {
        return pref.getString(TP_CODE, null);
    }

    public void setDailyCollectionList(String dailyCollectionList) {
        editor.putString(DAILY_COLLECTION_LIST, dailyCollectionList);
        editor.commit();
    }

    public String getDailyCollectionList() {
        return pref.getString(DAILY_COLLECTION_LIST, null);
    }

    public void setTaxTypeList(String taxTypeList) {
        editor.putString(TAX_TYPE_LIST, taxTypeList);
        editor.commit();
    }

    public String getTaxTypeList() {
        return pref.getString(TAX_TYPE_LIST, null);
    }
    public void setTraderLicenseTypeList(String TraderLicenseTypeList) {
        editor.putString(TRADER_LICENSE_TYPE_LIST, TraderLicenseTypeList);
        editor.commit();
    }

    public String getTraderLicenseTypeList() {
        return pref.getString(TRADER_LICENSE_TYPE_LIST, null);
    }
    public void setTradersList(String TradersList) {
        editor.putString(TRADERS_LIST, TradersList);
        editor.commit();
    }

    public String getTradersList() {
        return pref.getString(TRADERS_LIST, null);
    }
    public void setFieldVisitHistoryList(String fieldVisitHistoryList) {
        editor.putString(FIELD_VISIT_HISTORY_LIST, fieldVisitHistoryList);
        editor.commit();
    }

    public String getFieldVisitHistoryList() {
        return pref.getString(FIELD_VISIT_HISTORY_LIST, null);
    }


    public void clearSession() {
        editor.clear();
        editor.commit();
    }


    public void setUserAuthKey(String userAuthKey) {
        editor.putString(KEY_USER_AUTH_KEY, userAuthKey);
        editor.commit();
    }

    public String getKeyDeleteId() {
        return pref.getString(KEY_DELETE_ID,null);
    }

    public void setKeyDeleteId(String deleteId) {
        editor.putString(KEY_DELETE_ID,deleteId);
        editor.commit();
    }

    public Integer getKeyDeletePosition() {
        return pref.getInt(KEY_DELETE_POSITION,0);
    }

    public void setKeyDeletePosition(Integer deletePos) {
        editor.putInt(KEY_DELETE_POSITION,deletePos);
        editor.commit();
    }

    public String getUserAuthKey() {
        return pref.getString(KEY_USER_AUTH_KEY, null);
    }

    public void setUserPassKey(String userPassKey) {
        editor.putString(KEY_USER_PASS_KEY, userPassKey);
        editor.commit();
    }

    public String getMotivatorId() {
        return pref.getString(MOTIVATOR_ID, null);
    }

    public void setMotivatorId(String userPassKey) {
        editor.putString(MOTIVATOR_ID, userPassKey);
        editor.commit();
    }

    public String getUserPassKey() {
        return pref.getString(KEY_USER_PASS_KEY, null);
    }


    public void setUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    public String getUserName() { return pref.getString(KEY_USER_NAME, null); }

    public void setUserPassword(String userPassword) {
        editor.putString(KEY_USER_PASSWORD, userPassword);
        editor.commit();
    }

    public String getUserPassword() { return pref.getString(KEY_USER_PASSWORD, null); }


    public void setEncryptPass(String pass) {
        editor.putString(KEY_ENCRYPT_PASS, pass);
        editor.commit();
    }

    public String getEncryptPass() {
        return pref.getString(KEY_ENCRYPT_PASS, null);
    }

    public Object setDistrictCode(Object key) {
        editor.putString(KEY_DISTRICT_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getDistrictCode() {
        return pref.getString(KEY_DISTRICT_CODE, null);
    }


    public Object setBlockCode(Object key) {
        editor.putString(KEY_BLOCK_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getBlockCode() {
        return pref.getString(KEY_BLOCK_CODE, null);
    }



    public Object setPvCode(Object key) {
        editor.putString(KEY_PV_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getPvCode() {
        return pref.getString(KEY_PV_CODE, null);
    }

    public Object setHabCode(Object key) {
        editor.putString(KEY_HAB_CODE, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getHabCode() {
        return pref.getString(KEY_HAB_CODE, null);
    }



    public Object setDistrictName(Object key) {
        editor.putString(KEY_DISTRICT_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getDistrictName() {
        return pref.getString(KEY_DISTRICT_NAME, null);
    }

    public Object setBlockName(Object key) {
        editor.putString(KEY_BLOCK_NAME, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getBlockName() {
        return pref.getString(KEY_BLOCK_NAME, null);
    }


    public Object setDesignation(Object key) {
        editor.putString(KEY_DESIGNATION, String.valueOf(key));
        editor.commit();
        return key;
    }

    public String getDesignation() {
        return pref.getString(KEY_DESIGNATION, null);
    }



    public void setName(String userName) {
        editor.putString(KEY_NAME, userName);
        editor.commit();
    }

    public String getName() {
        return pref.getString(KEY_NAME, null);
    }

    public  void setSchemeName(String key) {
        editor.putString(KEY_SCHEME_NAME,key);
        editor.commit();
    }

    public String getSchemeName() {return pref.getString(KEY_SCHEME_NAME,null);}

    public void setFinancialyearName(String key) {
        editor.putString(KEY_FINANCIALYEAR_NAME,key);
        editor.commit();
    }

    public String getFinancialyearName() {return pref.getString(KEY_FINANCIALYEAR_NAME,null);}


    public void setKeySpinnerSelectedPvcode(String userName) {
        editor.putString(KEY_SPINNER_SELECTED_PVCODE, userName);
        editor.commit();
    }

    public String getKeySpinnerSelectedPVcode() {
        return pref.getString(KEY_SPINNER_SELECTED_PVCODE, null);
    }


    public void clearSharedPreferences(Context context) {
        pref = _context.getSharedPreferences(AppConstant.PREF_NAME, PRIVATE_MODE);
        editor.clear();
        editor.apply();
    }


    public void setBlockCodeJson(JSONArray jsonarray) {
        editor.putString(KEY_BLOCK_CODE_JSON, jsonarray.toString());
        editor.commit();
    }

    private String getBlockCodeJsonList() {
        return pref.getString(KEY_BLOCK_CODE_JSON, null);
    }

    public JSONArray getBlockCodeJson() {
        JSONArray jsonData = null;
        String strJson = getBlockCodeJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                jsonData = new JSONArray(strJson);
            }
        } catch (Exception e) {

        }
        Log.d("prefBlockJson",""+jsonData);
        return jsonData;
    }

    public void setVillagePvCodeJson(JSONArray jsonarray) {
        editor.putString(KEY_VILLAGE_CODE_JSON, jsonarray.toString());
        editor.commit();
    }

    private String getVillagePvCodeJsonList() {
        return pref.getString(KEY_VILLAGE_CODE_JSON, null);
    }

    public JSONArray getVillagePvCodeJson() {
        JSONArray jsonData = null;
        String strJson = getVillagePvCodeJsonList();//second parameter is necessary ie.,Value to return if this preference does not exist.
        try {
            if (strJson != null) {
                jsonData = new JSONArray(strJson);
            }
        } catch (Exception e) {

        }
        Log.d("prefVillageJson",""+jsonData);
        return jsonData;
    }

    public void setKeySpinnerSelectedBlockcode(String userName) {
        editor.putString(KEY_SPINNER_SELECTED_BLOCKCODE, userName);
        editor.commit();
    }

    public String getKeySpinnerSelectedBlockcode() {
        return pref.getString(KEY_SPINNER_SELECTED_BLOCKCODE, null);
    }

    public void setVillageListPvName(String key) {
        editor.putString(KEY_VILLAGE_LIST_PV_NAME,  key);
        editor.commit();
    }

    public String getVillageListPvName() {
        return pref.getString(KEY_VILLAGE_LIST_PV_NAME, null);
    }

    public void setCheckBoxClicked(String key) {
        editor.putString(CHECK_BOX_CLICKED,  key);
        editor.commit();
    }

    public String getCheckBoxClicked() {
        return pref.getString(CHECK_BOX_CLICKED, null);
    }

    public void setMiTankSurveyId(String key) {
        editor.putString(MI_TANK_SURVEY_ID,  key);
        editor.commit();
    }

    public String getMiTankSurveyId() {
        return pref.getString(MI_TANK_SURVEY_ID, null);
    }

}
