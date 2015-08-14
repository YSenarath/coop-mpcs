DROP DATABASE IF EXISTS COOP;

CREATE database COOP;
USE COOP;


CREATE TABLE employee(

	employee_id INT NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	voucher_issued BOOLEAN NOT NULL DEFAULT false,
	position VARCHAR(75) NOT NULL,
	
	CONSTRAINT PRIMARY KEY (employee_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE user_credentials(

	user_name VARCHAR(32) NOT NULL UNIQUE,
	password blob NOT NULL,
	access_level enum('manager','cashier','inventory_manager') NOT NULL,
	isLoggedIn BOOLEAN NOT NULL DEFAULT FALSE,

	CONSTRAINT PRIMARY KEY (user_name)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE counter(

	counter_id INT NOT NULL,
	current_amount DOUBLE NOT NULL DEFAULT 0,

	CONSTRAINT PRIMARY KEY (counter_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE counter_login(

	shift_id INT NOT NULL AUTO_INCREMENT,
	user_name VARCHAR(32) NOT NULL,
	counter_id INT NOT NULL,
	log_in_time TIME NOT NULL,
	log_in_date DATE NOT NULL,
	log_off_time TIME DEFAULT NULL,
	log_off_date DATE DEFAULT NULL,
	initial_amount DOUBLE NOT NULL DEFAULT 0,
	cash_withdrawals DOUBLE NOT NULL DEFAULT 0,
	log_off_amount_expected DOUBLE NOT NULL DEFAULT 0,
	log_off_amount_actual DOUBLE NOT NULL DEFAULT 0,
	shift_ended BOOLEAN NOT NULL DEFAULT FALSE,

	CONSTRAINT PRIMARY KEY (shift_id),
	CONSTRAINT foreign KEY (user_name) REFERENCES user_credentials(user_name) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT foreign KEY (counter_id) REFERENCES counter(counter_id)  ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE cash_withdrawal(

	cash_withdrawal_id INT NOT NULL AUTO_INCREMENT,
	shift_id INT NOT NULL,
	withdrawal_time TIME NOT NULL,
	withdrawal_date DATE NOT NULL,
	amount DOUBLE NOT NULL DEFAULT 0,

	CONSTRAINT PRIMARY KEY (cash_withdrawal_id),
	CONSTRAINT foreign KEY (shift_id) REFERENCES counter_login(shift_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE department (

	department_id INT NOT NULL AUTO_INCREMENT,
	department_name VARCHAR(32) NOT NULL,

	CONSTRAINT PRIMARY KEY(department_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE category (

	department_id INT NOT NULL,
	category_id INT NOT NULL,
	category_name VARCHAR(32) NOT NULL,
	description VARCHAR(64),
	discounted BOOLEAN NOT NULL DEFAULT FALSE,

	CONSTRAINT PRIMARY KEY(category_id,department_id),
	CONSTRAINT FOREIGN KEY (department_id) REFERENCES Department(department_id) ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE category_discount(

	category_id INT NOT NULL,
	department_id INT NOT NULL,
	discount DOUBLE NOT NULL,
	start_date DATE NOT NULL,
	end_date DATE DEFAULT NULL,
	promotional BOOLEAN NOT NULL DEFAULT TRUE,
	qty int DEFAULT NULL,
	members_only BOOLEAN NOT NULL DEFAULT FALSE,

	CONSTRAINT PRIMARY KEY(category_id,department_id),
	CONSTRAINT FOREIGN KEY (category_id,department_id) REFERENCES category(category_id,department_id) ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE product (

	product_id INT NOT NULL AUTO_INCREMENT,
	product_name VARCHAR(32) NOT NULL,
	barcode LONG NOT NULL,
	description VARCHAR(64),
	category_id INT NOT NULL,
	department_id INT NOT NULL,
	unit VARCHAR(10) NOT NULL,
	pack_size DOUBLE NOT NULL,
	reorder_value DOUBLE NOT NULL DEFAULT 0,
	reorder_qty INT NOT NULL DEFAULT 0,
	max_qty INT NOT NULL DEFAULT 0,

	CONSTRAINT PRIMARY KEY(product_id),
	CONSTRAINT FOREIGN KEY(category_id,department_id) REFERENCES category(category_id,department_id) ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE supplier(

	supplier_id INT NOT NULL AUTO_INCREMENT,
	sup_name VARCHAR(64) NOT NULL,
	contact_person VARCHAR(32) NOT NULL,
    address VARCHAR(255) NOT NULL,
    tel_number INT,
    fax_number INT,
    e_mail VARCHAR(64) NOT NULL,
    -- credit_limit DOUBLE,
    reg_date DATE,
    cancel_date DATE,
    
	CONSTRAINT PRIMARY KEY(supplier_id)
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              	
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE grn(

	grn_number INT NOT NULL AUTO_INCREMENT,
    
    invoice_no INT NOT NULL,
	invoice_date DATE NOT NULL,
	supplier_id INT NOT NULL,
	location VARCHAR(32) NOT NULL,
	payment_method VARCHAR(8) NOT NULL,
	loading_fee DOUBLE NOT NULL DEFAULT 0.0,
	PurchasingBill_discount DOUBLE NOT NULL DEFAULT 0.0,
	sellingBill_discount DOUBLE NOT NULL DEFAULT 0.0,
    

	CONSTRAINT PRIMARY KEY(grn_number),
	CONSTRAINT FOREIGN KEY(supplier_id) REFERENCES supplier(supplier_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE batch(

	batch_id INT NOT NULL,
	product_id INT NOT NULL,
	grn_number INT NOT NULL,
	unit_cost DOUBLE NOT NULL,
	cost_discount DOUBLE NOT NULL DEFAULT 0 ,
	unit_price DOUBLE NOT NULL,
	qty DOUBLE NOT NULL,
	exp_date DATE,
	notify_date DATE,
	recieved_qty DOUBLE DEFAULT NULL,
	sold_qty DOUBLE DEFAULT 0,
	in_stock BOOLEAN NOT NULL DEFAULT FALSE,
	discounted BOOLEAN NOT NULL DEFAULT FALSE,

	CONSTRAINT PRIMARY KEY(batch_id,product_id),
	CONSTRAINT FOREIGN KEY(product_id) REFERENCES product(product_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FOREIGN KEY(grn_number) REFERENCES grn(grn_number)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE batch_discount (

	batch_id INT NOT NULL,
	product_id INT NOT NULL,
	discount DOUBLE NOT NULL,
	start_date DATE NOT NULL,
	end_date DATE DEFAULT NULL,
	promotional BOOLEAN NOT NULL DEFAULT TRUE, -- if true give even for 1 item
	qty DOUBLE DEFAULT NULL, -- get no of products and give to multiples of qty only
	members_only BOOLEAN NOT NULL DEFAULT FALSE,

	CONSTRAINT PRIMARY KEY(batch_id,product_id),
	CONSTRAINT FOREIGN KEY(batch_id,product_id) REFERENCES batch(batch_id,product_id) ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE invoice(

	bill_id INT NOT NULL AUTO_INCREMENT,
	shift_id INT NOT NULL,
	bill_time TIME NOT NULL,
	bill_date DATE NOT NULL,
	amount DOUBLE NOT NULL,

	CONSTRAINT PRIMARY KEY (bill_id),
	CONSTRAINT foreign KEY (shift_id) REFERENCES counter_login(shift_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE invoice_items(

	bill_id INT NOT NULL,
	batch_id INT NOT NULL ,
	product_id INT NOT NULL,
	discount DOUBLE NOT NULL DEFAULT 0,
	unit_price DOUBLE NOT NULL,
	qty DOUBLE NOT NULL,

	CONSTRAINT PRIMARY KEY (bill_id,batch_id,product_id),

	CONSTRAINT foreign KEY (bill_id) REFERENCES invoice(bill_id),
	CONSTRAINT foreign KEY (batch_id,product_id) REFERENCES batch(batch_id,product_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE refund(

	refund_id INT NOT NULL AUTO_INCREMENT,
	bill_id INT NOT NULL,
	shift_id INT NOT NULL,
	refund_time TIME NOT NULL,
	refund_date DATE NOT NULL,
	amount DOUBLE NOT NULL,

	CONSTRAINT PRIMARY KEY (refund_id,bill_id),
	CONSTRAINT foreign KEY (bill_id) REFERENCES invoice(bill_id),
	CONSTRAINT foreign KEY (shift_id) REFERENCES counter_login(shift_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE refund_items(

	refund_id INT NOT NULL,
	batch_id INT NOT NULL ,
	product_id INT NOT NULL,
	discount DOUBLE NOT NULL DEFAULT 0,
	unit_price DOUBLE NOT NULL,
	qty DOUBLE NOT NULL,

	CONSTRAINT PRIMARY KEY (refund_id,batch_id,product_id),
	CONSTRAINT foreign KEY (refund_id) REFERENCES refund(refund_id),
	CONSTRAINT foreign KEY (batch_id,product_id) REFERENCES batch(batch_id,product_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE cash_payment(

	bill_id INT NOT NULL,
	cash_payment_id INT NOT NULL,
	amount DOUBLE NOT NULL DEFAULT 0,
	change_amount DOUBLE NOT NULL DEFAULT 0,

	CONSTRAINT PRIMARY KEY (bill_id,cash_payment_id),
	CONSTRAINT foreign KEY (bill_id) REFERENCES invoice(bill_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE card_payment(

	bill_id INT NOT NULL,
	card_payment_id INT NOT NULL,
	card_type enum('AMEX','MASTER','VISA') NOT NULL,
	card_no VARCHAR(5) NOT NULL,
	amount DOUBLE NOT NULL DEFAULT 0,

	CONSTRAINT PRIMARY KEY (bill_id,card_payment_id),
	CONSTRAINT foreign KEY (bill_id) REFERENCES invoice(bill_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE poshana_payment(

	bill_id INT NOT NULL,
	poshana_payment_id INT NOT NULL,
	stamp_id VARCHAR(32) NOT NULL,
	customer_name VARCHAR(100) NOT NULL ,
	amount DOUBLE NOT NULL DEFAULT 0,

	CONSTRAINT PRIMARY KEY (bill_id,poshana_payment_id),
	CONSTRAINT foreign KEY (bill_id) REFERENCES invoice(bill_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE employee_voucher_payment(

	bill_id INT NOT NULL,
	voucher_payment_id INT NOT NULL,
	employee_id INT NOT NULL,
	amount DOUBLE NOT NULL,

	CONSTRAINT PRIMARY KEY (bill_id,voucher_payment_id),
	CONSTRAINT foreign KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT foreign KEY (bill_id) REFERENCES invoice(bill_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE customer_voucher_payment(

	bill_id INT NOT NULL,
	voucher_payment_id INT NOT NULL,
	voucher_id VARCHAR(32) NOT NULL,
	amount DOUBLE NOT NULL DEFAULT 0,

	CONSTRAINT PRIMARY KEY (bill_id,voucher_payment_id),
	CONSTRAINT foreign KEY (bill_id) REFERENCES invoice(bill_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE credit_customer(

	customer_id INT NOT NULL AUTO_INCREMENT,
	customer_name VARCHAR(50) NOT NULL,
	customer_address VARCHAR(255) NOT NULL,
	customer_telephone VARCHAR(12) ,
	customer_nic VARCHAR(10) NOT NULL,
	current_credit DOUBLE DEFAULT 0,

	CONSTRAINT PRIMARY KEY (customer_id)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE coop_credit_payment(

	bill_id INT NOT NULL,
	coop_credit_payment_id INT NOT NULL,
	customer_id INT NOT NULL,
	amount DOUBLE NOT NULL DEFAULT 0,
	amount_settled DOUBLE NOT NULL DEFAULT 0,

	CONSTRAINT PRIMARY KEY (bill_id,coop_credit_payment_id),
	CONSTRAINT foreign KEY (bill_id) REFERENCES invoice(bill_id),
	CONSTRAINT foreign KEY (customer_id) REFERENCES credit_customer(customer_id) ON DELETE CASCADE ON UPDATE CASCADE

)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE settings(

	prop_key VARCHAR(32) NOT NULL,
	prop_value VARCHAR(32) NOT NULL,

	CONSTRAINT PRIMARY KEY (prop_key)

)ENGINE=InnoDB DEFAULT CHARSET=latin1;

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