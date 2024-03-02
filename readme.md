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

O microsserviço Ecommerce_Login utiliza, por padrão, a porta 8084.

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

### 1.5 Realizar login

Para realizar login, independente de usuário convencional/administrador, realizar uma requisição do tipo POST para o endpoint /login, passando as credenciais do usuário no corpo da requisição:

```bash
   curl --location 'localhost:8084/login' \
   --header 'Content-Type: application/x-www-form-urlencoded' \
   --header 'Cookie: JSESSIONID=672E5470F07A21312AED2D1550E66B0D' \
   --data-urlencode 'username=admin' \
   --data-urlencode 'password=postechfase5'
```

Resposta: retorna 200-OK e um token JWT para utilização nos demais endpoints
![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/49ca0408-6f86-4659-895b-ab45ce74f35e)

Caso o usuário não seja identificado, o HTML da página de login é retornado:
![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/16ad1e06-f58f-4b83-903d-fba7e5c4b4b1)


## 2. Ecommerce_Itens

O microsserviço Ecommerce_Itens utiliza, por padrão, a porta 8082.

### 2.1 Criar item

Para adicionar um item, realizar uma requisição do tipo POST para o endpoint /gestaoItens, passando as informações do novo item e o token JWT obtido via login realizado previamente:

```bash
  curl --location 'localhost:8082/gestaoItens' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA5Mzc0NjAyLCJleHAiOjE3MDkzNzU2MDJ9.VF843V2Oclt0tpS9-ueFDD2GQixDd0e84KWxJQXzSWKWw5FiPqjlX8TpfFrdu9t44xGbr11sIxIZiTYgHbyD7Q' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=3F3A2FB70C0553A79A03070E7038AB6A' \
--data '{
   "precoItem": 1.5,
  "nomeItem": "Coquinha",
  "marcaItem": "Coca cola",
  "descricao": "Refrigerante",
  "quantidadeEstoque": 30
}'
```

Resposta: retorna 200-OK e as informações do item criado
![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/035b916a-5546-4e2e-af1e-6c75d23d3ced)

**Importante: o token JWT necessita estar atrelado a um usuário administrador. Usuários convencionais receberão um erro 403 forbidden:**

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/5aded238-5036-4f0f-8a62-48c2a6d91a46)

Caso o item tenha informações inválidas, como preço ou quantidade negativos, o usuário receberá uma mensagem de erro como retorno:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/4bfafa6f-a661-45a6-9051-4b2fdd425487)


### 2.2 Obter todos os itens

Para obter todos os itens cadastrados, realizar uma requisição do tipo GET para o endpoint /obterItens:


```bash
curl --location 'localhost:8082/gestaoItens' \
--header 'Cookie: JSESSIONID=07C1643AFD772AFC087B6959E60A2A50' \
--data ''
```

Resposta: retorna 200-OK e as informações de todos os itens criados:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/a1c55b93-ea8e-44ff-9fae-f5358207fd4f)

Para requisições GET, o endpoint /gestaoItens é aberto a todos os usuários, mesmo os não cadastrados. Por isso, não foi necessário enviar um token JWT na requisição.

### 2.3 Obter item por ID

Para obter um item específico, realizar uma requisição do tipo GET para o endpoint /gestaoItens, passando o ID do item como parâmetro na URL:


```bash
curl --location 'http://localhost:8082/gestaoItens/85faaef6-0cfd-47ca-90bc-6b4292cb6fd9' \
--header 'Cookie: JSESSIONID=07C1643AFD772AFC087B6959E60A2A50' \
--data ''
```

Resposta: retorna 200-OK e a informação do item buscado:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/ccfe5b96-977f-498f-a96c-fc5210d7311a)

Caso o item não exista, uma mensagem de erro será retornada:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/2a78b478-c911-4388-a401-57c5c35b8cc7)

Para requisições GET, o endpoint /gestaoItens/{ID} é aberto a todos os usuários, mesmo os não cadastrados. Por isso, não foi necessário enviar um token JWT na requisição.


### 2.4 Atualizar item

Para atualizar um item, realizar uma requisição do tipo PUT para o endpoint /gestaoItens, passando o ID do item como parâmetro na URL e as informações atualizadas do item no body:

