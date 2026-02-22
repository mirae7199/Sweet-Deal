package com.mirae.global.errorcode;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ItemErrorCode implements ErrorCode {

    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, 1100, "상품을 찾을 수 없습니다."),
    INVALID_ITEM_INFO(HttpStatus.BAD_REQUEST, 1101, "상품 정보가 유효하지 않습니다."),
    ITEM_UPDATE_DENIED(HttpStatus.FORBIDDEN, 1102, "상품 수정 권한이 없습니다."),
    FAIL_CREATE_ITEM(HttpStatus.INTERNAL_SERVER_ERROR,1103, "상품 등록에 실패했습니다."),
    ITEM_ALREADY_EXISTS(HttpStatus.CONFLICT, 1104, "상품이 이미 존재합니다."),
    ITEM_ALREADY_SOLD(HttpStatus.BAD_REQUEST, 1105, "판매된 상품은 삭제할 수 없습니다."),
    ITEM_ALREADY_DELETED(HttpStatus.BAD_REQUEST, 1106, "이미 삭제된 상품입니다."),
    ITEM_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, 1107, "이미 등록된 상품입니다."),
    INVALID_ITEM_QUANTITY(HttpStatus.BAD_REQUEST, 1108, "상품 수량은 1개 이상이어야 합니다."),
    INVALID_ITEM_NAME(HttpStatus.BAD_REQUEST, 1109, "상품 이름은 비어 있을 수 없습니다."),
    INVALID_ITEM_EXPIRED_DATE(HttpStatus.BAD_REQUEST, 1110, "유통기한은 현재 날짜 이후여야 합니다."),
    INVALID_ITEM_PRICE(HttpStatus.BAD_REQUEST, 1111, "가격은 0원 이상이어야 합니다."),
    INVALID_ITEM_STATUS_CHANGE(HttpStatus.BAD_REQUEST, 1112, "삭제되거나 판매된 상품은 상태를 변경할 수 없습니다."),
    INSUFFICIENT_ITEM_QUANTITY(HttpStatus.BAD_REQUEST, 1113, "현재 상품 재고보다 더 삭제할 수 없습니다."),
    STORE_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1114, "Store 서비스와의 통신 중 오류가 발생했습니다."),
    STORE_ID_REQUIRED(HttpStatus.BAD_REQUEST, 1115, "요청에 매장 ID가 필요합니다."),
    STORE_NOT_OWNED(HttpStatus.FORBIDDEN, 1116, "해당 매장은 사용자의 소유가 아닙니다.");

    private final HttpStatus httpStatus;
    private final Integer errorCode;
    private final String description;


}
