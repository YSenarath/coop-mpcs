USE COOP;

INSERT INTO settings (prop_key,prop_value) VALUES ('main_cashier_name','yps');
INSERT INTO settings (prop_key,prop_value) VALUES ('main_cashier_password','159');
INSERT INTO settings (prop_key,prop_value) VALUES ('poshana_amount','1500.00');
INSERT INTO settings (prop_key,prop_value) VALUES ('employee_voucher_amount','2000.00');
INSERT INTO settings (prop_key,prop_value) VALUES ('customer_credit_limit','10000.00');


INSERT INTO employee (name) VALUES ('Shehan');
INSERT INTO employee (name) VALUES ('Yasas');


INSERT INTO user_credentials (user_name,password,access_level) VALUES ('msw','123','manager');
INSERT INTO user_credentials (user_name,password,access_level) VALUES ('Jack','1234','cashier');
INSERT INTO user_credentials (user_name,password,access_level) VALUES ('yps','159','cashier');


INSERT INTO counter (counter_id, current_amount) VALUES (1,0);
INSERT INTO counter (counter_id, current_amount) VALUES (2,0);
INSERT INTO counter (counter_id, current_amount) VALUES (3,0);


INSERT INTO department (department_id,department_name) VALUES (1,'Beverages');
INSERT INTO department (department_id,department_name) VALUES (2,'Cosmatics');


INSERT INTO category (department_id,category_id,category_name,discounted) VALUES (1,1,'Tea',true);
INSERT INTO category (department_id,category_id,category_name,discounted) VALUES (1,2,'Cola',true);
INSERT INTO category (department_id,category_id,category_name) VALUES (2,1,'Shampoo');
INSERT INTO category (department_id,category_id,category_name,discounted) VALUES (2,2,'Toothpaste',true);


INSERT INTO category_discount (department_id,category_id,discount,start_date) VALUES (1,1,5.00,'2015-3-5');

INSERT INTO category_discount (department_id,category_id,discount,start_date,promotional,qty) VALUES (1,2,3.00,'2015-4-20',false,2);

INSERT INTO category_discount (category_id,department_id,discount,start_date) VALUES (2,2,1.00,'2015-5-2');


INSERT INTO product (department_id,category_id,product_id,product_name,barcode,description,unit,pack_size) VALUES (1,1,1,'Lipton Tea',123456789,'Lipton tea packet 500g','bulk',1);

INSERT INTO product (department_id,category_id,product_id,product_name,barcode,description,unit,pack_size) VALUES (1,1,2,'Watawele Tea',987654321,'Watawele tea packet 400g','bulk',1);

INSERT INTO product (department_id,category_id,product_id,product_name,barcode,description,unit,pack_size) VALUES (1,2,3,'Cocacola',876543219,'Cocacola bottle 1.5L','bulk',1);

INSERT INTO product (department_id,category_id,product_id,product_name,barcode,description,unit,pack_size) VALUES (1,2,4,'Sprite',765432198,'Sprite bottle 1.5L','bulk',1);

INSERT INTO product (department_id,category_id,product_id,product_name,barcode,description,unit,pack_size) VALUES (2,1,5,'Head & Shoulders',543219876,'Mens 400g','bulk',1);

INSERT INTO product (department_id,category_id,product_id,product_name,barcode,description,unit,pack_size) VALUES (2,2,6,'Coolgate',654321987,'Coolgate toothpase 100g','bulk',1);
