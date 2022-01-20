## Micronaut 3.2.5 Documentation

- [User Guide](https://docs.micronaut.io/3.2.5/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.2.5/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.2.5/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

## How to Run

To run the server 
```bash
./gradlew run
```

To run all of the unit tests 
```bash
./gradlew test
```

## Info

Item has four fields - id, name, location, and quantity

Alongside basic CRUD functionality, also able to filter on specific field values with endpoint /items/list?args

Database uses SQLite with just RAM memory for demonstration purposes 

Reasoning behind certain Micronaut tags are shown in inline comments 