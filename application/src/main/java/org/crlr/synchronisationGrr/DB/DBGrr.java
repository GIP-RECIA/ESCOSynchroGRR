/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: jalopy.xml,v 1.1 2010/06/15 12:20:58 ent_breyton Exp $
 */

package org.crlr.synchronisationGrr.DB;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crlr.exception.base.CrlrException;
import org.crlr.synchronisationGrr.DTO.DataEtablissementDTO;
import org.crlr.synchronisationGrr.DTO.DomaineDTO;
import org.crlr.synchronisationGrr.DTO.EntryDTO;
import org.crlr.synchronisationGrr.DTO.EntryModerateDTO;
import org.crlr.synchronisationGrr.DTO.EtablissementDTO;
import org.crlr.synchronisationGrr.DTO.OverloadDTO;
import org.crlr.synchronisationGrr.DTO.PeriodeDTO;
import org.crlr.synchronisationGrr.DTO.PersonneDTO;
import org.crlr.synchronisationGrr.DTO.Profil;
import org.crlr.synchronisationGrr.DTO.RepeatDTO;
import org.crlr.synchronisationGrr.DTO.RoomDTO;
import org.crlr.synchronisationGrr.DTO.SiteDTO;
import org.crlr.synchronisationGrr.DTO.TypeResaDTO;
import org.crlr.utils.BooleanUtils;
import org.crlr.utils.CollectionUtils;
import org.crlr.utils.StringUtils;

/**
 * Les méthodes d'accès à la base de données de cahier de texte.
 *
 * @author aurore.weber
 * @version $Revision: 1.3 $
 */
public class DBGrr extends AbstractBusiness {

    /** Le prefixe des tables de grr. */
    private String prefixeGrr ;
    private String nomSiteParDefaut ;
    private String nomDomainesDefaut;
    private String typeDomainesDefaut;
    private String createSiteOnCreateEtab;
    
    public void setPrefixeGrr(String prefixeGrr) {
		this.prefixeGrr = prefixeGrr;
	}

	public void setNomSiteParDefaut(String nomSiteParDefaut) {
		this.nomSiteParDefaut = nomSiteParDefaut;
	}

	public void setNomDomainesDefaut(String nomDomainesDefaut) {
		this.nomDomainesDefaut = nomDomainesDefaut;
	}

	public void setTypeDomainesDefaut(String typeDomainesDefaut) {
		this.typeDomainesDefaut = typeDomainesDefaut;
	}

	public void setCreateSiteOnCreateEtab(String createSiteOnCreateEtab) {
		this.createSiteOnCreateEtab = createSiteOnCreateEtab;
	}

	private String[] getNomDomainesDefaut(){
    	
    	if (StringUtils.isEmpty(nomDomainesDefaut)){
    		return null;
    	} else {
    		return nomDomainesDefaut.split("\\$");
    	}
    }

    // /******************** Methodes public ********************************/
    /**
     * Synchronisation dans le cas de la modification d'une personne.
     *
     * @param persDTO la personne a synchroniser.
     *
     * @throws CrlrException .
     */
    public void propagePersonneModification(PersonneDTO persDTO)
                                     throws CrlrException {
    	Integer idEtablissement = getIdEtablissement(persDTO.getCodeEtablissementPrincipal());
    	
    	if (existUser(persDTO.getUid())) {
    		updatePersonne(persDTO,idEtablissement);
    	} else {
    		createPersonne(persDTO,idEtablissement);
    	}
    	
    	//On supprime tous les droits sur les établissements pour les recrées.
    	deleteDroitEtablissements(persDTO);
    	switch (persDTO.getProfil()){
    	case AdministrateurEtablissement : 
    		addDroitAdministrateurEtablissement(persDTO);
    	case Usager : 
    		addDroitUsagerEtablissement(persDTO);
    		break;
    	case Administrateur :
    		//Rien à faire.
    		default:
    	}

    }


	/**
     * Synchronisation dans le cas de la modification d'une structure.
     *
     * @param structureDTO l'établissement à synchroniser.
     *
     * @throws CrlrException .
     */
    public void propageStructureModification(EtablissementDTO structureDTO)
                                      throws CrlrException {
        final Integer idEtab = getIdEtablissement(structureDTO.getCode());
        if (idEtab != 0) {
        	updateStructure(structureDTO,idEtab);
        } else {
        	createStructure(structureDTO);
        } 

        ////////////////////////////////////////////////
        // MODIFICATION RECIA | DEBUT | 2012-04-17
        ////////////////////////////////////////////////
    	// On supprime l'eventuel regroupement ou l'etablissement
        // est considere comme secondaire
    	deleteRegroupementEtablissement(structureDTO);
        // On recree un eventuel regroupement
        addRegroupementEtablissement(structureDTO);
        ////////////////////////////////////////////////
        // MODIFICATION RECIA | FIN
        ////////////////////////////////////////////////
    }

    


	
    
    //    /***************** Modifications de la  BDD ************************************/
    
