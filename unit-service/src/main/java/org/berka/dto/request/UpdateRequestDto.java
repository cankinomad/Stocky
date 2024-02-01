package org.berka.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestDto {

    @NotNull(message = "id bos birakilamaz")
    Long id;
    @NotBlank(message = "isim bos birakilamaz")
    String name;
}
