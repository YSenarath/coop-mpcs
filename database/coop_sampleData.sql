USE COOP;

INSERT INTO settings (prop_key,prop_value) VALUES ('main_cashier_name','yps');
INSERT INTO settings (prop_key,prop_value) VALUES ('main_cashier_password','159');
INSERT INTO settings (prop_key,prop_value) VALUES ('poshana_amount','1500.00');
INSERT INTO settings (prop_key,prop_value) VALUES ('employee_voucher_amount','2000.00');
INSERT INTO settings (prop_key,prop_value) VALUES ('customer_credit_limit','10000.00');


INSERT INTO employee (name,position) VALUES ('Shehan','manager');
INSERT INTO employee (name,position) VALUES ('Yasas','cashier');


INSERT INTO user_credentials (user_name,password,access_level) VALUES ('msw','40BD001563085FC35165329EA1FF5C5ECBDBBEEF','manager');
INSERT INTO user_credentials (user_name,password,access_level) VALUES ('Jack','7110EDA4D09E062AA5E4A390B0A572AC0D2C0220','cashier');
INSERT INTO user_credentials (user_name,password,access_level) VALUES ('yps','6B6277AFCB65D33525545904E95C2FA240632660','cashier');


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


INSERT INTO grn(grn_number) VALUES (1);
INSERT INTO grn(grn_number) VALUES (2);

-- 1.Lipton Tea
INSERT INTO batch (product_id,batch_id,grn_number,unit_cost,unit_price,qty,exp_date,notify_date,recieved_qty,in_stock,discounted) VALUES (1,1,1,300.00,400.00,100,'2017-12-12','2017-11-12',60,true,true);

	-- not in stock
INSERT INTO batch (product_id,batch_id,grn_number,unit_cost,unit_price,qty,exp_date,notify_date,recieved_qty) VALUES (1,2,2,310.00,390.00,60,'2016-12-20','2016-11-20',60);


-- 2. Watawele Tea
INSERT INTO batch (product_id,batch_id,grn_number,unit_cost,unit_price,qty,exp_date,notify_date,recieved_qty,in_stock,discounted) VALUES (2,1,1,400.00,450.00,100,'2019-12-20','2019-11-20',100,true,true);

	-- expired
INSERT INTO batch (product_id,batch_id,grn_number,unit_cost,unit_price,qty,exp_date,notify_date,recieved_qty,in_stock) VALUES (2,2,2,250.00,280.00,40,'2015-02-20','2015-01-20',20,true);


-- 3. Cocacola
INSERT INTO batch (product_id,batch_id,grn_number,unit_cost,unit_price,qty,exp_date,notify_date,recieved_qty,in_stock,discounted) VALUES (3,1,2,160.00,220.00,30,'2016-02-20','2016-01-20',30,true,true);

-- 4. Sprite
INSERT INTO batch (product_id,batch_id,grn_number,unit_cost,unit_price,qty,exp_date,notify_date,recieved_qty,in_stock) VALUES (4,1,2,150.00,200.00,50,'2015-12-12','2015-11-12',50,true);

-- 5. Head & Shoulders
INSERT INTO batch (product_id,batch_id,grn_number,unit_cost,unit_price,qty,exp_date,notify_date,recieved_qty,in_stock,discounted) VALUES (5,1,2,100.00,120.00,300,'2018-12-20','2018-11-20',300,true,true);

INSERT INTO batch (product_id,batch_id,grn_number,unit_cost,unit_price,qty,exp_date,notify_date,recieved_qty,in_stock) VALUES (5,2,2,400.00,500.00,150,'2017-11-20','2017-10-20',150,true);

-- 6. Coolgate
INSERT INTO batch (product_id,batch_id,grn_number,unit_cost,unit_price,qty,exp_date,notify_date,recieved_qty,in_stock) VALUES (6,1,2,400.00,450.00,20,'2019-02-20','2019-01-20',20,true);


-- 1.Lipton Tea
INSERT INTO batch_discount (product_id,batch_id,discount,start_date,end_date,promotional,qty) VALUES (1,1,12.00,'2015-7-1','2015-8-1',false,4);

-- 2. Watawele Tea
INSERT INTO batch_discount (product_id,batch_id,discount,start_date,end_date) VALUES (2,1,5.00,'2015-8-1','2015-9-1');

-- 3. Cocacola
INSERT INTO batch_discount (product_id,batch_id,discount,start_date,end_date) VALUES (3,1,8.00,'2015-7-1','2015-10-1');

-- 5. Head & Shoulders
INSERT INTO batch_discount (product_id,batch_id,discount,start_date,end_date,members_only) VALUES (5,1,6.00,'2015-7-1','2015-11-1',true);


INSERT INTO credit_customer(customer_name,customer_address,customer_telephone,customer_nic) VALUES ('Kamal','abc','012-3456789','123456789V') ;

INSERT INTO credit_customer(customer_name,customer_address,customer_telephone,customer_nic) VALUES ('Amal','pqr','123-4567890','234567891V') ;

INSERT INTO credit_customer(customer_name,customer_address,customer_telephone,customer_nic) VALUES ('Bimal','srt','234-5678901','345678912V') ;
