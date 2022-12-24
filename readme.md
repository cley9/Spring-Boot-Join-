### Ejemplo de select joins entre diferentes tablas con JPA.

La documentación actualizada de este proyecto esta en: [http://www.profesor-p.com/2019/04/05/optimizando-consultas-con-hibernate/](http://www.profesor-p.com/2019/04/05/optimizando-consultas-con-hibernate/) 

Se verán diferentes tipos de unión explicando como realizar uniones entre tablas de modo perezoso (**lazy**) o agresivo  (**eager**). Se unirán tablas por un solo campo, por varios e incluso por uno pero añadiendo una condición estática.

EL proyecto de ejemplo esta en: https://github.com/chuchip/jpajoins y esta desarrollado en **Spring Boot** con **Hibernate**, usando como base de datos H2.

Las tablas están definidas en el fichero `schema.sql` y se cargan datos para pruebas en el fichero `data.sql`

Este es el esquema de la base de datos:

![jpajoins_schema](https://raw.githubusercontent.com/chuchip/jpajoins/master/jpajoins_schema.png)

Nota importante:**

> Si el nombre de la entidad o de la columna en las clases **Java**  tiene una mayúscula en medio, JPA interpretara que su hay un guion en medio y ese será la tabla o columna que buscara en la base de datos.
> De esta manera si a la clase `Invoiceheader.java` le renombráramos a `InvoiceHeader.java`  *Hibernate*, buscaría la tabla **invoice_header** en la base de datos y fallaría pues no la encontraría.
>
> Como ejemplo se puede ver el campo **line_details** de la tabla **invoiceDetails**, que en la clase **Invoicedetails.java** (obsérvese que la D es minúscula) es llamada con la variable **lineDetails**.



#### Realizando una '*select join*' entre la tabla cabeceras de factura (invoiceHeader) y clientes (customer)

- Enlace *perezoso* 

```java
@ManyToOne(fetch=FetchType.LAZY)
@JoinColumn(name="articleId",referencedColumnName="id")	
Customer customer;
```

Al realizar la búsqueda la select resultante sera:

```
select
        invoicehea0_.id as id1_3_0_,
        invoicehea0_.customerid as customer2_3_0_,
        invoicehea0_.fiscalyear as fiscalye3_3_0_,
        invoicehea0_.numberinvoice as numberin4_3_0_ 
    from
        invoiceheader invoicehea0_ 
    where
        invoicehea0_.id=?
```

 y cuando se realice un consulta  sobre la columna **customer** ejecutara la sentencia *select* necesaria para buscar los datos del cliente:

```
 select
        customer0_.id as id1_1_0_,
        customer0_.active as active2_1_0_,
        customer0_.address as address3_1_0_,
        customer0_.name as name4_1_0_ 
    from
        customer customer0_ 
    where
        customer0_.id=?
```

* Enlace *duro*

```
@ManyToOne(fetch=FetchType.EDGER)
@JoinColumn(name="articleId",referencedColumnName="id")	
Customer customer;
```

Al estar el tipo de busqueda establecido a `FetchType.EDGER`  realizara una única select con su correspondiente *left outer join*

```
 select
        invoicehea0_.id as id1_3_0_,
        invoicehea0_.customerid as customer2_3_0_,
        invoicehea0_.fiscalyear as fiscalye3_3_0_,
        invoicehea0_.numberinvoice as numberin4_3_0_,
        customer1_.id as id1_1_1_,
        customer1_.active as active2_1_1_,
        customer1_.address as address3_1_1_,
        customer1_.name as name4_1_1_ 
    from
        invoiceheader invoicehea0_ 
    left outer join
        customer customer1_ 
            on invoicehea0_.customerid=customer1_.id 
    where
        invoicehea0_.id=?
```

- Rizando el rizo.Añadiendo valores fijos

Pero, ¿y si queremos  que nos enlace las dos tablas por una columna y además con un valor fijo en otra?

En la tabla `customer` se definió la columna *active* y queremos que solo nos muestre los datos de la factura cuando el valor de esa columna sea `1` Para ello necesitaremos la ayuda de la etiqueta **@JoinColumnsOrFormulas** que nos permite realizar uniones tanto entre dos columnas como estableciendo valores a la columna de la tabla destino (en este caso *customer*)

```
@ManyToOne(fetch=FetchType.EDGER)
	@JoinColumnsOrFormulas({
		 @JoinColumnOrFormula(column=@JoinColumn(name="customerid", referencedColumnName ="id") ),
		 @JoinColumnOrFormula(formula = @JoinFormula(value="1",referencedColumnName = "active"))
	})	
	Customer customer;
```

La *select* ejecutada será:

```
select
        invoicehea0_.id as id1_3_0_,
        invoicehea0_.customerid as customer2_3_0_,
        invoicehea0_.fiscalyear as fiscalye3_3_0_,
        invoicehea0_.numberinvoice as numberin4_3_0_,
        1 as formula1_0_,
        customer1_.id as id1_1_1_,
        customer1_.active as active2_1_1_,
        customer1_.address as address3_1_1_,
        customer1_.name as name4_1_1_ 
    from
        invoiceheader invoicehea0_ 
    left outer join
        customer customer1_ 
            on invoicehea0_.customerid=customer1_.id 
            and 1=customer1_.active 
    where
        invoicehea0_.id=?
```

En el caso de que no encuentre ningún registro, la variable **customer** tendrá un valor nulo.

Si el tipo de enlace fuera *lazy* como en el caso anterior se haría primero una *query* sobre la tabla **invoiceheader** y cuando se pidiera el valor de la variable **customer** se realizaría sobre su correspondiente tabla-

### Uniendo tabla cabeceras facturas y líneas de facturas.

Para unir las dos tablas pondremos el siguiente código en la clase `Invoicedetails.java`

```java
@OneToMany
@JoinColumns(
    {
        @JoinColumn(name="fiscalyear",referencedColumnName="fiscalyear"),
       	@JoinColumn(name="numberinvoice",referencedColumnName="numberinvoice")
    }			
)
List<Invoicedetails> details;	
```


Como se ve, al ser dos campos los que unen ambas tablas haremos uso de la etiqueta **@JoinColumns** con sus correspondientes **@JoinColumn** dentro.

Como no hemos especificado nada, la unión se hará del tipo **EAGER** por lo cual la consulta realizada a la base de datos será la siguiente:

```
  select
        invoicehea0_.id as id1_3_0_,
        invoicehea0_.customerid as customer2_3_0_,
        invoicehea0_.fiscalyear as fiscalye3_3_0_,
        invoicehea0_.numberinvoice as numberin4_3_0_,
        1 as formula1_0_,
        details1_.fiscalyear as fiscalye2_2_1_,
        details1_.numberinvoice as numberin5_2_1_,
        details1_.id as id1_2_1_,
        details1_.id as id1_2_2_,
        details1_.articleid as articlei6_2_2_,
        details1_.fiscalyear as fiscalye2_2_2_,
        details1_.linea_details as linea_de3_2_2_,
        details1_.numberarticles as numberar4_2_2_,
        details1_.numberinvoice as numberin5_2_2_,
        article2_.id as id1_0_3_,
        article2_.description as descript2_0_3_,
        article2_.price as price3_0_3_ 
    from
        invoiceheader invoicehea0_ 
    left outer join
        invoicedetails details1_ 
            on invoicehea0_.fiscalyear=details1_.fiscalyear 
            and invoicehea0_.numberinvoice=details1_.numberinvoice 
    left outer join
        article article2_ 
            on details1_.articleid=article2_.id 
    where
        invoicehea0_.id=?
```

El ultimo *"left outer join"* haciendo referencia a la tabla **article**  lo pone *Hibernate* porque en la clase **Invoicedetails.java** tenemos el código:

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumns({
		@JoinColumn(name="articleid",referencedColumnName="id")		
	})
	Article articles;	
para que nos muestre los datos del articulo por cada línea del articulo, y como esta marcada la unión a tipo **EAGER**, *Hibernate* es lo suficimiente listo para hacer una sola consulta a la base de datos.

Si realizamos un llamada a http://localhost:8080/1 observaremos la siguiente salida que devuelve la clase **Invoiceheader.java** veremos lo siguiente:

```
{
    "id": 1,
    "fiscalYear": 2019,
    "numberInvoice": 1,
    "customerId": 1,
    "customer": {
        "id": 1,
        "name": "customer 1 name",
        "address": "customer 1 address",
        "active": 1
    },
    "details": [
        {
            "id": 1,
            "fiscalyear": 2019,
            "numberinvoice": 1,
            "lineaDetails": 1,
            "numberarticles": 5,
            "articles": {
                "id": 1,
                "description": "article 1 description",
                "price": 10.1
            }
        },
        {
            "id": 2,
            "fiscalyear": 2019,
            "numberinvoice": 1,
            "lineaDetails": 2,
            "numberarticles": 3,
            "articles": {
                "id": 2,
                "description": "article 2 description",
                "price": 12.3
            }
        }
    ]
}
```



 Y así queda demostrado la importancia de establecer el tipo de unión pues si imaginamos una factura que tenga miles de líneas (improbable lo sé), si establecemos el método de unión a **lazy**  en vez de hacer una sola consulta a la base de datos, haría 1000 lo cual, por supuesto ralentizaría muchísimo nuestra consulta, aparte de sobrecargar innecesariamente el servidor de la base de datos