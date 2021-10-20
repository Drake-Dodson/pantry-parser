use pantry_parser;
SET sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));

SELECT DISTINCT r.* FROM recipes r 
JOIN recipe_ingredient ri ON r.id = ri.recipe_id
JOIN (SELECT * FROM ingredients WHERE name IN ('concrete', 'gluten', 'soft')) AS i ON i.id = ri.ingredient_id
GROUP BY r.id
ORDER BY COUNT(*) DESC;