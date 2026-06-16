MODERN PERSONAL DATA GUI — Setup & Run
=======================================

Requirements:
  - Java JDK 11 or later
  - MySQL server (optional – app works without it, just no DB persistence)

MySQL Setup (optional):
  CREATE DATABASE personal_db;
  USE personal_db;
  CREATE TABLE personal_data (
      id INT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(100),
      dob VARCHAR(30),
      gender VARCHAR(10),
      favourites VARCHAR(200)
  );
  -- Edit DatabaseManager.java to set your DB credentials if needed.

Compile:
  Windows : double-click compile.bat
  Mac/Linux: bash compile_run.sh

Run after compiling:
  Windows : double-click run.bat
  Mac/Linux: java -cp .:mysql-connector-j-9.7.0.jar PersonalDataGUI
