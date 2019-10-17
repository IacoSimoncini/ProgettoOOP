# Progetto Programmazione ad oggetti - Iacopo Simoncini, Mohammad Wafa

Il progetto si occupa di gestire un dataset fornito tramite un link contenente diversi URL dal quale scaricare il file.
Il link in questione si presenta come una stringa JSON dentro la quale il programma seleziona l'URL corretto per scaricare
il file tsv che contiene il dataset. A questo punto il programma gestisce il dataset attraverso la classe modellante 
costruendo una lista di oggetti della classe modellante che rappresentano il dataset. Tramite il framework Spring è stato
possibile creare dei metodi per fare operazioni sul dataset attraverso dei metodi GET gestiti da un'opportuna 
classe di controllo. A questi metodi possono essere applicati dei filtri specificandoli direttamente nella richiesta GET.

Nel nostro caso i campi del nostro file tsv sono: c_resid, unit, nace_r2, geo e time. A questi campi susseguono una serie
di valori relativi ad ogni anno, partendo dal 2007 fino al 2018.

# Struttura del progetto
Il progetto è strutturato attraverso dei package.

- `com.momoiaco.progetto.controllo`:
  - `NottiNazioneControllo`: classe che gestisce le richieste del server.

- `com.momoiaco.progetto.modello`:
  - `NottiNazione`: classe che modella il dataset.
  
- `com.momoiaco.progetto.servizi`:
  - `Download`: classe che gestisce il download del file.tsv e che si occupa di gestire alcuni metodi get che poi verranno
  richiamati dal controllo.
  - `Filtri`: classe che contiene i filtri e il metodo che applica i filtri.
  - `Statistics`: classe che contiene i metodi relativi alle statistiche sul dataset.
  
- `com.momoiaco.progetto`:
  - `ProgettoApplication`: classe principale contenente il `main()` che, grazie ad `@Autowired`, (che si trova all'interno 
  della classe controllo) richiama il costruttore della classe `Download`.

# Funzionamento

All'avvio il programma verifica se il dataset è già stato scaricato, in tal caso carica il dataset da locale. Se così non fosse 
avvia il costruttore della classe `Download` che attua il download del dataset tramite l'URL    . Dopo aver effettuato il 
dowload effettua il `parsing` del dataset, ovvero una procedura che carica ogni record all'interno di una lista sotto forma di
oggetti. Questo processo legge ogni riga, sostituisce ad ogni "," il carattere "\t" e divide la linea ogni volta che trova
il carattere tab. All'interno del dataset abbiamo notato che vi erano dei caratteri all'interno dei valori double cosi abbiamo 
deciso di sostituire anche quelli con degli spazi vuoti (""). Ogni suddivisione permette di caricare le stringhe nei campi
della classe modellante senza imbattersi in errori. L'oggetto così creato viene caricato all'interno di una lista contenente 
gli oggetti dello stesso tipo della classe modellante. A questo punto si effettua il metodo `Metadata` che genera i metadati
tramite una mappa che ha come campi `Alias` (chiave dell'alias), `SourceField` (Nome del campo) e `Type` (tipo di dato).
Ogni mappa viene inserita all'interno di una lista di mappe che verranno poi restituite su richiesta dell'utente. 
A questo punto vene avviato un server web locale sulla porta 8080 tramite l'utilizzo del framework Spring. Tramite questo 
framework sarà possibile effettuare le richieste GET e POST:

`localhost:8080`

  RICHIESTE GET

  - `/getRecord`: restituisce l'intero dataset.
  
  - `/getTime`: restituisce il vettore degli anni.
  
  - `/getMetadati`: restituisce la lista dei metadati.
  
  - `/getRecord/{i}`: restituisce un elemento all'indice i della lista dei record.
  
  - `/getStatistiche`: restituisce tutte le statistiche relative ad un campo inserito dall'utente.
  
  RICHIESTE POST
  
  - `/getFilteredStatistiche`: restituisce le  statistiche filtrate di un campo inserito dall'utente tramite il body.
  
  - `/getFilteredRecord`: restuisce il record filtrato passando tramite l'utente il body.
  
# Struttura del filtro

Il filtro deve essere inserito nel body durante la richiesta POST con la seguente sintassi: 

`{ "nomedelcampo" : { "operatore" : "riferimento"}}`

Gli operatori da inserire sono:

 - `$not`: operatore logico che indica la disuguaglianza.
 
 - `$gt`: operatore che indica il maggiore.
 
 - `$gte`: operatore che indica il maggiore e uguale.
 
 - `$lt`: operatore che indica il minore.
 
 - `$lte`: operatore che indica il minore e uguale.
 
 - `$in`: operatore che verifica se quell'elemento appartiene a quell'insieme.
 
 - `$nin`: operatore di non appartenenza.
 
 - `$bt`: operatore che verifica se un elemento è compreso tra due elementi.
 
# TEST DI FUNZIONAMENTO

Possibile avviare un test di funzionamento effettuando le seguenti richieste :

- `http://localhost:8080/getRecord`

- `http://localhost:8080/getRecord/10`

- `http://localhost:8080/getTime`

- `http://localhost:8080/MetaDati`

- `localhost:8080/getStatistiche?Field=2008`

- `http://localhost:8080/getFilteredRecord`    body: { "geo": {"$in": ["AT11","AT12"] }}

Test di errore

- `http://localhost:8080/getFilteredStatistiche`    body: { "geo": {"$lt" : ["AT11","AT12"]}}   (Bad Request)

# UML

All'interno della repository, nella cartella UML, si possono trovare i diagrammi UML delle classi, 
dei casi d'uso e delle sequenze.


 
