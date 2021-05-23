# cowin-alerter
Spring Boot application for alerting on COVID vaccine availability in India. <br>
Run the application on your local machine, it hits the <a href = "https://apisetu.gov.in/public/marketplace/api/cowin">COWIN Public API</a> at fixed intervals and 
plays a beep sound when there is a vaccine slot available, and a second beep if more than certain vaccines are available.
Currently, the check is set for districts in Delhi.
