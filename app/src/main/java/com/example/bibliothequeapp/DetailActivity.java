package com.example.bibliothequeapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private TextView tvTitre, tvAuteur, tvIsbn, tvDisponibilite;
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

        livre = (Livre) getIntent().getSerializableExtra("livre");

        if (livre != null) {
            tvTitre.setText(livre.getTitre());
            tvAuteur.setText(livre.getAuteur());
            tvIsbn.setText(livre.getIsbn());

            // Applique le style initial
            mettreAJourDesignBadge();

            // BONUS : Cliquer sur le badge change le statut en temps réel !
            tvDisponibilite.setOnClickListener(v -> {
                // Inverse la valeur booléenne
                livre.setDisponible(!livre.isDisponible());

                // Actualise immédiatement les couleurs à l'écran
                mettreAJourDesignBadge();

                // Prépare le paquet retour pour la MainActivity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("livre_modifie", livre);
                setResult(Activity.RESULT_OK, resultIntent);

                Toast.makeText(this, "Statut du livre mis à jour !", Toast.LENGTH_SHORT).show();
            });
        }
    }

    // Petite méthode interne pour éviter la répétition du code de couleur
    private void mettreAJourDesignBadge() {
        if (livre.isDisponible()) {
            tvDisponibilite.setText("DISPONIBLE EN RAYON");
            tvDisponibilite.setBackgroundColor(Color.parseColor("#2E7D32")); // Vert moderne
        } else {
            tvDisponibilite.setText("EMPRUNTÉ / INDISPONIBLE");
            tvDisponibilite.setBackgroundColor(Color.parseColor("#C62828")); // Rouge moderne
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Ferme et retourne à la liste
        return true;
    }
}