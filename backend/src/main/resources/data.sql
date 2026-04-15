INSERT INTO users (username, password) VALUES
('admin', '123456'),
('joao', '123456'),
('maria', '123456'),
('ana', '123456'),
('carlos', '123456');

INSERT INTO classroom (name) VALUES
('Classe A'),
('Classe B'),
('Classe C'),
('Classe D'),
('Classe E');

INSERT INTO student (name, classroom_id) VALUES
('Pedro', 1),
('Lucas', 1),
('Marcos', 1),
('Julia', 1),
('Fernanda', 1),
('Pedro', 2),
('Lucas', 2),
('Marcos', 2),
('Julia', 2),
('Fernanda', 2),
('Pedro', 3),
('Lucas', 3),
('Marcos', 3),
('Julia', 3),
('Fernanda', 3),
('Pedro', 4),
('Lucas', 4),
('Marcos', 4),
('Julia', 4),
('Fernanda', 4),
('Pedro', 5),
('Lucas', 5),
('Marcos', 5),
('Julia', 5),
('Fernanda', 5);

INSERT INTO discipline (name) VALUES
('Matematica'),
('Portugues'),
('Historia'),
('Geografia'),
('Fisica');

INSERT INTO evaluation (name, weight, discipline_id, classroom_id) VALUES
-- Classe A (classroom_id=1) - múltiplas avaliações por disciplina
('Test 1 Matematica', 2.0, 1, 1),
('Prova 1 Matematica', 3.0, 1, 1),
('Trabalho Matematica', 1.5, 1, 1),
('Test 1 Portugues', 2.0, 2, 1),
('Prova 1 Portugues', 3.0, 2, 1),
('Trabalho Portugues', 1.5, 2, 1),
('Test 1 Historia', 2.0, 3, 1),
('Prova 1 Historia', 3.0, 3, 1),
('Trabalho Historia', 1.5, 3, 1),
('Test 1 Geografia', 2.0, 4, 1),
('Prova 1 Geografia', 3.0, 4, 1),
('Trabalho Geografia', 1.5, 4, 1),
('Test 1 Fisica', 2.0, 5, 1),
('Prova 1 Fisica', 3.0, 5, 1),
('Trabalho Fisica', 1.5, 5, 1),
-- Classe B (classroom_id=2) - múltiplas avaliações por disciplina
('Test 1 Matematica', 2.0, 1, 2),
('Prova 1 Matematica', 3.0, 1, 2),
('Trabalho Matematica', 1.5, 1, 2),
('Test 1 Portugues', 2.0, 2, 2),
('Prova 1 Portugues', 3.0, 2, 2),
('Trabalho Portugues', 1.5, 2, 2),
('Test 1 Historia', 2.0, 3, 2),
('Prova 1 Historia', 3.0, 3, 2),
('Trabalho Historia', 1.5, 3, 2),
('Test 1 Geografia', 2.0, 4, 2),
('Prova 1 Geografia', 3.0, 4, 2),
('Trabalho Geografia', 1.5, 4, 2),
('Test 1 Fisica', 2.0, 5, 2),
('Prova 1 Fisica', 3.0, 5, 2),
('Trabalho Fisica', 1.5, 5, 2),
-- Classe C (classroom_id=3) - múltiplas avaliações por disciplina
('Test 1 Matematica', 2.0, 1, 3),
('Prova 1 Matematica', 3.0, 1, 3),
('Trabalho Matematica', 1.5, 1, 3),
('Test 1 Portugues', 2.0, 2, 3),
('Prova 1 Portugues', 3.0, 2, 3),
('Trabalho Portugues', 1.5, 2, 3),
('Test 1 Historia', 2.0, 3, 3),
('Prova 1 Historia', 3.0, 3, 3),
('Trabalho Historia', 1.5, 3, 3),
('Test 1 Geografia', 2.0, 4, 3),
('Prova 1 Geografia', 3.0, 4, 3),
('Trabalho Geografia', 1.5, 4, 3),
('Test 1 Fisica', 2.0, 5, 3),
('Prova 1 Fisica', 3.0, 5, 3),
('Trabalho Fisica', 1.5, 5, 3),
-- Classe D (classroom_id=4) - múltiplas avaliações por disciplina
('Test 1 Matematica', 2.0, 1, 4),
('Prova 1 Matematica', 3.0, 1, 4),
('Trabalho Matematica', 1.5, 1, 4),
('Test 1 Portugues', 2.0, 2, 4),
('Prova 1 Portugues', 3.0, 2, 4),
('Trabalho Portugues', 1.5, 2, 4),
('Test 1 Historia', 2.0, 3, 4),
('Prova 1 Historia', 3.0, 3, 4),
('Trabalho Historia', 1.5, 3, 4),
('Test 1 Geografia', 2.0, 4, 4),
('Prova 1 Geografia', 3.0, 4, 4),
('Trabalho Geografia', 1.5, 4, 4),
('Test 1 Fisica', 2.0, 5, 4),
('Prova 1 Fisica', 3.0, 5, 4),
('Trabalho Fisica', 1.5, 5, 4),
-- Classe E (classroom_id=5) - múltiplas avaliações por disciplina
('Test 1 Matematica', 2.0, 1, 5),
('Prova 1 Matematica', 3.0, 1, 5),
('Trabalho Matematica', 1.5, 1, 5),
('Test 1 Portugues', 2.0, 2, 5),
('Prova 1 Portugues', 3.0, 2, 5),
('Trabalho Portugues', 1.5, 2, 5),
('Test 1 Historia', 2.0, 3, 5),
('Prova 1 Historia', 3.0, 3, 5),
('Trabalho Historia', 1.5, 3, 5),
('Test 1 Geografia', 2.0, 4, 5),
('Prova 1 Geografia', 3.0, 4, 5),
('Trabalho Geografia', 1.5, 4, 5),
('Test 1 Fisica', 2.0, 5, 5),
('Prova 1 Fisica', 3.0, 5, 5),
('Trabalho Fisica', 1.5, 5, 5);

