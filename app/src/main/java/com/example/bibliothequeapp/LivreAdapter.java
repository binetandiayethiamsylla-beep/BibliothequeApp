package com.example.bibliothequeapp;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LivreAdapter extends RecyclerView.Adapter<LivreAdapter.LivreViewHolder> {

    private ArrayList<Livre> listeLivres;
    private MainActivity mainActivity; // Référence vers l'activité principale

    public LivreAdapter(ArrayList<Livre> listeLivres, MainActivity mainActivity) {
        this.listeLivres = listeLivres;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public LivreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livre, parent, false);
        return new LivreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LivreViewHolder holder, int position) {
        Livre livre = listeLivres.get(position);
        holder.tvTitreLivre.setText(livre.getTitre());
        holder.tvAuteurLivre.setText("Auteur : " + livre.getAuteur());
        holder.tvIsbnLivre.setText("ISBN : " + livre.getIsbn());

        if (livre.isDisponible()) {
            holder.tvDisponibilite.setText("Disponible");
            holder.tvDisponibilite.setBackgroundColor(Color.parseColor("#2E7D32")); // Vert
        } else {
            holder.tvDisponibilite.setText("Indisponible");
            holder.tvDisponibilite.setBackgroundColor(Color.parseColor("#C62828")); // Rouge
        }

        // Étape 5 : Gestion du clic sur la ligne
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("livre", livre);

            // Utilisation de la méthode de la MainActivity pour un lancement avec suivi
            mainActivity.lancerDetailActivity(intent, position);
        });
    }

    @Override
    public int getItemCount() {
        return listeLivres.size();
    }

    public static class LivreViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitreLivre, tvAuteurLivre, tvIsbnLivre, tvDisponibilite;

        public LivreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitreLivre    = itemView.findViewById(R.id.tvTitreLivre);
            tvAuteurLivre   = itemView.findViewById(R.id.tvAuteurLivre);
            tvIsbnLivre     = itemView.findViewById(R.id.tvIsbnLivre);
            tvDisponibilite = itemView.findViewById(R.id.tvDisponibilite);
        }
    }
}