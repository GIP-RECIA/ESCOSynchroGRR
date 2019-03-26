package fr.recia.grr.batch.synchronisation.repository.dao;

import fr.recia.grr.batch.synchronisation.entity.dao.GrrEtablissementRegroupement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Grr_etablissement Repository
 */

@Repository
public interface IEtablissementRegroupementRepositoryDAO extends JpaRepository<GrrEtablissementRegroupement,Integer> {




}
