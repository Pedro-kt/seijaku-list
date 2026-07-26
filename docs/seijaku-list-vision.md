# Seijaku List — Documento de Visión y Reorganización de Producto

> Documento de contexto para evaluación externa.
> Resume el estado actual, la identidad encontrada, las decisiones de producto tomadas y las preguntas abiertas de la reorganización de Seijaku List.
> Fecha del documento: julio 2026.

---

## 0. Qué es este documento y cómo usarlo

Seijaku List es una app Android (Kotlin + Jetpack Compose) de registro de anime y manga, en desarrollo desde mayo de 2025 por un solo desarrollador. Empezó como proyecto de aprendizaje y de uso personal, y evolucionó hacia un producto con intención de publicarse en Play Store.

Este documento captura el resultado de un proceso de repensado profundo del producto: **pasar de "un tracker más" a un producto con identidad propia.** Está pensado para que otras IAs que trabajen en evaluación, diseño de UI, arquitectura o estrategia tengan el contexto completo del *porqué* de Seijaku, no solo del *qué*.

**Regla de oro para evaluar cualquier propuesta sobre Seijaku:** toda idea, feature o pantalla debe pasar por un único filtro —
> *Un tracker guarda lo que hiciste. El refugio guarda lo que significó, y te deja seguir depositando con el tiempo.*
>
> Si una idea solo registra un dato, es tracker. Si acumula la relación del usuario con la obra, es refugio.

---

## 1. La identidad: qué ES Seijaku

**Seijaku no es un tracker de anime. Es un refugio.**

Definición central, en las palabras del propio fundador:

> *"Seijaku es el lugar donde tenés cerca lo que amás. Tu refugio."*

El tracking (búsqueda, estados, puntuación, fechas, etc.) es el **cuerpo**: el piso conocido, funcional, sin fricción de aprendizaje, y la razón funcional para volver a abrir la app seguido. Pero el **corazón** es el refugio: el lugar íntimo donde el usuario tiene cerca las obras que ama y puede depositar lo que le hicieron sentir.

- El **tracker es la excusa** para volver.
- El **refugio es lo que hace que volver se sienta distinto** a cualquier otra app.

AniHyou (el principal competidor) te recibe con datos del mundo. Seijaku te recibe con lo tuyo.

### El nombre ya lo decía

**Seijaku (静寂)** significa *quietud, calma, silencio*. El fundador eligió ese nombre hace más de un año sin poder explicar del todo por qué. Resultó ser la definición más exacta del producto: un refugio íntimo y calmo. La app no tuvo que cambiar de nombre — tuvo que *alcanzar* su nombre.

### El origen real (por qué esto no es marketing)

Seijaku nació de una necesidad emocional concreta, no de una oportunidad de mercado. El fundador atravesaba un momento personal difícil y **K-On!** (un slice of life) fue su punto de apoyo. En las primeras versiones de la app, abría Seijaku **solo para tener a K-On! cerca**: buscaba la serie en el buscador una y otra vez, entraba a la pantalla de detalle del personaje (Mio Akiyama) solo para verla. No para registrar nada —ya la había visto— sino para *estar cerca*.

Ese comportamiento —volver a una app sin ninguna razón funcional, solo por afecto— es exactamente la retención que las apps de journaling/santuario intentan fabricar y no pueden. Seijaku la tuvo orgánica en su versión más cruda. **Ese es el activo diferencial y el origen del alma del producto.**

---

## 2. El posicionamiento: refugio hacia adentro

Muchos otakus no encajan del todo en su entorno social y buscan lugares donde ser ellos mismos. Casi todos esos lugares son **hacia afuera** y con audiencia:

- Instagram (subir memes) → performance ante otros.
- Reddit (expresarse) → ante una comunidad.
- Subir dibujos / videos → para recibir validación.
- Comunidades de debate → mirada ajena constante.

**Seijaku ocupa el espacio que ninguno de esos da: un refugio HACIA ADENTRO. Sin audiencia. Sin likes. Sin la mirada de nadie.** El lugar donde el usuario es él mismo sin tener que mostrárselo a nadie. Es coherente con el nombre (quietud, intimidad) y es un posicionamiento que ninguna red social puede copiar, porque toda red vive de la mirada y Seijaku vive de su ausencia.

### Retención por amor, no por dependencia

Decisión ética y estratégica explícita del fundador:

