package fr.recia.grr.batch.tasklet;

import fr.recia.grr.batch.config.BatchConfig;
import fr.recia.grr.batch.synchronisation.repository.dao.IEntryModerateRepositoryDAO;
import fr.recia.grr.batch.synchronisation.repository.dao.IEntryRepositoryDAO;
import fr.recia.grr.batch.synchronisation.repository.dao.IRepeatRepositoryDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ReservationAncienneTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(ReservationAncienneTasklet.class);

    @Autowired
    private IEntryRepositoryDAO reservationServiceDAO;

    @Autowired
    private IEntryModerateRepositoryDAO reservationModerateServiceDAO;

    @Autowired
    private IRepeatRepositoryDAO repeatServiceDAO;

    @Value("${anneeReservationTropAncienne}")
    private int anneeReservationTropAncienne;


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        final long dateActuel = Instant.now().getEpochSecond();
        final long dateRequis = dateActuel - ((long) anneeReservationTropAncienne *365*24*60*60);
        log.info("Suppression des anciennes réservations dans la table grr_entry");
        int i = reservationServiceDAO.deleteReservationAcienneNjour(dateRequis);
        log.info("Suppression des anciennes réservations dans la table grr_entry_moderate");
        i += reservationModerateServiceDAO.deleteReservationAcienneNjour(dateRequis);
        log.info("Suppression des anciennes réservations dans la table grr_repeat");
        i += repeatServiceDAO.deleteReservationAcienneNjour(dateRequis);
        chunkContext.getStepContext().getStepExecution().setWriteCount(i);
        return RepeatStatus.FINISHED;
    }
}
