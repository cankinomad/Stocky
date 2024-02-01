package org.berka.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestDto {
    @NotNull(message = "Id bos olamaz!")
    Long id;

    @NotBlank(message = "Kategori ismi bos birakilamaz")
    String name;

    @NotNull(message = "ana kategori ozelligini belirtmelisiniz!")
    Boolean isMainCategory;

    List<Long> categories;
}
