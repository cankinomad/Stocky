package org.berka.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestDto {


    @NotNull(message = "Id bilgisi bos birakilamaz")
    Long id;
    @NotNull(message = "Kategori bilgisi bos birakilamaz")
    Long categoryId;
    @NotNull(message = "Depo bilgisi bos birakilamaz")
    Long storageId;
    @NotNull(message = "Birim bilgidi bos birakilamaz")
    Long unitId;
    @NotBlank(message = "isim bilgisi bos birakilamaz")
    String name;
    @NotNull(message = "Miktar bilgisi bos birakilamaz")
    Long amount;
}
