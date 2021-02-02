package com.example.michal.app100;


import android.hardware.display.VirtualDisplay;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.RemoteInput;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TopFreeFragment extends Fragment {
    //variables
    private ListView listR;
    //private TextView test;
    private FirebaseAuth firebaseAuth;
    private Connector connector;
    private DatabaseReference ref;
    private String idZehot;
    private ArrayList<ReportDisplay> results = new ArrayList<ReportDisplay>();
    /////////////////////////////////
    private ArrayList<String> mUserReports = new ArrayList<>();

    public TopFreeFragment() {
        // Required empty public constructor
    }

    /////////////////////////////////////////////////////////////////////////////////

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_top_free, container, false);
        //start
        //pointer to results
        //ArrayList report_details = getListData(v);
        //test =  (TextView) v.findViewById(R.id.textView7);
        listR = (ListView) v.findViewById(R.id.listV);
        getListData();
        //end
        //auth user
        //getUserReports(v);
        return v;
    }
    /////////////////////////////////////////////////////////////////////////////////
    private void getListData() {
        identifyUser();
    }

    /////////////////////////////////////////////////////////////////////////////////
    private void identifyUser() {
        firebaseAuth = FirebaseAuth.getInstance();
        String Uid = firebaseAuth.getCurrentUser().getUid();
        connector = new Connector();
        ref = connector.databaseReferenceUsers.child(Uid).child("idUser");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //test.setText(dataSnapshot.getValue(String.class));//delete later
                idZehot=dataSnapshot.getValue(String.class);
                getUserReports(idZehot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    /////////////////////////////////////////////////////////////////////////////////
    private void getUserReports(String idZehot) {
        final List<Report> list1 = new ArrayList<Report>();
        ArrayAdapter<Report> reportAdapter = new ArrayAdapter<Report>(getActivity(),android.R.layout.simple_list_item_1,list1);

        connector = new Connector();
        //Query
        ref = connector.databaseReferenceReport.child("Car");
        ref.orderByChild("userZehot").equalTo(idZehot).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get all of the children at this level
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                //shake hands with each of them
                for (DataSnapshot child: children){
                    Report r1= child.getValue(Report.class);
                    list1.add(r1);

                }
                CustomListAdapter adapter = new CustomListAdapter(getActivity().getApplicationContext(),list1);
                listR.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /////////////////////////////////////////////////////////////////////////////////

}

