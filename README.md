# technical-test-payment-service
A service consumer designed to stream data from kafka topics to postgres database for persistence. It handles both online and offline payments. 

### :computer: How to execute

_Provide a description of how to run/execute your program..._

### :memo: Notes

_Some notes or explaination of your solution..._

### :pushpin: Things to improve
- I would change the producer to use protobuf directly so that the deserialization will be easier and also more compact as a byte[]
- Would add in SonarQube, Jacoco, Snyke and google checkstyle as part of the final build CI/CD process
- More unit testing for the kafka stream
- Update the producer, and all protos to store the currency as micro and add in currency type so as not to lose monetary precision and preserve the currency unit
- Add in integration tests using testcontainers for kafka etc