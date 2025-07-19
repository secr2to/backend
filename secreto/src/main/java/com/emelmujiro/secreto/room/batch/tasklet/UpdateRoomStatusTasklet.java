package com.emelmujiro.secreto.room.batch.tasklet;

import com.emelmujiro.secreto.notification.dto.request.SaveNotificationRequestDto;
import com.emelmujiro.secreto.notification.dto.request.SendNotificationRequestDto;
import com.emelmujiro.secreto.notification.entity.NotificationType;
import com.emelmujiro.secreto.notification.service.NotificationService;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.entity.RoomUser;
import com.emelmujiro.secreto.room.repository.RoomRepository;
import com.emelmujiro.secreto.room.repository.RoomUserRepository;
import com.emelmujiro.secreto.user.entity.User;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpdateRoomStatusTasklet implements Tasklet, StepExecutionListener {

    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;
    private final NotificationService notificationService;

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

            List<RoomUser> roomUserList = roomUserRepository.findAllByRoomIdAndStandbyYn(room.getId(), false);

            List<User> userList = new ArrayList<>();
            for(RoomUser roomUser : roomUserList) {
                userList.add(roomUser.getUser());
            }

            notificationService.sendNotification(SendNotificationRequestDto.builder()
                    .notificationType(NotificationType.ROOM_END)
                    .content(NotificationType.ROOM_END.getMessage())
                    .author(room.getName())
                    .targetId(room.getId())
                    .build());

            notificationService.saveNotification(SaveNotificationRequestDto.builder()
                    .notificationType(NotificationType.ROOM_END)
                    .author(room.getName())
                    .content(NotificationType.ROOM_END.getMessage())
                    .receiverList(userList)
                    .room(room)
                    .referenceId(room.getId())
                    .build());
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
