package com.example.midterm_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class SignupTabFragment extends Fragment {
    EditText email, phone, pass, confirm_pass;
    Button signup;
    float v=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment, container, false);

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

        return root;
    }
}
