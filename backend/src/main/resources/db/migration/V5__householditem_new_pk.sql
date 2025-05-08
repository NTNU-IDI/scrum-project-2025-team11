ALTER TABLE household_items
  DROP PRIMARY KEY;

ALTER TABLE household_items
  ALTER COLUMN acquired_date SET NOT NULL;

ALTER TABLE household_items
  ADD PRIMARY KEY (household_id, item_id, acquired_date);