    /**
     * cree une structure dans Grr avec les zones associées.
     *
     * @param ou le nom de l'etablissement
     * @param siren le siren de l'etablissement
     *
     * @throws CrlrException .
     */
    private void createStructure(EtablissementDTO structureDTO)
                          throws CrlrException {
    	final Integer idEtablissement = createEtablissement(structureDTO);
    	
    	if (BooleanUtils.isTrue(createSiteOnCreateEtab)){
    	
	    	final BigInteger idSite = createSite(structureDTO);
	    	
	    	createJointureEtablissementSite(idEtablissement, idSite);
	    	
	    	if (getNomDomainesDefaut() != null){
	    		for (String nomDomaine : getNomDomainesDefaut()){
	    	        final BigInteger idArea = createArea(nomDomaine);
	    	        createJointureSiteArea(idSite,idArea);
	    		}
	    	}
    	}
    }
    
    
    private void updateStructure(EtablissementDTO structureDTO,Integer idEtablissement)
    		throws CrlrException {
    	updateEtablissement(structureDTO,idEtablissement);
    	
    	if (BooleanUtils.isTrue(createSiteOnCreateEtab) && getNbSiteParEtablissement(idEtablissement).compareTo(BigInteger.ONE) < 0 ){
    		final BigInteger idSite = createSite(structureDTO);
        	createJointureEtablissementSite(idEtablissement, idSite);
        	
        	if (getNomDomainesDefaut() != null){
        		for (String nomDomaine : getNomDomainesDefaut()){
        	        final BigInteger idArea = createArea(nomDomaine);
        	        createJointureSiteArea(idSite,idArea);
        		}
        	}
    	}

    }
    
    private void deleteDroitEtablissements(PersonneDTO persDTO) {
    	deleteUsagerEtablissement(persDTO.getUid());
    	deleteAdministrateurEtablissement(persDTO.getUid());
	}

    private void addDroitUsagerEtablissement(PersonneDTO persDTO) throws CrlrException {
    	for (String code : persDTO.getEtablissements()){
    		Integer idEtablissement = getIdEtablissement(code);
    		if (idEtablissement != 0) {
    			createJointureUsagerEtablissement(persDTO.getUid(), idEtablissement);
                ////////////////////////////////////////////////
                // MODIFICATION RECIA | DEBUT | 2012-04-19
                ////////////////////////////////////////////////
                Integer idEtablissementPrincipal = getIdEtablissementPrincipal(code);
                if(idEtablissementPrincipal != 0) {
    			    createJointureUsagerEtablissement(persDTO.getUid(), idEtablissementPrincipal);
                }
                ////////////////////////////////////////////////
                // MODIFICATION RECIA | FIN
                ////////////////////////////////////////////////
    		}
    	}
	}
    
    private void addDroitAdministrateurEtablissement(PersonneDTO persDTO) throws CrlrException {
    	for (String code : persDTO.getEtablissementsAdmin()){
    		Integer idEtablissement = getIdEtablissement(code);
    		if (idEtablissement != 0) {
    			createJointureAdministrateurEtablissement(persDTO.getUid(), idEtablissement);
                ////////////////////////////////////////////////
                // MODIFICATION RECIA | DEBUT | 2012-04-19
                ////////////////////////////////////////////////
                Integer idEtablissementPrincipal = getIdEtablissementPrincipal(code);
                if(idEtablissementPrincipal != 0) {
    			    createJointureAdministrateurEtablissement(persDTO.getUid(), idEtablissementPrincipal);
                }
                ////////////////////////////////////////////////
                // MODIFICATION RECIA | FIN
                ////////////////////////////////////////////////
    		}
    	}  
		
	}
    
    ////////////////////////////////////////////////
    // MODIFICATION RECIA | DEBUT | 2012-04-17
    ////////////////////////////////////////////////
    private void addRegroupementEtablissement(EtablissementDTO structureDTO) throws CrlrException {
        String uaiPrincipal = structureDTO.getUaiPrincipal();
        if(! StringUtils.isEmpty(uaiPrincipal)) {
            createStructureRegroupement(structureDTO.getCode(),uaiPrincipal);
        }
	}

    private void deleteRegroupementEtablissement(EtablissementDTO structureDTO) {
        deleteStructureRegroupement(structureDTO.getCode());
	}
    ////////////////////////////////////////////////
    // MODIFICATION RECIA | FIN
    ////////////////////////////////////////////////
    
    
//  /*****************  Modification d'une seule table dans la BDD ************************************/
    
    private void deleteUsagerEtablissement(String uid) {
    	final String queryDeleteJointure =
    			"DELETE FROM " + prefixeGrr + "j_user_etablissement" +
    					" WHERE login = ? ";
    	getEntityManagerGrr().createNativeQuery(queryDeleteJointure)
    	.setParameter(1, uid)
    	.executeUpdate();
	}
    
    private void createJointureUsagerEtablissement(String uid, Integer idEtablissement) {
    	final String queryCreate =
    			"INSERT IGNORE INTO " + prefixeGrr + "j_user_etablissement (id_etablissement, login) VALUES (?,?)";

        getEntityManagerGrr().createNativeQuery(queryCreate)
        	.setParameter(1, idEtablissement)
            .setParameter(2, uid).executeUpdate();
	}
    
    private void deleteAdministrateurEtablissement(String uid) {
    	final String queryDeleteJointure =
    			"DELETE FROM " + prefixeGrr + "j_useradmin_etablissement" +
    					" WHERE login = ? ";
    	getEntityManagerGrr().createNativeQuery(queryDeleteJointure)
    	.setParameter(1, uid)
    	.executeUpdate();
	}
    
    private void createJointureAdministrateurEtablissement(String uid, Integer idEtablissement) {
    	final String queryCreate =
    			"INSERT IGNORE INTO " + prefixeGrr + "j_useradmin_etablissement (id_etablissement, login) VALUES (?,?)";

        getEntityManagerGrr().createNativeQuery(queryCreate)
        	.setParameter(1, idEtablissement)
            .setParameter(2, uid).executeUpdate();
	}

