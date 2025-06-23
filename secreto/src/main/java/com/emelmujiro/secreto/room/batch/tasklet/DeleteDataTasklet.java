package com.emelmujiro.secreto.room.batch.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DeleteDataTasklet implements Tasklet, StepExecutionListener {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        // TODO
        log.info("종료된 방들의 불필요한 데이터 삭제할 예정");

        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

        log.info("DeleteDataTasklet start");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        log.info("DeleteDataTasklet end");
        return ExitStatus.COMPLETED;
    }
}
