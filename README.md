# 📡 Project MIS - Signal Strength Analyzer

## 📌 Overview
**Project MIS** is an Android application designed to measure, store, and analyze **Wi-Fi signal strength**. It utilizes a **Nearest Neighbor Algorithm** to find the closest matched coordinates based on signal strength data. The results are then visualized on a **custom map**.

## 🚀 Features
- 📊 **Signal Strength Input**: Users can input three signal strength values.
- 🔍 **Nearest Neighbor Algorithm**: Matches input values with the closest available dataset.
- 🗺 **Custom Map Visualization**: Displays matched locations on an interactive map.
- 📋 **Editable Data List**: Users can edit or remove signal strength entries.
- ☁ **Retrofit API Integration**: Fetches real-time data from a Node.js backend.

---

## 🛠 Tech Stack
- **Kotlin** - Primary programming language  
- **Room Database** - Local data persistence  
- **Retrofit** - API calls to fetch signal data  
- **Coroutines (Dispatchers.IO)** - Handles async API requests efficiently  
- **Android Jetpack Components** - Ensures smooth UI performance  
- **RecyclerView & ListView** - Manages dynamic lists effectively  

---

## 🔧 Installation & Setup

### **1️⃣ Clone the repository**
```sh
git clone https://github.com/YOUR_GITHUB_USERNAME/signals-mapping.git
cd signals-mapping
```

### 2️⃣ Open the project in Android Studio
- Ensure you have the required SDK (**minSdk: 30, targetSdk: 34**).
- Sync **Gradle** and install dependencies.
- Run the app on an **emulator** or a **real device**.

---

## 🔄 API Endpoints (Backend Integration)
| **Method** | **Endpoint** | **Description** |
|-----------|------------|----------------|
| **GET** | `/api/signalStrength` | Fetches available signal strength records |
| **GET** | `/api/allCoordinates` | Retrieves all stored coordinates |
| **GET** | `/api/getCoordinates/{matavimasId}` | Finds coordinates for a given `matavimasId` |

---

## 🚀 Future Enhancements
- 📌 Implement **LiveData / Flow** for real-time data updates.  
- 🌍 **Google Maps API** integration for a more interactive experience.  
- 📶 Optimize signal strength calculation using **AI models**.  

---

## 🤝 Contributing
Want to contribute? Follow these steps:

1. **Fork this repository**  
2. **Create a feature branch**  
   ```sh
   git checkout -b feature-name
   ```
3. Commit your changes
```sh
git commit -m "Added a new feature"
```
4. Push to the branch
```sh
git push origin feature-name
```
5. Open a Pull Request 🚀

---

## 📜 License
This project is licensed under the MIT License.

