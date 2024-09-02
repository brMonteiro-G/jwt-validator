# JWT Validator 📜

O Jwt validator é um projeto construido com o intuito de testar as habilidades e conhecimentos com spring security + aws

## Tecnologias ✨

- Spring security
- Java 17 
- DynamoDb
- Localstack
- EC2 + VPC
- CI/CD com github actions

## Como funciona? ✨

Por padrão temos três rotas simples: 

- /signup --> Faz o cadastro de um usuário em uma base de dados. 
- /login  --> Verifica a presença do usuário no cadastro e gera um token algumas contendo algumas claims
- /retrieve  --> Verifica se o usuário possui as roles necessárias baseadas no token para acessar o recurso

## Teste Local ✨

- Para rodar localmente, será necessário a utilização do docker e terminal shell para execução de script.

  Na raiz do projeto, basta rodar o comando ```sh setup-development.sh``` que será criada a infraestrutura necessária para o desenvolvimento da aplicação e como porta padrão para teste no postman estará habilitada a porta 8080.  
  Esse arquivo permitirá a criação de um container contendo um dynamodb que registrará os dados durante os testes e desenvolvimento. 


## Claims ✨

No momento, o bearer token gerado contém 3 claims diferentes: 

- Name  --> claim que referência o usuário 
- Seed  --> é um número primo gerado aleatóriamente para cada chamada
- Roles  --> indicação da posição do usuário dentro do sistema, baseado no domínio de email cadastrado, sendo assim, para domínios @gmail (ROLE_ADMIN), @hotmail (ROLE_MEMBER), @outros (ROLE_EXTERNAL).

- 
## Validação ✨

Para garantir uma boa experiência de testes, podemos realizar a validação dos jwt na rota /retrieve. 

### Cenário de testes: 
 - Um usuário não autenticado passa na requisição um token com formatação inválida.
 - As claims contidas no token passado não atendem as regras de geraçaõ das mesmas (Não possuir mais de 3 claims, além das restrições de caracteres presentes)

