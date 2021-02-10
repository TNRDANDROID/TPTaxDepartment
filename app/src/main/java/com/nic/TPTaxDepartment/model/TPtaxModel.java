package com.nic.TPTaxDepartment.model;

import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

/**
 * Created by AchanthiSundar on 01-11-2017.
 */

public class TPtaxModel  implements Serializable {

    private String distictCode;
    private String districtName;
    private String blockCode;
    private String Longitude;
    private String typeOfPhoto;
    private String imageRemark;
    private String LicenceValidity;
    private String imageAvailable;
    private String HabCode;
    private String Description;
    private String Latitude;
    private String Gender;
    private String HabitationName;
    private String pmayId;
    private String fatherName;
    private String SeccId;
    private String PvCode;
    private String LicenceType;
    private String blockName;

    private String miTankStructureId;
    private String miTankStructureName;
    private String miTankSurveyId;
    private String minorIrrigationType;
    private String nameOftheMITank;
    private String localName;
    private String area;
    private String miTankStructureDetailId;
    private String miTankStructureSerialId;
    private String miTankConditionId;
    private String miTankConditionName;
    private String miTankSkillLevel;
    public String pointType;
    public Integer tradecode;
    public String tradecodeId;
    public String TaxTypeId;
    public String TaxTypeName;
    public String TaxCollection;
    public String AssessmentId;
    public String CurrentStatus;


    public String traderName;
    public String traderCode;
    public String traderPayment;
    public String traders_typ;
    public String tradedesct;
    public String tradeImage;
    public String doorno;
    public String apfathername_ta;
    public String establishment_name_ta;
    public String licence_validity;
    public String traders_license_type_name;
    public String mobileno;
    public String tradersdetails_id;
    public String lb_sno;
    public String tradedetails_id;
    public String description_en;
    public String description_ta;
    public String traderate;
    public String traders_rate;
    public String trade_date;
    public String tradersperiod;
    public String tradedesce;
    public String licencefor;
    public String fin_year;
    public String traderstypee;
    public String demandtype;
    public String onlineapplicationno;
    public String email;
    public String licencetypeid;
    public String apgender;
    public String apgenderId;
    public String apage;
    public String apfathername_en;
    public String licenceno;
    public String statecode;
    public String dcode;
    public String wardId;
    public String wardname;
    public String streetId;
    public String streetname;
    public String lbcode;
    public String apname_ta;
    public String establishment_name_en;
    public String paymentdate;
    public String paymentStatus;
    public String request_id;
    public String field_visit_img_id;
    public Bitmap tradeBitmapImage;
    public byte[] imageByte;
    public String ownerStatus;
    public String motorStatus;
    public String generatorStatus;
    public String professtionlStatus;
    public String propertyStatus;
    public String motor_type_id;
    public String amount_range_id;
    public String generator_range_id;
    public String propertyTaxAssessmentNumber;

    public String getPropertyTaxAssessmentNumber() {
        return propertyTaxAssessmentNumber;
    }

    public TPtaxModel setPropertyTaxAssessmentNumber(String propertyTaxAssessmentNumber) {
        this.propertyTaxAssessmentNumber = propertyTaxAssessmentNumber;
        return this;
    }

    public String getMotor_type_id() {
        return motor_type_id;
    }

    public TPtaxModel setMotor_type_id(String motor_type_id) {
        this.motor_type_id = motor_type_id;
        return this;
    }

    public String getAmount_range_id() {
        return amount_range_id;
    }

    public TPtaxModel setAmount_range_id(String amount_range_id) {
        this.amount_range_id = amount_range_id;
        return this;
    }

    public String getGenerator_range_id() {
        return generator_range_id;
    }

    public TPtaxModel setGenerator_range_id(String generator_range_id) {
        this.generator_range_id = generator_range_id;
        return this;
    }

    public String getOwnerStatus() {
        return ownerStatus;
    }

    public TPtaxModel setOwnerStatus(String ownerStatus) {
        this.ownerStatus = ownerStatus;
        return this;
    }

    public String getMotorStatus() {
        return motorStatus;
    }

