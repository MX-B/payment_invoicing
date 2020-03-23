moneyex Portal - Billing
=====================

This project does the billing part of the moneyex Portal.

#### Environment Configuration

You'll need these environment variables set in order to run the project. Some variables are **required** and *MUST* be set in order to run the server:

**Required Environment Variables:**

>- `moneyex_PORTAL_BILLING_EXTERNAL_URL` **(required)**: Public endpoint where the server is running (in order for external webhooks to call this server)
>- `moneyex_PORTAL_BILLING_JDBC_CONNECTION_STRING` **(required)**: JDBC Connection String **(note: the server expects ALL times are at UTC timezone both at the server AND database)**.
>   _Recommended Default for MySQL_: `jdbc:mysql://localhost:3306/moneyex_portal_billing?useLegacyDatetimeCode=false&useTimezone=true&serverTimezone=UTC`
>- `moneyex_PORTAL_BILLING_PAGARME_APIKEY` **(required)**: Pagar.me API Key.
>- `moneyex_PORTAL_BILLING_PAGARME_ENCKEY` **(required)**: Pagar.me Encryption Key.
>- `moneyex_CA_URL` **(required)**: CA Portal URL.
>- `moneyex_CA_OAUTH2_CLIENT_ID` **(required)**: CA Portal OAuth2 Client ID.
>- `moneyex_CA_OAUTH2_CLIENT_SECRET` **(required)**: CA Portal OAuth2 Client Secret.
>- `moneyex_CA_OAUTH2_ACCESS_TOKEN_URL` **(required)**: CA Portal OAuth2 Access Token URL.
>- `moneyex_CA_OAUTH2_SCOPE` **(required)**: CA Portal OAuth2 Scope.
>- `moneyex_RECIPIENT_ID` **(required)**: The moneyex RecipientID to receive payments.
>- `SENDGRID_API_KEY` **(required)**: The sendgrid API Key (sendgrid.com).
>- `SENDGRID_SENDER_EMAIL` **(required)**: The sendgrid Sender email.
>- `SENDGRID_SENDER_DEFAULT_RECEIVER` **(required)**: The sendgrid default email to receive some notifications.
>- `GRID_PORTAL_BILLING_GRAVITEE_URL` **(required)**: Gravitee API Url to generate Bill
>- `GRID_PORTAL_BILLING_GRAVITEE_USERNAME` **(required)**: Gravitee user to authenticate to generate Bill
>- `GRID_PORTAL_BILLING_GRAVITEE_PASSWORD` **(required)**: Gravitee password to authenticate to generate Bill

**Optional Environment Variables (and their defaults)**

>- `moneyex_PORTAL_BILLING_PORT`: TCP Port where the server will run. Default: `8088`
>- `moneyex_PORTAL_BILLING_ACTUATOR_ENDPOINT`: Base endpoint to serve _Spring Actuator_ information (only the `/health` module is active). Default: `/monitoring`
>- `moneyex_PORTAL_BILLING_LOG_LEVEL`: Log level. Default: `INFO`
>- `moneyex_PORTAL_BILLING_DB_USER`: MySQL Database User. Default: `<blank>`
>- `moneyex_PORTAL_BILLING_DB_PASSWORD`: MySQL Database Password. Default: `<blank>`
>- `moneyex_PORTAL_BILLING_DB_LOG_SQL`: Show which SQL queries are being executed? Default: `false`
>- `moneyex_PORTAL_BILLING_DB_USE_SSL`: Whether to use SSL connection or not. Detault: `true`
>- `moneyex_PORTAL_BILLING_MQ_HOST`: MQ Sever Host - By default we are using RabbitMQ but any server that implements AMQP can be used. Default: `localhost`
>- `moneyex_PORTAL_BILLING_MQ_PORT`: MQ Sever Port. Default: `5672`
>- `moneyex_PORTAL_BILLING_MQ_USER`: MQ Sever User. Default: `<blank>`
>- `moneyex_PORTAL_BILLING_MQ_PASSWORD`: MQ Sever Password. Default: `<blank>`
>- `moneyex_SUBSCRIPTIONS_URL`: moneyex Portal Subscriptions Service URL. Default: `localhost:8080`
>- `moneyex_SUBSCRIPTIONS_AUTH_TOKEN`: moneyex Portal Subscriptions Service Auth Token. Default: `<blank>`
>- `moneyex_GATEWAY_URL`: moneyex Portal Gateway URL. Default: `<blank>`
>- `moneyex_PAGING_DEFAULT_LIMIT`: Default page size. Default: `20`
>- `moneyex_PAGING_MAX_LIMIT`: Maximum page size. Default: `100`
>- `GRID_PORTAL_BILLING_CACHE_EVICT`: Interval in millis when a cache purge will occur. Default: `600000` (10 minutes)

#### Executing the Project

In order to execute this project, all you have to do is clone the project and then:

1. Run `mvn clean package`
2. Then run `java -jar target/moneyex-billing-service-java-0.1.jar`

>**Note**: All tables and data (sometimes known as "migrations") needed for the system will be created by [Flyway](https://flywaydb.org/) when it starts. If any changes are needed, they will be ran upon start.

#### Docker

To build the docker image locally, run `mvn clean package dockerfile:build` at the root of this project.

Near the end of the output, you'll a message like:

```
...
[INFO] Successfully tagged gcr.io/moneyex-moneyex/moneyex-billing-service-java:0.1.af8d9452
[INFO] 
[INFO] Detected build of image with id 8caf0ef698db
[INFO] Building jar: /home/rafael/git/moneyex-billing-service-java/target/moneyex-billing-service-java-0.1-docker-info.jar
[INFO] Successfully built gcr.io/moneyex-moneyex/moneyex-billing-service-java:0.1.af8d9452
...
```

That line contains the newly built image name (`gcr.io/moneyex-moneyex/moneyex-billing-service-java`) and its version (`0.1.short_commit_code`)

To run all that is needed **after configuring the required env vars above** is to execute `docker run gcr.io/moneyex-moneyex/moneyex-billing-service-java:0.1.af8d9452` (given the above example, where the version is `0.1.af8d9452`)

_Yes, its magic. Please, read the note on the previous item._