```bash

curl --location --request PUT 'localhost:8082/gestaoItens/770e24fc-1316-48c4-b165-d01a02c7079b' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA3OTU4OTY5LCJleHAiOjE3MDc5NTk5Njl9.eREVfa--vf2lWCC01U1xc9PovES8Se71rhTX1sHstLEONEWirTSo5idbP9tRS-jYEGEBojSlqUcIM6pHKBJGJQ' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=3351404F993FB5BC7CBED6A34B7DEBB2' \
--data '{
    "precoItem": 10.50,
  "nomeItem": "Produto de Exemplo1",
  "marcaItem": "Marca Exemplo1",
  "descricao": "Descrição do Produto1",
  "quantidadeEstoque": 51
}'

```

Resposta: retorna 200-OK e a informação atualizada do item:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/278d4513-c919-4d79-8ad7-70b361cf6d6b)



**Importante: o token JWT necessita estar atrelado a um usuário administrador. Usuários convencionais receberão um erro 403 forbidden:**

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/8f565cd7-332b-42bc-bd82-653a3f7ac94c)



### 2.5 Excluir item

Para excluir um item, realizar uma requisição do tipo DELETE para o endpoint /gestaoItens, passando o ID do item como parâmetro na URL:

```bash
curl --location --request DELETE 'localhost:8082/gestaoItens/85faaef6-0cfd-47ca-90bc-6b4292cb6fd3' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA5Mzc1MzE0LCJleHAiOjE3MDkzNzYzMTR9.VdA81Iwo3et0vyodci6AjvPD4o2gVIOYIskJQ94cSTdfOsH61iKYpF-Mrpmo4KEpL6g-g1NgOfE0mPVDMR0vlg' \
--header 'Cookie: JSESSIONID=BE5B5AB0D8AB32EED13D5BE01262E616' \
--data ''
```

Resposta: retorna 200-OK e o ID do item excluído.

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/d822b4ce-900d-4057-ae08-c14d95e447b7)


**Importante: o token JWT necessita estar atrelado a um usuário administrador. Usuários convencionais receberão um erro 403 forbidden:**

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/cf4603cb-2b6f-42c2-8b44-bdc822ec958e)



## 3. Ecommerce_Carrinho

O microsserviço Ecommerce_Carrinho utiliza, por padrão, a porta 8081.

### 3.1 Adicionar item ao carrinho

Para adicionar um item ao carrinho, realizar uma requisição do tipo POST para o endpoint /carrinho/adicionarItem, passando as informações do Pedido na requisição (UUID do item e quantidade):

```bash
curl --location 'localhost:8081/carrinho/adicionarItem' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA5Mzc2Mjk2LCJleHAiOjE3MDkzNzcyOTZ9.X5J1RIQWRVLiCMX2X8TvsCr1cXfuW-sUYD6V3udnoMVZBdGpuLmJ-h248_rRr02jiq2eruzL94TCfh_IZGuQeg' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=5247B208DCFE5B628522A24C0E70D938' \
--data '{
    "idItem": "2487e581-b5f9-44e6-a772-60f3ee48fd59",
    "quantidade": 6
}'
```

Resposta: retorna 200 - OK e o carrinho do usuário, contendo todos os itens e o valor total do carrinho:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/e1972449-981b-4e2f-8d26-4ca664befe10)

Caso o microsserviço de carrinho solicite uma quantidade superior à quantidade em estoque, o microsserviço de itens retorna um erro:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/799982b1-5fce-43d2-be4e-3156c873373a)


### 3.2 Remover item do carrinho

Para remover um item do carrinho, realizar uma requisição do tipo DELETE para o endpoint /carrinho/removerItem, passando o ID do item como parâmetro da URL:

```bash
curl --location --request POST 'localhost:8081/carrinho/removerItem/2487e581-b5f9-44e6-a772-60f3ee48fd59' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA5Mzc2Mjk2LCJleHAiOjE3MDkzNzcyOTZ9.X5J1RIQWRVLiCMX2X8TvsCr1cXfuW-sUYD6V3udnoMVZBdGpuLmJ-h248_rRr02jiq2eruzL94TCfh_IZGuQeg' \
--header 'Cookie: JSESSIONID=5247B208DCFE5B628522A24C0E70D938' \
--data ''
```