> Seijaku NO busca generar dependencia (mecánicas de enganche, rachas, notificaciones manipulativas). Busca ser **extrañado**. La gente vuelve porque se siente cómoda y segura, no porque la engancharon.

Esto no es solo un valor: es la retención más fuerte y sana que existe, y el fundador ya la vivió en carne propia (volvía a ver a Mio sin que nada lo empujara). Construir para "enganchar" traicionaría el alma del producto. La brújula ante cualquier tensión de este tipo es siempre: **el refugio primero.**

---

## 3. El concepto central: LA HUELLA

Este es el mecanismo que hace que el refugio exista. Es el resultado más importante del proceso de diseño.

### Definición

**La huella es una marca que el usuario deja pegada a una obra, para registrar lo que le hizo sentir o qué momento lo marcó.** No es una opinión pública, no es una reseña, no es un rating. Es algo íntimo que le dejás *a la obra*.

**Principio clave: la huella no pertenece al tracking. Pertenece a la obra.**

Esto resuelve toda la arquitectura conceptual: la huella vive en la obra (K-On!), no en "el episodio que marcaste". La obra es el lugar donde se acumulan las huellas.

### La huella unifica dos modelos sin que el usuario elija

Durante el diseño aparecieron dos formas de registrar, y el fundador confirmó que **las dos son válidas y conviven** (distintas obras se viven distinto):

- **Modelo emoción (estado):** *"este anime me destruyó internamente"*, *"esto me dio paz"*. Registra el estado interno. Ejemplo típico: K-On! (una sensación general que impregna todo).
- **Modelo momento (instante):** *"el opening del episodio 6 fue una locura"*, *"la escena del cap 19 me voló la cabeza"*. Registra un instante puntual, anclado a un episodio/escena. Ejemplo típico: un thriller con un giro fuerte.
- **Y una tercera capa, la más profunda:** *"este anime me remarcó algo que me viene pasando"* → la obra tocando la vida del usuario.

**La solución NO es dos features separadas.** Es una sola: la huella. La huella puede *opcionalmente*:
- anclarse a un punto (episodio/escena) → se comporta como *momento*.
- llevar una emoción → se comporta como *estado*.
- llevar texto libre → como el actual apartado de "opinión".
- llevar varias de esas cosas, o una sola.

**El usuario nunca elige "modelo" ni "formato".** Solo deja su huella, y la huella toma la forma que necesita según lo que sintió esa vez. Una sola puerta, que se abre tanto o tan poco como el usuario quiera. Esto preserva la regla de **fricción mínima**: el gesto base siempre es el mismo y liviano; lo que se le cuelga encima es opcional.

> Nota: el fundador expresó que **le cuesta redactar y expresar sentimientos en palabras** (y asume que buena parte de su público también). Por eso el **gesto rápido** (tocar una emoción, marcar un momento como especial) debe ser el camino principal, y la escritura una opción, nunca una obligación. Un campo de texto vacío que pregunta "¿cómo te sentiste?" mata el registro igual que un diario en blanco.

### Dos caminos hacia la misma huella

La huella se puede dejar desde dos contextos, pero **es siempre lo mismo viviendo en el mismo lugar (la obra)**:

1. **Camino nostalgia (desacoplado del tracking):** el usuario entra a una obra cualquier día, sin marcar ni avanzar nada, porque lo agarró la nostalgia, y deja una huella. (Esto replica el comportamiento original del fundador: entrar solo para tener K-On! cerca.)
2. **Camino tracking (acoplado al momento):** el usuario marca un episodio como visto y, si lo tiene fresco y algo le pegó, la puerta a dejar una huella está *ahí mismo*, disponible, opcional.

**El tracking no CONTIENE la huella. El tracking es UNA de las puertas que llevan a ella. La otra puerta es simplemente entrar a la obra.** Por eso están *desacoplados pero no desligados*: se tocan en la obra. El usuario nunca piensa "¿esto es tracking o huella?" — solo deja huellas en las obras que le importan, a veces mientras avanza, a veces porque las extrañó.

---

## 4. Los tres pilares son UN solo corazón

Lo que al principio parecían tres features separadas resultaron ser **el mismo objeto (la huella / la obra) visto en tres momentos:**

