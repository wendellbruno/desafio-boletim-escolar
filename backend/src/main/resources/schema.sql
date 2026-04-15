CREATE TABLE users (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

CREATE TABLE CLASSROOM (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE STUDENT (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  classroom_id BIGINT,
  CONSTRAINT fk_student_classroom FOREIGN KEY (classroom_id) REFERENCES classroom(id)
);

CREATE TABLE DISCIPLINE (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE EVALUATION (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  weight DECIMAL(10,2) NOT NULL,
  discipline_id BIGINT,
  CONSTRAINT fk_evaluation_discipline FOREIGN KEY (discipline_id) REFERENCES discipline(id)
);


CREATE TABLE GRADE (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  student_id BIGINT,
  evaluation_id BIGINT,
  grade_value DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_grade_student FOREIGN KEY (student_id) REFERENCES student(id),
  CONSTRAINT fk_grade_evaluation FOREIGN KEY (evaluation_id) REFERENCES evaluation(id)
);

ALTER TABLE GRADE
ADD CONSTRAINT unique_student_evaluation
UNIQUE (student_id, evaluation_id);

CREATE TABLE USER_CLASSROOM (
  id BIGINT AUTO_INCREMENT  PRIMARY KEY,
  user_id BIGINT,
  classroom_id BIGINT,
  active BOOLEAN NOT NULL,
  CONSTRAINT fk_user_classroom_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_user_classroom_classroom FOREIGN KEY (classroom_id) REFERENCES classroom(id)
);

CREATE TABLE  USER_DISCIPLINE_CLASSROOM (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  discipline_id BIGINT,
  classroom_id BIGINT,
  user_id BIGINT,
  active BOOLEAN NOT NULL,
  CONSTRAINT fk_USER_DISCIPLINE_CLASSROOM_discipline FOREIGN KEY (discipline_id) REFERENCES discipline(id),
  CONSTRAINT fk_USER_DISCIPLINE_CLASSROOM_classroom FOREIGN KEY (classroom_id) REFERENCES classroom(id),
  CONSTRAINT fk_USER_DISCIPLINE_CLASSROOM_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE GRADE_AUDIT (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  student_id BIGINT,
  evaluation_id BIGINT,
  discipline_id BIGINT,
  modified_by BIGINT,
  old_value DECIMAL(10,2) NOT NULL,
  new_value DECIMAL(10,2) NOT NULL,
  modification_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_audit_student FOREIGN KEY (student_id) REFERENCES student(id),
  CONSTRAINT fk_audit_evaluation FOREIGN KEY (evaluation_id) REFERENCES evaluation(id),
  CONSTRAINT fk_audit_discipline FOREIGN KEY (discipline_id) REFERENCES discipline(id),
  CONSTRAINT fk_audit_user FOREIGN KEY (modified_by) REFERENCES users(id)
);

--CREATE TRIGGER trg_auditoria_nota
--AFTER UPDATE ON grade
--FOR EACH ROW
--CALL "AuditoriaNotaTrigger";