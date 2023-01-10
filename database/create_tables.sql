/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

-- -----------------------------------------------------------------------
-- ADDRESS
-- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS address (
    address_aid BYTEA PRIMARY KEY,
    address BYTEA NOT NULL,
    aid TEXT NOT NULL,
    created_utc TIMESTAMP WITH TIME ZONE NOT NULL
);

-- -----------------------------------------------------------------------
-- BLOCK
-- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS block (
    block_hash BYTEA PRIMARY KEY,
    address_aid BYTEA NOT NULL,
    src_url TEXT NOT NULL,
    created_utc TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY(address_aid) REFERENCES address(address_aid)
);

-- -----------------------------------------------------------------------
-- TRANSACTION
-- -----------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS txn (
    txn_hash BYTEA PRIMARY KEY,
    block_hash BYTEA NOT NULL,
    created_utc TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY(block_hash) REFERENCES block(block_hash)
);