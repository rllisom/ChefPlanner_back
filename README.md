# ChefPlanner 🍳

**"Evita desperdiciar comida planificando las recetas de tu menú semanal"**

ChefPlanner es una solución integral diseñada para optimizar la organización alimentaria doméstica. 
La aplicación permite a los usuarios gestionar su recetario, planificar menús semanales y generar listas de la compra inteligentes basadas en lo que realmente necesitan cocinar, evitando compras innecesarias y aprovechando mejor lo que ya tienen en casa.

---

## 📋 Información del Proyecto
* **Contexto:** Proyecto Interdisciplinar 2º DAM (PSP, AD, PMDM, DI y OPT).

## 🛠️ Stack Tecnológico
El ecosistema de ChefPlanner se divide en tres capas principales:
* **Mobile:** 📱 Flutter (Android/iOS).
* **Backend:** 🌐 API REST desarrollada con **Spring Boot 3**.
* **Base de Datos:** 🗄️ **PostgreSQL** para la persistencia de datos.

---

## 🚀 Funcionalidades Principales
1.  **Exploración de Recetas:** Búsqueda avanzada por texto, dificultad (EASY, MEDIUM, HARD) y tiempo de preparación.
2.  **Gestión de Recetario:** Los usuarios pueden crear, editar y personalizar sus propias recetas asociando ingredientes y cantidades.
3.  **Planificador de Menú:** Organización de comidas por fecha y tipo (Desayuno, Almuerzo, Cena).
4.  **Lista de la Compra Inteligente:** Generación automática de una lista agregada por ingrediente y unidad entre dos fechas seleccionadas.
5.  **Gestión de Despensa:** Control de los ingredientes disponibles en el hogar.

---

## 🔐 Acceso y Roles
El sistema utiliza **Spring Security** para la gestión de acceso. El `DataLoader` inicializa los siguientes usuarios de prueba:

| Usuario | Contraseña | Rol | Descripción |
| :--- | :--- | :--- | :--- |
| `admin` | `prueba123` | **ADMIN** | Gestión global y moderación del sistema. |
| `chef_maria` | `contrasena123` | **USER** | Gestión de recetas propias y planificación. |

> **Nota:** Existe también un rol **MANAGER** opcional para destacar recetas (`featured`) en la plataforma.

---

## 🏗️ Modelo de Datos
El núcleo del sistema se basa en las siguientes entidades principales:
* **User:** Representa a la persona que utiliza la app y es dueño de sus planes y recetas.
* **Recipe:** Receta que incluye título, descripción, minutos y dificultad.
* **Ingredient:** Catálogo maestro de ingredientes para evitar duplicidades.
* **RecipeIngredient:** Relación con atributos (cantidad y unidad) entre recetas e ingredientes.
* **MenuItem:** Elemento del menú planificado por un usuario en una fecha y tipo de comida.

---

## 📐 Reglas de Negocio
* **Integridad:** Una receta debe tener al menos 1 ingrediente para ser válida.
* **Unicidad:** Un usuario no puede planificar dos recetas distintas para el mismo día y tipo de comida.
* **Validación:** El tiempo de preparación (`minutes`) debe ser siempre positivo.
* **Agregación de Compra:** El sistema suma cantidades cuando coinciden exactamente el **Ingrediente + Unidad**.

---

## 🛠️ Instalación y Configuración (Backend)

### Requisitos previos
* Java 17 o superior.
* PostgreSQL.
* Maven.

### Pasos
1. **Clonar el repositorio:**
   ```bash
   git clone <tu-url-de-github>
   cd gestioCaseta

## 🐳 Despliegue con Docker


### Requisitos previos
* Tener instalado [Docker Desktop](https://www.docker.com/products/docker-desktop/) y que el demonio de Docker esté en ejecución.

### Gestión del Entorno

Para gestionar los contenedores, sitúate en la raíz del proyecto donde se encuentra el archivo `docker-compose.yml` y utiliza los siguientes comandos:

#### 1. Levantar el sistema
Este comando descarga las imágenes necesarias, compila el código fuente de la API y arranca tanto la base de datos como el servidor:
```bash
docker-compose up --build
