package com.nic.TPTaxDepartment.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.android.volley.VolleyError;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.databinding.ExistingTradeDetailsViewBinding;
import com.nic.TPTaxDepartment.databinding.ExistingTradeDetailsViewNewBinding;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.Gender;
import com.nic.TPTaxDepartment.model.SpinnerAdapter;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nic.TPTaxDepartment.activity.NewTradeLicenceScreen.getSize;

public class ExistTradeViewClass extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener
        , CompoundButton.OnCheckedChangeListener,AdapterView.OnItemSelectedListener
        , View.OnFocusChangeListener {

    ArrayList<Gender> genders;
    ArrayList<CommonModel> wards;
    ArrayList<CommonModel> streets;
    ArrayList<CommonModel> selectedStreets;
    ArrayList<CommonModel> finYear;
    ArrayList<CommonModel> traderLicenseTypeList;
    private List<TPtaxModel> District = new ArrayList<>();
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    String[] mApps = {"Male", "Female", "Others"};
    private PrefManager prefManager;
    String pref_district;
    private Handler handler = new Handler();
    private static TextView date;
    private List<TPtaxModel> LicenceType = new ArrayList<>();
    private List<TPtaxModel> LicenceValidity = new ArrayList<>();
    HashMap<String, String> spinnerMap;
    HashMap<String, String> spinnerMapStreets;
    HashMap<String, String> spinnerMapWard;
    HashMap<String, String> spinnerMapFinYear;
    HashMap<String, String> spinnerMapLicenceType;
    HashMap<String, String> spinnerTradeCode;
    String selectedGenderId;
    String selectedGender = "";
    String selectedWardId;
    String selectedTradeCode;
    String selectedTrdeCodeDetailsID;
    String selectedWardName = "";
    String selectedFinId;
    String selectedFinName = "";
    String selectedLicenceTpeId;
    String selectedLicenceTypeName = "";
    String selectedStreetId;
    String selectedStreetName = "";
    String owner_status_text = "";
    String motor_available_status_text = "";
    String generator_available_status_text = "";
    String professional_tax_paid_status_text = "";
    String property_tax_paid_status_text = "";
    private String fileString = "";
    Uri uri;
    File myFile;
    String displayName = "";
    private String fileSize = "";
    private byte[] bytes;
    private String uriString;
    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;
    String traderCode, tradeDate, licenseType, tradeDescription, traderName, traderNameTa, tradeImage, traderGender, traderAge,
            fatherName, fatherNameTa, mobileNo, email, establishmentName, ward, street, doorNo, licenseValidity, paymentStatus;
    int position;
    int tradersImagePosition;
    Boolean flag, wardFlag = false;
    Boolean Annualflag = false;
    Boolean Motorflag = false;
    Boolean Generatorflag = false;
    ArrayList<TPtaxModel> traders;
    ArrayList<TPtaxModel> tradersImageList;

    String image = "", lat = "", lan = "";
    String mobileNumber = "";
    ArrayList<CommonModel> loadTradeCodeList;
    ArrayAdapter<String> tradeCodeSpArray;
    ArrayAdapter<String> licenceTypeArray;
    ArrayAdapter<String> genderArray;
    ArrayAdapter<String> wardArray;
    ArrayAdapter<String> streetArray;
    ArrayAdapter<String> licenceValidityArray;

    ArrayAdapter<String> annualSaleArray;
    ArrayAdapter<String> motorRangeArray;
    ArrayAdapter<String> generatorRangeArray;
    HashMap<String, String> spinnerMapAnnualSale;
    HashMap<String, String> spinnerMapMotorRange;
    HashMap<String, String> spinnerMapGeneratorRange;
    ArrayList<CommonModel> AnnualSaleList;
    ArrayList<CommonModel> motorRangeList;
    ArrayList<CommonModel> generatorRangeList;


    ArrayList<CommonModel> filterAnnualSale;
    ArrayList<CommonModel> filtermotorRangeList;
    ArrayList<CommonModel> filterGeneratorList;

    String selectedAnnualId;
    String selectedAnnualSale = "";
    String selectedMotorId;
    String selectedMotorRange = "";
    String selectedGeneratorId;
    String selectedGeneratorRange = "";

    ExistingTradeDetailsViewBinding existingTradeDetailsViewBinding;
    ExistingTradeDetailsViewNewBinding existingTradeDetailsViewNewBinding;

    private int visible_count;
    Animation animation;
    Animation animationOut;
    Context context;
    Integer pageNumber = 0;
    String DocumentString = "";
    String TraderImageString = "";
    final Calendar myCalendar = Calendar.getInstance();
    String[] motorRangeItems,generatorItems;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        existingTradeDetailsViewNewBinding = DataBindingUtil.setContentView(this, R.layout.existing_trade_details_view_new);
        existingTradeDetailsViewNewBinding.setActivity(this);
        context = this;
        prefManager = new PrefManager(this);
       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mApps);
        existingTradeDetailsViewNewBinding.licenceValidity.setAdapter(adapter);*/
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        this.getWindow().setStatusBarColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

        existingTradeDetailsViewNewBinding.scrollView.setNestedScrollingEnabled(true);
        date = existingTradeDetailsViewNewBinding.date;
        existingTradeDetailsViewNewBinding.date.setText(context.getResources().getString(R.string.select_Date));

        existingTradeDetailsViewNewBinding.documentLayout.setVisibility(View.GONE);
        existingTradeDetailsViewNewBinding.main.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
        animationOut = AnimationUtils.loadAnimation(this, R.anim.slide_enter);
        visible_count = 0;
        existingTradeDetailsViewNewBinding.first.setVisibility(View.VISIBLE);
        existingTradeDetailsViewNewBinding.second.setVisibility(View.GONE);
        existingTradeDetailsViewNewBinding.third.setVisibility(View.GONE);
        existingTradeDetailsViewNewBinding.previous.setVisibility(View.GONE);
        existingTradeDetailsViewNewBinding.next.setText(context.getResources().getString(R.string.next));


        try {
            LoadFinYearSpinner();
            LoadGenderSpinner();
            LoadTradeCodeListSpinner();
            LoadLicenceTypeSpinner();
           /* LoadAnnualSaleListSpinner();
            LoadGeneratorRangeListSpinner();
            LoadMotorRangeListSpinner();*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //getIntent Data
        traders = new ArrayList<TPtaxModel>();
        tradersImageList = new ArrayList<TPtaxModel>();
        flag = getIntent().getBooleanExtra("flag", false);
        if (flag) {
            position = getIntent().getIntExtra("position", 0);
            tradersImagePosition = getIntent().getIntExtra("tradersImagePosition", 0);
            traders = (ArrayList<TPtaxModel>) getIntent().getSerializableExtra("tradersList");
            tradersImageList = (ArrayList<TPtaxModel>) getIntent().getSerializableExtra("tradersImageList");
            try {
                LoadStreetSpinner(traders.get(position).getWardId(), "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            LoadPendingTraderDetails();
            //shoewDetails();
        } else {
            LoadWardSpinner();
        }

        String colored = "*";
        String mobileView = "கைபேசி எண் / Mobile No";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(mobileView);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();


        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        existingTradeDetailsViewNewBinding.mobileHint.setText(builder);

        //Spinners
        existingTradeDetailsViewNewBinding.gender.setOnItemSelectedListener(this);
        existingTradeDetailsViewNewBinding.licenceValidity.setOnItemSelectedListener(this);
        existingTradeDetailsViewNewBinding.tradeCodeSpinner.setOnItemSelectedListener(this);
        existingTradeDetailsViewNewBinding.licenceType.setOnItemSelectedListener(this);
        existingTradeDetailsViewNewBinding.wardNo.setOnItemSelectedListener(this);
        existingTradeDetailsViewNewBinding.streetsName.setOnItemSelectedListener(this);
        existingTradeDetailsViewNewBinding.annualSale.setOnItemSelectedListener(this);
        existingTradeDetailsViewNewBinding.motorRangeSpinner.setOnItemSelectedListener(this);
        existingTradeDetailsViewNewBinding.generatorSpinner.setOnItemSelectedListener(this);

        //is paid
        existingTradeDetailsViewNewBinding.isPaid.setOnCheckedChangeListener(this::onCheckedChanged);

        //Owner Status
        existingTradeDetailsViewNewBinding.ownerStatusNo.setOnCheckedChangeListener(this::onCheckedChanged);
        existingTradeDetailsViewNewBinding.ownerStatusYes.setOnCheckedChangeListener(this::onCheckedChanged);

        //Motor Available
        existingTradeDetailsViewNewBinding.motorAvilableStatusYes.setOnCheckedChangeListener(this::onCheckedChanged);
        existingTradeDetailsViewNewBinding.motorAvilableStatusNo.setOnCheckedChangeListener(this::onCheckedChanged);

        //Generator Available
        existingTradeDetailsViewNewBinding.geneartorAvilableStatusYes.setOnCheckedChangeListener(this::onCheckedChanged);
        existingTradeDetailsViewNewBinding.generatorAvilableStatusNo.setOnCheckedChangeListener(this::onCheckedChanged);

        //Professional Tax
        existingTradeDetailsViewNewBinding.professionalTaxYes.setOnCheckedChangeListener(this::onCheckedChanged);
        existingTradeDetailsViewNewBinding.professionalTaxNo.setOnCheckedChangeListener(this::onCheckedChanged);

        //Property Tax
        existingTradeDetailsViewNewBinding.propertyTaxYes.setOnCheckedChangeListener(this::onCheckedChanged);
        existingTradeDetailsViewNewBinding.propertyTaxNo.setOnCheckedChangeListener(this::onCheckedChanged);

        //Focus Change
        existingTradeDetailsViewNewBinding.applicantName.setOnFocusChangeListener(this::onFocusChange);


    }

    private void LoadPendingTraderDetails() {
        existingTradeDetailsViewNewBinding.viewDoc.setVisibility(View.VISIBLE);
        existingTradeDetailsViewNewBinding.leaseAggrementLayout.setVisibility(View.VISIBLE);
        String tt = traders.get(position).getAmount_range_id();
        System.out.println("getAmount_range_id" + tt);
        wardFlag = false;
        LoadWardSpinner();

        /*getTradeImage();
        getFieldDocument();*/

        try {
            LoadStreetSpinner(traders.get(position).getWardId(), spinnerMapStreets.get(traders.get(position).getStreetId()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int tradePosition = tradeCodeSpArray.getPosition(spinnerTradeCode.get(traders.get(position).getTradedetails_id()));
            if (tradePosition >= 0) {
                existingTradeDetailsViewNewBinding.tradeCodeSpinner.setSelection(tradePosition);
            } else {
                existingTradeDetailsViewNewBinding.tradeCodeSpinner.setAdapter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int licenceTypePosition = licenceTypeArray.getPosition(traders.get(position).getTraders_license_type_name());
            if (licenceTypePosition >= 0) {
                existingTradeDetailsViewNewBinding.licenceType.setSelection(licenceTypePosition);
            } else {
                existingTradeDetailsViewNewBinding.licenceType.setAdapter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int genderPosition = genderArray.getPosition(spinnerMap.get(traders.get(position).getApgenderId()));
            if (genderPosition >= 0) {
                existingTradeDetailsViewNewBinding.gender.setSelection(genderPosition);
            } else {
                existingTradeDetailsViewNewBinding.gender.setAdapter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int wardPosition = wardArray.getPosition(spinnerMapWard.get(traders.get(position).getWardId()));
            if (wardPosition >= 0) {
                existingTradeDetailsViewNewBinding.wardNo.setSelection(wardPosition);
            } else {
                existingTradeDetailsViewNewBinding.wardNo.setAdapter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            int licencePosition = licenceValidityArray.getPosition(spinnerMapFinYear.get(traders.get(position).getLicenceValidity()));
            if (licencePosition >= 0) {
                existingTradeDetailsViewNewBinding.licenceValidity.setSelection(licencePosition);
            } else {
                existingTradeDetailsViewNewBinding.licenceValidity.setAdapter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        try {
            int annualPosition = annualSaleArray.getPosition(spinnerMapAnnualSale.get(traders.get(position).getAmount_range_id()));
            if(annualPosition >= 0){
                existingTradeDetailsViewNewBinding.annualSale.setSelection(annualPosition);
            }else {
                existingTradeDetailsViewNewBinding.annualSale.setAdapter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

//        existingTradeDetailsViewNewBinding.date.setText(traders.get(position).getTrade_date());
        existingTradeDetailsViewNewBinding.tradeDescription.setText(traders.get(position).getTradedesce());
        existingTradeDetailsViewNewBinding.applicantName.setText(traders.get(position).getTraderName());
        existingTradeDetailsViewNewBinding.applicantNameTamil.setText(traders.get(position).getApname_ta());
        existingTradeDetailsViewNewBinding.age.setText(traders.get(position).getApage());
        existingTradeDetailsViewNewBinding.fatherHusName.setText(traders.get(position).getApfathername_en());
        existingTradeDetailsViewNewBinding.fatherHusNameTamil.setText(traders.get(position).getApfathername_ta());
        existingTradeDetailsViewNewBinding.mobileNo.setText(traders.get(position).getMobileno());
        existingTradeDetailsViewNewBinding.emailId.setText(traders.get(position).getEmail());
        existingTradeDetailsViewNewBinding.establishName.setText(traders.get(position).getEstablishment_name_en());
        existingTradeDetailsViewNewBinding.descriptionEnglish.setText(traders.get(position).getDescription_en());
        existingTradeDetailsViewNewBinding.descriptionTamil.setText(traders.get(position).getDescription_ta());
        existingTradeDetailsViewNewBinding.remarksField.setText(traders.get(position).getRemark());

        existingTradeDetailsViewNewBinding.doorNo.setText(traders.get(position).getDoorno());

       /* if(traders.get(position).getPaymentStatus() != null && traders.get(position).getPaymentStatus().equals("Paid")){
            existingTradeDetailsViewNewBinding.isPaid.setChecked(true);
        }else {
            existingTradeDetailsViewNewBinding.isPaid.setChecked(false);
        }*/

        if (traders.get(position).getOwnerStatus() != null && traders.get(position).getOwnerStatus().equals(context.getResources().getString(R.string.no))) {
            existingTradeDetailsViewNewBinding.ownerStatusYes.setChecked(false);
            existingTradeDetailsViewNewBinding.ownerStatusNo.setChecked(true);
            existingTradeDetailsViewNewBinding.chooseFileLayout.setVisibility(View.VISIBLE);
            existingTradeDetailsViewNewBinding.leaseAggrementLayout.setVisibility(View.GONE);
            Shader shader = new LinearGradient(0, 0, 0, existingTradeDetailsViewNewBinding.viewDoc.getLineHeight(),
                    context.getResources().getColor(R.color.colorPrimary),
                    context.getResources().getColor(R.color.colorPrimaryDark), Shader.TileMode.REPEAT);
            existingTradeDetailsViewNewBinding.viewDoc.getPaint().setShader(shader);
            existingTradeDetailsViewNewBinding.yes.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.no.setVisibility(View.GONE);
//            getFieldDocument();

        } else if (traders.get(position).getOwnerStatus() != null && traders.get(position).getOwnerStatus().equals(context.getResources().getString(R.string.yes))) {
            existingTradeDetailsViewNewBinding.ownerStatusYes.setChecked(true);
            existingTradeDetailsViewNewBinding.ownerStatusNo.setChecked(false);
            existingTradeDetailsViewNewBinding.chooseFileLayout.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.yes.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.no.setVisibility(View.GONE);
        } else {
            existingTradeDetailsViewNewBinding.ownerStatusYes.setChecked(false);
            existingTradeDetailsViewNewBinding.ownerStatusNo.setChecked(false);
            existingTradeDetailsViewNewBinding.chooseFileLayout.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.yes.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.no.setVisibility(View.GONE);
        }


        if (traders.get(position).getPropertyStatus() != null && traders.get(position).getPropertyStatus().equals(context.getResources().getString(R.string.yes))) {
            existingTradeDetailsViewNewBinding.propertyTaxYes.setChecked(true);
            existingTradeDetailsViewNewBinding.propertyTaxNo.setChecked(false);
            existingTradeDetailsViewNewBinding.propertyTaxAssessmentLayout.setVisibility(View.VISIBLE);
            existingTradeDetailsViewNewBinding.propertyTaxAssessmentNumber.setText(traders.get(position).getPropertyTaxAssessmentNumber());
            existingTradeDetailsViewNewBinding.propertyYes.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.propertyNo.setVisibility(View.GONE);
        } else if (traders.get(position).getPropertyStatus() != null && traders.get(position).getPropertyStatus().equals(context.getResources().getString(R.string.no))) {
            existingTradeDetailsViewNewBinding.propertyTaxYes.setChecked(false);
            existingTradeDetailsViewNewBinding.propertyTaxNo.setChecked(true);
            existingTradeDetailsViewNewBinding.propertyTaxAssessmentLayout.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.propertyYes.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.propertyNo.setVisibility(View.GONE);
        } else {
            existingTradeDetailsViewNewBinding.propertyTaxYes.setChecked(false);
            existingTradeDetailsViewNewBinding.propertyTaxNo.setChecked(false);
            existingTradeDetailsViewNewBinding.propertyTaxAssessmentLayout.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.propertyYes.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.propertyNo.setVisibility(View.GONE);
        }

        if (traders.get(position).getProfesstionlStatus() != null && traders.get(position).getProfesstionlStatus().equals(context.getResources().getString(R.string.yes))) {
            existingTradeDetailsViewNewBinding.professionalTaxYes.setChecked(true);
            existingTradeDetailsViewNewBinding.professionalTaxNo.setChecked(false);
            existingTradeDetailsViewNewBinding.professionalTaxLayout.setVisibility(View.VISIBLE);
            existingTradeDetailsViewNewBinding.professionalYes.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.professionalNo.setVisibility(View.GONE);

        } else if (traders.get(position).getProfesstionlStatus() != null && traders.get(position).getProfesstionlStatus().equals(context.getResources().getString(R.string.no))) {
            existingTradeDetailsViewNewBinding.professionalTaxYes.setChecked(false);
            existingTradeDetailsViewNewBinding.professionalTaxNo.setChecked(true);
            existingTradeDetailsViewNewBinding.professionalTaxLayout.setVisibility(View.VISIBLE);
            existingTradeDetailsViewNewBinding.professionalYes.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.professionalNo.setVisibility(View.GONE);
        } else {
            existingTradeDetailsViewNewBinding.professionalTaxYes.setChecked(false);
            existingTradeDetailsViewNewBinding.professionalTaxNo.setChecked(false);
            existingTradeDetailsViewNewBinding.professionalTaxLayout.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.professionalYes.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.professionalNo.setVisibility(View.GONE);
        }
        existingTradeDetailsViewNewBinding.tv1.setText(context.getResources().getString(R.string.amount_to_pay) + " " + "\u20b9" + " " + (traders.get(position).getTraders_rate()));
//        setDisableToFields();
//        setEnableToFields();
    }

    public void getTradeImage() {
        if(Utils.isOnline()) {
        try {
            new ApiService(this).makeJSONObjectRequest("TraderImage", Api.Method.POST, UrlGenerator.TradersUrl(),
                    traderImageJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        } else {
            Utils.showAlert(this,getApplicationContext().getResources().getString(R.string.no_internet_connection));
        }
    }

    public void getFieldDocument() {
        if(Utils.isOnline()) {
        try {
            new ApiService(this).makeJSONObjectRequest("TraderDocument", Api.Method.POST, UrlGenerator.TradersUrl(),
                    traderDocumentJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } else {
        Utils.showAlert(this,getApplicationContext().getResources().getString(R.string.no_internet_connection));
    }
    }

    public JSONObject traderDocumentJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector),
                traderDocumentParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("TraderDocument", "" + dataSet);
        return dataSet;
    }

    public JSONObject traderDocumentParams() throws JSONException{
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "TradersRentLeaseAgrement");
        dataSet.put("tradersdetails_id",traders.get(position).getTradersdetails_id());
        return dataSet;
    }
    public JSONObject traderImageJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector),
                traderImageParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("TraderImage", "" + dataSet);
        return dataSet;
    }

    public JSONObject traderImageParams() throws JSONException{
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "TradersShopImage");
        dataSet.put("tradersdetails_id",traders.get(position).getTradersdetails_id());
        return dataSet;
    }


    private void setDisableToFields() {
        existingTradeDetailsViewNewBinding.tradeCodeSpinner.setEnabled(false);
        existingTradeDetailsViewNewBinding.date.setEnabled(false);
        existingTradeDetailsViewNewBinding.licenceType.setEnabled(false);
        existingTradeDetailsViewNewBinding.tradeDescription.setEnabled(false);
        existingTradeDetailsViewNewBinding.applicantName.setEnabled(false);
        existingTradeDetailsViewNewBinding.applicantNameTamil.setEnabled(false);
        existingTradeDetailsViewNewBinding.gender.setEnabled(false);
        existingTradeDetailsViewNewBinding.age.setEnabled(false);
        existingTradeDetailsViewNewBinding.fatherHusName.setEnabled(false);
        existingTradeDetailsViewNewBinding.fatherHusNameTamil.setEnabled(false);
        existingTradeDetailsViewNewBinding.mobileNo.setEnabled(false);
        existingTradeDetailsViewNewBinding.emailId.setEnabled(false);
        existingTradeDetailsViewNewBinding.establishName.setEnabled(false);
        existingTradeDetailsViewNewBinding.wardNo.setEnabled(false);
        existingTradeDetailsViewNewBinding.streetsName.setEnabled(false);
        existingTradeDetailsViewNewBinding.doorNo.setEnabled(false);
        existingTradeDetailsViewNewBinding.licenceValidity.setEnabled(false);
        existingTradeDetailsViewNewBinding.descriptionEnglish.setEnabled(false);
        existingTradeDetailsViewNewBinding.descriptionTamil.setEnabled(false);
        existingTradeDetailsViewNewBinding.remarksTxInp.setEnabled(false);
        existingTradeDetailsViewNewBinding.propertyAssessmentNoTxInp.setEnabled(false);
        existingTradeDetailsViewNewBinding.annualSale.setEnabled(false);
        existingTradeDetailsViewNewBinding.motorRangeSpinner.setEnabled(false);
        existingTradeDetailsViewNewBinding.generatorSpinner.setEnabled(false);

        existingTradeDetailsViewNewBinding.tradersCodeTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.tradeDescriptionTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.descriptionEnglishTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.descriptionTamilTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.applicantNameTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.applicantNameTamilTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.ageTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.fatherHusNameTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.fatherHusNameTamilTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.mobileNoTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.emailIdTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.establishNameTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.doorNoTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.remarksTxInp.setHintEnabled(false);
        existingTradeDetailsViewNewBinding.propertyAssessmentNoTxInp.setHintEnabled(false);

        existingTradeDetailsViewNewBinding.tradersCodeTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.tradeDescriptionTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.descriptionEnglishTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.descriptionTamilTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.applicantNameTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.applicantNameTamilTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.ageTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.fatherHusNameTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.fatherHusNameTamilTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.mobileNoTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.emailIdTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.establishNameTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.doorNoTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.remarksTxInp.setHintAnimationEnabled(false);
        existingTradeDetailsViewNewBinding.propertyAssessmentNoTxInp.setHintAnimationEnabled(false);

        existingTradeDetailsViewNewBinding.btnRegister.setVisibility(View.GONE);
        existingTradeDetailsViewNewBinding.fab.setVisibility(View.GONE);
        existingTradeDetailsViewNewBinding.isPaid.setEnabled(false);
        existingTradeDetailsViewNewBinding.calendarIcon.setEnabled(false);
        existingTradeDetailsViewNewBinding.calendarIcon.setClickable(false);
        existingTradeDetailsViewNewBinding.ownerStatusYes.setEnabled(false);
        existingTradeDetailsViewNewBinding.ownerStatusNo.setEnabled(false);
        existingTradeDetailsViewNewBinding.motorAvilableStatusYes.setEnabled(false);
        existingTradeDetailsViewNewBinding.motorAvilableStatusNo.setEnabled(false);
        existingTradeDetailsViewNewBinding.geneartorAvilableStatusYes.setEnabled(false);
        existingTradeDetailsViewNewBinding.generatorAvilableStatusNo.setEnabled(false);
        existingTradeDetailsViewNewBinding.professionalTaxYes.setEnabled(false);
        existingTradeDetailsViewNewBinding.professionalTaxNo.setEnabled(false);
        existingTradeDetailsViewNewBinding.propertyTaxYes.setEnabled(false);
        existingTradeDetailsViewNewBinding.propertyTaxNo.setEnabled(false);
        existingTradeDetailsViewNewBinding.generatorSpinner.getBackground().setColorFilter(getResources().getColor(R.color.grey2), PorterDuff.Mode.SRC_ATOP);
        existingTradeDetailsViewNewBinding.viewDoc.setVisibility(View.VISIBLE);
        existingTradeDetailsViewNewBinding.leaseAggrementLayout.setVisibility(View.GONE);
        closeKeyboard();
    }

    private void setEnableToFields() {
        existingTradeDetailsViewNewBinding.tradeCodeSpinner.setEnabled(true);
        existingTradeDetailsViewNewBinding.date.setEnabled(true);
        existingTradeDetailsViewNewBinding.licenceType.setEnabled(true);
        existingTradeDetailsViewNewBinding.tradeDescription.setEnabled(true);
        existingTradeDetailsViewNewBinding.applicantName.setEnabled(true);
        existingTradeDetailsViewNewBinding.applicantNameTamil.setEnabled(true);
        existingTradeDetailsViewNewBinding.gender.setEnabled(true);
        existingTradeDetailsViewNewBinding.age.setEnabled(true);
        existingTradeDetailsViewNewBinding.fatherHusName.setEnabled(true);
        existingTradeDetailsViewNewBinding.fatherHusNameTamil.setEnabled(true);
        existingTradeDetailsViewNewBinding.mobileNo.setEnabled(true);
        existingTradeDetailsViewNewBinding.emailId.setEnabled(true);
        existingTradeDetailsViewNewBinding.establishName.setEnabled(true);
        existingTradeDetailsViewNewBinding.wardNo.setEnabled(true);
        existingTradeDetailsViewNewBinding.streetsName.setEnabled(true);
        existingTradeDetailsViewNewBinding.doorNo.setEnabled(true);
        existingTradeDetailsViewNewBinding.licenceValidity.setEnabled(true);
        existingTradeDetailsViewNewBinding.descriptionEnglish.setEnabled(true);
        existingTradeDetailsViewNewBinding.descriptionTamil.setEnabled(true);
        existingTradeDetailsViewNewBinding.remarksTxInp.setEnabled(true);
        existingTradeDetailsViewNewBinding.propertyAssessmentNoTxInp.setEnabled(true);
        existingTradeDetailsViewNewBinding.annualSale.setEnabled(true);
        existingTradeDetailsViewNewBinding.motorRangeSpinner.setEnabled(true);
        existingTradeDetailsViewNewBinding.generatorSpinner.setEnabled(true);

        existingTradeDetailsViewNewBinding.tradersCodeTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.tradeDescriptionTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.descriptionEnglishTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.descriptionTamilTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.applicantNameTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.applicantNameTamilTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.ageTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.fatherHusNameTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.fatherHusNameTamilTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.mobileNoTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.emailIdTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.establishNameTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.doorNoTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.remarksTxInp.setHintEnabled(true);
        existingTradeDetailsViewNewBinding.propertyAssessmentNoTxInp.setHintEnabled(true);

        existingTradeDetailsViewNewBinding.tradersCodeTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.tradeDescriptionTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.descriptionEnglishTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.descriptionTamilTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.applicantNameTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.applicantNameTamilTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.ageTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.fatherHusNameTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.fatherHusNameTamilTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.mobileNoTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.emailIdTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.establishNameTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.doorNoTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.remarksTxInp.setHintAnimationEnabled(true);
        existingTradeDetailsViewNewBinding.propertyAssessmentNoTxInp.setHintAnimationEnabled(true);

        existingTradeDetailsViewNewBinding.btnRegister.setVisibility(View.VISIBLE);
        existingTradeDetailsViewNewBinding.fab.setVisibility(View.VISIBLE);
        existingTradeDetailsViewNewBinding.isPaid.setEnabled(true);
        existingTradeDetailsViewNewBinding.calendarIcon.setEnabled(true);
        existingTradeDetailsViewNewBinding.calendarIcon.setClickable(true);
        existingTradeDetailsViewNewBinding.ownerStatusYes.setEnabled(true);
        existingTradeDetailsViewNewBinding.ownerStatusNo.setEnabled(true);
        existingTradeDetailsViewNewBinding.motorAvilableStatusYes.setEnabled(true);
        existingTradeDetailsViewNewBinding.motorAvilableStatusNo.setEnabled(true);
        existingTradeDetailsViewNewBinding.geneartorAvilableStatusYes.setEnabled(true);
        existingTradeDetailsViewNewBinding.generatorAvilableStatusNo.setEnabled(true);
        existingTradeDetailsViewNewBinding.professionalTaxYes.setEnabled(true);
        existingTradeDetailsViewNewBinding.professionalTaxNo.setEnabled(true);
        existingTradeDetailsViewNewBinding.propertyTaxYes.setEnabled(true);
        existingTradeDetailsViewNewBinding.propertyTaxNo.setEnabled(true);

        closeKeyboard();
    }

    private void closeKeyboard() {
        try {
            if (ExistTradeViewClass.this != null) {
                // Check if no view has focus:
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.owner_status_no:
                if (compoundButton.isChecked()) {
                    owner_status_text = context.getResources().getString(R.string.n);
                    existingTradeDetailsViewNewBinding.ownerStatusYes.setChecked(false);
                    existingTradeDetailsViewNewBinding.chooseFileLayout.setVisibility(View.VISIBLE);

                } else {
                    owner_status_text = context.getResources().getString(R.string.y);
                    existingTradeDetailsViewNewBinding.ownerStatusYes.setChecked(true);
                    existingTradeDetailsViewNewBinding.chooseFileLayout.setVisibility(View.GONE);
                    fileString = "";
                    existingTradeDetailsViewNewBinding.fileLocation.setText("");
                }
                break;

            case R.id.owner_status_yes:
                if (compoundButton.isChecked()) {
                    owner_status_text = context.getResources().getString(R.string.y);
                    existingTradeDetailsViewNewBinding.ownerStatusNo.setChecked(false);
                    existingTradeDetailsViewNewBinding.chooseFileLayout.setVisibility(View.GONE);
                    fileString = "";
                    existingTradeDetailsViewNewBinding.fileLocation.setText("");
                } else {
                    owner_status_text = context.getResources().getString(R.string.n);
                    existingTradeDetailsViewNewBinding.ownerStatusNo.setChecked(true);
                    existingTradeDetailsViewNewBinding.chooseFileLayout.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.geneartor_avilable_status_yes:
                if (compoundButton.isChecked()) {
                    generator_available_status_text = context.getResources().getString(R.string.y);
                    existingTradeDetailsViewNewBinding.generatorAvilableStatusNo.setChecked(false);
                    existingTradeDetailsViewNewBinding.generatorSpinnerLayout.setVisibility(View.VISIBLE);
//                    existingTradeDetailsViewNewBinding.generatorSpinner.setAdapter(generatorRangeArray);
                    existingTradeDetailsViewNewBinding.generatorSpinner.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, generatorItems));

                } else {
                    generator_available_status_text = context.getResources().getString(R.string.n);
                    existingTradeDetailsViewNewBinding.generatorAvilableStatusNo.setChecked(true);
                    existingTradeDetailsViewNewBinding.generatorSpinnerLayout.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.generatorSpinner.setAdapter(null);
                    selectedGeneratorId = null;
                    selectedGeneratorRange = "";
                    amountTextShow();
                }
                break;

            case R.id.generator_avilable_status_no:
                if (compoundButton.isChecked()) {
                    generator_available_status_text = context.getResources().getString(R.string.n);
                    existingTradeDetailsViewNewBinding.geneartorAvilableStatusYes.setChecked(false);
                    existingTradeDetailsViewNewBinding.generatorSpinnerLayout.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.generatorSpinner.setAdapter(null);
                    selectedGeneratorId = null;
                    selectedGeneratorRange = "";
                    amountTextShow();
                } else {
                    generator_available_status_text = context.getResources().getString(R.string.y);
                    existingTradeDetailsViewNewBinding.geneartorAvilableStatusYes.setChecked(true);
                    existingTradeDetailsViewNewBinding.generatorSpinnerLayout.setVisibility(View.VISIBLE);
//                    existingTradeDetailsViewNewBinding.generatorSpinner.setAdapter(generatorRangeArray);
                    existingTradeDetailsViewNewBinding.generatorSpinner.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, generatorItems));

                }
                break;

            case R.id.motor_avilable_status_yes:
                if (compoundButton.isChecked()) {
                    motor_available_status_text = context.getResources().getString(R.string.y);
                    existingTradeDetailsViewNewBinding.motorAvilableStatusNo.setChecked(false);
                    existingTradeDetailsViewNewBinding.motorSpinnerLayout.setVisibility(View.VISIBLE);
//                    existingTradeDetailsViewNewBinding.motorRangeSpinner.setAdapter(motorRangeArray);
                    existingTradeDetailsViewNewBinding.motorRangeSpinner.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, motorRangeItems));

                } else {
                    motor_available_status_text = context.getResources().getString(R.string.n);
                    existingTradeDetailsViewNewBinding.motorAvilableStatusNo.setChecked(true);
                    existingTradeDetailsViewNewBinding.motorSpinnerLayout.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.motorRangeSpinner.setAdapter(null);
                    selectedMotorId = null;
                    selectedMotorRange = "";
                    amountTextShow();
                }
                break;

            case R.id.motor_avilable_status_no:
                if (compoundButton.isChecked()) {
                    motor_available_status_text = context.getResources().getString(R.string.n);
                    existingTradeDetailsViewNewBinding.motorAvilableStatusYes.setChecked(false);
                    existingTradeDetailsViewNewBinding.motorSpinnerLayout.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.motorRangeSpinner.setAdapter(null);
                    selectedMotorId = null;
                    selectedMotorRange = "";
                    amountTextShow();
                } else {
                    motor_available_status_text = context.getResources().getString(R.string.y);
                    existingTradeDetailsViewNewBinding.motorAvilableStatusYes.setChecked(true);
                    existingTradeDetailsViewNewBinding.motorSpinnerLayout.setVisibility(View.VISIBLE);
//                    existingTradeDetailsViewNewBinding.motorRangeSpinner.setAdapter(motorRangeArray);
                    existingTradeDetailsViewNewBinding.motorRangeSpinner.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, motorRangeItems));

                }
                break;

            case R.id.professional_tax_yes:
                if (compoundButton.isChecked()) {
                    professional_tax_paid_status_text = context.getResources().getString(R.string.y);
                    existingTradeDetailsViewNewBinding.professionalTaxNo.setChecked(false);
                } else {
                    professional_tax_paid_status_text = context.getResources().getString(R.string.n);
                    existingTradeDetailsViewNewBinding.professionalTaxNo.setChecked(true);
                }
                break;

            case R.id.professional_tax_no:
                if (compoundButton.isChecked()) {
                    professional_tax_paid_status_text = context.getResources().getString(R.string.n);
                    existingTradeDetailsViewNewBinding.professionalTaxYes.setChecked(false);
                } else {
                    professional_tax_paid_status_text = context.getResources().getString(R.string.y);
                    existingTradeDetailsViewNewBinding.professionalTaxYes.setChecked(true);
                }
                break;

            case R.id.property_tax_yes:
                if (compoundButton.isChecked()) {
                    property_tax_paid_status_text = context.getResources().getString(R.string.y);
                    existingTradeDetailsViewNewBinding.propertyTaxNo.setChecked(false);
                    existingTradeDetailsViewNewBinding.propertyTaxAssessmentLayout.setVisibility(View.VISIBLE);

                } else {
                    property_tax_paid_status_text = context.getResources().getString(R.string.n);
                    existingTradeDetailsViewNewBinding.propertyTaxNo.setChecked(true);
                    existingTradeDetailsViewNewBinding.propertyTaxAssessmentLayout.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.propertyTaxAssessmentNumber.setText("");
                }
                break;

            case R.id.property_tax_no:
                if (compoundButton.isChecked()) {
                    property_tax_paid_status_text = context.getResources().getString(R.string.n);
                    existingTradeDetailsViewNewBinding.propertyTaxYes.setChecked(false);
                    existingTradeDetailsViewNewBinding.propertyTaxAssessmentLayout.setVisibility(View.GONE);
                } else {
                    professional_tax_paid_status_text = context.getResources().getString(R.string.y);
                    existingTradeDetailsViewNewBinding.propertyTaxYes.setChecked(true);
                    existingTradeDetailsViewNewBinding.propertyTaxAssessmentLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.is_paid:
                if (compoundButton.isChecked()) {
                    paymentStatus = context.getResources().getString(R.string.paid);
                } else {
                    paymentStatus = context.getResources().getString(R.string.unPaid);
                }
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        if (parent == existingTradeDetailsViewNewBinding.gender) {
            String gender = parent.getSelectedItem().toString();
            selectedGender = gender;
            ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));

            for (Map.Entry<String, String> entry : spinnerMap.entrySet()) {
                if (entry.getValue() == selectedGender) {
                    selectedGenderId = entry.getKey();
                    break;
                }
            }
            System.out.println("selectedGenderId >> " + selectedGenderId);
            System.out.println("selectedGender >> " + selectedGender);

        } else if (parent == existingTradeDetailsViewNewBinding.licenceValidity) {
            String finYear = parent.getSelectedItem().toString();
            selectedFinName = finYear;
            ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));

            for (Map.Entry<String, String> entry : spinnerMapFinYear.entrySet()) {
                if (entry.getValue() == selectedFinName) {
                    selectedFinId = entry.getKey();
                    break;
                }
            }
        } else if (parent == existingTradeDetailsViewNewBinding.tradeCodeSpinner) {
            String tradeCode = parent.getSelectedItem().toString();
            String tradeID = spinnerTradeCode.get(parent.getSelectedItemPosition());
            selectedTrdeCodeDetailsID = tradeID;
            ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));

            System.out.println("tradeCode>> " + tradeCode);
            System.out.println("TradeId>> " + selectedTrdeCodeDetailsID);
            selectedTradeCode = tradeCode;

        } else if (parent == existingTradeDetailsViewNewBinding.licenceType) {
            String LicenceTypeName = parent.getSelectedItem().toString();
            selectedLicenceTypeName = LicenceTypeName;
            ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));

            for (Map.Entry<String, String> entry : spinnerMapLicenceType.entrySet()) {
                if (entry.getValue() == selectedLicenceTypeName) {
                    selectedLicenceTpeId = entry.getKey();
                    break;
                }
            }
            if (selectedLicenceTpeId != null && !selectedLicenceTypeName.equals(context.getResources().getString(R.string.select_Licence_Type))) {
                existingTradeDetailsViewNewBinding.annualSale.setEnabled(true);
                existingTradeDetailsViewNewBinding.motorRangeSpinner.setEnabled(true);
                existingTradeDetailsViewNewBinding.generatorSpinner.setEnabled(true);
                LoadAnnualSaleListSpinner();
                LoadGeneratorRangeListSpinner();
                LoadMotorRangeListSpinner();
            }

            if (selectedLicenceTypeName.equals(context.getResources().getString(R.string.industrial))) {
                existingTradeDetailsViewNewBinding.annualSaleTextId.setText(context.getResources().getString(R.string.production));
            } else if (selectedLicenceTypeName.equals(context.getResources().getString(R.string.commercial))) {
                existingTradeDetailsViewNewBinding.annualSaleTextId.setText(context.getResources().getString(R.string.annual_Sale));
            } else if (selectedLicenceTypeName.equals(context.getResources().getString(R.string.select_Licence_Type))) {
                existingTradeDetailsViewNewBinding.annualSale.setAdapter(null);
                existingTradeDetailsViewNewBinding.motorRangeSpinner.setAdapter(null);
                existingTradeDetailsViewNewBinding.generatorSpinner.setAdapter(null);
                existingTradeDetailsViewNewBinding.generatorAvilableStatusNo.setChecked(false);
                existingTradeDetailsViewNewBinding.geneartorAvilableStatusYes.setChecked(false);
                existingTradeDetailsViewNewBinding.motorAvilableStatusYes.setChecked(false);
                existingTradeDetailsViewNewBinding.motorAvilableStatusNo.setChecked(false);
                amountTextShow();
            }

        } else if (parent == existingTradeDetailsViewNewBinding.wardNo) {
            String ward = parent.getSelectedItem().toString();
            selectedWardName = ward;
            ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));

            for (Map.Entry<String, String> entry : spinnerMapWard.entrySet()) {
                if (entry.getValue() == selectedWardName) {
                    selectedWardId = entry.getKey();
                    break;
                }
            }

            if (selectedWardId != null && !selectedWardName.equals(context.getResources().getString(R.string.select_Ward))) {
                if (wardFlag) {
                    LoadStreetSpinner(selectedWardId, "");
                } else {
                }
                System.out.println("selectedWardId >> " + selectedWardId);
                if (!flag) {
                    wardFlag = false;
                    LoadStreetSpinner(selectedWardId, "");
                } else {
                    wardFlag = true;
                }
            } else {
                existingTradeDetailsViewNewBinding.streetsName.setAdapter(null);
            }

        } else if (parent == existingTradeDetailsViewNewBinding.streetsName) {
            String street = parent.getSelectedItem().toString();
            selectedStreetName = street;
            ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));

            for (Map.Entry<String, String> entry : spinnerMapStreets.entrySet()) {
                if (entry.getValue() == selectedStreetName) {
                    selectedStreetId = entry.getKey();
                    break;
                }
            }

        } else if (parent == existingTradeDetailsViewNewBinding.annualSale) {
            String annual_sale = parent.getSelectedItem().toString();
            selectedAnnualSale = annual_sale;
            ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));

            // iterate each entry of hashmap
            for (Map.Entry<String, String> entry : spinnerMapAnnualSale.entrySet()) {
                // if give value is equal to value from entry
                // print the corresponding key
                if (entry.getValue() == selectedAnnualSale) {
                    selectedAnnualId = entry.getKey();
                    break;
                }
            }
            if (selectedAnnualSale.equals(context.getResources().getString(R.string.select_AnnualSale))) {
                selectedAnnualId = null;
                amountTextShow();
            } else {
                amountTextShow();
            }


        } else if (parent == existingTradeDetailsViewNewBinding.motorRangeSpinner) {
            String motor_range = parent.getSelectedItem().toString();
            selectedMotorRange = motor_range;
            ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));

            // iterate each entry of hashmap
            for (Map.Entry<String, String> entry : spinnerMapMotorRange.entrySet()) {
                // if give value is equal to value from entry
                // print the corresponding key
                if (entry.getValue() == selectedMotorRange) {
                    selectedMotorId = entry.getKey();
                    break;
                }
            }

            if (selectedMotorRange.equals(context.getResources().getString(R.string.select_Motor_Range))) {
                selectedMotorId = null;
                amountTextShow();
            } else {
                amountTextShow();
            }

        } else if (parent == existingTradeDetailsViewNewBinding.generatorSpinner) {
            String generator_range = parent.getSelectedItem().toString();
            selectedGeneratorRange = generator_range;
            ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.grey2));

            // iterate each entry of hashmap
            for (Map.Entry<String, String> entry : spinnerMapGeneratorRange.entrySet()) {
                // if give value is equal to value from entry
                // print the corresponding key
                if (entry.getValue() == selectedGeneratorRange) {
                    selectedGeneratorId = entry.getKey();
                    break;
                }
            }

            if (selectedGeneratorRange.equals(context.getResources().getString(R.string.select_Generator_Range))) {
                selectedGeneratorId = null;
                amountTextShow();
            } else {
                amountTextShow();
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        /*switch (view.getId()){
            case R.id.applicant_name:
                if(existingTradeDetailsViewNewBinding.applicantName.getText().toString().trim().length() == 0){
                    existingTradeDetailsViewNewBinding.applicantNameTxInp.setError("Please Enter Name");
                }
                else { existingTradeDetailsViewNewBinding.applicantNameTxInp.setError(null); }
        }*/
    }

    public void showDatePickerDialog() {
      /*  DialogFragment newFragment = new datePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
*/
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        //following line to restrict future date selection
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        existingTradeDetailsViewNewBinding.date.setText(sdf.format(myCalendar.getTime()));
    }


  /*  public static class datePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        static Calendar cldr = Calendar.getInstance();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(), this, year,
                    month, day);
            cldr.set(year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Do something with the date chosen by the user
            // date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            String start_date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
            cldr.set(Calendar.YEAR, year);
            cldr.set(Calendar.MONTH, (monthOfYear));
            cldr.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Log.d("startdate", "" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            try {
                dateformate(start_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }
*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();
            String status;
            if ("SaveLicenseTraders".equals(urlType) && responseObj != null) {
                String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                JSONObject jsonObject = new JSONObject(userDataDecrypt);
                status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                Log.d("Response", "" + userDataDecrypt);

                if (status.equalsIgnoreCase("SUCCESS")) {

                    Dashboard.db.delete(DBHelper.SAVE_TRADE_IMAGE, AppConstant.MOBILE + "=?", new String[]{mobileNumber});
                    //Dashboard.db.delete(DBHelper.SAVE_NEW_TRADER_DETAILS, AppConstant.MOBILE + "=?", new String[]{traders.get(position).getMobileno()});
                    Utils.showToast(this, Utils.NotNullString(jsonObject.getString("MESSAGE")));
//                    Utils.showAlert(this,  Utils.NotNullString(jsonObject.getString("MESSAGE")));
//                    Utils.showAlert(this,  Utils.NotNullString(jsonObject.getString("MESSAGE_TA")));
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            //OnBackPressed();
                            dashboard();
                            finish();
                        }
                    };
                    handler.postDelayed(runnable, 3000);


                } else if (status.equalsIgnoreCase("FAILD")) {
//                    Utils.showAlert(this,  Utils.NotNullString(jsonObject.getString("MESSAGE")));
                    Utils.showToast(this, Utils.NotNullString(jsonObject.getString("MESSAGE")));

                } else {
//                    Utils.showAlert(this,  Utils.NotNullString(jsonObject.getString("MESSAGE")));
                    Utils.showToast(this, Utils.NotNullString(jsonObject.getString("MESSAGE")));

                }
            }

            if ("TraderDocument".equals(urlType) && responseObj != null) {
                try{
                    String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                    String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                    Log.d("TraderDocumentuser_data", "" + user_data);
                    Log.d("TraderDocumentDatadecry", "" + userDataDecrypt);
                    JSONObject jsonObject = new JSONObject(userDataDecrypt);

                    status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    if (status.equalsIgnoreCase("SUCCESS") ) {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1=jsonObject.getJSONObject("DATA");
                        if(jsonObject1.getString("rentleaseagrement_available").equals("Y")) {
                            DocumentString = jsonObject1.getString("rentleaseagrement");
                            Log.d("TraderDocument", "" + jsonObject);

                            viewDocument();
                        }
                        else {
                            Utils.showAlert(this,getApplicationContext().getResources().getString(R.string.no_RECORD_FOUND));
                        }


                    }
                    else if(status.equalsIgnoreCase("FAILD") ) {
                        Utils.showAlert(this,jsonObject.getString("MESSAGE"));
                    }
                }catch (Exception e){}
            }
            if ("TraderImage".equals(urlType) && responseObj != null) {

                try {
                    String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                    String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                    Log.d("TraderImageDatadecry", "" + userDataDecrypt);
                    JSONObject jsonObject = new JSONObject(userDataDecrypt);

                    // status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    if (status.equalsIgnoreCase("SUCCESS")) {
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1 = jsonObject.getJSONObject("DATA");
                        if(jsonObject1.getString("tradersshopimage_available").equals("Y")) {
                            TraderImageString = jsonObject1.getString("tradersshopimage");
                            showImage();
                            Log.d("TraderImage", "" + jsonObject);
                        }
                        else {
                            Utils.showAlert(this,getApplicationContext().getResources().getString(R.string.no_RECORD_FOUND));
                        }

                    } else if (status.equalsIgnoreCase("FAILD")) {
                        Utils.showAlert(this, jsonObject.getString("MESSAGE"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void LoadGenderSpinner() {
        genders = new ArrayList<Gender>();
        String select_query = "SELECT *FROM " + DBHelper.GENDER_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    Gender commonModel = new Gender();
                    commonModel.setGender_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.GENDER_CODE)));
                    commonModel.setGender_name_en(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.GENDER_EN)));
                    commonModel.setGender_name_ta(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.GENDER_TA)));
                    genders.add(commonModel);
                } while (cursor.moveToNext());
            }
        }
        Collections.sort(genders, (lhs, rhs) -> lhs.getGender_name_en().toLowerCase().compareTo(rhs.getGender_name_en().toLowerCase()));

        if (genders != null && genders.size() > 0) {

            spinnerMap = new HashMap<String, String>();
            spinnerMap.put(null, null);
            final String[] items = new String[genders.size() + 1];
            items[0] = context.getResources().getString(R.string.select_Gender);
            for (int i = 0; i < genders.size(); i++) {
                spinnerMap.put(genders.get(i).getGender_code(), genders.get(i).getGender_name_en());
                String Class = genders.get(i).getGender_name_en();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    genderArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    /*
                    genderArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeDetailsViewNewBinding.gender.setAdapter(genderArray);
                    existingTradeDetailsViewNewBinding.gender.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    existingTradeDetailsViewNewBinding.gender.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                    selectedGenderId = "0";
                    selectedGender = "";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }

    }

    private void LoadStreetSpinner(String selectedWardId, String streetName) {
        streets = new ArrayList<CommonModel>();
        String select_query = "SELECT *FROM " + DBHelper.STREET_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    CommonModel commonModel = new CommonModel();
                    commonModel.setStatecode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.KEY_STATE_CODE))));
                    commonModel.setDcode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE))));
                    commonModel.setLbcode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.LB_CODE))));
                    commonModel.setWard_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_CODE)));
                    commonModel.setWard_id(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.WARD_ID))));
                    commonModel.setStreetid(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.STREET_ID))));
                    commonModel.setStreet_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STREET_CODE)));
                    commonModel.setStreet_name_ta(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STREET_NAME_TA)));
                    commonModel.setStreet_name_en(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STREET_NAME_EN)));
                    commonModel.setBuilding_zone(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.BUILDING_ZONE)));
                    streets.add(commonModel);
                } while (cursor.moveToNext());
            }
        }
        Collections.sort(streets, (lhs, rhs) -> lhs.getStreet_name_ta().toLowerCase().compareTo(rhs.getStreet_name_ta().toLowerCase()));

        selectedStreets = new ArrayList<CommonModel>();
        for (int i = 0; i < streets.size(); i++) {
            if (streets.get(i).ward_id.equals(selectedWardId)) {
                selectedStreets.add(streets.get(i));
            } else {
            }
        }

        if (selectedStreets != null && selectedStreets.size() > 0) {

            spinnerMapStreets = new HashMap<String, String>();
            spinnerMapStreets.put(null, null);
            final String[] items = new String[selectedStreets.size() + 1];
            items[0] = context.getResources().getString(R.string.select_Street);
            for (int i = 0; i < selectedStreets.size(); i++) {
                spinnerMapStreets.put((selectedStreets.get(i).getStreetid()).toString(), selectedStreets.get(i).getStreet_name_ta());
                String Class = selectedStreets.get(i).getStreet_name_ta();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    streetArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                   /*
                    streetArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeDetailsViewNewBinding.streetsName.setAdapter(streetArray);
                    existingTradeDetailsViewNewBinding.streetsName.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    existingTradeDetailsViewNewBinding.streetsName.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                    selectedStreetId = "0";
                    selectedStreetName = streetName;
                    existingTradeDetailsViewNewBinding.streetsName.setSelection(streetArray.getPosition(selectedStreetName));

                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }

    }

    //display wards list
    public void LoadWardSpinner() {
        wards = new ArrayList<CommonModel>();
        String select_query = "SELECT *FROM " + DBHelper.WARD_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do {
                    CommonModel commonModel = new CommonModel();
                    commonModel.setStatecode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.KEY_STATE_CODE))));
                    commonModel.setDcode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE))));
                    commonModel.setLbcode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.LB_CODE))));
                    commonModel.setWard_code(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_CODE)));
                    commonModel.setWard_id(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.WARD_ID))));
                    commonModel.setWard_name_en(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_NAME_EN)));
                    commonModel.setWard_name_ta(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.WARD_NAME_TA)));
                    wards.add(commonModel);
                } while (cursor.moveToNext());
            }
        }
        Collections.sort(wards, (lhs, rhs) -> lhs.getWard_code().compareTo(rhs.getWard_code()));

        if (wards != null && wards.size() > 0) {

            spinnerMapWard = new HashMap<String, String>();
            spinnerMapWard.put(null, null);
            final String[] items = new String[wards.size() + 1];
            items[0] = context.getResources().getString(R.string.select_Ward);
            for (int i = 0; i < wards.size(); i++) {
                spinnerMapWard.put(wards.get(i).getWard_id(), wards.get(i).getWard_code());
                String Class = wards.get(i).getWard_code();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    wardArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    /*
                    wardArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeDetailsViewNewBinding.wardNo.setAdapter(wardArray);
                    existingTradeDetailsViewNewBinding.wardNo.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    existingTradeDetailsViewNewBinding.wardNo.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                    selectedWardId = "0";
                    selectedWardName = "";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }


    }

    public void LoadFinYearSpinner() {
        finYear = new ArrayList<CommonModel>();
        String select_query = "SELECT *FROM " + DBHelper.LICENCE_VALIDITY_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if (cursor.getCount() > 0) {

            if (cursor.moveToFirst()) {
                do {
                    CommonModel commonModel = new CommonModel();
                    commonModel.setFIN_YEAR_ID(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.FIN_YEAR_ID))));
                    commonModel.setFIN_YEAR(String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FIN_YEAR))));
                    commonModel.setFROM_FIN_YEAR(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.FROM_FIN_YEAR))));
                    commonModel.setFROM_FIN_MON(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.FROM_FIN_MON))));
                    commonModel.setTO_FIN_YEAR(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.TO_FIN_YEAR))));
                    commonModel.setTO_FIN_MON(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.TO_FIN_MON))));
                    finYear.add(commonModel);
                } while (cursor.moveToNext());
            }
        }
        Collections.sort(finYear, (lhs, rhs) -> lhs.getFIN_YEAR().compareTo(rhs.getFIN_YEAR()));

        if (finYear != null && finYear.size() > 0) {

            spinnerMapFinYear = new HashMap<String, String>();
            spinnerMapFinYear.put(null, null);
            final String[] items = new String[finYear.size() + 1];
            items[0] = context.getResources().getString(R.string.select_Licence_Validity);
            for (int i = 0; i < finYear.size(); i++) {
                spinnerMapFinYear.put(finYear.get(i).getFIN_YEAR_ID(), finYear.get(i).getFIN_YEAR());
                String Class = finYear.get(i).getFIN_YEAR();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    licenceValidityArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                   /*
                    licenceValidityArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeDetailsViewNewBinding.licenceValidity.setAdapter(licenceValidityArray);
                    existingTradeDetailsViewNewBinding.licenceValidity.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    existingTradeDetailsViewNewBinding.licenceValidity.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                    selectedFinId = "0";
                    selectedFinName = "";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }


    }

    private void LoadLicenceTypeSpinner() throws JSONException {
        traderLicenseTypeList = new ArrayList<CommonModel>();
        JSONArray jsonarray = new JSONArray(prefManager.getTraderLicenseTypeList());
        if (jsonarray != null && jsonarray.length() > 0) {
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String traders_license_type_id = jsonobject.getString("traders_license_type_id");
                String traders_license_type_name = jsonobject.getString("traders_license_type_name");
                CommonModel Detail = new CommonModel();
                Detail.setTraders_license_type_id(traders_license_type_id);
                Detail.setTraders_license_type_name(traders_license_type_name);
                traderLicenseTypeList.add(Detail);
            }
            Collections.sort(traderLicenseTypeList, (lhs, rhs) -> lhs.getTraders_license_type_name().toLowerCase().compareTo(rhs.getTraders_license_type_name().toLowerCase()));
        }


        if (traderLicenseTypeList != null && traderLicenseTypeList.size() > 0) {

            spinnerMapLicenceType = new HashMap<String, String>();
            spinnerMapLicenceType.put(null, null);
            final String[] items = new String[traderLicenseTypeList.size() + 1];
            items[0] = context.getResources().getString(R.string.select_Licence_Type);
            for (int i = 0; i < traderLicenseTypeList.size(); i++) {
                spinnerMapLicenceType.put(traderLicenseTypeList.get(i).getTraders_license_type_id(), traderLicenseTypeList.get(i).getTraders_license_type_name());
                String Class = traderLicenseTypeList.get(i).getTraders_license_type_name();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    licenceTypeArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                   /*
                    licenceTypeArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeDetailsViewNewBinding.licenceType.setAdapter(licenceTypeArray);
                    existingTradeDetailsViewNewBinding.licenceType.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    existingTradeDetailsViewNewBinding.licenceType.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                    selectedLicenceTpeId = "0";
                    selectedLicenceTypeName = "";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }


    }

    private void LoadTradeCodeListSpinner() {
        loadTradeCodeList = new ArrayList<>();
        String select_query = "SELECT *FROM " + DBHelper.TRADE_CODE_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    CommonModel commonModel = new CommonModel();
                    commonModel.setTRADE_DETAILS_ID(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.TRADE_DETAILS_ID))));
                    commonModel.setTRADE_RATE(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.TRADE_RATE))));
                    commonModel.setLB_TRADE_CODE(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.LB_TRADE_CODE))));
                    commonModel.setSTATECODE(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.STATECODE)));
                    commonModel.setDcode(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE))));
                    commonModel.setLICENSE_TYPE_ID(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.LICENSE_TYPE_ID))));
                    commonModel.setFINYEAR(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.FINYEAR)));
                    commonModel.setDESCRIPTION_EN(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.DESCRIPTION_EN)));
                    commonModel.setDESCRIPTION_TA(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.DESCRIPTION_TA)));
                    commonModel.setLbcode(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LB_CODE)));
                    commonModel.setDATE_FIELD(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.DATE_FIELD)));

                    loadTradeCodeList.add(commonModel);
                } while (cursor.moveToNext());
            }
        }


        if (loadTradeCodeList != null && loadTradeCodeList.size() > 0) {

            spinnerTradeCode = new HashMap<String, String>();
            spinnerTradeCode.put(null, null);
            final String[] items = new String[loadTradeCodeList.size() + 1];
            items[0] = context.getResources().getString(R.string.select_TradeCode);
            for (int i = 0; i < loadTradeCodeList.size(); i++) {
                spinnerTradeCode.put(loadTradeCodeList.get(i).getTRADE_DETAILS_ID(), loadTradeCodeList.get(i).getLB_TRADE_CODE() + " - " + loadTradeCodeList.get(i).getDESCRIPTION_EN());
                String Class = loadTradeCodeList.get(i).getLB_TRADE_CODE() + " - " + loadTradeCodeList.get(i).getDESCRIPTION_EN();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    tradeCodeSpArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    /*
                    tradeCodeSpArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeDetailsViewNewBinding.tradeCodeSpinner.setAdapter(tradeCodeSpArray);
                    existingTradeDetailsViewNewBinding.tradeCodeSpinner.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    existingTradeDetailsViewNewBinding.tradeCodeSpinner.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                    selectedTrdeCodeDetailsID = "0";
                    selectedTradeCode = "";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {
//        Utils.showAlert(this, context.getResources().getString(R.string.try_after_some_time));

    }


    @Override
    public void onBackPressed() {
        if (existingTradeDetailsViewNewBinding.documentLayout.getVisibility() == View.VISIBLE) {
            existingTradeDetailsViewNewBinding.documentLayout.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.main.setVisibility(View.VISIBLE);
        } else {
            previous();
        }
    }

    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void openDocument() {

    }

    public void closeDoc() {
        existingTradeDetailsViewNewBinding.documentLayout.setVisibility(View.GONE);
        existingTradeDetailsViewNewBinding.main.setVisibility(View.VISIBLE);
    }

    public void openCameraScreen() {
        if (selectedTradeCode != null && !existingTradeDetailsViewNewBinding.tradeCodeSpinner.getSelectedItem().toString().isEmpty()
                && !context.getResources().getString(R.string.select_TradeCode).equalsIgnoreCase(selectedTradeCode) && !existingTradeDetailsViewNewBinding.mobileNo.getText().toString().equals("")) {
            Intent intent = new Intent(this, CameraScreen.class);
            intent.putExtra(AppConstant.TRADE_CODE, existingTradeDetailsViewNewBinding.tradeCodeSpinner.getSelectedItemPosition());
            intent.putExtra(AppConstant.MOBILE, existingTradeDetailsViewNewBinding.mobileNo.getText().toString());
            intent.putExtra(AppConstant.KEY_SCREEN_STATUS, "new");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } else {
            Utils.showAlert(ExistTradeViewClass.this, context.getResources().getString(R.string.select_Trade_Code_and_Mobile_Number));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void viewDocument() {
        pageNumber=0;
        if (DocumentString != null && !DocumentString.equals("")) {
        byte[] decodedString = new byte[0];
        try {
            //byte[] name = java.util.Base64.getEncoder().encode(fileString.getBytes());
            decodedString = Base64.decode(DocumentString/*traders.get(position).getDocument().toString()*/, Base64.DEFAULT);
            System.out.println(new String(decodedString));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        existingTradeDetailsViewNewBinding.documentLayout.setVisibility(View.VISIBLE);
        existingTradeDetailsViewNewBinding.main.setVisibility(View.GONE);
        existingTradeDetailsViewNewBinding.documentViewer.fromBytes(decodedString).
                onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        pageNumber = page;
                        setTitle(String.format("%s %s / %s", "PDF", page + 1, pageCount));
                        existingTradeDetailsViewNewBinding.pageNum.setText(pageNumber + 1 + "/" + pageCount);
                    }
                }).defaultPage(pageNumber).swipeHorizontal(true).enableDoubletap(true).load();

    }else {
            Utils.showAlert(this,context.getResources().getString(R.string.no_RECORD_FOUND));
            existingTradeDetailsViewNewBinding.documentLayout.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.main.setVisibility(View.VISIBLE);
        }

}

     public void showImage(){
         if (TraderImageString/*tradersImageList.get(tradersImagePosition).getImageByte()*/ != null &&
                 !TraderImageString.equals("")) {
             byte [] encodeByte = Base64.decode(TraderImageString,Base64.DEFAULT);
             Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
             Bitmap converetdImage = Utils.getResizedBitmap(bitmap, 500);
             TraderImageString=Utils.bitmapToString(converetdImage);
             String value = new String(tradersImageList.get(tradersImagePosition).getImageByte());
             Intent intent = new Intent(this, FullImageActivity.class);
             intent.putExtra(AppConstant.TRADE_CODE, "");
             intent.putExtra(AppConstant.MOBILE, "");
             intent.putExtra(AppConstant.KEY_SCREEN_STATUS, "");
             intent.putExtra(AppConstant.TRADE_IMAGE,TraderImageString /*value*/);
             intent.putExtra("key", "ExistTradeViewClass");
             startActivity(intent);
             overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
         } else {
             Utils.showAlert(ExistTradeViewClass.this, context.getResources().getString(R.string.no_image_Found));
         }
}

        public void viewImageScreen() {
            if (getSaveTradeImageTable()==1) {
                Intent intent = new Intent(this, FullImageActivity.class);
                intent.putExtra(AppConstant.TRADE_CODE, selectedTrdeCodeDetailsID);
                intent.putExtra(AppConstant.MOBILE, existingTradeDetailsViewNewBinding.mobileNo.getText().toString());
                intent.putExtra(AppConstant.KEY_SCREEN_STATUS, "new");
                intent.putExtra("key", "NewTradeLicence");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }else {
                getTradeImage();
                }
    }

    public static void dateformate(String date1) throws ParseException {
        final String OLD_FORMAT = "yyyy-MM-dd";
        final String NEW_FORMAT = "yyyy-MM-dd";

// August 12, 2010
        String oldDateString = date1;
        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = sdf.parse(oldDateString);
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);
        date.setText(newDateString);

    }



    public int getSaveTradeImageTable(){
        image="";lat="";lan="";

        String sql = "SELECT * FROM " + DBHelper.SAVE_TRADE_IMAGE + " WHERE screen_status = 'new' and mobileno ="+existingTradeDetailsViewNewBinding.mobileNo.getText().toString();
        Cursor cursor = Dashboard.db.rawQuery(sql, null);
        Log.d("cursor_count", String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    image = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TRADE_IMAGE));
                    lat = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LATITUDE));
                    lan = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LONGITUDE));
                    Log.d("lat", "" + lat);
                    Log.d("image", "" + image);
                } while (cursor.moveToNext());
            }
        }
        if(image.getBytes().length>0) {
            return 1;
        }

        else {
            return 0;
        }
    }


    public void LoadAnnualSaleListSpinner(){
        AnnualSaleList = new ArrayList<CommonModel>();
        String select_query= "SELECT *FROM " + DBHelper.ANNUAL_SALE_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setAnnual_id(cursor.getString(cursor.getColumnIndexOrThrow("amount_range_id")));
                    commonModel.setAnnual_sale(cursor.getString(cursor.getColumnIndexOrThrow("amount_range")));
                    commonModel.setSlab_amount(cursor.getString(cursor.getColumnIndexOrThrow("slab_amount")));
                    commonModel.setTraders_license_type_id(cursor.getString(cursor.getColumnIndexOrThrow("traders_license_type_id")));

                    AnnualSaleList.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
        Collections.sort(AnnualSaleList, (lhs, rhs) -> lhs.getAnnual_sale().compareTo(rhs.getAnnual_sale()));
        filterAnnualSale = new ArrayList<CommonModel>();


        for (int i = 0; i < AnnualSaleList.size(); i++) {
            if(AnnualSaleList.get(i).getTraders_license_type_id().equals(selectedLicenceTpeId)){
                filterAnnualSale.add(AnnualSaleList.get(i));
            }else { }
        }

        if(filterAnnualSale != null && filterAnnualSale.size() >0) {

            spinnerMapAnnualSale = new HashMap<String, String>();
            spinnerMapAnnualSale.put(null, null);
            final String[] items = new String[filterAnnualSale.size() + 1];
            if(selectedLicenceTypeName.equals("Industrial")) {
                items[0] = getApplicationContext().getResources().getString(R.string.select_ProductionSale);
            }
            else {
                items[0] = getApplicationContext().getResources().getString(R.string.select_AnnualSale);
            }
            for (int i = 0; i < filterAnnualSale.size(); i++) {
                {
                    spinnerMapAnnualSale.put(filterAnnualSale.get(i).getAnnual_id(), filterAnnualSale.get(i).getAnnual_sale());
                    String Class = filterAnnualSale.get(i).getAnnual_sale();
                    items[i + 1] = Class;
                }
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    annualSaleArray = new ArrayAdapter<String>(this,android. R.layout.simple_spinner_item, items);
                   /*
                    annualSaleArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeDetailsViewNewBinding.annualSale.setAdapter(annualSaleArray);
                    existingTradeDetailsViewNewBinding.annualSale.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    existingTradeDetailsViewNewBinding.annualSale.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                    selectedAnnualId=null;
                    selectedAnnualSale="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            if(!Annualflag) {
                     try {
                        int annualPosition = annualSaleArray.getPosition(spinnerMapAnnualSale.get(traders.get(position).getAmount_range_id()));
                        if(annualPosition >= 0){
                            Annualflag=true;
                            existingTradeDetailsViewNewBinding.annualSale.setSelection(annualPosition);
                        }else {
                            existingTradeDetailsViewNewBinding.annualSale.setAdapter(null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

            }else {
                existingTradeDetailsViewNewBinding.annualSale.setSelection(0);
            }
        }else {
            selectedAnnualId=null;
            selectedAnnualSale="";
            existingTradeDetailsViewNewBinding.annualSale.setAdapter(null);
        }

    }
    public void LoadMotorRangeListSpinner(){
        motorRangeList = new ArrayList<CommonModel>();
        String select_query= "SELECT *FROM " + DBHelper.MOTOR_RANG_POWER;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setMotor_id(cursor.getString(cursor.getColumnIndexOrThrow("motor_id")));
                    commonModel.setMotor_range(cursor.getString(cursor.getColumnIndexOrThrow("motor_type_name")));
                    commonModel.setSlab_amount(cursor.getString(cursor.getColumnIndexOrThrow("slab_amount")));
                    commonModel.setTraders_license_type_id(cursor.getString(cursor.getColumnIndexOrThrow("traders_license_type_id")));


                    motorRangeList.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
        Collections.sort(motorRangeList, (lhs, rhs) -> lhs.getMotor_range().compareTo(rhs.getMotor_range()));
        filtermotorRangeList = new ArrayList<CommonModel>();

        for (int i = 0; i < motorRangeList.size(); i++) {
            if(motorRangeList.get(i).getTraders_license_type_id().equals(selectedLicenceTpeId)){
                filtermotorRangeList.add(motorRangeList.get(i));
            }else { }
        }
        if(filtermotorRangeList != null && filtermotorRangeList.size() >0) {

            spinnerMapMotorRange = new HashMap<String, String>();
            spinnerMapMotorRange.put(null, null);
            motorRangeItems = new String[filtermotorRangeList.size() + 1];
            motorRangeItems[0] = context.getResources().getString(R.string.select_Motor_Range);
            for (int i = 0; i < filtermotorRangeList.size(); i++) {
                {
                    spinnerMapMotorRange.put(filtermotorRangeList.get(i).getMotor_id(), filtermotorRangeList.get(i).getMotor_range());
                    String Class = filtermotorRangeList.get(i).getMotor_range();
                    motorRangeItems[i + 1] = Class;
                }
            }
            System.out.println("items" + motorRangeItems.toString());

            try {
                if (motorRangeItems != null && motorRangeItems.length > 0) {
                    motorRangeArray = new ArrayAdapter<String>(this,android. R.layout.simple_spinner_item, motorRangeItems);
                    /*
                    motorRangeArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeDetailsViewNewBinding.motorRangeSpinner.setAdapter(motorRangeArray);
                    existingTradeDetailsViewNewBinding.motorRangeSpinner.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    existingTradeDetailsViewNewBinding.motorRangeSpinner.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, motorRangeItems));

                    selectedMotorId=null;
                    selectedMotorRange="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            if(!Motorflag) {
                if(traders.get(position).getMotorStatus() != null && traders.get(position).getMotorStatus().equals(context.getResources().getString(R.string.yes))){
                    existingTradeDetailsViewNewBinding.motorAvilableStatusYes.setChecked(true);
                    existingTradeDetailsViewNewBinding.motorAvilableStatusNo.setChecked(false);
                    existingTradeDetailsViewNewBinding.motorSpinnerLayout.setVisibility(View.VISIBLE);
                    existingTradeDetailsViewNewBinding.motorYes.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.motorNo.setVisibility(View.GONE);
                    try {
                        int motorPosition = motorRangeArray.getPosition(spinnerMapMotorRange.get(traders.get(position).getMotor_type_id()));
                        if(motorPosition >= 0){
                            Motorflag=true;
                            existingTradeDetailsViewNewBinding.motorRangeSpinner.setSelection(motorPosition);
                        }else {
                            existingTradeDetailsViewNewBinding.motorRangeSpinner.setAdapter(null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }else  if(traders.get(position).getMotorStatus() != null && traders.get(position).getMotorStatus().equals(context.getResources().getString(R.string.no))){
                    existingTradeDetailsViewNewBinding.motorAvilableStatusYes.setChecked(false);
                    existingTradeDetailsViewNewBinding.motorAvilableStatusNo.setChecked(true);
                    existingTradeDetailsViewNewBinding.motorSpinnerLayout.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.motorYes.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.motorNo.setVisibility(View.GONE);
                }else {
                    existingTradeDetailsViewNewBinding.motorAvilableStatusYes.setChecked(false);
                    existingTradeDetailsViewNewBinding.motorAvilableStatusNo.setChecked(false);
                    existingTradeDetailsViewNewBinding.motorSpinnerLayout.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.motorYes.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.motorNo.setVisibility(View.GONE);
                }
            }else {
                existingTradeDetailsViewNewBinding.motorRangeSpinner.setSelection(0);
            }
        }else {
            selectedMotorId=null;
            selectedMotorRange="";
            existingTradeDetailsViewNewBinding.motorRangeSpinner.setAdapter(null);
        }

    }
    public void LoadGeneratorRangeListSpinner(){
        generatorRangeList = new ArrayList<CommonModel>();
        String select_query= "SELECT *FROM " + DBHelper.GENERATOR_RANGE_POWER;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){
            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setGenerator_id(cursor.getString(cursor.getColumnIndexOrThrow("generator_range_id")));
                    commonModel.setGenerator_range(cursor.getString(cursor.getColumnIndexOrThrow("generator_range_name")));
                    commonModel.setSlab_amount(cursor.getString(cursor.getColumnIndexOrThrow("slab_amount")));
                    commonModel.setTraders_license_type_id(cursor.getString(cursor.getColumnIndexOrThrow("traders_license_type_id")));


                    generatorRangeList.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
        Collections.sort(generatorRangeList, (lhs, rhs) -> lhs.getGenerator_range().compareTo(rhs.getGenerator_range()));
        filterGeneratorList = new ArrayList<CommonModel>();


        for (int i = 0; i < generatorRangeList.size(); i++) {
            if(generatorRangeList.get(i).getTraders_license_type_id().equals(selectedLicenceTpeId)){
                filterGeneratorList.add(generatorRangeList.get(i));
            }else { }
        }
        if(filterGeneratorList != null && filterGeneratorList.size() >0) {

            spinnerMapGeneratorRange = new HashMap<String, String>();
            spinnerMapGeneratorRange.put(null, null);
            generatorItems = new String[filterGeneratorList.size() + 1];
            generatorItems[0] = context.getResources().getString(R.string.select_Generator_Range);
            for (int i = 0; i < filterGeneratorList.size(); i++) {
                {
                    spinnerMapGeneratorRange.put(filterGeneratorList.get(i).getGenerator_id(), filterGeneratorList.get(i).getGenerator_range());
                    String Class = filterGeneratorList.get(i).getGenerator_range();
                    generatorItems[i + 1] = Class;
                }
            }

            System.out.println("items" + generatorItems.toString());

            try {
                if (generatorItems != null && generatorItems.length > 0) {
                    generatorRangeArray = new ArrayAdapter<String>(this,android. R.layout.simple_spinner_item, generatorItems);
                   /*
                    generatorRangeArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    existingTradeDetailsViewNewBinding.generatorSpinner.setAdapter(generatorRangeArray);
                    existingTradeDetailsViewNewBinding.generatorSpinner.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    existingTradeDetailsViewNewBinding.generatorSpinner.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, generatorItems));

                    selectedGeneratorId=null;
                    selectedGeneratorRange="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            if(!Generatorflag) {
                if(traders.get(position).getGeneratorStatus() != null && traders.get(position).getGeneratorStatus().equals(context.getResources().getString(R.string.yes))){
                    existingTradeDetailsViewNewBinding.geneartorAvilableStatusYes.setChecked(true);
                    existingTradeDetailsViewNewBinding.generatorAvilableStatusNo.setChecked(false);
                    existingTradeDetailsViewNewBinding.generatorSpinnerLayout.setVisibility(View.VISIBLE);
                    existingTradeDetailsViewNewBinding.generatorYes.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.generatorNo.setVisibility(View.GONE);
                    try {
                        int geneartorPosition = generatorRangeArray.getPosition(spinnerMapGeneratorRange.get(traders.get(position).getGenerator_range_id()));
                        if(geneartorPosition >= 0){
                            Generatorflag=true;
                            existingTradeDetailsViewNewBinding.generatorSpinner.setSelection(geneartorPosition);
                        }else {
                            existingTradeDetailsViewNewBinding.generatorSpinner.setAdapter(null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }else if(traders.get(position).getGeneratorStatus() != null && traders.get(position).getGeneratorStatus().equals(context.getResources().getString(R.string.no))){
                    existingTradeDetailsViewNewBinding.geneartorAvilableStatusYes.setChecked(false);
                    existingTradeDetailsViewNewBinding.generatorAvilableStatusNo.setChecked(true);
                    existingTradeDetailsViewNewBinding.generatorSpinnerLayout.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.generatorYes.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.generatorNo.setVisibility(View.GONE);
                }else {
                    existingTradeDetailsViewNewBinding.geneartorAvilableStatusYes.setChecked(false);
                    existingTradeDetailsViewNewBinding.generatorAvilableStatusNo.setChecked(false);
                    existingTradeDetailsViewNewBinding.generatorSpinnerLayout.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.generatorYes.setVisibility(View.GONE);
                    existingTradeDetailsViewNewBinding.generatorNo.setVisibility(View.GONE);
                }
            }else {
                existingTradeDetailsViewNewBinding.generatorSpinner.setSelection(0);
            }
        }else {
            existingTradeDetailsViewNewBinding.generatorSpinner.setAdapter(null);
            selectedGeneratorId=null;
            selectedGeneratorRange="";
        }

    }


    public String changeTextColor(String text){
        String input="<font color=" + "#878787" + ">" + text  + "</font> ";
        return input;
    }
    public void amountTextShow(){
        int annual_amount=0;
        int generator_amount=0;
        int motor_amount=0;
        if(selectedAnnualId!=null&&selectedMotorId!=null&&selectedGeneratorId!=null){
            for(int i=0;i<filterAnnualSale.size();i++){
                if(filterAnnualSale.get(i).getAnnual_id().equals(selectedAnnualId)){
                    annual_amount=Integer.parseInt(filterAnnualSale.get(i).getSlab_amount());

                }
            }
            for(int i=0;i<filtermotorRangeList.size();i++){
                if(filtermotorRangeList.get(i).getMotor_id().equals(selectedMotorId)){
                    motor_amount=Integer.parseInt(filtermotorRangeList.get(i).getSlab_amount());
                }
            }

            for(int i=0;i<filterGeneratorList.size();i++){
                if(filterGeneratorList.get(i).getGenerator_id().equals(selectedGeneratorId)){
                    generator_amount=Integer.parseInt(filterGeneratorList.get(i).getSlab_amount());
                }
            }
            //annual_amount=Integer.parseInt(filterAnnualSale.get(existingTradeDetailsViewNewBinding.annualSale.getSelectedItemPosition()+1).getSlab_amount());
            //generator_amount=Integer.parseInt(filterGeneratorList.get(existingTradeDetailsViewNewBinding.generatorSpinner.getSelectedItemPosition()+1).getSlab_amount());
            //motor_amount=Integer.parseInt(filtermotorRangeList.get(existingTradeDetailsViewNewBinding.motorRangeSpinner.getSelectedItemPosition()+1).getSlab_amount());
            existingTradeDetailsViewNewBinding.tv1.setText(context.getResources().getString(R.string.amount_to_pay)+" "+"\u20b9"+" "+(annual_amount+generator_amount+motor_amount));
        }

        else if((selectedAnnualId!=null)&&(selectedMotorId!=null)){
            for(int i=0;i<filterAnnualSale.size();i++){
                if(filterAnnualSale.get(i).getAnnual_id().equals(selectedAnnualId)){
                    annual_amount=Integer.parseInt(filterAnnualSale.get(i).getSlab_amount());

                }
            }
            for(int i=0;i<filtermotorRangeList.size();i++){
                if(filtermotorRangeList.get(i).getMotor_id().equals(selectedMotorId)){
                    motor_amount=Integer.parseInt(filtermotorRangeList.get(i).getSlab_amount());
                }
            }
            //annual_amount=Integer.parseInt(filterAnnualSale.get(existingTradeDetailsViewNewBinding.annualSale.getSelectedItemPosition()+1).getSlab_amount());
            //motor_amount=Integer.parseInt(filtermotorRangeList.get(existingTradeDetailsViewNewBinding.motorRangeSpinner.getSelectedItemPosition()+1).getSlab_amount());
            existingTradeDetailsViewNewBinding.tv1.setText(context.getResources().getString(R.string.amount_to_pay)+" "+"\u20b9"+" "+(annual_amount+generator_amount+motor_amount));
        }
        else if((selectedMotorId!=null)&&(selectedGeneratorId!=null)){
            for(int i=0;i<filtermotorRangeList.size();i++){
                if(filtermotorRangeList.get(i).getMotor_id().equals(selectedMotorId)){
                    motor_amount=Integer.parseInt(filtermotorRangeList.get(i).getSlab_amount());
                }
            }
            for(int i=0;i<filterGeneratorList.size();i++){
                if(filterGeneratorList.get(i).getGenerator_id().equals(selectedGeneratorId)){
                    generator_amount=Integer.parseInt(filterGeneratorList.get(i).getSlab_amount());
                }
            }
            //generator_amount=Integer.parseInt(filterGeneratorList.get(existingTradeDetailsViewNewBinding.generatorSpinner.getSelectedItemPosition()+1).getSlab_amount());
            //motor_amount=Integer.parseInt(filtermotorRangeList.get(existingTradeDetailsViewNewBinding.motorRangeSpinner.getSelectedItemPosition()+1).getSlab_amount());
            existingTradeDetailsViewNewBinding.tv1.setText(context.getResources().getString(R.string.amount_to_pay)+" "+"\u20b9"+" "+(annual_amount+generator_amount+motor_amount));
        }
        else if((selectedAnnualId!=null)&&(selectedGeneratorId!=null)){
            for(int i=0;i<filterAnnualSale.size();i++){
                if(filterAnnualSale.get(i).getAnnual_id().equals(selectedAnnualId)){
                    annual_amount=Integer.parseInt(filterAnnualSale.get(i).getSlab_amount());

                }
            }
            for(int i=0;i<filterGeneratorList.size();i++){
                if(filterGeneratorList.get(i).getGenerator_id().equals(selectedGeneratorId)){
                    generator_amount=Integer.parseInt(filterGeneratorList.get(i).getSlab_amount());
                }
            }
            //annual_amount=Integer.parseInt(filterAnnualSale.get(existingTradeDetailsViewNewBinding.annualSale.getSelectedItemPosition()+1).getSlab_amount());
            //generator_amount=Integer.parseInt(filterGeneratorList.get(existingTradeDetailsViewNewBinding.generatorSpinner.getSelectedItemPosition()+1).getSlab_amount());
            existingTradeDetailsViewNewBinding.tv1.setText(context.getResources().getString(R.string.amount_to_pay)+" "+"\u20b9"+" "+(annual_amount+generator_amount+motor_amount));
        }
        else if((selectedAnnualId!=null)||(selectedMotorId!=null)||(selectedGeneratorId!=null)){
            if((selectedAnnualId!=null)){
                for(int i=0;i<filterAnnualSale.size();i++){
                    if(filterAnnualSale.get(i).getAnnual_id().equals(selectedAnnualId)){
                        annual_amount=Integer.parseInt(filterAnnualSale.get(i).getSlab_amount());

                    }
                }

                existingTradeDetailsViewNewBinding.tv1.setText(context.getResources().getString(R.string.amount_to_pay)+" "+"\u20b9"+" "+(annual_amount+generator_amount+motor_amount));
            }
            else if((selectedMotorId!=null)){
                for(int i=0;i<filtermotorRangeList.size();i++){
                    if(filtermotorRangeList.get(i).getMotor_id().equals(selectedMotorId)){
                        motor_amount=Integer.parseInt(filtermotorRangeList.get(i).getSlab_amount());
                    }
                }
                existingTradeDetailsViewNewBinding.tv1.setText(context.getResources().getString(R.string.amount_to_pay)+" "+"\u20b9"+" "+(annual_amount+generator_amount+motor_amount));
            }
            else if((selectedGeneratorId!=null)){
                for(int i=0;i<filterGeneratorList.size();i++){
                    if(filterGeneratorList.get(i).getGenerator_id().equals(selectedGeneratorId)){
                        generator_amount=Integer.parseInt(filterGeneratorList.get(i).getSlab_amount());
                    }
                }
                existingTradeDetailsViewNewBinding.tv1.setText(context.getResources().getString(R.string.amount_to_pay)+" "+"\u20b9"+" "+(annual_amount+generator_amount+motor_amount));
            }
        }

        else {
            existingTradeDetailsViewNewBinding.tv1.setText(context.getResources().getString(R.string.amount_to_pay)+" "+"\u20b9"+" "+(annual_amount+generator_amount+motor_amount));
        }

    }
    public boolean ValidationFirst(){
        if(!existingTradeDetailsViewNewBinding.applicantName.getText().toString().isEmpty()){
            if(!existingTradeDetailsViewNewBinding.applicantNameTamil.getText().toString().isEmpty()){
                if (!context.getResources().getString(R.string.select_Gender).equalsIgnoreCase(selectedGender) && !existingTradeDetailsViewNewBinding.gender.getSelectedItem().toString().isEmpty()) {
                    if(!existingTradeDetailsViewNewBinding.age.getText().toString().isEmpty()&& Integer.parseInt(existingTradeDetailsViewNewBinding.age.getText().toString())>18){
                        if(!existingTradeDetailsViewNewBinding.fatherHusName.getText().toString().isEmpty()){
                            if(!existingTradeDetailsViewNewBinding.fatherHusNameTamil.getText().toString().isEmpty()){
                                if(!existingTradeDetailsViewNewBinding.mobileNo.getText().toString().isEmpty()){
                                    if (Utils.isValidMobile(existingTradeDetailsViewNewBinding.mobileNo.getText().toString())) {
                                        if(!existingTradeDetailsViewNewBinding.emailId.getText().toString().isEmpty()){
                                            if (Utils.isEmailValid(existingTradeDetailsViewNewBinding.emailId.getText().toString())) {
                                                if(!existingTradeDetailsViewNewBinding.descriptionEnglish.getText().toString().isEmpty()){
                                                    if(!existingTradeDetailsViewNewBinding.descriptionTamil.getText().toString().isEmpty()){
                                                        return true;
                                                    }
                                                    else {
                                                        Utils.showAlert(this,context.getResources().getString(R.string.enter_Description_Tamil));
                                                        existingTradeDetailsViewNewBinding.descriptionTamil.requestFocus();
                                                        return false;
                                                    }
                                                }
                                                else {
                                                    Utils.showAlert(this,context.getResources().getString(R.string.enter_Description_English));
                                                    existingTradeDetailsViewNewBinding.descriptionEnglish.requestFocus();
                                                }
                                            }
                                            else {
                                                Utils.showAlert(this,context.getResources().getString(R.string.enter_Valid_Email_ID));
                                                existingTradeDetailsViewNewBinding.emailId.requestFocus();
                                            }
                                        }
                                        else {
                                            Utils.showAlert(this,context.getResources().getString(R.string.enter_Email_ID));
                                            existingTradeDetailsViewNewBinding.emailId.requestFocus();
                                        }
                                    }
                                    else {
                                        Utils.showAlert(this,context.getResources().getString(R.string.enter_Valid_Mobile_Number));
                                        existingTradeDetailsViewNewBinding.mobileNo.requestFocus();
                                    } }
                                else {
                                    Utils.showAlert(this,context.getResources().getString(R.string.enter_Mobile_Number));
                                    existingTradeDetailsViewNewBinding.mobileNo.requestFocus();
                                }
                            }
                            else {
                                Utils.showAlert(this,context.getResources().getString(R.string.enter_Fathers_Husband_Name_Tamil));
                                existingTradeDetailsViewNewBinding.fatherHusNameTamil.requestFocus();
                            }
                        }
                        else {
                            Utils.showAlert(this,context.getResources().getString(R.string.enter_Father_Husband_Name_English));
                            existingTradeDetailsViewNewBinding.fatherHusName.requestFocus();
                        }
                    }
                    else {
                        Utils.showAlert(this,context.getResources().getString(R.string.enter_Valid_Age));
                        existingTradeDetailsViewNewBinding.age.requestFocus();
                    }
                }
                else {
                    Utils.showAlert(this,context.getResources().getString(R.string.select_Gender));
                    existingTradeDetailsViewNewBinding.genderLayout.requestLayout();
                }
            }
            else {
                Utils.showAlert(this,context.getResources().getString(R.string.enter_Applicant_Name_Tamil));
                existingTradeDetailsViewNewBinding.applicantNameTamil.requestFocus();
            }
        }
        else {
            Utils.showAlert(this,context.getResources().getString(R.string.enter_Applicant_Name_English));
            existingTradeDetailsViewNewBinding.applicantName.requestFocus();
        }
        return false;
    }
    public boolean ValidationSecond(){
        if (!existingTradeDetailsViewNewBinding.wardNo.getSelectedItem().toString().isEmpty() && !context.getResources().getString(R.string.select_Ward).equalsIgnoreCase(selectedWardName)) {
            if (!existingTradeDetailsViewNewBinding.streetsName.getSelectedItem().toString().isEmpty() && !context.getResources().getString(R.string.select_Street).equalsIgnoreCase(selectedStreetName)) {
                if (!existingTradeDetailsViewNewBinding.doorNo.getText().toString().isEmpty()) {

                    if (!existingTradeDetailsViewNewBinding.licenceValidity.getSelectedItem().toString().isEmpty() && !context.getResources().getString(R.string.select_Licence_Validity).equalsIgnoreCase(selectedFinName)) {
                        if(ownerDetailsCondition()){
//                            if (!existingTradeDetailsViewNewBinding.date.getText().toString().isEmpty() && !existingTradeDetailsViewNewBinding.date.getText().toString().equals(context.getResources().getString(R.string.select_Date))) {
                                if (existingTradeDetailsViewNewBinding.tradeCodeSpinner.getSelectedItem() != null && selectedTradeCode != null && !existingTradeDetailsViewNewBinding.tradeCodeSpinner.getSelectedItem().toString().isEmpty() && !context.getResources().getString(R.string.select_TradeCode).equalsIgnoreCase(selectedTradeCode)) {

                                    if (!existingTradeDetailsViewNewBinding.licenceType.getSelectedItem().toString().isEmpty() && !context.getResources().getString(R.string.select_Licence_Type).equalsIgnoreCase(selectedLicenceTypeName)) {
                                        if (existingTradeDetailsViewNewBinding.annualSale.getSelectedItem() != null && !existingTradeDetailsViewNewBinding.annualSale.getSelectedItem().toString().isEmpty()&&!context.getResources().getString(R.string.select_AnnualSale).equalsIgnoreCase(selectedAnnualSale)) {
                                            return true;
                                        }
                                        else {
                                            Utils.showAlert(this, context.getResources().getString(R.string.select_AnnualSale));
                                            existingTradeDetailsViewNewBinding.annualSaleLayout.requestLayout();
                                            return false;
                                        }

                                    } else {
                                        Utils.showAlert(this, context.getResources().getString(R.string.select_Licence_Type));
                                        existingTradeDetailsViewNewBinding.licenceTypeLayout.requestLayout();
                                        return false;
                                    }
                                } else {
                                    Utils.showAlert(this, context.getResources().getString(R.string.select_TradeCode));
                                    existingTradeDetailsViewNewBinding.tradersCodeLayout.requestLayout();
                                }
                            /*}
                            else {
                                Utils.showAlert(this,context.getResources().getString(R.string.select_Date));
                                existingTradeDetailsViewNewBinding.dateLayout.requestFocus();
                            }*/
                        }
                    }
                    else {
                        Utils.showAlert(this,context.getResources().getString(R.string.select_Licence_Validity));
                        existingTradeDetailsViewNewBinding.licenceValidityLayout.requestLayout();
                    }

                }
                else {
                    Utils.showAlert(this,context.getResources().getString(R.string.enter_Door_Number));
                    existingTradeDetailsViewNewBinding.doorNo.requestFocus();
                }
            }
            else {
                Utils.showAlert(this,context.getResources().getString(R.string.select_Street));
                existingTradeDetailsViewNewBinding.streetLayout.requestLayout();
            }
        }
        else {
            Utils.showAlert(this,context.getResources().getString(R.string.select_Ward));
            existingTradeDetailsViewNewBinding.wardLayout.requestLayout();
        }
        return false;
    }
    public boolean ValidationThird(){
        if(motorDetailsCondition()){
            if(generatorDetailsCondition()){
                if(existingTradeDetailsViewNewBinding.professionalTaxYes.isChecked()||existingTradeDetailsViewNewBinding.professionalTaxNo.isChecked()){
                    if(propertyDetailsCondition()){
                        if(existingTradeDetailsViewNewBinding.remarksField.getText() != null && !existingTradeDetailsViewNewBinding.remarksField.getText().toString().isEmpty()){
/*
                                if ((existingTradeDetailsViewNewBinding.isPaid.isChecked())) {
*/
                            if (getSaveTradeImageTable()==1) {
                                if (Utils.isOnline()) {
                                    //savenewTraderINLocal();
                                    SaveLicenseTraders();
                                } else {
                                    //savenewTraderINLocal();
                                    Utils.showAlert(this, getResources().getString(R.string.no_internet));
                                }
                            } else {
                                Utils.showAlert(ExistTradeViewClass.this, context.getResources().getString(R.string.capture_One_Image));
                            }
                                /*}
                                else {
                                    Utils.showAlert(this,"Choose Payment Status");
                                    existingTradeDetailsViewNewBinding.isPaid.requestLayout();
                                }*/
                        }
                        else {
                            Utils.showAlert(this,context.getResources().getString(R.string.enter_Remarks));
                            existingTradeDetailsViewNewBinding.remarksField.requestFocus();
                        }
                    }

                }
                else {
                    Utils.showAlert(this,context.getResources().getString(R.string.choose_Professional_Tax_Paid_status));
                    existingTradeDetailsViewNewBinding.professionalTaxLayout.requestLayout();
                }
            }
        }

        return false;
    }

    public boolean ownerDetailsCondition(){
        if(existingTradeDetailsViewNewBinding.ownerStatusYes.isChecked()||existingTradeDetailsViewNewBinding.ownerStatusNo.isChecked()){
            if(existingTradeDetailsViewNewBinding.ownerStatusYes.isChecked()){
                return true;
            }
            else {
                if(fileString!=null && !fileString.isEmpty() && !fileString.equals("")){
                    return true;
                }else{
                    if(existingTradeDetailsViewNewBinding.viewDoc.getVisibility() == View.VISIBLE){
                        return true;
                    }else {
                        Utils.showAlert(this,context.getResources().getString(R.string.upload_Document));
                        existingTradeDetailsViewNewBinding.areYouOwnerLayout.requestFocus();
                        return false;
                    }

                   }
            }

        }
        else {
            Utils.showAlert(this,context.getResources().getString(R.string.choose_Owner_or_Not));
        }
        return false;
    }
    public boolean motorDetailsCondition(){
        if(existingTradeDetailsViewNewBinding.motorAvilableStatusYes.isChecked()||existingTradeDetailsViewNewBinding.motorAvilableStatusNo.isChecked()){
            if(existingTradeDetailsViewNewBinding.motorAvilableStatusYes.isChecked()){
                if(existingTradeDetailsViewNewBinding.motorRangeSpinner!= null &&
                        existingTradeDetailsViewNewBinding.motorRangeSpinner.getSelectedItem() !=null
                        &&  !existingTradeDetailsViewNewBinding.motorRangeSpinner.getSelectedItem().toString().equals(context.getResources().getString(R.string.select_Motor_Range))) {
                    return true;
                }
                else {
                    Utils.showAlert(this, context.getResources().getString(R.string.select_Motor_Range));
                    existingTradeDetailsViewNewBinding.motorSpinnerLayout.requestLayout();
                    return false;

                }
            }
            else {
                return true;
            }
        }
        else {
            Utils.showAlert(this,context.getResources().getString(R.string.choose_motor_available_status));
            existingTradeDetailsViewNewBinding.moterLayout.requestLayout();
            return false;
        }
    }
    public boolean generatorDetailsCondition(){
        if(existingTradeDetailsViewNewBinding.geneartorAvilableStatusYes.isChecked()||existingTradeDetailsViewNewBinding.generatorAvilableStatusNo.isChecked()){
            if(existingTradeDetailsViewNewBinding.geneartorAvilableStatusYes.isChecked()){
                if(existingTradeDetailsViewNewBinding.generatorSpinner!= null &&
                        existingTradeDetailsViewNewBinding.generatorSpinner.getSelectedItem() !=null
                        &&  !existingTradeDetailsViewNewBinding.generatorSpinner.getSelectedItem().toString().equals(context.getResources().getString(R.string.select_Generator_Range))) {
                    return true;
                }
                else {
                    Utils.showAlert(this, context.getResources().getString(R.string.select_Generator_Range));
                    existingTradeDetailsViewNewBinding.genaratorLayout.requestLayout();
                    return false;
                }
            }
            else {
                return true;
            }
        }
        else {
            Utils.showAlert(this,context.getResources().getString(R.string.choose_generator_available_status));
            existingTradeDetailsViewNewBinding.genaratorLayout.requestLayout();
            return false;
        }
    }
    public boolean propertyDetailsCondition(){
        if(existingTradeDetailsViewNewBinding.propertyTaxYes.isChecked()||existingTradeDetailsViewNewBinding.propertyTaxNo.isChecked()){
            if(existingTradeDetailsViewNewBinding.propertyTaxYes.isChecked()){
                if(existingTradeDetailsViewNewBinding.propertyTaxAssessmentNumber.getText() != null &&
                        !existingTradeDetailsViewNewBinding.propertyTaxAssessmentNumber.getText().toString().equals("")) {
                    return true;
                }
                else {
                    Utils.showAlert(this, context.getResources().getString(R.string.enter_Property_Tax_Assessment_Number));
                    existingTradeDetailsViewNewBinding.propertyTaxAssessmentLayout.requestLayout();
                    return false;
                }
            }
            else {
                return true;
            }
        }
        else {
            Utils.showAlert(this,context.getResources().getString(R.string.choose_Property_Tax_Payment_status));
            existingTradeDetailsViewNewBinding.genaratorLayout.requestLayout();
            return false;
        }
    }
    public  void next() {
//        existingTradeDetailsViewNewBinding.scrollView.scrollTo(0, 0);

        if (visible_count == 0) {
            if (ValidationFirst()){
                existingTradeDetailsViewNewBinding.scrollView.scrollTo(0, 0);
                visible_count = 1;
                existingTradeDetailsViewNewBinding.first.setVisibility(View.GONE);
                existingTradeDetailsViewNewBinding.second.setVisibility(View.VISIBLE);
                existingTradeDetailsViewNewBinding.third.setVisibility(View.GONE);
                existingTradeDetailsViewNewBinding.previous.setVisibility(View.VISIBLE);
                existingTradeDetailsViewNewBinding.next.setText(context.getResources().getString(R.string.next));
                existingTradeDetailsViewNewBinding.second.setAnimation(animation);
                animation.start();
            }
        }

        else if(visible_count==1){
            if (ValidationSecond()) {
                existingTradeDetailsViewNewBinding.scrollView.scrollTo(0, 0);
                visible_count = 2;
                existingTradeDetailsViewNewBinding.first.setVisibility(View.GONE);
                existingTradeDetailsViewNewBinding.second.setVisibility(View.GONE);
                existingTradeDetailsViewNewBinding.third.setVisibility(View.VISIBLE);
                existingTradeDetailsViewNewBinding.previous.setVisibility(View.VISIBLE);
                existingTradeDetailsViewNewBinding.next.setText(context.getResources().getString(R.string.update));
                existingTradeDetailsViewNewBinding.third.setAnimation(animation);
                animation.start();
            }
        }
        else if (existingTradeDetailsViewNewBinding.next.getText().toString().equals(context.getResources().getString(R.string.update))){
            ValidationThird();
        }
    }
    public  void previous() {
//        existingTradeDetailsViewNewBinding.scrollView.scrollTo(0,0);

        if(visible_count==2) {
            visible_count=1;
            existingTradeDetailsViewNewBinding.first.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.second.setVisibility(View.VISIBLE);
            existingTradeDetailsViewNewBinding.third.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.previous.setVisibility(View.VISIBLE);
            existingTradeDetailsViewNewBinding.next.setText(context.getResources().getString(R.string.next));
            existingTradeDetailsViewNewBinding.second.setAnimation(animationOut);
            animationOut.start();
        }
        else if(visible_count==1){
            visible_count=0;
            existingTradeDetailsViewNewBinding.first.setVisibility(View.VISIBLE);
            existingTradeDetailsViewNewBinding.second.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.third.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.previous.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.next.setText(context.getResources().getString(R.string.next));
            existingTradeDetailsViewNewBinding.first.setAnimation(animationOut);
            animationOut.start();
        }
        else if(visible_count==0){
           /* existingTradeDetailsViewNewBinding.fullDetails.setVisibility(View.VISIBLE);
            existingTradeDetailsViewNewBinding.documentLayout.setVisibility(View.GONE);
            existingTradeDetailsViewNewBinding.main.setVisibility(View.GONE);*/
            super.onBackPressed();
            setResult(Activity.RESULT_CANCELED);
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }

    public void SaveLicenseTraders() {
        try {
            new ApiService(this).makeJSONObjectRequest("SaveLicenseTraders", Api.Method.POST, UrlGenerator.TradersUrl(), dataSavedEncryptJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        } }

    public JSONObject dataSavedEncryptJsonParams() throws JSONException {
        String sql = "SELECT * FROM " + DBHelper.SAVE_TRADE_IMAGE + " WHERE screen_status = 'new' and mobileno ="+existingTradeDetailsViewNewBinding.mobileNo.getText().toString();
        Cursor cursor = Dashboard.db.rawQuery(sql, null);
        Log.d("cursor_count", String.valueOf(cursor.getCount()));
        JSONArray imageArray = new JSONArray();
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    image = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TRADE_IMAGE));
                    lat = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LATITUDE));
                    lan = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LONGITUDE));
                    Log.d("lat", "" + lat);
                    Log.d("image", "" + image);
                } while (cursor.moveToNext());
            }
        }
        Log.d("olat", "" + lat);
        Log.d("oimage", "" + image);

        /*Log.d("DataSet", "" + dataSet);
        String authKey = dataSet.toString();
        int maxLogSize = 2000;
        for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > authKey.length() ? authKey.length() : end;
            Log.v("to_send+_plain", authKey.substring(start, end));
        }*/
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), dataTobeSavedJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        JSONObject dataSet1 = new JSONObject();

        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        //dataSet.put(AppConstant.LATITUDE, lat);
        //dataSet.put(AppConstant.LONGITUDE, lan);
        dataSet1.put(AppConstant.TRADE_IMAGE, image);
        dataSet1.put(AppConstant.TRADE_DOCUMENT, fileString);
        imageArray.put(dataSet1);
        dataSet.put(AppConstant.ATTACHMENT_FILES,imageArray);

        Log.d("TraderLicenseTypeList", "" + authKey);
        Log.d("DataSetS__:",""+dataSet);
        return dataSet;
    }

    public JSONObject dataTobeSavedJsonParams() throws JSONException {
        mobileNumber=existingTradeDetailsViewNewBinding.mobileNo.getText().toString();
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "SaveLicenseTraders");
        dataSet.put(AppConstant.MODE, "NEW");
        dataSet.put("tradedetails_id",selectedTrdeCodeDetailsID);
