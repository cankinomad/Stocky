package org.berka.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequestDto {

    @NotBlank(message = "Kategori ismi bos birakilamaz!")
    String name;

    @NotNull(message = "ana kategori ozelligini belirtmelisiniz!")
    Boolean isMainCategory;

    List<Long> categories;
}
