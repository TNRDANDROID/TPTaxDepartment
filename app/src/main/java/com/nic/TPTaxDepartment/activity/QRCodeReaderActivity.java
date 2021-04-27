package com.nic.TPTaxDepartment.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nic.TPTaxDepartment.Api.Api;
import com.nic.TPTaxDepartment.Api.ApiService;
import com.nic.TPTaxDepartment.Api.ServerResponse;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.dataBase.DBHelper;
import com.nic.TPTaxDepartment.model.CommonModel;
import com.nic.TPTaxDepartment.model.SpinnerAdapter;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.UrlGenerator;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.Map;


public class QRCodeReaderActivity extends AppCompatActivity implements Api.ServerResponseListener{
    private TextView resultTextView;
    RelativeLayout whole_ll,scanner_rl,document_rl,scanner;
    View bar;
    Animation animation;
    public static MediaPlayer mp;
    Context context;
    String urlType;
    String DocumentString="";
    private PrefManager prefManager;
    Integer pageNumber = 0;
    PDFView documentViewer;
    TextView pageNum,alertMsg;
    ImageView refresh2,refresh,log_out;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    ArrayList<CommonModel> taxType ;
    HashMap<String,String> spinnerMapTaxType;
    String selectedTaxTypeId;
    String selectedTaxTypeName="";
    String qr_code_value="";
    Spinner tax_type_spinner;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner_layout);
        context=this;
        prefManager = new PrefManager(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        this.getWindow().setStatusBarColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

         bar = findViewById(R.id.bar);
         whole_ll=findViewById(R.id.whole_ll);
        scanner_rl=findViewById(R.id.scanner_rl);
         document_rl=findViewById(R.id.document_layout);
        documentViewer=findViewById(R.id.documentViewer);
        pageNum=findViewById(R.id.pageNum);
        refresh2=findViewById(R.id.refresh2);
        refresh=findViewById(R.id.refresh);
        resultTextView=findViewById(R.id.qr_code_text);
        log_out=findViewById(R.id.log_out);

        scanner=findViewById(R.id.scanner);
        alertMsg=findViewById(R.id.alertMsg);
        tax_type_spinner=findViewById(R.id.tax_type);


        scanner_rl.setVisibility(View.VISIBLE);
        document_rl.setVisibility(View.GONE);
        scanner.setVisibility(View.GONE);
        alertMsg.setVisibility(View.VISIBLE);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        animation = AnimationUtils.loadAnimation(this, R.anim.scanning_animation_view);
        bar.setVisibility(View.VISIBLE);

        bar.startAnimation(animation);
        //animation.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                bar.setVisibility(View.VISIBLE);
                bar.startAnimation(animation);
                //animation.start();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bar.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
               /*bar.setVisibility(View.VISIBLE);
                bar.startAnimation(animation);*/
            //animation.start();
            }
        });

        mp = MediaPlayer.create(this,R.raw.beep);

