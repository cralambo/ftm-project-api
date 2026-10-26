-- 2. Créer l'utilisateur administrateur
INSERT INTO users (
    id,
    email,
    password,
    firstname,
    lastname,
    user_type,
    enabled,
    account_non_expired,
    account_non_locked,
    credentials_non_expired,
    created_date
)
VALUES (
           gen_random_uuid(),
           'admin@tanymeva.org',
           '${admin-password-hash}',
           'System',
           'Administrator',
           'ADMIN',
           TRUE,
           TRUE,
           TRUE,
           TRUE,
           CURRENT_TIMESTAMP
       )
    ON CONFLICT (email) DO NOTHING;

-- 3. Associer l'utilisateur au rôle ADMIN
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         CROSS JOIN roles r
WHERE LOWER(u.email) = LOWER('admin@tanymeva.org')
  AND r.name = 'ADMIN'
    ON CONFLICT DO NOTHING;