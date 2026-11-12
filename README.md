<p align="center">
  <img width="150" height="150" alt="ic_launcher" src=".github/assets/ic_launcher_readme.png" />
</p>

<h1 align="center">ExpenSync</h1>

<p align="center">
  <strong>Gestión inteligente, elegante y simplificada de gastos compartidos para Android</strong>
</p>

<p align="center">
  <a href="https://kotlinlang.org/"><img src="https://img.shields.io/badge/Kotlin-2.0.21-7F52FF.svg?logo=kotlin&logoColor=white" alt="Kotlin" /></a>
  <a href="https://developer.android.com/jetpack/compose"><img src="https://img.shields.io/badge/Jetpack%20Compose-M3-4285F4.svg?logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" /></a>
  <a href="https://developer.android.com/training/dependency-injection/hilt-android"><img src="https://img.shields.io/badge/Dagger%20Hilt-2.60.1-008080.svg" alt="Hilt" /></a>
  <a href="https://supabase.com/"><img src="https://img.shields.io/badge/Backend-Supabase%20PostgREST-3ECF8E.svg?logo=supabase&logoColor=white" alt="Supabase" /></a>
  <a href="https://developer.android.com/training/data-storage/room"><img src="https://img.shields.io/badge/Local%20Storage-Room%202.6.1-FF6F00.svg" alt="Room" /></a>
  <a href="https://developer.android.com/about/versions/14"><img src="https://img.shields.io/badge/Android-SDK%2026%2B-3DDC84.svg?logo=android&logoColor=white" alt="Android SDK" /></a>
</p>

---

## Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Características Principales](#-características-principales)
- [Diseño y Experiencia de Usuario (UI/UX)](#-diseño-y-experiencia-de-usuario-uiux)
- [Algoritmo de Liquidación de Deudas](#-algoritmo-de-liquidación-de-deudas)
- [Arquitectura del Proyecto](#-arquitectura-del-proyecto)
- [Estructura Multi-Módulo](#-estructura-multi-módulo)
- [Stack Tecnológico](#-stack-tecnológico)
- [Persistencia y Capa de Datos](#-persistencia-y-capa-de-datos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Seguridad](#-seguridad)
- [Licencia](#-licencia)

---

## Descripción General

**ExpenSync** es una aplicación móvil nativa para Android diseñada para resolver de forma definitiva los dolores de cabeza de las cuentas compartidas en viajes, pisos de estudiantes, salidas grupales y proyectos en equipo.

Permite registrar gastos en segundos, calcular automáticamente la cuota correspondiente a cada participante y generar un **Plan de Liquidación Inteligente** con el número mínimo de transferencias bancarias para quedar a mano.

---

## Características Principales

- 💳 **Tarjeta Bancaria de Lujo Flotante (`LuxuryBankCard`)**: Indicador principal de gastos con estética metálica *Black Titanium*, chip EMV grabado en oro, pagos sin contacto NFC, emblema de red de lujo y animación de levitación suave.
- 🧮 **Cálculo y Simplificación Voraz de Deudas**: Algoritmo optimizado que equilibra saldos y reduce drásticamente las transferencias necesarias entre participantes.
- 🌊 **Plan de Liquidación Desmarcado**: Visualización fluida sin cajas pesadas, conectada por líneas vivas de energía y vectores de flujo animados entre deudor y acreedor.
- ⚡ **Estrategia Offline-First**: Almacenamiento local ultra rápido con **Room Database (SQLite)** y sincronización en la nube mediante **Supabase PostgREST**.
- 🔍 **Buscador Takeover Integrado**: Barra de búsqueda envolvente con transición suave de resortes, auto-focus y borrado rápido en un toque.
- 🏷️ **Filtros Segmentados por Categoría**: Píldoras compactas para explorar transacciones por comida, transporte, entretenimiento, compras, servicios o saldo individual.
- 📝 **Hoja de Registro Rápido (`AddExpenseSheet`)**: Modal inferior con conceptos rápidos frecuentes (Cena, Supermercado, Uber, Café, Gasolina), selección del pagador y distribución en tiempo real.
- 👥 **Avatares Dinámicos con Resplandor**: Identificadores visuales basados en la paleta cromática de la marca (*Golden Amber, Terracotta Copper, Slate Mineral, Pine Night*).

---

## Diseño y Experiencia de Usuario (UI/UX)

La aplicación sigue estrictamente los principios de **Material Design 3 (M3)** combinados con un lenguaje visual distintivo:

| Componente | Detalles de Implementación |
| :--- | :--- |
| **Tarjeta Hero de Lujo** | Simula una tarjeta de crédito física *Titanium Obsidian*, con acabado cepillado, bordes achaflanados en oro/platino, destello de luz diagonal periódico y física de flote flotando en el eje Y. |
| **Chip EMV de Oro** | Renderizado en Canvas con microcircuitos grabados y contacto central dividido. |
| **Líneas Vivas (Living Lines)** | Separadores dinámicos donde un haz de luz láser en degradé dorado y cobre recorre la pantalla continuamente. |
| **Avatares con Halo** | Micro-animaciones de respiración (*breathing aura*) que diferencian instantáneamente a cada usuario (`Yo`, `Ana`, `Carlos`, `Luis`). |
| **Modo Oscuro Atmosférico** | Fondo inmersivo con capas de luz radial en verde petróleo, ámbar y pizarra que aportan profundidad tridimensional. |

---

## Algoritmo de Liquidación de Deudas

El cálculo de saldos y la simplificación de deudas se implementa en `domain/usecase/CalculateDebtsUseCase.kt`:

1. **Balance Neto Individual**:
   $$\text{Saldo}_i = \text{Total Aportado}_i - \sum \text{Cuotas Asignadas}_i$$
   - $\text{Saldo}_i > 0$: Acreedor (le deben dinero).
   - $\text{Saldo}_i < 0$: Deudor (debe dinero).
   - $\text{Saldo}_i = 0$: En equilibrio.

2. **Resolución Voraz (Greedy Settlement)**:
   - Se empareja iterativamente al mayor deudor con el mayor acreedor.
   - Se salda el monto mínimo entre la deuda pendiente y el crédito por cobrar:
     $$\text{Monto} = \min(\text{DeudaRestante}, \text{CreditoRestante})$$
   - El resultado minimiza las transacciones entre los miembros del grupo, evitando transferencias circulares o redundantes.

---

## Arquitectura del Proyecto

ExpenSync está construido bajo **Clean Architecture** y principios de diseño modular:

```
┌────────────────────────────────────────────────────────┐
│                   :app (Application)                   │
│          Application class, Insets & Hilt App          │
└───────────────────────────┬────────────────────────────┘
                            │
       ┌────────────────────┼────────────────────┐
       ▼                    ▼                    ▼
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  :features   │     │    :data     │     │    :core     │
│ UI (Compose) │     │ Room + S敵   │     │ Utilidades   │
│ ViewModels   │     │ Repositorios │     │ Extensiones  │
└──────┬───────┘     └──────┬───────┘     └──────────────┘
       │                    │
       └───────────┬────────┘
                   ▼
       ┌──────────────────────┐
       │       :domain        │
       │ Modelos & Casos Uso  │
       │  (Kotlin puro / JVM) │
       └──────────────────────┘
```

---

## Estructura Multi-Módulo

El repositorio está dividido en 5 módulos desacoplados:

- **`:app`**: Módulo ejecutable principal. Contiene `MainActivity.kt`, temas globales, insets edge-to-edge y configuración del Android Application Plugin.
- **`:domain`**: Módulo puro de Kotlin (sin dependencias del SDK de Android). Aloja:
  - Modelos: `Expense`, `Debt`, `User`.
  - Casos de uso: `CalculateDebtsUseCase`, `AddExpenseUseCase`, `GetExpensesUseCase`.
  - Contrato del repositorio: `ExpenseRepository`.
- **`:data`**: Implementación de la persistencia y conectividad:
  - Base de datos local: `ExpenSyncDatabase`, `ExpenseEntity`, `ExpenseDao` (Room).
  - API remota: `SupabaseModule`, `ExpenseDto` (Supabase PostgREST + Ktor).
  - Repositorio: `ExpenseRepositoryImpl` (estrategia Offline-First con `Flow`).
- **`:features`**: Interfaz de usuario reactiva en Jetpack Compose:
  - `HomeScreen.kt`: Pantalla principal con feed de transacciones y estados reactivos.
  - `LuxuryBankCard.kt`: Tarjeta bancaria premium con animación de levitación.
  - `DebtSummarySection.kt`: Resumen desmarcado del plan de liquidación.
  - `AddExpenseSheet.kt`: BottomSheet interactivo de registro de gastos.
  - `ExpenseViewModel.kt`: Gestión de estado unidireccional (UDF) mediante `StateFlow`.
- **`:core`**: Clases base, reglas de consumo y utilidades compartidas.

---

## Stack Tecnológico

| Componente | Tecnología | Versión |
| :--- | :--- | :--- |
| **Lenguaje** | Kotlin | 2.0.21 |
| **UI Toolkit** | Jetpack Compose (BOM) | 2026.02.01 |
| **Design System** | Material Design 3 | 1.10.0+ |
| **Dependency Injection** | Dagger Hilt | 2.60.1 |
| **Procesador de Anotaciones** | Google KSP | 2.0.21-1.0.28 |
| **Base de Datos Local** | AndroidX Room | 2.6.1 |
| **Backend & Base de Datos Remota** | Supabase PostgREST | 3.0.2 |
| **Motor HTTP** | Ktor Client CIO | 3.0.0 |
| **Serialización** | Kotlinx Serialization JSON | 1.6.3 |
| **Build System** | Gradle (Kotlin DSL) | 9.3.1 (AGP 9.1.1) |

---

## Persistencia y Capa de Datos

ExpenSync utiliza un patrón **Offline-First**:
1. Al abrir la app, `ExpenseRepositoryImpl.getExpenses()` emite inmediatamente los datos locales desde SQLite vía Room DAO.
2. En paralelo, mediante el operador `onStart`, se realiza una petición HTTP al endpoint de Supabase (`expenses`).
3. Las entidades remotas recibidas se insertan en la base de datos local Room, disparando una actualización reactiva sin parpadeos ni bloqueos de red.
4. Si el dispositivo no cuenta con conexión a internet, la aplicación sigue funcionando de manera continua e ininterrumpida.

---

## Instalación y Configuración

### Prerrequisitos
- **Android Studio**: Ladybug (2024.2.1) o superior.
- **JDK**: Java 17 o Java 21 (Temurin).
- **Android SDK**: `minSdk = 26`, `compileSdk = 36` (Android 8.0 Oreo hasta Android 16).

### Clonar y Compilar
```bash
# 1. Clonar el repositorio
git clone https://github.com/JastinBolanos/ExpenSync.git
cd ExpenSync

# 2. Compilar la aplicación en modo Debug
gradle assembleDebug

# 3. Ejecutar las pruebas unitarias locales
gradle testDebugUnitTest
```

---
