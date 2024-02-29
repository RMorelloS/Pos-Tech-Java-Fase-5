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

### 1.1 
