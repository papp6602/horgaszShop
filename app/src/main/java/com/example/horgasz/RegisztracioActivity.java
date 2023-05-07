package com.example.horgasz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisztracioActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisztracioActivity.class.getName();
    private SharedPreferences preferences;
    private static  final String PREF_KEY = RegisztracioActivity.class.getPackage().toString();
    private FirebaseAuth mAuth;

    EditText fNev;
    EditText nev;
    EditText jelszo1;
    EditText jelszo;
    EditText email;
    EditText telefonszam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisztracio_activity);

        Bundle bundle = getIntent().getExtras();
        int secret_key = bundle.getInt("SECRET_KEY");
        if(secret_key!=410) {
            finish();
        }

        fNev = findViewById(R.id.EditTextFNev);
        nev = findViewById(R.id.EditTextNev);
        jelszo1 = findViewById(R.id.EditTextJelszo1);
        jelszo = findViewById(R.id.EditTextJelszo);
        email = findViewById(R.id.EditTextEmail);
        telefonszam =findViewById(R.id.EditTextTelefonszam);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String fNevPref = preferences.getString("userName","");
        String jelszoPref = preferences.getString("password","");

        fNev.setText(fNevPref);
        jelszo.setText(jelszoPref);
        jelszo1.setText(jelszoPref);

        mAuth = FirebaseAuth.getInstance();
    }

    public void megse(View view) {
        finish();
    }

    public void regisztracio(View view) {
        String fNevStr = fNev.getText().toString();
        String jelszoStr = jelszo.getText().toString();
        String jelszo1Str = jelszo1.getText().toString();
        String nevStr = nev.getText().toString();
        String emailStr = email.getText().toString();
        String telefonszamStr = telefonszam.getText().toString();

        if(jelszoStr.equals(jelszo1Str)) {
            mAuth.createUserWithEmailAndPassword(emailStr,jelszoStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Log.d(LOG_TAG, "Felhasználó létrehozva!");
                        shopInditas();
                    } else {
                        Log.d(LOG_TAG, "Felhasználó nem lett létrehozva!");
                        Toast.makeText(RegisztracioActivity.this,"Felhasználó nem lett létrehozva! "+task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            return;
        }
    }

    public void shopInditas() {
        Intent shop = new Intent(this,ShopListaActivity.class);

        startActivity(shop);
    }
}