package com.nic.TPTaxDepartment.model;

import android.graphics.Bitmap;

public class CommonModel {
    public String statecode;
    public String dcode;
    public String lbcode;
    public String ward_id;
    public String ward_code;
    public String ward_name_en;
    public String ward_name_ta;
    public String streetid;
    public String street_code;
    public String street_name_en;
    public String street_name_ta;
    public String building_zone;
    public String traders_license_type_id;
    public String traders_license_type_name;
    public String taxtypeid;
    public String taxtypedesc_en;
    public String taxtypedesc_ta;
    public String taxcollection_methodlogy;
    public String installmenttypeid;
    public  String FIN_YEAR_ID ;
    public  String FIN_YEAR ;
    public  String FROM_FIN_YEAR ;
    public  String FROM_FIN_MON ;
    public  String TO_FIN_YEAR ;
    public  String TO_FIN_MON ;

   ///TradeCodeListDetails
   public  String TRADE_DETAILS_ID;
    public  String LB_TRADE_CODE ;
    public  String DESCRIPTION_EN;
    public  String DESCRIPTION_TA ;
    public  String DATE_FIELD;
    public  String FINYEAR ;
    public  String TRADE_RATE;
    public  String LICENSE_TYPE_ID ;
    public  String STATECODE ;


    ////FieldVisitStatus
    public String FIELD_VISIT_STATUS_ID;
    public String FIELD_VISIT_STATUS;


    ///ServiceListFieldVisitTypes
    public String service_list_field_visit_taxtype_id;
    public String service_list_field_visit_service_id;
    public String service_list_field_visit_types_desc;


  /// Assessment Status Fields
    public String lb_assessmentno;
    public String assessmentnameeng;
    public String organizationtype;

    public String assessment_no;
    public String owner_name;
    public String father_name;
    public String permanent_address;
    public String area_in_sq_feet;

    public String lb_traderscode;
    public String apfathername_ta;
    public String apfathername_en;
    public String doorno;
    public String traders_rate;
    public String from_fin_year;
    public String to_fin_year;
    public String trade_description_en;

    public String lb_connectionno;
    public String connectionname;
    public String water_charges;

    public String lb_leaseassessmentno;
    public String leasee_name_en;
    public String leasee_name_ta;
    public String lease_type_code;
    public String lease_type_description_en;
    public String lease_payment_due_type;
    public String annuallease_amount;


////Motor,Generator,Annual List;
    public String annual_id,motor_id,generator_id;
    public String annual_sale,motor_range,generator_range;
    ///////

    String request_id;
    String data_ref_id;
    String ownername;
    String door_no;
    String plotarea;
    String buildage;
    String buildusage;
    String buildstructure;
    String taxlocation;
    private Bitmap Image;
    private String Latitude;
    private String Longitude;
    private String remark;

    /////............../////


    public String getLb_leaseassessmentno() {
        return lb_leaseassessmentno;
    }

    public CommonModel setLb_leaseassessmentno(String lb_leaseassessmentno) {
        this.lb_leaseassessmentno = lb_leaseassessmentno;
        return this;
    }

    public String getLeasee_name_en() {
        return leasee_name_en;
    }

    public CommonModel setLeasee_name_en(String leasee_name_en) {
        this.leasee_name_en = leasee_name_en;
        return this;
    }

    public String getLeasee_name_ta() {
        return leasee_name_ta;
    }

    public CommonModel setLeasee_name_ta(String leasee_name_ta) {
        this.leasee_name_ta = leasee_name_ta;
        return this;
    }

    public String getLease_type_code() {
        return lease_type_code;
    }

    public CommonModel setLease_type_code(String lease_type_code) {
        this.lease_type_code = lease_type_code;
        return this;
    }

    public String getLease_type_description_en() {
        return lease_type_description_en;
    }

    public CommonModel setLease_type_description_en(String lease_type_description_en) {
        this.lease_type_description_en = lease_type_description_en;
        return this;
    }

    public String getLease_payment_due_type() {
        return lease_payment_due_type;
    }

    public CommonModel setLease_payment_due_type(String lease_payment_due_type) {
        this.lease_payment_due_type = lease_payment_due_type;
        return this;
    }

    public String getAnnuallease_amount() {
        return annuallease_amount;
    }

    public CommonModel setAnnuallease_amount(String annuallease_amount) {
        this.annuallease_amount = annuallease_amount;
        return this;
    }

    public String getRequest_id() {
        return request_id;
    }

    public CommonModel setRequest_id(String request_id) {
        this.request_id = request_id;
        return this;
    }

    public String getData_ref_id() {
        return data_ref_id;
    }

    public CommonModel setData_ref_id(String data_ref_id) {
        this.data_ref_id = data_ref_id;
        return this;
    }

