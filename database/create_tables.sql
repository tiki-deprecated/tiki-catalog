/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

-- -----------------------------------------------------------------------
-- ADDRESS
-- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS address (
    address_id BIGSERIAL PRIMARY KEY,
    address TEXT NOT NULL,
    app_id TEXT NOT NULL,
    user_id TEXT,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (app_id, address)
);

--- -----------------------------------------------------------------------
--- BLOCK
--- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS block (
    block_id BIGSERIAL PRIMARY KEY,
    block_hash TEXT NOT NULL,
    src_url TEXT NOT NULL UNIQUE ,
    created TIMESTAMP WITH TIME ZONE NOT NULL
);

--- -----------------------------------------------------------------------
--- TITLE
--- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS title (
    title_id BIGSERIAL PRIMARY KEY,
    txn_hash TEXT NOT NULL UNIQUE,
    block_id BIGINT NOT NULL REFERENCES block(block_id),
    address_id BIGINT NOT NULL REFERENCES address(address_id),
    ptr TEXT NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL
);

--- -----------------------------------------------------------------------
--- LICENSE
--- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS license (
     license_id BIGSERIAL PRIMARY KEY,
     txn_hash TEXT NOT NULL UNIQUE,
     block_id BIGINT NOT NULL REFERENCES block(block_id),
     address_id BIGINT NOT NULL REFERENCES address(address_id),
     title_id BIGINT REFERENCES title(title_id),
     created TIMESTAMP WITH TIME ZONE NOT NULL
);

--- -----------------------------------------------------------------------
--- USE
--- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS use (
    use_id BIGSERIAL PRIMARY KEY,
    usecase TEXT NOT NULL,
    destination TEXT,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE(usecase, destination)
);

--- -----------------------------------------------------------------------
--- LICENSE_USE
--- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS license_use (
    license_use_id BIGSERIAL PRIMARY KEY,
    license_id BIGINT NOT NULL REFERENCES license(license_id),
    use_id BIGINT NOT NULL REFERENCES use(use_id),
    created TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    UNIQUE(license_id, use_id)
);

--- -----------------------------------------------------------------------
--- TAG
--- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS tag (
    tag_id BIGSERIAL PRIMARY KEY,
    val TEXT NOT NULL UNIQUE,
    created TIMESTAMP WITH TIME ZONE NOT NULL
);

--- -----------------------------------------------------------------------
--- TITLE_TAG
--- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS title_tag (
    title_tag_id BIGSERIAL PRIMARY KEY,
    title_id BIGINT NOT NULL REFERENCES title(title_id),
    tag_id BIGINT NOT NULL REFERENCES tag(tag_id),
    created TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    UNIQUE(title_id, tag_id)
);
