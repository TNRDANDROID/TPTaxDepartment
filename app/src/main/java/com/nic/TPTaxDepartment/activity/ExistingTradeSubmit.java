package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.nic.TPTaxDepartment.Adapter.TraderListAdapter;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.databinding.ExistTradeSubmitBinding;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ExistingTradeSubmit extends AppCompatActivity {
    private ExistTradeSubmitBinding existTradeSubmitBinding;
    ArrayList<TPtaxModel> tradersList;
    int position;
    private PrefManager prefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        existTradeSubmitBinding = DataBindingUtil.setContentView(this, R.layout.exist_trade_submit);
        existTradeSubmitBinding.setActivity(this);
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        prefManager = new PrefManager(this);
        tradersList = new ArrayList<TPtaxModel>();
        position = getIntent().getIntExtra("position",0);
        tradersList = (ArrayList<TPtaxModel>)getIntent().getSerializableExtra("tradersList");
        shoewDetails();
    }

    private void shoewDetails() {

        String ApNameEn="TraderName : "+tradersList.get(position).getTraderName() + "\n"+ "\n";
        String lb_traderscode="LocalPanchayatTradersCode : "+tradersList.get(position).getTraderCode() + "\n"+ "\n";
        String traders_type="TradersType : "+tradersList.get(position).getTraders_typ() + "\n"+ "\n";
        String tradedesct="TradeDescriptionTamil : "+tradersList.get(position).getTradedesct() + "\n"+ "\n";
        String doorno="DoorNo : "+tradersList.get(position).getDoorno() + "\n"+ "\n";
        String apfathername_ta="FatherName : "+tradersList.get(position).getApfathername_ta() + "\n"+ "\n";
        String establishment_name_ta="EstablishmentName : "+tradersList.get(position).getEstablishment_name_ta() + "\n"+ "\n";
        String licence_validity="LicenceValidity : "+tradersList.get(position).getLicenceValidity() + "\n"+ "\n";
        String traders_license_type_name="LicenseTypeName : "+tradersList.get(position).getTraders_license_type_name() + "\n"+ "\n";
        String paymentstatus="PaymentStatus : "+tradersList.get(position).getTraderPayment() + "\n"+ "\n";
        String mobileno="MobileNo : "+tradersList.get(position).getMobileno() + "\n"+ "\n";
        String paymentdate="PaymentDate : "+tradersList.get(position).getPaymentdate() + "\n"+ "\n";
        String tradersdetails_id="TradersDetailsId : "+tradersList.get(position).getTradersdetails_id() + "\n"+ "\n";
        String lb_sno="LocalPanchayat Sno : "+tradersList.get(position).getLb_sno() + "\n"+ "\n";
        String tradedetails_id="TradeDetailsId : "+tradersList.get(position).getTradedetails_id() + "\n"+ "\n";
        String description_en="Description : "+tradersList.get(position).getDescription_en() + "\n"+ "\n";
        String description_ta="DescriptionTamil : "+tradersList.get(position).getDescription_ta() + "\n"+ "\n";
        String traderate="TradeRate : "+tradersList.get(position).getTraderate() + "\n"+ "\n";
        String traders_rate="TradersRate : "+tradersList.get(position).getTraders_rate() + "\n"+ "\n";
        String date="Date : "+tradersList.get(position).getTrade_date() + "\n"+ "\n";
        String tradersperiod="TradersPeriod : "+tradersList.get(position).getTradersperiod() + "\n"+ "\n";
        String tradedesce="TradeDescription : "+tradersList.get(position).getTradedesce() + "\n"+ "\n";
        String licencefor="LicenseFor : "+tradersList.get(position).getLicencefor() + "\n"+ "\n";
        String fin_year="FinancialYear : "+tradersList.get(position).getFin_year() + "\n"+ "\n";
        String traderstypee="TradersType : "+tradersList.get(position).getTraderstypee() + "\n"+ "\n";
        String demandtype="DemandType : "+tradersList.get(position).getDemandtype() + "\n"+ "\n";
        String onlineapplicationno="OnlineApplicationNumber : "+tradersList.get(position).getOnlineapplicationno() + "\n"+ "\n";
        String email="Email : "+tradersList.get(position).getEmail() + "\n"+ "\n";
        String licencetypeid="LicenseTypeID : "+tradersList.get(position).getLicencetypeid() + "\n"+ "\n";
        String apgender="ApplicantGender : "+tradersList.get(position).getApgender() + "\n"+ "\n";
        String apage="ApplicantAge : "+tradersList.get(position).getApage() + "\n"+ "\n";
        String apfathername_en="ApplicantFatherNameEnglish : "+tradersList.get(position).getApfathername_en() + "\n"+ "\n";
        String licenceno="LicenseNumber : "+tradersList.get(position).getLicenceno() + "\n"+ "\n";
        String statecode="StateCode : "+tradersList.get(position).getStatecode() + "\n"+ "\n";
        String dcode="DistrictCode : "+tradersList.get(position).getDcode() + "\n"+ "\n";
        String lbcode="LocalPanchayatCode : "+tradersList.get(position).getLbcode() + "\n"+ "\n";
        String apname_ta="ApplicantNameTamil : "+tradersList.get(position).getApname_ta() + "\n"+ "\n";
        String establishment_name_en="EstablishmentNameEnglish : "+tradersList.get(position).getEstablishment_name_en() + "\n"+ "\n";


        existTradeSubmitBinding.text.setText(ApNameEn	+lb_traderscode+	traders_type+	tradedesct	+doorno	+apfathername_ta
                +establishment_name_ta+ licence_validity+	traders_license_type_name	+paymentstatus	+mobileno
                +paymentdate+	tradersdetails_id+ lb_sno+	tradedetails_id	+description_en+	description_ta+	traderate
                +traders_rate+	date+	tradersperiod+ tradedesce+	licencefor+	fin_year	+traderstypee+	demandtype
                +onlineapplicationno+	email	+licencetypeid+ apgender+	apage+	apfathername_en	+licenceno+	statecode
                +dcode	+lbcode	+apname_ta	+establishment_name_en);

    }

    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void openCameraScreen() {
        Intent intent = new Intent(this, CameraScreen.class);
        intent.putExtra(AppConstant.TRADE_CODE,"");
        intent.putExtra(AppConstant.MOBILE, "");
        intent.putExtra(AppConstant.KEY_SCREEN_STATUS,"exist");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void viewImageScreen() {
        Intent intent = new Intent(this, FullImageActivity.class);
        intent.putExtra(AppConstant.TRADE_CODE,"");
        intent.putExtra(AppConstant.KEY_SCREEN_STATUS,"exist");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