    public String getOwnername() {
        return ownername;
    }

    public CommonModel setOwnername(String ownername) {
        this.ownername = ownername;
        return this;
    }

    public String getDoor_no() {
        return door_no;
    }

    public CommonModel setDoor_no(String door_no) {
        this.door_no = door_no;
        return this;
    }

    public String getPlotarea() {
        return plotarea;
    }

    public CommonModel setPlotarea(String plotarea) {
        this.plotarea = plotarea;
        return this;
    }

    public String getBuildage() {
        return buildage;
    }

    public CommonModel setBuildage(String buildage) {
        this.buildage = buildage;
        return this;
    }

    public String getBuildusage() {
        return buildusage;
    }

    public CommonModel setBuildusage(String buildusage) {
        this.buildusage = buildusage;
        return this;
    }

    public String getBuildstructure() {
        return buildstructure;
    }

    public CommonModel setBuildstructure(String buildstructure) {
        this.buildstructure = buildstructure;
        return this;
    }

    public String getTaxlocation() {
        return taxlocation;
    }

    public CommonModel setTaxlocation(String taxlocation) {
        this.taxlocation = taxlocation;
        return this;
    }

    public Bitmap getImage() {
        return Image;
    }

    public CommonModel setImage(Bitmap image) {
        Image = image;
        return this;
    }

    public String getLatitude() {
        return Latitude;
    }

    public CommonModel setLatitude(String latitude) {
        Latitude = latitude;
        return this;
    }

    public String getLongitude() {
        return Longitude;
    }

