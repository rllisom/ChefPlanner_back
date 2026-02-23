-- =============================================
-- USERS
-- =============================================
INSERT INTO users (id, email, username, password)
VALUES
    ('a1b2c3d4-0000-0000-0000-000000000001', 'admin@chefplanner.com', 'admin', '$2a$10$7QJ8z1Z1Z1Z1Z1Z1Z1Z1ZeK1K1K1K1K1K1K1K1K1K1K1K1K1K1K1K'),
    ('a1b2c3d4-0000-0000-0000-000000000002', 'chef1@chefplanner.com', 'chef_maria', '$2a$10$7QJ8z1Z1Z1Z1Z1Z1Z1Z1ZeK1K1K1K1K1K1K1K1K1K1K1K1K1K1K1K'),
    ('a1b2c3d4-0000-0000-0000-000000000003', 'chef2@chefplanner.com', 'chef_pedro', '$2a$10$7QJ8z1Z1Z1Z1Z1Z1Z1Z1ZeK1K1K1K1K1K1K1K1K1K1K1K1K1K1K1K');

-- =============================================
-- USER ROLES (tabla de colección)
-- =============================================
INSERT INTO user_roles (user_id, role)
VALUES
    ('a1b2c3d4-0000-0000-0000-000000000001', 'ADMIN'),
    ('a1b2c3d4-0000-0000-0000-000000000002', 'USER'),
    ('a1b2c3d4-0000-0000-0000-000000000003', 'USER');

-- =============================================
-- USER PROFILES
-- =============================================
INSERT INTO user_profiles (id, user_uuid)
VALUES
    (1, 'a1b2c3d4-0000-0000-0000-000000000001'),
    (2, 'a1b2c3d4-0000-0000-0000-000000000002'),
    (3, 'a1b2c3d4-0000-0000-0000-000000000003');

-- =============================================
-- INGREDIENTS
-- =============================================
INSERT INTO ingredient (id, name)
VALUES
    (1, 'Tomate'),
    (2, 'Cebolla'),
    (3, 'Ajo'),
    (4, 'Aceite de oliva'),
    (5, 'Sal'),
    (6, 'Harina'),
    (7, 'Huevo'),
    (8, 'Leche'),
    (9, 'Pollo'),
    (10, 'Arroz');

-- =============================================
-- RECIPES
-- =============================================
INSERT INTO recipes (id, title, description, minutes, difficulty, author_id, featured)
VALUES
    (1, 'Tortilla Española', 'Tortilla clásica de patata y cebolla', '00:30:00', 'EASY', 2, true),
    (2, 'Pollo al ajillo', 'Pollo con ajo y vino blanco', '00:45:00', 'MEDIUM', 2, false),
    (3, 'Arroz con tomate', 'Arroz caldoso con sofrito de tomate', '00:25:00', 'EASY', 3, true),
    (4, 'Croquetas caseras', 'Croquetas de pollo con bechamel', '01:30:00', 'HARD', 3, false);

-- =============================================
-- RECIPE INGREDIENTS (tabla intermedia con clave compuesta)
-- =============================================
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit)
VALUES
    (1, 7, 4, 'unidades'),   -- Tortilla: 4 huevos
    (1, 5, 1, 'cucharadita'), -- Tortilla: sal
    (2, 9, 500, 'gramos'),   -- Pollo al ajillo: 500g pollo
    (2, 3, 6, 'dientes'),    -- Pollo al ajillo: 6 dientes de ajo
    (2, 4, 3, 'cucharadas'), -- Pollo al ajillo: aceite
    (3, 10, 200, 'gramos'),  -- Arroz con tomate: 200g arroz
    (3, 1, 3, 'unidades'),   -- Arroz con tomate: 3 tomates
    (3, 2, 1, 'unidad'),     -- Arroz con tomate: 1 cebolla
    (4, 6, 200, 'gramos'),   -- Croquetas: harina
    (4, 8, 500, 'ml'),       -- Croquetas: leche
    (4, 9, 300, 'gramos');   -- Croquetas: pollo

-- =============================================
-- USER PANTRY (despensa del usuario, ManyToMany)
-- =============================================
INSERT INTO user_pantry_ingredients (user_profile_id, ingredient_id)
VALUES
    (2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
    (3, 5), (3, 6), (3, 7), (3, 8);

-- =============================================
-- MENU ITEMS
-- =============================================
INSERT INTO menu_item (id, user_id, date, meal_type, recipe_id)
VALUES
    (1, 2, '2026-02-24', 'BREAKFAST', 1),
    (2, 2, '2026-02-24', 'LUNCH', 2),
    (3, 2, '2026-02-25', 'DINNER', 3),
    (4, 3, '2026-02-24', 'LUNCH', 4),
    (5, 3, '2026-02-25', 'BREAKFAST', 1);
