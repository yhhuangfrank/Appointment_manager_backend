package com.frank.cmn.service;

import com.frank.cmn.repository.DictionaryRepository;
import com.frank.model.Dictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    public List<Dictionary> getChildrenData(Long id) {
        return dictionaryRepository.findAllByParentId(id);
    }
}
