# ValentinesGarage - Architecture & Integration Overview

## Role: Architecture + Integration Lead

**Responsibilities Covered:**
- Set up project structure (MVVM / layered architecture)
- Created shared models: `Truck`, `Employee`, `RepairTask`
- Database setup using **Room** (SQLite)
- Connected all modules together


---

# Project Structure
com.example.valentinesgarage
├── ui/ ← UI Layer
│ ├── checkin/
│ │ ├── CheckInScreen.kt ← Compose UI for truck check-in
│ │ └── CheckInViewModel.kt ← Handles truck input logic
│ ├── repair/
│ │ ├── RepairScreen.kt ← Compose UI for repair tasks
│ │ └── RepairViewModel.kt ← Handles repair logic
│ └── report/
│ ├── ReportScreen.kt ← Compose UI for reports
│ └── ReportViewModel.kt ← Handles report logic
│
├── data/ ← Data Layer
│ ├── local/
│ │ ├── entity/ ← Data models / tables
│ │ │ ├── Truck.kt
│ │ │ ├── Employee.kt
│ │ │ └── RepairTask.kt
│ │ ├── dao/ ← Database access
│ │ │ ├── TruckDao.kt
│ │ │ ├── EmployeeDao.kt
│ │ │ └── RepairTaskDao.kt
│ │ └── database/
│ │ └── AppDatabase.kt
│ └── repository/
│ └── GarageRepository.kt ← Handles all CRUD operations
│
├── domain/ ← Optional business logic
│ └── ValidateTruck.kt
│
├── di/ ← Hilt Dependency Injection
│ └── AppModule.kt
│
├── MainActivity.kt ← Entry point
└── build.gradle.kts ← Project configuration 
---

## Dependencies

- **Hilt** for dependency injection  
- **Room** for local SQLite database  
- **Kotlin Coroutines** for asynchronous operations  
- **Jetpack Compose** for UI  

---

## Database

- Tables created:  
  - `Truck`  
  - `Employee`  
  - `RepairTask`  

- DAO interfaces provide CRUD operations:  
  - `TruckDao`  
  - `EmployeeDao`  
  - `RepairTaskDao`  

- Repository (`GarageRepository`) handles all data operations and exposes flows to ViewModels.

---

## Architecture Flow

1. **UI Layer (Compose screens)** collects input and observes data.  
2. **ViewModel** communicates with **Repository** to fetch or update data.  
3. **Repository** interacts with **DAOs**, which talk to the **Room database**.  
4. Hilt manages injection of repository and DAOs into ViewModels.

---

## Completed Tasks

- [x] MVVM project structure  
- [x] Shared data models (`Truck`, `Employee`, `RepairTask`)  
- [x] Room database setup with DAOs  
- [x] Repository (`GarageRepository`) fully functional  
- [x] Hilt DI integration  
- [x] Navigation and MainActivity setup  
- [x] Tested repository flow with a sample screen  

---

## Next Steps for Team Members

- **Kujii**: Implement check-in screen using `CheckInViewModel`.  
- **Lazi**: Implement repair tracking screen using `RepairViewModel`.  
- **Student**: Implement reporting screen using `ReportViewModel`.  

---

## Notes

- The database is currently local (Room). Later, you can integrate **Firebase** if we want.  
- All modules are fully injected via Hilt.  
- Make sure to observe flows from repository in ViewModels for live updates.  
