DO $$
BEGIN
   IF EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_name = 'usuarios'
        AND column_name = 'name'
   ) THEN
      ALTER TABLE usuarios DROP COLUMN name;
   END IF;

   IF EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_name = 'usuarios'
        AND column_name = 'password'
   ) THEN
      ALTER TABLE usuarios DROP COLUMN password;
   END IF;
END $$;
