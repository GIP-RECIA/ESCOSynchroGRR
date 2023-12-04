package fr.recia.grr.batch.tasklet;

import fr.recia.grr.batch.synchronisation.entity.dao.GrrArea;
import fr.recia.grr.batch.synchronisation.entity.dao.GrrEtablissement;
import fr.recia.grr.batch.synchronisation.entity.dao.GrrSite;
import fr.recia.grr.batch.synchronisation.entity.dao.GrrUtilisateurs;
import fr.recia.grr.batch.synchronisation.repository.dao.IUtilisateursRepositoryDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SupprimerAncienAdminAreaTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(SupprimerAncienAdminAreaTasklet.class);

    @Autowired
    private IUtilisateursRepositoryDAO utilisateursRepositoryDAO;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        int cpt = 0;
        Collection<GrrUtilisateurs> response = utilisateursRepositoryDAO.findAll();
        List<GrrArea> area_to_remove = new ArrayList<>();
        for(GrrUtilisateurs utilisateur : response){
            // On vérifie qu'il est bien dans l'établissement assoicé aux domaines dont il est admin
            for(GrrArea grrArea : utilisateur.getGrr_j_useradmin_area()){
                // Pour cela on récpère le site du domaine
                for(GrrSite grrSite : grrArea.getGrr_j_area_site()){
                    // Et l'établissement associé au site
                    for(GrrEtablissement grrEtablissement : grrSite.getGrr_j_site_etablissement()){
                        // On peut alors faire la comparaison
                        if(!utilisateur.getGrr_j_user_etablissement().contains(grrEtablissement)
                           && !utilisateur.getGrr_j_useradmin_etablissement().contains(grrEtablissement)){
                            area_to_remove.add(grrArea);
                        }
                    }
                }
            }
            // Une fois qu'on a tout parcouru, on retire les area dont l'utilisateur ne doit plus être admin
            if(!area_to_remove.isEmpty()){
                for(GrrArea grrArea: area_to_remove){
                    log.info("Suppression de la ligne pour login="+utilisateur.getLogin()+" et area_id="+grrArea.getId()
                            +" dans la table grr_j_useradmin_area car l'utilisateur a changé d'établissement");
                    utilisateur.remove_Grr_j_user_admin_area(grrArea);
                    cpt += 1;
                }
                area_to_remove.clear();
            }
        }
        chunkContext.getStepContext().getStepExecution().setWriteCount(cpt);
        return RepeatStatus.FINISHED;
    }
}