    public TPtaxModel setMotorStatus(String motorStatus) {
        this.motorStatus = motorStatus;
        return this;
    }

    public String getGeneratorStatus() {
        return generatorStatus;
    }

    public TPtaxModel setGeneratorStatus(String generatorStatus) {
        this.generatorStatus = generatorStatus;
        return this;
    }

    public String getProfesstionlStatus() {
        return professtionlStatus;
    }

    public TPtaxModel setProfesstionlStatus(String professtionlStatus) {
        this.professtionlStatus = professtionlStatus;
        return this;
    }

    public String getPropertyStatus() {
        return propertyStatus;
    }

    public TPtaxModel setPropertyStatus(String propertyStatus) {
        this.propertyStatus = propertyStatus;
        return this;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public TPtaxModel setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
        return this;
    }

    public Bitmap getTradeBitmapImage() {
        return tradeBitmapImage;
    }

    public TPtaxModel setTradeBitmapImage(Bitmap tradeBitmapImage) {
        this.tradeBitmapImage = tradeBitmapImage;
        return this;
    }

    public String getField_visit_img_id() {
        return field_visit_img_id;
    }

    public TPtaxModel setField_visit_img_id(String field_visit_img_id) {
        this.field_visit_img_id = field_visit_img_id;
        return this;
    }

    public String getRequest_id() {
        return request_id;
    }

    public TPtaxModel setRequest_id(String request_id) {
        this.request_id = request_id;
        return this;
    }

    public String getApgenderId() {
        return apgenderId;
    }

    public void setApgenderId(String apgenderId) {
        this.apgenderId = apgenderId;
    }

    public String getWardname() {
        return wardname;
    }