1. **La captura** — dejar una huella (el gesto, cuando lo sentís).
2. **El santuario** — volver a la obra y encontrar todas tus huellas juntas. *El santuario NO es una feature aparte: es literalmente la obra llena de las huellas que le fuiste dejando.* La obra se vuelve refugio porque acumuló tu historia con ella.
3. **El final** — la huella especial del momento en que terminás una obra amada.

Un solo corazón unido, no tres features. Esto era un requisito explícito del fundador: que el refugio sea **un corazón único**, no módulos pegados.

### El final: la firma emocional de Seijaku

El sentimiento más distintivo y defendible del producto: **el vacío de terminar una obra que amaste** (conocido en el fandom como *post-anime depression* / síndrome del final). Esa sensación agridulce de orfandad cuando se acaba algo que te sostuvo.

- **Un tracker común trata el final como un logro:** marcás "completado" y a la siguiente.
- **Seijaku trata el final como un momento que merece ser reconocido.** Donde el tracker festeja que lo cerraste, Seijaku reconoce que cerrarlo a veces duele.

Ninguna otra app entiende ese momento, porque nacieron para gestionar listas, no para acompañar lo que una historia deja. Seijaku nació del lado correcto: su origen *es* literalmente no querer soltar a K-On!.

**Cuidado de diseño (importante):** el vacío debe tratarse como algo **agridulce y cálido, no depresivo**. La diferencia entre reconocer el vacío y regodearse en él es la diferencia entre un refugio y un pozo. Seijaku siempre debe dejar al usuario un poco mejor de como entró. El antídoto del vacío es el propio refugio: cuando algo amado termina, Seijaku te ofrece el lugar para seguir teniéndolo cerca igual. Reconoce la herida y en el mismo gesto ofrece el bálsamo.

---

## 5. Alcance: qué va en la V1 y qué después

### Decisión estratégica sobre el lanzamiento

- **NO hay fecha de salida.** El único reloj es el fundador (sin inversores, sin runway, sin presión externa). Esto es un lujo real.
- Las descargas **no son el objetivo**. El objetivo es **hacer las cosas bien** y que Seijaku pueda ser, de verdad, el refugio de al menos una persona desde el día uno.
- **El MVP "mínimo para validar" NO aplica a este proyecto.** La v1 debe salir con el **alma completa**, no con el alma insinuada. Lanzar un refugio a medias es peor que no lanzar.

### La distinción crítica (evita la trampa de la "app eterna")

Hay que separar dos frases que parecen iguales y no lo son:

- ✅ **VERDADERO:** "La v1 debe tener el alma COMPLETA." El refugio tiene que estar entero *como concepto* — alguien debe poder instalar la v1 y refugiarse de verdad, no como promesa.
- ❌ **TRAMPA:** "La v1 debe tener todas las funcionalidades que Seijaku tendrá algún día." Eso significa que la v1 no sale jamás, porque "todo lo que será siempre" es un horizonte que se corre con cada idea nueva. Esta es la receta de la app eterna que nunca ve la luz.

**El alma completa NO requiere todas las features. Requiere las suficientes para que el alma respire.** El refugio, el santuario y el final son el *alma*. El resto son *profundizaciones* del alma que la hacen más honda pero no la hacen *nacer*.

### V1 — El corazón mínimo viable (alma completa)

El cuerpo ya está construido y **no se toca ni se re-suma**:
- Búsqueda de anime, manga y personajes, con filtros y detalles (vía API de AniList).
- Tracking: estados, prioridades, puntuación, fechas.
- Perfil con estadísticas y favoritos.
- Compartir imagen a redes.
- Themes propios.

Lo que la v1 debe agregar (el corazón):
1. **La huella** — poder dejar una huella en una obra (gesto rápido, emoción y/o momento y/o texto, todo opcional), desde los dos caminos (nostalgia y tracking).
2. **El santuario** — poder destacar una obra como refugio y volver a ella para encontrar sus huellas acumuladas, mostrada de forma hermosa y generosa (no como una fila de datos).
3. **El final tratado distinto** — al menos una versión cuidada del momento de completar una obra amada.

Con esas tres, un usuario **ya se refugia de verdad**. Ya es algo que AniHyou no es.

### V2 y más allá — Profundizaciones (NO en v1)

