package com.example.bibliothequeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLivres;
    private LivreAdapter livreAdapter;
    private ArrayList<Livre> listeLivres;
    private int positionLivreModifie = -1;

    private final ActivityResultLauncher<Intent> detailActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Livre livreModifie = (Livre) result.getData().getSerializableExtra("livre_modifie");
                    if (livreModifie != null && positionLivreModifie != -1) {
                        listeLivres.set(positionLivreModifie, livreModifie);
                        livreAdapter.notifyItemChanged(positionLivreModifie);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewLivres = findViewById(R.id.recyclerViewLivres);

        listeLivres = new ArrayList<>();
        listeLivres.add(new Livre(1, "Le Petit Prince", "Antoine de Saint-Exupéry", "9780156013987", true));
        listeLivres.add(new Livre(2, "L'Étranger", "Albert Camus", "9782070360024", false));
        listeLivres.add(new Livre(3, "Les Misérables", "Victor Hugo", "9782253096344", true));
        listeLivres.add(new Livre(4, "Une si longue lettre", "Mariama Bâ", "9782841290529", true));
        listeLivres.add(new Livre(5, "Le Vieux Nègre et la Médaille", "Ferdinand Oyono", "9782264018304", false));
        listeLivres.add(new Livre(6, "Madame Bovary", "Gustave Flaubert", "9782070409228", true));
        listeLivres.add(new Livre(7, "La Peste", "Albert Camus", "9782070360420", false));
        listeLivres.add(new Livre(8, "Sous l'orage", "Seydou Badian", "9782708707691", true));

        recyclerViewLivres.setLayoutManager(new LinearLayoutManager(this));

        livreAdapter = new LivreAdapter(listeLivres, this);
        recyclerViewLivres.setAdapter(livreAdapter);
    }

    public void lancerDetailActivity(Intent intent, int position) {
        this.positionLivreModifie = position;
        detailActivityLauncher.launch(intent);
    }
}