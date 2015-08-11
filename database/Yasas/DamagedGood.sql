USE coop;

CREATE TABLE damaged_stock(

	damaged_stock_id INT NOT NULL AUTO_INCREMENT,
	ds_date DATE NOT NULL,
	location VARCHAR(30) NOT NULL,

	CONSTRAINT PRIMARY KEY (damaged_stock_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE damaged_stock_item(

	id INT NOT NULL AUTO_INCREMENT,
	damaged_stock_id INT NOT NULL ,
	product_id INT NOT NULL,
	qty DOUBLE NOT NULL DEFAULT 0,
	qty_damaged DOUBLE NOT NULL DEFAULT 0,
	price DOUBLE NOT NULL DEFAULT 0,

	CONSTRAINT PRIMARY KEY (id),

	CONSTRAINT foreign KEY (damaged_stock_id) REFERENCES damaged_stock(damaged_stock_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;