Explícitamente fuera de la v1 (buenas ideas, todas anotadas, ninguna descartada):
- **Sonido / OST / openings-endings** como parte del refugio (el fundador destaca que la música de K-On! le genera algo único). Additivo, no funda el alma.
- **Datos / estadísticas emocionales** orientados a la *relación* del usuario con lo que ama (no a su consumo). Requieren que el usuario ya tenga huellas cargadas.
- **IA** para ayudar al usuario a *entenderse a través de lo que ama* (leer sus huellas y devolverle algo sobre sí mismo). Cara, y depende de material emocional previo.
- **Coleccionista físico** (trackear tomos/figuras de la colección física). Fue evaluado como posible *centro* del producto y descartado como alma —porque no es lo que el fundador abriría a diario— pero queda como posible **capa futura** subordinada al refugio (ej: "cosas que querés tener / lugares que soñás visitar", como el sueño del fundador de visitar la escuela de Toyosato).

> **Razón de fondo para diferir datos e IA:** en una app recién instalada están vacíos, no tienen huellas que masticar. El alma ya está viva sin ellos; ellos la profundizan después.

---

## 6. Análisis competitivo (contexto de mercado)

Todos los competidores relevantes son **clientes de API**: frontends sobre los mismos datos de AniList o MyAnimeList. Compiten entre ellos en lo mismo (pulido, velocidad, cantidad de filtros, widgets, stats). **Ninguno tiene una posición emocional propia; todos usan el mismo mensaje: "track & discover anime & manga".** Ahí está el hueco de Seijaku: no en features, sino en *significado*.

- **AniHyou (axiel7)** — cliente de AniList. Es el rival de verdad, no un "segundón". Open source, gratis, Material 3, sin recolección de datos, con versiones para Apple Watch / Wear OS, widgets, múltiples idiomas, comunidad fiel en Discord. UI/UX muy pulida (elogiada por usuarios). Ya es rápido, ordenado y "calmo" sin proponérselo. **Conclusión: no se le gana con "más features" ni con "un poco más limpio". Su identidad es *ser completo*, y por eso NO puede ser un refugio íntimo sin desnaturalizarse. Cada feature que suma, le regala terreno emocional a Seijaku.**
- **MoeList (axiel7)** — el mismo dev, versión para MyAnimeList. +100.000 (hasta ~160.000) descargas. Maduro, Material 3, Kotlin, Compose. Confirma que el nicho de "cliente pulido" ya está ocupado y muy bien atendido.
- **AniHive (SoulApps Studio)** — más comercial y "comunidad", con ads. Se siente menos cuidado. No compite en lo emocional.
- **Anichive (thekeeperofpie)** — cliente de AniList con una función rara y reveladora: base de datos para trackear **art prints, CDs y merch de convenciones** (evitar comprar duplicados). **Valida que existe demanda del coleccionista físico**, pero la trata como feature experimental detrás de un paywall con ads, en una app por lo demás genérica. Nadie tomó ese nicho como el corazón de un producto.

**Lección competitiva central:** no se le gana a un líder establecido siendo una versión levemente mejor de lo que él ya hace bien. Seijaku no compite en la cancha del "cliente de API"; crea una categoría nueva (el refugio) donde el líder no puede seguirlo sin dejar de ser lo que es.

---

## 7. Proceso: hipótesis evaluadas y descartadas

Parte del valor de este documento es mostrar qué caminos se descartaron y *por qué*, para no volver a proponerlos sin un motivo nuevo:

| Hipótesis | Estado | Por qué |
|---|---|---|
| **Merge** (tracking digital + colección física como dos centros) | Descartada | Dos *centros* abruman y diluyen el nombre. Una app puede tener muchos elementos pero **un solo alma**. |
| **Coleccionista físico como alma** | Descartada como centro (queda como capa v2+) | Hueco real y validado por Anichive, pero NO es lo que el fundador abriría a diario. Construir algo que a uno no le tira el cuerpo a usar = receta de abandono en proyectos solo-dev. |
| **Tracker "calmo/minimalista"** | Descartada | "Calmo" no es un hueco vacío: AniHyou ya es rápido, ordenado y limpio sin proponérselo. El propio fundador admitió que, siendo solo "un poco más calmo", él mismo seguiría usando AniHyou. |
| **Diario emocional por texto** | Reformulada | El alma es correcta (registrar lo que significó), pero pedir redacción mata el uso. Se reformuló como *huella con gesto rápido*. |
| **Mood tracking por tags** (sugerido en su momento por otra IA) | Reformulada | "Tag de emoción" sigue siendo tracking con maquillaje, copiable en una tarde. Lo valioso no era clasificar por emoción, sino *preservar lo que se sintió y poder volver a ello*. |
| **Diseñar para generar dependencia** | Rechazada (ética + estrategia) | Contradice el refugio. Se reemplazó por "ser extrañado": retención por amor, no por enganche. |

