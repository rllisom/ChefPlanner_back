-- =============================================
-- LIMPIAR TABLAS (orden inverso por foreign keys)
-- =============================================
TRUNCATE TABLE menu_item,
               user_pantry_ingredients,
               recipe_ingredient,
               recipes,
               user_profiles,
               user_roles,
               ingredient,
               users
RESTART IDENTITY CASCADE;

-- =============================================
-- AJUSTE DE RESTRICCIONES
-- Permitir que recipe_id sea NULL en menu_item
-- (necesario para poder borrar recetas sin borrar el historial del menú)
-- =============================================
ALTER TABLE menu_item ALTER COLUMN recipe_id DROP NOT NULL;


-- =============================================
-- USERS
-- =============================================
INSERT INTO users (id, email, username, password)
VALUES
    ('a1b2c3d4-0000-0000-0000-000000000001', 'admin@chefplanner.com', 'admin','{noop}prueba123'),
    ('a1b2c3d4-0000-0000-0000-000000000002', 'chef1@chefplanner.com', 'chef_maria','{noop}contrasena123'),
    ('a1b2c3d4-0000-0000-0000-000000000003', 'chef2@chefplanner.com', 'chef_pedro','{noop}contrasena123')
ON CONFLICT DO NOTHING;

-- =============================================
-- USER ROLES (tabla de colección)
-- =============================================
INSERT INTO user_roles (user_id, role)
VALUES
    ('a1b2c3d4-0000-0000-0000-000000000001', 'ADMIN'),
    ('a1b2c3d4-0000-0000-0000-000000000002', 'USER'),
    ('a1b2c3d4-0000-0000-0000-000000000003', 'USER')
ON CONFLICT DO NOTHING;

-- =============================================
-- USER PROFILES
-- =============================================
INSERT INTO user_profiles (id, user_uuid)
VALUES
    (1, 'a1b2c3d4-0000-0000-0000-000000000001'),
    (2, 'a1b2c3d4-0000-0000-0000-000000000002'),
    (3, 'a1b2c3d4-0000-0000-0000-000000000003')
ON CONFLICT DO NOTHING;

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
    (10, 'Arroz')
ON CONFLICT DO NOTHING;

-- =============================================
-- RECIPES
-- =============================================
INSERT INTO recipes (id, title, description, minutes, difficulty, author_id, featured)
VALUES
    (1, 'Tortilla Española', 'Tortilla clásica de patata y cebolla', '00:30:00', 'EASY', 2, true),
    (2, 'Pollo al ajillo', 'Pollo con ajo y vino blanco', '00:45:00', 'MEDIUM', 2, false),
    (3, 'Arroz con tomate', 'Arroz caldoso con sofrito de tomate', '00:25:00', 'EASY', 3, true),
    (4, 'Croquetas caseras', 'Croquetas de pollo con bechamel', '01:30:00', 'HARD', 3, false)
ON CONFLICT DO NOTHING;

-- =============================================
-- RECIPE INGREDIENTS (tabla intermedia con clave compuesta)
-- =============================================
INSERT INTO recipe_ingredient (recipe_id, ingredient_id, quantity, unit)
VALUES
    (1, 7, 4, 'unidades'),
    (1, 5, 1, 'cucharadita'),
    (2, 9, 500, 'gramos'),
    (2, 3, 6, 'dientes'),
    (2, 4, 3, 'cucharadas'),
    (3, 10, 200, 'gramos'),
    (3, 1, 3, 'unidades'),
    (3, 2, 1, 'unidad'),
    (4, 6, 200, 'gramos'),
    (4, 8, 500, 'ml'),
    (4, 9, 300, 'gramos')
ON CONFLICT DO NOTHING;

-- =============================================
-- USER PANTRY (despensa del usuario, ManyToMany)
-- =============================================
INSERT INTO user_pantry_ingredients (user_profile_id, ingredient_id)
VALUES
    (2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
    (3, 5), (3, 6), (3, 7), (3, 8)
ON CONFLICT DO NOTHING;

-- =============================================
-- MENU ITEMS
-- =============================================
INSERT INTO menu_item (id, user_id, date, meal_type, recipe_id)
VALUES
    (1, 2, '2026-02-24', 'BREAKFAST', 1),
    (2, 2, '2026-02-24', 'LUNCH', 2),
    (3, 2, '2026-02-25', 'DINNER', 3),
    (4, 3, '2026-02-24', 'LUNCH', 4),
    (5, 3, '2026-02-25', 'BREAKFAST', 1)
ON CONFLICT DO NOTHING;


-- =============================================
-- SINCRONIZACIÓN DE SECUENCIAS
-- Necesario porque los INSERTs usan IDs explícitos
-- y las secuencias de PostgreSQL quedan desincronizadas
-- =============================================
SELECT setval(pg_get_serial_sequence('user_profiles', 'id'),
              (SELECT MAX(id) FROM user_profiles), true);

SELECT setval(pg_get_serial_sequence('ingredient', 'id'),
              (SELECT MAX(id) FROM ingredient), true);

SELECT setval(pg_get_serial_sequence('recipes', 'id'),
              (SELECT MAX(id) FROM recipes), true);

SELECT setval(pg_get_serial_sequence('menu_item', 'id'),
              (SELECT MAX(id) FROM menu_item), true);