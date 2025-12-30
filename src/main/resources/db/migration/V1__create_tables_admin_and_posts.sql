-- Criação da tabela de Administradores
CREATE TABLE tb_admin (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) UNIQUE NOT NULL,
                          password VARCHAR(255) NOT NULL,
                          profile_picture VARCHAR(255),
                          bio TEXT
);

-- Criação da tabela de Posts
CREATE TABLE tb_posts (
                          id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          content TEXT NOT NULL,
                          image_url VARCHAR(255),
                          data_publication TIMESTAMP NOT NULL,
                          summary VARCHAR(255),
                          status BOOLEAN,
                          admin_id BIGINT,
    -- Chave Estrangeira ligando Post ao Admin
                          CONSTRAINT fk_post_admin FOREIGN KEY (admin_id) REFERENCES tb_admin(id)
);