INSERT INTO grade (student_id, evaluation_id, grade_value) VALUES
-- Classe A (classroom_id=1) - 5 alunos x 15 avaliações (3 por disciplina)
-- Pedro (1) - Matematica (1-3), Portugues (4-6), Historia (7-9), Geografia (10-12), Fisica (13-15)
(1, 1, 7.5), (1, 2, 8.2), (1, 3, 6.8), (1, 4, 9.1), (1, 5, 7.3), (1, 6, 8.0), (1, 7, 7.4), (1, 8, 6.9), (1, 9, 8.5), (1, 10, 7.1), (1, 11, 8.3), (1, 12, 7.6), (1, 13, 8.1), (1, 14, 7.8), (1, 15, 8.4),
-- Lucas (2) - Matematica (1-3), Portugues (4-6), Historia (7-9), Geografia (10-12), Fisica (13-15)
(2, 1, 8.7), (2, 2, 7.9), (2, 3, 8.1), (2, 4, 8.0), (2, 5, 7.6), (2, 6, 8.3), (2, 7, 7.4), (2, 8, 7.1), (2, 9, 8.5), (2, 10, 6.8), (2, 11, 8.3), (2, 12, 7.9), (2, 13, 8.1), (2, 14, 7.5), (2, 15, 8.2),
-- Marcos (3) - Matematica (1-3), Portugues (4-6), Historia (7-9), Geografia (10-12), Fisica (13-15)
(3, 1, 6.2), (3, 2, 7.8), (3, 3, 6.5), (3, 4, 8.3), (3, 5, 7.1), (3, 6, 7.9), (3, 7, 6.5), (3, 8, 7.2), (3, 9, 6.8), (3, 10, 8.3), (3, 11, 7.6), (3, 12, 8.1), (3, 13, 7.1), (3, 14, 7.8), (3, 15, 6.9),
-- Julia (4) - Matematica (1-3), Portugues (4-6), Historia (7-9), Geografia (10-12), Fisica (13-15)
(4, 1, 9.4), (4, 2, 8.9), (4, 3, 9.2), (4, 4, 9.0), (4, 5, 8.8), (4, 6, 8.7), (4, 7, 9.2), (4, 8, 8.9), (4, 9, 9.1), (4, 10, 8.8), (4, 11, 9.0), (4, 12, 8.7), (4, 13, 8.8), (4, 14, 8.5), (4, 15, 9.0),
-- Fernanda (5) - Matematica (1-3), Portugues (4-6), Historia (7-9), Geografia (10-12), Fisica (13-15)
(5, 1, 7.9), (5, 2, 6.7), (5, 3, 8.1), (5, 4, 7.6), (5, 5, 7.0), (5, 6, 8.2), (5, 7, 8.1), (5, 8, 7.4), (5, 9, 8.0), (5, 10, 7.6), (5, 11, 7.9), (5, 12, 7.3), (5, 13, 7.0), (5, 14, 7.8), (5, 15, 7.5),
-- Classe B (classroom_id=2) - 5 alunos x 15 avaliações (16-30)
-- Pedro (6) - Matematica (16-18), Portugues (19-21), Historia (22-24), Geografia (25-27), Fisica (28-30)
(6, 16, 8.1), (6, 17, 7.4), (6, 18, 7.9), (6, 19, 8.5), (6, 20, 8.0), (6, 21, 7.8), (6, 22, 7.9), (6, 23, 8.1), (6, 24, 7.6), (6, 25, 8.5), (6, 26, 8.0), (6, 27, 7.9), (6, 28, 8.0), (6, 29, 7.4), (6, 30, 8.1),
-- Lucas (7) - Matematica (16-18), Portugues (19-21), Historia (22-24), Geografia (25-27), Fisica (28-30)
(7, 16, 7.2), (7, 17, 7.8), (7, 18, 6.6), (7, 19, 8.7), (7, 20, 7.9), (7, 21, 8.3), (7, 22, 6.6), (7, 23, 7.8), (7, 24, 7.2), (7, 25, 8.7), (7, 26, 7.9), (7, 27, 8.3), (7, 28, 7.9), (7, 29, 7.8), (7, 30, 7.2),
-- Marcos (8) - Matematica (16-18), Portugues (19-21), Historia (22-24), Geografia (25-27), Fisica (28-30)
(8, 16, 6.8), (8, 17, 8.3), (8, 18, 7.1), (8, 19, 6.5), (8, 20, 7.4), (8, 21, 8.3), (8, 22, 7.1), (8, 23, 6.8), (8, 24, 7.4), (8, 25, 6.5), (8, 26, 7.4), (8, 27, 8.3), (8, 28, 7.4), (8, 29, 8.3), (8, 30, 6.8),
-- Julia (9) - Matematica (16-18), Portugues (19-21), Historia (22-24), Geografia (25-27), Fisica (28-30)
(9, 16, 9.0), (9, 17, 8.8), (9, 18, 9.3), (9, 19, 9.1), (9, 20, 8.6), (9, 21, 8.8), (9, 22, 9.3), (9, 23, 9.0), (9, 24, 8.6), (9, 25, 9.1), (9, 26, 8.6), (9, 27, 8.8), (9, 28, 8.6), (9, 29, 8.8), (9, 30, 9.0),
-- Fernanda (10) - Matematica (16-18), Portugues (19-21), Historia (22-24), Geografia (25-27), Fisica (28-30)
(10, 16, 8.2), (10, 17, 7.5), (10, 18, 8.0), (10, 19, 7.8), (10, 20, 8.4), (10, 21, 7.5), (10, 22, 8.0), (10, 23, 8.2), (10, 24, 8.4), (10, 25, 7.8), (10, 26, 8.4), (10, 27, 7.5), (10, 28, 8.4), (10, 29, 7.5), (10, 30, 8.2),
-- Classe C (classroom_id=3) - 5 alunos x 15 avaliações (31-45)
-- Pedro (11) - Matematica (31-33), Portugues (34-36), Historia (37-39), Geografia (40-42), Fisica (43-45)
(11, 31, 7.0), (11, 32, 8.1), (11, 33, 7.5), (11, 34, 8.4), (11, 35, 7.2), (11, 36, 8.1), (11, 37, 7.5), (11, 38, 7.0), (11, 39, 7.2), (11, 40, 8.4), (11, 41, 7.2), (11, 42, 8.1), (11, 43, 7.2), (11, 44, 8.1), (11, 45, 7.0),
-- Lucas (12) - Matematica (31-33), Portugues (34-36), Historia (37-39), Geografia (40-42), Fisica (43-45)
(12, 31, 8.4), (12, 32, 7.9), (12, 33, 8.2), (12, 34, 6.8), (12, 35, 8.0), (12, 36, 7.9), (12, 37, 8.2), (12, 38, 8.4), (12, 39, 8.0), (12, 40, 6.8), (12, 41, 8.0), (12, 42, 7.9), (12, 43, 8.0), (12, 44, 7.9), (12, 45, 8.4),
-- Marcos (13) - Matematica (31-33), Portugues (34-36), Historia (37-39), Geografia (40-42), Fisica (43-45)
(13, 31, 6.9), (13, 32, 8.3), (13, 33, 7.7), (13, 34, 7.1), (13, 35, 7.8), (13, 36, 8.3), (13, 37, 7.7), (13, 38, 6.9), (13, 39, 7.8), (13, 40, 7.1), (13, 41, 7.8), (13, 42, 8.3), (13, 43, 7.8), (13, 44, 8.3), (13, 45, 6.9),
-- Julia (14) - Matematica (31-33), Portugues (34-36), Historia (37-39), Geografia (40-42), Fisica (43-45)
(14, 31, 9.1), (14, 32, 8.7), (14, 33, 9.0), (14, 34, 8.9), (14, 35, 8.5), (14, 36, 8.7), (14, 37, 9.0), (14, 38, 9.1), (14, 39, 8.5), (14, 40, 8.9), (14, 41, 8.5), (14, 42, 8.7), (14, 43, 8.5), (14, 44, 8.7), (14, 45, 9.1),
-- Fernanda (15) - Matematica (31-33), Portugues (34-36), Historia (37-39), Geografia (40-42), Fisica (43-45)
(15, 31, 8.0), (15, 32, 7.6), (15, 33, 8.3), (15, 34, 8.2), (15, 35, 7.7), (15, 36, 7.6), (15, 37, 8.3), (15, 38, 8.0), (15, 39, 7.7), (15, 40, 8.2), (15, 41, 7.7), (15, 42, 7.6), (15, 43, 7.7), (15, 44, 7.6), (15, 45, 8.0),
-- Classe D (classroom_id=4) - 5 alunos x 15 avaliações (46-60)
-- Pedro (16) - Matematica (46-48), Portugues (49-51), Historia (52-54), Geografia (55-57), Fisica (58-60)
(16, 46, 7.7), (16, 47, 8.2), (16, 48, 7.4), (16, 49, 8.0), (16, 50, 7.5), (16, 51, 8.2), (16, 52, 7.4), (16, 53, 7.7), (16, 54, 7.5), (16, 55, 8.0), (16, 56, 7.5), (16, 57, 8.2), (16, 58, 7.5), (16, 59, 8.2), (16, 60, 7.7),
-- Lucas (17) - Matematica (46-48), Portugues (49-51), Historia (52-54), Geografia (55-57), Fisica (58-60)
(17, 46, 8.6), (17, 47, 7.9), (17, 48, 8.1), (17, 49, 7.3), (17, 50, 8.8), (17, 51, 7.9), (17, 52, 8.1), (17, 53, 8.6), (17, 54, 8.8), (17, 55, 7.3), (17, 56, 8.8), (17, 57, 7.9), (17, 58, 8.8), (17, 59, 7.9), (17, 60, 8.6),
-- Marcos (18) - Matematica (46-48), Portugues (49-51), Historia (52-54), Geografia (55-57), Fisica (58-60)
(18, 46, 7.4), (18, 47, 8.7), (18, 48, 7.2), (18, 49, 7.8), (18, 50, 7.9), (18, 51, 8.7), (18, 52, 7.2), (18, 53, 7.4), (18, 54, 7.9), (18, 55, 7.8), (18, 56, 7.9), (18, 57, 8.7), (18, 58, 7.9), (18, 59, 8.7), (18, 60, 7.4),
-- Julia (19) - Matematica (46-48), Portugues (49-51), Historia (52-54), Geografia (55-57), Fisica (58-60)
(19, 46, 9.2), (19, 47, 8.4), (19, 48, 9.1), (19, 49, 8.8), (19, 50, 8.7), (19, 51, 8.4), (19, 52, 9.1), (19, 53, 9.2), (19, 54, 8.7), (19, 55, 8.8), (19, 56, 8.7), (19, 57, 8.4), (19, 58, 8.7), (19, 59, 8.4), (19, 60, 9.2),
-- Fernanda (20) - Matematica (46-48), Portugues (49-51), Historia (52-54), Geografia (55-57), Fisica (58-60)
(20, 46, 7.8), (20, 47, 8.0), (20, 48, 7.9), (20, 49, 8.3), (20, 50, 7.6), (20, 51, 8.0), (20, 52, 7.9), (20, 53, 7.8), (20, 54, 7.6), (20, 55, 8.3), (20, 56, 7.6), (20, 57, 8.0), (20, 58, 7.6), (20, 59, 8.0), (20, 60, 7.8),
-- Classe E (classroom_id=5) - 5 alunos x 15 avaliações (61-75)
-- Pedro (21) - Matematica (61-63), Portugues (64-66), Historia (67-69), Geografia (70-72), Fisica (73-75)
(21, 61, 8.3), (21, 62, 7.1), (21, 63, 8.6), (21, 64, 8.0), (21, 65, 7.9), (21, 66, 7.1), (21, 67, 8.6), (21, 68, 8.3), (21, 69, 7.9), (21, 70, 8.0), (21, 71, 7.9), (21, 72, 7.1), (21, 73, 7.9), (21, 74, 7.1), (21, 75, 8.3),
-- Lucas (22) - Matematica (61-63), Portugues (64-66), Historia (67-69), Geografia (70-72), Fisica (73-75)
(22, 61, 7.5), (22, 62, 8.2), (22, 63, 7.8), (22, 64, 8.1), (22, 65, 7.4), (22, 66, 8.2), (22, 67, 7.8), (22, 68, 7.5), (22, 69, 7.4), (22, 70, 8.1), (22, 71, 7.4), (22, 72, 8.2), (22, 73, 7.4), (22, 74, 8.2), (22, 75, 7.5),
-- Marcos (23) - Matematica (61-63), Portugues (64-66), Historia (67-69), Geografia (70-72), Fisica (73-75)
(23, 61, 8.8), (23, 62, 7.6), (23, 63, 8.9), (23, 64, 7.7), (23, 65, 8.0), (23, 66, 7.6), (23, 67, 8.9), (23, 68, 8.8), (23, 69, 8.0), (23, 70, 7.7), (23, 71, 8.0), (23, 72, 7.6), (23, 73, 8.0), (23, 74, 7.6), (23, 75, 8.8),
-- Julia (24) - Matematica (61-63), Portugues (64-66), Historia (67-69), Geografia (70-72), Fisica (73-75)
(24, 61, 9.0), (24, 62, 8.4), (24, 63, 9.3), (24, 64, 8.8), (24, 65, 8.2), (24, 66, 8.4), (24, 67, 9.3), (24, 68, 9.0), (24, 69, 8.2), (24, 70, 8.8), (24, 71, 8.2), (24, 72, 8.4), (24, 73, 8.2), (24, 74, 8.4), (24, 75, 9.0),
-- Fernanda (25) - Matematica (61-63), Portugues (64-66), Historia (67-69), Geografia (70-72), Fisica (73-75)
(25, 61, 8.1), (25, 62, 7.9), (25, 63, 8.0), (25, 64, 7.5), (25, 65, 7.7), (25, 66, 7.9), (25, 67, 8.0), (25, 68, 8.1), (25, 69, 7.7), (25, 70, 7.5), (25, 71, 7.7), (25, 72, 7.9), (25, 73, 7.7), (25, 74, 7.9), (25, 75, 8.1);

INSERT INTO user_classroom (user_id, classroom_id, active) VALUES
(1, 1, TRUE),
(1, 3, TRUE),
(2, 1, TRUE),
(2, 2, TRUE),
(3, 3, TRUE),
(3, 5, TRUE),
(4, 4, TRUE),
(4, 2, TRUE),
(5, 5, TRUE),
(5, 4, TRUE);

INSERT INTO USER_DISCIPLINE_CLASSROOM (discipline_id, classroom_id, user_id, active) VALUES
(1, 1, 2, TRUE),
(2, 2, 2,TRUE),
(3, 3, 1, TRUE),
(3, 3, 1, TRUE),
(4, 4, 3, TRUE),
(2, 1, 3, TRUE),
(5, 5, 4,TRUE),
(3, 1, 4,TRUE);

-- A tabela grade_audit será populada automaticamente pelos triggers quando notas forem inseridas ou atualizadas
