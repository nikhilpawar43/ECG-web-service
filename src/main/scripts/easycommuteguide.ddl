
    create table BusRoute_BoardingPoint (
        id int4 not null,
        haltTime varchar(255),
        BoardingPoint_ID int4,
        Bus_ID int4,
        primary key (id)
    );

    create table boardingpoints (
        id int4 not null,
        boardingPointName varchar(255),
        cost float8,
        lattitude float8,
        longitude float8,
        nbrRegisteredUsers int4 not null,
        primary key (id)
    );

    create table boardingpoints_employees (
        boardingpoints_id int4 not null,
        registeredEmployees_id int4 not null
    );

    create table busroutes (
        id int4 not null,
        busNumber varchar(255),
        busOwner varchar(255),
        destinationCTSFacility varchar(255),
        endTime varchar(255),
        nbrRegisteredUsers int4 not null,
        routeName varchar(255),
        routeNumber varchar(255),
        seatAvailable boolean not null,
        session varchar(255),
        startTime varchar(255),
        totalSeats int4 not null,
        primary key (id)
    );

    create table employees (
        id int4 not null,
        employeeName varchar(255),
        password varchar(255),
        registeredDate timestamp,
        primary key (id)
    );

    create table users (
        id int4 not null,
        enabled boolean not null,
        password varchar(255),
        username varchar(255),
        primary key (id)
    );

    alter table boardingpoints_employees 
        add constraint UK_cvrwv0lo7vsp1532n7tdnoa9d unique (registeredEmployees_id);

    alter table BusRoute_BoardingPoint 
        add constraint FK_3qro00ojh3lg7acyddxeoj8qx 
        foreign key (BoardingPoint_ID) 
        references boardingpoints;

    alter table BusRoute_BoardingPoint 
        add constraint FK_dwk8rm0d7joies67g198bg00y 
        foreign key (Bus_ID) 
        references busroutes;

    alter table boardingpoints_employees 
        add constraint FK_cvrwv0lo7vsp1532n7tdnoa9d 
        foreign key (registeredEmployees_id) 
        references employees;

    alter table boardingpoints_employees 
        add constraint FK_ep9p04tde0tqi9wpwfs8aywib 
        foreign key (boardingpoints_id) 
        references boardingpoints;

    create sequence hibernate_sequence;
