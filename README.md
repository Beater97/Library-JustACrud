
# Just a crud

Just a CRUD è una libreria Java progettata per semplificare e accelerare lo sviluppo di operazioni CRUD (Create, Read, Update, Delete) su database relazionali. Si integra strettamente con Spring Framework, sfruttando la potente iniezione di dipendenze di Spring per gestire le connessioni al database e automatizzare le operazioni di database comuni.

La libreria fornisce una serie di classi utili, tra cui DatabaseRequest e QueryBuilder, che consentono agli sviluppatori di interagire con il database in modo più intuitivo e meno verboso rispetto all'uso diretto di JDBC o JPA per operazioni specifiche. Questo si traduce in un codice più pulito, una maggiore leggibilità e, in definitiva, un incremento della produttività dello sviluppatore.

La classe DatabaseRequest, ad esempio, astrae la complessità di creare e gestire connessioni JDBC, preparare statement e eseguire query o aggiornamenti sul database. Fornisce metodi come executeUpdate per eseguire operazioni di scrittura (INSERT, UPDATE, DELETE) e customQuery per eseguire query di selezione personalizzate, restituendo i risultati in una lista di oggetti del tipo specificato. Questo approccio riduce notevolmente il boilerplate code associato all'esecuzione di operazioni di database in Java, gestendo automaticamente aspetti critici come la gestione delle eccezioni SQL e la conversione dei risultati delle query in oggetti Java.

Inoltre, la classe QueryBuilder offre un'interfaccia fluida per costruire dinamicamente stringhe di query SQL. Supporta la concatenazione di varie clausole SQL (come SELECT, FROM, JOIN, WHERE, GROUP BY, HAVING e ORDER BY) in modo programmatico, permettendo di specificare condizioni, ordinamenti e raggruppamenti in maniera flessibile e sicura. La classe WhereClauseBuilder, in particolare, si concentra sulla costruzione della clausola WHERE delle query, supportando operatori logici (AND, OR), confronti (LIKE, IN) e la gestione delle parentesi per controllare la precedenza delle operazioni.

La possibilità di costruire query SQL dinamicamente rende Just a CRUD particolarmente adatta per applicazioni che necessitano di una grande flessibilità nelle operazioni di database, come applicazioni enterprise, dashboard dinamiche o sistemi di reportistica.

Utilizzando Just a CRUD, gli sviluppatori possono sfruttare la familiarità e le funzionalità avanzate di Spring, come la gestione delle transazioni e l'iniezione di dipendenze, mentre mantengono il controllo diretto sulle query SQL eseguite, ottimizzando le prestazioni e personalizzando il comportamento secondo le necessità specifiche dell'applicazione.

In conclusione, Just a CRUD rappresenta un utile strumento per lo sviluppo rapido di applicazioni Java che interagiscono con database relazionali, combinando la potenza e la flessibilità del SQL puro con la semplicità e la robustezza del framework Spring.



## Autore

- [@Beater97](https://github.com/Beater97)


## Documentation





[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
