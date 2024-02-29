# Pós Tech - Fase 5

Para a entrega do 5º tech challenge, foram desenvolvidos 4 microsserviços para um e-commerce.

Foram desenvolvidas os seguintes microsserviços, cada qual alocado em uma branch diferente deste repositório:

1. Ecommerce_Login: Cadastro de usuários convencionais e usuários administradores. Geração de token JWT para autenticação das solicitações aos demais serviços. **Todos os microsserviços validam o token para cada requisição.**
2. Ecommerce_Itens: Cadastro, visualização, exclusão e atualização de itens. 
3. Ecommerce_Carrinho: Inclusão de itens no carrinho de compras. Visualização dos itens e exclusão do carrinho.
4. Ecommerce_Pagamento: Cadastro, visualização, exclusão e atualização de formas de pagamento. Visualização e pagamento de fatura.
   
Foram utilizadas as seguintes tecnologias/técnicas:
 * **Java + Spring** - recebimento e processamento de requisições
 * **Banco de dados NoSQL DynamoDB** - armazenamento das informações gerais de cada usuário, item, carrinho ou forma de pagamento
 * **Spring Security** - autenticação e permissionamento.
 * **JWT** -  geração de token JWT pelo serviço de login para autenticação pelos demais serviços. Filtro de solicitações para usuários de acordo com as roles (ADMIN, USER)

A arquitetura do sistema é apresentada no fluxo a seguir:

![Diagrama_postech_fase5 drawio](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/c3411817-be0f-4eb7-9c21-96e30508d9dc)

# Funcionalidades

## 1. Ecommerce_Login

### 1.1 Criar usuário convencional

Para criar um usuário convencional, realizar uma requisição do tipo POST para o endpoint /usuarios/criarUsuario, passando as credenciais do usuário:

```bash
   curl --location 'localhost:8084/usuarios/criarUsuario' \
   --header 'Content-Type: application/json' \
   --header 'Cookie: JSESSIONID=B9FBBA1519FFF896443C0E16EA70D06D' \
   --data '{
   "userlogin":"usuarioExemplo",
   "userkey":"usuarioExemplo",
   "authorities":"USER"
   }'
```

Resposta: retorna 200-OK e as informações do usuário criado
![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/82deb9c1-88e8-4ddc-893a-b8b1d56bfad7)


### 1.2 Criar usuário administrador

Para criar um usuário administrador, realizar uma requisição do tipo POST para o endpoint /usuarios/criarUsuarioAdmin, passando as credenciais do usuário e o cookie do usuário administrador logado:

**Importante: para criar um usuário administrador, é necessário estar logado com outro usuário administrador.**

```bash
curl --location 'localhost:8084/usuarios/criarUsuarioAdmin' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=74C9850649D50E7A82C8BFCDA091532C' \
--data '{
"userlogin":"ricardo",
"userkey":"ricardo",
"authorities":"ADMIN"
}'
```

Resposta: retorna 200-OK e as informações do usuário administrador criado

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/3f6391ac-340c-4e4d-89e9-6c00eb72a3ac)

**Importante: caso um usuário convencional tente cadastrar um usuário administrador, será retornada a seguinte mensagem:**

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/a130aedd-faf8-46ba-ba70-0708e8c226a4)


### 1.3 Atualizar usuário convencional

Para atualizar um usuário convencional, realizar uma requisição do tipo PUT para o endpoint /usuarios/atualizarUsuario, passando as novas credenciais do usuário:

```bash
curl --location --request PUT 'localhost:8084/usuarios/atualizarUsuario' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=74C9850649D50E7A82C8BFCDA091532C' \
--data '{
"userlogin":"usuarioExemplo",
"userkey":"usuarioExemploNovaSenha",
"authorities":"USER"
}'
```

Resposta: retorna 200-OK e as informações atualizadas do usuário

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/b4fc18f8-6ae7-4b1a-8ef9-daccb2018e4a)

**Importante: existe uma cláusula no código para os endpoints abertos que impede um usuário convencional de cadastrar ou atualizar suas authorities para as de administrador**

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/8710300c-6572-489d-a62c-db91f0fa828b)


### 1.4 Atualizar usuário administrador

Para atualizar um usuário administrador, realizar uma requisição do tipo PUT para o endpoint /usuarios/atualizarUsuarioAdmin, passando as novas credenciais do usuário:

```bash
   curl --location --request PUT 'localhost:8084/usuarios/atualizarUsuarioAdmin' \
   --header 'Content-Type: application/json' \
   --header 'Cookie: JSESSIONID=672E5470F07A21312AED2D1550E66B0D' \
   --data '{
   "userlogin":"ricardo",
   "userkey":"ricardoNovaSenha",
   "authorities":"ADMIN"
   }'
```

Resposta: retorna 200-OK e as informações atualizadas do usuário administrador
![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/a8a89517-a983-4fb8-adeb-26875600710f)


**Importante: caso um usuário convencional tente atualizar um usuário administrador, será retornada a seguinte mensagem:**

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/e7f2d739-0507-4f62-a20c-fa97393a68c0)



