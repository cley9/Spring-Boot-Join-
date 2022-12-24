package com.profesorp.jpajoins.controller;

import java.util.List;
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

	// @GetMapping("/data")
	// // public ResponseEntity<Invoiceheader> get(@PathVariable int id)
	// public ResponseEntity<Invoiceheader> get()
	// {
	// Optional<Invoiceheader> invoice=invoiceHeaderRepository.findAll();
	// // Optional<Invoiceheader> invoice=invoiceHeaderRepository.findById(id);
	// //List<Invoiceheader> invoice = invoiceHeaderRepository.findAll();
	// if (invoice.isPresent())
	// return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
	// return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	// }

	@GetMapping("/data")
	public ResponseEntity<List<Invoiceheader>> get() {
		List<Invoiceheader> facturas = invoiceHeaderRepository.findAll();
		if (facturas.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(facturas, HttpStatus.OK);
	}

}
