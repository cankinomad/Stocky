import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.exception.UnitManagerException;
import org.berka.rabbitmq.producer.UnitIdProducer;
import org.berka.repository.IUnitRepository;
import org.berka.repository.entity.Unit;
import org.berka.service.UnitService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UnitServiceTest {

    @Mock
    private IUnitRepository repository;

    @InjectMocks
    private UnitService service;

    @Mock
    private UnitIdProducer unitIdProducer;


    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
    }


    //========================================================= create Tests =======================================================

    @Test
    public void create_ShouldBeSuccessful(){
        Unit unitUnsaved = Unit.builder().name("name").build();
        Unit unitSaved = Unit.builder().name("name").id(1L).build();
        when(repository.existsByName(unitUnsaved.getName())).thenReturn(false);
        when(repository.save(unitUnsaved)).thenReturn(unitSaved);

        Assertions.assertEquals(unitUnsaved.getName(), unitSaved.getName());
    }

    @Test
    public void create_ShouldThrowException(){
        CreateRequestDto dto = CreateRequestDto.builder().name("name").build();
        when(repository.existsByName(dto.getName())).thenReturn(true);

       assertThrows(UnitManagerException.class,()->service.createUnit(dto));
    }


    //========================================================= Approve Tests =======================================================

    @Test
    public void approve_Success(){
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        Boolean approveUnit = service.approveUnit(id);
        assertTrue(approveUnit);

    }

    @Test
    public void approve_Fail(){
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(false);

        Boolean approveUnit = service.approveUnit(id);
        assertFalse(approveUnit);

    }


    //========================================================= Update Tests =======================================================


    @Test
    public void update_ShouldSuccess(){
        UpdateRequestDto dto = UpdateRequestDto.builder().id(2L).name("name").build();

        Unit unit = Unit.builder().name("newName").build();
        when(repository.findById(dto.getId())).thenReturn(Optional.of(unit));

        MessageResponseDto messageResponseDto = service.updateUnit(dto);
        assertEquals("Birim bilgisi basariyla guncellenmistir",messageResponseDto.getMessage());
    }

    @Test
    public void update_ShouldThrowDefaultException(){
        UpdateRequestDto dto = UpdateRequestDto.builder().id(1L).name("name").build();

        assertThrows(UnitManagerException.class, () -> service.updateUnit(dto));
    }
    @Test
    public void update_ShouldThrowUnitNotFound(){
        UpdateRequestDto dto = UpdateRequestDto.builder().id(2L).name("name").build();

        when(repository.findById(dto.getId())).thenReturn(Optional.empty());

        assertThrows(UnitManagerException.class, () -> service.updateUnit(dto));
    }

    @Test
    public void update_ShouldThrowUnitExist(){
        UpdateRequestDto dto = UpdateRequestDto.builder().id(2L).name("name").build();

        Unit unit = Unit.builder().name("newName").build();
        when(repository.findById(dto.getId())).thenReturn(Optional.of(unit));
        when(repository.existsByName(dto.getName())).thenReturn(true);
        assertThrows(UnitManagerException.class, () -> service.updateUnit(dto));
    }

    //========================================================= Delete Tests =======================================================

    @Test
    public void delete_ShouldSuccessful(){
        Long id=2L;
        Unit unit = Unit.builder().id(2L).name("name").build();
        when(repository.findById(id)).thenReturn(Optional.of(unit));
        MessageResponseDto messageResponseDto = service.deleteUnit(id);

        assertEquals(unit.getName()+" birimi basariyla silinmistir",messageResponseDto.getMessage());
    }

    @Test
    public void delete_ShouldThrowDefault(){
        Long id=1L;

        assertThrows(UnitManagerException.class,()->  service.deleteUnit(id));

    }

    @Test
    public void delete_ShouldThrowUnitNotFound(){
        Long id=2L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UnitManagerException.class,()->  service.deleteUnit(id));
    }

    //========================================================= getName Tests =======================================================

    @Test
    public void getName_ShouldBeSuccessful(){
        Long id=2L;
        Unit unit = Unit.builder().name("name").id(2L).build();

        when(repository.findById(id)).thenReturn(Optional.of(unit));

        MessageResponseDto name = service.getName(id);
        assertEquals(unit.getName(),name.getMessage());
    }

    @Test
    public void getName_ShouldThrowUnitNotFound(){
        Long id=2L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UnitManagerException.class,()->  service.getName(id));
    }

}
