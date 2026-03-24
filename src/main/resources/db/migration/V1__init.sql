CREATE TABLE creators (
    id              BIGSERIAL PRIMARY KEY,
    external_id     VARCHAR(255) NOT NULL UNIQUE,
    creator_name    VARCHAR(512) NOT NULL,
    account_created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    trust_history_score INTEGER NOT NULL DEFAULT 50,
    total_uploads   INTEGER NOT NULL DEFAULT 0,
    flagged_uploads INTEGER NOT NULL DEFAULT 0,
    country         VARCHAR(64),
    account_status  VARCHAR(32) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE uploads (
    id                  BIGSERIAL PRIMARY KEY,
    creator_id          BIGINT NOT NULL REFERENCES creators (id),
    song_title          VARCHAR(512) NOT NULL,
    artist_name         VARCHAR(512) NOT NULL,
    album_name          VARCHAR(512),
    label_name          VARCHAR(512),
    release_date        DATE,
    genre               VARCHAR(128),
    language            VARCHAR(64),
    lyrics              TEXT,
    optional_external_claims TEXT,
    normalized_title    VARCHAR(512) NOT NULL,
    normalized_artist   VARCHAR(512) NOT NULL,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    upload_status       VARCHAR(32) NOT NULL DEFAULT 'PENDING_VALIDATION'
);

CREATE INDEX idx_uploads_creator ON uploads (creator_id);
CREATE INDEX idx_uploads_norm_title_artist ON uploads (normalized_title, normalized_artist);
CREATE INDEX idx_uploads_created_at ON uploads (created_at);

CREATE TABLE validation_results (
    id                      BIGSERIAL PRIMARY KEY,
    upload_id               BIGINT NOT NULL UNIQUE REFERENCES uploads (id) ON DELETE CASCADE,
    duplicate_score         DOUBLE PRECISION,
    title_similarity_score  DOUBLE PRECISION,
    trust_score             INTEGER NOT NULL,
    risk_level              VARCHAR(16) NOT NULL,
    decision                VARCHAR(32) NOT NULL,
    reason_codes            JSONB NOT NULL DEFAULT '[]'::jsonb,
    validated_at            TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE suspicious_alerts (
    id              BIGSERIAL PRIMARY KEY,
    upload_id       BIGINT NOT NULL REFERENCES uploads (id) ON DELETE CASCADE,
    alert_type      VARCHAR(64) NOT NULL,
    alert_message   TEXT,
    status          VARCHAR(32) NOT NULL DEFAULT 'OPEN',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_alerts_upload ON suspicious_alerts (upload_id);
CREATE INDEX idx_alerts_created ON suspicious_alerts (created_at);
