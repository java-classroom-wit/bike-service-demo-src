package pl.javasolutions.apps.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.javasolutions.apps.dto.PartDTO;
import pl.javasolutions.apps.exception.ResourceNotFoundException;
import pl.javasolutions.apps.mapper.PartMapper;
import pl.javasolutions.apps.repository.PartRepository;
import pl.javasolutions.apps.repository.model.PartEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartServiceTest {

    @Mock
    private PartRepository partRepository;

    @Mock
    private PartMapper partMapper;

    @InjectMocks
    private PartService partService;

    @Test
    void findById_existingId_returnsPart() {
        // given
        PartEntity part = new PartEntity();
        part.setId(1L);
        part.setName("Łańcuch Shimano 11s");
        when(partRepository.findById(1L)).thenReturn(Optional.of(part));

        // when
        PartEntity result = partService.findById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Łańcuch Shimano 11s");
        verify(partRepository).findById(1L);
    }

    @Test
    void findById_nonExistingId_throwsException() {
        // given
        when(partRepository.findById(999L)).thenReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> partService.findById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void create_validDTO_savesAndReturnsPart() {
        // given
        PartDTO dto = new PartDTO("Łańcuch Shimano 11s", "Shimano", new BigDecimal("89.99"));
        PartEntity entity = new PartEntity();
        entity.setName("Łańcuch Shimano 11s");
        PartEntity saved = new PartEntity();
        saved.setId(1L);
        saved.setName("Łańcuch Shimano 11s");

        when(partMapper.toEntity(dto)).thenReturn(entity);
        when(partRepository.save(entity)).thenReturn(saved);

        // when
        PartEntity result = partService.create(dto);

        // then
        assertThat(result.getId()).isEqualTo(1L);
        verify(partRepository).save(entity);
    }
}

