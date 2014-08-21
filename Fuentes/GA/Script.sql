CREATE table "GA_DCM" (
    "DCM_ID"          NUMBER(10,0) NOT NULL,
    "DCM_PUBLICACION" DATE NOT NULL,
    "DCM_TITULO"      VARCHAR2(4000) NOT NULL,
    "DCM_URL"         VARCHAR2(4000),
    constraint  "GA_DCM_ID_PK" primary key ("DCM_ID")
)
/

CREATE sequence "GA_DCM_ID_SEQ" 
/

CREATE trigger "BI_GA_DCM"  
  before insert on "GA_DCM"              
  for each row 
begin  
    select "GA_DCM_ID_SEQ".nextval into :NEW.DCM_ID from dual;
end;
/   

alter table "GA_DCM" add
constraint GA_DCM_UK
unique ("DCM_ID","DCM_TITULO")
/   





*****
CREATE table "GA_TIPO_USR" (
    "TIPO_USR_ID"          NUMBER(10,0) NOT NULL,
    "TIPO_USR_TIPO"        VARCHAR2(4000) NOT NULL,
    "TIPO_USR_DESCRIPCION" VARCHAR2(4000) NOT NULL,
    constraint  "GA_TIPO_USR_ID_PK" primary key ("TIPO_USR_ID")
)
/

CREATE sequence "GA_TIPO_USR_ID_SEQ" 
/

CREATE trigger "BI_GA_TIPO_USR"  
  before insert on "GA_TIPO_USR"              
  for each row 
begin  
    select "GA_TIPO_USR_ID_SEQ".nextval into :NEW.TIPO_USR_ID from dual;
end;
/   

alter table "GA_TIPO_USR" add
constraint GA_TIPO_USR_UK
unique ("TIPO_USR_ID","TIPO_USR_TIPO")
/   



********



CREATE table "GA_USR" (
    "USR_ID"         NUMBER(10,0),
    "USR_CONTRASENA" VARCHAR2(4000) NOT NULL,
    "USR_NOMBRE"     VARCHAR2(4000) NOT NULL,
    "USR_MAIL"       VARCHAR2(4000) NOT NULL,
    "USR_TIPO_USR"   NUMBER(10,0) NOT NULL,
    constraint  "GA_USR_ID_PK" primary key ("USR_ID")
)
/

CREATE sequence "GA_USR_ID_SEQ" 
/

CREATE trigger "BI_GA_USR"  
  before insert on "GA_USR"              
  for each row 
begin  
    select "GA_USR_ID_SEQ".nextval into :NEW.USR_ID from dual;
end;
/   

ALTER TABLE "GA_USR" ADD CONSTRAINT "GA_USR_FK_TIPO" 
FOREIGN KEY ("USR_TIPO_USR")
REFERENCES "GA_TIPO_USR" ("TIPO_USR_ID")

/
alter table "GA_USR" add
constraint GA_USR_UK
unique ("USR_ID","USR_MAIL")
/   



*****************


CREATE table "GA_ARCHIVO" (
    "ARCHIVO_ID"        NUMBER(10,0),
    "ARCHIVO_DIRECCION" VARCHAR2(4000) NOT NULL,
    "ARCHIVO_NOMBRE"    VARCHAR2(4000) NOT NULL,
    constraint  "GA_ARCHIVO_ID_PK" primary key ("ARCHIVO_ID")
)
/

CREATE sequence "GA_ARCHIVO_ID_SEQ" 
/

CREATE trigger "BI_GA_ARCHIVO"  
  before insert on "GA_ARCHIVO"              
  for each row 
begin  
    select "GA_ARCHIVO_ID_SEQ".nextval into :NEW.ARCHIVO_ID from dual;
end;
/   

alter table "GA_ARCHIVO" add
constraint GA_ARCHIVO_UK
unique ("ARCHIVO_ID","ARCHIVO_DIRECCION")
/   






******



