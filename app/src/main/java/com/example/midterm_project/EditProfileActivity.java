package com.example.midterm_project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.midterm_project.Domain.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    CircleImageView avatar;
    EditText name, address, phone, email;
    Button updateProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        avatar = findViewById(R.id.picture);
        name = findViewById(R.id.person_name);
        address = findViewById(R.id.person_address);
        phone = findViewById(R.id.person_phone);
        email = findViewById(R.id.person_email);
        updateProfile = findViewById(R.id.updateProfile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Profile profile = (Profile) getIntent().getSerializableExtra("object");
        showProfile(profile);

        updateProfile.setOnClickListener(view -> {
            profile.setName(name.getText().toString());
            profile.setAddress(address.getText().toString());
            profile.setPhone(phone.getText().toString());
            profile.setEmail(email.getText().toString());

            mDatabase.child("users").child(mAuth.getUid()).setValue(profile).addOnSuccessListener(unused -> {

            });
        });
    }

    private void showProfile(Profile profile) {
        if (profile != null) {
            if (profile.getAvatar() != null)
                Glide.with(this)
                        .load(profile.getAvatar())
                        .into(avatar);

            if (profile.getName() != null)
                name.setText(profile.getName());

            if (profile.getAddress() != null)
                address.setText(profile.getAddress());

            if (profile.getPhone() != null)
                phone.setText(profile.getPhone());

            if (profile.getEmail() != null)
                email.setText(profile.getEmail());
        }
    }
}
