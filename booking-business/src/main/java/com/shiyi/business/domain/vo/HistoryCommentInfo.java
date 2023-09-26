package com.shiyi.business.domain.vo;

import lombok.Data;

@Data
public class HistoryCommentInfo {
    private String taskName;
    private String comment;
    private String startTime;
    private String endTime;
    private String durationInMillis;
}
