/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractBusiness.java,v 1.2 2009/09/16 08:46:39 weberent Exp $
 */

package org.crlr.synchronisationGrr.DB;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 * Classe abstraite définissant l'entityManager de la couche métier.
 *
 * @author breytond.
 */
public abstract class AbstractBusiness {
    /** Le persistence unit injecté par Spring pour accèder à la base de données de Cahier de texte. */
    @PersistenceContext(unitName = "Grr", type = PersistenceContextType.EXTENDED)
    private EntityManager entityManagerGrr;
    
    /**
     * Constructeur de l'objet GenericEntityManagerDao.java.
     */
    public AbstractBusiness() {
        super();
    }    
    
    /**
     * Accesseur de entityManagerCahier.
     * @return le entityManagerCahier
     */
    public EntityManager getEntityManagerGrr() {
        return entityManagerGrr;
    }

    /**
     * Mutateur de entityManagerCahier.
     * @param entityManagerCahier le entityManagerCahier à modifier.
     */
    public void setEntityManagerGrr(EntityManager entityManagerGrr) {
        this.entityManagerGrr = entityManagerGrr;
    }
   
}
