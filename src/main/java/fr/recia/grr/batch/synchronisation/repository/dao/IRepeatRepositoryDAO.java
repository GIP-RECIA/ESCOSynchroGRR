package fr.recia.grr.batch.synchronisation.repository.dao;

import fr.recia.grr.batch.synchronisation.entity.dao.GrrRepeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * GrrRepeat Repository
 */
@Repository
public interface IRepeatRepositoryDAO extends JpaRepository<GrrRepeat,Integer> {

    /**
     *
     * @param dateRequis
     * @return les reservation anciennes depuis N jours
     */
    @Modifying
    @Query("delete from GrrRepeat r   where  r.end_date <= :dateRequis ")
    int deleteReservationAcienneNjour(@Param("dateRequis") long dateRequis);


}