CREATE table "GA_INSTITUTO" (
    "INSTITUTO_ID"          NUMBER(10,0) NOT NULL,
    "INSTITUTO_DESCRIPCION" VARCHAR2(4000),
    "INSTITUTO_DIRECCION"   VARCHAR2(4000),
    "INSTITUTO_NOMBRE"      VARCHAR2(4000) NOT NULL,
    constraint  "GA_INSTITUTO_ID_PK" primary key ("INSTITUTO_ID")
)
/

CREATE sequence "GA_INSTITUTO_ID_SEQ" 
/

CREATE trigger "BI_GA_INSTITUTO"  
  before insert on "GA_INSTITUTO"              
  for each row 
begin  
    select "GA_INSTITUTO_ID_SEQ".nextval into :NEW.INSTITUTO_ID from dual;
end;
/   

alter table "GA_INSTITUTO" add
constraint GA_INSTITUTO_ID_UK
unique ("INSTITUTO_ID")
/   



*****













CREATE table "GA_DCM_ARCHIVO" (
    "DCM_ARCHIVO_ID"          NUMBER(10,0) NOT NULL,
    "DCM_ARCHIVO_ID_ARCHIVO"  NUMBER(10,0) NOT NULL,
    "DCM_ARCHIVO_ID_DCM"      NUMBER(10,0) NOT NULL,
    "DCM_ARCHIVO_DESCRIPCION" VARCHAR2(4000),
    constraint  "GA_DCM_ARCHIVO_ID_PK" primary key ("DCM_ARCHIVO_ID")
)
/

CREATE sequence "GA_DCM_ARCHIVO_ID_SEQ" 
/

CREATE trigger "BI_GA_DCM_ARCHIVO"  
  before insert on "GA_DCM_ARCHIVO"              
  for each row 
begin  
    select "GA_DCM_ARCHIVO_ID_SEQ".nextval into :NEW.DCM_ARCHIVO_ID from dual;
end;
/   

ALTER TABLE "GA_DCM_ARCHIVO" ADD CONSTRAINT "GA_DCM_ARCHIVO_FK_ID_DCM" 
FOREIGN KEY ("DCM_ARCHIVO_ID_DCM")
REFERENCES "GA_DCM" ("DCM_ID")
ON DELETE CASCADE

/
ALTER TABLE "GA_DCM_ARCHIVO" ADD CONSTRAINT "GA_DCM_ARCHIVO_FK_ID_ARCHIVO" 
FOREIGN KEY ("DCM_ARCHIVO_ID_ARCHIVO")
REFERENCES "GA_ARCHIVO" ("ARCHIVO_ID")
ON DELETE CASCADE

/
alter table "GA_DCM_ARCHIVO" add
constraint GA_DCM_ARCHIVO_ID_UK
unique ("DCM_ARCHIVO_ID")
/   



CREATE table "DOCUMENTOXPERSONA" (
    "ID"          NUMBER(10,0),
    "DESCRIPCION" VARCHAR2(4000),
    "IDPERSONA"   NUMBER(10,0) NOT NULL,
    "IDDOCUMENTO" NUMBER(10,0) NOT NULL,
    constraint  "DOCUMENTOXPERSONA_PK" primary key ("ID")
)
/

CREATE sequence "DOCUMENTOXPERSONA_SEQ" 
/

CREATE trigger "BI_DOCUMENTOXPERSONA"  
  before insert on "DOCUMENTOXPERSONA"              
  for each row 
begin  
    select "DOCUMENTOXPERSONA_SEQ".nextval into :NEW.ID from dual;
end;
/   

ALTER TABLE "DOCUMENTOXPERSONA" ADD CONSTRAINT "DOCUMENTOXPERSONA_FK" 
FOREIGN KEY ("IDPERSONA")
REFERENCES "PERSONAJE" ("ID")
ON DELETE CASCADE

/
ALTER TABLE "DOCUMENTOXPERSONA" ADD CONSTRAINT "DOCUMENTOXPERSONA_FK2" 
FOREIGN KEY ("IDDOCUMENTO")
REFERENCES "DOCUMENTO" ("ID")
ON DELETE CASCADE

/
