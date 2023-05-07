package com.example.horgasz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShopListaActivity extends AppCompatActivity {
    private static final String LOG_TAG = ShopListaActivity.class.getName();
    private FirebaseUser felhasznalo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_lista);

        felhasznalo = FirebaseAuth.getInstance().getCurrentUser();
        if(felhasznalo != null) {
            Log.d(LOG_TAG, "Azonosított felhasználó!");
        } else {
            Log.d(LOG_TAG, "Nem azonosított felhasználó!");
            finish();
        }
    }
}