INSERT INTO roles (id, name, description)
VALUES
    (
        gen_random_uuid(),
        'ROLE_USER',
        'Utilisateur standard'
    ),
    (
        gen_random_uuid(),
        'ROLE_ADMIN',
        'Administrateur'
    ),
    (
        gen_random_uuid(),
        'ROLE_EMPLOYEE',
        'Utilisateur associé à un employé'
    ),
    (
        gen_random_uuid(),
        'ROLE_BOARD_MEMBER',
        'Utilisateur associé à un membre du conseil'
    ),
    (
        gen_random_uuid(),
        'ROLE_PARTNER_CONTACT',
        'Utilisateur associé à un partenaire'
    );