package com.nic.TPTaxDepartment.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.nic.TPTaxDepartment.R;
import com.nic.TPTaxDepartment.constant.AppConstant;
import com.nic.TPTaxDepartment.databinding.ExistTraderDetailsWholeViewBinding;
import com.nic.TPTaxDepartment.model.TPtaxModel;
import com.nic.TPTaxDepartment.session.PrefManager;
import com.nic.TPTaxDepartment.utils.Utils;
import com.nic.TPTaxDepartment.windowpreferences.WindowPreferencesManager;

import java.util.ArrayList;

public class NewExistTraderWholeDetailsViewClass extends AppCompatActivity {

    ExistTraderDetailsWholeViewBinding existTraderDetailsWholeViewBinding;
    Context context;
    private PrefManager prefManager;
    ArrayList<TPtaxModel> traders;
    ArrayList<TPtaxModel> tradersImageList;
    int position;
    int tradersImagePosition;
    Boolean flag = false;
    int pageNumber=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        existTraderDetailsWholeViewBinding = DataBindingUtil.setContentView(this, R.layout.exist_trader_details_whole_view);
        existTraderDetailsWholeViewBinding.setActivity(this);
        context = this;
        prefManager = new PrefManager(this);
       /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mApps);
        existTraderDetailsWholeViewBinding.licenceValidity.setAdapter(adapter);*/
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());

        //getIntent Data
        traders = new ArrayList<TPtaxModel>();
        tradersImageList = new ArrayList<TPtaxModel>();
        flag = getIntent().getBooleanExtra("flag", false);
        if (flag) {
            position = getIntent().getIntExtra("position", 0);
            tradersImagePosition = getIntent().getIntExtra("tradersImagePosition", 0);
            traders = (ArrayList<TPtaxModel>) getIntent().getSerializableExtra("tradersList");
            tradersImageList = (ArrayList<TPtaxModel>) getIntent().getSerializableExtra("tradersImageList");
        }
        
        try {
            shoewDetails();
        }catch (Exception e){
            e.printStackTrace();
        }
        existTraderDetailsWholeViewBinding.fullDetails.setVisibility(View.VISIBLE);
        existTraderDetailsWholeViewBinding.documentLayout.setVisibility(View.GONE);
        existTraderDetailsWholeViewBinding.editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewExistTraderWholeDetailsViewClass.this, ExistTradeViewClass.class);
                intent.putExtra("position", 0);
                intent.putExtra("tradersList", traders);
                intent.putExtra("tradersImageList", tradersImageList);
                intent.putExtra("tradersImagePosition", 0);
                intent.putExtra("flag",true);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(existTraderDetailsWholeViewBinding.fullDetails.getVisibility()==View.VISIBLE){
            super.onBackPressed();
            setResult(Activity.RESULT_CANCELED);
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
        else  if(existTraderDetailsWholeViewBinding.documentLayout.getVisibility() == View.VISIBLE  ){
            existTraderDetailsWholeViewBinding.fullDetails.setVisibility(View.VISIBLE);
            existTraderDetailsWholeViewBinding.documentLayout.setVisibility(View.GONE);
        }
    }

    public void shoewDetails() {

        
        String tradersdetails_id= changeTextColor(getApplicationContext().getResources().getString(R.string.tradersDetailsID))+traders.get(position).getTradersdetails_id() + "\n"+ "\n";
        String lb_sno= changeTextColor(getApplicationContext().getResources().getString(R.string.lb_sno))+traders.get(position).getLb_sno() + "\n"+ "\n";
        String lb_traderscode= changeTextColor(getApplicationContext().getResources().getString(R.string.lb_trader_code))+traders.get(position).getTraderCode() + "\n"+ "\n";
        String tradedetails_id= changeTextColor(getApplicationContext().getResources().getString(R.string.trade_details_id))+traders.get(position).getTradedetails_id() + "\n"+ "\n";
        String traders_rate= changeTextColor(getApplicationContext().getResources().getString(R.string.trader_rate))+traders.get(position).getTraders_rate() + "\n"+ "\n";
        String traders_type= changeTextColor(getApplicationContext().getResources().getString(R.string.trader_type))+traders.get(position).getTraders_typ() + "\n"+ "\n";
        String tradersperiod= changeTextColor(getApplicationContext().getResources().getString(R.string.trader_period))+traders.get(position).getTradersperiod() + "\n"+ "\n";
        String traderstypee= changeTextColor(getApplicationContext().getResources().getString(R.string.trader_type_en))+traders.get(position).getTraders_typ() + "\n"+ "\n";

        String apname_ta= changeTextColor(getApplicationContext().getResources().getString(R.string.trader_name_ta))+traders.get(position).getApname_ta() + "\n"+ "\n";
        String apname_en=changeTextColor(getApplicationContext().getResources().getString(R.string.trader_name_en))+traders.get(position).getTraderName() + "\n"+ "\n";
        String apfathername_ta= changeTextColor(getApplicationContext().getResources().getString(R.string.father_name_ta))+traders.get(position).getApfathername_en() + "\n"+ "\n";
        String apfathername_en= changeTextColor(getApplicationContext().getResources().getString(R.string.father_name_en))+traders.get(position).getApfathername_ta() + "\n"+ "\n";
        String apgender= changeTextColor(getApplicationContext().getResources().getString(R.string.gender_c))+traders.get(position).getApgenderId() + "\n"+ "\n";


        String apage= changeTextColor(getApplicationContext().getResources().getString(R.string.age_c))+traders.get(position).getApage() + "\n"+ "\n";
        String date= changeTextColor(getApplicationContext().getResources().getString(R.string.date_c))+traders.get(position).getTrade_date() + "\n"+ "\n";
        String dcode= changeTextColor(getApplicationContext().getResources().getString(R.string.district_code))+traders.get(position).getDcode() + "\n"+ "\n";
        String doorno= changeTextColor(getApplicationContext().getResources().getString(R.string.door_no))+traders.get(position).getDoorno() + "\n"+ "\n";
        String email= changeTextColor(getApplicationContext().getResources().getString(R.string.email))+traders.get(position).getEmail() + "\n"+ "\n";


        String description_ta= changeTextColor(getApplicationContext().getResources().getString(R.string.establishment_name_ta))+traders.get(position).getEstablishment_name_ta() + "\n"+ "\n";
        String description_en= changeTextColor(getApplicationContext().getResources().getString(R.string.establishment_name_en))+traders.get(position).getEstablishment_name_en() + "\n"+ "\n";

        String statecode=changeTextColor(getApplicationContext().getResources().getString(R.string.state_code))+traders.get(position).getStatecode() + "\n"+ "\n";
        String wardid= changeTextColor(getApplicationContext().getResources().getString(R.string.ward_id))+traders.get(position).getWardId() + "\n"+ "\n";
        String streetid= changeTextColor(getApplicationContext().getResources().getString(R.string.street_id))+traders.get(position).getStreetId() + "\n"+ "\n";;


        String mobileno= changeTextColor(getApplicationContext().getResources().getString(R.string.mobile_no))+traders.get(position).getMobileno() + "\n"+ "\n";
        String licencetypeid= changeTextColor(getApplicationContext().getResources().getString(R.string.licence_type_id))+traders.get(position).getLicencetypeid() + "\n"+ "\n";
        String licence_validity= changeTextColor(getApplicationContext().getResources().getString(R.string.licence_validity))+traders.get(position).getLicence_validity() + "\n"+ "\n";
        String traders_license_type_name= changeTextColor(getApplicationContext().getResources().getString(R.string.licence_type))+traders.get(position).getTraders_license_type_name() + "\n"+ "\n";


        String ownerStatus= changeTextColor(getApplicationContext().getResources().getString(R.string.owner_status))+traders.get(position).getOwnerStatus() + "\n"+ "\n";
        String motorStatus= changeTextColor(getApplicationContext().getResources().getString(R.string.motor_status))+traders.get(position).getMotorStatus() + "\n"+ "\n";
        String generatorStatus= changeTextColor(getApplicationContext().getResources().getString(R.string.generator_status))+traders.get(position).getGeneratorStatus() + "\n"+ "\n";
        String propertyStatus= changeTextColor(getApplicationContext().getResources().getString(R.string.property_status))+traders.get(position).getPropertyStatus() + "\n"+ "\n";
        String professtionlStatus= changeTextColor(getApplicationContext().getResources().getString(R.string.professional_status))+traders.get(position).getProfesstionlStatus() + "\n"+ "\n";
        String motor_type_id= changeTextColor(getApplicationContext().getResources().getString(R.string.motor_type_id))+traders.get(position).getMotor_type_id() + "\n"+ "\n";


        String amount_range_id= changeTextColor(getApplicationContext().getResources().getString(R.string.amount_range_id))+traders.get(position).getAmount_range_id() + "\n"+ "\n";
        String generator_range_id= changeTextColor(getApplicationContext().getResources().getString(R.string.generator_range_id))+traders.get(position).getGenerator_range_id() + "\n"+ "\n";
        String propertyTaxAssessmentNumber= changeTextColor(getApplicationContext().getResources().getString(R.string.property_tax_assessment_no))+traders.get(position).getPropertyTaxAssessmentNumber() + "\n"+ "\n";
        String document= changeTextColor(getApplicationContext().getResources().getString(R.string.document_c))+traders.get(position).getDocument() + "\n"+ "\n";
        String remark= changeTextColor(getApplicationContext().getResources().getString(R.string.remark_c))+traders.get(position).getRemark() + "\n"+ "\n";


        String annual_sale_production_amount= changeTextColor(getApplicationContext().getResources().getString(R.string.annual_sale_production_amount))+traders.get(position).getAnnual_sale_production_amount() + "\n"+ "\n";
        String annual_sale_production_range= changeTextColor(getApplicationContext().getResources().getString(R.string.annual_sale_production_range))+traders.get(position).getAnnual_sale_production_range() + "\n"+ "\n";
        String generator_range= changeTextColor(getApplicationContext().getResources().getString(R.string.generator_range))+traders.get(position).getGenerator_range() + "\n"+ "\n";
        String generator_range_amount= changeTextColor(getApplicationContext().getResources().getString(R.string.generator_range_amount))+traders.get(position).getGenerator_range_amount() + "\n"+ "\n";
        String motor_range= changeTextColor(getApplicationContext().getResources().getString(R.string.motor_range))+traders.get(position).getMotor_range() + "\n"+ "\n";
        String motor_range_amount= changeTextColor(getApplicationContext().getResources().getString(R.string.motor_range_amount))+traders.get(position).getMotor_range_amount() + "\n"+ "\n";
        String street_name_ta= changeTextColor(getApplicationContext().getResources().getString(R.string.street_name_ta))+traders.get(position).getStreet_name_ta() + "\n"+ "\n";
        String street_name_en= changeTextColor(getApplicationContext().getResources().getString(R.string.street_name_en))+traders.get(position).getStreet_name_en() + "\n"+ "\n";
        String ward_name_en= changeTextColor(getApplicationContext().getResources().getString(R.string.ward_name_en))+traders.get(position).getWard_name_en() + "\n"+ "\n";
        String ward_name_ta= changeTextColor(getApplicationContext().getResources().getString(R.string.ward_name_ta))+traders.get(position).getWard_name_ta() + "\n"+ "\n";

        existTraderDetailsWholeViewBinding.t1.setText(Html.fromHtml(tradersdetails_id));
        existTraderDetailsWholeViewBinding.t2.setText(Html.fromHtml(lb_sno));
        existTraderDetailsWholeViewBinding.t3.setText(Html.fromHtml(lb_traderscode));
        existTraderDetailsWholeViewBinding.t4.setText(Html.fromHtml(tradedetails_id));
        existTraderDetailsWholeViewBinding.t5.setText(Html.fromHtml(traders_rate));
        existTraderDetailsWholeViewBinding.t6.setText(Html.fromHtml(traders_type));
        existTraderDetailsWholeViewBinding.t7.setText(Html.fromHtml(tradersperiod));
        existTraderDetailsWholeViewBinding.t8.setText(Html.fromHtml(traderstypee));
        existTraderDetailsWholeViewBinding.t9.setText(Html.fromHtml(apname_ta));
        existTraderDetailsWholeViewBinding.t10.setText(Html.fromHtml(apname_en));
        existTraderDetailsWholeViewBinding.t11.setText(Html.fromHtml(apfathername_ta));
        existTraderDetailsWholeViewBinding.t12.setText(Html.fromHtml(apfathername_en));
        existTraderDetailsWholeViewBinding.t13.setText(Html.fromHtml(apgender));
        existTraderDetailsWholeViewBinding.t14.setText(Html.fromHtml(apage));
        existTraderDetailsWholeViewBinding.t15.setText(Html.fromHtml(date));
        existTraderDetailsWholeViewBinding.t16.setText(Html.fromHtml(dcode));
        existTraderDetailsWholeViewBinding.t17.setText(Html.fromHtml(doorno));
        existTraderDetailsWholeViewBinding.t18.setText(Html.fromHtml(email));
        existTraderDetailsWholeViewBinding.t19.setText(Html.fromHtml(description_ta));
        existTraderDetailsWholeViewBinding.t20.setText(Html.fromHtml(description_en));


        existTraderDetailsWholeViewBinding.t21.setText(Html.fromHtml(statecode));
        existTraderDetailsWholeViewBinding.t22.setText(Html.fromHtml(wardid));
        existTraderDetailsWholeViewBinding.t23.setText(Html.fromHtml(streetid));
        existTraderDetailsWholeViewBinding.t24.setText(Html.fromHtml(mobileno));
        existTraderDetailsWholeViewBinding.t25.setText(Html.fromHtml(licencetypeid));
        existTraderDetailsWholeViewBinding.t26.setText(Html.fromHtml(licence_validity));
        existTraderDetailsWholeViewBinding.t27.setText(Html.fromHtml(traders_license_type_name));

        existTraderDetailsWholeViewBinding.t28.setText(Html.fromHtml(ownerStatus));
        existTraderDetailsWholeViewBinding.t29.setText(Html.fromHtml(motorStatus));
        existTraderDetailsWholeViewBinding.t30.setText(Html.fromHtml(generatorStatus));
        existTraderDetailsWholeViewBinding.t31.setText(Html.fromHtml(propertyStatus));
        existTraderDetailsWholeViewBinding.t32.setText(Html.fromHtml(professtionlStatus));
        existTraderDetailsWholeViewBinding.t33.setText(Html.fromHtml(motor_type_id));
        existTraderDetailsWholeViewBinding.t34.setText(Html.fromHtml(amount_range_id));
        existTraderDetailsWholeViewBinding.t35.setText(Html.fromHtml(generator_range_id));
        existTraderDetailsWholeViewBinding.t36.setText(Html.fromHtml(propertyTaxAssessmentNumber));
        existTraderDetailsWholeViewBinding.t37.setText(Html.fromHtml(remark));

        existTraderDetailsWholeViewBinding.t38.setText(Html.fromHtml(annual_sale_production_amount));
        existTraderDetailsWholeViewBinding.t39.setText(Html.fromHtml(annual_sale_production_range));
        existTraderDetailsWholeViewBinding.t40.setText(Html.fromHtml(generator_range));
        existTraderDetailsWholeViewBinding.t41.setText(Html.fromHtml(generator_range_amount));
        existTraderDetailsWholeViewBinding.t42.setText(Html.fromHtml(motor_range));
        existTraderDetailsWholeViewBinding.t43.setText(Html.fromHtml(motor_range_amount));
        existTraderDetailsWholeViewBinding.t44.setText(Html.fromHtml(street_name_ta));
        existTraderDetailsWholeViewBinding.t45.setText(Html.fromHtml(street_name_en));
        existTraderDetailsWholeViewBinding.t46.setText(Html.fromHtml(ward_name_en));
        existTraderDetailsWholeViewBinding.t47.setText(Html.fromHtml(ward_name_ta));







        existTraderDetailsWholeViewBinding.trderDetailsValue.setText(Html.fromHtml(tradersdetails_id	+"<br/>"+"<br/>"+ lb_sno +"<br/>"+"<br/>"+	lb_traderscode+	"<br/>"+"<br/>"+tradedetails_id	+"<br/>"+"<br/>"+ traders_rate	+"<br/>"+"<br/>"+ traders_type
                +"<br/>"+"<br/>"+ tradersperiod+ "<br/>"+"<br/>"+ traderstypee+"<br/>"+"<br/>"+ apname_ta +"<br/>"+"<br/>"+ apname_en +"<br/>"+"<br/>"+ apfathername_ta
                +"<br/>"+"<br/>"+ apfathername_en+"<br/>"+"<br/>"+ 	apgender+"<br/>"+"<br/>"+  apage+"<br/>"+"<br/>"+	date	+ "<br/>"+"<br/>"+ dcode+"<br/>"+"<br/>"+	doorno+"<br/>"+"<br/>"+	email
                +"<br/>"+"<br/>"+ description_ta+"<br/>"+"<br/>"+	description_en+"<br/>"+"<br/>"+	statecode+"<br/>"+"<br/>"+ wardid+"<br/>"+"<br/>"+	streetid+"<br/>"+"<br/>"+	mobileno	+"<br/>"+"<br/>"+licence_validity+"<br/>"+"<br/>"+	licencetypeid
                +"<br/>"+"<br/>"+traders_license_type_name+	"<br/>"+"<br/>"+ ownerStatus	+ "<br/>"+"<br/>"+ motorStatus + "<br/>"+"<br/>"+ generatorStatus +"<br/>"+"<br/>"+ propertyStatus+"<br/>"+"<br/>"+	professtionlStatus	+"<br/>"+"<br/>"+ motor_type_id+"<br/>"+"<br/>"+	amount_range_id
                +"<br/>"+"<br/>"+generator_range_id	+"<br/>"+"<br/>"+propertyTaxAssessmentNumber +"<br/>"+"<br/>"+remark+"<br/>"+"<br/>"+annual_sale_production_amount+"<br/>"+"<br/>"+annual_sale_production_range+"<br/>"+"<br/>"+generator_range+"<br/>"+"<br/>"+generator_range_amount
                +"<br/>"+"<br/>"+motor_range+"<br/>"+"<br/>"+motor_range_amount +"<br/>"+"<br/>"+street_name_ta+"<br/>"+"<br/>"+street_name_en+"<br/>"+"<br/>"+ward_name_en+"<br/>"+"<br/>"+ward_name_ta));

        //existTraderDetailsWholeViewBinding.trderDetailsValue.setText(Html.fromHtml(tradersdetails_id +"<br/>"+"<br/>"+  lb_sno));
    }

    public String changeTextColor(String text){
        String input="<font color=" + "#878787" + ">" + text  + "</font> ";
        return input;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void viewDocument() {
        byte[] decodedString = new byte[0];
        try {
            //byte[] name = java.util.Base64.getEncoder().encode(fileString.getBytes());
            decodedString = Base64.decode(/*DocumentString*/traders.get(position).getDocument().toString(), Base64.DEFAULT);
            System.out.println(new String(decodedString));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        existTraderDetailsWholeViewBinding.documentLayout.setVisibility(View.VISIBLE);
        existTraderDetailsWholeViewBinding.fullDetails.setVisibility(View.GONE);

        existTraderDetailsWholeViewBinding.documentViewer.fromBytes(decodedString).
                onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        pageNumber = page;
                        setTitle(String.format("%s %s / %s", "PDF", page + 1, pageCount));
                        existTraderDetailsWholeViewBinding.pageNum.setText(pageNumber+1 +"/"+pageCount);
                    }
                }).defaultPage(pageNumber).swipeHorizontal(true).enableDoubletap(true).load();

    }
    public void viewImageScreen() {


            if (tradersImageList.get(tradersImagePosition).getImageByte() != null ) {
                String value = new String(tradersImageList.get(tradersImagePosition).getImageByte());
                Intent intent = new Intent(this, FullImageActivity.class);
                intent.putExtra(AppConstant.TRADE_CODE, "");
                intent.putExtra(AppConstant.MOBILE, "");
                intent.putExtra(AppConstant.KEY_SCREEN_STATUS, "");
                intent.putExtra(AppConstant.TRADE_IMAGE,/*TraderImageString*/ value);
                intent.putExtra("key", "ExistTradeViewClass");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            } else {
                Utils.showAlert(NewExistTraderWholeDetailsViewClass.this, getApplicationContext().getResources().getString(R.string.no_image_found));
            }

    }

    public void dashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

}
