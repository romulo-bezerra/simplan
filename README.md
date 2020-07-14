# simplan
Simples analizador de CSV com escrita de arquivos e integração com banco de dados para automatização e manipulação de dados.

##### Algumas informações:
* A lib utilizada para manipulação do CSV é a [OpenCSV](http://opencsv.sourceforge.net/). Logo há muitos recursos para manipulação de CSV. Correr documentação.  
* O projeto encontra-se numa estrutura básica, com poucas funcionalidades, mas pode ser ser incrementado facilmente.
* A classe principal de execução é a ApplicationInit, é nela onde se pode escrever as instruções de execução.
* Os arquivos de saída devem ser criados manualmente, ou em caso de melhoria, crie um esquema de geração automática desses arquivos com a classe File.
* O caminho padrão de armazenamento dos arquivos de entrada (arquivo csv para análise) e de saída é o `src/main/resources/`.
* O apicativo está com as configurações para conexão com o banco PostgreSQL, mas pode ser implementado outras configs de conexão.