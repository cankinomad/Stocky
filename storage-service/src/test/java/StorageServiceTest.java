import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.exception.StorageManagerException;
import org.berka.rabbitmq.producer.StorageIdProducer;
import org.berka.repository.IStorageRepository;
import org.berka.repository.entity.Storage;
import org.berka.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StorageServiceTest {

    @Mock
    private IStorageRepository repository;

    @InjectMocks
    private StorageService service;

    @Mock
    StorageIdProducer storageIdProducer;

    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
    }



    //========================================================= create Tests =======================================================

    @Test
    public void create_ShouldThrowException(){
        CreateRequestDto dto = CreateRequestDto.builder().name("name").build();
        when(repository.existsByName(dto.getName())).thenReturn(true);

        assertThrows(StorageManagerException.class, () -> service.createStorage(dto));

    }

    @Test
    public void create_ShouldBeSuccessful(){
        CreateRequestDto dto = CreateRequestDto.builder().name("name").build();
        Storage storageUnsaved = Storage.builder().name("name").build();
        Storage storageSaved = Storage.builder().id(1L).name("name").build();

        when(repository.existsByName(dto.getName())).thenReturn(false);
        when(repository.save(storageUnsaved)).thenReturn(storageSaved);

        Storage storage = service.createStorage(dto);

        assertEquals(storageUnsaved.getName(),storage.getName());

    }


    //========================================================= Update Tests =======================================================
    @Test
    public void update_ShouldSuccess(){
        UpdateRequestDto dto = UpdateRequestDto.builder().id(2L).name("name").build();

        Storage storage = Storage.builder().name("newName").build();
        when(repository.findById(dto.getId())).thenReturn(Optional.of(storage));

        MessageResponseDto messageResponseDto = service.updateStorage(dto);
        assertEquals("Depo basariyla guncellenmistir",messageResponseDto.getMessage());
    }

    @Test
    public void update_ShouldThrowDefaultException(){
        UpdateRequestDto dto = UpdateRequestDto.builder().id(1L).name("name").build();

        assertThrows(StorageManagerException.class, () -> service.updateStorage(dto));
    }
    @Test
    public void update_ShouldThrowUnitNotFound(){
        UpdateRequestDto dto = UpdateRequestDto.builder().id(2L).name("name").build();

        when(repository.findById(dto.getId())).thenReturn(Optional.empty());

        assertThrows(StorageManagerException.class, () -> service.updateStorage(dto));
    }

    @Test
    public void update_ShouldThrowUnitExist(){
        UpdateRequestDto dto = UpdateRequestDto.builder().id(2L).name("name").build();

        Storage storage = Storage.builder().name("newName").build();
        when(repository.findById(dto.getId())).thenReturn(Optional.of(storage));
        when(repository.existsByName(dto.getName())).thenReturn(true);
        assertThrows(StorageManagerException.class, () -> service.updateStorage(dto));
    }


    //========================================================= Delete Tests =======================================================

    @Test
    public void delete_ShouldSuccessful(){
        Long id=2L;
        Storage storage = Storage.builder().id(2L).name("name").build();
        when(repository.findById(id)).thenReturn(Optional.of(storage));
        MessageResponseDto messageResponseDto = service.deleteStorage(id);

        assertEquals(storage.getName()+" isimli depo basariyla silinmistir",messageResponseDto.getMessage());
    }

    @Test
    public void delete_ShouldThrowDefault(){
        Long id=1L;

        assertThrows(StorageManagerException.class,()->  service.deleteStorage(id));

    }

    @Test
    public void delete_ShouldThrowUnitNotFound(){
        Long id=2L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(StorageManagerException.class,()->  service.deleteStorage(id));
    }

    //========================================================= Approve Tests =======================================================

    @Test
    public void approve_Success(){
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        Boolean approveStorage = service.approveStorage(id);
        assertTrue(approveStorage);

    }

    @Test
    public void approve_Fail(){
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(false);

        Boolean approveStorage = service.approveStorage(id);
        assertFalse(approveStorage);

    }


    //========================================================= getName Tests =======================================================

    @Test
    public void getName_ShouldBeSuccessful(){
        Long id=2L;
        Storage storage = Storage.builder().name("name").id(2L).build();

        when(repository.findById(id)).thenReturn(Optional.of(storage));

        MessageResponseDto name = service.getName(id);
        assertEquals(storage.getName(),name.getMessage());
    }

    @Test
    public void getName_ShouldThrowUnitNotFound(){
        Long id=2L;

        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(StorageManagerException.class,()->  service.getName(id));
    }
}
