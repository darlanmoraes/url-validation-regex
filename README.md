## URL Validation Regex
This application is responsible for the validation of URLs using regex expressions. These expressions are loaded from a **MySQL** database that is populated through a **RabbitMQ** Queue. This application does not have REST APIs and is accessible using only **RabbitMQ**.
There are 3 queues(remember to always use the **property** `content_type=application/json`):
- `deadletter.queue`: Receives all messages that were not processed;
- `insertion.queue`: Responsible for the creation of new rules in the regex whitelist. A listener is attached to the queue and validates/creates new whitelist rules on **MySQL**;
   - Ex. 1: Invalid message, ends up in the deadletter. body = `{"client": "client1", "regex": "http://.***"}`;
   - Ex. 2: Valid message, creates a private whitelist for the client. body = `{"client": "client1", "regex": "http://darlan\\.com"}`;
   - Ex. 3: Valid message, creates a global whitelist for all clients. body = `{"client": "", "regex": "http://.*"}`;
- `validation.queue`: Receives messages with content to be validated against the whitelist. The validation result is sent to the exchange `response.exchange` with the route key `response.routing.key`. If the message has a whitelist match, the result will be sent with `match: true` and `regex: <REGEX_FOUND>`. If the message does not have a match, then it will send `match: false` and `regex: null`. The `correlationId` is always sent.
   - Ex. 1: Valid message, matches a client whitelist. body = `{"client": "client1", "url": "http://darlan.com", "correlationId": 123}`;
   - Ex. 2: Valid message, matches a global whitelist. body = `{"client": "client1", "url": "http://www.google.com", "correlationId": 123}`;
   - Ex. 3: Valid message, does not match client or global. body = `{"client": "client1", "url": "httpw://www.google.com", "correlationId": 123}`;
   - Ex. 4: Invalid message, ends up in the deadletter. body = `{"client": "", "url": "http://darlan.com", "correlationId": 123}`;

### Dependencies
- `java 1.8.0_241`;
- `maven 3.6.3`;
- `docker 19.03.6`;
- `docker-compose 1.25.4`;

### Starting
Run the following commands(the `docker-compose` file has **healthchecks** for **MySQL** and **RabbitMQ**, so the **Java** application will wait for them before the startup process):
```
git clone https://github.com/darlanmoraes/url-validation-regex.git
cd url-validation-regex
mvn install
docker-compose -f docker-compose.yml up --build
```

### Queue Access
Open the browser and paste: `http://localhost:15672/#/queues`;
- username: `guest`;
- password: `guest`;