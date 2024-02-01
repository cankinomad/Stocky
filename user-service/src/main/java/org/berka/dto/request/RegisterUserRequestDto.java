package org.berka.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserRequestDto {

    @NotBlank(message = "kullanici adi bos birakilamaz")
    String username;
    @NotBlank(message = "Sifre bos birakilamaz")
    @Size(min = 4,max = 32,message = "Sifre en az 4, en fazla 32 karakterden olusabilir")
    String password;
    @NotBlank(message = "Lutfen pozisyonunuzu giriniz")
    String title;
}
