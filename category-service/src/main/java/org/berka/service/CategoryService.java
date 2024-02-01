package org.berka.service;

import org.berka.dto.request.CreateRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.exception.CategoryManagerException;
import org.berka.exception.ErrorType;
import org.berka.mapper.ICategoryMapper;
import org.berka.rabbitmq.producer.CategoryIdProducer;
import org.berka.repository.ICategoryRepository;
import org.berka.repository.entity.Category;
import org.berka.utility.ServiceManager;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService extends ServiceManager<Category,Long> {

    private final ICategoryRepository repository;
    private final CategoryIdProducer categoryIdProducer;

    public CategoryService(ICategoryRepository repository, CategoryIdProducer categoryIdProducer) {
        super(repository);
        this.repository = repository;
        this.categoryIdProducer = categoryIdProducer;
    }

    @PostConstruct
    public void createOtherCategory(){
        boolean existsById = repository.existsById(1L);
        if (!existsById) {
            save(Category.builder().name("Diger").isMainCategory(true).build());
        }
    }

    public Category createCategory(CreateRequestDto dto) {

        Category category = ICategoryMapper.INSTANCE.toCategory(dto);

        boolean isCategoryExist = repository.existsByName(dto.getName());
        if (isCategoryExist) {
            throw new CategoryManagerException(ErrorType.CATEGORYNAME_EXIST);
        }

        if(category.getCategories()!= null){
        for (Long categoryCategory : category.getCategories()) {
            boolean b = repository.existsById(categoryCategory);
            if(!b){
                throw new CategoryManagerException(ErrorType.CATEGORY_NOT_FOUND);
            }
        }}

        return save(category);
    }

    public MessageResponseDto deleteCategory(Long id) {
        if(id==1L){
            throw new CategoryManagerException(ErrorType.CATEGORY_CANNOT_BE_DELETED);
        }
        Category category = repository.findById(id).orElseThrow(() -> new CategoryManagerException(ErrorType.CATEGORY_NOT_FOUND));

        List<Category> allCategories = repository.findAll();

        List<Category> containsDeletedElement = allCategories.stream().filter(x -> x.getCategories().contains(id)).toList();


        if(!containsDeletedElement.isEmpty()){
        for (Category category1 : containsDeletedElement) {
            List<Long> list = category1.getCategories().stream().filter(x -> x != id).collect(Collectors.toList());


            category1.setCategories(list);
            update(category1);
        }}

        categoryIdProducer.categoryId(category.getId());

        delete(category);
        return new MessageResponseDto("Kategori basariyla silinmistir");
    }


    public MessageResponseDto updateCategory(UpdateRequestDto dto) {

        if (dto.getId() == 1L) {
            throw new CategoryManagerException(ErrorType.CATEGORY_CANNOT_BE_DELETED);
        }

        Category category = repository.findById(dto.getId()).orElseThrow(() -> new CategoryManagerException(ErrorType.CATEGORY_NOT_FOUND));

        if (!category.getName().equals(dto.getName()) && repository.existsByName(dto.getName())) {
            throw new CategoryManagerException(ErrorType.CATEGORYNAME_EXIST);
        }

        if (!category.getName().equals(dto.getName())) {
            category.setName(dto.getName());
        }
        category.setIsMainCategory(dto.getIsMainCategory());

        boolean anyMatch = dto.getCategories().stream().anyMatch(x -> x == category.getId());
        if (anyMatch) {
            throw new CategoryManagerException(ErrorType.CANNOT_ADD_ITSELF);
        }

        category.setCategories(dto.getCategories());

        update(category);
        return new MessageResponseDto(category.getName() + " kategori bilgileri basariyla guncellendi");
    }

    public Boolean approveCategory(Long id) {
        return repository.existsById(id);
    }

    public MessageResponseDto getName(Long id) {
        Category category = repository.findById(id).orElseThrow(() -> new CategoryManagerException(ErrorType.CATEGORY_NOT_FOUND));
        return new MessageResponseDto(category.getName());

    }

    public Category getItself(Long id) {
        return repository.findById(id).orElseThrow(() -> new CategoryManagerException(ErrorType.CATEGORY_NOT_FOUND));
    }
}
