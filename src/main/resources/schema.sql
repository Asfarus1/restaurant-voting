DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS lunches;
DROP TABLE IF EXISTS refresh_tokens;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS menu_items;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS dishes;

DROP SEQUENCE IF EXISTS hibernate_sequence;
CREATE SEQUENCE hibernate_sequence;

CREATE TABLE dishes
(
    id    BIGINT       NOT NULL DEFAULT NEXTVAL('hibernate_sequence'),
    title VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT       NOT NULL DEFAULT NEXTVAL('hibernate_sequence'),
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled  boolean      NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    UNIQUE (username)
);

CREATE TABLE user_roles
(
    user_id BIGINT       NOT NULL,
    role    VARCHAR(255) NOT NULL,
    UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
    id    BIGINT       NOT NULL DEFAULT NEXTVAL('hibernate_sequence'),
    title VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE menu
(
    id            BIGINT NOT NULL DEFAULT NEXTVAL('hibernate_sequence'),
    date          DATE   NOT NULL,
    restaurant_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (date, restaurant_id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);

CREATE TABLE menu_items
(
    id      BIGINT         NOT NULL DEFAULT NEXTVAL('hibernate_sequence'),
    menu_id BIGINT         NOT NULL,
    dish_id BIGINT         NOT NULL,
    price   decimal(19, 2) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (menu_id, dish_id),
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes (id) ON DELETE CASCADE
);

CREATE TABLE lunches
(
    id            BIGINT NOT NULL DEFAULT NEXTVAL('hibernate_sequence'),
    date          date   NOT NULL,
    user_id       BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (date, user_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);

CREATE TABLE refresh_tokens
(
    user_id BIGINT      NOT NULL,
    token   VARCHAR(36) NOT NULL,
    expired timestamp   NOT NULL,
    UNIQUE (user_id, token),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);