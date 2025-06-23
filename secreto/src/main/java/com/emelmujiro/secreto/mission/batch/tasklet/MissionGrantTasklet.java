package com.emelmujiro.secreto.mission.batch.tasklet;

import com.emelmujiro.secreto.mission.entity.RoomMission;
import com.emelmujiro.secreto.mission.entity.RoomMissionHistory;
import com.emelmujiro.secreto.mission.repository.RoomMissionHistoryRepository;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.entity.RoomStatus;
import com.emelmujiro.secreto.room.repository.RoomMissionRepository;
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
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Component
public class MissionGrantTasklet implements Tasklet, StepExecutionListener {

    private final RoomRepository roomRepository;
    private final RoomMissionRepository roomMissionRepository;
    private final RoomMissionHistoryRepository roomMissionHistoryRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        List<Room> findProgressRoomList = roomRepository.findAllByRoomStatus(RoomStatus.PROGRESS);

        LocalDateTime today = LocalDateTime.now();
        for(Room room : findProgressRoomList) {
            int missionPeriod = room.getMissionPeriod();

            long daysElapsed = ChronoUnit.DAYS.between(room.getStartDate(), today);

            if (daysElapsed >= 0 && daysElapsed % missionPeriod == 0) {

                List<RoomMission> missionList = roomMissionRepository.findAllByRoomIdAndExecuteYn(room.getId(), false);

                if (missionList.isEmpty()) {
                    log.info("Room {}: 미수행 미션이 없습니다.", room.getId());
                    continue;
                }

                RoomMission selectedMission = missionList.get(new Random().nextInt(missionList.size()));

                RoomMissionHistory history = RoomMissionHistory.builder()
                        .room(room)
                        .content(selectedMission.getContent())
                        .createDate(today)
                        .build();

                roomMissionHistoryRepository.save(history);
            }
        }

        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

        log.info("missionGrantTasklet start");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        log.info("missionGrantTasklet end");
        return ExitStatus.COMPLETED;
    }
}
