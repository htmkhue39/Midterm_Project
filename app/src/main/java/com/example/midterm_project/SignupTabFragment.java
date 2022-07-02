package com.example.midterm_project;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class SignupTabFragment extends Fragment {
    public static final String TAG = SignupTabFragment.class.getName();

    private FirebaseAuth mAuth;

    EditText email, phone, pass, confirm_pass;
    Button signup;
    float v=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();

        email = root.findViewById(R.id.email);
        pass = root.findViewById(R.id.pass);
        phone = root.findViewById(R.id.phone);
        confirm_pass = root.findViewById(R.id.confirm_pass);
        signup = root.findViewById(R.id.sign_up);


        email.setTranslationX(600);
        pass.setTranslationX(600);
        phone.setTranslationX(600);
        signup.setTranslationX(600);

        email.setAlpha(v);
        pass.setAlpha(v);
        phone.setAlpha(v);
        confirm_pass.setAlpha(v);
        signup.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        phone.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();
        confirm_pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(900).start();
        signup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(1100).start();

        signup.setOnClickListener(view -> {
            signupAccount(view);
        });

        return root;
    }

    private void signupAccount(View view) {
        String e = email.getText().toString();
        String p = pass.getText().toString();

        Log.d(TAG, e);
        Log.d(TAG, p);

        mAuth.createUserWithEmailAndPassword(e, p)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