**Cada hipótesis caída afiló el "sí" final.** No fueron fracasos; fue el método.

---

## 8. Principios de producto (para cualquier decisión futura)

1. **Un alma, no dos.** Muchos elementos al servicio de un solo corazón; nunca dos centros peleando por el volante.
2. **El refugio primero.** Ante cualquier tensión (retención, features, monetización), gana el refugio.
3. **Fricción mínima, siempre opcional.** La huella base es un gesto liviano; todo lo que se le cuelga es opcional. Nunca obligar a redactar, nunca poner una decisión de formato en la cara del usuario en el momento emocional.
4. **La calma se construye sacando, no agregando.** Cada feature debe justificar su existencia contra el ruido que introduce.
5. **Ser extrañado, no volver dependiente.** Sin mecánicas de enganche manipulativas.
6. **El alma completa en v1; las profundizaciones después.** Distinguir siempre "alma entera" de "producto completo".
7. **Diseñar para que otro sienta lo que el fundador sintió con Mio** — no para replicar la feature, sino el sentimiento. Ese es el verdadero problema de diseño de Seijaku.

---

## 9. Consideraciones técnicas / de contexto

- **Plataforma:** Android nativo, Kotlin, Jetpack Compose, arquitecturas modernas.
- **Fuente de datos:** API de **AniList** (GraphQL). Se migró desde Jikan a principios de junio de 2026, porque Jikan se da de baja. *(Verificar el estado y los términos de uso vigentes de la API de AniList al momento de evaluar.)*
- **Estado de diseño:** durante ~1 año se desarrolló **sin ningún diseño de UI previo**, lo que generó rework constante. **A partir de ahora, el diseño de UI/UX debe pensarse antes de codear.** Seijaku va a pasar por un proceso de **refactorización** completo: la reorganización cambia funcionalidades y lo que la app ofrece.
- **Recurso más escaso:** la energía del fundador (solo-dev). Toda decisión debe cuidar ese recurso; el mayor riesgo del perfil es el perfeccionismo sin una definición de "listo".
- **Antecedente relevante:** el fundador ya llegó a un MVP de una startup previa (cobros por QR) que no salió a producción por factores externos. Seijaku es el intento de llevar *un* producto hasta el final, bien hecho.

---

## 10. Preguntas abiertas (trabajo en curso)

Estas son las decisiones de diseño todavía sin cerrar al momento de escribir este documento:

1. **La bienvenida a la obra-refugio:** al entrar a una obra llena de huellas, ¿el usuario ve primero *la obra* (arte, personajes, lo hermoso de la serie — como la vieja pantalla de detalle que el fundador amaba) con las huellas debajo, o ve primero *sus huellas* (su historia con la obra) con la obra como marco? Dicho corto: ¿ve primero a *ella* o se ve primero a *sí mismo en ella*?
2. **El vocabulario emocional del gesto:** ¿las emociones del gesto rápido son genéricas (alegría, tristeza, calma, intensidad) o tienen "la voz de Seijaku" (el vacío del final, la nostalgia por algo no vivido, la paz rara de una obra)? El fundador se inclina fuertemente por la voz propia; el "vacío del final" ya se identificó como firma.
3. **El tratamiento concreto del momento del final** (cómo se ve/siente marcar "completado" en una obra amada sin caer en lo depresivo).
4. **Traducción a pantallas concretas** de todo lo anterior, y definición del recorte fino de la v1.
5. **Qué es lo primero que ve un usuario nuevo** al abrir Seijaku por primera vez (onboarding del refugio: cómo elige/descubre su obra-refugio sin fricción).

---

## Resumen en una frase

> **Seijaku List es el refugio íntimo del otaku: no la lista de lo que consumís, sino el lugar sin audiencia donde tenés cerca lo que amás, dejás huellas de lo que te hizo sentir, y sos apañado cuando algo que amaste termina. La gente no se vuelve dependiente — te extraña.**
