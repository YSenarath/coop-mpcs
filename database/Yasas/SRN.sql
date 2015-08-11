USE coop;

CREATE TABLE srn(

	srn_number INT NOT NULL AUTO_INCREMENT,
	grn_number INT NOT NULL,
	srn_date DATE NOT NULL,
	supplier_id INT NOT NULL,
	location VARCHAR(30) NOT NULL,

	CONSTRAINT PRIMARY KEY (srn_number),

	CONSTRAINT foreign KEY (grn_number) REFERENCES grn(grn_number),
	CONSTRAINT foreign KEY (supplier_id) REFERENCES supplier(supplier_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE srn_item(

	id INT NOT NULL AUTO_INCREMENT,
	srn_no INT NOT NULL ,
	product_id INT NOT NULL,
	qty DOUBLE NOT NULL DEFAULT 0,
	cost DOUBLE NOT NULL DEFAULT 0,
	price DOUBLE NOT NULL DEFAULT 0,

	CONSTRAINT PRIMARY KEY (id),

	CONSTRAINT foreign KEY (srn_no) REFERENCES srn(srn_number),
	CONSTRAINT FOREIGN KEY (product_id) REFERENCES product(product_id) 

)ENGINE=InnoDB DEFAULT CHARSET=latin1;