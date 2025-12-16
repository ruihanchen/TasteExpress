package org.chendev.tasteexpress.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data

public class DataOverViewQueryDTO {

    private LocalDateTime begin;

    private LocalDateTime end;

}
