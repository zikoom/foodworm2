package com.example.foodworm2;

import android.content.Intent;
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
import android.widget.Button;
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

import java.lang.reflect.Array;
import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link buyhistory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link buyhistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class buyhistory extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //리스트뷰 변수
    ListView listView;

    //거래 기록을 담을 컬렉션
    ArrayList<PurchaseHistory> PList;
    //리스트뷰에 올릴 어레이리스트를 만든다
    ArrayList<String> arrayList;
    ArrayList<String> arrayList2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    //유저의 User객체값을 저장할 변수
    private User myself;
    //유저의 아이디를 저장할 변수
    private String UserId;
    private FirebaseAuth mAuth;
    //DataBase리퍼런스
    DatabaseReference mDatabase;
    //정보를 긁어올 query변수
    Query mQ;

    public buyhistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment buyhistory.
     */
    // TODO: Rename and change types and number of parameters
    public static buyhistory newInstance(String param1, String param2) {
        buyhistory fragment = new buyhistory();
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
        Log.d("유저아이디 받기(buyhistory)", UserId);

        arrayList = new ArrayList<>();
        arrayList2 = new ArrayList<>();
        PList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buyhistory, container, false);
        listView = (ListView) view.findViewById(R.id.lv_buy);

        //스피너 변수
        Spinner spinner_sort = (Spinner) view.findViewById(R.id.spinner_sort);
        ArrayAdapter sortAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinnerArray, android.R.layout.simple_spinner_dropdown_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_sort.setAdapter(sortAdapter);


        //사용자 거래내역 받기
        //PList에 사용자의 거래내역 클래스(PurchaseHistory)가 차곡차곡 담긴다
        mQ = mDatabase.child("PurchaseHistory").orderByChild("id").equalTo(UserId);
        Log.d("쿼리변수 구매내역 받음", "진행");
        mQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    PurchaseHistory data = ds.getValue(PurchaseHistory.class);
                    PList.add(data);
                    //PList에 현재 user의 거래내역 한 개 담았다. 이것을 하나의 스트링으로 예쁘게 만들어서 ArrayList에 추가하자.
                    //1.PurchaseHistory의 내용을 예쁘게 제단하여 하나의 스트링으로 만든다.
                    StringBuilder LV_row = new StringBuilder();      //ListView의 한 행에 올라갈 데이터
                    //1.아이디추가 2.구매날짜추가 3. 구매한각각의 식권추가
                    LV_row.append(data.id + "\n");            //id추가후 줄바꿈
                    LV_row.append("구매한날짜 :");
                    LV_row.append(data.date_year + "/");
                    LV_row.append(data.date_month + "/");
                    LV_row.append(data.date_day + "\n"); //날짜 추가후 줄바꿈
                    LV_row.append("식권별 구매 개수: ");
                    LV_row.append("한식: " + data.kor + " ");
                    LV_row.append("일식: " + data.jpa + " ");
                    LV_row.append("양식: " + data.usa);         //각 식권별 구매장수 입력
                    //문자열 하나로 예쁘게만들었다. arrayList에 추가
                    Log.d("AList용 데이터", LV_row.toString());
                    arrayList.add(LV_row.toString());
                    arrayList2.add(LV_row.toString());
                }

                Collections.reverse(arrayList2);

                ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        arrayList
                );
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
                        //Collections.reverse(arrayList);
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
                                arrayList2
                        );
                        listView.setAdapter(listViewAdapter2);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
        // return inflater.inflate(R.layout.fragment_buyhistory, container, false);
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
