package org.omsf.error.Exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * packageName    : org.omsf.error.code
 * fileName       : ErrorCode
 * author         : Yeong-Huns
 * date           : 2024-06-18
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-06-18        Yeong-Huns       최초 생성
 */
@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E1", "페이지가 응답하지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E2", "인가받지 않은 호출입니다."),
    NOT_VALID_JSON(HttpStatus.METHOD_NOT_ALLOWED, "E2", "JSON 형식을 기대했지만, JSON 형식으로 변환할 수 없는 값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E3", "서버 에러가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "E4", "존재하지 않는 대상입니다."),
    MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "E5", "지원하지 않는 형식의 입력값입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "E4", "찾을수 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "E4", "등록되지 않은 유저입니다."),
    NOT_FOUND_STORE_OWNER(HttpStatus.NOT_FOUND, "E4", "점포 주인이 등록되지 않았습니다."),
    NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "E4", "등록된 점포가 없습니다."),
    NOT_FOUND_PHOTO(HttpStatus.NOT_FOUND, "E4", "사진을 찾을 수 없습니다."),
    NOT_FOUND_MESSAGE(HttpStatus.NOT_FOUND, "no_record", "메세지 기록이 없습니다."),
    NOT_ALLOWED_REQUEST(HttpStatus.METHOD_NOT_ALLOWED, "E2", "로직상 있을 수 없는 요청입니다."),
    NOT_FOUND_STORENO(HttpStatus.NOT_FOUND, "E4", "해당 주소로 등록된 storeNo가 없습니다."),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "E4", "등록된 주문이 없습니다."),
    ;


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.message = message;
        this.code = code;
        this.status = status;
    }
}
