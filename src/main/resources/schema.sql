-- The generated value also covers shops saved before name uniqueness was introduced.
ALTER TABLE shop ADD COLUMN IF NOT EXISTS name_key VARCHAR
    GENERATED ALWAYS AS (LOWER(TRIM(name)));
CREATE UNIQUE INDEX IF NOT EXISTS ux_shop_name ON shop(name_key);
