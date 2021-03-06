package com.nic.TPTaxDepartment.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.LocaleList;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.library.BuildConfig;

import com.nic.TPTaxDepartment.Application.NICApplication;

import com.nic.TPTaxDepartment.Interface.DateInterface;
import com.nic.TPTaxDepartment.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Utils {

    private static final String SHARED_PREFERENCE_UTILS = "Nimble";
    private static final int SECONDS_IN_A_MINUTE = 60;
    private static final int MINUTES_IN_AN_HOUR = 60;
    private static SharedPreferences sharedPreferences;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static String CIPHER_NAME = "AES/CBC/NoPadding";
    private static String CIPHER_NAME2 = "AES/CBC/PKCS5PADDING";
    private static int CIPHER_KEY_LEN = 16; //128 bits
    private static boolean flag = false; //128 bits
    private static String fromToDate = "";
    private static  boolean date_flag =true;
    private static String fromDate,toDate;

    static DateInterface dateInterface  ;

    private static void initializeSharedPreference() {
        sharedPreferences = NICApplication.getGlobalContext()
                .getSharedPreferences(SHARED_PREFERENCE_UTILS,
                        Context.MODE_PRIVATE);
    }

    public static boolean isOnline() {
        boolean status = false;
        ConnectivityManager cm = (ConnectivityManager) NICApplication.getGlobalContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = true;
            }
        } else {
            status = false;
        }

        return status;
    }

    public static Boolean isValidEmail(String email) {
        Boolean status = true;
        String mail[] = email.split(",");
        for (int i = 0; i < mail.length; i++) {
            if (!validateMail(mail[i])) {
                return false;
            }
        }
        return status;
    }
    public static String getSHA512(String input){

        String toReturn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            toReturn = String.format("%0128x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toReturn;
    }

    public static boolean validateMail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(emailPattern)) {
            return true;
        } else {
            return false;
        }
    }

/*
    public static boolean isEmailValid(String email) {

        boolean flag;
        String expression = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,64}" + "(" + "\\."
                + "[a-zA-Z0-9][a-zA-Z0-9\\-]{1,25}" + ")+";

        CharSequence inputStr = email.trim();
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        flag = matcher.matches();
        return flag;

    }
*/
    public static boolean isEmailValid(String email) {

        boolean flag = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return flag;

    }
/*
    public static boolean isEmailValid(String email){
        boolean flag;
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "(gmail\\.com|yahoo\\.com|nic\\.in|hotmail\\.com)$";
        CharSequence inputStr = email.trim();
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        flag = matcher.matches();
        return flag;
    }
*/


