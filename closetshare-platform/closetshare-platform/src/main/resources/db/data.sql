INSERT INTO hash_tags (tag_name, date_created, last_updated)
VALUES
    ('fashion', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('style', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('outfit', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('clothing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('accessories', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('trend', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('casual', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('elegant', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('luxury', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('minimalist', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO Users (
    seq_id, user_id, user_password, role, user_name, user_phone_number, address, sub_address, gender, date_created, last_updated
)
VALUES
    (1, 'user1', '$2a$10$4MbNSFBCO0ke7hr27cyAm.Yv1XlAfG6nKCIyI2bbeV4FCwfD4NEji', 'MEMBER', 'Alice', '01012345678', 'Seoul', 'Gangnam', 'WOMEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'user2', '$2a$10$4MbNSFBCO0ke7hr27cyAm.Yv1XlAfG6nKCIyI2bbeV4FCwfD4NEji', 'ADMIN', 'Bob', '01087654321', 'Busan', 'Haeundae', 'WOMEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'user3', '$2a$10$4MbNSFBCO0ke7hr27cyAm.Yv1XlAfG6nKCIyI2bbeV4FCwfD4NEji', 'MEMBER', 'Charlie', '01055556666', 'Incheon', 'Bupyeong', 'WOMEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 'user4', '$2a$10$4MbNSFBCO0ke7hr27cyAm.Yv1XlAfG6nKCIyI2bbeV4FCwfD4NEji', 'MEMBER', 'Daisy', '01044443333', 'Daegu', 'Dong-gu', 'MEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 'user5', '$2a$10$4MbNSFBCO0ke7hr27cyAm.Yv1XlAfG6nKCIyI2bbeV4FCwfD4NEji', 'MEMBER', 'Ethan', '01033332222', 'Gwangju', 'Seo-gu', 'MEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (6, 'user6', '$2a$10$4MbNSFBCO0ke7hr27cyAm.Yv1XlAfG6nKCIyI2bbeV4FCwfD4NEji', 'MEMBER', 'Fiona', '01022221111', 'Jeju', 'Jeju-si', 'MEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (7, 'user7', '$2a$10$4MbNSFBCO0ke7hr27cyAm.Yv1XlAfG6nKCIyI2bbeV4FCwfD4NEji', 'MEMBER', 'George', '01011110000', 'Ulsan', 'Nam-gu', 'NONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (8, 'user8', '$2a$10$4MbNSFBCO0ke7hr27cyAm.Yv1XlAfG6nKCIyI2bbeV4FCwfD4NEji', 'ADMIN', 'Hannah', '01000009999', 'Sejong', 'Hansol-dong', 'NONE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (9, 'test1', '$2a$10$4MbNSFBCO0ke7hr27cyAm.Yv1XlAfG6nKCIyI2bbeV4FCwfD4NEji', 'MEMBER', 'yeseul', '0101235678', 'Seoul', 'Gangnam', 'WOMEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (10, 'test2', '$2a$10$4MbNSFBCO0ke7hr27cyAm.Yv1XlAfG6nKCIyI2bbeV4FCwfD4NEji', 'MEMBER', 'song', '0108764321', 'Busan', 'Haeundae', 'MEN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
