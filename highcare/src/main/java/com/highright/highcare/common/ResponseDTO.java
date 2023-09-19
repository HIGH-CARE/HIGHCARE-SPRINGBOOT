package com.highright.highcare.common;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResponseDTO {

    private int status;          // 상태코드값
    private String message;     // 응답 매세지
    private Object data;        // 응답 데이터

    public ResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Builder
    public ResponseDTO(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
