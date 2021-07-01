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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private Button btnsignUp, btnlogIn;
    private EditText etcorreo, etcontra;
    private SignInButton signInButton;

    private View v;



    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        v = (View) findViewById(R.id.authLayout);

        etcorreo = (EditText) findViewById(R.id.editTextEmail);
        etcontra = (EditText) findViewById(R.id.editTextPassword);
        btnsignUp = (Button) findViewById(R.id.buttonRegistrar);
        btnlogIn = (Button) findViewById(R.id.buttonAcceder);
        signInButton = (SignInButton) findViewById(R.id.btnSignInGoogle);

        //Setup
        setup();
        session();
    }

    @Override
    protected void onStart() {
        super.onStart();

        v.setVisibility(View.VISIBLE);
    }


    //INICIO RECUPERAR DATOS

    private void session(){
        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String email = prefs.getString("email",null);
        String provider = prefs.getString("provider",null);

        if (email!= null && provider != null){
            v.setVisibility(View.INVISIBLE);
            showHome(email,ProviderType.valueOf(provider));
        }

    }
    //END RECUPERAR DATOS



    private void setup(){
        setTitle("Autenticación");//establecer titulo de la aplicacion

        //START BOTON  REGISTRAR
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
        //END BOTON  REGISTRAR

        //START BOTON  ACCEDER
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
        //END BOTON  ACCEDER


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInClient mGoogleSignInClient;

                // Configure Google Sign In
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                // [END config_signin]

                mGoogleSignInClient.signOut();

                // [START signin]
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                // [END signin]



            }
        });


    }


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // [START declare_auth]
        FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        // [END declare_auth]

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

                // [START auth_with_google]
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);
                                    showHome(user.getEmail(),ProviderType.GOOGLE);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithCredential:failure", task.getException());
                                    //updateUI(null);
                                    showAlert();
                                }
                            }
                        });
                // [END auth_with_google]
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    // [END onactivityresult]

    private void updateUI(FirebaseUser user) {

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

    //START MOSTRAR HOME
    private void showHome(String email, ProviderType provider){
        //START GO HOME ACTIVITY
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("provider", provider.name());
        startActivity(intent);
        //END GO HOME ACTIVITY
    }
    //END MOSTRAR HOME

}