package com.MOPR.diaryapp.DTO;

import lombok.Data;

import java.util.List;

@Data
public class DiaryNoteDTO {
    private Long userId;
    private String title;
    private String content;
    private List<String> emojiUrls; // danh sách URL emoji được chọn
}
