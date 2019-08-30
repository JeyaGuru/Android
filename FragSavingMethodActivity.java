package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FragSavingMethodActivity extends Fragment {
    private TextView inputName;
    private DatabaseReference mRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.activity_frag_saving_method,container,false);
        inputName=(TextView)v.findViewById(R.id.saveid);
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query lastQuery = databaseReference.child("Voltage").orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String message = dataSnapshot.getValue().toString();
                double aDouble = Double.parseDouble(message.substring(22,26));
                double result=(((aDouble+aDouble)/100)*150);
                String result1=Double.toString(result);
                inputName.setText(result1.substring(result1.length()-2,result1.length()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Handle possible errors.
            }
        });



        return v;

    }
}
