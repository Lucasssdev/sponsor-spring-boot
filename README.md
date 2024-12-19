# sponsor-spring-boot
# Projeto: Gerenciamento de Usuários, Patrocinadores e Eventos

Este projeto é uma aplicação desenvolvida em **Java** utilizando o framework **Spring Boot** para gerenciar entidades de **Usuários**, **Patrocinadores** e **Eventos**. Ele segue uma arquitetura baseada em **Model**, **Controller** e **Service**, com suporte para operações CRUD (Create, Read, Update, Delete). Também foi implementado um teste unitário utilizando **JUnit** para validar o CRUD da entidade **User**.

## Estrutura do Projeto

### Entidades

1. **User**  
   Representa os usuários do sistema.
    - Campos:
        - `id`: Identificador único do usuário.
        - `name`: Nome do usuário.
        - `email`: E-mail do usuário.
        - `createdAt`: Data/hora de criação.
        - `updatedAt`: Data/hora da última atualização.

2. **Sponsors**  
   Representa os patrocinadores do sistema.
    - Campos:
        - `id`: Identificador único do patrocinador.
        - `name`: Nome do patrocinador.
        - `cnpj`: CNPJ do patrocinador.
        - `description`: Descrição do patrocinador.
        - `createdAt`: Data/hora de criação.
        - `updatedAt`: Data/hora da última atualização.
        - `createdBy`: Relacionamento com a entidade **User** que criou o registro.

3. **Events**  
   Representa os eventos do sistema.
    - Campos:
        - `id`: Identificador único do evento.
        - `name`: Nome do evento.
        - `description`: Descrição do evento.
        - `startDate`: Data de início do evento.
        - `startTime`: Horário de início do evento.
        - `sponsors`: Lista de patrocinadores associados ao evento (relacionamento com **Sponsors**).
        - `createdBy`: Relacionamento com a entidade **User** que criou o registro.
        - `createdAt`: Data/hora de criação.
        - `updatedAt`: Data/hora da última atualização.

### Camadas

1. **Model**: Define as entidades do sistema e suas relações utilizando anotações do JPA.

2. **Repository**: Interfaces que estendem `JpaRepository` para manipulação dos dados no banco.

3. **Service**: Contém as regras de negócio e faz a intermediação entre o Controller e o Repository.

4. **Controller**: Define os endpoints da API REST para realizar as operações CRUD.

### Exemplos de Endpoints

#### UserController
- `GET /users`: Lista todos os usuários.
- `GET /users/{id}`: Busca um usuário pelo ID.
- `POST /users`: Cria um novo usuário.
- `PUT /users/{id}`: Atualiza um usuário existente.
- `DELETE /users/{id}`: Exclui um usuário.

#### SponsorsController
- `GET /sponsors`: Lista todos os patrocinadores.
- `GET /sponsors/{id}`: Busca um patrocinador pelo ID.
- `POST /sponsors`: Cria um novo patrocinador.
- `PUT /sponsors/{id}`: Atualiza um patrocinador existente.
- `DELETE /sponsors/{id}`: Exclui um patrocinador.

#### EventsController
- `GET /events`: Lista todos os eventos.
- `GET /events/{id}`: Busca um evento pelo ID.
- `POST /events`: Cria um novo evento.
- `PUT /events/{id}`: Atualiza um evento existente.
- `DELETE /events/{id}`: Exclui um evento.

### Regras de Negócio

- **Sponsors**:
    - O campo `createdBy` deve referenciar um usuário válido existente no sistema.
    - As informações de `name`, `cnpj` e `description` são obrigatórias para criação.

- **Events**:
    - Deve conter pelo menos um patrocinador associado.
    - O campo `createdBy` deve referenciar um usuário válido.
    - As informações de `startDate` e `startTime` são obrigatórias para criação.

## Testes Unitários

Foi desenvolvido um teste unitário utilizando **JUnit** para validar as operações CRUD na entidade **User**. Os testes cobrem os seguintes cenários:

1. **Criação de Usuário**: Verifica se um usuário é criado corretamente.
2. **Leitura de Usuário**: Verifica se os dados de um usuário são recuperados corretamente pelo ID.
3. **Atualização de Usuário**: Verifica se os dados de um usuário são atualizados corretamente.
4. **Exclusão de Usuário**: Verifica se um usuário é excluído corretamente.

### Exemplo de Teste CRUD para User

```java
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("Teste User");
        user.setEmail("teste@user.com");
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Teste User", savedUser.getName());
    }

    @Test
    public void testFindUserById() {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        assertEquals("Teste User", user.getName());
    }
}
```

## Como Executar

1. Clone o repositório do projeto.
2. Configure o banco de dados no arquivo `application.properties`.
3. Execute o projeto com o comando:
   ```bash
   mvn spring-boot:run
   ```
4. Utilize ferramentas como **Postman** ou **cURL** para testar os endpoints da API.

## Exemplo de JSON para Requisições

### POST /users
```json
{
    "name": "João Silva",
    "email": "joao.silva@email.com"
}
```

### POST /sponsors
```json
{
    "name": "Patrocinador X",
    "cnpj": "12.345.678/0001-90",
    "description": "Empresa patrocinadora de eventos",
    "created_by": 1
}
```

### POST /events
```json
{
    "name": "Evento Y",
    "description": "Descrição do evento",
    "startDate": "2024-12-20",
    "startTime": "18:00:00",
    "sponsors": [1, 2],
    "created_by": 1
}
```

## Conclusão

O projeto fornece uma API REST funcional para gerenciar entidades de usuários, patrocinadores e eventos, com suporte completo para CRUD e testes unitários. Ele pode ser expandido para incluir funcionalidades adicionais, como autenticação, autorização e integração com serviços externos.

