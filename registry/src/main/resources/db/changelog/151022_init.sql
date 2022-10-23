CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

CREATE TABLE IF NOT EXISTS products(
    id BIGSERIAL PRIMARY KEY
    , code VARCHAR(10)
    , name VARCHAR(50)
    , lock BOOLEAN
    , last_updated TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS versions(
    id BIGSERIAL PRIMARY KEY
    , product_id BIGINT not null references products (id)
    , version_code VARCHAR(10)
    , lock BOOLEAN
    , status VARCHAR(25)
    , type VARCHAR(50)
    , product_info JSONB
    , distr_link TEXT
    , size_info BIGINT
    , checksum_check_link TEXT
    , release_date DATE
    , last_updated TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS versions_code_index ON versions(version_code);
CREATE INDEX IF NOT EXISTS versions_date_index ON versions(last_updated DESC);