	private String createPersonne(PersonneDTO persDTO, Integer idEtablissementPrincipal) {

    	final String queryCreateUser =
    			"INSERT INTO " + prefixeGrr + "utilisateurs" +
    					" SET login  = ?, " +
    					"     nom    = ?, " +
    					"     prenom = ?, " +
    					"     email  = ?, " +
    					"	  statut = ?," +
    					"     etat   = ?," +
    					"     default_etablissement = ?," +
    					"     default_site = ?," +
    					"     default_area = ?," +
    					"     default_room = ?," +
    					"     source = ? " ;

    	final String statut = Profil.Administrateur.equals(persDTO.getProfil()) ? "administrateur" : "utilisateur";
    	getEntityManagerGrr().createNativeQuery(queryCreateUser)
    	.setParameter(1, persDTO.getUid())
    	.setParameter(2, persDTO.getNom())
    	.setParameter(3, persDTO.getPrenom())
    	.setParameter(4, persDTO.getEmail())
    	.setParameter(5, statut)
    	.setParameter(6, "actif")
    	.setParameter(7, idEtablissementPrincipal)
    	.setParameter(8, 0)
    	.setParameter(9, 0)
    	.setParameter(10, 0)
    	.setParameter(11, "ext")
    	.executeUpdate();


    	return persDTO.getUid();
    }
    
    private String updatePersonne(PersonneDTO persDTO, Integer idEtablissementPrincipal) {
    	final String queryCreateUser =
    			"UPDATE " + prefixeGrr + "utilisateurs" +
    					" SET nom    = ?, " +
    					"     prenom = ?, " +
    					"     email  = ?, " +
    					"	  statut = ?," +
    					"     etat   = ?," +
    					"     default_etablissement = ?," +
    					"     source = ? " +
    					" WHERE login = ? ";

    	final String statut = Profil.Administrateur.equals(persDTO.getProfil()) ? "administrateur" : "utilisateur";

    	getEntityManagerGrr().createNativeQuery(queryCreateUser)
    	.setParameter(1, persDTO.getNom())
    	.setParameter(2, persDTO.getPrenom())
    	.setParameter(3, persDTO.getEmail())
    	.setParameter(4, statut)
    	.setParameter(5, "actif")
    	.setParameter(6, idEtablissementPrincipal)
    	.setParameter(7, "ext")
    	.setParameter(8, persDTO.getUid())
    	.executeUpdate();

    	return persDTO.getUid();
    }
    
    private Integer createEtablissement(EtablissementDTO structureDTO) throws CrlrException {
    	final String queryCreate =
    			"INSERT INTO " + prefixeGrr + "etablissement (code, shortname, name, adresse, ville, codepostal) VALUES (?,?,?,?,?,?)";

    	getEntityManagerGrr().createNativeQuery(queryCreate)
    		.setParameter(1, structureDTO.getCode())
    		.setParameter(2, StringUtils.truncate(structureDTO.getNomCourt(), 30))
    		.setParameter(3, StringUtils.truncate(structureDTO.getNomLong() , 50))
    		.setParameter(4, StringUtils.truncate(structureDTO.getAdresse(),50))
    		.setParameter(5, StringUtils.truncate(structureDTO.getVille(),50))
    		.setParameter(6, StringUtils.truncate(structureDTO.getCodePostal(),50))
    		.executeUpdate();

    	return getIdEtablissement(structureDTO.getCode());
	}
    
    private Integer updateEtablissement(EtablissementDTO structureDTO,Integer id) throws CrlrException {
    	final String queryCreate =
    			"UPDATE " + prefixeGrr + "etablissement SET code = ?, shortname = ?, name = ?, adresse = ?, ville =?, codepostal = ? WHERE id = ?";

    	getEntityManagerGrr().createNativeQuery(queryCreate)
    		.setParameter(1, structureDTO.getCode())
    		.setParameter(2, StringUtils.truncate(structureDTO.getNomCourt(),30))
    		.setParameter(3, StringUtils.truncate(structureDTO.getNomLong() ,50))
    		.setParameter(4, StringUtils.truncate(structureDTO.getAdresse(),50))
    		.setParameter(5, StringUtils.truncate(structureDTO.getVille(),50))
    		.setParameter(6, StringUtils.truncate(structureDTO.getCodePostal(),50))
    		.setParameter(7, id)
    		.executeUpdate();

    	return getIdEtablissement(structureDTO.getCode());
	}
    
    private BigInteger createSite(EtablissementDTO structureDTO) throws CrlrException {
    	final String queryCreate =
    			"INSERT INTO " + prefixeGrr + "site (sitecode,sitename) VALUES (?,?)";

    	final String nomSite = nomSiteParDefaut.replace("%nomCourt%", structureDTO.getNomCourt());
    	
        getEntityManagerGrr().createNativeQuery(queryCreate).setParameter(1, structureDTO.getCode())
            .setParameter(2, StringUtils.truncate(nomSite, 50)).executeUpdate();

        return getIdSite(structureDTO.getCode());
	}
    
    private void createJointureEtablissementSite(Integer idEtablissement, BigInteger idSite) throws CrlrException {
    	final String queryCreate =
    			"INSERT INTO " + prefixeGrr + "j_etablissement_site (id_etablissement, id_site) VALUES (?,?)";

        getEntityManagerGrr().createNativeQuery(queryCreate)
        	.setParameter(1, idEtablissement)
            .setParameter(2, idSite).executeUpdate();
	}
    
    
    
    /**
     * Crée une area dans un site .
     *
     * @param idSite l'id du site de cette area
     * @param nom le nom de l'area
     *
     * @throws CrlrException .
     */
    private BigInteger createArea(String nom)
                     throws CrlrException {
        final String queryCreateArea =
            "INSERT INTO " + prefixeGrr +
            "area (area_name,access, ip_adr, morningstarts_area,eveningends_area," +
            "resolution_area,duree_par_defaut_reservation_area, display_days  ) " +
            "VALUES (?,?, '', 8,19,900,900, 'yyyyyyy')";

        getEntityManagerGrr().createNativeQuery(queryCreateArea)
            .setParameter(1, nom).setParameter(2, StringUtils.isEmpty(typeDomainesDefaut)? "" : typeDomainesDefaut)
            .executeUpdate();

        final String queryGetidArea = "select LAST_INSERT_ID()";
        final BigInteger idArea =
            (BigInteger) getEntityManagerGrr().createNativeQuery(queryGetidArea)
                             .getSingleResult();
        return idArea;
    }
    
