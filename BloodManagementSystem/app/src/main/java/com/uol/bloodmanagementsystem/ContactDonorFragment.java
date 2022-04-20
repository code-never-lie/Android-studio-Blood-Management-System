package com.uol.bloodmanagementsystem;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;


public class ContactDonorFragment extends Fragment {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ContactDonor_chatMessage> adpater;
    RelativeLayout Fragment_Main;
    private FloatingActionButton sendbtn;
    private ListView lv;
    ArrayAdapter<String> groupAdapter;

    public ContactDonorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_donor, container, false);


/*
        if(FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(Fragment_Main,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
        }
*/
    }

    private AdapterView.OnItemSelectedListener listener;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv = (ListView) view.findViewById(R.id.contact_donor);

        final String[] arrayGroup = new String[]
                {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};

        groupAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayGroup);
        lv.setAdapter(groupAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // go to activity to load pizza details fragment
                //listener.onCountrySelected(position); // (3) Communicate with Activity using Listener

                Intent chatIntent = new Intent(getActivity().getApplicationContext(), ContactDonor_ChatBoxActivity.class);
                chatIntent.putExtra("group", arrayGroup[position]);
                startActivity(chatIntent);
            }
        });

    }
    public interface OnItemSelectedListener {
        void onCountrySelected(int position);
    }

}

