# Informações

Autor: Pedro Isaac

> [Desafio STOOM (CRUD Backend)](https://gist.github.com/pedroits/9a42411f44ba9d75a70bfb7122c6f642)

Pra resolver o desafio criei o projeto <b>desafio-stoom-backend</b> rodando na `(porta 8080)`

Foi utilizada as seguintes tecnologias:

- Microserviços com Spring Boot 2.1.1
- Linguagem Java 11
- Mensageria ActiveMQ + JMS "embedded server/in-memory"
- Banco de Dados H2 "embedded server/in-memory"
- Spring Data JPA com JPQL
- Spring Security
- Design Patterns
- Arquitetura REST para os EndPoints
- JWT para autenticação dos EndPoints
- Google Geocoding API
- Testes Unitários com jUnit5 & Mockito

A Classe <b>"InitialDbConfig.java"</b> popula o Banco com os dados iniciais: 
- 1 Usuário para o JWT 
- 2 Endereços default

<h3>`Fluxo:`</h3> 
<p>
Ao cadastrar um Endereço, salva no banco H2, verifica se as coordenadas (latitude & longitude) foram informadas, se NÃO FORAM ele publica o enderdeço no Topic "buscar-geocoding-google"
O Consumer <b>"GeocodingConsumer"</b> que fica ouvindo o Topic, assim que identifica que algo foi registrado, consulta a API Geocoding do Google para baixar as coordenadas do endereço.
</p>

## _Executar_
```
mvn spring-boot:run
```

Exportei a lista dos EndPoints direto do Postman (arquivo esta na raiz do projeto)
- Melhor ser testado no Postman, pois já deixei configurado uma variavel de ambiente p/ todas as requisições. Ao executar a requisição de /login, automaticamente ele já preenche o Header "Authorization" Bearer das requests como Token gerado. 
```
> desafio-stoom-backend.postman_collection.json
```

Gerar o Package do Projeto (o arquivo `desafio-stoom-backend.jar` será criado na pasta `/target`)
```
# mvn clean package -Dmaven.test.skip=true
```


Docker (para buildar a imagem, o Package do projeto tem que estar criado)
```
Build Image | Dockerfile 

# docker build -t desafio-stoom .


Build and run with Compose | docker-compose.yml

# docker-compose up
```

 <h3>Testes Unitários</h4>
- obs: os testes não estão rodando por uma incompatibilidade com o `ActiveMQ "embedded server/in-memory"` do Spring Boot, mas foram testados e estão funcionais, caso queira testar terá que remover o Broker do ActiveMQ.
  
- A Incompatibilidade se dá pelo Broker fechar a conexão após cada teste gerando erro no processo, poderia ter subido um container com o Broker Fisico no docker-compose.yml e estaria resolvido, mas 
  não deu tempo, só pude trabalhar 1 dia para resolver esse desafio. 
>
>`shouldFetchAllAddresses()` Lista Todos os Endereços 
> 
>`shouldFetchOneAddressById()` Busca Endereço por ID
> 
>`shouldReturn404WhenFindAddressById()` Busca Endereço com ID não Cadastrado
> 
>`shouldCreateNewAddress()` Criar novo Endereço
> 
>`shouldReturn400WhenCreateNewAddressWithoutZipcode()` Cadastra novo Endereço sem dado obrigatório
> 
>`shouldUpdateAddress()` Atualiza Endereço
> 

