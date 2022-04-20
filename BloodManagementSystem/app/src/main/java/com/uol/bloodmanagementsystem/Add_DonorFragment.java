package com.uol.bloodmanagementsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class Add_DonorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add__donor,container,false);
    }

    public Add_DonorFragment() {
        // Required empty public constructor
    }

    TextView donor_Email;
    EditText donor_Name, donor_Contact, donor_City;
    Spinner blood_Group;
    private String get_name, get_blood_group, get_contact, get_city, get_email;
    Button save;
    private FirebaseUser mAuth;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        donor_Email =(TextView) view.findViewById(R.id.donorEmail);
        donor_Name = (EditText) view.findViewById(R.id.donorName);
        donor_City = (EditText) view.findViewById(R.id.donorCity);
        donor_Contact = (EditText) view.findViewById(R.id.donorPhone);
        blood_Group = (Spinner) view.findViewById(R.id.bloodGroup);
        save = (Button) view.findViewById(R.id.btnSave);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();

        String[] arraySpinner = new String[]
                { "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blood_Group.setAdapter(adapter);

        if (mAuth != null) {
            // Name, email address etc
            //String name = user.getDisplayName();
            get_email = mAuth.getEmail();
            donor_Email.setText(get_email);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_name = donor_Name.getText().toString().trim();
                get_contact = donor_Contact.getText().toString().trim();
                get_city = donor_City.getText().toString().trim();
                get_blood_group = blood_Group.getSelectedItem().toString();


                if (get_name.isEmpty()) {
                    donor_Name.setError("Donor name Required");
                    donor_Name.requestFocus();
                    return;
                }
                if (get_city.isEmpty()) {
                    donor_City.setError("City Required");
                    donor_City.requestFocus();
                    return;
                }
                if (get_contact.isEmpty()) {
                    donor_Contact.setError("Phone Number Required");
                    donor_Contact.requestFocus();
                    return;
                }
                if (get_contact.length() != 11) {
                    donor_Contact.setError("Number should be 11 digits long");
                    donor_Contact.requestFocus();
                    return;
                }

                final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Please wait ...", "Saving ...", true);

                Add_DonorInfo donor = new Add_DonorInfo(get_email, get_name, get_blood_group, get_city, get_contact);
                FirebaseDatabase.getInstance().getReference("Donors_Info")
                       // .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .push().setValue(donor).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            //    progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Donor Added Successfully.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Donor data not saved.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                                    /*
                                    Toast.makeText(SignUpActivity.this,"Registered Successfully.", Toast.LENGTH_LONG).show();
                                    Intent go = new Intent(SignUpActivity.this,LoginActivity.class);
                                    startActivity(go); */
            }

        });

    }
}
