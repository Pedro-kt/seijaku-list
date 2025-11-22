# 📌 SeijakuList

<p align="center">
  <img src="./app/src/main/res/drawable/seijaku_logo_design.png" alt="SeijakuList Logo" width="150" style="border-radius: 20px;"/>
</p>

**SeijakuList** es una aplicación Android desarrollada en **Kotlin con Jetpack Compose**, pensada para que los usuarios gestionen y registren sus animes favoritos de una forma simple y moderna.  

---

## 📖 Descripción  

Con **SeijakuList** podés:  
- 📺 Registrar animes y organizar su estado (Viendo, Completado, Pendiente, etc.).  
- 📝 Marcar episodios vistos y veces vistas.  
- ☁️ Guardar tu progreso en la nube gracias a Firebase.  
- 🔄 Sincronizar automáticamente tu lista entre dispositivos.  
- 🔍 Filtrar y buscar animes fácilmente.
- 📚 Obtener información detallada sobre tus personajes, animes, estudios, etc.
- 🏆 Obtener logros mediante el uso de la aplicación.
- 👤 Personalizar tu perfil.
- 🎨 Entre otras cosas.

---

## 🛠️ Tecnologías usadas  

- **Lenguaje:** Kotlin  
- **UI:** Jetpack Compose  
- **Arquitectura:** MVVM + StateFlow  
- **Persistencia local:** Room (ORM sobre SQLite)  
- **Inyección de dependencias:** Hilt  
- **Navegación:** Navigation Compose  
- **Asincronía:** Kotlin Coroutines + Flow  
- **Red:** Retrofit + Gson (consumo de APIs)  
- **Carga de imágenes:** Coil  
- **Backend:** Firebase (Auth + Firestore + Storage + Crashlytics) partes en desarrollo e integracion


---

## 📸 Vistazo Rápido (Screenshots)

<p align="center">
  <img src="./images/mockup-welcome-login-register.png" width="700" title="Welcome, Login y Register">
</p>

<p align="center">
  <img src="./images/mockup-home-search.png" width="700" title="Home y Búsqueda">
</p>

<p align="center">
  <img src="./images/mockup-search-my_animes-detail.png" width="700" title="Búsqueda, Mis Animes y Detalle">
</p>

<p align="center">
  <img src="./images/mockup-add-list.png" width="700" title="Agregar a Lista">
</p>

<p align="center">
  <img src="./images/mockup-logros.png" width="700" title="Logros">
</p>

---

## 🚀 Instalación y uso  

1. Clonar el repositorio:  
   ```bash
   git clone https://github.com/Pedro-kt/seijaku-list.git

2. Abrir el proyecto en Android Studio.

3. Configurar tu proyecto en Firebase y descargar el google-services.json.

4. Colocar el archivo en la carpeta app/.

5. Selecciona un dispositivo o emulador con API 26 (Android 8.0) o superior y ejecutalo en el mismo


