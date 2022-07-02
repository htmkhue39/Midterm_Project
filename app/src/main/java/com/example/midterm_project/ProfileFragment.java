package com.example.midterm_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.midterm_project.Domain.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    CircleImageView avatar;
    TextView name, address, phone, email, logout;
    Button editProfile;

    Profile profile = new Profile();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        avatar = root.findViewById(R.id.picture);
        name = root.findViewById(R.id.person_name);
        address = root.findViewById(R.id.person_address);
        phone = root.findViewById(R.id.person_phone);
        email = root.findViewById(R.id.person_email);
        logout = root.findViewById(R.id.logout);
        editProfile = root.findViewById(R.id.editProfile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editProfile.setEnabled(false);

        mDatabase.child("users").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profile = snapshot.getValue(Profile.class);
                profile.setEmail(mAuth.getCurrentUser().getEmail());

                showProfile(profile);

                editProfile.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, error.toString());
            }
        });

        editProfile.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), EditProfileActivity.class);
            i.putExtra("object", profile);
            startActivity(i);
        });

        logout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        return root;
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
