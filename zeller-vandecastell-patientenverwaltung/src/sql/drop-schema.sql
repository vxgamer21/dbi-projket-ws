
    set client_min_messages = WARNING;

    alter table if exists arzt 
       drop constraint if exists FK65cixdg1koag8t7vd3dx4msty;

    alter table if exists arzt 
       drop constraint if exists FK_Arztpraxis_2_Arzt;

    alter table if exists behandlungen 
       drop constraint if exists FKgugtelsgafems7qb4sedu7b5d;

    alter table if exists behandlungen 
       drop constraint if exists FK_Behandlungsraum_2_Behandlung;

    alter table if exists behandlungen 
       drop constraint if exists FK_Patient_2_Behandlung;

    alter table if exists behandlungsraum 
       drop constraint if exists FK_Arztpraxis_2_Behandlungsraum;

    alter table if exists behandlungsraum_behandlungen 
       drop constraint if exists FKbussjw7tpbkclskwl5nu06iiv;

    alter table if exists behandlungsraum_behandlungen 
       drop constraint if exists FKp2ymxgv76kb8twh8pd7sb5d5p;

    alter table if exists medikamente 
       drop constraint if exists FK_Medikament_2_Behandlung;

    alter table if exists mitarbeiter 
       drop constraint if exists FKdk8fqh3eaypsg6g6uh4bnl6t3;

    alter table if exists mitarbeiter 
       drop constraint if exists FK_Arztpraxis_2_Mitarbeiter;

    alter table if exists rechnung 
       drop constraint if exists FK_Rechnung_2_Arzt;

    alter table if exists rechnung 
       drop constraint if exists FK_Rechnung_2_Patient;

    alter table if exists warteraum 
       drop constraint if exists FK_Arztpraxis_2_Warteraum;

    drop table if exists arzt cascade;

    drop table if exists arztpraxen cascade;

    drop table if exists behandlungen cascade;

    drop table if exists behandlungsraum cascade;

    drop table if exists behandlungsraum_behandlungen cascade;

    drop table if exists medikamente cascade;

    drop table if exists mitarbeiter cascade;

    drop table if exists patient cascade;

    drop table if exists rechnung cascade;

    drop table if exists warteraum cascade;

    drop sequence if exists arzt_seq;

    drop sequence if exists arztpraxen_seq;

    drop sequence if exists behandlungen_seq;

    drop sequence if exists behandlungsraum_seq;

    drop sequence if exists mitarbeiter_seq;

    drop sequence if exists patient_seq;

    drop sequence if exists rechnung_seq;

    drop sequence if exists warteraum_seq;

    set client_min_messages = WARNING;

    alter table if exists arzt 
       drop constraint if exists FK65cixdg1koag8t7vd3dx4msty;

    alter table if exists arzt 
       drop constraint if exists FK_Arztpraxis_2_Arzt;

    alter table if exists behandlungen 
       drop constraint if exists FKgugtelsgafems7qb4sedu7b5d;

    alter table if exists behandlungen 
       drop constraint if exists FK_Behandlungsraum_2_Behandlung;

    alter table if exists behandlungen 
       drop constraint if exists FK_Patient_2_Behandlung;

    alter table if exists behandlungsraum 
       drop constraint if exists FK_Arztpraxis_2_Behandlungsraum;

    alter table if exists behandlungsraum_behandlungen 
       drop constraint if exists FKbussjw7tpbkclskwl5nu06iiv;

    alter table if exists behandlungsraum_behandlungen 
       drop constraint if exists FKp2ymxgv76kb8twh8pd7sb5d5p;

    alter table if exists medikamente 
       drop constraint if exists FK_Medikament_2_Behandlung;

    alter table if exists mitarbeiter 
       drop constraint if exists FKdk8fqh3eaypsg6g6uh4bnl6t3;

    alter table if exists mitarbeiter 
       drop constraint if exists FK_Arztpraxis_2_Mitarbeiter;

    alter table if exists rechnung 
       drop constraint if exists FK_Rechnung_2_Arzt;

    alter table if exists rechnung 
       drop constraint if exists FK_Rechnung_2_Patient;

    alter table if exists warteraum 
       drop constraint if exists FK_Arztpraxis_2_Warteraum;

    drop table if exists arzt cascade;

    drop table if exists arztpraxen cascade;

    drop table if exists behandlungen cascade;

    drop table if exists behandlungsraum cascade;

    drop table if exists behandlungsraum_behandlungen cascade;

    drop table if exists medikamente cascade;

    drop table if exists mitarbeiter cascade;

    drop table if exists patient cascade;

    drop table if exists rechnung cascade;

    drop table if exists warteraum cascade;

    drop sequence if exists arzt_seq;

    drop sequence if exists arztpraxen_seq;

    drop sequence if exists behandlungen_seq;

    drop sequence if exists behandlungsraum_seq;

    drop sequence if exists mitarbeiter_seq;

    drop sequence if exists patient_seq;

    drop sequence if exists rechnung_seq;

    drop sequence if exists warteraum_seq;
