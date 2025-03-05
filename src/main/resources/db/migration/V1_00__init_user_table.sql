CREATE TABLE users
(
    id           UUID PRIMARY KEY,
    username     VARCHAR(255) UNIQUE NOT NULL,
    password     VARCHAR(255)        NOT NULL,
    created_by   UUID,
    created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by   UUID,
    updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create a unique index on the username column
CREATE INDEX idx_users_username ON users USING btree(username);
