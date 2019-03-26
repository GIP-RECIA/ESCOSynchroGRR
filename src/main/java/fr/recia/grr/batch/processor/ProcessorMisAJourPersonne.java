package fr.recia.grr.batch.processor;

import fr.recia.grr.batch.synchronisation.entity.dao.GrrEtablissement;
import fr.recia.grr.batch.synchronisation.entity.dao.GrrUtilisateurs;
import fr.recia.grr.batch.synchronisation.entity.ldap.ODMPersonne;
import fr.recia.grr.batch.synchronisation.repository.dao.IEtablissementRepositoryDAO;
import fr.recia.grr.batch.synchronisation.repository.dao.IUtilisateursRepositoryDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessorMisAJourPersonne implements ItemProcessor<ODMPersonne, GrrUtilisateurs> {
    private static final Logger log = LoggerFactory.getLogger(ProcessorMisAJourPersonne.class);

    /*
     * ===============================================
     * Propriétés de la classe
     * ===============================================
     */

    @Autowired
    private IUtilisateursRepositoryDAO utilisateursRepositoryDAO;

    @Autowired
    private IEtablissementRepositoryDAO etablissementServiceDAO;


    @Value("${statutCodeEtablissement}")
    private String statutCodeEtablissement;

    @Value("${statutUtilisateur}")
    private String statutUtilisateur;

    @Value("${statutAdministrateur}")
    private String statutAdministrateur;

    @Value("${statutAdminEtablissement}")
    private String statutAdminEtablissement;


    @Value("${etablissementPrincipal}")
    private String etablissementPrincipal;

    /*
     * ===============================================
     * synchronisation des données de ldap avec la base de donnée
     * ===============================================
     */

    @Override
    public GrrUtilisateurs process(ODMPersonne odmPpersonne) throws Exception {
        log.info("Debut Process pour l'utilisateur : ".concat(odmPpersonne.getUid()));

        Optional<GrrUtilisateurs> user = utilisateursRepositoryDAO.findById(odmPpersonne.getUid());
        //RG-4	Etablissement principal de l’utilisateur
        Pattern p5 = Pattern.compile(etablissementPrincipal);
        for (String s:odmPpersonne.getIsMemberOf()) {
            Matcher matcher = p5.matcher(s);
            while (matcher.find()) {
                odmPpersonne.setDefaultEtablissement(matcher.group(2));
                log.info("Detection d'un établissement par defaut : ".concat(matcher.group(2)));
            }
        }


        user.ifPresentOrElse(
                value -> log.info("Mise a jour de l'utilisateur ID : ".concat(String.valueOf(value.getLogin()))),
                () -> log.info("Création de l'utilisateur")
        );
        GrrUtilisateurs grrUtilisateurs = updateUtilisateur(user.orElse(new GrrUtilisateurs(odmPpersonne.getUid())), odmPpersonne);

        if(grrUtilisateurs.getDefault_etablissement()==null){
            log.error("Le code etablissement detecté pour l'utilisateur ".concat(odmPpersonne.getUid()).concat(" est introuvable en base - Code : ").concat(odmPpersonne.getDefaultEtablissement()));
            return null;
        }

        /*
        •	Si l’utilisateur possède le statut utilisateur (RG-5),
        les tables de jointure grr_j_user_etablissement et grr_j_useradmin_etablissement doivent être mise à jour de la manière suivante
         */
        Pattern p = Pattern.compile(statutAdministrateur);
        Pattern p2 = Pattern.compile(statutUtilisateur);
        Pattern p3 = Pattern.compile(statutCodeEtablissement);
        Pattern p4 = Pattern.compile(statutAdminEtablissement);

        boolean gotEtabl = false;
        for (String s:odmPpersonne.getIsMemberOf()) {
            boolean isAdmin = false;
            boolean isAdminEtablissement = false;
            boolean isUtilisateur = false;
            String codeEtablissement = null;

            if (p.matcher(s).matches()) {
                isAdmin=true;
                log.info("Detection d'un statut admin :  admin:GRR:central ");
                grrUtilisateurs.setStatut("isAdmin");
            }
            if (p4.matcher(s).matches()) {
                log.info("Detection d'un statut utilisateur :  admin:GRR:local ");
                isAdminEtablissement = true;
            }
            if (p2.matcher(s).matches()) {
                log.info("Detection d'un statut utilisateur : applications:GRR  ");
                isUtilisateur =true;
            }
            Matcher matcher = p3.matcher(s);
            while (matcher.find()) {
                codeEtablissement= matcher.group(2);
                log.info("Detection d'un code établissement : ".concat(matcher.group(2)));
            }


            if(codeEtablissement !=null){
                gotEtabl=true;

                Optional<GrrEtablissement> grrEtablissement = etablissementServiceDAO.findByCode(codeEtablissement);
                boolean finalIsAdminEtablissement = isAdminEtablissement;
                boolean finalIsUtilisateur = isUtilisateur;
                boolean finalIsAdmin = isAdmin;
                String finalCodeEtablissement = codeEtablissement;
                grrEtablissement.ifPresentOrElse(value -> {

                // Recuperer letablissmeent principal - Effectuer également l’opération avec l’établissement principal associé à l’établissement en cours s’il existe (RG-7).
                    Set<GrrEtablissement> principal = new HashSet<>();
                    value.getEtablissements_principal().forEach(grrEtablissementRegroupement ->  etablissementServiceDAO.findByCode(grrEtablissementRegroupement.getCode_etablissement_principal()).ifPresent(principal::add));

                    principal.forEach(grrEtablissement1 -> log.info("Detection d'un établissement principal - Code: ".concat(grrEtablissement1.getCode())));

                     /*
                        Retirer des tables grr_j_user_etablissement et grr_j_useradmin_etablissement les enregistrements concernant l’utilisateur.
                         */
                    if(!finalIsAdmin){
                        grrUtilisateurs.getGrr_j_useradmin_etablissement().clear();
                        grrUtilisateurs.getGrr_j_user_etablissement().clear();
                    }

                     /*
                grr_j_useradmin_etablissement
                 */
                    if(finalIsAdminEtablissement){
                        log.info("Ajout a useradmin_etablissement ");
                        grrUtilisateurs.getGrr_j_useradmin_etablissement().add(value);
                        grrUtilisateurs.getGrr_j_useradmin_etablissement().addAll(principal);
                    }
                      /*
                grr_j_user_etablissement
                 */
                    if(finalIsUtilisateur){
                        log.info("Ajout a user_etablissement");
                        grrUtilisateurs.getGrr_j_user_etablissement().add(value);
                        grrUtilisateurs.getGrr_j_user_etablissement().addAll(principal);
                    }
                },() ->  log.error("Le code etablissement detecté est introuvable en base - Code : ".concat(finalCodeEtablissement)));
            }
        }

        if(!gotEtabl) {
            log.error("Aucun code etablissement detecté Login : ".concat(odmPpersonne.getUid()));
        }

        log.info("Fin Process pour l'utilisateur : ".concat(odmPpersonne.getUid()));
        return grrUtilisateurs;
    }

    private GrrUtilisateurs updateUtilisateur(GrrUtilisateurs user, ODMPersonne odmPpersonne) {
        user.setNom(odmPpersonne.getNom());
        user.setPrenom(odmPpersonne.getPrenom());
        user.setEmail(odmPpersonne.getEmail());
        user.setStatut("utilisateur");
        user.setEtat("actif");
        Optional<GrrEtablissement> grrEtablissement = etablissementServiceDAO.findByCode(odmPpersonne.getDefaultEtablissement());
        grrEtablissement.ifPresent(grrEtablissement1 -> user.setDefault_etablissement((grrEtablissement1.getId().shortValue())));
        user.setSource("ext");
        return user;
    }
}
