-- Altere de tb_admin para tb_admins
CREATE TABLE tb_admins (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           email VARCHAR(255) UNIQUE NOT NULL,
                           password VARCHAR(255) NOT NULL,
                           profile_picture VARCHAR(255),
                           bio TEXT
);

CREATE TABLE tb_posts (
                          id BIGSERIAL PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          content TEXT NOT NULL,
                          image_url VARCHAR(255),
                          data_publication TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Use default para evitar null
                          summary VARCHAR(255),
                          status BOOLEAN,
                          admin_id BIGINT,
                          CONSTRAINT fk_post_admin FOREIGN KEY (admin_id) REFERENCES tb_admins(id) -- Apontando para tb_admins
);