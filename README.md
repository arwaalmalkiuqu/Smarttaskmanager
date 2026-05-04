# Smarttaskmanager
Project Overview

The Smart Task Manager is an enterprise-level Java application designed to organize daily tasks with integrated real-time weather intelligence. It follows a decoupled architecture, ensuring the core logic remains independent of the graphical user interface

2. Technical Pillars :

1. Reactive Programming Implementation of Project Reactor (Flux and Mono) for non-blocking asynchronous data streams.
2. Concurrency: Offloading I/O and network operations to Schedulers.boundedElastic() to maintain UI responsiveness.
3. Thread Safety: Utilization of ConcurrentHashMap and strict adherence to the Swing Event Dispatch Thread (EDT) rules.
4.  API Integration: Live weather data fetching from OpenWeatherMap with an intelligent 10-minute caching mechanism.

3. Core Architecture & Design Patterns

The system is engineered as a Reusable Java API, strictly separating business logic from the graphical interface.
1- Facade Pattern: The TaskManager class acts as a single entry point to the system's complex sub-systems.
2- Builder Pattern: Implemented via DefaultTaskManagerBuilder to provide a clean and flexible way to configure the manager instance (e.g., setting API keys).
3- Reactive Programming: Built using Project Reactor (Flux & Mono) to handle data streams asynchronously without blocking execution threads


Q1: How to run this app?
1- Ensure JDK 17+ and Maven are installed.  
2- Run mvn clean install then mvn exec:java -Dexec.mainClass="taskmanager.api.MainApp".  

Q2: Where to put the API Key?

Open MainApp.java and paste your key in the .withWeatherApiKey("YOUR_KEY") method inside the main function.