//        dataSet.put(AppConstant.DATE,existingTradeDetailsViewNewBinding.date.getText().toString());
        dataSet.put(AppConstant.LICENCE_TYPE_ID,selectedLicenceTpeId);
        dataSet.put(AppConstant.APPLICANT_NAME_EN, existingTradeDetailsViewNewBinding.applicantName.getText().toString());
        dataSet.put(AppConstant.APPLICANT_NAME_TA, existingTradeDetailsViewNewBinding.applicantNameTamil.getText().toString());
        dataSet.put(AppConstant.GENDER,selectedGenderId);
        dataSet.put(AppConstant.AGE, existingTradeDetailsViewNewBinding.age.getText().toString());
        dataSet.put(AppConstant.FATHER_HUSBAND_NAME_EN, existingTradeDetailsViewNewBinding.fatherHusName.getText().toString());
        dataSet.put(AppConstant.FATHER_HUSBAND_NAME_TA, existingTradeDetailsViewNewBinding.fatherHusNameTamil.getText().toString());
        dataSet.put(AppConstant.MOBILE, mobileNumber);
        dataSet.put(AppConstant.E_MAIL, existingTradeDetailsViewNewBinding.emailId.getText().toString());
        dataSet.put(AppConstant.ESTABLISHMENT_NAME_EN, existingTradeDetailsViewNewBinding.descriptionEnglish.getText().toString());
        dataSet.put(AppConstant.ESTABLISHMENT_NAME_TA, existingTradeDetailsViewNewBinding.descriptionTamil.getText().toString());
