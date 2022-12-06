### FIAP: 1SCJR: _Integrations & DevelopmentTools_ - Trabalho Final - Turma 04

Aluno   | RM
--------- | ------
GIULIANA FERNANDES MUNARETTO | 346982
DANIEL DA CUNHA NOBREGA | 347114
JORGE ROSIVAN RODRIGUES BATISTA | 346669
PEDRO SANTANA | 347374
ROBERTO GUEDES GARRONES | 346541


Olá! Seja bem vindo  ;)

## Índice
1. [Sobre o Projeto](#projeto)
2. [Tecnologias Utilizadas](#tecnologias)
3. [Estrutura do Projeto](#estrutura)
4. [Metodologia](#metodologia)
5. [Observações (sobre os Servidores de Email)](#observacoes)


## Projeto
Desenvolva uma solução para o agronegócio que a coleta de dados via sensores de temperatura e umidade instalados em um drone.
A cada 10 segundos são enviados para message broker os dados de temperatura e umidade capturado naquele instante. Desta forma, 
desenvolva uma aplicação web na qual podemos informar manualmente:

	- id_drone (Identificador do Drone)
	- Latitude e longitude (Precisamos de uma latitude e longitude validas.);
	- Temperatura (-25º até 40º);
	- Umidade (0% - 100%);
	- Ativar rastreamento (ligada-desligada)
Regras:
1.	A cada 10 segundos é feito uma leitura dos dados (temperatura e umidade) e os dados enviados para um serviço de mensagens.
2.	O microsserviço deve enviar um alerta (pode ser um email) quando em 1 minuto:  
      a.	Temperatura (>= 35 ou <=0) ou (Umidade <= 15%).  
      b.	Envie no corpo do e-mail o id_drone e os valores capturados.

Requisitos:  
* Código publicado no GitHub.  
* Readme.md (use os estilos para formatação) com detalhes do projeto, de como subir, 
configurar, printscreen com o funcionamento da aplicação, ou um vídeo.  
* Pense na implementação que possa suportar vários drones, desta forma considere isso 
para o funcionamento dos alarmes e do rastreamento.  
* Use RabbitMQ ou Apache Kafka.  

## Tecnologias
        
        O Sistema foi desenvolvido no utilizando as seguintes tecnologias:
        - Docker com a imagem do Apache Kafka (um message broker com alto throughput, 
          confiável e com baixa latência para lidar com consumo de dados em real time e 
          do Apache ZooKeeper, que é um serviço escalável e altamente confiável para
          coordenação em ambientes distribuídos;
        - IDE de desenvolviemnto Intellij Community;
        - Gerenciadores de dependências: MAVEN e GRADLE;
        - Linguagens de programação JAVA - versão: 11 e KOTLIN;
        - Dependências: Java annotation LOMBOK, Spring WEB, Apache KAFKA e Java Mail Sender

## Estrutura
        Basicamente o projeto possui 3 pastas específicas, sendo:
        - KAFKA: pasta com o projeto de configuração do Apache Kafka;
        - PRODUCER: Projeto responsável por enviar mensagens para um ou mais Topics dos Brokers; 
        - CONSUMER: Projeto que consome as mensagens de um ou mais Brokers. Utiliza um ponteiro
          (topic, partition, offset) que controla a leitura em um Topic. Tem o estado de qual
          mensagem já foi consumida.


## Metodologia
> PROJETO **KAFKA**: Necessário para configurar e dar o start no Docker.
- O computador deve ter instalado o WSL (Windows Subsystem for Linux). Link e instruções:   
    https://learn.microsoft.com/pt-br/windows/wsl/install
- Necessário instalar o DOCKER. Link e instruções:  
    https://docs.docker.com/desktop/install/windows-install/
- Criar um projeto JAVA, vazio no Intelij (no caso, foi utilizado o nome KAFKA);
- Na pasta root do projeto, criar o arquivo `docker-compose.yml`, responsável pela configuração  
  do Docker,e dar o start nas imagens do KAFKA e ZOOKEEPER;

```xml
version: '2'
services:  
    zookeeper:
        image: confluentinc/cp-zookeeper:latest
        environment:
            ZOOKEEPER_CLIENT_PORT: 2181
            ZOOKEEPER_TICK_TIME: 2000
        ports:
            - 22181:2181

    kafka:
        image: confluentinc/cp-kafka:latest
        depends_on:
            - zookeeper
        ports:
            - 29092:29092
        environment:
            KAFKA_BROKER_ID: 1
            KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
            KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092, PLAINTEXT_HOST://localhost:29092
            KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
            KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
            KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```
- Executar o projeto, entrando no `CMD do Windows` e dar o comando `docker-compose up -d`, para criar a 
imagem e subir o docker (no Dock Desktop deve estar com o status *Running*), e após esse 
comando, o projeto pode ser fechado, não há necessidade de permanecer aberto:



![docker](/img/docker.png)

#
> PROJETO **PRODUCER**: Gera as mensagens e envia ao Kafka, onde são *enfileiradas* até serem *consumidas*.
- Rodar na porta 8081 (importante - evitar conflito com o Consumer);
- Criar o projeto MAVEM utilizando o spring initializr:  
  https://start.spring.io/, com as opções abaixo:

![producer](/img/producer.png)

Arquivos principais do projeto:
    
        - application.yaml: Arquivo com as configurações de comunicação com o Kafka;
        - Classe Drone: Gera coordenadas randômicas a partir do centro geodésico do Brasil, num raio de
          1.200km:
              * Possui flag (tracker) para ligar e desligar o rastreamento;
        - Classe Sensor: Monitora a Temperatura e Umidade, com opção de alarme para medidas críticas
          recorrentes em determinado período;
        - Classe EmailCfg / EmailController: Envia as mensagens de Alerta para um destinatário;
        - Classe MessageProducer: Envia as mensagens capturadas para a fila do Kafka;
        - Classe KafkaController: Responsável por expôr a API para receber os comandos do navegador, e são: 
            * http://localhost:8081/api/create_drone        => chamada responsável para criar os Drones; 
            * http://localhost:8081/api/tracker/{idDrone}   => chamada para ativar/desativar rastreamento;

#
Chamada da API para criação dos DRONES (http://localhost:8081/api/create_drone):  

![criando_drones](/img/criando_drones.png)

#
Tela do Producer sendo executado, sendo:  
* 3 drones criados (o tracker já entra ativado por default)
  * Valores mockados na classe Sensor, para simular alarme:  

      *private final int TEMPERATURA_MINIMA = -25;*  
      *private final int TEMPERATURA_MAXIMA = -1;//40;*  
      *private final int UMIDADE_MINIMA = 0;*  
      *private final int UMIDADE_MAXIMA = 14;//100;*

 ![producer_run](/img/producer_run.png)



#
> *PROJETO **CONSUMER**: Responsável por *consumir* as mensagens enfileiradas.
- Rodar na porta 8080 (importante - evitar conflito com o Producer);
- Criar o projeto MAVEM utilizando o spring initializr:  
  https://start.spring.io/, com as opções abaixo:


![consumer](/img/consumer.png)

Arquivos principais do projeto:  

        - application.yaml: Arquivo com as configurações de comunicação com o Kafka;
        - Classe MessageConsumer: Única classe, responsável por monitora a fila constamente e capturar as mensagens 
          enfileiradas no Kafka;
#
Tela do Consumer sendo executado:  

![consumer_run](/img/consumer_run.png)


## Telas do Alarme sendo disparado:

### Producer:  
___

![alerta_producer](/img/alerta_producer.png)

### Consumer:  
___
![alerta_consumer](/img/alerta_consumer.png)


## Tracker dos Drones sendo desativados:

### Chamada da API de Ativar / Desativar o Tracker (http://localhost:8081/api/tracker/3)  
___

![call_tracker](/img/call_tracker.png)


### Tracker desativado:  
___

![tracker_off](/img/tracker_off.png)


#

## Observacoes 

Os testes com os Servidores de E-mail populares (gmail/yahoo/hotmail/outlook), geravam alto índices de ***conexão recusada***.
Por esse motivo optou-se por utilizar um e-mail FAKE, que serviu perfeitamente para testes.  
Foi escolhido Mailtrap (https://mailtrap.io/blog/spring-send-email/), que oferece possibilidades de 500 mensagems por mes, com limites
de 5 mensagens por 10 segundos no plano gratuito, o que atende as necessidades.  
Link para maiores detalhes: (https://www.youtube.com/watch?v=JUMbefx67wk)

## Tela dos e-mails recebidos:  
___
![email](/img/email.png)



#  
___
