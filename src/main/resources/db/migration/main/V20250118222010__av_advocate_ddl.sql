CREATE TABLE advocate (
    id UUID NOT NULL,
    tenantid VARCHAR(128) NOT NULL,
    applicationnumber VARCHAR(64),
    barregistrationnumber VARCHAR(64),
    advocatetype VARCHAR(64) NOT NULL,
    organisationid UUID,
    individualid VARCHAR(255),
    isactive BOOLEAN DEFAULT true,
    workflow JSONB,
    documents JSONB,
    auditdetails JSONB,
    createdBy character varying(64),
    lastModifiedBy character varying(64),
    createdTime bigint,
    lastModifiedTime bigint,
    CONSTRAINT unq_advocate UNIQUE (tenantid, applicationnumber)
);

CREATE INDEX idx_advocate_id ON advocate (id);
CREATE INDEX idx_advocate_applicationnumber ON advocate (applicationnumber);
CREATE INDEX idx_advocate_barregistrationnumber ON advocate (barregistrationnumber);