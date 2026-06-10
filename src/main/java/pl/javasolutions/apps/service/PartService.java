package pl.javasolutions.apps.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.javasolutions.apps.dto.PartDTO;
import pl.javasolutions.apps.exception.ResourceNotFoundException;
import pl.javasolutions.apps.mapper.PartMapper;
import pl.javasolutions.apps.repository.PartRepository;
import pl.javasolutions.apps.repository.model.PartEntity;

import java.math.BigDecimal;

@Service
public class PartService {

    private final PartRepository partRepository;
    private final PartMapper partMapper;

    public PartService(PartRepository partRepository, PartMapper partMapper) {
        this.partRepository = partRepository;
        this.partMapper = partMapper;
    }

    @Cacheable("parts")
    public Page<PartEntity> findAll(String manufacturer, BigDecimal minPrice,
                                    BigDecimal maxPrice, String name, Pageable pageable) {
        return partRepository.findWithFilters(manufacturer, minPrice, maxPrice, name, pageable);
    }

    @Cacheable(value = "part", key = "#id")
    public PartEntity findById(Long id) {
        return partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part", id));
    }

    @CacheEvict(value = {"parts", "part"}, allEntries = true)
    public PartEntity create(PartDTO dto) {
        PartEntity part = partMapper.toEntity(dto);
        return partRepository.save(part);
    }

    @CacheEvict(value = {"parts", "part"}, allEntries = true)
    public PartEntity update(Long id, PartDTO dto) {
        PartEntity part = findById(id);
        partMapper.updateEntity(part, dto);
        return partRepository.save(part);
    }

    @CacheEvict(value = {"parts", "part"}, allEntries = true)
    public PartEntity patch(Long id, PartDTO dto) {
        PartEntity part = findById(id);
        partMapper.patchEntity(part, dto);
        return partRepository.save(part);
    }

    @CacheEvict(value = {"parts", "part"}, allEntries = true)
    public void delete(Long id) {
        PartEntity part = findById(id);
        partRepository.delete(part);
    }
}

