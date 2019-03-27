package fr.recia.grr.batch.config;

import fr.recia.grr.batch.listener.ExecutionListenerJob;
import fr.recia.grr.batch.listener.ExecutionListenerStep;
import fr.recia.grr.batch.processor.ProcessorMisAJourEtablissement;
import fr.recia.grr.batch.reader.ReaderDAOAllEtablissement;
import fr.recia.grr.batch.synchronisation.entity.dao.GrrEtablissement;
import fr.recia.grr.batch.synchronisation.entity.ldap.ODMStructure;
import fr.recia.grr.batch.writer.WriterMisAjourEtablissement;
import fr.recia.grr.utils.DateBatch;
import fr.recia.grr.utils.DateDerniereMiseAJourLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Profile("!BatchMigration")
@Configuration
public class BatchConfig {
    private static final Logger log = LoggerFactory.getLogger(BatchConfig.class);

    /*
     * ===============================================
     * Propriétés de la classe
     * ===============================================
     */



    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ResourceLoader resourceLoader;
    private DateBatch dateBatch;

    /*
     * ===============================================
     * Configuration du batch
     * ===============================================
     */



    @PostConstruct
    private void postInitialize()  {
        Optional<String> s = null;
        try {
            s = DateDerniereMiseAJourLoader.loadDate(resourceLoader);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info("Date de derniere Execution : {}", s.orElse(null));
        //date=20190219000000Z
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyyMMddHHmmssX");
        currentDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        String format = currentDate.format(new Date());

        log.info("Date de nouvelle execution Execution : {}", format);
        dateBatch = new DateBatch(s.orElse(null), format);
    }

   @Bean
    public Job config(ExecutionListenerJob listener,
                      Step misAjourEtablissement,
                      Step endBatch)  {


        return jobBuilderFactory.get("Syncro")
                .incrementer(new RunIdIncrementer())
                .listener(listener).start(misAjourEtablissement).on(FlowExecutionStatus.FAILED.getName()).end().next(endBatch)
                .build().build();
    }

    /*
     * ===============================================
     * Step 1: Mis a jour des etablissements
     * ===============================================
     */
    @Bean
    public ReaderDAOAllEtablissement misAJourEtablissementReader(){
        return new ReaderDAOAllEtablissement(dateBatch);
    }

    @Bean
    public ProcessorMisAJourEtablissement misAJourEtablissementProcessor(){
        return new ProcessorMisAJourEtablissement();
    }

    @Bean
    public WriterMisAjourEtablissement misAJourEtablissementWriter(){
        return new WriterMisAjourEtablissement();
    }
    @Bean
    public Step misAjourEtablissement(ExecutionListenerStep listener){

        return stepBuilderFactory.get("misAjourEtablissement")
                .<ODMStructure, GrrEtablissement> chunk(1)
                .reader(misAJourEtablissementReader())
                .processor( misAJourEtablissementProcessor())
                .writer(misAJourEtablissementWriter())
                .listener(listener)
                .build();
    }





    @Bean
    public Step endBatch(ExecutionListenerStep listener){
        return stepBuilderFactory.get("endBatch")
                .tasklet(endBatchTxt())
                .listener(listener)
                .build();
    }

    private Tasklet endBatchTxt() {
        return (stepContribution, chunkContext) -> {
            log.info("Mise a jour de la date de derniere mise a jour : {} " ,dateBatch.getCurrentBatch());
            DateDerniereMiseAJourLoader.write(resourceLoader, dateBatch.getCurrentBatch());
            return RepeatStatus.FINISHED;
        };
    }
}
