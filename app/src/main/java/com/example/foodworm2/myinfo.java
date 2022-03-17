package com.example.foodworm2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link myinfo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link myinfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myinfo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button btn_logout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //유저의 User객체값을 저장할 변수
    public User myself;
    //유저의 아이디를 저장할 변수
    public String UserId;
    public FirebaseAuth mAuth;
    public DatabaseReference mDatabase;

    public TextView tv_id;
    public TextView tv_curticket;
    public TextView tv_usedticket;


    public myinfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myinfo.
     */
    // TODO: Rename and change types and number of parameters
    public static myinfo newInstance(String param1, String param2) {
        myinfo fragment = new myinfo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //사용자 정보 가져오기
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UserId = currentUser.getEmail();
        Log.d("myself_init",UserId);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myinfo,container,false);

        tv_id = view.findViewById(R.id.tv_mp_id);
        tv_curticket = view.findViewById(R.id.tv_mp_curticket);
        tv_usedticket = view.findViewById(R.id.tv_mp_usedticket);

        Query myQ = mDatabase.child("user").orderByChild("id").equalTo(UserId);
        myQ.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("myself","In DataChange");
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    myself = ds.getValue(User.class);
                    //유저 객체 받아오기 완료
                    Log.d("myself", myself.id);
                    tv_id.setText("ID" + myself.id);
                    tv_curticket.setText("현재 보유한식권    한식: "+myself.kor+" 일식: "+myself.jpa+ " 양식: "+myself.usa);
                    tv_usedticket.setText("사용한 식권    한식: "+myself.numofusing_kor+" 일식: "+myself.numofusing_jpa+ " 양식: "+myself.numofusing_usa);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
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
}
