package com.example.authaplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    Button btnsignUp,btnlogIn, btngoogle;
    EditText etcorreo,etcontra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etcorreo = (EditText) findViewById(R.id.editTextEmail);
        etcontra = (EditText) findViewById(R.id.editTextPassword);
        btnsignUp = (Button) findViewById(R.id.buttonRegistrar);
        btnlogIn = (Button) findViewById(R.id.buttonAcceder);
        //btngoogle = (Button) findViewById(R.id.btnSignInGoogle);

        //Setup
        setup();

    }

    private void setup(){
        setTitle("Autenticación");//establecer titulo de la aplicacion

        btnsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( (etcorreo.getText().toString().isEmpty()==false) && (etcontra.getText().toString().isEmpty()==false) ){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.createUserWithEmailAndPassword(etcorreo.getText().toString(),etcontra.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        AuthResult result = task.getResult();
                                        // You can access the new user via result.getUser()
                                        // Additional user info profile *not* available via:
                                        // result.getAdditionalUserInfo().getProfile() == null
                                        // You can check if the user is new or existing:
                                        // result.getAdditionalUserInfo().isNewUser()
                                        //Toast.makeText(LoginActivity.this, "se registró el correo"+ result.getUser().getEmail()+"  "+ ProviderType.BASIC, Toast.LENGTH_SHORT).show();
                                        showHome(result.getUser().getEmail(),ProviderType.BASIC);
                                    } else {
                                        //Toast.makeText(LoginActivity.this, "no se registró el correo", Toast.LENGTH_SHORT).show();
                                        showAlert();
                                    }
                                }
                            });
                }else{
                    showAlertFieldEmpty();
                }
            }
        });
        btnlogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((etcorreo.getText().toString().isEmpty()==false) && (etcontra.getText().toString().isEmpty()==false)){
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signInWithEmailAndPassword(etcorreo.getText().toString(),etcontra.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        AuthResult result = task.getResult();
                                        // You can access the new user via result.getUser()
                                        // Additional user info profile *not* available via:
                                        // result.getAdditionalUserInfo().getProfile() == null
                                        // You can check if the user is new or existing:
                                        // result.getAdditionalUserInfo().isNewUser()
                                        //Toast.makeText(LoginActivity.this, "se registró el correo"+ result.getUser().getEmail()+"  "+ ProviderType.BASIC, Toast.LENGTH_SHORT).show();
                                        showHome(result.getUser().getEmail(),ProviderType.BASIC);
                                    } else {
                                        //Toast.makeText(LoginActivity.this, "no se registró el correo", Toast.LENGTH_SHORT).show();
                                        showAlert();
                                    }
                                }
                            });
                }else{
                    showAlertFieldEmpty();
                }
            }
        });

    }

    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando al usuario");
        builder.setPositiveButton("Aceptar",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showAlertFieldEmpty(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Datos incompletos");
        builder.setMessage("Verifique si ha ingresado el correo y la contraseña");
        builder.setPositiveButton("Aceptar",null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showHome(String email, ProviderType provider){
        //START GO HOME ACTIVITY
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("provider", provider.name());
        startActivity(intent);
        //END GO HOME ACTIVITY
    }



}