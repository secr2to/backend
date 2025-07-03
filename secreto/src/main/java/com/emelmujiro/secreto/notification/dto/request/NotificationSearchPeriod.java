package com.emelmujiro.secreto.notification.dto.request;

public enum NotificationSearchPeriod {
    TODAY,
    WEEK,
    ALL,
    ;

    public static NotificationSearchPeriod checkValidation(NotificationSearchPeriod period) {
        if (period == null) {
            throw new RuntimeException("잘못된 요청입니다. (TODAY, WEEK, ALL)");
        }
        return period;
    }
}
