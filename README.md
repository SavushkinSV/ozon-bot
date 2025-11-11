Для сборки образа необходимо воспользоваться командой

```bash
cd docker/
docker build -t oil-bot:1.0 .
```

Для запуска необходимых сервисов:

```bash
docker-compose up -d
```

Для запуска контейнера с телеграм ботом:

```bash
docker run --name oil-bot --rm -d -p 8080:8080 --network docker_default --env-file .env oil-bot:1.0
```