//        requestCameraPermission(MEDIA_TYPE_IMAGE);
//         QrCodeInitialize();
        getTaxTypeList();
        tax_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String TaxTypeName = parent.getSelectedItem().toString();
                String TaxTypeId ="";
                // iterate each entry of hashmap
                for(Map.Entry<String, String> entry: spinnerMapTaxType.entrySet()) {
                    // if give value is equal to value from entry
                    // print the corresponding key
                    if(entry.getValue() == TaxTypeName) {
                        TaxTypeId=entry.getKey();
                        break;
                    }
                }
                selectedTaxTypeId=TaxTypeId;
                selectedTaxTypeName=TaxTypeName;

                if(selectedTaxTypeId!=null && !selectedTaxTypeId.equals("") && position >0){
                    scanner.setVisibility(View.GONE);
                    alertMsg.setVisibility(View.GONE);
                    //initiating the qr code scan
                    performQrCodeReader();
                }else {
                    scanner.setVisibility(View.GONE);
                    alertMsg.setVisibility(View.VISIBLE);
            }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        //whole_ll.setOnTouchListener(new RelativeLayoutTouchListener(this));
        refresh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnBackPressed();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dashboard();
            }
        });


    }
    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void performQrCodeReader(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

    }
    public  void getTaxTypeList() {
        taxType = new ArrayList<CommonModel>();
        String select_query= "SELECT *FROM " + DBHelper.TAX_TYPE_LIST;
        Cursor cursor = Dashboard.db.rawQuery(select_query, null);
        if(cursor.getCount()>0){

            if(cursor.moveToFirst()){
                do{
                    CommonModel commonModel=new CommonModel();
                    commonModel.setTaxtypeid(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.TAX_TYPE_ID))));
                    commonModel.setTaxtypedesc_en(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TAX_TYPE_DESC_EN)));
                    commonModel.setTaxtypedesc_ta(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TAX_TYPE_DESC_TA)));
                    commonModel.setTaxcollection_methodlogy(cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.TAX_COLLECTION_METHODLOGY)));
                    commonModel.setInstallmenttypeid(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(AppConstant.INSTALLMENT_TYPE_ID))));
                    taxType.add(commonModel);
                }while (cursor.moveToNext());
            }
        }
       /* Collections.sort(taxType, (lhs, rhs) -> lhs.getTaxtypeid().compareTo(rhs.getTaxtypeid()));*/
        Collections.sort(taxType, (lhs, rhs) -> Integer.valueOf((lhs.getTaxtypeid())).compareTo(Integer.valueOf((rhs.getTaxtypeid()))));

        if(taxType != null && taxType.size() >0) {

            spinnerMapTaxType = new HashMap<String, String>();
            spinnerMapTaxType.put(null, null);
            final String[] items = new String[taxType.size() + 1];
            items[0] = context.getResources().getString(R.string.select_TaxType);
            for (int i = 0; i < taxType.size(); i++) {
                spinnerMapTaxType.put(taxType.get(i).getTaxtypeid(), taxType.get(i).getTaxtypedesc_en());
                String Class = taxType.get(i).getTaxtypedesc_en();
                items[i + 1] = Class;
            }
            System.out.println("items" + items.toString());

            try {
                if (items != null && items.length > 0) {
                    /*ArrayAdapter<String> RuralUrbanArray = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
                    RuralUrbanArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tax_type_spinner.setAdapter(RuralUrbanArray);
                    tax_type_spinner.setPopupBackgroundResource(R.drawable.cornered_border_bg_strong);
*/
                    tax_type_spinner.setAdapter(new SpinnerAdapter(this, R.layout.simple_spinner_dropdown_item, items));

                    selectedTaxTypeId="0";
                    selectedTaxTypeName="";
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                showAlert(QRCodeReaderActivity.this,context.getResources().getString(R.string.permissions_required));

            }
        }
    }
    public  void showAlert(Activity activity, String msg){
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            text.setText(msg);

            Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getQrCodeReport() {
        try {
            new ApiService(this).makeJSONObjectRequest("QrCodeReport", Api.Method.POST, UrlGenerator.TradersUrl(), QrCodeReportEncryptParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void OnBackPressed() {
        scanner_rl.setVisibility(View.VISIBLE);
        document_rl.setVisibility(View.GONE);
    }
    public JSONObject QrCodeReportEncryptParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), QrCodeReportJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("QrCodeReport", "" + dataSet);
        return dataSet;
    }
    public JSONObject QrCodeReportJsonParams() throws JSONException{
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "ReceiptValidation");
        dataSet.put(AppConstant.TAX_TYPE_ID, selectedTaxTypeId);
        dataSet.put(AppConstant.QR_CODE_VALUE, qr_code_value);
        Log.d("QrCodeReport", "" + dataSet);
        return dataSet;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject responseObj = serverResponse.getJsonResponse();
            urlType = serverResponse.getApi();
            String status;
            String RECEIPT_FOUND;
            if ("QrCodeReport".equals(urlType) && responseObj != null) {
                try {
                    String user_data = Utils.NotNullString(responseObj.getString(AppConstant.ENCODE_DATA));
                    String userDataDecrypt = Utils.decrypt(prefManager.getUserPassKey(), user_data);
                    Log.d("QrCodeReport", "" + userDataDecrypt);
                    JSONObject jsonObject = new JSONObject(userDataDecrypt);

                    status = Utils.NotNullString(jsonObject.getString(AppConstant.KEY_STATUS));
                    RECEIPT_FOUND = Utils.NotNullString(jsonObject.getString("RECEIPT_FOUND"));
                    if (status.equalsIgnoreCase("SUCCESS")&&RECEIPT_FOUND.equals("YES")) {
                        tax_type_spinner.setSelection(0);
                        JSONObject attachment_files=(responseObj.getJSONObject(AppConstant.ATTACHMENT_FILES));
                        DocumentString = attachment_files.getString("receipt");
                        viewDocument();
                        Log.d("QrCodeReport", "" + jsonObject);

                    } else if (status.equalsIgnoreCase("SUCCESS")&&RECEIPT_FOUND.equals("NO")) {
                        Utils.showAlert(this, context.getResources().getString(R.string.no_RECEIPT_FOUND));
                        tax_type_spinner.setSelection(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showAlert(this, context.getResources().getString(R.string.something_wrong));
                    tax_type_spinner.setSelection(0);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Utils.showAlert(this, context.getResources().getString(R.string.scan_again));
            tax_type_spinner.setSelection(0);

        }
    }
    @Override
    public void OnError(VolleyError volleyError) {
        Utils.showAlert(this, context.getResources().getString(R.string.scan_again));
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
            scanner_rl.setVisibility(View.GONE);
            document_rl.setVisibility(View.VISIBLE);
            documentViewer.fromBytes(decodedString).
                    onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            pageNumber = page;
                            setTitle(String.format("%s %s / %s", "PDF", page + 1, pageCount));
                            pageNum.setText(pageNumber + 1 + "/" + pageCount);
                        }
                    }).defaultPage(pageNumber).swipeHorizontal(true).enableDoubletap(true).load();

        }else {
            Utils.showAlert(this,context.getResources().getString(R.string.no_RECORD_FOUND));
            scanner_rl.setVisibility(View.VISIBLE);
            document_rl.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBackPressed() {
        if (document_rl.getVisibility() == View.VISIBLE) {
            scanner_rl.setVisibility(View.VISIBLE);
            document_rl.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                qr_code_value=result.getContents();
                if(Utils.isOnline()) {
                    getQrCodeReport();
                }
                else {
                    Utils.showAlert(QRCodeReaderActivity.this,getApplicationContext().getResources().getString(R.string.no_internet_connection));
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
