package com.example.websocket.games.backgammon.repo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Board {
    @Id
    private Long id;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    private String state;
    @ManyToOne
    private Player player1;
    @ManyToOne
    private Player player2;

}
