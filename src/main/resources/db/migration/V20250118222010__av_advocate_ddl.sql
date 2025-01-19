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
    additionaldetails JSONB,
    CONSTRAINT unq_advocate UNIQUE (tenantid, applicationnumber)
);