    public CommonModel setLongitude(String longitude) {
        Longitude = longitude;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public CommonModel setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getLb_connectionno() {
        return lb_connectionno;
    }

    public CommonModel setLb_connectionno(String lb_connectionno) {
        this.lb_connectionno = lb_connectionno;
        return this;
    }

    public String getConnectionname() {
        return connectionname;
    }

    public CommonModel setConnectionname(String connectionname) {
        this.connectionname = connectionname;
        return this;
    }

    public String getWater_charges() {
        return water_charges;
    }

    public CommonModel setWater_charges(String water_charges) {
        this.water_charges = water_charges;
        return this;
    }

    public String getLb_traderscode() {
        return lb_traderscode;
    }

    public CommonModel setLb_traderscode(String lb_traderscode) {
        this.lb_traderscode = lb_traderscode;
        return this;
    }

    public String getApfathername_ta() {
        return apfathername_ta;
    }

    public CommonModel setApfathername_ta(String apfathername_ta) {
        this.apfathername_ta = apfathername_ta;
        return this;
    }

    public String getApfathername_en() {
        return apfathername_en;
    }

    public CommonModel setApfathername_en(String apfathername_en) {
        this.apfathername_en = apfathername_en;
        return this;
    }

    public String getDoorno() {
        return doorno;
    }

    public CommonModel setDoorno(String doorno) {
        this.doorno = doorno;
        return this;
    }

    public String getTraders_rate() {
        return traders_rate;
    }

    public CommonModel setTraders_rate(String traders_rate) {
        this.traders_rate = traders_rate;
        return this;
    }

    public String getFrom_fin_year() {
        return from_fin_year;
    }

    public CommonModel setFrom_fin_year(String from_fin_year) {
        this.from_fin_year = from_fin_year;
        return this;
    }

    public String getTo_fin_year() {
        return to_fin_year;
    }

    public CommonModel setTo_fin_year(String to_fin_year) {
        this.to_fin_year = to_fin_year;
        return this;
    }

    public String getTrade_description_en() {
        return trade_description_en;
    }

    public CommonModel setTrade_description_en(String trade_description_en) {
        this.trade_description_en = trade_description_en;
        return this;
    }

    public String getAssessment_no() {
        return assessment_no;
    }

    public CommonModel setAssessment_no(String assessment_no) {
        this.assessment_no = assessment_no;
        return this;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public CommonModel setOwner_name(String owner_name) {
        this.owner_name = owner_name;
        return this;
    }

    public String getFather_name() {
        return father_name;
    }

    public CommonModel setFather_name(String father_name) {
        this.father_name = father_name;
        return this;
    }

    public String getPermanent_address() {
        return permanent_address;
    }

    public CommonModel setPermanent_address(String permanent_address) {
        this.permanent_address = permanent_address;
        return this;
    }

    public String getArea_in_sq_feet() {
        return area_in_sq_feet;
    }

    public CommonModel setArea_in_sq_feet(String area_in_sq_feet) {
        this.area_in_sq_feet = area_in_sq_feet;
        return this;
    }

    public String getLb_assessmentno() {
        return lb_assessmentno;
    }

    public CommonModel setLb_assessmentno(String lb_assessmentno) {
        this.lb_assessmentno = lb_assessmentno;
        return this;
    }

    public String getAssessmentnameeng() {
        return assessmentnameeng;
    }

    public CommonModel setAssessmentnameeng(String assessmentnameeng) {
        this.assessmentnameeng = assessmentnameeng;
        return this;
    }

    public String getOrganizationtype() {
        return organizationtype;
    }

    public CommonModel setOrganizationtype(String organizationtype) {
        this.organizationtype = organizationtype;
        return this;
    }

    public String getService_list_field_visit_taxtype_id() {
        return service_list_field_visit_taxtype_id;
    }

    public void setService_list_field_visit_taxtype_id(String service_list_field_visit_taxtype_id) {
        this.service_list_field_visit_taxtype_id = service_list_field_visit_taxtype_id;
    }

    public String getService_list_field_visit_service_id() {
        return service_list_field_visit_service_id;
    }

    public void setService_list_field_visit_service_id(String service_list_field_visit_service_id) {
        this.service_list_field_visit_service_id = service_list_field_visit_service_id;
    }

    public String getService_list_field_visit_types_desc() {
        return service_list_field_visit_types_desc;
    }

    public void setService_list_field_visit_types_desc(String service_list_field_visit_types_desc) {
        this.service_list_field_visit_types_desc = service_list_field_visit_types_desc;
    }

    public String getFIELD_VISIT_STATUS_ID() {
        return FIELD_VISIT_STATUS_ID;
    }

    public void setFIELD_VISIT_STATUS_ID(String FIELD_VISIT_STATUS_ID) {
        this.FIELD_VISIT_STATUS_ID = FIELD_VISIT_STATUS_ID;
    }

    public String getFIELD_VISIT_STATUS() {
        return FIELD_VISIT_STATUS;
    }

    public void setFIELD_VISIT_STATUS(String FIELD_VISIT_STATUS) {
        this.FIELD_VISIT_STATUS = FIELD_VISIT_STATUS;
    }

    public String getTRADE_DETAILS_ID() {
        return TRADE_DETAILS_ID;
    }

    public void setTRADE_DETAILS_ID(String TRADE_DETAILS_ID) {
        this.TRADE_DETAILS_ID = TRADE_DETAILS_ID;
    }

    public String getLB_TRADE_CODE() {
        return LB_TRADE_CODE;
    }

    public void setLB_TRADE_CODE(String LB_TRADE_CODE) {
        this.LB_TRADE_CODE = LB_TRADE_CODE;
    }

    public String getDESCRIPTION_EN() {
        return DESCRIPTION_EN;
    }

    public void setDESCRIPTION_EN(String DESCRIPTION_EN) {
        this.DESCRIPTION_EN = DESCRIPTION_EN;
    }

    public String getDESCRIPTION_TA() {
        return DESCRIPTION_TA;
    }

    public void setDESCRIPTION_TA(String DESCRIPTION_TA) {
        this.DESCRIPTION_TA = DESCRIPTION_TA;
    }

    public String getDATE_FIELD() {
        return DATE_FIELD;
    }

    public void setDATE_FIELD(String DATE_FIELD) {
        this.DATE_FIELD = DATE_FIELD;
    }

    public String getFINYEAR() {
        return FINYEAR;
    }

    public void setFINYEAR(String FINYEAR) {
        this.FINYEAR = FINYEAR;
    }

    public String getTRADE_RATE() {
        return TRADE_RATE;
    }

    public void setTRADE_RATE(String TRADE_RATE) {
        this.TRADE_RATE = TRADE_RATE;
    }

    public String getLICENSE_TYPE_ID() {
        return LICENSE_TYPE_ID;
    }

    public void setLICENSE_TYPE_ID(String LICENSE_TYPE_ID) {
        this.LICENSE_TYPE_ID = LICENSE_TYPE_ID;
    }

    public String getSTATECODE() {
        return STATECODE;
    }

    public void setSTATECODE(String STATECODE) {
        this.STATECODE = STATECODE;
    }

    /////.................//////
    public String getFIN_YEAR_ID() {
        return FIN_YEAR_ID;
    }

    public void setFIN_YEAR_ID(String FIN_YEAR_ID) {
        this.FIN_YEAR_ID = FIN_YEAR_ID;
    }

    public String getFIN_YEAR() {
        return FIN_YEAR;
    }

    public void setFIN_YEAR(String FIN_YEAR) {
        this.FIN_YEAR = FIN_YEAR;
    }

    public String getFROM_FIN_YEAR() {
        return FROM_FIN_YEAR;
    }

    public void setFROM_FIN_YEAR(String FROM_FIN_YEAR) {
        this.FROM_FIN_YEAR = FROM_FIN_YEAR;
    }

    public String getFROM_FIN_MON() {
        return FROM_FIN_MON;
    }

    public void setFROM_FIN_MON(String FROM_FIN_MON) {
        this.FROM_FIN_MON = FROM_FIN_MON;
    }

    public String getTO_FIN_YEAR() {
        return TO_FIN_YEAR;
    }

    public void setTO_FIN_YEAR(String TO_FIN_YEAR) {
        this.TO_FIN_YEAR = TO_FIN_YEAR;
    }

    public String getTO_FIN_MON() {
        return TO_FIN_MON;
    }

    public void setTO_FIN_MON(String TO_FIN_MON) {
        this.TO_FIN_MON = TO_FIN_MON;
    }



    public String getTaxtypeid() {
        return taxtypeid;
    }

    public void setTaxtypeid(String taxtypeid) {
        this.taxtypeid = taxtypeid;
    }

    public String getTaxtypedesc_en() {
        return taxtypedesc_en;
    }

    public void setTaxtypedesc_en(String taxtypedesc_en) {
        this.taxtypedesc_en = taxtypedesc_en;
    }

    public String getTaxtypedesc_ta() {
        return taxtypedesc_ta;
    }

    public void setTaxtypedesc_ta(String taxtypedesc_ta) {
        this.taxtypedesc_ta = taxtypedesc_ta;
    }

    public String getTaxcollection_methodlogy() {
        return taxcollection_methodlogy;
    }

    public void setTaxcollection_methodlogy(String taxcollection_methodlogy) {
        this.taxcollection_methodlogy = taxcollection_methodlogy;
    }

    public String getInstallmenttypeid() {
        return installmenttypeid;
    }

    public void setInstallmenttypeid(String installmenttypeid) {
        this.installmenttypeid = installmenttypeid;
    }




    public String getTraders_license_type_id() {
        return traders_license_type_id;
    }

    public void setTraders_license_type_id(String traders_license_type_id) {
        this.traders_license_type_id = traders_license_type_id;
    }

    public String getTraders_license_type_name() {
        return traders_license_type_name;
    }

    public void setTraders_license_type_name(String traders_license_type_name) {
        this.traders_license_type_name = traders_license_type_name;
    }



    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }

    public String getLbcode() {
        return lbcode;
    }

    public void setLbcode(String lbcode) {
        this.lbcode = lbcode;
    }

    public String getWard_id() {
        return ward_id;
    }

    public void setWard_id(String ward_id) {
        this.ward_id = ward_id;
    }

    public String getWard_code() {
        return ward_code;
    }

    public void setWard_code(String ward_code) {
        this.ward_code = ward_code;
    }

    public String getWard_name_en() {
        return ward_name_en;
    }

    public void setWard_name_en(String ward_name_en) {
        this.ward_name_en = ward_name_en;
    }

    public String getWard_name_ta() {
        return ward_name_ta;
    }

    public void setWard_name_ta(String ward_name_ta) {
        this.ward_name_ta = ward_name_ta;
    }


    public String getStreetid() {
        return streetid;
    }

    public void setStreetid(String streetid) {
        this.streetid = streetid;
    }

    public String getStreet_code() {
        return street_code;
    }

    public void setStreet_code(String street_code) {
        this.street_code = street_code;
    }

    public String getStreet_name_en() {
        return street_name_en;
    }

    public void setStreet_name_en(String street_name_en) {
        this.street_name_en = street_name_en;
    }

    public String getStreet_name_ta() {
        return street_name_ta;
    }

    public void setStreet_name_ta(String street_name_ta) {
        this.street_name_ta = street_name_ta;
    }

    public String getBuilding_zone() {
        return building_zone;
    }

    public void setBuilding_zone(String building_zone) {
        this.building_zone = building_zone;
    }


   ////Motor Annaul and Generator List;

    public String getAnnual_id() {
        return annual_id;
    }

    public void setAnnual_id(String annual_id) {
        this.annual_id = annual_id;
    }

    public String getMotor_id() {
        return motor_id;
    }

    public void setMotor_id(String motor_id) {
        this.motor_id = motor_id;
    }

    public String getGenerator_id() {
        return generator_id;
    }

    public void setGenerator_id(String generator_id) {
        this.generator_id = generator_id;
    }

    public String getAnnual_sale() {
        return annual_sale;
    }

    public void setAnnual_sale(String annual_sale) {
        this.annual_sale = annual_sale;
    }

    public String getMotor_range() {
        return motor_range;
    }

    public void setMotor_range(String motor_range) {
        this.motor_range = motor_range;
    }

    public String getGenerator_range() {
        return generator_range;
    }

    public void setGenerator_range(String generator_range) {
        this.generator_range = generator_range;
    }
}