    private void createJointureSiteArea(BigInteger idSite, BigInteger idArea) throws CrlrException {
    	final String queryCreate =
    			"INSERT INTO " + prefixeGrr + "j_site_area (id_site,id_area) VALUES (?,?)";

        getEntityManagerGrr().createNativeQuery(queryCreate)
        	.setParameter(1, idSite)
            .setParameter(2, idArea).executeUpdate();
	}
    

    ////////////////////////////////////////////////
    // MODIFICATION RECIA | DEBUT | 2012-04-17
    ////////////////////////////////////////////////
    private void createStructureRegroupement(String uaiSecondaire, String uaiPrincipal) throws CrlrException {
    	final String queryCreate =
    			"INSERT INTO " + prefixeGrr + "etablissement_regroupement" +
                " (code_etablissement_secondaire, code_etablissement_principal) " +
                " VALUES (?,?)";

        getEntityManagerGrr().createNativeQuery(queryCreate)
        	.setParameter(1, uaiSecondaire)
            .setParameter(2, uaiPrincipal)
            .executeUpdate();
	}

    private void deleteStructureRegroupement(String uaiSecondaire) {
    	final String queryDelete =
    			"DELETE FROM " + prefixeGrr + "etablissement_regroupement" +
    					" WHERE code_etablissement_secondaire = ? ";
    	getEntityManagerGrr().createNativeQuery(queryDelete)
    	.setParameter(1, uaiSecondaire)
    	.executeUpdate();
	}
    ////////////////////////////////////////////////
    // MODIFICATION RECIA | FIN
    ////////////////////////////////////////////////

	//    /***************** Getter simples sur la BDD ************************************/
 
    /**
     * Recherche si l'utilisateur existe dans la BDD de Grr.
     *
     * @param uid l'uid de l'utilisateur
     *
     * @return vrai sss l'utilisateur existe
     */
    @SuppressWarnings("unchecked")
    private boolean existUser(String uid) {
        final String queryExistUser =
            "SELECT 1 from " + prefixeGrr + "utilisateurs where login = ?";
        final List<Integer> liste =
            getEntityManagerGrr().createNativeQuery(queryExistUser).setParameter(1, uid)
                .getResultList();

        return !CollectionUtils.isEmpty(liste);
    }
    
    /**
     * Recherche l'id d'un site .
     *
     * @param code le code du site.
     *
     * @return l'id du site
     *
     * @throws CrlrException si le site n'existe pas ou que trop de site sont
     *         associés à ce code.
     */
    @SuppressWarnings("unchecked")
    private BigInteger getIdSite(String code) throws CrlrException {
        final String queryExistStructure =
            "SELECT id from " + prefixeGrr + "site where sitecode = ?";
        final List<Integer> liste =
            getEntityManagerGrr().createNativeQuery(queryExistStructure)
                .setParameter(1, code).getResultList();

        if (CollectionUtils.isEmpty(liste)) {
            return BigInteger.ZERO;
        } else if (CollectionUtils.size(liste) > 1) {
            throw new CrlrException("Trop de sites associés au code " + code);
        } else {
            return BigInteger.valueOf(liste.get(0).longValue());
        }
    }
    
    /**
     * Recherche l'id d'un établissement.
     *
     * @param code le code de l'etablissement
     *
     * @return l'id de l'établissement
     *
     * @throws CrlrException si l'etablissement n'existe pas ou que trop d'établissement sont
     *         associés à ce code
     */
    @SuppressWarnings("unchecked")
    private Integer getIdEtablissement(String code) throws CrlrException {
        final String queryExistStructure =
            "SELECT id from " + prefixeGrr + "etablissement WHERE code = ?";
        final List<Integer> liste =
            getEntityManagerGrr().createNativeQuery(queryExistStructure)
                .setParameter(1, code).getResultList();

        if (CollectionUtils.isEmpty(liste)) {
            return 0;
        } else if (CollectionUtils.size(liste) > 1) {
            throw new CrlrException("Trop de sites associés au code " + code);
        } else {
            return liste.get(0);
        }
    }
    
    
    private BigInteger getNbSiteParEtablissement(Integer idEtablissement) throws CrlrException {
        final String queryNbSite =
            "SELECT count(id_site) from " + prefixeGrr + "j_etablissement_site WHERE id_etablissement = ?";

        return 
            (BigInteger) getEntityManagerGrr().createNativeQuery(queryNbSite)
                .setParameter(1, idEtablissement).getSingleResult();

    }

	@SuppressWarnings("unchecked")
	public List<String> findListeCodesEtablissements() {
		final String queryListeEtab =
	            "SELECT code from " + prefixeGrr + "etablissement ";

	        return 
	            getEntityManagerGrr().createNativeQuery(queryListeEtab)
	                .getResultList();
	}

