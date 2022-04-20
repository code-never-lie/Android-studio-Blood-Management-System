package com.uol.bloodmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

public class DonorsAllListActivity extends AppCompatActivity {

    private FirebaseListAdapter<DonorList> DonorAdapter;
    SearchView searchDonor;
    ListView lvDonors;
    TextView get_name, get_contact, get_blood_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donors_all_list);

        searchDonor = (SearchView)findViewById(R.id.mySearch);
        lvDonors = (ListView)findViewById(R.id.myDonorList);
        lvDonors.setTextFilterEnabled(true);

        showAllDonors();


        lvDonors.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int i, long id)
            {
                Intent intent = new Intent(DonorsAllListActivity.this,DonorDetailActivity.class);

                intent.putExtra("name", i);
//                intent.putExtra("contact", contact[i]);
//                intent.putExtra("blood_group", blood_group[i]);

                startActivity(intent);
            }
        });


        searchDonor.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //myAdapter.(newText);
                return false;
            }
        });
    }


    private void showAllDonors()
    {
        DonorAdapter = new FirebaseListAdapter<DonorList>(this,DonorList.class,R.layout.custom_list_adapter,
                FirebaseDatabase.getInstance().getReference("Donors_Info"))
        {
            @Override
            protected void populateView(View v, DonorList model, int position)
            {
                get_name = v.findViewById(R.id.donorname);
                get_contact = v.findViewById(R.id.donorcontact);
                get_blood_group = v.findViewById(R.id.donorblood_group);

                get_name.setText(model.getDonor_name());
                get_contact.setText(model.getDonor_phoneNo());
                get_blood_group.setText(model.getDonor_bloodGroup());
            }
        };
        lvDonors.setAdapter(DonorAdapter);
    }
}
