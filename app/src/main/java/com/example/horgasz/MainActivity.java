package com.example.horgasz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final int SECRET_KEY = 410;
    private SharedPreferences preferences;
    private static  final String PREF_KEY = MainActivity.class.getPackage().toString();
    private static  final int RC_SIGN_KEY = 123;
    EditText fNev;
    EditText jelszo;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fNev = findViewById(R.id.EditTextFNev);
        jelszo = findViewById(R.id.EditTextJelszo);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSign = GoogleSignIn.getClient(this, gso);

    }

    public void belepes(View view) {


        String fNevStr = fNev.getText().toString();
        String jelszoStr = jelszo.getText().toString();

        mAuth.signInWithEmailAndPassword(fNevStr,jelszoStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(LOG_TAG, "Felhasználó azonosítva!");
                    shopInditas();
                } else {
                    Log.d(LOG_TAG, "Felhasználó nem azonosítható!");
                    Toast.makeText(MainActivity.this,"Felhasználó nem azonosítható! "+task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                }
            }
        });

        Log.i(LOG_TAG, "Felhasználó név: "+fNevStr+" jelszó: "+jelszoStr);
    }

    public void regisztracio(View view) {
        Intent regisztracio = new Intent(this,RegisztracioActivity.class);
        regisztracio.putExtra("SECRET_KEY",410);

        startActivity(regisztracio);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName",fNev.getText().toString());
        editor.putString("password",jelszo.getText().toString());
        editor.apply();
    }

    public void belepesVendeg(View view) {
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(LOG_TAG, "Felhasználó anoním!");
                    shopInditas();
                } else {
                    Log.d(LOG_TAG, "Felhasználó nem azonosítható!");
                    Toast.makeText(MainActivity.this,"Felhasználó nem azonosítható! "+task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestKod, int resultKod, Intent data) {
        super.onActivityResult(requestKod, resultKod, data);
        if(requestKod == RC_SIGN_KEY) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                belepesGoogleAccount(account.getIdToken());
            } catch (ApiException e) {
                Log.w(LOG_TAG,"Nem sikerült az azonosítás! ", e);
            }
        }
    }

    private void belepesGoogleAccount(String idToken) {
        AuthCredential cred = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(cred).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(LOG_TAG, "Felhasználó azonosítva!");
                    shopInditas();
                } else {
                    Log.d(LOG_TAG, "Felhasználó nem azonosítható!");
                    Toast.makeText(MainActivity.this,"Felhasználó nem azonosítható! "+task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void belepesGoogle(View view) {
        Intent signen = mGoogleSign.getSignInIntent();
        startActivityForResult(signen, RC_SIGN_KEY);
    }

    public void shopInditas() {
        Intent shop = new Intent(this,ShopListaActivity.class);
        startActivity(shop);
    }
}