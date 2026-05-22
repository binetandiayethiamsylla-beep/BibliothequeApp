package com.example.bibliothequeapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    // Déclaration des vues
    private TextView tvTitre, tvAuteur, tvIsbn, tvDisponibilite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Activation du bouton retour dans la barre d'action
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Récupération des composants graphiques
        tvTitre = findViewById(R.id.tvTitre);
        tvAuteur = findViewById(R.id.tvAuteur);
        tvIsbn = findViewById(R.id.tvIsbn);
        tvDisponibilite = findViewById(R.id.tvDisponibilite);

        // Récupération de l'objet Livre envoyé par l'Adapter
        Livre livre = (Livre) getIntent().getSerializableExtra("livre");

        if (livre != null) {
            tvTitre.setText(livre.getTitre());
            tvAuteur.setText("Auteur: " + livre.getAuteur());
            tvIsbn.setText("ISBN: " + livre.getIsbn());

            // Gestion de l'affichage du statut de disponibilité
            if (livre.isDisponible()) {
                tvDisponibilite.setText("Disponible");
            } else {
                tvDisponibilite.setText("Indisponible");
            }
        }
    }

    // Gestion du clic sur le bouton retour de la barre d'action
    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Ferme cette activité et retourne à la liste
        return true;
    }
}