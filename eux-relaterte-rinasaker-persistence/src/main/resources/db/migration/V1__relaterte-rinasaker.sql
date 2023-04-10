CREATE TABLE relaterte_rinasaker
(
    relaterte_rinasaker_id uuid primary key,
    rinasak_id_a           varchar(31),
    rinasak_id_b           varchar(31),
    beskrivelse            varchar(31),
    opprettet_dato         timestamp
);
