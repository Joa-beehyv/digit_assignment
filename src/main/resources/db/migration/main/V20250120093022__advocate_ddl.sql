CREATE TABLE advocate (
    id                    VARCHAR(128) NOT NULL,
    tenantid              VARCHAR(128) NOT NULL,
    applicationnumber     VARCHAR(64),
    barregistrationnumber VARCHAR(64),
    advocatetype          VARCHAR(64) NOT NULL,
    organisationid        VARCHAR(255),
    individualid          VARCHAR(255),
    isactive              BOOLEAN DEFAULT true,
    createdby             VARCHAR(256)  NOT NULL,
    createdtime           bigint                  NOT NULL,
    lastmodifiedby        VARCHAR(256),
    lastmodifiedtime      bigint,
    CONSTRAINT unq_advocate UNIQUE (tenantid, applicationnumber)
);

CREATE INDEX idx_advocate_id ON advocate (id);
CREATE INDEX idx_advocate_applicationnumber ON advocate (applicationnumber);
CREATE INDEX idx_advocate_barregistrationnumber ON advocate (barregistrationnumber);