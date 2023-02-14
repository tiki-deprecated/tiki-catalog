/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

-- -----------------------------------------------------------------------
-- ADDRESS
-- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS address (
    id BIGSERIAL PRIMARY KEY,
    address BYTEA NOT NULL,
    app_id TEXT NOT NULL,
    created_utc TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (app_id, address)
);

-- -----------------------------------------------------------------------
-- BLOCK
-- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS block (
    id BIGSERIAL PRIMARY KEY,
    block_hash BYTEA NOT NULL UNIQUE,
    address_id BIGINT NOT NULL,
    src_url TEXT NOT NULL,
    created_utc TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY(address_id) REFERENCES address(id)
);

-- -----------------------------------------------------------------------
-- TRANSACTION
-- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS txn (
    id BIGSERIAL PRIMARY KEY,
    txn_hash BYTEA NOT NULL UNIQUE,
    block_id BIGINT NOT NULL,
    created_utc TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY(block_id) REFERENCES block(id)
);