    ////////////////////////////////////////////////
    // MODIFICATION RECIA | DEBUT | 2012-04-19
    ////////////////////////////////////////////////
    /**
     * Recherche l'id d'un etablissement principal, à partir du code 
     * d'un potentiel etablissement secondaire
     *
     * @param code le code du potentiel etablissement secondaire
     *
     * @return l'id de l'etablissement principal
     *
     * @throws CrlrException si l'etablissement n'existe pas ou que trop d'etablissements
     *          principaux sont associes a ce code
     */
    @SuppressWarnings("unchecked")
    private Integer getIdEtablissementPrincipal(String codeEtablissementSecondaire) throws CrlrException {
        final String queryStructurePrincipale = "SELECT ge.id from " + prefixeGrr + "etablissement ge,"
                                                + " " + prefixeGrr + "etablissement_regroupement ger"
                                                + " WHERE ge.code = ger.code_etablissement_principal"
                                                + " AND ger.code_etablissement_secondaire = ?";
        final List<Integer> liste = getEntityManagerGrr().createNativeQuery(queryStructurePrincipale)
                                    .setParameter(1, codeEtablissementSecondaire)
                                    .getResultList();

        if (CollectionUtils.isEmpty(liste)) {
            return 0;
        } else if (CollectionUtils.size(liste) > 1) {
            throw new CrlrException("Trop de sites principaux associés au code " + codeEtablissementSecondaire);
        } else {
            return liste.get(0);
        }
    }
    ////////////////////////////////////////////////
    // MODIFICATION RECIA | FIN
    ////////////////////////////////////////////////
	
	
	/******************** Methodes pour la migration  ****************/
	public void propageMigration(String uai,
			DataEtablissementDTO datasEtablissement) throws CrlrException {
		Integer idEtablissement = getIdEtablissement(uai);
		Map<String, BigInteger>[] mapsTypeResa = migreTypeArea(idEtablissement, datasEtablissement.getTypesResa());
		migreCalendar(idEtablissement, datasEtablissement.getDays());
		
		for (SiteDTO site : datasEtablissement.getSites()){
			migreSite(idEtablissement, site, mapsTypeResa);
		}
		
		
	}

	private void migreSite(Integer idEtablissement, SiteDTO site, Map<String, BigInteger>[] mapsTypeResa) throws CrlrException {
		BigInteger idSite = getNextIdForTable("site");
		if (site.getSitecode() != null ){
			final String queryInsertSite =
		            "INSERT INTO " + prefixeGrr + "site SET " +
		            "id = "+idSite +", "+
		            "sitecode = '"+site.getSitecode()+"' , "+
		            "sitename = '"+correctQuote(site.getSitename())+"' , "+
		            "adresse_ligne1 = '"+correctQuote(StringUtils.trimToBlank(site.getAdresse_ligne1()))+"' , "+
		            "adresse_ligne2 = '"+correctQuote(StringUtils.trimToBlank(site.getAdresse_ligne2()))+"' , "+
		            "adresse_ligne3 = '"+correctQuote(StringUtils.trimToBlank(site.getAdresse_ligne3()))+"' , "+
		            "cp = '"+correctQuote(StringUtils.trimToBlank(site.getCp()))+"' , "+
		            "ville = '"+correctQuote(StringUtils.trimToBlank(site.getVille()))+"' , "+
		            "pays = '"+correctQuote(StringUtils.trimToBlank(site.getPays()))+"' , "+
		            "tel = '"+correctQuote(StringUtils.trimToBlank(site.getTel()))+"' , "+
		            "fax = '"+correctQuote(StringUtils.trimToBlank(site.getFax()))+"'";
			getEntityManagerGrr().createNativeQuery(queryInsertSite).executeUpdate();
		} else {
			final String queryInsertSite =
		            "INSERT INTO " + prefixeGrr + "site SET " +
		            "id = "+idSite +", "+
		            "sitecode = '"+idSite+"' , "+
		            "sitename = 'Site par défaut' ";
			getEntityManagerGrr().createNativeQuery(queryInsertSite).executeUpdate();
		}
		
		createJointureEtablissementSite(idEtablissement, idSite);
		
		for (String login : site.getUsersAdmin()){
			final String queryInsertUserAdmin =
		            "INSERT INTO " + prefixeGrr + "j_useradmin_site SET " +
		            "login = '"+login +"', "+
		            "id_site = " +idSite ;
			getEntityManagerGrr().createNativeQuery(queryInsertUserAdmin).executeUpdate();
		}
		
		for (DomaineDTO domaine : site.getDomaines()){
			migreDomaine(domaine, idSite, mapsTypeResa);
		}
		
	}

