package com.barapp.web.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.barapp.web.business.service.BaseService;
import com.barapp.web.model.BaseDto;

public abstract class BaseController<D extends BaseDto> {
    
    @GetMapping()
    public List<D> getAll() {
	try {
	    return getService().getAll();
	} catch (Exception e) {
	    e.printStackTrace();
	    return new ArrayList<>();
	}
    }
    
    @PostMapping("/{id}")
    public ResponseEntity<String> save(@RequestBody D dto, @PathVariable String id) {
	try {
	    if (id == null || id.isEmpty() || id.equals("null")) {
		return new ResponseEntity<>(getService().save(dto), HttpStatus.OK);
	    }
	    return new ResponseEntity<>(getService().save(dto, id), HttpStatus.OK);
	} catch (Exception e) {
	    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<D> get(@PathVariable String id) {
	try {
	    return new ResponseEntity<>(getService().get(id), HttpStatus.OK);
	} catch (Exception e) {
	    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
	try {
	    getService().delete(id);
	    return new ResponseEntity<>(id, HttpStatus.OK);
	} catch (Exception e) {
	    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
    }
    
    public abstract BaseService<D> getService();
}
