package com.emelmujiro.secreto.mission.batch.config;

import com.emelmujiro.secreto.mission.batch.tasklet.MissionGrantTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MissionGrantJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final MissionGrantTasklet missionGrantTasklet;

    @Bean
    public Job missionGrantJob() {

        return new JobBuilder("missionGrantJob", jobRepository)
                .start(missionGrantStep())
                .build();
    }

    @Bean
    public Step missionGrantStep() {

        return new StepBuilder("missionGrantStep", jobRepository)
                .tasklet(missionGrantTasklet, platformTransactionManager)
                .build();
    }
}