	private void migreDomaine(DomaineDTO domaine, BigInteger idSite, Map<String, BigInteger>[] mapsTypeResa) throws CrlrException {
		BigInteger idDomaine = getNextIdForTable("area");
		String oldId = domaine.getId_type_par_defaut();
		String nvId = oldId;
		if (! oldId.equals("-1")){
			nvId = mapsTypeResa[0].get(oldId)+"";
		}
		final String queryInsertDomaine =
		            "INSERT INTO " + prefixeGrr + "area SET " +
		            "id = "+idDomaine +", "+
		            "area_name = '"+correctQuote(StringUtils.trimToBlank(domaine.getArea_name()))+"' , "+
		            "access = '"+domaine.getAccess()+"' , "+
		            "order_display = "+domaine.getOrder_display()+" , "+
		            "ip_adr = '"+correctQuote(StringUtils.trimToBlank(domaine.getIp_adr()))+"' , "+
		            "morningstarts_area = "+StringUtils.trimToBlank(domaine.getMorningstarts_area())+" , "+
		            "eveningends_area = "+StringUtils.trimToBlank(domaine.getEveningends_area())+" , "+
		            "duree_max_resa_area = "+StringUtils.trimToBlank(domaine.getDuree_max_resa_area())+" , "+
		            "resolution_area = "+StringUtils.trimToBlank(domaine.getResolution_area())+" , "+
		            "eveningends_minutes_area = "+StringUtils.trimToBlank(domaine.getEveningends_minutes_area())+" , "+
		            "weekstarts_area = "+StringUtils.trimToBlank(domaine.getWeekstarts_area())+" , "+
		            "twentyfourhour_format_area = "+StringUtils.trimToBlank(domaine.getTwentyfourhour_format_area())+"," +
		            "calendar_default_values = '"+StringUtils.trimToBlank(domaine.getCalendar_default_values())+"'," +
		            "enable_periods = '"+StringUtils.trimToBlank(domaine.getEnable_periods())+"'," +
		            "display_days = '"+StringUtils.trimToBlank(domaine.getDisplay_days())+"'," +
		            "id_type_par_defaut = "+nvId+"," +
		            "duree_par_defaut_reservation_area = "+StringUtils.trimToBlank(domaine.getDuree_par_defaut_reservation_area())+"," +
		            "max_booking = '"+StringUtils.trimToBlank(domaine.getMax_booking())+"' " ;
			getEntityManagerGrr().createNativeQuery(queryInsertDomaine).executeUpdate();
			
			createJointureSiteArea(idSite, idDomaine);
			
			for (String login : domaine.getUsersAdmin()){
				final String queryInsertUserAdmin =
			            "INSERT INTO " + prefixeGrr + "j_useradmin_area SET " +
			            "login = '"+login +"', "+
			            "id_area = " +idDomaine ;
				getEntityManagerGrr().createNativeQuery(queryInsertUserAdmin).executeUpdate();
			}
			
			for (String login : domaine.getUsers()){
				final String queryInsertUserAdmin =
			            "INSERT INTO " + prefixeGrr + "j_user_area SET " +
			            "login = '"+login +"', "+
			            "id_area = " +idDomaine ;
				getEntityManagerGrr().createNativeQuery(queryInsertUserAdmin).executeUpdate();
			}
			
			for (PeriodeDTO periode : domaine.getPeriodes()){
				final String queryInsertPeriode =
			            "INSERT INTO " + prefixeGrr + "area_periodes SET " +
			            "nom_periode = '"+correctQuote(StringUtils.trimToBlank(periode.getNom_periode())) +"', "+
			            "num_periode = " +StringUtils.trimToBlank(periode.getNum_periode()) + ","+
			            "id_area =" + idDomaine;
				getEntityManagerGrr().createNativeQuery(queryInsertPeriode).executeUpdate();
			}
			
			for (OverloadDTO overload : domaine.getOverloads()){
				final String queryInsertOverload =
						"INSERT INTO "+ prefixeGrr + "overload SET "+
						"id_area = " + idDomaine + ", "+
						"fieldname = '" + correctQuote(StringUtils.trimToBlank(overload.getFieldname())) +"', "+
						"fieldtype = '" + correctQuote(StringUtils.trimToBlank(overload.getFieldtype())) +"', "+
						"fieldlist = '" + correctQuote(StringUtils.trimToBlank(overload.getFieldlist())) +"', "+
						"obligatoire = '"+StringUtils.trimToBlank(overload.getObligatoire()) + "', "+
						"affichage = '"+ StringUtils.trimToBlank(overload.getAffichage()) + "', "+
						"confidentiel = '" + StringUtils.trimToBlank(overload.getConfidentiel()) +"', "+
						"overload_mail = '" + correctQuote(StringUtils.trimToBlank(overload.getOverload_mail())) + "'";
				getEntityManagerGrr().createNativeQuery(queryInsertOverload).executeUpdate();
			}
		
			for (String oldIdTypeArea : domaine.getIdTypesArea()){
				BigInteger newIdTypeArea = mapsTypeResa[0].get(oldIdTypeArea);
				final String queryInsertJointureTypeAreaArea =
						"INSERT INTO "+ prefixeGrr + "j_type_area SET "+
						"id_area = " + idDomaine + ", "+
						"id_type = " +newIdTypeArea;
				getEntityManagerGrr().createNativeQuery(queryInsertJointureTypeAreaArea).executeUpdate();
			}
			
			for (RoomDTO room : domaine.getRooms()){
				migreRoom(room, idDomaine, mapsTypeResa);
			}
	}

