CREATE TABLE transactions(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reference_number VARCHAR(50) UNIQUE NOT NULL,
    source_account_number VARCHAR(20) NOT NULL,
    destination_account_number VARCHAR(20) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Index untuk mempercepat query pencarian riwayat transaksi
CREATE INDEX idx_transactions_source_account ON transactions(source_account_number);
CREATE INDEX idx_transactions_destination_account ON transactions(destination_account_number);
CREATE INDEX idx_transactions_reference_number ON transactions(reference_number);