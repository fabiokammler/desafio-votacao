CREATE TABLE agenda(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    duration INT,
    created_at DATETIME(6) NOT NULL,
    deadline DATETIME(6)
);

CREATE TABLE votes(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    agenda_id INT NOT NULL,
    associate_id INT NOT NULL,
    vote BOOLEAN NOT NULL,
    created_at DATETIME(6) NOT NULL,
    FOREIGN KEY(agenda_id) references agenda(id),
    UNIQUE(agenda_id, associate_id)
);