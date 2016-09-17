insert into employees (id, employeeName, password, registeredDate) values (372515, 'Nikhil Pawar', 'sniper', current_timestamp);

insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (100, 'Hotel Lokesh Lane', 2000.00, 18.486817, 73.857200, 10);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (101, 'Panchami Hotel', 2000.00, 18.492424, 73.857673, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (102, 'Laxminarayan Theatre', 2000.00, 18.496061, 73.857559, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (103, 'Sarasbagh (Near Khandoba Temple, Pmt Stop )', 2000.00, 18.499505, 73.853445, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (104, 'Beneath Neelayam Bridge', 2000.00, 18.499610, 73.850671, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (105, 'Dandekar Pool', 2000.00, 18.501372, 73.845548, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (106, 'Senadatta Peth Police Chowki', 2000.00, 18.505228, 73.844784, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (107, 'Wakad Chowk (Near Pmt Stop)', 1600.00, 18.592313, 73.761923, 12);

insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (108, 'Kothrud Depot', 1800.00, 18.507907, 73.794899, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (109, 'Alka Theatre', 1800.00, 18.512313, 73.843357, 12);

insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (110, 'Ishana Appartment', 1800.00, 18.508008, 73.790180 , 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (111, 'Vanaz', 1800.00, 18.507576, 73.807836, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (112, 'Krishna Hospital', 1800.00, 18.509475, 73.812778, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (113, 'Anand Nagar (Paud Rd)', 1800.00, 18.509843, 73.814783, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (114, 'Jog Hospital (Paud Rd)', 1800.00, 18.509193, 73.821640, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (115, 'Paud road (Under Flyover)', 1800.00, 18.506202, 73.824537, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (116, 'Nal stop', 1800.00, 18.508641, 73.831363, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (117, 'Garware College', 1800.00, 18.512095, 73.838295, 12);

insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (118, 'Balaji Auto', 1800.00, 18.508251, 73.792449, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (119, 'Mhatre bridge', 1800.00, 18.504550, 73.835020, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (120, 'Dattawadi', 1800.00, 18.504954, 73.839911, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (121, 'Saras baug', 1800.00, 18.499564, 73.853041, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (122, 'City pride', 1800.00, 18.488630, 73.857382, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (123, 'Padmavati', 1800.00, 18.477121, 73.856915, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (124, 'Dhankawadi petrol pump', 1800.00, 18.463325, 73.858279, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (125, 'Bibewadi mahesh society', 1800.00, 18.468995, 73.863901, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (126, 'Bharat jyoti bus stop', 1800.00, 18.472837, 73.862992, 12);
insert into boardingpoints (id, boardingPointName, cost, lattitude, longitude, nbrRegisteredUsers) values (127, 'Ganga dham', 1800.00, 18.480913, 73.872226, 12);

insert into boardingpoints_employees (boardingpoints_id, registeredEmployees_id) values (104, 372515);

insert into busroutes (id, busNumber, busOwner, destinationCTSFacility, endTime, nbrRegisteredUsers, routeName, routeNumber, seatAvailable, session, startTime, totalSeats) values (1, 'MH 14 BA 9749', 'MP Travels', 'DLF Quadron', '09:00', 45, 'Panchami route', 'Route number - 6', TRUE, 'Morning', '8:05', 45);
insert into busroutes (id, busNumber, busOwner, destinationCTSFacility, endTime, nbrRegisteredUsers, routeName, routeNumber, seatAvailable, session, startTime, totalSeats) values (2, 'MH 14 BA 9748', 'MP Travels', 'DLF Quadron', '19:50', 45, 'Panchami route', 'Route number - 20', TRUE, 'Evening', '18:15', 45);
insert into busroutes (id, busNumber, busOwner, destinationCTSFacility, endTime, nbrRegisteredUsers, routeName, routeNumber, seatAvailable, session, startTime, totalSeats) values (3, 'MH 14 BA 9756', 'MP Travels', 'DLF Quadron', '21:00', 45, 'Vanaz - Nal Stop Route', 'Route number - 6', TRUE, 'Evening', '19:30', 45);
insert into busroutes (id, busNumber, busOwner, destinationCTSFacility, endTime, nbrRegisteredUsers, routeName, routeNumber, seatAvailable, session, startTime, totalSeats) values (4, 'MH 14 BA 9732', 'MP Travels', 'DLF Quadron', '22:30', 45, 'Dhankawadi Route', 'Route number - 7', TRUE, 'Evening', '21:00', 45);

insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (1, '7:55', 100, 1);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (2, '8:00', 101, 1);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (3, '8:02', 102, 1);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (4, '8:00', 103, 1);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (5, '8:02', 104, 1);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (6, '8:10', 105, 1);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (7, '8:15', 106, 1);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (8, '8:20', 109, 1);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (9, '8:35', 107, 1);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (10, '09:00', 128, 1);

insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (11, '19:00', 108, 2);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (12, '19:20', 109, 2);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (13, '19:25', 106, 2);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (14, '19:30', 105, 2);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (15, '19:35', 104, 2);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (16, '19:37', 103, 2);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (17, '19:45', 102, 2);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (18, '19:50', 101, 2);

insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (19, '20:10', 110, 3);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (20, '20:15', 111, 3);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (21, '20:20', 112, 3);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (22, '20:25', 113, 3);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (23, '20:27', 114, 3);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (24, '20:35', 115, 3);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (25, '20:40', 116, 3);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (26, '20:42', 117, 3);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (27, '20:50', 109, 3);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (28, '20:52', 106, 3);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (29, '20:55', 105, 3);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (30, '21:00', 104, 3);

insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (31, '21:30', 110, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (32, '21:32', 118, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (33, '21:40', 108, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (34, '21:43', 111, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (35, '21:45', 112, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (36, '21:47', 114, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (37, '21:50', 116, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (38, '21:55', 119, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (39, '21:58', 120, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (40, '22:05', 104, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (41, '22:07', 121, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (42, '22:10', 102, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (43, '22:15', 101, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (44, '22:17', 122, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (45, '22:25', 123, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (46, '22:30', 124, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (47, '22:25', 125, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (48, '22:40', 126, 4);
insert into BusRoute_BoardingPoint (id, haltTime, BoardingPoint_ID, Bus_ID) values (49, '22:45', 127, 4);