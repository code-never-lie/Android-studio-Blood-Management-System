package com.uol.bloodmanagementsystem;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.text.format.DateFormat;
import android.widget.Toast;

public class ContactDonor_ChatActivity extends AppCompatActivity {

    //private Toolbar mToolbar;
    private FloatingActionButton SendMessageButton;
    private EditText userMessageInput;
    private ListView listOfMessages;
    private Spinner spinnerTo;
    TextView messageText, messageUser, messageTime;

    String messageId, showId;
    private FirebaseAuth myAuth;
    private DatabaseReference UserRef, UserNameRef , UserMessageKeyRef;

    private FirebaseListAdapter<ContactDonor_chatMessage> adapter;

    private String currentuserId, currentuserName, currentuserEmail, currentDate, currentTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_donor__chat);

        listOfMessages = (ListView)findViewById(R.id.list_of_message);
        userMessageInput = (EditText)findViewById(R.id.input);
        SendMessageButton = (FloatingActionButton)findViewById(R.id.sendbtn);
        spinnerTo = (Spinner) findViewById(R.id.messageTo);

        String[] arraySpinner = new String[]
                { "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(adapter);


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userMessageInput.getText().toString().isEmpty())
                {
                    Toast.makeText(ContactDonor_ChatActivity.this,"Please write some message...", Toast.LENGTH_SHORT).show();
                    userMessageInput.requestFocus();
                }
                else {
                    messageId = spinnerTo.getSelectedItem().toString();

                    //ContactDonor_chatMessage chat = new ContactDonor_chatMessage(userMessageInput.getText().toString(),
                      //      FirebaseAuth.getInstance().getCurrentUser().getEmail(), messageToo.getText().toString());

                    FirebaseDatabase.getInstance().getReference("Messages").child(messageId)
                            .push().setValue(new ContactDonor_chatMessage(userMessageInput.getText().toString(),
                                    FirebaseAuth.getInstance().getCurrentUser().getEmail()));

                    userMessageInput.setText("");
                }
            }
        });


        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayChatMessages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //displayChatMessages();
    }

    private void displayChatMessages()
    {
        showId = spinnerTo.getSelectedItem().toString();
        adapter = new FirebaseListAdapter<ContactDonor_chatMessage>(this,ContactDonor_chatMessage.class,R.layout.contact_donor_list_item,
        FirebaseDatabase.getInstance().getReference("Messages").child(showId))
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
/*
        //Get Current user Name ,retrieve group name from previous fragment and show to next Group Activity
        currentGroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(GroupChatActivity.this, currentGroupName , Toast.LENGTH_SHORT).show();

        myAuth =FirebaseAuth.getInstance();
        currentuserId = myAuth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserNameRef = FirebaseDatabase.getInstance().getReference().child("");


        InitializeFields();

        GetUserInfo();

        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SaveMessageInfoToDatabase();

                userMessageInput.setText(null);

                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void InitializeFields()
    {

        mToolbar = (Toolbar) findViewById(R.id.main_create_group_option);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentGroupName);

        SendMessageButton = (FloatingActionButton) findViewById(R.id.sendbtn);
        userMessageInput = (EditText) findViewById(R.id.input);
        displayTextMessages = (TextView) findViewById(R.id.group_chat_text_display);
        mScrollView = (ScrollView) findViewById(R.id.my_scroll_view);
    }


    private void GetUserInfo()
    {
        UserRef.child(currentuserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    currentuserName = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void SaveMessageInfoToDatabase()
    {
        String message = userMessageInput.getText().toString();
        String messageKey = GroupNameRef.push().getKey(); // create key in group for each message a unique key
        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(this, "Write Message First..!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calForTime.getTime());

            HashMap <String, Object> groupMessageKey = new HashMap<>();
            GroupNameRef.updateChildren(groupMessageKey);

            GroupMessageKeyRef = GroupNameRef.child(messageKey);
            HashMap <String ,Object> messageinfoMap = new HashMap<>();
            messageinfoMap.put("name",currentuserName);
            messageinfoMap.put("message",message);
            messageinfoMap.put("date",currentDate);
            messageinfoMap.put("time",currentTime);
            GroupMessageKeyRef.updateChildren(messageinfoMap);

        }
    }


    private void DisplayMessages(DataSnapshot dataSnapshot) {

        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext())
        {
            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            displayTextMessages.append(chatName +" :\n"+ chatMessage +" \n"+chatDate +"    "+ chatTime+"\n\n\n");

            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
*/
}
