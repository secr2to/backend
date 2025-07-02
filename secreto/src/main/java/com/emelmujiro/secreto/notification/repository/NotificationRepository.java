package com.emelmujiro.secreto.notification.repository;

import com.emelmujiro.secreto.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.user.id = :userId")
    Page<Notification> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select n from Notification n where n.user.id = :userId and n.readYn = :readYn")
    Page<Notification> findAllByUserIdAndReadYn(@Param("userId") Long userId,
                                                @Param("readYn") Boolean readYn,
                                                Pageable pageable);

//    @Query("select n from Notification n where " +
//            "n.user.id = :userId and " +
//            "n.generatedDate >= :startDate and " +
//            "n.generatedDate <= :endDate")
    @Query("select n from Notification n where " +
        "n.user.id = :userId and " +
        "n.generatedDate between :startDate and :endDate")
    Page<Notification> findAllByUserIdAndStartDateAndEndDate(@Param("userId") Long userId,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate,
                                                    Pageable pageable);

//    @Query("select n from Notification n where " +
//            "n.user.id = :userId and " +
//            "n.generatedDate >= :startDate and " +
//            "n.generatedDate <= :endDate and " +
//            "n.readYn = :readYn")
    @Query("select n from Notification n where " +
        "n.user.id = :userId and " +
        "n.generatedDate between :startDate and :endDate and " +
            "n.readYn = :readYn")
    Page<Notification> findAllByUserIdAndStartDateAndEndDateAndReadYn(@Param("userId") Long userId,
                                                             @Param("startDate") LocalDateTime startDate,
                                                             @Param("endDate") LocalDateTime endDate,
                                                             @Param("readYn") Boolean readYn,
                                                             Pageable pageable);
}
