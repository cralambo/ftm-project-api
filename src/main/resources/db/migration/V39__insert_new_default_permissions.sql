INSERT INTO permissions (id, name, description)
VALUES
-- Membres du conseil d'administration
(gen_random_uuid(), 'board_members.create', 'Créer un membre du conseil d’administration'),
(gen_random_uuid(), 'board_members.view', 'Consulter les membres du conseil d’administration'),
(gen_random_uuid(), 'board_members.update', 'Modifier les informations d’un membre du conseil d’administration'),
(gen_random_uuid(), 'board_members.delete', 'Supprimer un membre du conseil d’administration'),

-- Co-financeurs
(gen_random_uuid(), 'co_funders.create', 'Créer un co-financeur'),
(gen_random_uuid(), 'co_funders.view', 'Consulter les co-financeurs'),
(gen_random_uuid(), 'co_funders.update', 'Modifier les informations d’un co-financeur'),
(gen_random_uuid(), 'co_funders.delete', 'Supprimer un co-financeur'),

-- Départements
(gen_random_uuid(), 'departments.create', 'Créer un département'),
(gen_random_uuid(), 'departments.view', 'Consulter les départements'),
(gen_random_uuid(), 'departments.update', 'Modifier un département'),
(gen_random_uuid(), 'departments.delete', 'Supprimer un département'),

-- Activités de décaissement
(gen_random_uuid(), 'disbursements.activities.view', 'Consulter les activités de décaissement'),
(gen_random_uuid(), 'disbursements.activities.create', 'Créer une activité de décaissement'),
(gen_random_uuid(), 'disbursements.activities.update', 'Modifier une activité de décaissement'),
(gen_random_uuid(), 'disbursements.activities.delete', 'Supprimer une activité de décaissement'),

-- Échéanciers de décaissement
(gen_random_uuid(), 'disbursements.schedules.create', 'Créer un échéancier de décaissement'),
(gen_random_uuid(), 'disbursements.schedules.view', 'Consulter les échéanciers de décaissement'),

-- Districts
(gen_random_uuid(), 'districts.create', 'Créer un district'),
(gen_random_uuid(), 'districts.view', 'Consulter les districts'),
(gen_random_uuid(), 'districts.update', 'Modifier un district'),
(gen_random_uuid(), 'districts.delete', 'Supprimer un district'),

-- Employés
(gen_random_uuid(), 'employees.create', 'Créer un employé'),
(gen_random_uuid(), 'employees.view', 'Consulter les employés'),
(gen_random_uuid(), 'employees.update', 'Modifier les informations d’un employé'),
(gen_random_uuid(), 'employees.delete', 'Supprimer un employé'),

-- Paysages
(gen_random_uuid(), 'landscapes.create', 'Créer un paysage'),
(gen_random_uuid(), 'landscapes.view', 'Consulter les paysages'),
(gen_random_uuid(), 'landscapes.update', 'Modifier un paysage'),
(gen_random_uuid(), 'landscapes.delete', 'Supprimer un paysage'),

-- Communes
(gen_random_uuid(), 'municipalities.create', 'Créer une commune'),
(gen_random_uuid(), 'municipalities.view', 'Consulter les communes'),
(gen_random_uuid(), 'municipalities.update', 'Modifier une commune'),
(gen_random_uuid(), 'municipalities.delete', 'Supprimer une commune'),

-- Organisations
(gen_random_uuid(), 'organizations.create', 'Créer une organisation'),
(gen_random_uuid(), 'organizations.view', 'Consulter les organisations'),
(gen_random_uuid(), 'organizations.update', 'Modifier une organisation'),
(gen_random_uuid(), 'organizations.delete', 'Supprimer une organisation'),

-- Partenaires
(gen_random_uuid(), 'partners.create', 'Créer un partenaire'),
(gen_random_uuid(), 'partners.view', 'Consulter les partenaires'),
(gen_random_uuid(), 'partners.update', 'Modifier un partenaire'),
(gen_random_uuid(), 'partners.delete', 'Supprimer un partenaire'),

-- Contacts des partenaires
(gen_random_uuid(), 'partner_contacts.create', 'Créer un contact partenaire'),
(gen_random_uuid(), 'partner_contacts.view', 'Consulter les contacts des partenaires'),
(gen_random_uuid(), 'partner_contacts.update', 'Modifier un contact partenaire'),
(gen_random_uuid(), 'partner_contacts.delete', 'Supprimer un contact partenaire'),

-- Permissions
(gen_random_uuid(), 'permissions.view', 'Consulter les permissions'),
(gen_random_uuid(), 'permissions.create', 'Créer une permission'),
(gen_random_uuid(), 'permissions.update', 'Modifier une permission'),
(gen_random_uuid(), 'permissions.delete', 'Supprimer une permission'),

-- Postes
(gen_random_uuid(), 'positions.create', 'Créer un poste'),
(gen_random_uuid(), 'positions.view', 'Consulter les postes'),
(gen_random_uuid(), 'positions.update', 'Modifier un poste'),
(gen_random_uuid(), 'positions.delete', 'Supprimer un poste'),

