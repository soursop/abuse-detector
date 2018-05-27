abuse-detector
========

Spring Boot Rest Api for detecting for fraud pattern.

How to use
========
 - Support java version : 8
 - Support gradle version : greater than 3.X for spring boot

Run
========
- Build
```
cd {projectRoot}
gradle clean bootJar
```
- Execute 
```
java -jar {projectRoot}/build/libs/gs-rest-service-0.1.0.jar 
```
    
Test
========
- Rest API test bash shell    
```
bash {projectRoot}/src/test/resources/RuleA.sh
bash {projectRoot}/src/test/resources/RuleB.sh
bash {projectRoot}/src/test/resources/RuleC.sh
```
- Rule Engine Test case
```
test/java/com.abuse.RuleTest
```

Description
========
 - com.abuse.rule : rule engine
 - com.abuse.module : service layer object
 - com.abuse.Application : main class
 
API
========
- Date Format : 2018-05-26T09:00:59.000
 
- GET : Check Fraud User
```
curl http://localhost:8080/v1/fraud/{userId}
```
- PUT : Add Send Money Log
```
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/{userId}/{account}/send \
--data-urlencode "date={eventDate}" \
--data-urlencode "fromBalance={balance}" \
--data-urlencode "toAccount={toAccount}" \
--data-urlencode "toId={toId}" \
--data-urlencode "amount={amount}"
```
- PUT : Add Charge Money Log
```
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/{userId}/charge/{account} \
--data-urlencode "date={eventDate}" \
--data-urlencode "amount={amount}" \
--data-urlencode "bankAccount={bankAccount}"

```
- PUT : Add Create Account Log
```
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/{userId}/create/{account} --data-urlencode "date={createDate}"

```
- PUT : Add Receive Money Log
```
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/{userId}/{account}/receive \
--data-urlencode "date={eventDate}" \
--data-urlencode "beforeBalance={beforeBalance}" \
--data-urlencode "fromAccount={fromAccount}" \
--data-urlencode "fromId={fromId}" \
--data-urlencode "amount={amount}"
```

Rule Engine Interface
========

 - com.abuse.Rulable
   - rule를 규정하기 위한 기본 인터페이스. 
   - 기본적으로 모든 Rule은 Rulable을 상속받음. Rulable을 상속받음으로써 재귀적으로 자료구조 정의가 가능함.
      
 - com.abuse.rule.Terminal
   - 재귀적인 자료구조에서 가장 마지막 단말 조건
   - Evaluation 객체를 가지고, 로그에서 직접적으로 해당 Rule이 유효한지 아닌지 확인함.
   - 모든 Terminal 객체는 frequency(빈도) 와 duration(유효기간)을 가짐.
   - 기본적으로 Teminal 객체는 timestamp 기반으로 유효기간을 가진다. 기본적으로는 현재 시간으로 부터 유효기간를 가지게 되고, 유효기간이 지나면 로그를 저장하는 queue에서 삭제되게 된다.
   - 만약 상위 Rule이 Sequencial한 rule이라면 현재 시간 기준으로 유효기간을 판정하지 않는다. 대신 유효기간이 기준이 삽입될 때, 해당 시간을 기준으로 유효기간을 산정한다.
 
 - com.abuse.rule.Rules
   - 여러개의 Rulable를 자식으로 가진 객체
   - Conjunction을 가지고 있는데, Conjunction 성격에 따라 하위 조건의 조합이 Rule에 유효한 조건인지 아닌지를 판단한다. 
 
 - com.abuse.rule.Aggregator
   - 로그를 저장하고 전체 rule의 반복횟수를 수집한다.
   - 전체 Rule을 소유하고 있으며, 실시간으로 현재까지 수집된 로그 기반으로 감지되는 rule를 반환한다.