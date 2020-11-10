package com.nic.TPTaxDepartment.helper;

import com.nic.TPTaxDepartment.model.TranslatedText;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by almaz on 23.04.17.
 */

public interface APIHelper {

    @POST("api/v1.5/tr.json/translate")
    Call<TranslatedText> getTranslation(@Query("key") String APIKey, @Query("text") String textToTranslate, @Query("lang") String lang);
}