-- Projets
(gen_random_uuid(), 'projects.create', 'Créer un projet'),
(gen_random_uuid(), 'projects.view', 'Consulter les projets'),

-- Régions
(gen_random_uuid(), 'regions.create', 'Créer une région'),
(gen_random_uuid(), 'regions.view', 'Consulter les régions'),
(gen_random_uuid(), 'regions.update', 'Modifier une région'),
(gen_random_uuid(), 'regions.delete', 'Supprimer une région'),

-- Rapports financiers
(gen_random_uuid(), 'financials.reports.view', 'Consulter les rapports financiers'),
(gen_random_uuid(), 'financials.reports.create', 'Créer un rapport financier'),
(gen_random_uuid(), 'financials.reports.update', 'Modifier un rapport financier'),
(gen_random_uuid(), 'financials.reports.delete', 'Supprimer un rapport financier'),

-- Rapports
(gen_random_uuid(), 'reports.create', 'Créer un rapport'),
(gen_random_uuid(), 'reports.view', 'Consulter les rapports'),
(gen_random_uuid(), 'reports.update', 'Modifier un rapport'),
(gen_random_uuid(), 'reports.delete', 'Supprimer un rapport'),
(gen_random_uuid(), 'reports.submit', 'Soumettre un rapport pour validation'),

-- Pièces jointes des rapports
(gen_random_uuid(), 'reports.attachments.general.upload', 'Téléverser les pièces jointes générales d’un rapport'),
(gen_random_uuid(), 'reports.attachments.technical.upload', 'Téléverser les pièces justificatives techniques d’un rapport'),
(gen_random_uuid(), 'reports.attachments.financial.upload', 'Téléverser les pièces justificatives financières d’un rapport'),
(gen_random_uuid(), 'reports.attachments.general.view', 'Consulter les pièces jointes générales d’un rapport'),
(gen_random_uuid(), 'reports.attachments.financial.view', 'Consulter les pièces justificatives financières d’un rapport'),
(gen_random_uuid(), 'reports.attachments.download', 'Télécharger les pièces jointes d’un rapport'),
(gen_random_uuid(), 'reports.attachments.technical.view', 'Consulter les pièces justificatives techniques d’un rapport'),

-- Rapports techniques
(gen_random_uuid(), 'technicals.reports.view', 'Consulter les rapports techniques'),
(gen_random_uuid(), 'technicals.reports.delete', 'Supprimer un rapport technique'),

-- Rôles
(gen_random_uuid(), 'roles.view', 'Consulter les rôles'),
(gen_random_uuid(), 'roles.create', 'Créer un rôle'),
(gen_random_uuid(), 'roles.update', 'Modifier un rôle'),
(gen_random_uuid(), 'roles.delete', 'Supprimer un rôle'),
(gen_random_uuid(), 'roles.permissions.add', 'Attribuer une permission à un rôle'),
(gen_random_uuid(), 'roles.permissions.remove', 'Retirer une permission d’un rôle'),
(gen_random_uuid(), 'roles.permissions.view', 'Consulter les permissions associées à un rôle'),

-- Plans stratégiques
(gen_random_uuid(), 'strategic_plans.create', 'Créer un plan stratégique'),
(gen_random_uuid(), 'strategic_plans.view', 'Consulter les plans stratégiques'),
(gen_random_uuid(), 'strategic_plans.update', 'Modifier un plan stratégique'),
(gen_random_uuid(), 'strategic_plans.delete', 'Supprimer un plan stratégique'),

-- Sous-paysages
(gen_random_uuid(), 'sub_landscapes.create', 'Créer un sous-paysage'),
(gen_random_uuid(), 'sub_landscapes.view', 'Consulter les sous-paysages'),
(gen_random_uuid(), 'sub_landscapes.update', 'Modifier un sous-paysage'),
(gen_random_uuid(), 'sub_landscapes.delete', 'Supprimer un sous-paysage'),

-- Thématiques
(gen_random_uuid(), 'topics.create', 'Créer une thématique'),
(gen_random_uuid(), 'topics.view', 'Consulter les thématiques'),
(gen_random_uuid(), 'topics.update', 'Modifier une thématique'),
(gen_random_uuid(), 'topics.delete', 'Supprimer une thématique'),

-- Utilisateurs
(gen_random_uuid(), 'users.view', 'Consulter les utilisateurs'),
(gen_random_uuid(), 'users.update', 'Modifier les informations d’un utilisateur'),
(gen_random_uuid(), 'users.delete', 'Supprimer un utilisateur'),
(gen_random_uuid(), 'users.active', 'Activer ou désactiver un utilisateur'),
(gen_random_uuid(), 'users.roles.add', 'Attribuer un rôle à un utilisateur'),
(gen_random_uuid(), 'users.roles.remove', 'Retirer un rôle à un utilisateur'),
(gen_random_uuid(), 'users.roles.view', 'Consulter les rôles d’un utilisateur'),
(gen_random_uuid(), 'users.create', 'Créer un utilisateur')

    ON CONFLICT (name) DO NOTHING;