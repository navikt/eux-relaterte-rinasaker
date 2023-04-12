CREATE TABLE relaterte_rinasaker
(
    relaterte_rinasaker_gruppe_id uuid,
    rinasak_id                    varchar(31),
    CONSTRAINT fk_relaterte_rinasaker_gruppe
        FOREIGN KEY (relaterte_rinasaker_gruppe_id)
            REFERENCES relaterte_rinasaker_gruppe (relaterte_rinasaker_gruppe_id)
);

CREATE INDEX ON relaterte_rinasaker (relaterte_rinasaker_gruppe_id);
