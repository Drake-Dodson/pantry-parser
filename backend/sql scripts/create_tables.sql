#CREATE DATABASE pantry_parser;
use pantry_parser;

CREATE TABLE users (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	display_name varchar(255) NOT NULL,
	email varchar(255) NOT NULL UNIQUE,
	password varchar(255) NOT NULL,
	role varchar(255) NOT NULL
);

CREATE TABLE recipe_categories (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL
);

CREATE TABLE ingredient_categories (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL
);

CREATE TABLE recipes (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL,
    time INT NOT NULL,
    summary varchar(255) NOT NULL,
    description text NOT NULL,
    created_date timestamp NOT NULL,
    rating FLOAT,
    category_id BIGINT,
    creator_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES recipe_categories(id),
    FOREIGN KEY (creator_id) REFERENCES users(id)
);
    
CREATE TABLE ingredients (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name varchar(255) NOT NULL UNIQUE,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES ingredient_categories(id)
);
    
CREATE TABLE ingredients_of_recipes (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    amount FLOAT,
    unit varchar(255),
    ingredient_id BIGINT NOT NULL,
    recipe_id BIGINT NOT NULL,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id),
    FOREIGN KEY (recipe_id) REFERENCES ingredients(id)
);

CREATE TABLE steps_of_recipes (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    step text NOT NULL,
    step_num int NOT NULL,
    recipe_id BIGINT NOT NULL,
    FOREIGN KEY (recipe_id) REFERENCES recipes(id)
);
    
CREATE TABLE reviews (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    stars INT NOT NULL,
    comment text,
    user_id bigint not null,
    recipe_id bigint not null,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id)
);

CREATE TABLE favorites (
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id bigint not null,
    recipe_id bigint not null,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (recipe_id) REFERENCES recipes(id)
);
    
