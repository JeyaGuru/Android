package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText InputName;
    private EditText Inputpassword;
    private Button Login,SignUp;
    private ProgressDialog loadingBar;
    private String parentDbName="Users";
    private  boolean connected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputName = (EditText) findViewById(R.id.lnName);
        Inputpassword = (EditText) findViewById(R.id.pdtext);
        Login = (Button) findViewById(R.id.lnButton);
        SignUp=(Button) findViewById(R.id.snButton);
        loadingBar=new ProgressDialog(this);



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    login();
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                    if(!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent);
                    }

            }
        });
    }

    public void login() {
        String name= InputName.getText().toString();
        String password= Inputpassword.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please Enter the User Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Enter the Password",Toast.LENGTH_SHORT).show();
        }
        else if(true){
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

            if(!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
                Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
            else {
                loadingBar.setTitle("Login Account");
                loadingBar.setMessage("Please wait,While we are Checking the Credentials.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                validateUserPassword(name,password);
            }
        }

    }

    public void validateUserPassword(final String name,final String password)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentDbName).child(name).exists()){

                    Users userData=dataSnapshot.child(parentDbName).child(name).getValue(Users.class);

                    if(userData.getName().equals(name) && userData.getPassword().equals(password)) {
                        Intent intent = new Intent(MainActivity.this, SecondActivty.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, "Login Successfully!!", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Please Check your User Name/Password",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }else
                {
                    Toast.makeText(MainActivity.this,"This User Name/Password Not Exists",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
