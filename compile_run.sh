#!/bin/bash
javac -cp mysql-connector-j-9.7.0.jar *.java && java -cp .:mysql-connector-j-9.7.0.jar PersonalDataGUI
