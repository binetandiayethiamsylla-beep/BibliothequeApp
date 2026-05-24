package com.example.bibliothequeapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LivreAdapter extends RecyclerView.Adapter<LivreAdapter.LivreViewHolder> {

    // CORRECTION : l'interface a maintenant 4 callbacks clairs et distincts
    public interface OnLivreClickListener {
        void onLivreClick(Livre livre);           // clic simple → ouvrir détail
        void onLivreLongClick(Livre livre, int position); // clic long → menu options
        void onLivreModifierClick(Livre livre);   // icône modifier → formulaire directement
        void onLivreSupprimerClick(Livre livre);  // icône supprimer → confirmation directement
    }

    private List<Livre> listeLivres;
    private OnLivreClickListener listener;

    public LivreAdapter(List<Livre> listeLivres, OnLivreClickListener listener) {
        this.listeLivres = listeLivres;
        this.listener = listener;
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
            holder.tvDisponibilite.setText("✅ Disponible");
            holder.tvDisponibilite.setBackgroundColor(Color.parseColor("#2E7D32"));
            holder.tvDisponibilite.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvDisponibilite.setPadding(20, 10, 20, 10);
        } else {
            holder.tvDisponibilite.setText("❌ Indisponible");
            holder.tvDisponibilite.setBackgroundColor(Color.parseColor("#C62828"));
            holder.tvDisponibilite.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvDisponibilite.setPadding(20, 10, 20, 10);
        }

        // Clic simple sur la carte → ouvrir le détail
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLivreClick(livre);
            }
        });

        // Clic long sur la carte → menu Modifier / Supprimer
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    listener.onLivreLongClick(livre, currentPosition);
                }
            }
            return true;
        });

        // CORRECTION : icône Modifier → ouvre directement le formulaire de modification
        holder.btnModifierItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLivreModifierClick(livre); // ✅ callback dédié
            }
        });

        // CORRECTION : icône Supprimer → affiche directement la confirmation de suppression
        holder.btnSupprimerItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onLivreSupprimerClick(livre); // ✅ callback dédié
            }
        });
    }

    @Override
    public int getItemCount() {
        return listeLivres.size();
    }

    public void ajouterLivre(Livre livre) {
        listeLivres.add(0, livre);
        notifyItemInserted(0);
    }

    public void modifierLivre(Livre livre) {
        for (int i = 0; i < listeLivres.size(); i++) {
            if (listeLivres.get(i).getId() == livre.getId()) {
                listeLivres.set(i, livre);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void supprimerLivre(Livre livre) {
        for (int i = 0; i < listeLivres.size(); i++) {
            if (listeLivres.get(i).getId() == livre.getId()) {
                listeLivres.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public static class LivreViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitreLivre;
        TextView tvAuteurLivre;
        TextView tvIsbnLivre;
        TextView tvDisponibilite;
        ImageButton btnModifierItem;
        ImageButton btnSupprimerItem;

        public LivreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitreLivre = itemView.findViewById(R.id.tvTitreLivre);
            tvAuteurLivre = itemView.findViewById(R.id.tvAuteurLivre);
            tvIsbnLivre = itemView.findViewById(R.id.tvIsbnLivre);
            tvDisponibilite = itemView.findViewById(R.id.tvDisponibilite);
            btnModifierItem = itemView.findViewById(R.id.btnModifierItem);
            btnSupprimerItem = itemView.findViewById(R.id.btnSupprimerItem);
        }
    }
}