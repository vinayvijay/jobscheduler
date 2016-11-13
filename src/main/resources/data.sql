INSERT INTO skill (skillname) values ('MECHANIC');
INSERT INTO skill (skillname) values ('ELECTRICIAN');
INSERT INTO skill (skillname) values ('PLUMBER');

INSERT INTO user (username, password_hash, role, skill, shiftduration, address )
VALUES ('admin', '$2a$10$3PN9Zp87CpsP4za.zsIXP.ceMkHIRtglMQ5aQLLYpfsvaX6sAI0w6', 'CUSTOMERCARE', 'CALLCENTRE', 8, 'NA');
INSERT INTO user (username, password_hash, role, skill, shiftduration, address )
VALUES ('tech1', '$2a$10$cMUW7JTt8KYrA3r33b3UPOWhZtanTat5QxgvF3vdOXPRWnDvBmTN6', 'TECHNICIAN', 'MECHANIC', 6, 'Post office, 8th Cross, Malleshwaram, Bangalore');
INSERT INTO user (username, password_hash, role, skill, shiftduration, address )
VALUES ('tech2', '$2a$10$cMUW7JTt8KYrA3r33b3UPOWhZtanTat5QxgvF3vdOXPRWnDvBmTN6', 'TECHNICIAN', 'MECHANIC', 8, 'Marathahalli bridge, bangalore');

INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C1', 'Lalbagh, bangalore', 'MECHANIC', 'OPEN', 9874525978, 'CRITICAL', 1, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C2', 'iskcon temple, bangalore', 'MECHANIC', 'OPEN', 9958647899, 'MAJOR', 2, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C3', '12th Cross Rd, Ideal Homes Township, Rajarajeshwari Nagar, Bangalore', 'MECHANIC', 'OPEN', 4457899685, 'MAJOR', 2, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C4', 'Bangalore Agarwal Bhavan, 283, Puttalingiah Rd, Gururaja Layout, Padmanabhanagar, bangalore', 'MECHANIC', 'OPEN', 333265548, 'MAJOR', 1, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C5', 'Sri Sathya Sai hospital, Ambedkar Nagar, Whitefield Main Road, Bengaluru', 'MECHANIC', 'OPEN', 8879744564, 'MINOR', 1, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C6', 'M chinnaswamy stadium, bangalore', 'MECHANIC', 'OPEN', 2245466987, 'MAJOR', 1.5, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C7', 'cubbon park, bangalore', 'MECHANIC', 'OPEN', 9987544261, 'CRITICAL', 2, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C8', 'soul space spirit, bellandur village,varthur hobli, bangalore', 'MECHANIC', 'OPEN', 5584699874, 'CRITICAL', 1, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C9', 'bannerghatta biological park, bangalore', 'MECHANIC', 'OPEN', 6678944587, 'MAJOR', 1.5, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C10', 'UB City, 24, Vittal Mallya Road, bangalore', 'MECHANIC', 'OPEN', 2235644879, 'CRITICAL', 1.5, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C11', 'JP Park, Yeshwanthpur, bangalore', 'MECHANIC', 'OPEN', 1154688795, 'CRITICAL', 1, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C12', 'Sankey Road, bangalore', 'MECHANIC', 'OPEN', 5568977487, 'CRITICAL', 2, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C13', 'Ramada, 11, Park Road, Shivaji Nagar, bangalore', 'MECHANIC', 'OPEN', 6654988745, 'CRITICAL', 1, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C14', 'Chinmaya mission hosipital, bangalore', 'MECHANIC', 'OPEN', 3356487897, 'CRITICAL', 1, 1476815400000);
INSERT INTO workorder (customername, customeraddress, skill, status, customercontact, priority, workduration, cutoffdate )
VALUES ('C15', '88, Canara Bank Rd, 6th Block, Koramangala, bangalore', 'MECHANIC', 'OPEN', 1154266548, 'MINOR', 1, 1476815400000);
