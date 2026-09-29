# HPCLan

Compilatore e interprete per **HPCLan**, semplice linguaggio imperativo per HPC
(progetto del corso *Complementi di Linguaggi di Programmazione*, Università di Bologna).

HPCLan supporta funzioni (anche ricorsive, solo top-level), variabili e costanti,
array di lunghezza costante, `if`, `while` e il comando parallelo `mapred`.
Poiché non disponiamo di un HPC, `mapred` è simulato su una macchina uniprocessor.

## Pipeline

```
Samples.hpc ─► Lexer/Parser (ANTLR) ─► AST ─► analisi semantica ─► type checking
            ─► codice intermedio (Samples.hpc.asm) ─► SVM lexer/parser ─► ExecuteVM
```

1. `HPCLanLexer` / `HPCLanParser` (generati da `HPCLan.g4`) leggono il sorgente.
2. `HPCLanVisitorImpl` costruisce l'AST.
3. `checkSemantics` verifica dichiarazioni e uso degli identificatori (con symbol table).
4. `typeCheck` controlla i tipi bottom-up.
5. `codeGeneration` produce il codice per la macchina virtuale in `<file>.asm`.
6. `SVM.g4` / `SVMVisitorImpl` caricano il codice assembly, che `ExecuteVM` esegue.

Il punto d'ingresso è `mainPackage.Test`, che legge `Samples.hpc` dalla radice del progetto.

## Struttura del progetto

```
HPCLan/
├── README.md
├── Samples.hpc                  # programma HPCLan di esempio
├── Samples.hpc.asm              # codice intermedio generato
├── lib/
│   └── antlr-4.13.1-complete.jar
├── .vscode/
│   └── launch.json              # configurazione di esecuzione per VS Code
└── src/
    ├── mainPackage/
    │   └── Test.java            # main: esegue l'intera pipeline
    ├── parser/
    │   ├── HPCLan.g4            # grammatica del linguaggio (parser + lexer)
    │   ├── HPCLanErrorListener.java  # gestione degli errori sintattici
    │   ├── SVM.g4               # grammatica del codice assembly della VM
    │   └── HPCLan*.java, SVM*.java   # file generati da ANTLR
    ├── ast/
    │   ├── Node.java            # interfaccia comune dei nodi AST
    │   ├── HPCLanVisitorImpl.java    # parse tree -> AST
    │   ├── SVMVisitorImpl.java       # assembly -> array di istruzioni
    │   ├── Type.java, IntType, BoolType, ArrowType, ErrorType
    │   ├── ProgNode, DecNode, ConstDecNode, ArrayDecNode, FunNode, ParNode
    │   ├── AsgNode, ArrayStmNode, IfStmNode, WhileStmNode, MapredStmNode
    │   ├── IfExpNode, CallNode, IdNode, ArrayNode, IntNode, BoolNode
    │   └── Plus/Minus/Mult/Div/UMinus/Not/And/Or/Equal/UnEqual/Gt/Geq/Lt/LeqNode
    ├── semanticanalysis/
    │   ├── SymbolTable.java     # symbol table a pila di scope
    │   ├── STentry.java         # entry della symbol table
    │   ├── SemanticError.java   # errore semantico
    │   └── VoidType.java
    └── evaluator/
        ├── ExecuteVM.java       # macchina virtuale (interprete)
        ├── AssemblyClass.java   # singola istruzione assembly
        └── HPCLanlib.java       # generatore di label e raccolta codice delle funzioni
```

## Come si usa (VS Code)

1. Installare l'*Extension Pack for Java* (il jar in `lib/` viene riconosciuto come libreria).
2. Scrivere il programma in `Samples.hpc`.
3. Eseguire la configurazione **Test** (`.vscode/launch.json`).

Se si modifica una grammatica, rigenerare i file ANTLR dalla cartella `src/parser`:

```
java -jar ../../lib/antlr-4.13.1-complete.jar -visitor -package parser HPCLan.g4
```

## Scelte progettuali

### Analisi semantica e symbol table

Ogni nodo dell'AST implementa `checkSemantics(SymbolTable, nesting)`, che restituisce la
lista di `SemanticError` (gli errori vengono accumulati, non ci si ferma al primo).
Controlli effettuati:

- dichiarazioni multiple nello stesso scope;
- uso di identificatori non dichiarati (variabili, array, funzioni);
- assegnamento a una costante;
- array usato senza indice, o indice su un identificatore che non è un array;
- dimensione di un array non costante o non positiva;
- costante inizializzata con un'espressione non costante;
- variabile di `mapred` già dichiarata.

La `SymbolTable` è costituita da: 
- una pila di `HashMap<String, STentry>` (uno per scope) 
- una pila parallela degli offset. 

