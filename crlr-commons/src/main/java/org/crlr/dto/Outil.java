/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: Outil.java,v 1.1 2009/07/24 14:14:36 ent_breyton Exp $
 */

package org.crlr.dto;

/**
 * Outils disponible.
 * @author breytond
 *
 */
public enum Outil {
    /** main. */
    CONNEXION, ACCUEIL, 
    /** cahier de texte. */
    CAHIER, CAHIER_ARCHIVE, 
    /** Devoir. */
    DEVOIRS, AJOUT_DEVOIR, EDIT_DEVOIR, CONSULTER_DEVOIR,
    /** Séquences. */
    AJOUT_SEQUENCE, RECH_SEQUENCE, EDIT_SEQUENCE, DELETE_SEQUENCE, DUPLIQUER_SEQUENCE,
    /** Séances. */
    AJOUT_SEANCE, RECH_SEANCE, EDIT_SEANCE, DELETE_SEANCE, DUPLIQUER_SEANCE, CONSULTER_SEANCE;
}
