INSERT INTO permissions (
    id,
    name,
    description
)
VALUES
    (
        gen_random_uuid(),
        'users.view',
        'Consulter les utilisateurs'
    ),
    (
        gen_random_uuid(),
        'users.create',
        'Créer un utilisateur'
    ),
    (
        gen_random_uuid(),
        'users.update',
        'Modifier un utilisateur'
    ),
    (
        gen_random_uuid(),
        'users.delete',
        'Supprimer un utilisateur'
    ),
    (
        gen_random_uuid(),
        'employees.view',
        'Consulter les employés'
    ),
    (
        gen_random_uuid(),
        'employees.create',
        'Créer un employé'
    ),
    (
        gen_random_uuid(),
        'employees.update',
        'Modifier un employé'
    ),
    (
        gen_random_uuid(),
        'employees.delete',
        'Supprimer un employé'
    ),
    (
        gen_random_uuid(),
        'board_members.view',
        'Consulter les membres du conseil'
    ),
    (
        gen_random_uuid(),
        'board_members.create',
        'Créer un membre du conseil'
    ),
    (
        gen_random_uuid(),
        'board_members.update',
        'Modifier un membre du conseil'
    ),
    (
        gen_random_uuid(),
        'board_members.delete',
        'Supprimer un membre du conseil'
    ),
    (
        gen_random_uuid(),
        'projects.view',
        'Consulter les projets'
    ),
    (
        gen_random_uuid(),
        'projects.create',
        'Créer un projet'
    ),
    (
        gen_random_uuid(),
        'projects.update',
        'Modifier un projet'
    ),
    (
        gen_random_uuid(),
        'projects.delete',
        'Supprimer un projet'
    );