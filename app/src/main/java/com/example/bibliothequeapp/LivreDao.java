package com.example.bibliothequeapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LivreDao {

    @Insert
    void insert(Livre livre);

    @Update
    void update(Livre livre);

    @Delete
    void delete(Livre livre);

    @Query("SELECT * FROM livres ORDER BY id DESC")
    List<Livre> getAllLivres();

    @Query("DELETE FROM livres")
    void deleteAll();

    // BONUS : recherche par titre avec LIKE
    // Le % autour de :titre permet de chercher n'importe où dans le titre
    @Query("SELECT * FROM livres WHERE titre LIKE '%' || :titre || '%' ORDER BY id DESC")
    List<Livre> searchByTitre(String titre);
}