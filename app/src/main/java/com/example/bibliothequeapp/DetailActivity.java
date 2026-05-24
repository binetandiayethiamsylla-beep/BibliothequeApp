package com.example.bibliothequeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {

    private TextView tvTitre, tvAuteur, tvIsbn, tvDisponibilite;
    private Button btnModifier;

    private Livre livre;

    // CORRECTION : on a besoin de Room et d'un ExecutorService pour persister les changements
    private AppDatabase database;
    private ExecutorService executorService;

    // CORRECTION : un launcher pour récupérer le résultat de AddEditActivity
    private ActivityResultLauncher<Intent> editLauncher;

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

        // Initialisation de Room et du thread secondaire
        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        // CORRECTION : enregistrement du launcher AVANT de l'utiliser
        initialiserEditLauncher();

        livre = (Livre) getIntent().getSerializableExtra("livre");

        if (livre != null) {
            tvTitre.setText(livre.getTitre());
            tvAuteur.setText(livre.getAuteur());
            tvIsbn.setText(livre.getIsbn());
            mettreAJourDesignBadge();

            // CORRECTION : le toggle de disponibilité persiste maintenant dans Room
            tvDisponibilite.setOnClickListener(v -> {
                livre.setDisponible(!livre.isDisponible());
                mettreAJourDesignBadge();

                // Sauvegarde de la nouvelle disponibilité dans Room (thread secondaire)
                executorService.execute(() -> {
                    database.livreDao().update(livre);
                    runOnUiThread(() ->
                            Toast.makeText(this, "Statut mis à jour !", Toast.LENGTH_SHORT).show()
                    );
                });
            });

            // CORRECTION : on utilise le launcher pour récupérer les modifications
            btnModifier.setOnClickListener(v -> {
                Intent intent = new Intent(DetailActivity.this, AddEditActivity.class);
                intent.putExtra(AddEditActivity.EXTRA_MODE, AddEditActivity.MODE_EDIT);
                intent.putExtra(AddEditActivity.EXTRA_LIVRE, livre);
                editLauncher.launch(intent); // ✅ résultat capturé
            });
        }
    }

    private void initialiserEditLauncher() {
        editLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        Livre livreMaj = (Livre) data.getSerializableExtra(AddEditActivity.EXTRA_LIVRE);

                        if (livreMaj == null) return;

                        // CORRECTION : mise à jour dans Room depuis le thread secondaire
                        executorService.execute(() -> {
                            database.livreDao().update(livreMaj);
                            runOnUiThread(() -> {
                                // Mettre à jour l'objet local et rafraîchir l'affichage
                                livre = livreMaj;
                                tvTitre.setText(livre.getTitre());
                                tvAuteur.setText(livre.getAuteur());
                                tvIsbn.setText(livre.getIsbn());
                                mettreAJourDesignBadge();
                                Toast.makeText(this, "Livre modifié", Toast.LENGTH_SHORT).show();
                            });
                        });
                    }
                }
        );
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fermeture propre du thread secondaire
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}