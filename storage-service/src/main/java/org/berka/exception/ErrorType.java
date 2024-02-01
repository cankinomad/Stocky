package org.berka.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@AllArgsConstructor
@Getter
public enum ErrorType {
    INTERNAL_ERROR_SERVER(5100,"Sunucu Hatasi",INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4100, "Parametre hatasi", HttpStatus.BAD_REQUEST),
    STORAGENAME_EXIST(4110,"Depo ismi zaten mevcut",HttpStatus.BAD_REQUEST),
    USER_NOT_CREATED(4111,"Kullanici olusturulamadi",HttpStatus.BAD_REQUEST),
    STORAGE_NOT_FOUND(4112,"Boyle bir depo bulunamadi",HttpStatus.BAD_REQUEST),
    STORAGE_CANNOT_BE_DELETED(4112,"Bu depoyu silmeye/degistirmeye hakkiniz yok",HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4114,"Kullanici adi veya sifre hatali",HttpStatus.NOT_FOUND),
    INVALID_TOKEN(4116,"Gecersiz token" ,HttpStatus.BAD_REQUEST),
    TOKEN_NOT_CREATED(4117,"Token olusturulamadi" ,HttpStatus.BAD_REQUEST),
    INSUFFICIENT_PERMISSION(4119, "Bu islemi yapmaya yetkiniz yok", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1111,"Kayıt edilmemiş.",HttpStatus.BAD_REQUEST),
    NO_DATA_FOUND(12324,"Aradiginiz kriterlerde kullanici/kullanicilar bulunamadi.",HttpStatus.BAD_REQUEST);



    private int code;
    private String message;
    HttpStatus status;
}
