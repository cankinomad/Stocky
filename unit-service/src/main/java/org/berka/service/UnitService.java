package org.berka.service;

import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.exception.ErrorType;
import org.berka.exception.UnitManagerException;
import org.berka.rabbitmq.producer.UnitIdProducer;
import org.berka.repository.IUnitRepository;
import org.berka.repository.entity.Unit;
import org.berka.utility.ServiceManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UnitService extends ServiceManager<Unit, Long> {

    private final IUnitRepository repository;

    private final UnitIdProducer unitIdProducer;



    public UnitService(IUnitRepository repository, UnitIdProducer unitIdProducer) {
        super(repository);
        this.repository = repository;
        this.unitIdProducer = unitIdProducer;
    }




    @PostConstruct
    public void defaultUnit(){
        boolean existsById = repository.existsById(1L);
        if(!existsById) {
            save(Unit.builder().name("Diger").build());
        }
    }

    public Unit createUnit(CreateRequestDto dto) {

        boolean existsByName = repository.existsByName(dto.getName());
        if (existsByName) {
            throw new UnitManagerException(ErrorType.UNIT_EXIST);
        }
        return  save(Unit.builder().name(dto.getName()).build());
    }

    public Boolean approveUnit(Long id) {
        return repository.existsById(id);
    }

    public MessageResponseDto updateUnit(UpdateRequestDto dto) {
        if(dto.getId()==1L){
            throw new UnitManagerException(ErrorType.DEFAULT_CANNOT_BE_DELETED);
        }
        Unit unit = repository.findById(dto.getId()).orElseThrow(() -> new UnitManagerException(ErrorType.UNIT_NOT_FOUND));
        if (!unit.getName().equals(dto.getName())  && repository.existsByName(dto.getName())) {
            throw new UnitManagerException(ErrorType.UNIT_EXIST);
        }
        if (unit.getName().equals(dto.getName())) {
            return new MessageResponseDto("Herhangi bir degisiklik yapilmadi");
        }
        unit.setName(dto.getName());
        update(unit);
        return new MessageResponseDto("Birim bilgisi basariyla guncellenmistir");
    }

    public MessageResponseDto deleteUnit(Long id) {
        if (id == 1L) {
            throw new UnitManagerException(ErrorType.DEFAULT_CANNOT_BE_DELETED);
        }
        Unit unit = repository.findById(id).orElseThrow(() -> new UnitManagerException(ErrorType.UNIT_NOT_FOUND));

        String name = unit.getName();
        unitIdProducer.unitId(id);
        delete(unit);
        return new MessageResponseDto(name+" birimi basariyla silinmistir");
    }

    public MessageResponseDto getName(Long id) {
        Unit unit = repository.findById(id).orElseThrow(() -> new UnitManagerException(ErrorType.UNIT_NOT_FOUND));
        return new MessageResponseDto(unit.getName());
    }
}
