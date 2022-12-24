package com.profesorp.jpajoins.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.profesorp.jpajoins.entities.Invoiceheader;
import com.profesorp.jpajoins.repository.invoiceHeaderRepository;

@RestController
public class PrincipalController {
	@Autowired
	invoiceHeaderRepository invoiceHeaderRepository;
	
	@GetMapping("/{id}")
	public ResponseEntity<Invoiceheader> get(@PathVariable int id)
	{
		 Optional<Invoiceheader> invoice=invoiceHeaderRepository.findById(id);
		 if (invoice.isPresent())
			 return new ResponseEntity<>(invoice.get(),HttpStatus.OK);
		 return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
	}
}
