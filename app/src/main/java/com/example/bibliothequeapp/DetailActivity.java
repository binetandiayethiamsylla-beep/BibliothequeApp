package com.example.bibliothequeapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT_LIVRE = 200;

    private TextView tvTitre, tvAuteur, tvIsbn, tvDisponibilite;
    private Button btnModifier;
    private Livre livre;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        tvTitre = findViewById(R.id.tvTitre);
        tvAuteur = findViewById(R.id.tvAuteur);
        tvIsbn = findViewById(R.id.tvIsbn);
        tvDisponibilite = findViewById(R.id.tvDisponibilite);
        btnModifier = findViewById(R.id.btnModifier);

        livre = (Livre) getIntent().getSerializableExtra("livre");
        position = getIntent().getIntExtra("position", -1);

        if (livre != null) {
            tvTitre.setText(livre.getTitre());
            tvAuteur.setText(livre.getAuteur());
            tvIsbn.setText(livre.getIsbn());
            mettreAJourDesignBadge();

            tvDisponibilite.setOnClickListener(v -> {
                livre.setDisponible(!livre.isDisponible());
                mettreAJourDesignBadge();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("livre_modifie", livre);
                setResult(Activity.RESULT_OK, resultIntent);
                Toast.makeText(this, "Statut du livre mis à jour !", Toast.LENGTH_SHORT).show();
            });

            // Bouton Modifier
            btnModifier.setOnClickListener(v -> {
                Intent intent = new Intent(DetailActivity.this, AddEditActivity.class);
                intent.putExtra(AddEditActivity.EXTRA_MODE, AddEditActivity.MODE_EDIT);
                intent.putExtra(AddEditActivity.EXTRA_LIVRE, livre);
                intent.putExtra(AddEditActivity.EXTRA_POSITION, position);
                startActivityForResult(intent, REQUEST_EDIT_LIVRE);
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_LIVRE && resultCode == RESULT_OK && data != null) {
            livre = (Livre) data.getSerializableExtra(AddEditActivity.EXTRA_LIVRE);
            if (livre != null) {
                tvTitre.setText(livre.getTitre());
                tvAuteur.setText(livre.getAuteur());
                tvIsbn.setText(livre.getIsbn());
                mettreAJourDesignBadge();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("livre_modifie", livre);
                resultIntent.putExtra("position", position);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(this, "Livre modifié avec succès !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void mettreAJourDesignBadge() {
        if (livre.isDisponible()) {
            tvDisponibilite.setText("DISPONIBLE EN RAYON");
            tvDisponibilite.setBackgroundResource(R.drawable.badge_disponible);
        } else {
            tvDisponibilite.setText("EMPRUNTÉ / INDISPONIBLE");
            tvDisponibilite.setBackgroundResource(R.drawable.badge_indisponible);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}