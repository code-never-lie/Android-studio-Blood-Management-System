package com.uol.bloodmanagementsystem;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DonorDetailActivity extends AppCompatActivity {

    TextView name, donor_email, city, contact, blood_group;
    ImageButton pCall, pSms, vCall, chatActivity;

    private DatabaseReference detailRef1, detailRef2;

    private ArrayList<String> list_donors = new ArrayList<>() ;
    private String[] myDonorArray;
    private Integer receivedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_detail);

        detailRef2 = FirebaseDatabase.getInstance().getReference().child("Donors_Info");

        name = (TextView)findViewById(R.id.donorGetName);
        donor_email = (TextView)findViewById(R.id.donorGetEmail);
        contact = (TextView)findViewById(R.id.donorGetPhoneNo);
        city = (TextView)findViewById(R.id.donorGetCity);
        blood_group = (TextView)findViewById(R.id.donorGetBloodGroup);

        pCall = (ImageButton)findViewById(R.id.phoneCall);
        pSms = (ImageButton)findViewById(R.id.phoneSms);
        vCall = (ImageButton)findViewById(R.id.videoCall);
        chatActivity = (ImageButton)findViewById(R.id.chatBox);

        //receiving data
        Intent intent = getIntent();
        receivedId = intent.getIntExtra("name", -1);

        DonorDetailDisplay();

//        myDonorArray = new String[list_donors.size()];
//        myDonorArray = list_donors.toArray(myDonorArray);

        //detailRef1 = FirebaseDatabase.getInstance().getReference().child("Donors_Info").child("-LTwR2DkEmNe9FSoiJ2F");

        pCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contact.getText()));
                startActivity(callIntent);
            }
        });
        pSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.setData(Uri.parse("smsto:" + contact.getText()));
               // smsIntent.putExtra("address", "12125551212");
               // smsIntent.putExtra("sms_body","Body of Message");
                startActivity(smsIntent);
            }
        });


        chatActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent = new Intent(DonorDetailActivity.this,ContactDonor_ChatActivity.class);
                startActivity(chatIntent);
            }
        });

        //showAllDonors();
    }

    private void DonorDetailDisplay()
    {
        detailRef2.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while(iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_donors.clear();
                list_donors.addAll(set);
           //     list_donors.listIterator();
                //Toast.makeText(DonorDetailActivity.this, "List Data \n" + list_donors, Toast.LENGTH_SHORT).show();

                myDonorArray = new String[list_donors.size()];
                myDonorArray = list_donors.toArray(myDonorArray);

                detailRef1 = FirebaseDatabase.getInstance().getReference().child("Donors_Info").child(myDonorArray[receivedId]);

                //Toast.makeText(DonorDetailActivity.this, "Selected Data \n" + myDonorArray[receivedId], Toast.LENGTH_SHORT).show();

                showDonorData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showDonorData()
    {
        //DatabaseReference FirebaseRef = FirebaseDatabase.getInstance().getReference();

        detailRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                //Toast.makeText(DonorDetailActivity.this, "NEW  : " + snapshot.getValue(), Toast.LENGTH_LONG).show();

                //Toast.makeText(DonorDetailActivity.this, "NEW  : " + snapshot.getValue(), Toast.LENGTH_LONG).show();

                String get_email = (String)((snapshot).child("donor_addByEmail")).getValue();
                String get_BloodGroup = (String)((snapshot).child("donor_bloodGroup")).getValue();
                String get_city = (String)((snapshot).child("donor_city")).getValue();
                String get_Name = (String)((snapshot).child("donor_name")).getValue();
                String get_Number = (String)((snapshot).child("donor_phoneNo")).getValue();

                //Toast.makeText(DonorDetailActivity.this, "No of donor  : " + get_email, Toast.LENGTH_LONG).show();

                name.setText(get_Name);
                donor_email.setText(get_email);
                blood_group.setText(get_BloodGroup);
                city.setText(get_city);
                contact.setText(get_Number);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
