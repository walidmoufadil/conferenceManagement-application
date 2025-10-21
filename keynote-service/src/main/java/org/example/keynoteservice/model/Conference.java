package org.example.keynoteservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class Conference {

    private Long id;
    private String titre;
    private String type;
    private Date date;
    private Double duree;
    private int nombreInscrits;
    private Double score;
    private Long keynoteId;
}

