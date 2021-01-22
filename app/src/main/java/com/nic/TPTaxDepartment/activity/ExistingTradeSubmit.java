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
        /*try {
            LoadTradersList();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        shoewDetails();
    }
    private void LoadTradersList() throws JSONException {
        /*tradersList = new ArrayList<TPtaxModel>();
        for (int i = 0; i < 5; i++) {
            if(i==2){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTraderName("raj");
                Detail.setTraderCode("12");
                Detail.setTraders_typ("Industrial");
                Detail.setTradedesct("Traders Description");
                Detail.setDoorno("1/A");
                Detail.setApfathername_ta("Muthu");
                Detail.setEstablishment_name_ta("Establish");
                Detail.setLicenceValidity("2027");
                Detail.setTraders_license_type_name("TradersType");
                Detail.setTraderPayment("UnPaid");
                Detail.setMobileno("12233445");
                Detail.setPaymentdate("20/05/2020");
                tradersList.add(Detail);
            }else if(i==4){
                TPtaxModel Detail = new TPtaxModel();
                Detail.setTraderName("raj");
                Detail.setTraderCode("12");
                Detail.setTraders_typ("Industrial");
                Detail.setTradedesct("Traders Description");
                Detail.setDoorno("1/A");
                Detail.setApfathername_ta("Muthu");
                Detail.setEstablishment_name_ta("Establish");
                Detail.setLicenceValidity("2027");
                Detail.setTraders_license_type_name("TradersType");
                Detail.setTraderPayment("UnPaid");
                Detail.setMobileno("12233445");
                Detail.setPaymentdate("20/05/2020");
                tradersList.add(Detail);
            }else {
            TPtaxModel Detail = new TPtaxModel();
            Detail.setTraderName("raj");
            Detail.setTraderCode("12");
            Detail.setTraders_typ("Industrial");
            Detail.setTradedesct("Traders Description");
            Detail.setDoorno("1/A");
            Detail.setApfathername_ta("Muthu");
            Detail.setEstablishment_name_ta("Establish");
            Detail.setLicenceValidity("2027");
            Detail.setTraders_license_type_name("TradersType");
            Detail.setTraderPayment("Paid");
            Detail.setMobileno("12233445");
            Detail.setPaymentdate("20/05/2020");
            tradersList.add(Detail);}
        }
*/
        try {
            JSONArray jsonarray = new JSONArray(prefManager.getFilteredTradersList());
            if(jsonarray != null && jsonarray.length() >0) {
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String tradersdetails_id= jsonobject.getString("tradersdetails_id");
                    String lb_sno= jsonobject.getString("lb_sno");
                    String tradedetails_id= jsonobject.getString("tradedetails_id");
                    String lb_tradecode= jsonobject.getString("lb_tradecode");
                    String description_en= jsonobject.getString("description_en");
                    String description_ta= jsonobject.getString("description_ta");
                    String traderate= jsonobject.getString("traderate");
                    String lb_traderscode= jsonobject.getString("lb_traderscode");
                    String traders_rate= jsonobject.getString("traders_rate");
                    String traders_type= jsonobject.getString("traders_type");
                    String date= jsonobject.getString("date");
                    String tradersperiod= jsonobject.getString("tradersperiod");
                    String tradedesct= jsonobject.getString("tradedesct");
                    String tradedesce= jsonobject.getString("tradedesce");
                    String wardid= jsonobject.getString("wardid");
                    String streetid= jsonobject.getString("streetid");
                    String doorno= jsonobject.getString("doorno");
                    String licencefor= jsonobject.getString("licencefor");
                    String fin_year= jsonobject.getString("from_fin_year");
                    String traderstypee= jsonobject.getString("traderstypee");
                    String demandtype= jsonobject.getString("demandtype");
                    String onlineapplicationno= jsonobject.getString("onlineapplicationno");
                    String mobileno= jsonobject.getString("mobileno");
                    String email= jsonobject.getString("email");
                    String paymentstatus= jsonobject.getString("paymentstatus");
                    String licencetypeid= jsonobject.getString("licencetypeid");
                    String traders_license_type_name= jsonobject.getString("traders_license_type_name");
                    String apgender= jsonobject.getString("apgender");
                    String apage= jsonobject.getString("apage");
                    String apfathername_ta= jsonobject.getString("apfathername_ta");
                    String apfathername_en= jsonobject.getString("apfathername_en");
                    String licenceno= jsonobject.getString("licenceno");
                    String paymentdate= jsonobject.getString("paymentdate");
                    String statecode= jsonobject.getString("statecode");
                    String dcode= jsonobject.getString("dcode");
                    String lbcode= jsonobject.getString("lbcode");
                    String apname_ta= jsonobject.getString("apname_ta");
                    String apname_en= jsonobject.getString("apname_en");
                    String establishment_name_ta= jsonobject.getString("establishment_name_ta");
                    String establishment_name_en= jsonobject.getString("establishment_name_en");
                    String licence_validity= jsonobject.getString("licence_validity");
                    TPtaxModel Detail = new TPtaxModel();
                    Detail.setTraderName(apname_en);
                    Detail.setTraderCode(lb_traderscode);
                    Detail.setTraders_typ(traders_type);
                    Detail.setTradedesct(tradedesct);
                    Detail.setDoorno(doorno);
                    Detail.setApfathername_ta(apfathername_ta);
                    Detail.setEstablishment_name_ta(establishment_name_ta);
                    Detail.setLicenceValidity(licence_validity);
                    Detail.setTraders_license_type_name(traders_license_type_name);
                    Detail.setTraderPayment(paymentstatus);
                    Detail.setMobileno(mobileno);
                    Detail.setPaymentdate(paymentdate);
                    Detail.setTradersdetails_id(tradersdetails_id);
                    Detail.setLb_sno(lb_sno);
                    Detail.setTradedetails_id(tradedetails_id);
                    Detail.setDescription_en(description_en);
                    Detail.setDescription_ta(description_ta);
                    Detail.setTraderate(traderate);
                    Detail.setTraders_rate(traders_rate);
                    Detail.setTrade_date(date);
                    Detail.setTradersperiod(tradersperiod);
                    Detail.setTradedesce(tradedesce);
                    Detail.setLicencefor(licencefor);
                    Detail.setFin_year(fin_year);
                    Detail.setTraderstypee(traderstypee);
                    Detail.setDemandtype(demandtype);
                    Detail.setOnlineapplicationno(onlineapplicationno);
                    Detail.setEmail(email);
                    Detail.setLicencetypeid(licencetypeid);
                    Detail.setApgender(apgender);
                    Detail.setApage(apage);
                    Detail.setApfathername_en(apfathername_en);
                    Detail.setLicenceno(licenceno);
                    Detail.setStatecode(statecode);
                    Detail.setDcode(dcode);
                    Detail.setLbcode(lbcode);
                    Detail.setApname_ta(apname_ta);
                    Detail.setEstablishment_name_en(establishment_name_en);
                    tradersList.add(Detail);
                }
                Collections.sort(tradersList, (lhs, rhs) -> lhs.getTraderName().compareTo(rhs.getTraderName()));
                shoewDetails();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
