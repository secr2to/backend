package com.emelmujiro.secreto.room.batch.config;

import com.emelmujiro.secreto.room.batch.tasklet.DeleteDataTasklet;
import com.emelmujiro.secreto.room.batch.tasklet.UpdateRoomStatusTasklet;
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
@Configuration
@RequiredArgsConstructor
public class RoomTerminateJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final UpdateRoomStatusTasklet updateRoomStatusTasklet;
    private final DeleteDataTasklet deleteDataTasklet;

    @Bean
    public Job roomTerminateJob(){

        return new JobBuilder("roomTerminateJob", jobRepository)
                .start(updateRoomStatusEndStep())
                .next(deleteDataStep())
                .build();
    }

    @Bean
    public Step updateRoomStatusEndStep(){
        return new StepBuilder("updateRoomStatusEndStep", jobRepository)
                .tasklet(updateRoomStatusTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step deleteDataStep(){
        return new StepBuilder("deleteDataStep", jobRepository)
                .tasklet(deleteDataTasklet, transactionManager)
                .build();
    }
}