//    public static void showAlert(Context context, String message) {
//        try {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            @SuppressLint("InflateParams") View dialogView = inflater.inflate(R.layout.alert_dialog,null);
//            final AlertDialog alertDialog = builder.create();
//            alertDialog.setView(dialogView, 0, 0, 0, 0);
//            alertDialog.setCancelable(false);
//            alertDialog.show();
//
//            MyCustomTextView tv_message = (MyCustomTextView) dialogView.findViewById(R.id.tv_message);
//            tv_message.setText(message);
//
//            Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
//            btnOk.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertDialog.dismiss();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }



    public static void showAlert(Activity activity, String msg){
        try {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog);

       TextView text = (TextView) dialog.findViewById(R.id.tv_message);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  void showToast(Activity context,String msg){

        LayoutInflater inflater =context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout,null,false);


        TextView text = (TextView) layout.findViewById(R.id.tv_message);
        //TextView btn_ok = (TextView) layout.findViewById(R.id.btn_ok);
        //btn_ok.setVisibility(View.GONE);
        text.setText(msg);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
/*
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toast.cancel();
            }
        });
*/

    }
    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat(
                "MM/dd/yyyy HH:mm:ss");
        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }




    public static double getScreenInch(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / (double) dens;
        double hi = (double) height / (double) dens;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }



    public static boolean isValidMobile(String phone2) {
        boolean check= false;
        boolean startDigitCheck = false;
        boolean sameDigitCheck= false;
        String[] startDigit=new String[] {"0","1","2","3","4","5"};
        String[] sameDigit=new String[] {"6666666666","7777777777","8888888888","9999999999"};
        for(int i=0;i<startDigit.length;i++){
            if(phone2.startsWith(startDigit[i])){
                startDigitCheck=false;
                return false;
            }else {
                startDigitCheck=true;
            }
        }

        if(startDigitCheck){
            for(int i=0;i<sameDigit.length;i++){
                if(phone2.equals(sameDigit[i])){
                    sameDigitCheck=false;
                    return false;
                }else {
                    sameDigitCheck=true;
                }
            }

        }else {
              return  false;
        }
        if(sameDigitCheck){
            check = phone2.length() == 10;
        }else {
            return  false;
        }
        if(check){
            return check;
        }else {
            return  false;
        }

    }

    public static String emailOrNumberValues(List<String> values) {
        String listValues = "";
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        for (String number : values) {
            if (values.size() > 1) {
                builder.append(prefix);
                prefix = ",";
                builder.append(number);
            } else {
                builder.append(number);
            }
        }
        listValues = builder.toString();
        return listValues;
    }



    public static String enableSwitchValues(SwitchCompat enableSwitch) {
        String switchValue = "";
        if (enableSwitch.isChecked()) {
            switchValue = "on";
        } else {
            switchValue = "";
        }
        return switchValue;
    }




    //Version name
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        return version;
    }




    private static Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private static File saveBitmap(Bitmap bm, String fileName) {
        final String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/IrisReport";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        File oldFile = new File(new File(path), fileName);
        if (oldFile.exists())
            oldFile.delete();
        File file = new File(dir, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void captureMapScreen(final Activity activity, final View mView, ImageView imageView) {
        Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mView.draw(canvas);
        imageView.setImageDrawable(null);
        shareScreenShot(activity, bitmap);

    }

    public static void shareScreenShot(Activity activity, View view) {
        Bitmap bm = screenShot(view);
        shareScreenShot(activity, bm);
    }

    public static void shareScreenShot(Activity activity, Bitmap bm) {
        File file = saveBitmap(bm, "IrisReport.png");   /*change Uri.fromfile to Fileprovider.getUriForFile becoz the old method is not Supported for Android 8 so permission must be added in manifest file*/
//        Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));
        Uri uri = FileProvider.getUriForFile(activity,
                BuildConfig.APPLICATION_ID + ".provider",
                new File(file.getAbsolutePath()));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivity(Intent.createChooser(shareIntent, "share via"));
    }


    public static String randomChar() {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);
        Log.d("rand", sb.toString());
        return sb.toString();
    }

    public static String getSHA(String UserPassWord) {
        try {

            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(UserPassWord.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);

            return null;
        }
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String encrypt(String key, String iv, String data) {

        try {
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(fixKey(key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_NAME2);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

            byte[] encryptedData = cipher.doFinal((data.getBytes()));

            String encryptedDataInBase64 = android.util.Base64.encodeToString(encryptedData, 0);
            String ivInBase64 = android.util.Base64.encodeToString(iv.getBytes("UTF-8"), 0);

            return encryptedDataInBase64 + ":" + ivInBase64;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


    }

    private static String fixKey(String key) {

        if (key.length() < CIPHER_KEY_LEN) {
            int numPad = CIPHER_KEY_LEN - key.length();

            for (int i = 0; i < numPad; i++) {
                key += "0"; //0 pad to len 16 bytes
            }

            return key;

        }

        if (key.length() > CIPHER_KEY_LEN) {
            return key.substring(0, CIPHER_KEY_LEN); //truncate to 16 bytes
        }

        return key;
    }

    /**
     * Decrypt data using AES Cipher (CBC) with 128 bit key
     *
     * @param key  - key to use should be 16 bytes long (128 bits)
     * @param data - encrypted data with iv at the end separate by :
     * @return decrypted data string
     */


    public static String decrypt(String key, String data) {

        try {
            String[] parts = data.split(":");

            IvParameterSpec iv = new IvParameterSpec(android.util.Base64.decode(parts[1], 1));
            // System.out.println(fixKey(iv));
            SecretKeySpec secretKey = new SecretKeySpec(fixKey(key).getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            byte[] decodedEncryptedData = android.util.Base64.decode(parts[0], 1);

            byte[] original = cipher.doFinal(decodedEncryptedData);

            return new String(original);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static String notNullInt(String text){
        if(text.equals("")){
            return "0";
        }
        else if(text==null){
            return "0";

        }
        else if(text=="null"){
            return "0";
        }
        else {
            return text;
        }


    }

    public static String NotNullString(String text){
        if(text.equals("")){
            return "";
        }
          else if(text.equals("null")){
            return "";

        }
        else if(text==null){
            return "";

        }
          else {
              return text;
        }
    }

    public static void clearApplicationData(Context context) {
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static  void setLanguage(EditText view, String lang, String country){
        if (Build.VERSION.SDK_INT >= 24) {
            view.setImeHintLocales(new LocaleList(new Locale(lang, country)));

        }
    }
    public static void showDatePickerDialog(Context context) {
        fromToDate=" ";
        dateInterface= (DateInterface) context;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.date_picker_layout, null);
        final AlertDialog alertDialog = builder.create();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(alertDialog.getWindow().getAttributes());
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setAttributes(lp);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setView(dialogView, 0, 0, 0, 0);
        alertDialog.setCancelable(true);
        alertDialog.show();

        DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        TextView from = (TextView) dialogView.findViewById(R.id.from);
        TextView to = (TextView) dialogView.findViewById(R.id.to);
        TextView cancel = (TextView) dialogView.findViewById(R.id.cancel);
        TextView ok = (TextView) dialogView.findViewById(R.id.ok);
        TextView fromDateValue = (TextView) dialogView.findViewById(R.id.fromDateValue);
        TextView toDateValue = (TextView) dialogView.findViewById(R.id.toDateValue);
        LinearLayout t_layout = (LinearLayout) dialogView.findViewById(R.id.t_layout);
        LinearLayout f_layout = (LinearLayout) dialogView.findViewById(R.id.f_layout);
        RelativeLayout button_layout = (RelativeLayout) dialogView.findViewById(R.id.button_layout);

        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Poppins-Medium.ttf");
        from.setTypeface(custom_font);
        to.setTypeface(custom_font);
        fromDateValue.setTypeface(custom_font);
        toDateValue.setTypeface(custom_font);
        cancel.setTypeface(custom_font);
        ok.setTypeface(custom_font);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        date_flag=true;
        f_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        fromDateValue.setTextColor(context.getResources().getColor(R.color.white));
        from.setTextColor(context.getResources().getColor(R.color.white));
        toDateValue.setTextColor(context.getResources().getColor(R.color.grey2));
        to.setTextColor(context.getResources().getColor(R.color.grey2));
        t_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));

        datePicker.setMaxDate(new Date().getTime());
        datePicker.setMinDate(0);

        fromDate= format.format(c.getTime());
        fromDateValue.setText(fromDate);
        toDate= format.format(c.getTime());
        toDateValue.setText(toDate);
        datePicker.setMaxDate(new Date().getTime());
        datePicker.setMinDate(0);
// set current date into datepicker
        datePicker.init(year, month, day, null);
        f_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_flag=true;
                f_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                fromDateValue.setTextColor(context.getResources().getColor(R.color.white));
                from.setTextColor(context.getResources().getColor(R.color.white));
                toDateValue.setTextColor(context.getResources().getColor(R.color.grey2));
                to.setTextColor(context.getResources().getColor(R.color.grey2));
                t_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));

                datePicker.setMaxDate(new Date().getTime());
                datePicker.setMinDate(0);
            }
        });
        t_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date_flag=false;
                f_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                fromDateValue.setTextColor(context.getResources().getColor(R.color.grey2));
                from.setTextColor(context.getResources().getColor(R.color.grey2));
                toDateValue.setTextColor(context.getResources().getColor(R.color.white));
                to.setTextColor(context.getResources().getColor(R.color.white));
                t_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                Date date = null;
                try {
                    date = format.parse(fromDate);
                    datePicker.setMinDate(date.getTime());
                    datePicker.setMaxDate(new Date().getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);

                if(date_flag){

                    fromDate=datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                    fromDateValue.setText(updateLabel(fromDate));

                    try {
                        Date strDate = format.parse(fromDate);
                        Date endDate = format.parse(toDate);
                        if (!endDate.after(strDate)) {
                            toDate=datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                            toDateValue.setText(updateLabel(toDate));
                        }else {

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    date_flag=false;
                    f_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    fromDateValue.setTextColor(context.getResources().getColor(R.color.grey2));
                    from.setTextColor(context.getResources().getColor(R.color.grey2));
                    toDateValue.setTextColor(context.getResources().getColor(R.color.white));
                    to.setTextColor(context.getResources().getColor(R.color.white));
                    t_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    Date date = null;
                    try {
                        date = format.parse(fromDate);
                        datePicker.setMinDate(date.getTime());
                        datePicker.setMaxDate(new Date().getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else {



                    toDate=datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                    toDateValue.setText(updateLabel(toDate));

                    /*date_flag=true;
                    f_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    fromDateValue.setTextColor(context.getResources().getColor(R.color.white));
                    from.setTextColor(context.getResources().getColor(R.color.white));
                    toDateValue.setTextColor(context.getResources().getColor(R.color.grey2));
                    to.setTextColor(context.getResources().getColor(R.color.grey2));
                    t_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));

                    datePicker.setMaxDate(new Date().getTime());
                    datePicker.setMinDate(0);*/
                }
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    fromDate=datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                    fromDateValue.setText(updateLabel(fromDate));

                    try {
                        Date strDate = format.parse(fromDate);
                        Date endDate = format.parse(toDate);
                        if (!endDate.after(strDate)) {
                            toDate=datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                            toDateValue.setText(updateLabel(toDate));
                        }else {

                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    date_flag=false;
                    f_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    fromDateValue.setTextColor(context.getResources().getColor(R.color.grey2));
                    from.setTextColor(context.getResources().getColor(R.color.grey2));
                    toDateValue.setTextColor(context.getResources().getColor(R.color.white));
                    to.setTextColor(context.getResources().getColor(R.color.white));
                    t_layout.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    Date date = null;
                    try {
                        date = format.parse(fromDate);
                        datePicker.setMinDate(date.getTime());
                        datePicker.setMaxDate(new Date().getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }else {
                    fromToDate=fromDateValue.getText().toString()+":"+toDateValue.getText().toString();
                    dateInterface.getDate(fromToDate);
                    alertDialog.dismiss();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromToDate=" ";
                alertDialog.dismiss();
            }
        });

    }

    private static String updateLabel(String dateStr) {
        String myFormat="";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1 = format.parse(dateStr);
            System.out.println(date1);
            String dateTime = format.format(date1);
            System.out.println("Current Date Time : " + dateTime);
            myFormat = dateTime; //In which you need put here
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myFormat;
    }
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String bitmapToString(Bitmap bitmap1) {
        byte[] imageInByte = new byte[0];
        String image_str = "";
        Bitmap bitmap = bitmap1;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        imageInByte = baos.toByteArray();
        image_str = Base64.encodeToString(imageInByte, Base64.DEFAULT);
        return image_str;
    }
    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Bitmap converetdImage = Utils.getResizedBitmap(bitmap, 500);
            return converetdImage;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

}