	private void migreRoom(RoomDTO room, BigInteger idDomaine, Map<String,BigInteger>[] mapsTypeResa) {
		BigInteger idRoom = getNextIdForTable("room");
		
		final String queryCreateRoom = "INSERT INTO "+prefixeGrr+"room SET " +
				"id = "+idRoom+", "+
				"area_id = " +idDomaine + ", "+
				"room_name = ? ,"+
				"description = ?, "+
				"capacity = "+ correctQuote(StringUtils.trimToBlank(room.getCapacity())) + ", "+
				"max_booking = "+ StringUtils.trimToBlank(room.getMax_booking()) + ", "+
				"statut_room = '"+ StringUtils.trimToBlank(room.getStatut_room()) + "', "+
				"show_fic_room = '" + StringUtils.trimToBlank(room.getShow_fic_room()) + "', "+
				"picture_room = '" + correctQuote(StringUtils.trimToBlank(room.getPicture_room())) + "', "+
				"comment_room = ?, " +
				"show_comment = '" + StringUtils.trimToBlank(room.getShow_comment()) +"', "+
				"delais_max_resa_room = " + StringUtils.trimToBlank(room.getDelais_max_resa_room() )+ ", "+
				"delais_min_resa_room = " + StringUtils.trimToBlank(room.getDelais_min_resa_room()) + ", "+
				"allow_action_in_past = '" + StringUtils.trimToBlank(room.getAllow_action_in_past()) + "', "+
				"dont_allow_modify = '" + StringUtils.trimToBlank(room.getDont_allow_modify()) + "', "+
				"order_display = " + StringUtils.trimToBlank(room.getOrder_display()) + ", " +
				"delais_option_reservation = " + StringUtils.trimToBlank(room.getDelais_option_reservation()) + ", "+
				"type_affichage_reser =" + StringUtils.trimToBlank(room.getType_affichage_reser()) + ", " +
				"moderate = " + StringUtils.trimToBlank(room.getModerate()) + ",  " +
				"qui_peut_reserver_pour = '" + correctQuote(StringUtils.trimToBlank(room.getQui_peut_reserver_pour())) + "', "+
				"active_ressource_empruntee = '" + StringUtils.trimToBlank(room.getActive_ressource_empruntee()) + "', " +
				"who_can_see = " + correctQuote(StringUtils.trimToBlank(room.getWho_can_see())) + "" ;
		
		getEntityManagerGrr().createNativeQuery(queryCreateRoom)
		.setParameter(1, room.getRoom_name())
		.setParameter(2, room.getDescription())
		.setParameter(3, room.getComment_room())
		.executeUpdate();
		
		for (String login : room.getUser()){
			final String queryInsertUserAdmin =
		            "INSERT INTO " + prefixeGrr + "j_user_room SET " +
		            "login = '"+login +"', "+
		            "id_room = " +idRoom ;
			getEntityManagerGrr().createNativeQuery(queryInsertUserAdmin).executeUpdate();
		}
		
		for (String login : room.getMailuser()){
			final String queryInsertUserAdmin =
		            "INSERT INTO " + prefixeGrr + "j_mailuser_room SET " +
		            "login = '"+login +"', "+
		            "id_room = " +idRoom ;
			getEntityManagerGrr().createNativeQuery(queryInsertUserAdmin).executeUpdate();
		}
		
		Map<String, BigInteger> mapsOldLetterNewId = mapsTypeResa[1];
		for (EntryDTO entry : room.getEntries()){
			final String queryInsert =
		            "INSERT INTO " + prefixeGrr + "entry SET " +
		            "start_time =" + StringUtils.trimToBlank(entry.getStart_time()) +", "+
		            " 	end_time =" + StringUtils.trimToBlank(entry.getEnd_time()) +", "+
		            "	entry_type = "+correctQuote(StringUtils.trimToBlank(entry.getEntry_type())) +", "+
		            "	repeat_id = " + StringUtils.trimToBlank(entry.getRepeat_id())  +", "+
		            "	room_id = " + idRoom  +", "+
		            "	timestamp = '" + StringUtils.trimToBlank(entry.getTimestamp())  +"', "+
		            "	create_by ='" + correctQuote(StringUtils.trimToBlank(entry.getCreate_by()))  +"', "+
		            "	beneficiaire_ext ='" + correctQuote(StringUtils.trimToBlank(entry.getBeneficiaire_ext()))  +"', "+
		            "	beneficiaire ='" + correctQuote(StringUtils.trimToBlank(entry.getBeneficiaire()))  +"', "+
		            "	name = ?, "+
		            "	type ='"+mapsOldLetterNewId.get(entry.getType())+"', "+
 		            "	description = ?, "+
		            "	statut_entry ='" + StringUtils.trimToBlank(entry.getStatut_entry())  +"', "+
		            "	option_reservation = " +StringUtils.trimToBlank(entry.getOption_reservation())  +", "+
		            "	overload_desc ='" + correctQuote(StringUtils.trimToBlank(entry.getOverload_desc()))  +"', "+
		            "	moderate =" + StringUtils.trimToBlank(entry.getModerate() ) +", "+
		            "	jours = "+ StringUtils.trimToBlank(entry.getJours());
			getEntityManagerGrr().createNativeQuery(queryInsert)
			.setParameter(1, entry.getName())
			.setParameter(2, entry.getDescription())
			.executeUpdate();
		}
		for (EntryModerateDTO entry : room.getEntriesModerate()){
			final String queryInsert =
		            "INSERT INTO " + prefixeGrr + "entry_moderate SET " +
		            "login_moderateur = '" + correctQuote(StringUtils.trimToBlank(entry.getLogin_moderateur())) +"', "+
//		            "motivation_moderation = '" + correctQuote(StringUtils.trimToBlank(entry.getMotivation_moderation())) +"', "+
		            "motivation_moderation = ?," +
		            "start_time =" + StringUtils.trimToBlank(entry.getStart_time()) +", "+
		            " 	end_time =" + StringUtils.trimToBlank(entry.getEnd_time()) +", "+
		            "	entry_type = "+StringUtils.trimToBlank(entry.getEntry_type()) +", "+
		            "	repeat_id = " + StringUtils.trimToBlank(entry.getRepeat_id())  +", "+
		            "	room_id = " + idRoom  +", "+
		            "	timestamp = '" + StringUtils.trimToBlank(entry.getTimestamp())  +"', "+
		            "	create_by ='" + correctQuote(StringUtils.trimToBlank(entry.getCreate_by()))  +"', "+
		            "	beneficiaire_ext ='" + correctQuote(StringUtils.trimToBlank(entry.getBeneficiaire_ext()))  +"', "+
		            "	beneficiaire ='" + correctQuote(StringUtils.trimToBlank(entry.getBeneficiaire()))  +"', "+
//		            "	name ='" + correctQuote(StringUtils.trimToBlank(entry.getName()))  +"', "+
		            "	name = ?," +
		            "	type ='"+mapsOldLetterNewId.get(entry.getType()) +"', "+
// 		            "	description ='" + correctQuote(StringUtils.trimToBlank(entry.getDescription()))  +"', "+
		            "	description = ?," +
 		            "	statut_entry ='" + StringUtils.trimToBlank(entry.getStatut_entry())  +"', "+
		            "	option_reservation = " +correctQuote(StringUtils.trimToBlank(entry.getOption_reservation()))  +", "+
		            "	overload_desc ='" + correctQuote(StringUtils.trimToBlank(entry.getOverload_desc()))  +"', "+
		            "	moderate =" + StringUtils.trimToBlank(entry.getModerate())  ;
//                        getEntityManagerGrr().createNativeQuery(queryInsert).executeUpdate();
			try{getEntityManagerGrr()
                                .createNativeQuery(queryInsert)
				.setParameter(1,StringUtils.trimToBlank(entry.getMotivation_moderation()))
				.setParameter(2,StringUtils.trimToBlank(entry.getName()))
				.setParameter(3,StringUtils.trimToBlank(entry.getDescription()))
                                .executeUpdate();
                        }
                        catch(Exception e){
                             System.out.println(queryInsert);
		             throw new RuntimeException(e);
                        }
		}
		for (RepeatDTO entry : room.getRepeat()){
			final String queryInsert =
		            "INSERT INTO " + prefixeGrr + "repeat SET " +
		            "start_time =" + StringUtils.trimToBlank(entry.getStart_time()) +", "+
		 		    "end_time =" + StringUtils.trimToBlank(entry.getEnd_time()) +", "+
		 		    "rep_type ="+StringUtils.trimToBlank(entry.getRep_type()) +", "+
		 		    "end_date =" + StringUtils.trimToBlank(entry.getEnd_date()) +", "+
		 		    "rep_opt ='" + StringUtils.trimToBlank(entry.getRep_opt()) +"', "+
		 		    "	room_id = " + idRoom  +", "+
		            "	timestamp = '" + StringUtils.trimToBlank(entry.getTimestamp())  +"', "+
		            "	create_by ='" + correctQuote(StringUtils.trimToBlank(entry.getCreate_by()))  +"', "+
		            "	beneficiaire_ext ='" + correctQuote(StringUtils.trimToBlank(entry.getBeneficiaire_ext()))  +"', "+
		            "	beneficiaire ='" + correctQuote(correctQuote(StringUtils.trimToBlank(entry.getBeneficiaire())))  +"', "+
//		            "	name ='" + correctQuote(StringUtils.trimToBlank(entry.getName()))  +"', "+
		            "	name = ?," +
		            "	type ='"+mapsOldLetterNewId.get(entry.getType()) +"', "+
//		            "	description ='" + correctQuote(StringUtils.trimToBlank(entry.getDescription()))  +"', "+
		            "	description = ?," +
		            "	rep_num_weeks =" + StringUtils.trimToBlank(entry.getRep_num_weeks())  +", "+
		            "	overload_desc ='" + StringUtils.trimToBlank(entry.getOverload_desc())  +"', "+
		            "	jours = "+ entry.getJours();
//			getEntityManagerGrr().createNativeQuery(queryInsert).executeUpdate();
			getEntityManagerGrr()
				.createNativeQuery(queryInsert)
				.setParameter(1,StringUtils.trimToBlank(entry.getName()))
				.setParameter(2,StringUtils.trimToBlank(entry.getDescription()))
				.executeUpdate();
		}
		
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String,BigInteger>[] migreTypeArea(Integer idEtablissement,
			List<TypeResaDTO> typesResa) {
		//Une map pour mettre en relation les anciens id et les id dans la nouvelle BDD
		Map<String,BigInteger> mapIds = new HashMap<String, BigInteger>();
		//Une map pour mettre en relation les anciennes lettres et les id dans la nouvelle BDD
		Map<String,BigInteger> mapLetterId = new HashMap<String, BigInteger>();
		for (TypeResaDTO type : typesResa){
			BigInteger id = getNextIdForTable("type_area") ;
			
			final String queryInsertTypeArea =
					"INSERT INTO " + prefixeGrr + "type_area "+
							"SET id = "+id+","+
//							"type_name = '"+type.getType_name()+"',"+
		                                        "type_name = ?," +
							"order_display = 0,"+
							"couleur = '"+type.getCouleur()+"',"+
							"type_letter = '"+id+"',"+
							"disponible = '"+type.getDisponible()+"'";
//			getEntityManagerGrr().createNativeQuery(queryInsertTypeArea).executeUpdate();
			getEntityManagerGrr()
                                .createNativeQuery(queryInsertTypeArea)
                                .setParameter(1,type.getType_name())
                                .executeUpdate();
			
			final String queryInsertJointureTypeArea =
					"INSERT INTO " + prefixeGrr + "j_etablissement_type_area "+
							"SET id_etablissement = "+idEtablissement+","+
							"id_type_area = "+id+"";
			getEntityManagerGrr().createNativeQuery(queryInsertJointureTypeArea).executeUpdate();

			mapIds.put(type.getId(), id);
			mapLetterId.put(type.getType_letter(), id);
			
		}
		
		Map[] res = new Map[2];
		res[0] = mapIds;
		res[1] = mapLetterId;
		return res;
		
	}
	
	private void migreCalendar(Integer idEtablissement,
			List<String> days) {
		for (String day : days){
			final String queryInsertTypeArea =
					"INSERT INTO " + prefixeGrr + "j_etablissement_calendar "+
							"SET DAY = "+day+","+
							"id_etablissement = "+ idEtablissement;
			getEntityManagerGrr().createNativeQuery(queryInsertTypeArea).executeUpdate();

		}
		
	}
	
	private BigInteger getNextIdForTable(String nomTable){
		final String query = "SELECT max(id)+1 FROM "+ prefixeGrr +  nomTable ;
		BigInteger id =  (BigInteger) getEntityManagerGrr().createNativeQuery(query).getSingleResult();
		if (id == null ){
			return BigInteger.ONE; 
		} else {
			return id;
		}
	}
 
	private String correctQuote(String chaine){
		return chaine.replace("'", "\\'");
	}
}
