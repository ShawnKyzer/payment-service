# technical-test-payment-service
A service consumer designed to stream data from kafka topics to postgres database for persistence. It handles both online and offline payments. 


### :computer: How to execute

_Provide a description of how to run/execute your program..._

### :memo: Notes

_Some notes or explaination of your solution..._

### :pushpin: Things to improve

_If u have more time or want to improve somthing..._
1. I would change the producer to use protobuf directly so that the deserialization will be easier and also more compact as a byte[]
2. Would add in SonarQube, Jacoco, Snyke and google checkstyle as part of the final build CI/CD process
3. More unit testing for the kafka stream and better integration tests
3.