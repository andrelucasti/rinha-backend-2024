-- Coloque scripts iniciais aqui
CREATE TABLE clientes (
                          id serial primary key,
                          name varchar(100) not null,
                          "limit" integer not null,
                          balance integer not null default 0,
                          created_at timestamp default now(),
                          updated_at timestamp default now()
);

create table transactions (
                              id serial primary key,
                              description varchar(100) not null,
                              value integer not null,
                              transaction_type char not null,
                              client_id integer not null,
                              created_at timestamp default now(),
                              updated_at timestamp default now()
);

DO $$
BEGIN
INSERT INTO clientes (name, "limit")
VALUES
    ('o barato sai caro', 1000 * 100),
    ('zan corp ltda', 800 * 100),
    ('les cruders', 10000 * 100),
    ('padaria joia de cocaia', 100000 * 100),
    ('kid mais', 5000 * 100);
END; $$