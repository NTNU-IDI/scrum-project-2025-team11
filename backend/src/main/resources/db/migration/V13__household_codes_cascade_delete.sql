-- 1) Drop the existing constraint
ALTER TABLE household_codes
  DROP CONSTRAINT fk_household_code_household;

-- 2) Re-add it with ON DELETE CASCADE
ALTER TABLE household_codes
  ADD CONSTRAINT fk_household_code_household
    FOREIGN KEY (household_id)
    REFERENCES household(id)
    ON DELETE CASCADE;
