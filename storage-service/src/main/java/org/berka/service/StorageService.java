package org.berka.service;

import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.exception.ErrorType;
import org.berka.exception.StorageManagerException;
import org.berka.rabbitmq.producer.StorageIdProducer;
import org.berka.repository.IStorageRepository;
import org.berka.repository.entity.Storage;
import org.berka.utility.ServiceManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StorageService extends ServiceManager<Storage,Long> {

    private final IStorageRepository repository;
    private final StorageIdProducer storageIdProducer;


    public StorageService(IStorageRepository repository, StorageIdProducer storageIdProducer) {
        super(repository);
        this.repository = repository;
        this.storageIdProducer = storageIdProducer;
    }


    @PostConstruct
    public void defaultStorage(){
        boolean existsById = repository.existsById(1L);
        if(!existsById){
            save( Storage.builder().name("Diger").productAmount(0L).build());
        }
    }
    public Storage createStorage(CreateRequestDto dto) {

        boolean existsByName = repository.existsByName(dto.getName());
        if (existsByName) {
            throw new StorageManagerException(ErrorType.STORAGENAME_EXIST);
        }

        Storage storage = Storage.builder().name(dto.getName()).build();

        return save(storage);
    }

    public MessageResponseDto updateStorage(UpdateRequestDto dto) {
        if (dto.getId() == 1L) {
            throw new StorageManagerException(ErrorType.STORAGE_CANNOT_BE_DELETED);
        }
        Storage storage = repository.findById(dto.getId()).orElseThrow(() -> new StorageManagerException(ErrorType.STORAGE_NOT_FOUND));

        if (storage.getName() != dto.getName() && repository.existsByName(dto.getName())) {
            throw new StorageManagerException(ErrorType.STORAGENAME_EXIST);
        }

        storage.setName(dto.getName());
        update(storage);

        return new MessageResponseDto("Depo basariyla guncellenmistir");
    }

    public MessageResponseDto deleteStorage(Long id) {
        if(id==1L){
            throw new StorageManagerException(ErrorType.STORAGE_CANNOT_BE_DELETED);
        }
        Storage storage = repository.findById(id).orElseThrow(() -> new StorageManagerException(ErrorType.STORAGE_NOT_FOUND));
        String name = storage.getName();
        delete(storage);
        storageIdProducer.storageId(id);
        return new MessageResponseDto(name+ " isimli depo basariyla silinmistir");
    }

    public Boolean approveStorage(Long id) {
        return repository.existsById(id);
    }

    public MessageResponseDto getName(Long id) {
        Storage storage = repository.findById(id).orElseThrow(() -> new StorageManagerException(ErrorType.STORAGE_NOT_FOUND));
        return new MessageResponseDto(storage.getName());
    }
}
