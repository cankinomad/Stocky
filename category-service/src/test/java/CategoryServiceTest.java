import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.exception.CategoryManagerException;
import org.berka.mapper.ICategoryMapper;
import org.berka.rabbitmq.producer.CategoryIdProducer;
import org.berka.repository.ICategoryRepository;
import org.berka.repository.entity.Category;
import org.berka.service.CategoryService;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryServiceTest {

    @Mock
    private ICategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryIdProducer categoryIdProducer;

    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
    }


    //========================================================= create Tests =======================================================


    @Test
    public void create_ShouldBeSuccessful(){
        CreateRequestDto dto = CreateRequestDto.builder().name("name").categories(List.of(1L)).build();
        Category unsaved = ICategoryMapper.INSTANCE.toCategory(dto);
        Category saved = ICategoryMapper.INSTANCE.toCategory(dto);
        saved.setId(2L);
        when(repository.existsByName(dto.getName())).thenReturn(false);

        when(repository.existsById((1L))).thenReturn(true);
        when(repository.save(unsaved)).thenReturn(saved);

        Category category = service.createCategory(dto);

        assertEquals(dto.getName(), category.getName());

    }

    @Test
    public void create_ShouldThrowCategoryNameExist(){
        CreateRequestDto dto = CreateRequestDto.builder().name("name").build();

        when(repository.existsByName(dto.getName())).thenReturn(true);



        assertThrows( CategoryManagerException.class,()->service.createCategory(dto) );

    }

    @Test
    public void create_ShouldThrowCategoryNotFound(){
        CreateRequestDto dto = CreateRequestDto.builder().name("name").categories(List.of(1L)).build();
        when(repository.existsByName(dto.getName())).thenReturn(false);

        when(repository.existsById((1L))).thenReturn(false);

        assertThrows( CategoryManagerException.class,()->service.createCategory(dto) );

    }

    //========================================================= delete Tests =======================================================

    @Test
    public void delete_ShouldBeSuccessful(){
        Category category1 = Category.builder().id(1L).name("kat1").categories(new ArrayList<>()).build();
        Category category2 = Category.builder().id(2L).name("kat2").categories(new ArrayList<>()).build();
        Category category3 = Category.builder().id(3L).name("kat3").categories(new ArrayList<>()).build();

        when(repository.findById(3L)).thenReturn(Optional.of(category3));
        when(repository.findAll()).thenReturn(List.of(category1,category2,category3));


        MessageResponseDto messageResponseDto = service.deleteCategory(3L);
        assertEquals("Kategori basariyla silinmistir",messageResponseDto.getMessage());
    }

    @Test
    public void delete_ShouldThrowCategoryCannotBeDeleted(){

        assertThrows(CategoryManagerException.class,()->service.deleteCategory(1L));
    }

    @Test
    public void delete_ShouldThrowCategroyNotFound(){
        Category category1 = Category.builder().id(1L).name("kat1").categories(new ArrayList<>()).build();

        when(repository.findById(category1.getId())).thenReturn(Optional.empty());


        assertThrows(CategoryManagerException.class,()->service.deleteCategory(category1.getId()));
    }

    //========================================================= Update Tests =======================================================

    @Test
    public void update_ShouldBeSuccessful(){
        UpdateRequestDto dto = UpdateRequestDto.builder().name("newName").categories(new ArrayList<>()).id(2L).isMainCategory(true).build();
        Category notUpdated = Category.builder().name("name").categories(new ArrayList<>()).id(2L).isMainCategory(true).build();
        when(repository.findById(dto.getId())).thenReturn(Optional.of(notUpdated));

        when(repository.existsByName(dto.getName())).thenReturn(false);

        MessageResponseDto messageResponseDto = service.updateCategory(dto);
        assertEquals(notUpdated.getName()+" kategori bilgileri basariyla guncellendi",messageResponseDto.getMessage());
    }

    @Test
    public void update_ShouldThrowCategoryCannotBeDeleted(){
        UpdateRequestDto dto = UpdateRequestDto.builder().name("newName").categories(new ArrayList<>()).id(1L).isMainCategory(true).build();

        assertThrows(CategoryManagerException.class, () -> service.updateCategory(dto));    }

    @Test
    public void update_ShouldThrowCategoryNameExist(){
        UpdateRequestDto dto = UpdateRequestDto.builder().name("newName").categories(new ArrayList<>()).id(2L).isMainCategory(true).build();
        Category notUpdated = Category.builder().name("name").categories(new ArrayList<>()).id(2L).isMainCategory(true).build();
        when(repository.findById(dto.getId())).thenReturn(Optional.of(notUpdated));

        when(repository.existsByName(dto.getName())).thenReturn(true);

        assertThrows(CategoryManagerException.class, () -> service.updateCategory(dto));

    }

    @Test
    public void update_ShouldThrowCannotAddItself(){
        Category notUpdated = Category.builder().name("name").categories(new ArrayList<>()).id(2L).isMainCategory(true).build();
        UpdateRequestDto dto = UpdateRequestDto.builder().name("newName").categories(List.of(2L)).id(2L).isMainCategory(true).build();
        when(repository.findById(dto.getId())).thenReturn(Optional.of(notUpdated));

        when(repository.existsByName(dto.getName())).thenReturn(false);

        assertThrows(CategoryManagerException.class, () -> service.updateCategory(dto));
    }

    //========================================================= approve Tests =======================================================

    @Test
    public void approve_ShouldBeSuccessful(){
        when(repository.existsById(1L)).thenReturn(true);

        Boolean aBoolean = service.approveCategory(1L);
        assertTrue(aBoolean);
    }
    @Test
    public void approve_ShouldFail(){
        when(repository.existsById(1L)).thenReturn(false);

        Boolean aBoolean = service.approveCategory(1L);
        assertFalse(aBoolean);
    }

    //========================================================= getName Tests =======================================================

    @Test
    public void getName_ShouldBeSuccessful(){
        Long id=2L;
        Category category = Category.builder().name("name").id(2L).build();

        when(repository.findById(id)).thenReturn(Optional.of(category));

        MessageResponseDto name = service.getName(id);
        assertEquals(category.getName(),name.getMessage());
    }

    @Test
    public void getName_ShouldThrowUnitNotFound(){
        Long id=2L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CategoryManagerException.class,()->  service.getName(id));
    }

        //========================================================= getItself Tests =======================================================

    @Test
    public void getItself_ShouldBeSuccessful(){
        Category category = Category.builder().isMainCategory(true).build();
        when(repository.findById(1L)).thenReturn(Optional.of(category));

        Category itself = service.getItself(1L);
        assertTrue(itself.getIsMainCategory());
    }

    @Test
    public void getItself_ShouldFail(){
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryManagerException.class,()-> service.getItself(1L));
    }
}

