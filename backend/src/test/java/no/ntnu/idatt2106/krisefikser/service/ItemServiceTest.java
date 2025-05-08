package no.ntnu.idatt2106.krisefikser.service;

import no.ntnu.idatt2106.krisefikser.model.Item;
import no.ntnu.idatt2106.krisefikser.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock ItemRepository repo;
    @InjectMocks ItemServiceImpl service;

    private Item example;

    @BeforeEach
    void setUp() {
        example = new Item();
        example.setId(1);
        example.setName("Ex");
        example.setDescription("Desc");
    }

    @Test
    void findAllDelegates() {
        when(repo.findAll()).thenReturn(List.of(example));
        var result = service.findAll();
        assertThat(result).containsExactly(example);
        verify(repo).findAll();
    }

    @Test
    void createSavesEntity() {
        when(repo.save(any())).thenReturn(example);
        Item toCreate = new Item();
        toCreate.setName("X");
        toCreate.setDescription("x");
        var created = service.create(toCreate);
        assertThat(created).isEqualTo(example);
        verify(repo).save(toCreate);
    }

    @Test
    @DisplayName("delete(5) on existing item invokes repository delete")
    void deleteById_existing_deletes() {
        // Arrange: tell the repo that ID 5 exists
        when(repo.existsById(5)).thenReturn(true);

        // Act
        service.delete(5);

        // Assert
        verify(repo).deleteById(5);
    }

    @Test
    void findByIdReturnsOptional() {
        when(repo.findById(1)).thenReturn(Optional.of(example));
        var opt = service.findById(1);
        assertThat(opt).contains(example);
    }
}
