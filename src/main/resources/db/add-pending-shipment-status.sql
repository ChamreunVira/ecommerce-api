-- ShipmentStatus.PENDING was added to the Java enum but the existing
-- PostgreSQL check constraint was not updated by Hibernate ddl-auto.
ALTER TABLE tbl_shipment DROP CONSTRAINT IF EXISTS tbl_shipment_status_check;
ALTER TABLE tbl_shipment ADD CONSTRAINT tbl_shipment_status_check
    CHECK (status::text = ANY (ARRAY[
        'PENDING',
        'IN_TRANSMIT',
        'DELAYED',
        'DELIVERED',
        'CANCELLED'
    ]::text[]));
