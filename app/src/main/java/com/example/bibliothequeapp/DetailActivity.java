package com.example.bibliothequeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private TextView tvTitre, tvAuteur, tvIsbn, tvDisponibilite;
    private Button btnModifier;
    private Livre livre;

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

        if (livre != null) {
            tvTitre.setText(livre.getTitre());
            tvAuteur.setText(livre.getAuteur());
            tvIsbn.setText(livre.getIsbn());
            mettreAJourDesignBadge();

            tvDisponibilite.setOnClickListener(v -> {
                livre.setDisponible(!livre.isDisponible());
                mettreAJourDesignBadge();
                Toast.makeText(this, "Statut mis à jour !", Toast.LENGTH_SHORT).show();
            });

            btnModifier.setOnClickListener(v -> {
                Intent intent = new Intent(DetailActivity.this, AddEditActivity.class);
                intent.putExtra(AddEditActivity.EXTRA_MODE, AddEditActivity.MODE_EDIT);
                intent.putExtra(AddEditActivity.EXTRA_LIVRE, livre);
                startActivity(intent);
            });
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