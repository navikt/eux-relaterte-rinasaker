### EUX Relaterte Rinasaker

Service for å håndtere sammenknytning av rinasaker. Benyttes i hovedsak av eux neessi 
for å knytte og navigere mellom relaterte saker.

## Brukte teknologier
* Kotlin
* Spring
* Maven

#### Avhengigheter

* JDK 21

#### Kjøring av tester lokalt

Kjøring av database-tester krever en kjørende PostgreSQL database med følgende variabler satt korrekt:

```
set -x DATABASE_HOST localhost
set -x DATABASE_USERNAME postgres
set -x DATABASE_DATABASE postgres
set -x DATABASE_PORT 5432
```
