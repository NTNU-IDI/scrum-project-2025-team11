-- Addresses (Norske eksempler)
INSERT INTO `ADDRESS` (id, street, postal_code, city, latitude, longitude) VALUES
  (1, 'Storgata 1',      '0155', 'Oslo',   59.912345, 10.752345),
  (2, 'Bryggedokka 12',  '5003', 'Bergen', 60.393000,  5.324000);

-- Households (Husholdninger)
INSERT INTO `HOUSEHOLD` (id, name, member_count, address_id) VALUES
  (1, 'Familien Nordberg', 4, 1),
  (2, 'Familien Solbakken', 3, 2);

-- Users (Brukere)
INSERT INTO `USER_ACCOUNT` (id, email, password, role, household_id) VALUES
  (1, 'super@beredskap.no',      'passordhash1', 'super_admin', 1),
  (2, 'admin@nordberg.no',       'passordhash2', 'admin',       1),
  (3, 'bruker@solbakken.no',     'passordhash3', 'normal',      2);

-- Networks (Nettverk)
INSERT INTO `NETWORK` (id, name, description) VALUES
  (1, 'Lokalt Nødnettverk',    'Nabolagets beredskapsnettverk'),
  (2, 'Nødetater',             'Offentlige nødetater og samarbeidspartnere');

-- Network ↔ Household links (Nettverk–Husholdning)
INSERT INTO `NETWORK_HOUSEHOLD` (network_id, household_id) VALUES
  (1, 1),
  (1, 2),
  (2, 1);

-- Items (Varer)
INSERT INTO `ITEM` (id, name, description) VALUES
  (1, 'Vann',         'Flasker med drikkevann, 1 L hver'),
  (2, 'Matrasjoner',  'Hermetisk mat, 500 g per boks'),
  (3, 'Stearinlys',   'Standard stearinlys med veke'),
  (4, 'Lommelykt',    'LED-lommelykt med batterier');

-- Inventory per household (Beredskapslager)
INSERT INTO `HOUSEHOLD_ITEMS` (household_id, item_id, quantity, unit, acquired_date, expiration_date) VALUES
  (1, 1, 10, 'stk', '2025-04-01', '2030-04-01'),
  (1, 3,  5, 'stk', '2025-03-15', NULL),
  (2, 2, 20, 'stk', '2025-04-10', '2026-04-10'),
  (2, 4,  2, 'stk', '2025-02-20', NULL);

-- Points of Interest (Interessepunkter)
INSERT INTO `POINT_OF_INTEREST` (id, name, icon_type, description, latitude, longitude) VALUES
  (1, 'Sentral Sykehus',    'medical',       'Hovedsykehus i bykjernen',         59.915000, 10.754000),
  (2, 'Bytilfluktsrom',     'shelter',       'Underjordisk tilfluktsrom',        59.913500, 10.752800),
  (3, 'Flomfareområde',     'danger',        'Område utsatt for flom ved høyt vannnivå', 59.914200, 10.755100),
  (4, 'Møteplass',          'assembly_point','Felles samlingssted ved evakuering', 59.912800, 10.753400);

-- Events (Hendelser)
INSERT INTO `EVENT` (id, name, description, icon_type, time_start, time_end, latitude, longtitude, radius) VALUES
  (1, 'Brannøvelse',       'Kvartalsvis brannøvelse i nabolaget',   'normal',         '2025-05-01', '2025-05-01', 59.910000, 10.750000, 100),
  (2, 'Flomvarsel',        'Varsel om mulig flom i lavtliggende område', 'danger',        '2025-04-20', '2025-04-25', 59.914000, 10.755000, 500),
  (3, 'Nabolagsmøte',      'Informasjonsmøte om beredskapstiltak',   'assembly_point','2025-06-01', '2025-06-01', 59.912000, 10.753000, 50);
