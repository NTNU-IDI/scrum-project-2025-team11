-- V9__acquired_date_timestamp.sql

-- 1) Drop the existing primary key (no name needed)
ALTER TABLE household_items
  DROP PRIMARY KEY;

-- 2) Change the acquired_date column to a timestamp
ALTER TABLE household_items
  ALTER COLUMN acquired_date SET DATA TYPE TIMESTAMP;

-- 3) Re-create the composite primary key
ALTER TABLE household_items
  ADD PRIMARY KEY (household_id, item_id, acquired_date);
