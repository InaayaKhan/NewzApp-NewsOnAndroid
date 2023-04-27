package com.example.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.util.regex.Pattern;

public class RegisterTabFragment extends Fragment{
    EditText name, email, password, cpassword;
    DBHelper DB;
    TextView usernameText,userEmailText;
    SessionManagement sessionManagement;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.register_tab_fragment,container,false);

        name = root.findViewById(R.id.editText);
        email = root.findViewById(R.id.editText2);
        password = root.findViewById(R.id.editText3);
        cpassword = root.findViewById(R.id.editText4);
        View btn_register = root.findViewById(R.id.signup_btn);
        DB = new DBHelper(getActivity());
        sessionManagement = new SessionManagement(getContext());

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = name.getText().toString();
                String p = password.getText().toString();
                String e = email.getText().toString();
                String cp = cpassword.getText().toString();

                final Pattern PASSWORD_PATTERN =
                        Pattern.compile("^" +
                                //"(?=.*[0-9])" +         //at least 1 digit
                                //"(?=.*[a-z])" +         //at least 1 lower case letter
                                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                                "(?=.*[a-zA-Z])" +      //any letter
                                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                                "(?=\\S+$)" +           //no white spaces
                                ".{4,}" +               //at least 4 characters
                                "$");


                if (TextUtils.isEmpty(n) || TextUtils.isEmpty(p) || TextUtils.isEmpty(e) || TextUtils.isEmpty(e) || TextUtils.isEmpty(cp))
                    Toast.makeText(getContext(), "All fields are Required", Toast.LENGTH_SHORT).show();
                else {
                    if (p.equals(cp)) {
                        if (PASSWORD_PATTERN.matcher(p).matches()) {
                            if (Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                                Boolean checkEmail = DB.checkEmail(e);
                                if (checkEmail == false) {
                                    Boolean insert = DB.insertData(n, e, p);
                                    if (insert != null) {
                                        Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                        sessionManagement.createSession(n,e);

                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getActivity(), "User Already Exists", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Enter valid Email address !", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(), "Password too weak!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        return root;
    }
}
