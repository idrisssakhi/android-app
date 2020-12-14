create database android;
use android;
create table users(
    id INT NOT NULL AUTO_INCREMENT,
    userName VARCHAR(255), 
    mailAdress VARCHAR(255) UNIQUE, 
    password VARCHAR(255), 
    age INT(3), 
    Gender VARCHAR(255), 
    fonction VARCHAR(255),
    PRIMARY KEY (id)
);

create table stores(
    id INT NOT NULL AUTO_INCREMENT,
    mailAdress VARCHAR(255), 
    storeName VARCHAR(100),
    storePhone VARCHAR(24),
    storeAdress VARCHAR(200),
    storeCodePostal INT(6),
    storeLocality VARCHAR(100),
    storeLongitude FLOAT,
    storeLatitude FLOAT,
    storeDescription TEXT,
    storeCategory VARCHAR(100),
    storeOpening TIME,
    storeClosing TIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_mailAd
        FOREIGN KEY (mailAdress)
        REFERENCES users(mailAdress)
);

create table messages(
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255), 
    phone VARCHAR(100),
    sender VARCHAR(24),
    message TEXT,
    replyEmail VARCHAR(255),
    storeId INT,
    receiver VARCHAR(255),
    PRIMARY KEY (id),
    CONSTRAINT fk_mailAd_rec
        FOREIGN KEY (receiver)
        REFERENCES users(mailAdress),
    CONSTRAINT fk_mailAd_send
        FOREIGN KEY (receiver)
        REFERENCES users(mailAdress),
    CONSTRAINT fk_store
        FOREIGN KEY (storeId)
        REFERENCES stores(id)
);
ENGINE=InnoDB;


