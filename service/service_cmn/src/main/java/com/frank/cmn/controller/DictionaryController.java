package com.frank.cmn.controller;

import com.frank.cmn.service.DictionaryService;
import com.frank.common.Result;
import com.frank.model.Dictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/cmn/dict")
@RequiredArgsConstructor
@CrossOrigin
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @GetMapping("/getChildrenData/{id}")
    public Result<List<Dictionary>> getChildrenData(@PathVariable("id") Long id) {
        List<Dictionary> dictionaries =  dictionaryService.getChildrenData(id);
        return Result.ok(dictionaries);
    }
}
