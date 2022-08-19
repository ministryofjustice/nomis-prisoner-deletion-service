# Running locally with Nomis T3

In order to run locally against NOMIS T3, you require a tunnel using ssh to connect you to `CNOMT3` - This requires a vpn.

Once connected, you will need to run the required dependencies

```bash
docker compose up --scale nomis-prisoner-deletion-service=0 -d
```

When the dependencies are ready, the application can be run locally in the `dev-nomis-t3` profile

```bash
./gradlew bootRun --args='--spring.profiles.active=dev-nomis-t3'
```

or run in IntelliJ with the following property

`SPRING_PROFILES_ACTIVE=dev-nomis-t3`