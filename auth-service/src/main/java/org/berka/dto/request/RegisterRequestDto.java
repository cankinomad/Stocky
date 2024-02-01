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
public class RegisterRequestDto {

    @NotBlank(message = "username bos birakilamaz.")
    private String username;
    @NotBlank(message = "Sifre bos birakilamaz.")
    @Size(min = 4,max = 32, message = "Sifre en az 4, en fazla 32 karakterden olusmalidir.")
    private String password;

    @NotBlank(message = "Sifre tekrari bos birakilamaz!")
    private String rePassword;

}