    public void setWardname(String wardname) {
        this.wardname = wardname;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getTradecodeId() {
        return tradecodeId;
    }

    public void setTradecodeId(String tradecodeId) {
        this.tradecodeId = tradecodeId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTradeImage() {
        return tradeImage;
    }

    public void setTradeImage(String tradeImage) {
        this.tradeImage = tradeImage;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getStreetId() {
        return streetId;
    }

    public void setStreetId(String streetId) {
        this.streetId = streetId;
    }

    public String getDcode() {
        return dcode;
    }

    public void setDcode(String dcode) {
        this.dcode = dcode;
    }
    public String getTradersdetails_id() {
        return tradersdetails_id;
    }

    public void setTradersdetails_id(String tradersdetails_id) {
        this.tradersdetails_id = tradersdetails_id;
    }

    public String getLb_sno() {
        return lb_sno;
    }

    public void setLb_sno(String lb_sno) {
        this.lb_sno = lb_sno;
    }

    public String getTradedetails_id() {
        return tradedetails_id;
    }

    public void setTradedetails_id(String tradedetails_id) {
        this.tradedetails_id = tradedetails_id;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getDescription_ta() {
        return description_ta;
    }

    public void setDescription_ta(String description_ta) {
        this.description_ta = description_ta;
    }

    public String getTraderate() {
        return traderate;
    }

    public void setTraderate(String traderate) {
        this.traderate = traderate;
    }

    public String getTraders_rate() {
        return traders_rate;
    }

    public void setTraders_rate(String traders_rate) {
        this.traders_rate = traders_rate;
    }

    public String getTrade_date() {
        return trade_date;
    }

    public void setTrade_date(String trade_date) {
        this.trade_date = trade_date;
    }

    public String getTradersperiod() {
        return tradersperiod;
    }

    public void setTradersperiod(String tradersperiod) {
        this.tradersperiod = tradersperiod;
    }

    public String getTradedesce() {
        return tradedesce;
    }

    public void setTradedesce(String tradedesce) {
        this.tradedesce = tradedesce;
    }

    public String getLicencefor() {
        return licencefor;
    }

    public void setLicencefor(String licencefor) {
        this.licencefor = licencefor;
    }

    public String getFin_year() {
        return fin_year;
    }

    public void setFin_year(String fin_year) {
        this.fin_year = fin_year;
    }

    public String getTraderstypee() {
        return traderstypee;
    }

    public void setTraderstypee(String traderstypee) {
        this.traderstypee = traderstypee;
    }

    public String getDemandtype() {
        return demandtype;
    }

    public void setDemandtype(String demandtype) {
        this.demandtype = demandtype;
    }

    public String getOnlineapplicationno() {
        return onlineapplicationno;
    }

    public void setOnlineapplicationno(String onlineapplicationno) {
        this.onlineapplicationno = onlineapplicationno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLicencetypeid() {
        return licencetypeid;
    }

    public void setLicencetypeid(String licencetypeid) {
        this.licencetypeid = licencetypeid;
    }

    public String getApgender() {
        return apgender;
    }

    public void setApgender(String apgender) {
        this.apgender = apgender;
    }

    public String getApage() {
        return apage;
    }

    public void setApage(String apage) {
        this.apage = apage;
    }

    public String getApfathername_en() {
        return apfathername_en;
    }

    public void setApfathername_en(String apfathername_en) {
        this.apfathername_en = apfathername_en;
    }

    public String getLicenceno() {
        return licenceno;
    }

    public void setLicenceno(String licenceno) {
        this.licenceno = licenceno;
    }

    public String getStatecode() {
        return statecode;
    }

    public void setStatecode(String statecode) {
        this.statecode = statecode;
    }

    public String getLbcode() {
        return lbcode;
    }

    public void setLbcode(String lbcode) {
        this.lbcode = lbcode;
    }

    public String getApname_ta() {
        return apname_ta;
    }

    public void setApname_ta(String apname_ta) {
        this.apname_ta = apname_ta;
    }

    public String getEstablishment_name_en() {
        return establishment_name_en;
    }

    public void setEstablishment_name_en(String establishment_name_en) {
        this.establishment_name_en = establishment_name_en;
    }




    public String getTaxTypeName() {
        return TaxTypeName;
    }

    public void setTaxTypeName(String taxTypeName) {
        TaxTypeName = taxTypeName;
    }

    public String getTaxCollection() {
        return TaxCollection;
    }

    public void setTaxCollection(String taxCollection) {
        TaxCollection = taxCollection;
    }



    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }



    public String getTraders_typ() {
        return traders_typ;
    }

    public void setTraders_typ(String traders_typ) {
        this.traders_typ = traders_typ;
    }

    public String getTradedesct() {
        return tradedesct;
    }

    public void setTradedesct(String tradedesct) {
        this.tradedesct = tradedesct;
    }

    public String getDoorno() {
        return doorno;
    }

    public void setDoorno(String doorno) {
        this.doorno = doorno;
    }

    public String getApfathername_ta() {
        return apfathername_ta;
    }

    public void setApfathername_ta(String apfathername_ta) {
        this.apfathername_ta = apfathername_ta;
    }

    public String getEstablishment_name_ta() {
        return establishment_name_ta;
    }

    public void setEstablishment_name_ta(String establishment_name_ta) {
        this.establishment_name_ta = establishment_name_ta;
    }

    public String getLicence_validity() {
        return licence_validity;
    }

    public void setLicence_validity(String licence_validity) {
        this.licence_validity = licence_validity;
    }

    public String getTraders_license_type_name() {
        return traders_license_type_name;
    }

    public void setTraders_license_type_name(String traders_license_type_name) {
        this.traders_license_type_name = traders_license_type_name;
    }




    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    public String getTraderCode() {
        return traderCode;
    }

    public void setTraderCode(String traderCode) {
        this.traderCode = traderCode;
    }

    public String getTraderPayment() {
        return traderPayment;
    }

    public void setTraderPayment(String traderPayment) {
        this.traderPayment = traderPayment;
    }



    public String getMiTankStructureId() {
        return miTankStructureId;
    }

    public void setMiTankStructureId(String miTankStructureId) {
        this.miTankStructureId = miTankStructureId;
    }

    public String getMiTankStructureName() {
        return miTankStructureName;
    }

    public void setMiTankStructureName(String miTankStructureName) {
        this.miTankStructureName = miTankStructureName;
    }

    public String getMiTankSurveyId() {
        return miTankSurveyId;
    }

    public void setMiTankSurveyId(String miTankSurveyId) {
        this.miTankSurveyId = miTankSurveyId;
    }

    public String getMinorIrrigationType() {
        return minorIrrigationType;
    }

    public void setMinorIrrigationType(String minorIrrigationType) {
        this.minorIrrigationType = minorIrrigationType;
    }

    public String getNameOftheMITank() {
        return nameOftheMITank;
    }

    public void setNameOftheMITank(String nameOftheMITank) {
        this.nameOftheMITank = nameOftheMITank;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMiTankStructureDetailId() {
        return miTankStructureDetailId;
    }

    public void setMiTankStructureDetailId(String miTankStructureDetailId) {
        this.miTankStructureDetailId = miTankStructureDetailId;
    }

    public String getMiTankStructureSerialId() {
        return miTankStructureSerialId;
    }

    public void setMiTankStructureSerialId(String miTankStructureSerialId) {
        this.miTankStructureSerialId = miTankStructureSerialId;
    }

    public String getMiTankConditionId() {
        return miTankConditionId;
    }

    public void setMiTankConditionId(String miTankConditionId) {
        this.miTankConditionId = miTankConditionId;
    }

    public String getMiTankConditionName() {
        return miTankConditionName;
    }

    public void setMiTankConditionName(String miTankConditionName) {
        this.miTankConditionName = miTankConditionName;
    }

    public String getMiTankSkillLevel() {
        return miTankSkillLevel;
    }

    public void setMiTankSkillLevel(String miTankSkillLevel) {
        this.miTankSkillLevel = miTankSkillLevel;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getPmayId() {
        return pmayId;
    }

    public void setPmayId(String pmayId) {
        this.pmayId = pmayId;
    }


    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getHabitationName() {
        return HabitationName;
    }

    public void setHabitationName(String habitationName) {
        HabitationName = habitationName;
    }

    public String getSeccId() {
        return SeccId;
    }

    public void setSeccId(String seccId) {
        SeccId = seccId;
    }

    public String getHabCode() {
        return HabCode;
    }

    public void setHabCode(String habCode) {
        HabCode = habCode;
    }

    public String getTypeOfPhoto() {
        return typeOfPhoto;
    }

    public void setTypeOfPhoto(String typeOfPhoto) {
        this.typeOfPhoto = typeOfPhoto;
    }

    public String getImageAvailable() {
        return imageAvailable;
    }

    public void setImageAvailable(String imageAvailable) {
        this.imageAvailable = imageAvailable;
    }


    public String getImageRemark() {
        return imageRemark;
    }

    public void setImageRemark(String imageRemark) {
        this.imageRemark = imageRemark;
    }

    public String getLicenceValidity() {
        return LicenceValidity;
    }

    public void setLicenceValidity(String dateTime) {
        this.LicenceValidity = dateTime;
    }


    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }


    public String getLicenceType() {
        return LicenceType;
    }

    public void setLicenceType(String name) {
        LicenceType = name;
    }


    public String getDistictCode() {
        return distictCode;
    }

    public void setDistictCode(String distictCode) {
        this.distictCode = distictCode;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    private Bitmap Image;

    public String getPvCode() {
        return PvCode;
    }

    public void setPvCode(String pvCode) {
        this.PvCode = pvCode;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public Integer getTradecode() {
        return tradecode;
    }

    public void setTradecode(Integer pointType) {
        this.tradecode = tradecode;
    }

    public String getTaxTypeId() {
        return TaxTypeId;
    }

    public void setTaxTypeId(String taxtypeid) {

        TaxTypeId = taxtypeid;
    }

    public String getAssessmentId() {
        return AssessmentId;
    }

    public void setAssessmentId(String assessmentId) {

        AssessmentId = assessmentId;
    }

    public String getCurrentStatus() {
        return CurrentStatus;
    }

    public void setCurrentStatus(String currentStatus) {

        CurrentStatus = currentStatus;
    }

}