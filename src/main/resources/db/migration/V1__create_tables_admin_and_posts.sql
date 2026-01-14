-- 1. Limpeza total para evitar conflitos de nomes antigos
DROP TABLE IF EXISTS tb_posts CASCADE;
DROP TABLE IF EXISTS tb_categories CASCADE;
DROP TABLE IF EXISTS tb_admins CASCADE;
DROP TABLE IF EXISTS flyway_schema_history CASCADE;

-- 2. Criação da tabela de Administradores (Plural para bater com o Java)
CREATE TABLE tb_admins (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           email VARCHAR(255) UNIQUE NOT NULL,
                           password VARCHAR(255) NOT NULL,
                           profile_picture VARCHAR(255),
                           bio TEXT
);

-- 3. Criação da tabela de Categorias
CREATE TABLE tb_categories (
                               id BIGSERIAL PRIMARY KEY,
                               name VARCHAR(255) UNIQUE NOT NULL,
                               slug VARCHAR(255) UNIQUE NOT NULL
);

-- 4. Criação da tabela de Posts com as duas chaves estrangeiras
CREATE TABLE tb_posts (
                          id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          content TEXT NOT NULL,
                          image_url VARCHAR(255),
                          data_publication TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          summary VARCHAR(255),
                          status BOOLEAN DEFAULT TRUE,
                          admin_id BIGINT,
                          category_id BIGINT,

    -- Configuração das Constraints
                          CONSTRAINT fk_post_admin FOREIGN KEY (admin_id) REFERENCES tb_admins(id) ON DELETE SET NULL,
                          CONSTRAINT fk_post_category FOREIGN KEY (category_id) REFERENCES tb_categories(id) ON DELETE SET NULL
);