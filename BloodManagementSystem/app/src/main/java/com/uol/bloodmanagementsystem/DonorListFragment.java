package com.uol.bloodmanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DonorListFragment extends Fragment {

    public DonorListFragment()
    {
        // Required empty public constructor
    }

    ArrayList<DonorList> donors=new ArrayList<>();
    DonorList temp=new DonorList();

    ListView lv;
    TextView getMessage;
    Button showAllDonors, showDonorsByBloodGroup, showDonorsByCity;

    String name[]={"Asad Ahmad","Syed Abdullah","Ali Rehman","Asad Ahmad","Syed Abdullah","Ali Rehman","Asad Ahmad","Syed Abdullah","Ali Rehman","Asad Ahmad","Syed Abdullah","Ali Rehman","Asad Ahmad","Syed Abdullah","Ali Rehman","Asad Ahmad","Syed Abdullah","Ali Rehman"};
    String contact[]={"03349856746","03247583078","032345345","03349856746","03247583078","032345345","03349856746","03247583078","032345345","03349856746","03247583078","032345345","03349856746","03247583078","032345345","03349856746","03247583078","032345345"};
    String blood_group[]={"B+","O-","AB+","B+","O-","AB+","B+","O-","AB+","B+","O-","AB+","B+","O-","AB+","B+","O-","AB+"};

    String get_Name=null;

    private DatabaseReference myRef;

    private ArrayAdapter<String> Myarrayadapter;
    private ArrayList<String> list_of_donors = new ArrayList<>() ;

    private FirebaseListAdapter<DonorList> DonorAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.fragment_donor_list,container,false);
    }


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        //Myarrayadapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list_of_donors);
        //lv.setAdapter(Myarrayadapter);

        myRef = FirebaseDatabase.getInstance().getReference().child("Donors_Info");

        getMessage = (TextView) view.findViewById(R.id.Recieve_Message_DB);
        showAllDonors = (Button) view.findViewById(R.id.showAllDonorsbtn);
        showDonorsByCity = (Button) view.findViewById(R.id.showDonorsByCitybtn);
        showDonorsByBloodGroup = (Button) view.findViewById(R.id.showDonorsByBloodGroupbtn);


        showAllDonors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getActivity(),DonorsAllListActivity.class);
                startActivity(go);
            }
        });

        showDonorsByBloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getActivity(),DonorsListByBloodGroupActivity.class);
                startActivity(go);
            }
        });

        showDonorsByCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(getActivity(), DonorsAllListActivity.class);
                startActivity(go);
            }
        });

        //RetrieveDonors();

        //ChatListRef = FirebaseDatabase.getInstance().getReference().child("Donors_Info");

        // Comments from here worked fine
//        myRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                if(dataSnapshot.exists())
//                {
//                    DisplayMessages(dataSnapshot);
//                    //CustomListAdapter customAdapter = new CustomListAdapter(view.getContext(),donors);
//                    //lv.setAdapter(customAdapter);
//                }
//            }
//
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if(dataSnapshot.exists())
//                {
//                    //DisplayMessages(dataSnapshot);
//                }
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        //CustomListAdapter customAdapter = new CustomListAdapter();
       // lv.setAdapter(customAdapter);

    }


    private void DisplayMessages(DataSnapshot dataSnapshot) {

        Iterator iterator= dataSnapshot.getChildren().iterator();

        while(iterator.hasNext())
        {
            String getemail = (String)((DataSnapshot)iterator.next()).getValue();
            String get_BloodGroup = (String)((DataSnapshot)iterator.next()).getValue();
            //Toast.makeText(getActivity(), "Here : "+getname, Toast.LENGTH_SHORT).show();
            String getcity = (String)((DataSnapshot)iterator.next()).getValue();
            get_Name = (String)((DataSnapshot)iterator.next()).getValue();
            String get_Number = (String)((DataSnapshot)iterator.next()).getValue();
            //shahzaib
            temp.setDonor_bloodGroup(get_BloodGroup);
            temp.setDonor_name(get_Name);
            temp.setDonor_phoneNo(get_Number);
            donors.add(temp);

            getMessage.append(get_Name+" \n"+ get_BloodGroup + " \n" + get_Number +"\n\n");

            //getMessage.append("\n" + stringArrayList.size()+ " : " + get_Name +"\n");

        }

    }

    private void RetrieveDonors()
    {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while(iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_donors.clear();
                list_of_donors.addAll(set);
                Myarrayadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class CustomListAdapter extends ArrayAdapter {

        TextView get_name;
        TextView get_contact;
        TextView get_blood_group;

      ArrayList<DonorList> tempList;
        @Override
        public int getCount() {
            return tempList.size();
        }

        public CustomListAdapter(Context context,ArrayList<DonorList> donorrrr)
        {
            super(context, R.layout.custom_list_adapter);
            tempList=donorrrr;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           DonorList [] donorsArray=new DonorList[tempList.size()];
            donorsArray=  tempList.toArray(donorsArray);

            View view1 = getLayoutInflater().inflate(R.layout.custom_list_adapter,null);
            //getting view in row_data
             get_name = view1.findViewById(R.id.donorname);
             get_contact = view1.findViewById(R.id.donorcontact);
             get_blood_group = view1.findViewById(R.id.donorblood_group);

            //getMessage.append("\n\n" + stringArrayList.size());

//            for(int k=0; k<stringArrayList.size(); k++) {
//                if (!stringArrayList.get(k).equals(""))
//                {
//                    get_name.setText(stringArrayList.get(k));
//                    //txt1.append(i+1 + "=    " + com.get(i) + "\n\n");
//                }
//            }



            get_name.setText(donorsArray[i].getDonor_name());
            get_contact.setText(donorsArray[i].getDonor_phoneNo());
            get_blood_group.setText( donorsArray[i].getDonor_bloodGroup());

          /*  get_name.setText(name[i]);
            get_contact.setText(contact[i]);
            get_blood_group.setText(blood_group[i]);
*/
            return view1;
        }
    }

}


