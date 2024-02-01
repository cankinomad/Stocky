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
public class ChangePasswordRequestDto {
    @NotBlank(message = "token bos birakilamaz")
    String token;

    @NotBlank(message = "Şifre boş bırakılamaz")
    @Size(min = 4,max = 32, message = "Sifre en az 4, en fazla 32 karakterden olusmalidir.")
    String oldPassword;
    @NotBlank(message = "Sifre bos birakilamaz")
    @Size(min = 4,max = 32, message = "Sifre en az 4, en fazla 32 karakterden olusmalidir.")
    String password;
}
