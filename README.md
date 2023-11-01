# fujitsu

### 1. Open the project

### 2. Run FujitsuApplication

### 3. Refer to RESTdocumentation.pdf for more details

#### Download the pdf to copy text.

### The task:
CronJob for importing weather data
CronJob must be implemented to the code to request weather data from the weather portal of the
Estonian Environment Agency. The frequency of the cronjob has to be configurable. The default
configuration to run the CronJob is once every hour, 15 minutes after a full hour (HH:15:00).
URL to get data:
https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php
Additional information about the interface:
https://www.ilmateenistus.ee/teenused/ilmainfo/eesti-vaatlusandmed-xml/

As a result of the request, the weather data described in the database chapter has to be inserted
into the database for the following stations:
• Tallinn-Harku (city of Tallinn)
• Tartu-Tõravere (city of Tartu)
• Pärnu (city of Pärnu)
NB! The history of imported weather data has to be permanently stored. Therefore, new entries
must be inserted into the database as a result of each importing process (not overwrite existing
entries of the station).

Delivery fee calculation
A delivery fee has to be calculated according to input parameters from REST interface requests,
weather data from the database, and business rules. The total delivery fee consists of a regional
base fee for a specific vehicle types and extra fees for some weather conditions:

FUJITSU-PUBLIC Uncontrolled if printed 3 of 4 © Fujitsu 2023
Java Programming Trial Task 2023
Business rules to calculate regional base fee (RBF):
• In case City = Tallinn and:
• Vehicle type = Car, then RBF = 4 €
• Vehicle type = Scooter, then RBF = 3,5 €
• Vehicle type = Bike, then RBF = 3 €
• In case City = Tartu and:
• Vehicle type = Car, then RBF = 3,5 €
• Vehicle type = Scooter, then RBF = 3 €
• Vehicle type = Bike, then RBF = 2,5 €
• In case City = Pärnu and:
• Vehicle type = Car, then RBF = 3 €
• Vehicle type = Scooter, then RBF = 2,5 €
• Vehicle type = Bike, then RBF = 2 €
Business rules to calculate extra fees for weather conditions:
• Extra fee based on air temperature (ATEF) in a specific city is paid in case Vehicle type =
Scooter or Bike and:
• Air temperature is less than -10̊ C, then ATEF = 1 €
• Air temperature is between -10̊ C and 0̊ C, then ATEF = 0,5 €
• Extra fee based on wind speed (WSEF) in a specific city is paid in case Vehicle type = Bike
and:
• Wind speed is between 10 m/s and 20 m/s, then WSEF = 0,5 €
• In case of wind speed is greater than 20 m/s, then the error message “Usage of selected vehicle
type is forbidden” has to be given
• Extra fee based on weather phenomenon (WPEF) in a specific city is paid in case Vehicle
type = Scooter or Bike and:
• Weather phenomenon is related to snow or sleet, then WPEF = 1 €
• Weather phenomenon is related to rain, then WPEF = 0,5 €
• In case the weather phenomenon is glaze, hail, or thunder, then the error message “Usage of
selected vehicle type is forbidden” has to be given
NB!
Extra fees for weather conditions are paid only for conditions listed above.
Calculations must base on the latest weather data for a specific city.

Example calculation:
• Input parameters: TARTU and BIKE -> RBF = 2,5 €
• Latest weather data for Tartu (Tartu-Tõravere):
• Air temperature = -2,1̊ C -> ATEF = 0,5 €
• Wind speed = 4,7 m/s -> WSEF = 0 €
• Weather phenomenon = Light snow shower -> WPEF = 1 €
• Total delivery fee = RBF + ATEF + WSEF + WPEF = 2,5 + 0,5 + 0 + 1 = 4 €

REST interface
REST interface (endpoint), which enables other parts of the application to request delivery fees
according to the following input parameters:
• City: Tallinn / Tartu / Pärnu
• Vehicle type: Car / Scooter / Bike
In response to the request, the total delivery fee (calculated according to the description in the
chapter “Delivery fee calculation”) or an error message must be given.
REST interface must be documented.
