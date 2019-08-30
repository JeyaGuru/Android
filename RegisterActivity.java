package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.DatabaseMetaData;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText Inputname,Inputpassword,InputconsumerNumber,InputphoneNumber,InputEmail;
    private Button createAccount;
    private ProgressDialog loadingBar;
    private  boolean connected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Inputname=(EditText)findViewById(R.id.UsName);
        Inputpassword=(EditText)findViewById(R.id.signupPswd);
        InputconsumerNumber=(EditText)findViewById(R.id.consumerNumber);
        InputphoneNumber=(EditText)findViewById(R.id.phNumber);
        InputEmail=(EditText)findViewById(R.id.emailId);
        createAccount=(Button)findViewById(R.id.SignupButton);
        loadingBar=new ProgressDialog(this);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    CreateAccount();
            }
        });
    }
    private void CreateAccount() {
        String name= Inputname.getText().toString();
        String password= Inputpassword.getText().toString();
        String consumerNumber= InputconsumerNumber.getText().toString();
        String phoneNumber= InputphoneNumber.getText().toString();
        String email= InputEmail.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            Toast.makeText(this,"Please Enter the User Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please Enter the Password",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(consumerNumber))
        {
            Toast.makeText(this,"Please Enter the Consumer Number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneNumber))
        {
            Toast.makeText(this,"Please Enter the Phone Number",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Enter the email",Toast.LENGTH_SHORT).show();
        }
        else if(true)
        {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

            if(!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
                Toast.makeText(RegisterActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
            else {
                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("Please wait,While we are Creating the Credentials.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                validateTelephoneNumberPassword(name,phoneNumber,password,consumerNumber,email);
            }
        }


    }
    public void validateTelephoneNumberPassword(final String name, final String phone,final String password,final String consumerNumber,final String email)
    {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(name).exists())){
                    HashMap<String,Object> userDataMap=new HashMap<String, Object>();
                    userDataMap.put("name",name);
                    userDataMap.put("phone",phone);
                    userDataMap.put("password",password);
                    userDataMap.put("consumerNumber",consumerNumber);
                    userDataMap.put("email",email);

                    RootRef.child("Users").child(name).updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this,"Congratualtions, Your Account Created Successfully!!",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this,"Network Error Please try again Later!!",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

                }else
                {
                        Toast.makeText(RegisterActivity.this,"This User Name "+name+" Already Exists",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
