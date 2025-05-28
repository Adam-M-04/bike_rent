# Bike Rent
### Autor: Adam Mąka 152693

Bike Rent to aplikacja webowa służąca do zarządzania wypożyczalnią rowerów, napisana w języku Java z wykorzystaniem frameworka Spring Boot. Projekt umożliwia rejestrację i logowanie użytkowników, zarządzanie markami rowerów oraz obsługę procesu rezerwacji. System rozróżnia role użytkowników (klient, administrator), co pozwala na kontrolę dostępu do poszczególnych funkcji aplikacji dzięki integracji ze Spring Security.

Aplikacja udostępnia REST API, które pozwala na wykonywanie operacji CRUD na markach rowerów, zarządzanie użytkownikami oraz rezerwacjami. W środowisku testowym wykorzystywana jest baza H2, natomiast w środowisku produkcyjnym PostgreSQL. Testy jednostkowe i integracyjne realizowane są przy użyciu JUnit oraz MockMvc.

Aplikacja została przygotowana do uruchomienia w kontenerze Docker. W projekcie znajduje się plik `Dockerfile` oraz konfiguracja `docker-compose.yml`, pozwalająca na szybkie uruchomienie aplikacji wraz z bazą danych PostgreSQL.

## Pokrycie testów
![pokrycie_testow.png](docs/pokrycie_testow.png)

## Diagram ERD bazy danych
![baza.png](docs/baza.png)

## Kontenery docker
![docker.png](docs/docker.png)

## Dokumentacja API
![swagger1.png](docs/swagger1.png)
![swagger2.png](docs/swagger2.png)
![swagger3.png](docs/swagger3.png)
![swagger4.png](docs/swagger4.png)
