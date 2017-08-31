CREATE TABLE book (
   isbn		identity,
   title	VARCHAR(255) NOT NULL,
   author	VARCHAR(255) NOT NULL,
   genre	VARCHAR(255) NOT NULL,
   remCopies	INT	NOT NULL,
   PRIMARY KEY(isbn)
);

CREATE TABLE Customer (
   id			identity,
   firstName	VARCHAR(255) NOT NULL,
   lastName		VARCHAR(255) NOT NULL,
   phoneNumber	BIGINT NULL,   
   password		VARCHAR NOT NULL,
   userName		VARCHAR NOT NULL,
   custType		VARCHAR NOT NULL,
   PRIMARY KEY(ID)
);

CREATE TABLE Loan (
   loanId		identity,
   id			BIGINT NOT NULL,
   isbn			BIGINT NOT NULL,
   dateTaken	DATE NOT NULL,
   dateDue		DATE NOT NULL,
   PRIMARY KEY(loanId)
);