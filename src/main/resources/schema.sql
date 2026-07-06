-- Table: public.base_table

-- DROP TABLE IF EXISTS public.base_table;

CREATE TABLE IF NOT EXISTS public.base_table
(
    id uuid NOT NULL,
    created_at timestamp(3) with time zone,
    updated_at timestamp(3) with time zone,
    created_by character varying(32) COLLATE pg_catalog."default",
    modified_by character varying(32) COLLATE pg_catalog."default"
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.base_table
    OWNER to admin;


-- Schema core
CREATE SCHEMA IF NOT EXISTS core;


-- Table: core.book

-- DROP TABLE IF EXISTS core.book;

CREATE TABLE IF NOT EXISTS core.book
(
    -- Inherited from table public.base_table: id uuid NOT NULL,
    -- Inherited from table public.base_table: created_at timestamp(3) with time zone,
    -- Inherited from table public.base_table: updated_at timestamp(3) with time zone,
    -- Inherited from table public.base_table: created_by character varying(32) COLLATE pg_catalog."default" NOT NULL,
    -- Inherited from table public.base_table: modified_by character varying(32) COLLATE pg_catalog."default",
    is_available boolean NOT NULL DEFAULT true,
    is_deleted boolean NOT NULL DEFAULT false,
    published_year integer NOT NULL,
    author_name character varying(32) COLLATE pg_catalog."default" NOT NULL,
    isbn character varying(20) COLLATE pg_catalog."default" NOT NULL,
    title character varying(128) COLLATE pg_catalog."default" NOT NULL,
    version bigint DEFAULT 0,
    CONSTRAINT book_pkey PRIMARY KEY (id),
    CONSTRAINT book_isbn_key UNIQUE (isbn)
    )
    INHERITS (public.base_table)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS core.book
    OWNER to admin;


-- Table: core.borrow_book

-- DROP TABLE IF EXISTS core.borrow_book;

CREATE TABLE IF NOT EXISTS core.borrow_book
(
    -- Inherited from table public.base_table: id uuid NOT NULL,
    -- Inherited from table public.base_table: created_at timestamp(3) with time zone,
    -- Inherited from table public.base_table: updated_at timestamp(3) with time zone,
    -- Inherited from table public.base_table: created_by character varying(32) COLLATE pg_catalog."default" NOT NULL,
    -- Inherited from table public.base_table: modified_by character varying(32) COLLATE pg_catalog."default",
    book_id uuid NOT NULL,
    borrower_name character varying(32) COLLATE pg_catalog."default" NOT NULL,
    borrow_date timestamp(3) without time zone NOT NULL,
    return_date timestamp(3) without time zone,
    CONSTRAINT borrow_book_pkey PRIMARY KEY (id),
    CONSTRAINT borrow_book_book_id_fkey FOREIGN KEY (book_id)
    REFERENCES core.book (id) MATCH SIMPLE
                           ON UPDATE NO ACTION
                           ON DELETE NO ACTION
    )
    INHERITS (public.base_table)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS core.borrow_book
    OWNER to admin;
