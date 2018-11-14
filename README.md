# Concord

> concord. noun. agreement or harmony between people

Concord is a web application designed to easily crowd source labelling of text data for NLP. It's focus is on labeling text for training text classification models such as in chatbots.

 **NOTE: This is still a work in progress**

## get up and running (for developers)

### 1. Create database

```
docker run --rm -it -p 3306:3306 -e MYSQL_DATABASE=concorddb -e MYSQL_USER=concorduser -e MYSQL_PASSWORD=concordpwd -e MYSQL_ROOT_PASSWORD=password123 -d mysql:5.7
```

### 2. Setup  RASA NLU Server

By default Concord is configured to use a Rasa NLU server for testing. See *concord-server/server/src/main/server.yml*

#### 2a. Start server
```
docker run -p 5000:5000 rasa/rasa_nlu:latest-full
```

#### 2b. Populate RASA NLU server
In a new terminal window:
```
curl -XPOST --header "Content-Type: application/json" localhost:5000/train?project=taxibot -d @testbed/taxibotdata.json
```

#### 2c. Check status of model build
```
curl localhost:5000/status
```
When status of taxibot model is listed as ready, execute:
```
curl -XPOST localhost:5000/parse -d '{"q":"get me my taxi", "project": "taxibot"}'
```
**NOTE: Wait for this response to return before running other queries against the Rasa NLU server. May take several minutes.**

### 3. Create database and Start up server
In a new terminal window:

#### 3a. Create the database
```
cd concord-server/server
./gradlew createDatabase
```

#### 3b. Run server

```
cd concord-server/server
./gradlew run
```

### 4. Load test data

#### 4a. Get Token for API calls
By default Concord is configured to use a list of usernames/passwords held in it's config file. See *concord-server/server/src/main/server.yml*

```
curl -v -X POST http://127.0.0.1:9000/api/sessions --header "Content-Type: application/json" --data '{"userId":"Bob","password":"secret"}'
```
Extract token from response and use in place of XXXXXX in the following commands.

#### 4b. Load classification labels
```
curl -v -X POST http://127.0.0.1:9000/api/labels/bulk --header "Content-Type: text/csv" --header "Authorization: Bearer XXXXXX" --data-binary '@testbed/labels.csv'
```

#### 4c. Load unlabelled training data
```
curl -v -X POST http://127.0.0.1:9000/api/phrases/bulk --header "Content-Type: text/csv" --header "Authorization: Bearer XXXXXX" --data-binary '@testbed/unlabelled_phrases.csv'
```

### 5. Install and start up front end
In a new terminal window:

#### 5a. Install dependencies
```
cd concord-server/
npm install
```

#### 5b. Start up front end
```
cd concord-server/
npm start
```

### 5c. Login
Login as **bob**, password **secret** or as **alice**, password **garden**



