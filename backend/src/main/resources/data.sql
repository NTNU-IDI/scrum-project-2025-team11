-- Addresses (Norske eksempler)
INSERT INTO `ADDRESS` (street, postal_code, city, latitude, longitude) VALUES
  ('Storgata 1',      '0155', 'Oslo',   59.912345, 10.752345),
  ('Bryggedokka 12',  '5003', 'Bergen', 60.393000,  5.324000);

-- Households (Husholdninger)
INSERT INTO `HOUSEHOLD` (name, member_count, address_id) VALUES
  ('Familien Nordberg', 4, 1),
  ('Familien Solbakken', 3, 2);

-- Users (Brukere)
INSERT INTO `USER_ACCOUNT` (email, first_name, last_name, password, role, household_id) VALUES
  ('super@beredskap.no', 'Trond', 'Bakke',      'passordhash1', 'super_admin', 1),
  ('admin@nordberg.no', 'Ida', 'Tulliong',      'passordhash2', 'admin',       1),
  ('bruker@solbakken.no', 'Jon', 'Solbakken',     'passordhash3', 'normal',      2);

-- Networks (Nettverk)
INSERT INTO `NETWORK` (name, description) VALUES
  ('Lokalt Nødnettverk',    'Nabolagets beredskapsnettverk'),
  ('Nødetater',             'Offentlige nødetater og samarbeidspartnere');

-- Network ↔ Household links (Nettverk–Husholdning)
INSERT INTO `NETWORK_HOUSEHOLD` (network_id, household_id) VALUES
  (1, 1),
  (1, 2),
  (2, 1);

-- Items (Varer)
INSERT INTO `ITEM` (name, description) VALUES
  ('Vann',         'Flasker med drikkevann, 1 L hver'),
  ('Matrasjoner',  'Hermetisk mat, 500 g per boks'),
  ('Stearinlys',   'Standard stearinlys med veke'),
  ('Lommelykt',    'LED-lommelykt med batterier');

-- Inventory per household (Beredskapslager)
INSERT INTO `HOUSEHOLD_ITEMS` (household_id, item_id, quantity, unit, acquired_date, expiration_date) VALUES
  (1, 1, 10, 'stk', '2025-04-01', '2030-04-01'),
  (1, 3,  5, 'stk', '2025-03-15', NULL),
  (2, 2, 20, 'stk', '2025-04-10', '2026-04-10'),
  (2, 4,  2, 'stk', '2025-02-20', NULL);

-- Points of Interest (Interessepunkter)
INSERT INTO `POINT_OF_INTEREST` (name, icon_type, description, latitude, longitude) VALUES
  ('Sentral Sykehus',    'medical',       'Hovedsykehus i bykjernen',         59.915000, 10.754000),
  ('Bytilfluktsrom',     'shelter',       'Underjordisk tilfluktsrom',        59.913500, 10.752800),
  ('Flomfareområde',     'danger',        'Område utsatt for flom ved høyt vannnivå', 59.914200, 10.755100),
  ('Møteplass',          'assembly_point','Felles samlingssted ved evakuering', 59.912800, 10.753400);

-- Events (Hendelser)
INSERT INTO `EVENT` (name, description, icon_type, time_start, time_end, latitude, longitude, radius) VALUES
  ('Brannøvelse',       'Kvartalsvis brannøvelse i nabolaget',   'normal',         '2025-05-01', '2025-05-01', 59.910000, 10.750000, 100),
  ('Flomvarsel',        'Varsel om mulig flom i lavtliggende område', 'danger',        '2025-04-20', '2025-04-25', 59.914000, 10.755000, 500),
  ('Nabolagsmøte',      'Informasjonsmøte om beredskapstiltak',   'assembly_point','2025-06-01', '2025-06-01', 59.912000, 10.753000, 50);
