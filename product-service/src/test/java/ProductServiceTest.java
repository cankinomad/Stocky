import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.exception.ProductManagerException;
import org.berka.manager.ICategoryManager;
import org.berka.manager.IStorageManager;
import org.berka.manager.IUnitManager;
import org.berka.mapper.IProductMapper;
import org.berka.repository.IProductRepository;
import org.berka.repository.entity.Product;
import org.berka.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {

    @Mock
    private IProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Mock
    private ICategoryManager categoryManager;

    @Mock
    private IStorageManager storageManager;
    @Mock
    private IUnitManager unitManager;

    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
    }

    //========================================================= create Tests =======================================================

    @Test
    public void create_ShouldBeSuccessful(){

        CreateRequestDto dto= CreateRequestDto.builder().name("name").categoryId(1L).storageId(1L).unitId(1L).amount(5L).build();
        Product product = Product.builder().name("name").categoryId(1L).storageId(1L).unitId(1L).amount(5L).build();
        when(repository.existsByName(dto.getName())).thenReturn(false);
        when(categoryManager.approveCategory(dto.getCategoryId())).thenReturn(ResponseEntity.of(Optional.of(true)));
        when(storageManager.approveStorage(dto.getStorageId())).thenReturn(ResponseEntity.of(Optional.of(true)));
        when(unitManager.approveUnit(dto.getUnitId())).thenReturn(ResponseEntity.of(Optional.of(true)));

        when(repository.save(IProductMapper.INSTANCE.toProduct(dto))).thenReturn(product);

        Product product1 = service.createProduct(dto);

        assertEquals(dto.getName(),product1.getName());
    }

    @Test
    public void create_ShouldThrowNoCategoryException(){

        CreateRequestDto dto= CreateRequestDto.builder().name("name").categoryId(1L).storageId(1L).unitId(1L).amount(5L).build();
        when(repository.existsByName(dto.getName())).thenReturn(false);
        when(categoryManager.approveCategory(dto.getCategoryId())).thenReturn(ResponseEntity.of(Optional.of(true)));
        when(storageManager.approveStorage(dto.getStorageId())).thenReturn(ResponseEntity.of(Optional.of(false)));

        assertThrows(ProductManagerException.class, () -> service.createProduct(dto));
    }

    @Test
    public void create_ShouldThrowNoStorageException(){

        CreateRequestDto dto= CreateRequestDto.builder().name("name").categoryId(1L).storageId(1L).unitId(1L).amount(5L).build();
        when(repository.existsByName(dto.getName())).thenReturn(false);
        when(categoryManager.approveCategory(dto.getCategoryId())).thenReturn(ResponseEntity.of(Optional.of(true)));
        when(storageManager.approveStorage(dto.getStorageId())).thenReturn(ResponseEntity.of(Optional.of(false)));



        assertThrows(ProductManagerException.class, () -> service.createProduct(dto));
    }

    @Test
    public void create_ShouldThrowNoUnitException(){

        CreateRequestDto dto= CreateRequestDto.builder().name("name").categoryId(1L).storageId(1L).unitId(1L).amount(5L).build();
        when(repository.existsByName(dto.getName())).thenReturn(false);
        when(categoryManager.approveCategory(dto.getCategoryId())).thenReturn(ResponseEntity.of(Optional.of(true)));
        when(storageManager.approveStorage(dto.getStorageId())).thenReturn(ResponseEntity.of(Optional.of(true)));
        when(unitManager.approveUnit(dto.getUnitId())).thenReturn(ResponseEntity.of(Optional.of(false)));


        assertThrows(ProductManagerException.class, () -> service.createProduct(dto));
    }

    @Test
    public void create_ShouldThrowProductNameExist(){
        CreateRequestDto dto= CreateRequestDto.builder().name("name").categoryId(1L).storageId(1L).unitId(1L).amount(5L).build();
        when(repository.existsByName(dto.getName())).thenReturn(true);

        assertThrows(ProductManagerException.class, () -> service.createProduct(dto));
    }


    //========================================================= update Tests =======================================================

    @Test
    public void update_ShouldBeSuccessful(){

        UpdateRequestDto dto = UpdateRequestDto.builder().id(1L).name("name").categoryId(1L).amount(2L).storageId(1L).unitId(1L).build();
        Product product = Product.builder().id(1L).name("name").categoryId(1L).amount(2L).storageId(1L).unitId(1L).build();

        when(repository.findById(product.getId())).thenReturn(Optional.of(product));

        MessageResponseDto messageResponseDto = service.updateProduct(dto);
        assertEquals("Urun bilgileri guncellenmistir",messageResponseDto.getMessage());
    }

    @Test
    public void update_ShouldThrowProductNotFound(){

        UpdateRequestDto dto = UpdateRequestDto.builder().id(1L).name("name").categoryId(1L).amount(2L).storageId(1L).unitId(1L).build();
        when(repository.findById(dto.getId())).thenReturn(Optional.empty());
        assertThrows(ProductManagerException.class,()->service.updateProduct(dto));
    }

    @Test
    public void update_ShouldThrowProductNameExist(){

        UpdateRequestDto dto = UpdateRequestDto.builder().id(1L).name("name2").categoryId(1L).amount(2L).storageId(1L).unitId(1L).build();
        Product product = Product.builder().id(1L).name("name").categoryId(1L).amount(2L).storageId(1L).unitId(1L).build();

        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.existsByName(dto.getName())).thenReturn(true);

        assertThrows(ProductManagerException.class,()->service.updateProduct(dto));
    }

    @Test
    public void update_ShouldThrowNoCategoryFound(){

        UpdateRequestDto dto = UpdateRequestDto.builder().id(1L).name("name").categoryId(2L).amount(2L).storageId(1L).unitId(1L).build();
        Product product = Product.builder().id(1L).name("name").categoryId(1L).amount(2L).storageId(1L).unitId(1L).build();

        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.existsByName(dto.getName())).thenReturn(true);
        when(categoryManager.approveCategory(dto.getCategoryId())).thenReturn(ResponseEntity.of(Optional.of(false)));

        assertThrows(ProductManagerException.class,()->service.updateProduct(dto));
    }

    @Test
    public void update_ShouldThrowNoStorageFound(){

        UpdateRequestDto dto = UpdateRequestDto.builder().id(1L).name("name").categoryId(1L).amount(2L).storageId(2L).unitId(1L).build();
        Product product = Product.builder().id(1L).name("name").categoryId(1L).amount(2L).storageId(1L).unitId(1L).build();

        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.existsByName(dto.getName())).thenReturn(true);
        when(storageManager.approveStorage(dto.getStorageId())).thenReturn(ResponseEntity.of(Optional.of(false)));

        assertThrows(ProductManagerException.class,()->service.updateProduct(dto));
    }

    @Test
    public void update_ShouldThrowNoUnitFound(){

        UpdateRequestDto dto = UpdateRequestDto.builder().id(1L).name("name").categoryId(1L).amount(2L).storageId(1L).unitId(2L).build();
        Product product = Product.builder().id(1L).name("name").categoryId(1L).amount(2L).storageId(1L).unitId(1L).build();

        when(repository.findById(product.getId())).thenReturn(Optional.of(product));
        when(repository.existsByName(dto.getName())).thenReturn(true);
        when(unitManager.approveUnit(dto.getUnitId())).thenReturn(ResponseEntity.of(Optional.of(false)));

        assertThrows(ProductManagerException.class,()->service.updateProduct(dto));
    }

    //========================================================= delete Tests =======================================================

    @Test
    public void delete_ShouldBeSuccessful(){
        when(repository.existsById(1L)).thenReturn(true);

        MessageResponseDto messageResponseDto = service.deleteProduct(1L);
        assertEquals("Urun basariyla silinmistir",messageResponseDto.getMessage());
    }

    @Test
    public void delete_ShouldThrowProductNotFound(){
        when(repository.existsById(1L)).thenReturn(false);
        assertThrows(ProductManagerException.class, () -> service.deleteProduct(1L));
    }

    //========================================================= findByStorageId Tests =======================================================
    @Test
    public void findByStorageId_ShouldBeSuccessful(){

        when(repository.findByStorageId(1L)).thenReturn(List.of(new Product()));

        List<Product> byStorageId = service.findByStorageId(1L);
        assertEquals(byStorageId.size(),1L);
    }
    @Test
    public void findByStorageId_ShouldThrowProductNotFound(){
      when(repository.findByStorageId(1L)).thenReturn(Collections.emptyList());

     assertThrows(ProductManagerException.class,()->service.findByStorageId(1L));
    }

    //========================================================= findByCategoryId Tests =======================================================
    @Test
    public void findByCategoryId_ShouldBeSuccessful(){

        when(repository.findByCategoryId(1L)).thenReturn(List.of(new Product()));

        List<Product> byCategoryId = service.findByCategoryId(1L);
        assertEquals(byCategoryId.size(),1L);
    }
    @Test
    public void findByCategoryId_ShouldThrowProductNotFound(){
        when(repository.findByStorageId(1L)).thenReturn(Collections.emptyList());

        assertThrows(ProductManagerException.class,()->service.findByCategoryId(1L));
    }

}
