package com.example.foodworm2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link usehistory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link usehistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class usehistory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //리스트뷰 변수
    ListView listView;

    //사용 기록을 담을 컬렉션
    ArrayList<UsingHistroy> UList;
    //리스트뷰에 올릴 어레이리스트를 만든다
    ArrayList<String> arrayList;
    ArrayList<String> arrayList_reverse;

    //유저의 User객체값을 저장할 변수
    private User myself;
    //유저의 아이디를 저장할 변수
    private String UserId;
    private FirebaseAuth mAuth;
    //DataBase리퍼런스
    DatabaseReference mDatabase;

    //정보를 긁어올 query변수
    Query mQ;

    public usehistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment usehistory.
     */
    // TODO: Rename and change types and number of parameters
    public static usehistory newInstance(String param1, String param2) {
        usehistory fragment = new usehistory();
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
        Log.d("유저아이디 받기(buyhistory)",UserId);

        arrayList = new ArrayList<>();
        arrayList_reverse = new ArrayList<>();
        UList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usehistory,container,false);
        listView = (ListView) view.findViewById(R.id.lv_use);

        Spinner spinner_sort = (Spinner) view.findViewById(R.id.usehistory_spinner);
        ArrayAdapter sortAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinnerArray2, android.R.layout.simple_spinner_dropdown_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sort.setAdapter(sortAdapter);

        //사용자의 식권사용내역 받기
        //UList에 받는다.
        mQ = mDatabase.child("UsingHistory").orderByChild("id").equalTo(UserId);
        Log.d("쿼리변수 사용내역 받음", "진행");
        //쿼리변수 리스너 장착
        mQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    UsingHistroy data = ds.getValue(UsingHistroy.class);
                    UList.add(data);
                    StringBuilder LV_row = new StringBuilder();      //ListView의 한 행에 올라갈 데이터
                    //1.아이디추가 2.사용날짜추가 3.사용한 식권의 종류
                    LV_row.append(data.id+"\n");            //id추가후 줄바꿈
                    LV_row.append("사용날짜 :");
                    LV_row.append(data.date_year+"/");
                    LV_row.append(data.date_month+"/");
                    LV_row.append(data.date_day+"\n");      //날짜 추가후 줄바꿈
                    LV_row.append("사용한 식권의 종류: ");
                    LV_row.append(data.kindsofticket);
                    Log.d("PList용 데이터", LV_row.toString());
                    arrayList.add(LV_row.toString());
                    arrayList_reverse.add(LV_row.toString());
                }

                Collections.reverse(arrayList_reverse);

                ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        arrayList
                );
                Log.d("ListViewAdapter","성공");
                listView.setAdapter(listViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(

                                getActivity(),
                                android.R.layout.simple_list_item_1,
                                arrayList
                        );
                        listView.setAdapter(listViewAdapter);
                    case 1:

                        ArrayAdapter<String> listViewAdapter2 = new ArrayAdapter<String>(
                                getActivity(),
                                android.R.layout.simple_list_item_1,
                                arrayList_reverse
                        );
                        listView.setAdapter(listViewAdapter2);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
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
