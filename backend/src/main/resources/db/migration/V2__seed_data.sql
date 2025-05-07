-- Addresses (Norske eksempler)
INSERT INTO `ADDRESS` (street, postal_code, city, latitude, longitude) VALUES
  ('Storgata 1',      '0155', 'Oslo',   59.912345, 10.752345),
  ('Bryggedokka 12',  '5003', 'Bergen', 60.393000,  5.324000);

-- Households (Husholdninger)
INSERT INTO `HOUSEHOLD` (name, member_count, address_id) VALUES
  ('Familien Nordberg', 4, 1),
  ('Familien Solbakken', 3, 2);

-- Users (Brukere)
INSERT INTO `USER_ACCOUNT` (email, username, first_name, last_name, password, role, household_id) VALUES
  ('super@beredskap.no', 'thelegend27', 'Trond', 'Bakke',      '$2a$10$mKv8aQpYwh/IwSKzlRx6iuWpnJ.ixUhDisP1mdUlk6rsGHEk2KImu', 'super_admin', 1),
  ('admin@nordberg.no', 'SuperIda', 'Ida', 'Tulliong',      '$2a$10$ouCmMsEaDgrm8OxoVYzsmuq70qcNYmRQvLFkrByyIRgqh9jNk2Cwy', 'admin',       1),
  ('bruker@solbakken.no', 'SolbakkenJon', 'Jon', 'Solbakken',     '$2a$10$y11CfXG.abqs1zbA6cYOXeqO.n.9D2tFasTeGZvw7XJQjfbA3omiC', 'normal',      2),
  ('spradmn.krisefikser@gmail.com', 'superadmin', 'Garv', 'Sood', '$2a$10$mKv8aQpYwh/IwSKzlRx6iuWpnJ.ixUhDisP1mdUlk6rsGHEk2KImu', 'super_admin',2),
  ('admn.krisefikser@gmail.com', 'admin','Jonas', 'Johansen', '$2a$10$ouCmMsEaDgrm8OxoVYzsmuq70qcNYmRQvLFkrByyIRgqh9jNk2Cwy', 'admin', 2),
  ('bruker.krisefikser@gmail.com', 'test', 'August', '', '$2a$10$y11CfXG.abqs1zbA6cYOXeqO.n.9D2tFasTeGZvw7XJQjfbA3omiC', 'normal', 2);

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
INSERT INTO `EVENT` (name, description, icon_type, start_time, end_time, latitude, longitude, radius) VALUES
  ('Brannøvelse',       'Kvartalsvis brannøvelse i nabolaget',   'normal',         '2025-05-01', '2025-05-01', 59.910000, 10.750000, 100),
  ('Flomvarsel',        'Varsel om mulig flom i lavtliggende område', 'danger',        '2025-04-20', '2025-04-25', 59.914000, 10.755000, 500),
  ('Nabolagsmøte',      'Informasjonsmøte om beredskapstiltak',   'assembly_point','2025-06-01', '2025-06-01', 59.912000, 10.753000, 50);
