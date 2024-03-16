
## Autore

- [@Beater97](https://github.com/Beater97)


# Just a crud

Just a CRUD è una libreria Java progettata per semplificare e accelerare lo sviluppo di operazioni CRUD (Create, Read, Update, Delete) su database relazionali. Si integra strettamente con Spring Framework, sfruttando la potente iniezione di dipendenze di Spring per gestire le connessioni al database e automatizzare le operazioni di database comuni.

La libreria fornisce una serie di classi utili, tra cui DatabaseRequest e QueryBuilder, che consentono agli sviluppatori di interagire con il database in modo più intuitivo e meno verboso rispetto all'uso diretto di JDBC o JPA per operazioni specifiche. Questo si traduce in un codice più pulito, una maggiore leggibilità e, in definitiva, un incremento della produttività dello sviluppatore.

La classe DatabaseRequest, ad esempio, astrae la complessità di creare e gestire connessioni JDBC, preparare statement e eseguire query o aggiornamenti sul database. Fornisce metodi come executeUpdate per eseguire operazioni di scrittura (INSERT, UPDATE, DELETE) e customQuery per eseguire query di selezione personalizzate, restituendo i risultati in una lista di oggetti del tipo specificato. Questo approccio riduce notevolmente il boilerplate code associato all'esecuzione di operazioni di database in Java, gestendo automaticamente aspetti critici come la gestione delle eccezioni SQL e la conversione dei risultati delle query in oggetti Java.

Inoltre, la classe QueryBuilder offre un'interfaccia fluida per costruire dinamicamente stringhe di query SQL. Supporta la concatenazione di varie clausole SQL (come SELECT, FROM, JOIN, WHERE, GROUP BY, HAVING e ORDER BY) in modo programmatico, permettendo di specificare condizioni, ordinamenti e raggruppamenti in maniera flessibile e sicura. La classe WhereClauseBuilder, in particolare, si concentra sulla costruzione della clausola WHERE delle query, supportando operatori logici (AND, OR), confronti (LIKE, IN) e la gestione delle parentesi per controllare la precedenza delle operazioni.

La possibilità di costruire query SQL dinamicamente rende Just a CRUD particolarmente adatta per applicazioni che necessitano di una grande flessibilità nelle operazioni di database, come applicazioni enterprise, dashboard dinamiche o sistemi di reportistica.

Utilizzando Just a CRUD, gli sviluppatori possono sfruttare la familiarità e le funzionalità avanzate di Spring, come la gestione delle transazioni e l'iniezione di dipendenze, mentre mantengono il controllo diretto sulle query SQL eseguite, ottimizzando le prestazioni e personalizzando il comportamento secondo le necessità specifiche dell'applicazione.

In conclusione, Just a CRUD rappresenta un utile strumento per lo sviluppo rapido di applicazioni Java che interagiscono con database relazionali, combinando la potenza e la flessibilità del SQL puro con la semplicità e la robustezza del framework Spring.



## Documentation
esempio semplice : 

```java
package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import services.DatabaseRequest;
import services.QueryBuilder;


@RestController
public class Controller {
    
    @Autowired
    DatabaseRequest databaseRequest;

    @GetMapping("test")
    @ResponseBody
    public List<Persona> test(@RequestParam String [] id){

        QueryBuilder queryBuilder = new QueryBuilder();

        queryBuilder.select("*").from("tabella_dummy");
        queryBuilder.orderBy("id desc");
        if(id.length <= 1){
            queryBuilder.where().column("id").equalsTo(id[0]);
        }else{
            queryBuilder.where().column("id").in(id);
        }
        queryBuilder.stamp(true);
        
        List<Persona> persone = databaseRequest.customQuery(queryBuilder.build(),1, Persona.class);
        return persone;
    }

}

```

esempio where dinamica :

```java
package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import services.DatabaseRequest;
import services.QueryBuilder;
import services.WhereClauseBuilder;


@RestController
public class Controller {
    
    @Autowired
    DatabaseRequest databaseRequest;

    @GetMapping("test")
    @ResponseBody
    public List<Persona> test(@RequestParam(name = "nome",required = false) String nome, @RequestParam(name ="congnome",required = false) String cognome, @RequestParam(name ="id",required = false) String id)
    {
        QueryBuilder queryBuilder = new QueryBuilder();

        queryBuilder.select("*").from("tabella_dummy");
        queryBuilder.orderBy("id desc");
        queryBuilder.stamp(true); //stampa la query

        WhereClauseBuilder whereClause = new WhereClauseBuilder();

        if(id!= null && !id.isBlank() && !id.isEmpty() && isInteger(id)){
            whereClause.column("id").equalsTo(id);
        }
        if(nome != null && !nome.isEmpty() && !nome.isBlank()){
            whereClause.column("nome").equalsTo(nome);
        }
        if(cognome != null && !cognome.isEmpty() && !cognome.isBlank()){
            whereClause.column("cognome").equalsTo(cognome);
        }

        if(whereClause.equals(new WhereClauseBuilder())){
            return null;
        }
        
        queryBuilder.where(whereClause);

        //query in formato stringa, numero di righe richieste, classe
        List<Persona> persone = databaseRequest.customQuery(queryBuilder.build(),40, Persona.class);

        return persone;
    }

    public boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    

}

```



[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
