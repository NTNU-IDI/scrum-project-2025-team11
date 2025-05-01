CREATE TYPE role_enum AS ENUM (
  'normal',
  'admin',
  'super_admin'
);

CREATE TYPE poi_icon_enum AS ENUM (
  'none',
  'point',
  'normal',
  'danger',
  'assembly_point',
  'medical',
  'shelter'
);

CREATE TYPE evt_icon_enum AS ENUM (
  'none',
  'point',
  'normal',
  'danger',
  'assembly_point',
  'medical',
  'shelter'
);


-- ADDRESS
CREATE TABLE ADDRESS (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  street      VARCHAR(255),
  postal_code VARCHAR(20),
  city        VARCHAR(100),
  latitude    DECIMAL(9,6),
  longitude   DECIMAL(9,6)
);

-- HOUSEHOLD
CREATE TABLE HOUSEHOLD (
  id            INT PRIMARY KEY AUTO_INCREMENT,
  name          VARCHAR(255) NOT NULL,
  member_count  INT          NOT NULL,
  address_id    INT,
  CONSTRAINT FK_HH_ADDR FOREIGN KEY (address_id) REFERENCES ADDRESS(id)
);

-- USER_ACCOUNT
CREATE TABLE USER_ACCOUNT (
  id           INT PRIMARY KEY AUTO_INCREMENT,
  email        VARCHAR(255) NOT NULL UNIQUE,
  username    VARCHAR(255) NOT NULL UNIQUE,
  first_name   VARCHAR(255) NOT NULL,
  last_name    VARCHAR(255) NOT NULL,
  password     VARCHAR(255) NOT NULL,
  role         VARCHAR(20)  NOT NULL DEFAULT 'normal',
  household_id INT,
  CONSTRAINT FK_USER_HH FOREIGN KEY (household_id) REFERENCES HOUSEHOLD(id),
  CONSTRAINT CHK_USER_ROLE CHECK (role IN ('normal','admin','super_admin'))
);

-- NETWORK
CREATE TABLE NETWORK (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(255) NOT NULL,
  description TEXT
);

-- NETWORK_HOUSEHOLD
CREATE TABLE NETWORK_HOUSEHOLD (
  network_id   INT NOT NULL,
  household_id INT NOT NULL,
  PRIMARY KEY (network_id,household_id),
  CONSTRAINT FK_NH_NETWORK   FOREIGN KEY (network_id)   REFERENCES NETWORK(id),
  CONSTRAINT FK_NH_HOUSEHOLD FOREIGN KEY (household_id) REFERENCES HOUSEHOLD(id)
);

-- ITEM
CREATE TABLE ITEM (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  name        VARCHAR(255) NOT NULL,
  description TEXT
);

-- HOUSEHOLD_ITEMS
CREATE TABLE HOUSEHOLD_ITEMS (
  household_id    INT NOT NULL,
  item_id         INT NOT NULL,
  quantity        DECIMAL(20,6) NOT NULL,
  unit            VARCHAR(50),
  acquired_date   DATE,
  expiration_date DATE,
  PRIMARY KEY (household_id,item_id),
  CONSTRAINT FK_HI_HH   FOREIGN KEY (household_id) REFERENCES HOUSEHOLD(id),
  CONSTRAINT FK_HI_ITEM FOREIGN KEY (item_id)      REFERENCES ITEM(id)
);

-- POINT_OF_INTEREST
CREATE TABLE POINT_OF_INTEREST (
  id           INT            PRIMARY KEY AUTO_INCREMENT,
  name         VARCHAR(255)   NOT NULL,
  icon_type    poi_icon_enum  NOT NULL DEFAULT 'point',
  description  TEXT,
  latitude     DECIMAL(9,6),
  longitude    DECIMAL(9,6)
);

-- EVENT
CREATE TABLE EVENT (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  name        TEXT    NOT NULL,
  description TEXT,
  icon_type   VARCHAR(20) NOT NULL DEFAULT 'none',
  time_start  DATE,
  time_end    DATE,
  latitude    DECIMAL(9,6),
  longitude  DECIMAL(9,6),
  radius      INT,
  CONSTRAINT CHK_EVT_ICON CHECK (icon_type IN (
    'none','point','normal','danger','assembly_point','medical','shelter'
  ))
);

-- Now create the indexes that were inline in MySQL:

CREATE INDEX IDX_USER_HOUSEHOLD ON USER_ACCOUNT(household_id);
CREATE INDEX IDX_HH_ADDRESS       ON HOUSEHOLD(address_id);
CREATE INDEX IDX_NH_NETWORK       ON NETWORK_HOUSEHOLD(network_id);
CREATE INDEX IDX_NH_HOUSEHOLD     ON NETWORK_HOUSEHOLD(household_id);
CREATE INDEX IDX_HI_HOUSEHOLD     ON HOUSEHOLD_ITEMS(household_id);
CREATE INDEX IDX_HI_ITEM          ON HOUSEHOLD_ITEMS(item_id);