Tra i metodi troviamo ad esempio:
- `lookup` cerca dallo scope più interno verso l'esterno, mentre
- `top_lookup` guarda solo lo scope corrente (è quello usato per rilevare le dichiarazioni multiple, quindi lo shadowing tra scope diversi è permesso).

Ogni `STentry` contiene: 
- tipo, 
- offset (per le variabili),
- label (per le funzioni), 
- dimensione (per gli array), 
- valore (per le costanti),
- livello di nesting.

### Costanti e array

- Le **costanti** non occupano spazio nello stack: il valore è salvato nella `STentry`
  e, dove la costante è usata, il codice generato carica direttamente il valore (`storei`).
  Per questo possono essere usate come dimensione di un array o come limite di `mapred`.
- Gli **array** hanno lunghezza costante verificata a tempo di compilazione. Sono allocati
  nello stack con un unico `subi SP <dim>`, e l'elemento `i` si trova a `base - i`.

### Valutazione delle espressioni costanti (`constValue`)

Il linguaggio richiede che alcuni valori siano noti a tempo di compilazione, come per esempio l'inizializzatore
di una costante tramite espressione (`int const n = 3 + 4;`) e la lunghezza di un array (`int v[n];`). Per
verificarlo abbiamo aggiunto all'interfaccia `Node` il metodo

```java
default Integer constValue(SymbolTable ST) { return null; }
```

che restituisce il valore dell'espressione se è costante, oppure `null` se non lo è
(comportamento di default). Lo ridefiniscono i nodi che possono avere un valore noto.


`constValue` viene principalmente chiamato in due punti dell'analisi semantica:

- `ConstDecNode`: se l'inizializzatore vale `null` viene segnalato l'errore *"must be
  initialized with a constant value"*; altrimenti il valore viene salvato nella `STentry`;
- `ArrayDecNode`: la dimensione deve essere costante e positiva, altrimenti si segnala errore.

Le costanti vengono quindi risolte a tempo di compilazione: nel codice generato un
identificatore costante diventa un caricamento immediato del valore (`storei A0 <valore>`), senza
alcun accesso allo stack e senza risalire la catena statica.

### Type checking

Dopo l'analisi semantica, `typeCheck()` risale l'AST (bottom-up). I tipi sono `IntType`,
`BoolType`, `ArrowType` (funzioni: tipi dei parametri → tipo di ritorno) ed `ErrorType`.
Si controllano tra l'altro numero e tipo dei parametri nelle chiamate, tipo di ritorno
delle funzioni, tipo di guardie e indici, compatibilità negli assegnamenti.

### Activation record

Ogni activation record ha questo formato:

```
control link (FP salvato) | access link | parametri | indirizzo di ritorno (RA) | dichiarazioni locali
```

- **Control link**: permette di ripristinare `FP` al ritorno.
- **Access link** (catena statica): permette di raggiungere le variabili degli scope
  esterni. Le funzioni sono solo top-level, ma servono comunque per accedere a variabili,
  costanti e variabili globali dal corpo di una funzione o da `mapred`.
  Il numero di salti lungo la catena è dato da: `nesting` corrente - `nesting` della dichiarazione (salvato nella symbol table).
- Una variabile con offset `k` si trova all'indirizzo `AL - k`. Gli offset partono da 1 in
  ogni scope.
- Il valore di ritorno di una funzione è lasciato nel registro `A0`.
- Il programma principale crea l'activation record globale (`FP` e `AL`) e poi alloca le
  dichiarazioni globali; le funzioni sono emesse in coda dopo `halt`
  (`HPCLanlib.putCode`), con label univoche (`function0`, `label3`, ...).

### Implementazione di `mapred`

`mapred (i upto n: RES[j] = e)` viene simulato in modo sequenziale, come previsto dalla
specifica (l'ordine di esecuzione è arbitrario). Abbiamo scelto un ordine definito da due cicli `while`:

1. `i` da `n/2` fino a `0`;
2. `i` da `n/2 + 1` fino a `n - 1`.

Ogni indice `0..n-1` viene eseguito esattamente una volta. Per la variabile `i` viene
creato un activation record dedicato (control link, access link e lo slot di `i`), a
nesting `+1`, rimosso a fine comando. L'indice dell'array di destinazione è un'espressione
qualunque, quindi è ammesso anche `RES[5] = e` (risultato impredicibile, come da
specifica).

### Macchina virtuale (SVM)

`ExecuteVM` interpreta il codice assembly con memoria di 1000 celle e registri
`IP, SP, FP, AL, RA, A0, T1, T2`. Il codice generato viene scritto in `<file>.asm`,
riletto con il lexer/parser ANTLR di `SVM.g4` e trasformato in un array di
`AssemblyClass`.

### Gestione degli errori sintattici

`HPCLanErrorListener` estende `BaseErrorListener` e sostituisce il listener di default
del parser: stampa su `stderr` riga e colonna dell'errore, la riga di codice incriminata e
una sottolineatura `^^^` sotto il token errato.