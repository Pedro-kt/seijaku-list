# Seijaku List — Brief de Identidad

> Documento de identidad visual y de marca. Autónomo: contiene todo lo necesario para diseñar
> o implementar la cara de Seijaku sin contexto previo.
> Complementa a `seijaku-list-vision.md` (producto) y `refugio-definicion-operacional.md` (el corazón).
> Archivos de sistema que acompañan este brief: `SeijakuColors.kt`, `Type.kt`, y los SVG de logo.
> Fecha: julio 2026.

---

## 1. Qué es Seijaku (para quien diseña)

Seijaku List es una app Android de anime y manga que **no es un tracker — es un refugio.**
Un lugar íntimo, sin audiencia, donde el usuario tiene cerca lo que ama y guarda lo que le hizo sentir.
El tracking (listas, estados, progreso) es el cuerpo funcional; el corazón es el refugio.

**Seijaku (静寂)** significa *quietud, calma, silencio*. La identidad visual entera debe **verse y sentirse como eso.**
El error a evitar en cada decisión: que Seijaku parezca "un tracker enérgico más". El objetivo: que se sienta un refugio calmo.

**La brújula, en una frase:** cálido antes que frío, silencioso antes que llamativo, íntimo antes que impersonal.
Cuando haya que decidir entre dos opciones, ganar siempre la más cálida y la más callada.

---

## 2. Principios de diseño (gobiernan todo)

1. **Quietud.** El ~90% de cualquier pantalla es fondo + texto. El color entra solo donde importa.
   La calma se construye sacando, no agregando. Ante la duda, quitar.
2. **Calidez.** Nada frío ni clínico. Ni negros puros, ni blancos puros, ni colores saturados como norma.
   Todo tiene una gota de calor: el fondo oscuro tira a tinta, el claro a papel, el texto nunca es blanco/negro puro.
3. **Refugio.** El diseño acompaña a lo que el usuario ama, no compite con ello. La obra y las huellas
   son el protagonista; el resto (chrome, datos, controles) se corre para dejarlas brillar.

---

## 3. Sistema de color

Dos temas completos. **El light NO es el dark invertido** — cada tema tiene su set propio recalibrado.
Valores exactos y listos para Compose en `SeijakuColors.kt`. Resumen para diseño:

### Tema oscuro · carbón templado (tema principal)
- Fondo base `#1A1917` · cards `#232220` · elevado `#2E2B28` · borde `#3A3733`
- Texto: primario `#ECE7DE` · secundario `#A39E95` · tenue `#6E6A63` (cálidos, nunca blanco puro)
- **Acento principal — CREAM** `#D9C4A3` (claro `#E7D8BC`, profundo `#B89B72`)
- **Acento secundario — SALVIA** `#93A088` (clara `#A9B49F`, profunda `#6F7C66`)

### Tema claro · papel suave
- Fondo base `#F2EDE3` (papel, no blanco) · cards `#FBF7EF` · elevado `#FFFFFF` · borde `#E0D8C8`
- Texto: primario `#2A2621` · secundario `#6E685D` · tenue `#9A9184` (marrón cálido, nunca negro puro)
- **Cream recalibrado a caramelo tostado** `#8A6A38` (sobre papel el cream claro desaparece)
- **Salvia** `#5E7150`

### Regla de hierro de los acentos (vale en ambos temas)
- **CREAM = principal.** "Lo tuyo, lo emocional": huellas, obra-refugio, la voz, momentos que importan. El cream **habla**.
- **SALVIA = secundario.** "El aire, la calma": estados calmos, detalles, acentos de sección. La salvia **acompaña**.
- **NUNCA al mismo nivel. Cream manda, salvia apoya. Siempre.** El día que se usan 50/50, se rompe la identidad.

### Estados de lista (única excepción de color "vivo")
Son información funcional (escaneo de listas), por eso se les permite ser lo más vivo de la app —
pero apagados y **separados por tono** (cada uno en su zona del círculo cromático) para distinguirse sin gritar.
Nada más en la app toma prestada esta intensidad.

| Estado | Dark | Light |
|---|---|---|
| Planeado (lavanda) | `#9B86C4` | `#6E5EA0` |
| Viendo (celeste) | `#6FA9C7` | `#3F7E9C` |
| Completado (verde) | `#7FB56E` | `#40632E` |
| Pausado (ámbar) | `#D6A94E` | `#9A7420` |
| Abandonado (terracota) | `#D2705A` | `#A5502F` |

(Cada estado tiene además un tinte de fondo para los chips-pill; ver `SeijakuColors.kt`.)

---

## 4. Sistema tipográfico

Dos familias con roles estrictos, gemelo de la jerarquía cream/salvia. Escala completa en `Type.kt`.

- **ONEST — interfaz (el cuerpo).** Todo lo funcional: títulos, cuerpo, labels, botones, metadata, listas.
  Sans humanista cálida que "desaparece": sostiene el contenido sin llamar la atención.
- **LORA — voz (el alma).** Solo la voz de la app y las huellas del usuario. Serif cálida que abraza,
  con aire de diario íntimo. Sobre papel se ve aún más "escrita".

### Reglas de hierro tipográficas
- **Lora aparece ÚNICAMENTE en la voz y las huellas. NUNCA en interfaz** (botones, metadata, labels).
  Si la serif se escapa a lo funcional, queda amateur y se rompe el encanto.
