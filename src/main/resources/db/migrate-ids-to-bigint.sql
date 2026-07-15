-- Widen all primary/foreign key columns to bigint to match Long entity IDs.
BEGIN;

-- Drop foreign keys that depend on columns being altered
ALTER TABLE tbl_cart DROP CONSTRAINT IF EXISTS fkhv6grtjnmtoylt2yyt4wmqtf3;
ALTER TABLE tbl_cart_item DROP CONSTRAINT IF EXISTS fk84umwt3ihkiggf03us5gq116j;
ALTER TABLE tbl_cart_item DROP CONSTRAINT IF EXISTS fkhaw0aw4g8s9icxekpl4oi715a;
ALTER TABLE tbl_order DROP CONSTRAINT IF EXISTS fkhyolniflkctr0p6bp4t8me9vj;
ALTER TABLE tbl_order_item DROP CONSTRAINT IF EXISTS fk1oy9x003q55eqmuiv0y8a15e;
ALTER TABLE tbl_order_item DROP CONSTRAINT IF EXISTS fkmkqpajkg6p2wq4owcv1v08pc5;
ALTER TABLE tbl_payment DROP CONSTRAINT IF EXISTS fkac54xp3r2r3m9datds9351ric;
ALTER TABLE tbl_product DROP CONSTRAINT IF EXISTS fkbcejabdqv6xehq9386c69job;
ALTER TABLE tbl_product DROP CONSTRAINT IF EXISTS fkfq7110lh85cseoy13cgni7pet;
ALTER TABLE tbl_product_image DROP CONSTRAINT IF EXISTS fkmoh9bwnhu81rx27vxwxbg80up;
ALTER TABLE tbl_refresh_token DROP CONSTRAINT IF EXISTS fkjy4oisva8l5wptq3sslq6x5d1;
ALTER TABLE tbl_shipping_address DROP CONSTRAINT IF EXISTS fkm1mg4tlnovh8r80doh7awhbv7;
ALTER TABLE tbl_shipment DROP CONSTRAINT IF EXISTS fkb1wxiaexwu36k7740xfs776x1;

-- Primary keys
ALTER TABLE tbl_user ALTER COLUMN id TYPE bigint;
ALTER TABLE tbl_category ALTER COLUMN id TYPE bigint;
ALTER TABLE tbl_product ALTER COLUMN id TYPE bigint;
ALTER TABLE tbl_cart ALTER COLUMN id TYPE bigint;
ALTER TABLE tbl_cart_item ALTER COLUMN id TYPE bigint;
ALTER TABLE tbl_order ALTER COLUMN id TYPE bigint;
ALTER TABLE tbl_order_item ALTER COLUMN id TYPE bigint;
ALTER TABLE tbl_payment ALTER COLUMN id TYPE bigint;
ALTER TABLE tbl_refresh_token ALTER COLUMN id TYPE bigint;
ALTER TABLE tbl_shipping_address ALTER COLUMN id TYPE bigint;

-- Foreign keys / references
ALTER TABLE tbl_cart ALTER COLUMN user_id TYPE bigint;
ALTER TABLE tbl_cart_item ALTER COLUMN cart_id TYPE bigint;
ALTER TABLE tbl_cart_item ALTER COLUMN product_id TYPE bigint;
ALTER TABLE tbl_order ALTER COLUMN user_id TYPE bigint;
ALTER TABLE tbl_order_item ALTER COLUMN order_id TYPE bigint;
ALTER TABLE tbl_order_item ALTER COLUMN product_id TYPE bigint;
ALTER TABLE tbl_payment ALTER COLUMN order_id TYPE bigint;
ALTER TABLE tbl_product ALTER COLUMN user_id TYPE bigint;
ALTER TABLE tbl_product ALTER COLUMN category_id TYPE bigint;
ALTER TABLE tbl_product_image ALTER COLUMN product_id TYPE bigint;
ALTER TABLE tbl_refresh_token ALTER COLUMN user_id TYPE bigint;
ALTER TABLE tbl_shipping_address ALTER COLUMN user_id TYPE bigint;
ALTER TABLE tbl_shipment ALTER COLUMN order_id TYPE bigint;

-- Recreate foreign keys
ALTER TABLE tbl_cart
    ADD CONSTRAINT fkhv6grtjnmtoylt2yyt4wmqtf3 FOREIGN KEY (user_id) REFERENCES tbl_user(id);
ALTER TABLE tbl_cart_item
    ADD CONSTRAINT fk84umwt3ihkiggf03us5gq116j FOREIGN KEY (product_id) REFERENCES tbl_product(id);
ALTER TABLE tbl_cart_item
    ADD CONSTRAINT fkhaw0aw4g8s9icxekpl4oi715a FOREIGN KEY (cart_id) REFERENCES tbl_cart(id);
ALTER TABLE tbl_order
    ADD CONSTRAINT fkhyolniflkctr0p6bp4t8me9vj FOREIGN KEY (user_id) REFERENCES tbl_user(id);
ALTER TABLE tbl_order_item
    ADD CONSTRAINT fk1oy9x003q55eqmuiv0y8a15e FOREIGN KEY (product_id) REFERENCES tbl_product(id);
ALTER TABLE tbl_order_item
    ADD CONSTRAINT fkmkqpajkg6p2wq4owcv1v08pc5 FOREIGN KEY (order_id) REFERENCES tbl_order(id);
ALTER TABLE tbl_payment
    ADD CONSTRAINT fkac54xp3r2r3m9datds9351ric FOREIGN KEY (order_id) REFERENCES tbl_order(id);
ALTER TABLE tbl_product
    ADD CONSTRAINT fkbcejabdqv6xehq9386c69job FOREIGN KEY (user_id) REFERENCES tbl_user(id);
ALTER TABLE tbl_product
    ADD CONSTRAINT fkfq7110lh85cseoy13cgni7pet FOREIGN KEY (category_id) REFERENCES tbl_category(id);
ALTER TABLE tbl_product_image
    ADD CONSTRAINT fkmoh9bwnhu81rx27vxwxbg80up FOREIGN KEY (product_id) REFERENCES tbl_product(id);
ALTER TABLE tbl_refresh_token
    ADD CONSTRAINT fkjy4oisva8l5wptq3sslq6x5d1 FOREIGN KEY (user_id) REFERENCES tbl_user(id);
ALTER TABLE tbl_shipping_address
    ADD CONSTRAINT fkm1mg4tlnovh8r80doh7awhbv7 FOREIGN KEY (user_id) REFERENCES tbl_user(id);
ALTER TABLE tbl_shipment
    ADD CONSTRAINT fkb1wxiaexwu36k7740xfs776x1 FOREIGN KEY (order_id) REFERENCES tbl_order(id);

COMMIT;