Resposta: retorna 200 - OK e o carrinho do usuário atualizado, inclusive com o valor total do carrinho recalculado:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/18bd50b8-5dae-4a25-b5fc-46e1f837ba3a)

Após a remoção de um item do carrinho, o microsserviço de itens adicionará novamente a quantidade solicitada ao estoque:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/9607a45f-6c2c-4022-baca-2ea61ba188a1)

Caso o item não seja encontrado no carrinho do usuário ou não exista, uma mensagem de erro será retornada pelo microsserviço de itens:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/0f4e9c84-1cca-4a31-9316-1cf9c6c6c394)


### 3.3 Visualizar carrinho

Para visualizar o carrinho, realizar uma requisição do tipo GET para o endpoint /carrinho/visualizarCarrinho:

```bash
curl --location 'localhost:8081/carrinho/visualizarCarrinho' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA5Mzc2Mjk2LCJleHAiOjE3MDkzNzcyOTZ9.X5J1RIQWRVLiCMX2X8TvsCr1cXfuW-sUYD6V3udnoMVZBdGpuLmJ-h248_rRr02jiq2eruzL94TCfh_IZGuQeg' \
--header 'Cookie: JSESSIONID=5247B208DCFE5B628522A24C0E70D938' \
--data ''
```

Resposta: retorna 200-OK e os itens que constam no carrinho, bem como o valor total dos itens:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/192f840c-cf64-40c6-ac77-edc5d496353b)



## 4. Ecommerce_Pagamento

O microsserviço Ecommerce_Carrinho utiliza, por padrão, a porta 8083.


### 4.1 Adicionar forma de pagamento

Para adicionar uma forma de pagamento, realizar uma requisição do tipo POST para o endpoint /formaPagamento, passando as informações da forma de pagamento:

```bash

curl --location 'localhost:8083/formaPagamento/' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA5Mzc3MzA5LCJleHAiOjE3MDkzNzgzMDl9.d0BgSMQ43MLonGGzAfFuzkA8Zv2MW1xR3TYaNqA3I7LbVStJjBm6Cz9rPM4_qjQskSw9qH3VoWPKYQKBmvw4pA' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=6487F9325F5705B8BFC6AE4A13AD56E6' \
--data '{
    "apelido":"cartao-ricardo-11",
    "numeroCartao":"1234567891011",
    "cvv":212121,
    "dataVencimento":"19/05/2026"
}'

```

Resposta: retorna 200-OK e as informações da forma de pagamento adicionada:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/1529b36c-6699-4bbb-8913-bebd62f096d5)

### 4.2 Remover forma de pagamento

Para deletar uma forma de pagamento, realizar uma requisição do tipo DELETE para o endpoint /formaPagamento, passando o apelido da forma de pagamento a ser excluída:

```bash
curl --location --request DELETE 'localhost:8083/formaPagamento/cartao-ricardo-11' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA5Mzc3MzA5LCJleHAiOjE3MDkzNzgzMDl9.d0BgSMQ43MLonGGzAfFuzkA8Zv2MW1xR3TYaNqA3I7LbVStJjBm6Cz9rPM4_qjQskSw9qH3VoWPKYQKBmvw4pA' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=6487F9325F5705B8BFC6AE4A13AD56E6' \
--data '{
    "apelido":"cartao-ricardo",
    "numeroCartao":"1234567890",
    "CVV":"233",
    "dataVencimento":"19/05/2024"
}'
```

Resposta: retorna 200-OK e o apelido da forma de pagamento excluída:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/c2549043-17a8-4d40-a4f4-9e7a0b5df84d)


### 4.3 Visualizar forma de pagamento pelo apelido

Para visualizar uma forma de pagamento pelo apelido, realizar uma requisição do tipo GET para o endpoint /formaPagamento, passando o apelido da forma de pagamento como parâmetro na URL:

```bash
curl --location 'localhost:8083/formaPagamento/cartao-ricardo-11' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA5Mzc3MzA5LCJleHAiOjE3MDkzNzgzMDl9.d0BgSMQ43MLonGGzAfFuzkA8Zv2MW1xR3TYaNqA3I7LbVStJjBm6Cz9rPM4_qjQskSw9qH3VoWPKYQKBmvw4pA' \
--header 'Cookie: JSESSIONID=6487F9325F5705B8BFC6AE4A13AD56E6' \
--data ''
```

