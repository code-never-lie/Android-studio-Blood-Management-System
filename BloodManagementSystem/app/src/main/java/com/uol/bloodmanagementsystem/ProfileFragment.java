package com.uol.bloodmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class ProfileFragment extends Fragment {

    private TextView pUserName, pUserEmail;
    private ImageView pImage;
    private String pID, pName, pEmail, pUserImage;

    private FirebaseAuth pAuth;
    private DatabaseReference pRef;
    private FirebaseUser profileUser;

    private static final int RESULT_OK = -1;
    private static final int galleryPick = 1;
    private StorageReference userProfileImageRef;

    private ProgressDialog loadingBar;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view,savedInstanceState);

        pUserName = (TextView)view.findViewById(R.id.ProfileUserName);
        pUserEmail = (TextView)view.findViewById(R.id.ProfileUserEmail);
        pImage = (ImageView)view.findViewById(R.id.imageProfile);

        loadingBar = new ProgressDialog(getContext());

        pAuth = FirebaseAuth.getInstance();

        profileUser = FirebaseAuth.getInstance().getCurrentUser() ;
        pID = profileUser.getUid();

        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        pRef = FirebaseDatabase.getInstance().getReference();

        // Toast.makeText(this, "" + myFirebaseUser.getEmail(), Toast.LENGTH_SHORT).show();

        pRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pName = dataSnapshot.child("Users_Info").child(pID).child("user_name").getValue(String.class);
                pEmail = dataSnapshot.child("Users_Info").child(pID).child("user_email").getValue(String.class);

                pUserImage = dataSnapshot.child("Users_Info").child(pID).child("image").getValue().toString();

                //Toast.makeText(HomeActivity.this, "Retrieving profile data" , Toast.LENGTH_SHORT).show();

                pUserName.setText(pName);
                pUserEmail.setText(pEmail);
                Picasso.get().load(pUserImage).into(pImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), "Error retrieving profile data", Toast.LENGTH_SHORT).show();
            }
        });

        pImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, galleryPick);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==galleryPick && resultCode==RESULT_OK && data!=null)
        {
            Uri imageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(getContext(), this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK)
            {
                loadingBar.setTitle("Set Profile Image");
                loadingBar.setMessage("Please wait. Your profile image is updating ...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                Uri resultUri = result.getUri();

                final StorageReference filePath = userProfileImageRef.child(pID + ".jpg");
                filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity(), "Profile image uploaded", Toast.LENGTH_SHORT).show();

                            final String downloadUrl = task.getResult().toString();
                                    //task.getResult().getMetadata()..toString();

                            Toast.makeText(getActivity(), "Download: " + downloadUrl, Toast.LENGTH_SHORT).show();

                            pRef.child("Users_Info").child(pID).child("image").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            //loadingBar.dismiss();
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(getActivity(), "Image stored in Database ", Toast.LENGTH_SHORT).show();

                                                loadingBar.dismiss();
                                            }
                                            else
                                            {
                                                String message = task.getException().toString();
                                                Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });


                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();

                            loadingBar.dismiss();
                        }
                    }
                });
            }
        }
    }
}
