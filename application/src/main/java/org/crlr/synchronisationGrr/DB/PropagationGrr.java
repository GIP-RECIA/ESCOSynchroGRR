/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: PropagationCahierTexte.java,v 1.2 2009/09/01 09:26:07 weberent Exp $
 */

package org.crlr.synchronisationGrr.DB;

import java.util.List;

import org.crlr.exception.base.CrlrException;
import org.crlr.synchronisationGrr.DTO.DataEtablissementDTO;
import org.crlr.synchronisationGrr.DTO.EtablissementDTO;
import org.crlr.synchronisationGrr.DTO.PersonneDTO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Propagation des delta pour l'application cahier de texte.
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class PropagationGrr implements InterfacePropagationGrr {
    
    /**
     * {@inheritDoc}
     */
    @Transactional(
            readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
        )
     public void propagePersonneModification(PersonneDTO personneDTO)
            throws CrlrException {
    	dbGrr.propagePersonneModification(personneDTO);
        
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(
            readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
        )
    public void propageStructureModification(EtablissementDTO structure)
            throws CrlrException {
    	dbGrr.propageStructureModification(structure);
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional(
            readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
        )
	public List<String> findListeCodesEtablissements() {
		return dbGrr.findListeCodesEtablissements();
	}
    
    /**
     * {@inheritDoc}
     */
    @Transactional(
            readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class
        )
	public void propageMigration(String uai,
			DataEtablissementDTO datasEtablissement) throws CrlrException {
		dbGrr.propageMigration(uai,datasEtablissement);
	}

    /** Methodes sur la db de Cahier de Texte. */
    public DBGrr dbGrr;

    /**
     * Accesseur du dbCahierTexte.
     *
     * @return the dbCahierTexte
     */
    public DBGrr getDbGrr() {
        return dbGrr;
    }

    /**
     * Setter de dbCahierTexte.
     *
     * @param dbCahierTexte the dbCahierTexte to set
     */
    public void setDbGrr(DBGrr dbGrr) {
        this.dbGrr = dbGrr;
    }

	

   
}
