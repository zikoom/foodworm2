package com.example.foodworm2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link frag_statistics.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link frag_statistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_statistics extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public int nUser;
    public int nPurchase;
    public int nUsed;

    public int nSoldkor;
    public int nSoldjpa;
    public int nSoldusa;

    public int nUsedkor;
    public int nUsedjpa;
    public int nUsedusa;


    public TextView tv_nUser;
    public TextView tv_nPurchase;
    public TextView tv_nUsed;

    public TextView tv_nSoldkor;
    public TextView tv_nSoldjpa;
    public TextView tv_nSoldusa;

    public TextView tv_nUsedkor;
    public TextView tv_nUsedjpa;
    public TextView tv_nUsedusa;

    DatabaseReference mDatabase;

    public frag_statistics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment frag_statistics.
     */
    // TODO: Rename and change types and number of parameters
    public static frag_statistics newInstance(String param1, String param2) {
        frag_statistics fragment = new frag_statistics();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_frag_statistics,container, false);
        // Inflate the layout for this fragment

        tv_nUser = view.findViewById(R.id.tv_nUser);
        tv_nPurchase = view.findViewById(R.id.tv_nPurchase);
        tv_nUsed = view.findViewById(R.id.tv_nUsed);

        tv_nSoldkor = view.findViewById(R.id.tv_nSoldkor);
        tv_nSoldjpa = view.findViewById(R.id.tv_nSoldjpa);
        tv_nSoldusa = view.findViewById(R.id.tv_nSoldusa);

        tv_nUsedkor = view.findViewById(R.id.tv_nUsedkor);
        tv_nUsedjpa = view.findViewById(R.id.tv_nUsedjpa);
        tv_nUsedusa = view.findViewById(R.id.tv_nUsedusa);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nUser = dataSnapshot.child("NumofUser").getValue(Integer.class);
                nPurchase = dataSnapshot.child("NumofPurchase").getValue(Integer.class);
                nUsed = dataSnapshot.child("NumofUsing").getValue(Integer.class);

                nSoldkor =dataSnapshot.child("Soldticket").child("kor").getValue(Integer.class);
                nSoldjpa =dataSnapshot.child("Soldticket").child("jpa").getValue(Integer.class);
                nSoldusa =dataSnapshot.child("Soldticket").child("usa").getValue(Integer.class);

                nUsedkor =dataSnapshot.child("Usedticket").child("kor").getValue(Integer.class);
                nUsedjpa =dataSnapshot.child("Usedticket").child("jpa").getValue(Integer.class);
                nUsedusa =dataSnapshot.child("Usedticket").child("usa").getValue(Integer.class);

                tv_nUser.setText("총 회원수: "+nUser);
                tv_nPurchase.setText("누적 판매 횟수: "+nPurchase);
                tv_nUsed.setText("누적 식권사용 횟수: "+ nUsed);

                tv_nSoldkor.setText("누적 한식 식권 판매 개수: "+nSoldkor);
                tv_nSoldjpa.setText("누적 일식 식권 판매 개수: "+nSoldjpa);
                tv_nSoldusa.setText("누적 양식 식권 판매 개수: "+nSoldusa);

                tv_nUsedkor.setText("누적 한식 식권 사용 개수: "+ nUsedkor);
                tv_nUsedjpa.setText("누적 일식 식권 사용 개수: "+ nUsedjpa);
                tv_nUsedusa.setText("누적 양식 식권 사용 개수: "+ nUsedusa);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return view;
       // return inflater.inflate(R.layout.fragment_frag_statistics, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