Resposta: retorna 200-OK e as informações da forma de pagamento buscadas:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/acd3301c-9d5b-4f81-835d-d6f7ffb26e20)

Caso a forma de pagamento não exista, uma mensagem de erro será retornada:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/d1aca8ce-97fe-49e2-91a0-3bba2dd6e7aa)



### 4.4 Atualizar forma de pagamento

Para atualizar uma forma de pagamento, realizar uma requisição do tipo PUT para o endpoint/formaPagamento, passando o apelido da forma de pagamento como parâmetro na URL e as informações atualizadas no corpo da requisição:

```bash
curl --location --request PUT 'localhost:8083/formaPagamento/cartao-ricardo-11' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA5Mzc3MzA5LCJleHAiOjE3MDkzNzgzMDl9.d0BgSMQ43MLonGGzAfFuzkA8Zv2MW1xR3TYaNqA3I7LbVStJjBm6Cz9rPM4_qjQskSw9qH3VoWPKYQKBmvw4pA' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=6487F9325F5705B8BFC6AE4A13AD56E6' \
--data '{
    "apelido": "cartao-ricardo-11",
    "numeroCartao":"1110987654321",
    "cvv":212121,
    "dataVencimento":"19/05/2026"
}'
```

Resposta: retorna 200-OK e as informações atualizadas da forma de pagamento:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/e27b646c-64e3-4ba0-923a-2ef69f0b1798)


### 4.5 Visualizar fatura

Para visualizar a fatura total, realizar uma requisição do tipo GET para o endpoint /pagamento/visualizarFatura, passando o apelido da forma de pagamento como parâmetro na URL:

```bash
curl --location 'localhost:8083/pagamento/visualizarFatura/cartao-ricardo-11' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA5Mzc3MzA5LCJleHAiOjE3MDkzNzgzMDl9.d0BgSMQ43MLonGGzAfFuzkA8Zv2MW1xR3TYaNqA3I7LbVStJjBm6Cz9rPM4_qjQskSw9qH3VoWPKYQKBmvw4pA' \
--header 'Cookie: JSESSIONID=6487F9325F5705B8BFC6AE4A13AD56E6'
```

Resposta: retorna 200-OK e um JSON contendo três informações principais:

1. "itensCarrinho": Lista e informações de todos os itens adicionados ao carrinho
2. "totalCarrinho": Valor total calculado para a fatura, de acordo com os itens adicionados
3. "userLogin": login do usuário atrelado ao carrinho
4. "formaPagamento": informações da forma de pagamento selecionadas para liquidação da fatura

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/16bbcdd9-42d6-4bed-81a7-bba73789c9b7)


### 4.6 Pagar fatura

Para pagar a fatura, realizar uma requisição do tipo POST para o endpoint /pagamento/pagarFatura, passando o apelido da forma de pagamento como parâmetro na URL:

curl --location 'localhost:8083/pagamento/pagarFatura/cartao-ricardo-11' \
--header 'token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJBRE1JTiJ9XSwiaWF0IjoxNzA5Mzc3MzA5LCJleHAiOjE3MDkzNzgzMDl9.d0BgSMQ43MLonGGzAfFuzkA8Zv2MW1xR3TYaNqA3I7LbVStJjBm6Cz9rPM4_qjQskSw9qH3VoWPKYQKBmvw4pA' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=6487F9325F5705B8BFC6AE4A13AD56E6' \
--data '{
    "apelido":"cartao-ricardo-11",
    "numeroCartao":"teste",
    "cvv":212121,
    "dataVencimento":"19/05/2026"
}'

Resposta: retorna 200-OK e uma mensagem mostrando que a fatura foi paga. O carrinho do usuário e o valor total a ser pago serão zerados pelo microsserviço de carrinho.

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/e233955c-dc06-4e02-93fe-8618d4636988)

Caso o usuário tente visualizar novamente o carrinho, este estará vazio novamente:

![image](https://github.com/RMorelloS/Pos-Tech-Java-Fase-5/assets/32580031/e34ac63d-7cb6-4ded-a99e-0e8979443fa2)





