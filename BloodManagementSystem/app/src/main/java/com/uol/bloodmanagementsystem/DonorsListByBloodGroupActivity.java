package com.uol.bloodmanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DonorsListByBloodGroupActivity extends AppCompatActivity {

    Spinner bloodGroups;
    ListView listBloodDonors;
    private FirebaseListAdapter<DonorList> adapter;
    TextView bdonorname, bdonorBloodGroup, bdonorNo;

    String name[]={"Shahzaib","Sami","Umer","Uzair","Asad","Junaid","Ali"};
    String contact[]={"03012834746","03164649845","03004636456","03016456245","03127843265","03024576846","03349757497"};
    String blood_group[]={"A-","B-","B+","A-","B+","AB+","A+"};


    int count=0;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors_list_by_blood_group);

        bloodGroups = (Spinner) findViewById(R.id.bloodGroup);
        listBloodDonors = (ListView) findViewById(R.id.list_bloodDonors);

        String[] arraySpinner = new String[]
                {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroups.setAdapter(adapter);

        bloodGroups.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //displayDonors();
//                for(int i = 0; i<blood_group.length; i++) {
//                    if (bloodGroups.getSelectedItem().toString().equals(blood_group[i])) {
//
//                        Toast.makeText(DonorsListByBloodGroupActivity.this, "Hello 1 " + bloodGroups.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//
//                        CustomListAdapter adpt = new CustomListAdapter(getApplicationContext());
//                        listBloodDonors.setAdapter(adpt);
//                    }
//                }
                count=0;
                for (int k=0; k<name.length; k++)
                {

                    if (bloodGroups.getSelectedItem().toString().equals(blood_group[k]))
                    {
                        count++;
                    }
                }
                Toast.makeText(DonorsListByBloodGroupActivity.this, "Count " + count, Toast.LENGTH_LONG).show();

                //listBloodDonors.setAdapter(null);
                CustomListAdapter adpt = new CustomListAdapter();
                listBloodDonors.setAdapter(adpt);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //displayDonors();
    }


    public class CustomListAdapter extends BaseAdapter {

        TextView get_name;
        TextView get_contact;
        TextView get_blood_group;

        ArrayList<DonorList> tempList;

        ArrayList<Integer> ar1 = new ArrayList<Integer>();

        @Override
        public int getCount() {

            return count;
            //return name.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

//        public CustomListAdapter(Context context)
//        {
//            super(context, R.layout.custom_list_adapter);
//
//        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
//            DonorList [] donorsArray=new DonorList[tempList.size()];
            //          donorsArray=  tempList.toArray(donorsArray);

            View view1 = getLayoutInflater().inflate(R.layout.custom_list_adapter, null);
            //getting view in row_data
            get_name = view1.findViewById(R.id.donorname);
            get_contact = view1.findViewById(R.id.donorcontact);
            get_blood_group = view1.findViewById(R.id.donorblood_group);

            for (int k = 0; k < blood_group.length; k++) {

                if (bloodGroups.getSelectedItem().toString().equals(blood_group[k])) {

                    //Toast.makeText(DonorsListByBloodGroupActivity.this, "Hello 1 " + bloodGroups.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    ar1.add(k);

//                    get_name.setText(name[k]);
//                    get_contact.setText(contact[k]);
//                    get_blood_group.setText(blood_group[k]);

                }
            }

            for (int g = 0; g < ar1.size(); g++)
            {
                get_name.setText(name[ar1.get(i)]);
                get_contact.setText(contact[ar1.get(i)]);
                get_blood_group.setText(blood_group[ar1.get(i)]);
            }
            return view1;
        }
    }




    private String showBG;
    private void displayDonors()
    {
        showBG = bloodGroups.getSelectedItem().toString();
        adapter = new FirebaseListAdapter<DonorList>(this,DonorList.class,R.layout.custom_list_adapter,
                FirebaseDatabase.getInstance().getReference("Donors_Info"))
        {
            @Override
            protected void populateView(View v, DonorList model, int position)
            {

               // bdonorname =(TextView) v.findViewById(R.id.donorname);
//                bdonorNo =(TextView) v.findViewById(R.id.donorcontact);
//                bdonorBloodGroup =(TextView) v.findViewById(R.id.donorblood_group);

                bdonorname = v.findViewById(R.id.donorname);
                bdonorNo = v.findViewById(R.id.donorcontact);
                bdonorBloodGroup = v.findViewById(R.id.donorblood_group);

                Toast.makeText(DonorsListByBloodGroupActivity.this, "Hello 1 " + showBG, Toast.LENGTH_SHORT).show();
                if(showBG.equals(model.getDonor_bloodGroup()))
                {
                    Toast.makeText(DonorsListByBloodGroupActivity.this, "Hello 2 " + showBG, Toast.LENGTH_SHORT).show();


                    bdonorname.setText(model.getDonor_name());
                    bdonorNo.setText(model.getDonor_phoneNo());
                    bdonorBloodGroup.setText(model.getDonor_bloodGroup());
                }
                //bdonorname.setText(model.getDonorName());
                //bdonorNo.setText(model.getDonorPhoneNo());
                //bdonorBloodGroup.setText(model.getDonorBloodGroup());

            }
        };
        listBloodDonors.setAdapter(adapter);
    }
}
