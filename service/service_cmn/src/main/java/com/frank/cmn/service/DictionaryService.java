package com.frank.cmn.service;

import com.frank.cmn.repository.DictionaryRepository;
import com.frank.model.Dictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    @Cacheable(value = "dict", keyGenerator = "myKeyGenerator")
    public List<Dictionary> getChildrenData(Long id) {
        return dictionaryRepository.findAllByParentId(id);
    }

    @Cacheable(value = "dict_name", keyGenerator = "myKeyGenerator")
    public String getName(Long dictCode) {
        Optional<Dictionary> dictOptional = dictionaryRepository.findById(dictCode);
        return dictOptional.isPresent() ? dictOptional.get().getName() : "";
    }
}
