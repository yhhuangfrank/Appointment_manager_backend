package com.frank.config;


import com.frank.common.Result;
import com.frank.model.Dictionary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "service-cmn")
public interface DictionaryFeignClient {

    @GetMapping("/admin/cmn/dict/findChildrenData/{id}")
    Result<List<Dictionary>> findChildrenData(@PathVariable("id") Long id);

    @GetMapping("/admin/cmn/dict/findName/{id}")
    Result<String> findName(@PathVariable("id") Long dictCode);

}