//        dataSet.put(AppConstant.WARD_ID, selectedWardId);
        dataSet.put(AppConstant.STREET_ID, selectedStreetId);
        dataSet.put(AppConstant.DOOR_NO, existingTradeDetailsViewNewBinding.doorNo.getText().toString());
        dataSet.put(AppConstant.LICENCE_VALIDITY,selectedFinId);
        dataSet.put("wardid", selectedWardId);

        dataSet.put("motor_y_n", motor_available_status_text);
        dataSet.put("generator_y_n", generator_available_status_text);
        dataSet.put("motor_type_id", selectedMotorId);
        dataSet.put("generator_range_id", selectedGeneratorId);
        dataSet.put("amount_range_id", selectedAnnualId);
        dataSet.put("profess_tax_pay", professional_tax_paid_status_text);
        dataSet.put("property_tax_paid", property_tax_paid_status_text);
        dataSet.put("property_tax_assessment_no", existingTradeDetailsViewNewBinding.propertyTaxAssessmentNumber.getText().toString());
        dataSet.put("owner_y_n", owner_status_text);
        dataSet.put("edit_id", 0);
        dataSet.put("del_id", 0);
        dataSet.put("remark", existingTradeDetailsViewNewBinding.remarksField.getText().toString());


        //dataSet.put(AppConstant.LATITUDE, "10.3");
        //dataSet.put(AppConstant.LONGITUDE, "20.6");
        //dataSet.put(AppConstant.TRADE_IMAGE, "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GjBD6sDHcN79Na+cesHH/wBHwHzp6wcf/R8B86ZRl9H3cXQ+LHQ9j4v2f7dNPso0UJfeGNS9rZ930rcr2vavnHrBx/8AR8B86esHH/0fAfOg+lKyBQoFlAsBlNrcrW4Vi3EGQR7pcgNwu76IPMC3GvnXrBx/9HwHzp6wcf8A0fAfOg+kMsROYoC1wb5De4Fgb25Ej7a1ocBAkxlVOllRVGTRAgIGTTTRjVA9YOP/AKPgPnT1g4/+j4D50Tl9HaOIoUMYKEklTH0SSbkkW560aOI2vGDYAC8fUDcDhwB1r5x6wcf/AEfAfOnrBx/9HwHzpky+jGKE3vGuoIPxfEMbsOHWdTXjYeAoIzChjGoQxdEe4WtXzr1g4/8Ao+A+dPWDj/6PgPnTJlZ/S9gdzbqzdRHKq3XP2j6W4nE5d6IzlvaykcftrT/fMnzU7j50ezpdbZtWYor4vqGwdpQR4SJHmjVgDcFgCOkanFNhFxEk/wC1Lme3R3gCiwtw6/tr5Z++ZPmp3Hzp++ZPmp3Hzor1TpZqqqiufn9n0r0m2jBJhSscqO2ZdFYE8aqsczKHCmwcZW0Goveq/wDvmT5qdx86fvmT5qdx86LFjU6WzR8OJme/gscOMkjXKjWW5NrDiy5TxHKp/vGa98+t0PBeKCy9XVVZ/fMnzU7j50/fMnzU7j50bZ1ukmczHoshx0pFi+mUpwX2S2YjhzrKNrYjpfGe1cm6p1ixtppoOqqt++ZPmp3Hzp++ZPmp3HzobzR+Hos42nODcSa3U8F4quUdXLTt66hNjpXDBm0bLcBVA6N8tgBpa54c6rf75k+ancfOn75k+ancfOhvdJE5iPRYDOxQR36AJYCw4njrU0xsqsjB7GNcq6DQcuGvE8arn75k+ancfOn75k+ancfOjKddpZ4+ywYjEPK2dzdrAcANBwAA0ArHXD/fMnzU7j50/fMnzU7j50TH+hpojEeznUpSjnilKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUClKUH/2Q==");

        String sql = "SELECT * FROM " + DBHelper.SAVE_TRADE_IMAGE + " WHERE screen_status = 'new' and mobileno ="+existingTradeDetailsViewNewBinding.mobileNo.getText().toString();
        Cursor cursor = Dashboard.db.rawQuery(sql, null);
        Log.d("cursor_count", String.valueOf(cursor.getCount()));

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    image = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TRADE_IMAGE));
                    lat = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LATITUDE));
                    lan = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.LONGITUDE));
                    Log.d("lat", "" + lat);
                    Log.d("image", "" + image);
                } while (cursor.moveToNext());
            }
        }
        Log.d("olat", "" + lat);
        Log.d("oimage", "" + image);
        dataSet.put(AppConstant.LATITUDE, lat);
        dataSet.put(AppConstant.LONGITUDE, lan);
        //dataSet.put(AppConstant.TRADE_IMAGE, image);

        Log.d("DataSet", "" + dataSet);
        String authKey = dataSet.toString();
        int maxLogSize = 2000;
        for (int i = 0; i <= authKey.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > authKey.length() ? authKey.length() : end;
            Log.v("to_send+_plain", authKey.substring(start, end));
        }
        return dataSet;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void openFile() {
        pageNumber=0;
        byte[] decodedString = new byte[0];
        try {
            //byte[] name = java.util.Base64.getEncoder().encode(fileString.getBytes());
            decodedString = Base64.decode(fileString.toString(), Base64.DEFAULT);

            System.out.println(new String(decodedString));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        existingTradeDetailsViewNewBinding.documentLayout.setVisibility(View.VISIBLE);
        existingTradeDetailsViewNewBinding.main.setVisibility(View.GONE);
        //File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Test/TestTest/" + displayName);
        existingTradeDetailsViewNewBinding.documentViewer.fromBytes(decodedString).
                onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        pageNumber = page;
                        setTitle(String.format("%s %s / %s", "PDF", page + 1, pageCount));
                        existingTradeDetailsViewNewBinding.pageNum.setText(pageNumber+1 +"/"+pageCount);
                    }
                }).defaultPage(pageNumber).swipeHorizontal(true).enableDoubletap(true).load();

    }
    public void getDocument()
    {
        if(checkPermissions()){
            showFileChooser();
        }

    }
    private static final int FILE_SELECT_CODE = 0;
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, context.getResources().getString(R.string.select_a_File_to_Upload)),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, context.getResources().getString(R.string.please_install_a_File_Manager),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    uri = data.getData();
                    String uriString = uri.toString();
                    myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    Log.d("uri", uriString);
                    Log.d("myFile", myFile.toString());
                    Log.d("path", path);
                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = this.getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                                long fileSizeInBytes = cursor.getLong(sizeIndex);
                                existingTradeDetailsViewNewBinding.fileLocation.setText(displayName+" "+getSize(fileSizeInBytes));
                                Shader shader = new LinearGradient(0,0,0,existingTradeDetailsViewNewBinding.fileLocation.getLineHeight(),
                                        context.getResources().getColor(R.color.colorPrimary) ,
                                        context.getResources().getColor(R.color.colorPrimaryDark) , Shader.TileMode.REPEAT);
                                existingTradeDetailsViewNewBinding.fileLocation.getPaint().setShader(shader);
                                existingTradeDetailsViewNewBinding.fileSize.getPaint().setShader(shader);
                                ConvertToString(uri);
                                Log.d("fileString>>", fileString);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                        Log.d("displayName", displayName);
                        existingTradeDetailsViewNewBinding.fileLocation.setText(getFilePath(this,uri));
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private boolean checkPermissions() {
        String[] permissions = new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE

        };
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(ExistTradeViewClass.this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_REQUEST_CODE_PERMISSION);
            return false;
        }
        return true;
    }
    public void ConvertToString(Uri uri){
        uriString = uri.toString();
        Log.d("data", "onActivityResult: uri"+uriString);
        //            myFile = new File(uriString);
        //            ret = myFile.getAbsolutePath();
        //Fpath.setText(ret);
        try {
            InputStream in = getContentResolver().openInputStream(uri);

            bytes=getBytes(in);
            Log.d("data", "onActivityResult: bytes size="+bytes.length);
            String cnt_size;

            double size_kb = getFileSize(displayName) /1024;
            double size_mb = size_kb / 1024;
            double size_gb = size_mb / 1024 ;

            if (size_gb > 0){
                cnt_size = size_gb + " GB";
            }else if(size_mb > 0){
                cnt_size = size_mb + " MB";
            }else{
                cnt_size = size_kb + " KB";
            }
            Log.d("data", "onActivityResult: Base64string="+Base64.encodeToString(bytes,Base64.DEFAULT));
            fileString = Base64.encodeToString(bytes,Base64.DEFAULT);
            System.out.println("Base64>>"+Base64.encodeToString(bytes,Base64.DEFAULT));
            System.out.println("Base64fileString>>"+fileString);
            fileSize=cnt_size;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    String getFilePath(Context cntx, Uri uri) {
        Cursor cursor = null;
        try {
            String[] arr = { MediaStore.Images.Media.DATA };
            cursor = cntx.getContentResolver().query(uri,  arr, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    public static long getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            System.out.println("File doesn\'t exist");
            return -1;
        }
        return file.length();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case MY_REQUEST_CODE_PERMISSION: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (CALL_PHONE).
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i( "LOG_TAG",context.getResources().getString(R.string.permission_granted));
                    Toast.makeText(this.getApplicationContext(), context.getResources().getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
                    showFileChooser();

//                    this.doBrowseFile();
                }
                // Cancelled or denied.
                else {
                    Log.i("LOG_TAG",context.getResources().getString(R.string.permission_denied));
                    Toast.makeText(this.getApplicationContext(), context.getResources().getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
