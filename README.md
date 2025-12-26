#  ScentCast 🌬️🧴

**ScentCast** is a smart Android application that helps fragrance enthusiasts choose the perfect scent for the day. By integrating real-time weather data with a personal digital fragrance library, the app uses a custom algorithm to recommend the most suitable bottle based on temperature, season, and olfactory notes.

## 📱 Features
* **Smart Recommendations:** Automatically suggests a fragrance based on current local temperature (e.g., Fresh/Citrus for Hot weather, Woody/Spicy for Cool weather).
* **Digital Wardrobe:** Manage your collection with full CRUD functionality (Add, Edit, Delete bottles).
* **Live Weather Integration:** Fetches real-time data using the OpenWeatherMap API.
* **Longevity Tracker:** A built-in timer to track when you last applied a scent and reminds you when to reapply.
* **Discovery Mode:** Suggests popular fragrances for the current climate to help you discover new scents.
* **Dynamic Theming:** Fully supports Light and Dark modes with a custom Cyan/Grey modern aesthetic.

## 🛠️ Tech Stack & Architecture
The app follows modern Android development practices, utilizing the **MVVM (Model-View-ViewModel)** architecture to ensure separation of concerns and testability.

* **Language:** Kotlin
* **Architecture:** MVVM (Model-View-ViewModel)
* **UI:** XML Layouts with Material Design components
* **Local Database:** Room (SQLite abstraction)
* **Networking:** Retrofit & GSON (REST API integration)
* **Asynchronous Processing:** Kotlin Coroutines & Flow
* **Testing:** JUnit (Unit testing for Recommendation Engine logic)

## 🧩 How It Works
1.  **Data Layer:** The `FragranceRepository` acts as the single source of truth, mediating between the local `Room` database and the remote Weather API.
2.  **Logic Layer:** A dedicated `RecommendationEngine` processes the user's collection against the current temperature logic:
    * *Hot (>25°C):* Filters for Summer, Citrus, Aquatic.
    * *Cold (<10°C):* Filters for Winter, Oud, Vanilla, Leather.
3.  **UI Layer:** The `ScentViewModel` exposes `LiveData` to the Activities, ensuring the UI updates reactively without memory leaks.

## 📸 Screenshots
*(Add your screenshots here)*

## 🚀 Setup & Installation
1.  Clone the repository:
    ```bash
    git clone [https://github.com/YourUsername/ScentCast.git](https://github.com/YourUsername/ScentCast.git)
    ```
2.  Open the project in **Android Studio**.
3.  Add your API Key:
    * Get a free key from [OpenWeatherMap](https://openweathermap.org/api).
    * Open `Constants.kt` and replace the placeholder with your key.
4.  Build and Run on an Emulator or Physical Device.

## 🧪 Testing
The project includes Unit Tests verifying the core business logic.
Run tests via Android Studio or command line:
```bash
./gradlew test