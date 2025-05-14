package com.emelmujiro.secreto.room.entity;

import com.emelmujiro.secreto.room.error.RoomErrorCode;
import com.emelmujiro.secreto.room.exception.RoomException;

public enum RoomStatus {

    WAITING,
    PROGRESS,
    TERMINATED,
    ;

    public static RoomStatus fromString(String statusStr) {

        for (RoomStatus status : RoomStatus.values()) {
            if (status.name().equalsIgnoreCase(statusStr)) {
                return status;
            }
        }

        throw new IllegalArgumentException ();
    }
}
