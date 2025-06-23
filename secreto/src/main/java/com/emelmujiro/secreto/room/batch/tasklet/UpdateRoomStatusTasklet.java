package com.emelmujiro.secreto.room.batch.tasklet;

import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdateRoomStatusTasklet implements Tasklet, StepExecutionListener {

    private final RoomRepository roomRepository;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        LocalDateTime now = LocalDateTime.now().withNano(0);

        List<Room> findTerminatedRoomList = roomRepository.findAllByEndDate(now);

        if(findTerminatedRoomList.isEmpty()) {
            log.info("종료할 방 없음, 실행 시간 : " + now);
            return RepeatStatus.FINISHED;
        }

        for(Room room : findTerminatedRoomList) {
            room.terminateRoom();
        }

        log.info("총 {}개의 방 종료. 실행 시간 : {}", findTerminatedRoomList.size(), now);

        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

        log.info("UpdateRoomStatusTasklet start");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        log.info("UpdateRoomStatusTasklet end");
        return ExitStatus.COMPLETED;
    }
}
