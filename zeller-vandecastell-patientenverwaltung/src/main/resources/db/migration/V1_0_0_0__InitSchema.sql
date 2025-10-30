
    create sequence arzt_seq start with 1 increment by 50;

    create sequence arztpraxen_seq start with 1 increment by 50;

    create sequence behandlungen_seq start with 1 increment by 50;

    create sequence behandlungsraum_seq start with 1 increment by 50;

    create sequence mitarbeiter_seq start with 1 increment by 50;

    create sequence patient_seq start with 1 increment by 50;

    create sequence rechnung_seq start with 1 increment by 50;

    create sequence warteraum_seq start with 1 increment by 50;

    create table arzt (
        id bigint not null,
        art char(1),
        fachgebiet char(1) check (fachgebiet in ('O','H','C','G','A')),
        geb_datum date,
        lkennzahl varchar(3),
        okennzahl varchar(4),
        plz varchar(4),
        haus_nr varchar(6),
        svnr bigint,
        rufnummer varchar(13),
        stadt varchar(64),
        strasse varchar(128),
        email varchar(255),
        name varchar(255),
        primary key (id)
    );

    create table arztpraxen (
        id bigint not null,
        art char(1),
        ist_kassenarzt boolean,
        lkennzahl varchar(3),
        okennzahl varchar(4),
        plz varchar(4),
        haus_nr varchar(6),
        rufnummer varchar(13),
        stadt varchar(64),
        strasse varchar(128),
        name varchar(255) not null,
        primary key (id)
    );

    create table behandlungen (
        id bigint not null,
        arzt_id bigint not null,
        patient_id bigint not null,
        behandlungsraum_id bigint,
        beginn timestamp(6),
        ende timestamp(6),
        diagnose varchar(255),
        primary key (id)
    );

    create table behandlungsraum (
        id bigint not null,
        is_frei boolean,
        behandlungsraum_id bigint,
        ausstattung varchar(255),
        name varchar(255) not null,
        primary key (id)
    );

    create table behandlungsraum_behandlungen (
        behandlungen_id bigint not null unique,
        behandlungsraum_id bigint not null
    );

    create table medikamente (
        behandlung_id bigint not null,
        name varchar(255),
        wirkstoff varchar(255)
    );

    create table mitarbeiter (
        arztpraxis_id bigint,
        id bigint not null,
        art char(1),
        geb_datum date,
        lkennzahl varchar(3),
        okennzahl varchar(4),
        plz varchar(4),
        haus_nr varchar(6),
        gehalt bigint check ((gehalt>=0) and (gehalt<=100000)),
        mitarbeiter_id bigint,
        svnr bigint,
        rufnummer varchar(13),
        stadt varchar(64),
        strasse varchar(128),
        email varchar(255) not null,
        name varchar(255),
        primary key (id)
    );

    create table patient (
        id bigint not null,
        art char(1),
        geb_datum date,
        lkennzahl varchar(3),
        okennzahl varchar(4),
        plz varchar(4),
        versicherungsart char(1) check ( versicherungsart in ('K','P')),
        haus_nr varchar(6),
        svnr bigint,
        rufnummer varchar(13),
        stadt varchar(64),
        strasse varchar(128),
        name varchar(255),
        primary key (id)
    );

    create table rechnung (
        id bigint not null,
        arzt_id bigint not null,
        patient_id bigint not null,
        betrag float(53) not null,
        bezahlt boolean not null,
        zahlungsart char(1),
        datum timestamp(6),
        primary key (id)
    );

    create table warteraum (
        id bigint not null,
        anzahl_sitzplaetze integer not null check (anzahl_sitzplaetze>=0),
        warteraum_id bigint,
        name varchar(255) not null,
        primary key (id)
    );

    alter table arztpraxen add column arzt_id BIGINT;


    alter table if exists arztpraxen
       add constraint FK_Arztpraxis_2_Arzt 
       foreign key (arzt_id)
       references arzt;

    alter table if exists behandlungen 
       add constraint FKgugtelsgafems7qb4sedu7b5d 
       foreign key (arzt_id) 
       references arzt;

    alter table if exists behandlungen 
       add constraint FK_Behandlungsraum_2_Behandlung 
       foreign key (behandlungsraum_id) 
       references behandlungsraum;

    alter table if exists behandlungen 
       add constraint FK_Patient_2_Behandlung 
       foreign key (patient_id) 
       references patient;

    alter table if exists behandlungsraum 
       add constraint FK_Arztpraxis_2_Behandlungsraum 
       foreign key (behandlungsraum_id) 
       references arztpraxen;

    alter table if exists behandlungsraum_behandlungen 
       add constraint FKbussjw7tpbkclskwl5nu06iiv 
       foreign key (behandlungen_id) 
       references behandlungen;

    alter table if exists behandlungsraum_behandlungen 
       add constraint FKp2ymxgv76kb8twh8pd7sb5d5p 
       foreign key (behandlungsraum_id) 
       references behandlungsraum;

    alter table if exists medikamente 
       add constraint FK_Medikament_2_Behandlung 
       foreign key (behandlung_id) 
       references behandlungen;

    alter table if exists mitarbeiter 
       add constraint FKdk8fqh3eaypsg6g6uh4bnl6t3 
       foreign key (arztpraxis_id) 
       references arztpraxen;

    alter table if exists mitarbeiter 
       add constraint FK_Arztpraxis_2_Mitarbeiter 
       foreign key (mitarbeiter_id) 
       references arztpraxen;

    alter table if exists rechnung 
       add constraint FK_Rechnung_2_Arzt 
       foreign key (arzt_id) 
       references arzt;

    alter table if exists rechnung 
       add constraint FK_Rechnung_2_Patient 
       foreign key (patient_id) 
       references patient;

    alter table if exists warteraum 
       add constraint FK_Arztpraxis_2_Warteraum 
       foreign key (warteraum_id) 
       references arztpraxen;

