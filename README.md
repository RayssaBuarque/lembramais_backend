# Lembra+ API (Backend)

Este é o repositório do backend do **Lembra+**, um ecossistema de saúde digital focado no cuidado contínuo de pacientes. A API REST foi desenvolvida para conectar o aplicativo mobile (utilizado por pacientes e cuidadores) ao portal web (utilizado por médicos), gerenciando dados clínicos, rotinas de medicação, interações medicamentosas e histórico de saúde.

## 🚀 Tecnologias e Dependências

O projeto foi construído utilizando as seguintes tecnologias e bibliotecas:

*   **Linguagem:** [Kotlin](https://kotlinlang.org/)
*   **Framework:** [Spring Boot 3+](https://spring.io/projects/spring-boot) (Web, Data JPA)
*   **JDK:** Java 25 (ou a versão configurada no `build.gradle.kts`)
*   **Gerenciador de Dependências:** Gradle (Kotlin DSL)
*   **Banco de Dados:** [MySQL](https://www.mysql.com/) (hospedado na nuvem via [Aiven](https://aiven.io/))
*   **Documentação da API:** [Springdoc OpenAPI (Swagger)](https://springdoc.org/)
*   **Deploy:** [Vercel](https://vercel.com/) (via Docker Container)

### Principais Dependências (`build.gradle.kts`)
Para que a aplicação funcione, as seguintes dependências estão configuradas:
*   `spring-boot-starter-web`: Para criação dos endpoints REST.
*   `spring-boot-starter-data-jpa`: Para persistência e mapeamento objeto-relacional (ORM).
*   `mysql-connector-j`: Driver oficial de conexão com o MySQL.
*   `springdoc-openapi-starter-webmvc-ui`: Para geração automática da documentação Swagger.

---

## ⚙️ Pré-requisitos para Execução

Para rodar este projeto localmente, você precisará ter instalado em sua máquina:
1.  **JDK (Java Development Kit):** Versão 17 ou superior (o projeto foi testado com o JDK 25).
2.  **Git:** Para clonar o repositório.
3.  Um cliente MySQL (opcional, para visualizar o banco), como MySQL Workbench ou DBeaver.
4.  Acesso a uma instância do **MySQL** (local ou na nuvem).

---

## 🛠️ Configuração do Ambiente (Variáveis de Ambiente)

O projeto utiliza variáveis de ambiente para proteger as credenciais do banco de dados (seguindo as diretrizes da LGPD). **Nenhuma senha real deve ser commitada no código.**

Crie um arquivo chamado **`.env`** na raiz do projeto e insira as credenciais do seu banco de dados MySQL:

```env
SPRING_DATASOURCE_URL=jdbc:mysql://SEU_HOST:SUA_PORTA/lembra_mais?useSSL=true&serverTimezone=America/Sao_Paulo
SPRING_DATASOURCE_USERNAME=seu_usuario
SPRING_DATASOURCE_PASSWORD=sua_senha
```

*Nota: Certifique-se de que o arquivo `.env` está listado no seu `.gitignore`.*

---

## 🏃‍♂️ Como Executar a Aplicação (Localmente)

Existem duas formas principais de rodar o projeto localmente:

### Opção 1: Pela IDE (IntelliJ IDEA)
1. Abra o projeto no IntelliJ IDEA.
2. Aguarde o Gradle baixar as dependências.
3. No canto superior direito, clique na setinha ao lado do botão de "Run" e selecione **Edit Configurations**.
4. Na aba de configuração da sua aplicação Spring Boot, localize o campo **Environment variables**.
5. Adicione as três variáveis de ambiente (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`) com os valores do seu banco.
6. Clique no botão verde **Run** (ou pressione `Shift + F10`).

### Opção 2: Pelo Terminal
No terminal, dentro da raiz do projeto, execute o Gradle wrapper passando as variáveis de ambiente.

**No Linux/Mac:**
```bash
export $(cat .env | xargs) && ./gradlew bootRun
```

**No Windows (PowerShell):**
```powershell
# Carregue as variáveis manualmente ou configure no sistema e execute:
./gradlew bootRun
```

---

## 📚 Documentação da API (Swagger)

Com a aplicação rodando (seja localmente ou no Vercel), a documentação interativa do Swagger estará disponível nos seguintes endereços:

*   **Swagger UI:** `https://lembramais-backend.vercel.app/swagger-ui/index.html`
*   **OpenAPI JSON:** `https://lembramais-backend.vercel.app/v3/api-docs`

A interface do Swagger permite visualizar todos os endpoints (Pacientes, Medicamentos, Histórico, etc.), ver os contratos de requisição/resposta e até testar as chamadas diretamente pelo navegador.

---

## ☁️ Deploy (Produção no Vercel)

O deploy do backend é feito automaticamente via integração com o GitHub. A aplicação é empacotada em um container Docker.

*   **Dockerfile:** O arquivo `Dockerfile.vercel` na raiz do projeto é responsável por compilar o `.jar` executável e iniciá-lo na porta injetada pela Vercel (`${PORT:-80}`).
*   **URL de Produção:** `https://lembramais-backend.vercel.app/api/v1/...`
*   **Aviso de Cold Start:** Como o Vercel utiliza arquitetura *Serverless*, a aplicação entra em estado de hibernação após alguns minutos sem tráfego. A primeira requisição após esse período pode demorar de 30 a 60 segundos para responder.

---

## 🗺️ Estrutura do Banco de Dados

O backend consome um banco de dados relacional (MySQL) com as seguintes entidades principais:
*   `usuarios`
*   `pacientes`
*   `alergias` e `condicoes_cronicas`
*   `contatos_emergencia`
*   `medicamentos` e `horarios_medicamentos`
*   `interacoes_medicamentosas`
*   `historico_saude`
*   `compromissos`