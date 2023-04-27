package com.example.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class LoginTabFragment extends Fragment {
    EditText email, password;
    DBHelper DB;
    TextView usernameText,userEmailText;
    SessionManagement sessionManagement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);
        View btn_login = root.findViewById(R.id.signin_btn);
        email = root.findViewById(R.id.email_id);
        password = root.findViewById(R.id.password);
        DB = new DBHelper(getActivity());
        sessionManagement = new SessionManagement(getContext());

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String e = email.getText().toString();
                String p = password.getText().toString();
                if (TextUtils.isEmpty(p) || TextUtils.isEmpty(e))
                    Toast.makeText(getActivity(), "All fields are Required", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkEmailPassword = DB.checkEmailPassword(e,p);
                    if (checkEmailPassword==true){
                        Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                        String name = DB.getName(e);
                        sessionManagement.createSession(name,e);


                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                }

            }


        });
        return root;
    }
}
