package fr.recia.grr.batch.synchronisation.repository.dao;

import fr.recia.grr.batch.synchronisation.entity.dao.GrrEntryModerate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;


/**
 * GrrEntry Repository
 */
@Repository
public interface IEntryModerateRepositoryDAO extends JpaRepository<GrrEntryModerate,Integer> {

    /**
     *
     * @param dateRequis
     * @return les reservation anciennes depuis N jours
     */
    @Modifying
    @Query("delete from GrrEntryModerate r   where  r.end_time <= :dateRequis ")
    int deleteReservationAcienneNjour(@Param("dateRequis") long dateRequis);


}
