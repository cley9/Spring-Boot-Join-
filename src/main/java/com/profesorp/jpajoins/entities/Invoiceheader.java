package com.profesorp.jpajoins.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinColumnsOrFormulas;
import org.hibernate.annotations.JoinFormula;

import lombok.Data;

@Entity
@Data
public class Invoiceheader implements Serializable {
	@Id
	int id;

	@Column(name = "fiscalyear")
	int yearFiscal;
	@Column(name = "numberinvoice")
	int numberInvoice;

	@Column(name = "customerid", insertable = false, updatable = false)
	int customerId;

	@OneToOne(fetch = FetchType.EAGER)
	// @ManyToOne(fetch = FetchType.EAGER)
	// @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id", referencedColumnName = "id")
	// @JoinColumn(name = "mi columna id de esta tabla", referencedColumnName = "id
	// de la otra tabla a unir")
	Invoicedetails data;
	// @JoinColumns({
	// })

}
