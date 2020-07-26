package com.nic.VPTax.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.nic.VPTax.R;
import com.nic.VPTax.Support.ProgressHUD;
import com.nic.VPTax.activity.test;
import com.nic.VPTax.dataBase.DBHelper;
import com.nic.VPTax.dataBase.dbData;
import com.nic.VPTax.databinding.DashboardBinding;
import com.nic.VPTax.model.VPtaxModel;
import com.nic.VPTax.session.PrefManager;
import com.nic.VPTax.windowpreferences.WindowPreferencesManager;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<String> RuralUrbanList = new ArrayList<>();
    private DashboardBinding dashboardBinding;
    private List<VPtaxModel> District = new ArrayList<>();
    private List<VPtaxModel> Block = new ArrayList<>();
    public com.nic.VPTax.dataBase.dbData dbData = new dbData(getActivity());
    private SQLiteDatabase db;
    public static DBHelper dbHelper;
    Animation smalltobig, stb2;
    final Handler handler = new Handler();
    private PrefManager prefManager;
    String pref_Block, pref_district, pref_Village;
    boolean isPanchayatUnion, isMunicipality, isTownPanchayat, isCorporation;
    private ProgressHUD progressHUD;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dashboardBinding = DataBindingUtil.inflate(
                inflater, R.layout.dashboard, container, false);
        View view = dashboardBinding.getRoot();
        //here data must be an instance of the class MarsDataProvider
        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(getActivity());
        windowPreferencesManager.applyEdgeToEdgePreference(getActivity().getWindow());

        try {
            dbHelper = new DBHelper(getActivity());
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        smalltobig = AnimationUtils.loadAnimation(getActivity(), R.anim.smalltobig);
        stb2 = AnimationUtils.loadAnimation(getActivity(), R.anim.stb2);
        dashboardBinding.homeImg.setVisibility(View.GONE);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dashboardBinding.homeImg.setVisibility(View.VISIBLE);
                dashboardBinding.homeImg.startAnimation(smalltobig);
            }
        }, 500);

        prefManager = new PrefManager(getActivity());
dashboardBinding.setFragment(this);
        dashboardBinding.districtUserLayout.setTranslationX(800);
        dashboardBinding.blockUserLayout.setTranslationX(800);
        dashboardBinding.voteprogresscard.setTranslationX(800);
        dashboardBinding.attendanecard.setTranslationX(800);
        dashboardBinding.cameracard.setTranslationX(800);
        dashboardBinding.votecountcard.setTranslationX(800);
        dashboardBinding.viewPollingStationImage.setTranslationX(800);


        dashboardBinding.districtUserLayout.setAlpha(0);
        dashboardBinding.blockUserLayout.setAlpha(0);
        dashboardBinding.voteprogresscard.setAlpha(0);
        dashboardBinding.attendanecard.setAlpha(0);
        dashboardBinding.cameracard.setAlpha(0);
        dashboardBinding.votecountcard.setAlpha(0);
        dashboardBinding.viewPollingStationImage.setAlpha(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dashboardBinding.districtUserLayout.animate().translationX(0).alpha(1).setDuration(1200).setStartDelay(400).start();
                dashboardBinding.blockUserLayout.animate().translationX(0).alpha(1).setDuration(1300).setStartDelay(600).start();
                dashboardBinding.voteprogresscard.animate().translationX(0).alpha(1).setDuration(1400).setStartDelay(800).start();
                dashboardBinding.attendanecard.animate().translationX(0).alpha(1).setDuration(1500).setStartDelay(1000).start();
                dashboardBinding.cameracard.animate().translationX(0).alpha(1).setDuration(1600).setStartDelay(1200).start();
                dashboardBinding.votecountcard.animate().translationX(0).alpha(1).setDuration(1700).setStartDelay(1400).start();
                dashboardBinding.viewPollingStationImage.animate().translationX(0).alpha(1).setDuration(1800).setStartDelay(1600).start();
            }
        }, 1000);

        Animation anim = new ScaleAnimation(
                0.95f, 1f, // Start and end values for the X axis scaling
                0.95f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        anim.setRepeatMode(Animation.INFINITE);
        anim.setRepeatCount(Animation.INFINITE);


        dashboardBinding.imageVotingProgress.startAnimation(anim);
        dashboardBinding.imageAttendance.startAnimation(anim);
        dashboardBinding.imageCamera.startAnimation(anim);
        dashboardBinding.imageVotingCount.startAnimation(anim);
        dashboardBinding.imageVotingCount.startAnimation(anim);
        dashboardBinding.pollingStationImage.startAnimation(anim);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        syncButtonVisibility();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void Testeee(){
        Intent intent = new Intent(getActivity(), test.class);
        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.fleft, R.anim.fhelper);
    }
}
