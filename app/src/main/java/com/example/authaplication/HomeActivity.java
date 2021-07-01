package com.example.authaplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;
import java.util.Set;

enum ProviderType {
    BASIC,
    GOOGLE
}

public class HomeActivity extends AppCompatActivity {

    TextView tvcorreo,tvprovider;
    Button btnlogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvcorreo = (TextView) findViewById(R.id.textViewEmail);
        tvprovider = (TextView) findViewById(R.id.textViewProvider);
        btnlogOut = (Button) findViewById(R.id.buttonCerrar);

        Bundle bundle = getIntent().getExtras();

        setup(bundle.getString("email"),bundle.getString("provider"));

        //START GUARDADO DE DATOS
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        prefs.edit().putString("email",bundle.getString("email"));
        prefs.edit().putString("provider",bundle.getString("provider"));
        prefs.edit().apply();
        //END GUARDADO DE DATOS

    }

    private void setup(String email, String provider){
        setTitle("Inicio");

        tvcorreo.setText(email);
        tvprovider.setText(provider);

        btnlogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //START BORRADO DE DATOS
                SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
                prefs.edit().clear();
                prefs.edit().apply();
                //END BORRADO DE DATOS
                FirebaseAuth.getInstance().signOut();
                onBackPressed();//retorna al activity inicial, el main
            }
        });

    }
}