- **Nada de itálicas en la interfaz.** El itálico bold de la versión vieja daba urgencia deportiva —
  lo opuesto a la quietud. La ÚNICA itálica permitida es la serif Lora italic, y solo para el texto
  que el usuario escribe en sus huellas (se siente escrito a mano).
- Redonda, tranquila, con aire: line-heights relajados, letter-spacing suave. La app respira, no aprieta.

### Fuentes
Ambas open source (Google Fonts). **Empaquetar localmente** en `res/font` (no descarga en runtime),
para que la app se vea igual siempre, offline incluido, y sin parpadeo de fuente al abrir.

---

## 5. Logo

**Dirección aprobada:** apilado (lockup vertical) — el kanji **静** arriba, **Seijaku** debajo (en Lora),
**LIST** abajo (en Onest, letter-spacing amplio, color salvia). "Seijaku List" en texto **debe estar siempre**
(el kanji solo es mudo para quien no lee japonés, y en Play Store se busca por el nombre).

### El sistema de logo (tres usos, misma marca)
1. **Apilado** (kanji corona el nombre) — ceremonial: splash, pantalla de bienvenida.
2. **Horizontal** (kanji al lado del nombre, separados por una línea fina) — headers, barras.
3. **Ícono de app** — el kanji dentro de un tile redondeado carbón. Es el app icon y sirve para espacios chicos.

Colores del logo: kanji en **cream** `#D9C4A3` sobre oscuro / en **caramelo** `#8A6A38` sobre papel.
Nombre en texto primario del tema. "LIST" en salvia.

### ⚠️ Nota de producción (crítica)
Los SVG entregados (`seijaku-logo-dark.svg`, `seijaku-logo-light.svg`, `seijaku-app-icon.svg`) son
**bocetos de dirección**: el kanji está puesto con una fuente (Noto Serif JP), no como trazo propio.
Para producción hay que **vectorizar el 静 a trazos propios y ajustarlo a mano** (grosor, equilibrio),
para que (a) no dependa de que la fuente esté disponible y (b) sea una marca propia y no "un glifo de una tipografía".
Desde ahí, exportar los formatos de Android: **ícono adaptativo** (foreground/background), densidades, vector drawables.

**Tagline (opcional, sin decidir):** se probó "tu refugio de anime". Un descriptor ayuda en Play Store,
pero el silencio también es muy Seijaku. Decisión abierta.

---

## 6. Voz y tono (la identidad verbal)

El tono es el diferenciador más profundo y menos copiable de Seijaku. Principio central:
**la app te habla como un amigo que entiende, no como un sistema que te asiste.**

### Reglas de la voz
- **Nombra lo que sentís antes de que lo digas vos.** No pregunta "¿cómo te sentís?" (eso es trabajo);
  asume y acompaña ("cuesta soltarlo, ¿no?").
- **Habla poco.** Calidez sobria, deja aire. Sin signos de exclamación, sin emojis en la voz de la app.
- **Le habla a la persona, no felicita la acción.** Nunca "¡Completado! +1 🎉".
- **Te espera adentro, no te persigue afuera.** Jamás un push tipo "te extrañamos, volvé" (eso es la
  manipulación de la que Seijaku escapa). La voz cálida solo aparece cuando el usuario abrió la app por su cuenta.
- **Minúscula siempre** en los textos de voz. Más íntima, menos declarativa. Es parte de la identidad.

### La voz tiene registros según el peso del momento
Cuanto más cotidiano el momento, más liviana la voz. Cuanto más cargado, más se permite decir algo que toque.
- **Cotidiano** (marcar un episodio): un roce — "anotado", "seguimos".
- **Empieza a importar** (primera huella): un poco de calidez — "quedó tuya", "esto ya vive acá".
- **Momento que pesa** (el final, un eco): se abre — "lo terminaste… cuesta soltarlo, ¿no?".
- **Te encuentra** (volver a una obra vieja, al recibir): lo más delicado — "hacía rato que no venías por acá",
  "volviste". Solo cuando el usuario inició el encuentro.

### La voz NUNCA
Gamifica (logros, rachas, puntos) · manda push de reenganche · infantiliza ("holis", "¿cómo está tu corazoncito?")
· fuerza emoción en un momento liviano.

---

## 7. Resumen de reglas de hierro (checklist para no romper la identidad)

- [ ] Cream manda, salvia acompaña — nunca al mismo nivel.
- [ ] Ningún color saturado como norma; los estados son la única excepción (y aun así, apagados).
- [ ] Lora solo en voz y huellas; Onest en todo lo demás.
- [ ] Ninguna itálica en interfaz; solo Lora italic para lo que el usuario escribe.
- [ ] Ni negro puro ni blanco puro; fondo cálido (carbón templado / papel), texto cálido.
- [ ] La voz habla en minúscula, poco, y espera adentro (nunca persigue afuera).
- [ ] "Seijaku List" en texto siempre presente junto al kanji.
- [ ] Construir dark y light desde el día uno, probando ambos a medida (el light no es un port de última hora).
- [ ] Ante cualquier duda: la opción más cálida y más callada.

---

## Norte, en una frase

> **Seijaku no debe verse como una app que te asiste, sino sentirse como un lugar que te apaña:
> callado, cálido, y tuyo.**
