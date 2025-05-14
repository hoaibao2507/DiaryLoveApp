package com.MOPR.diaryapp.DTO;

import lombok.Data;

@Data
public class DiaryNoteDTO {
    private String title;
    private String content;
    private Long userId;
}
