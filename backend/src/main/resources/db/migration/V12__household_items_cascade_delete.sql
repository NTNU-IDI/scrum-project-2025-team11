-- V12__household_items_cascade_delete.sql

-- 1) Drop the old FK so we can rebuild it
ALTER TABLE household_items
  DROP CONSTRAINT FK_HI_HH;

-- 2) Rebuild the composite PK (this also lets us enforce NOT NULL on acquired_date)
ALTER TABLE household_items
  DROP PRIMARY KEY;

ALTER TABLE household_items
  ALTER COLUMN acquired_date SET NOT NULL;

ALTER TABLE household_items
  ADD PRIMARY KEY (household_id, item_id, acquired_date);

-- 3) Re-add the FK with ON DELETE CASCADE
ALTER TABLE household_items
  ADD CONSTRAINT FK_HI_HH
    FOREIGN KEY (household_id)
    REFERENCES household(id)
    ON DELETE CASCADE;
