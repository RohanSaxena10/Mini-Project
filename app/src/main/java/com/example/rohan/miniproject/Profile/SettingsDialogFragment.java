package com.example.rohan.miniproject.Profile;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rohan.miniproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Rohan on 31/03/17.
 */
public class SettingsDialogFragment extends DialogFragment {

    static SettingsDialogFragment newInstance(int num) {
        SettingsDialogFragment settingsDialogFragment = new SettingsDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        settingsDialogFragment.setArguments(args);

        return settingsDialogFragment;

    }



    EditText etmax;
    EditText etemergencynumber;
    Button btndone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_dialogfragment, container, false);

        etmax = (EditText)v.findViewById(R.id.etmax);
        etemergencynumber = (EditText)v.findViewById(R.id.etnumber);
        btndone = (Button)v.findViewById(R.id.btndone);
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Done!",Toast.LENGTH_LONG);
                if(!etmax.getText().toString().equals(""))
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("MaxBPM").setValue(Integer.parseInt(etmax.getText().toString()));
                if(!etemergencynumber.getText().toString().equals(""))
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("EmergencyContact").setValue(Integer.parseInt(etemergencynumber.getText().toString()));
                SettingsDialogFragment.this.dismiss();
            }
        });


        return v;
    }

}
