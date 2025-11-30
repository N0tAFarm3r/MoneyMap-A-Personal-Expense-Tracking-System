# MoneyMap-A-Personal-Expense-Tracking-System

Table of Contents

Project Overview
Requirements
Installation & Dependencies (Linux/Windows)
End User Usage
Test process & Results
Future Work & Improvements

Project Overview

MoneyMap is a lightweight JavaFX desktop application to track personal income and expenses. It provides simple screens to add income and expenses, view lists of entries, compute totals as well as averages, and export a brief report (CSV) that simply summarizes the total as well as averages, making financial management much more easy.

Primary users:  The primary intended User for this project are the individuals who want to simplify the management of their personal finances more effectively as well as efficiently. The program will be designed in a way that is simple and user friendly, and it will also target people such as students, young adults, and small business owners. 


Key features:
- Add Income entries (amount, description, date)
- Add Expense entries (amount, description, date)
- View totals, averages, and net balance
- Export a report summary (CSV) to local disk



Requirements

Functional requirements:
- Allow users to record income and expense entries.
- Persist entries to local CSV files.
- Show lists of incomes and expenses.
- Compute total income, total expenses, overall net, and averages for incomes and expenses.
- Export a summary report (CSV) which contains the above metrics.
Non Functional requirements:
- Simple/ easy to understand desktop UI using JavaFX.
- No remote services; all data is stored locally opposed to in the cloud.
- Easy to install on the common desktop OS (Windows/Linux/Mac) with Java 11+ and JavaFX.

Installation & Dependencies (Linux)

Repository:
URL: https://github.com/N0tAFarm3r/MoneyMap-A-Personal-Expense-Tracking-System

Quick summary:

Install Java and Maven (make sure latest), 
install JavaFX SDK (or let the Maven plugin manage it), 
git clone the repo,
From the project pom.xml file, adjust based on your java/ maven version.
from the project root run mvn clean javafx:run.

 Clone the repo
Open a terminal and run:

git clone https://github.com/N0tAFarm3r/MoneyMap-A-Personal-Expense-Tracking-System.git
cd MoneyMap-A-Personal-Expense-Tracking-System/moneymainmap
mvn clean javafx:run

Prerequisites
Java verification:
java -version
If you don’t have Java OpenJDK.
Maven (recommended to build & run):
mvn -v
If mvn is missing, install it

4. End user Use

How to run:

Start the application via mvn clean javafx:run.
Main menu: use the top menu to go to Add Income, Add Expense, and view reports screens.
Add Income: enter amount, description and pick a date after press Save the entry is then recorded to income_data.csv and the list shows the entry.
Add Expense: the same flow, saves to expense_data.csv.
View Reports: navigate to the View Reports screen to see Total Income, Total Expenses, Net, Average Income, Average Expense.
Export Reports: pressing Export Reports will open a Save option, simply save to you computer
Deletion of Entries: If you want to delete an entry, simply right click the entry you applied and a delete box will appear; click delete and it will delete from the .csv file.

5. Test Process and Results

Manual tests performed:
- Add Income: manually added sample income rows and verified income_data.csv contains lines like 100,Salary,on2025-10-01, after tested deleting entries.
- Add Expense: added sample expense rows and verified expense_data.csv, after tested deleting entries.
- View Reports: navigated to the View Reports screen and confirmed totals and averages matched manual calculations.
- Export Reports: used the Export Reports button to save a CSV and opened it to verify the summary lines.

6. Future work and Improvements.

I would like to  plan to incorporate the already existing ‘Settings’ button as a way to change different themes in the app other than the black and yellow style theme. I think this would be a nice way to add in more customization.
