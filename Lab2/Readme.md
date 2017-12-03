# Грамматика:

* `S -> V WORD ( VARGROUPS ) WORD`
* `V -> function | procedure`
* `VARGROUPS -> VARLIST: WORD VARGRROUPS`
* `VARGROUPS -> )`
* `VARGROUPS -> ; VARGROUPS`
* `VARLIST -> WORD, VARLIST`
* `VARLIST -> WORD`
* `WORD -> [A-Za-z]WORD`

Нетерминал    | Значение
------------- | -------------
S  | Описание заголовка функции в Паскале
V | ключевое слово function или procedure
VARGROUPS | Разбиение переменных на группы по типам
VARLIST | Список переменных, разделенные запятыми
WORD | Переменная или тип


## Устранение правового ветвления:

* `S->V VARGROUPS`
* `V->var`
* `VARGROUPS -> VARLIST: WORD VARGRROUPS`
* `VARGROUPS -> )`
* `VARGROUPS -> ; VARGROUPS`
* `VARLIST -> WORDVARLIST'`
* `VARLIST' -> , WORDVARLIST'`
* `VARLIST' -> eps`
* `WORD -> [A-Za-z]WORD`

Нетерминал    | Значение
------------- | -------------
S  | Описание заголовка функции в Паскале
V | ключевое слово function или procedure
VARGROUPS | Разбиение переменных на группы по типам
VARLIST | Список переменных, разделенные запятыми 
VARLIST' | Продолжение или окончание перечисления переменных
WORD | Переменная или тип


## Множества FIRST и FOLLOW для нетерминалов. 

`c` - символ из [A-Za-z]. 

Нетерминал | FIRST    | FOLLOW
-----------|----------|-------
S          | `p`, `f` |`$`
V          | `p`, `f` |` `
VARGROUPS  | `c`,`)`  |`$`
VARLIST    | `c`      |`:`
VARLIST'   | `,`, `;` |`:`
WORD       | `c`      |`;`,`,`
