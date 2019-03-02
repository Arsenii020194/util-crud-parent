package com.util.crud.controller;

import com.util.crud.dto.AbstractDto;
import com.util.crud.params.AbstractParams;
import com.util.crud.service.AbstractService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


public abstract class AbstractController<T extends AbstractDto, P extends AbstractParams> {

    @RequestMapping(value = "/filter", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public List<T> filter(@RequestBody P params) {
        return getService().filter(params);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public void remove(@RequestBody P id) {
        getService().remove(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public T update(@RequestBody P id) {
        return getService().update(id);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public T create(@RequestBody P id) {
        return getService().create(id);
    }

    @RequestMapping(value = "/remove/list", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public void remove(@RequestBody List<P> id) {
        id.forEach(this::remove);
    }

    @RequestMapping(value = "/update/list", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public List<T> update(@RequestBody List<P> id) {
        return id.stream().map(this::update).collect(Collectors.toList());
    }

    @RequestMapping(value = "/create/list", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public List<T> create(@RequestBody List<P> id) {
        return id.stream().map(this::create).collect(Collectors.toList());
    }

    public abstract AbstractService<T, P> getService();
}
