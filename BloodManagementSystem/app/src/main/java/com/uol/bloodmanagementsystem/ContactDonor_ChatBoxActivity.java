package com.uol.bloodmanagementsystem;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactDonor_ChatBoxActivity extends AppCompatActivity {

    private FloatingActionButton SendMessageButton;
    private EditText userMessageInput;
    private ListView listOfMessages;

    TextView messageText, messageUser, messageTime;

    String receivedGroup;
    private FirebaseAuth myAuth;
    private DatabaseReference UserRef, UserNameRef , UserMessageKeyRef;

    private FirebaseListAdapter<ContactDonor_chatMessage> adapter;

    //private String currentuserId, currentuserName, currentuserEmail, currentDate, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_donor__chat_box);

        listOfMessages = (ListView)findViewById(R.id.list_of_message);
        userMessageInput = (EditText)findViewById(R.id.input);
        SendMessageButton = (FloatingActionButton)findViewById(R.id.sendbtn);

        //receiving data
        Intent intent = getIntent();
        receivedGroup= intent.getStringExtra("group");

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userMessageInput.getText().toString().isEmpty())
                {
                    Toast.makeText(ContactDonor_ChatBoxActivity.this,"Please write some message...", Toast.LENGTH_SHORT).show();
                    userMessageInput.requestFocus();
                }
                else {

                    //ContactDonor_chatMessage chat = new ContactDonor_chatMessage(userMessageInput.getText().toString(),
                    //      FirebaseAuth.getInstance().getCurrentUser().getEmail(), messageToo.getText().toString());

                    FirebaseDatabase.getInstance().getReference("Messages").child(receivedGroup)
                            .push().setValue(new ContactDonor_chatMessage(userMessageInput.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getEmail()));

                    userMessageInput.setText("");
                }
            }
        });

        displayChatMessages();
    }

    private void displayChatMessages()
    {
       // showId = spinnerTo.getSelectedItem().toString();
        adapter = new FirebaseListAdapter<ContactDonor_chatMessage>(this,ContactDonor_chatMessage.class,R.layout.contact_donor_list_item,
                FirebaseDatabase.getInstance().getReference("Messages").child(receivedGroup))
        {
            @Override
            protected void populateView(View v, ContactDonor_chatMessage model, int position)
            {

                messageText =(TextView) v.findViewById(R.id.message_text);
                messageUser =(TextView) v.findViewById(R.id.message_user);
                messageTime =(TextView) v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageFrom());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

            }
        };
        listOfMessages.setAdapter(adapter);
    }

}
