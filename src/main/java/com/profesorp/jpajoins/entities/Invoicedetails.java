package com.profesorp.jpajoins.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
public class Invoicedetails {
	@Id
	int id;

	@Column(name = "fiscalyear")
	int year;

	@Column(name = "numberinvoice")
	int invoice;

	@Column(name = "lineaDetails")
	int linea; // La 'D' esta en mayuscualas adrede para que mapee el campo linea_details

	@Column
	int numberarticles;

	// @ManyToOne(fetch = FetchType.EAGER)
	// @JoinColumn(name = "articleid", referencedColumnName = "id")
	// Article articles;
	// @JoinColumns({
	// })
}
