printf "1. 1번 3333 계좌 생성"
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/create/3333 --data-urlencode "date=2018-05-20T09:00:00.000"

printf "\n====================================="
printf "\n2.1 1번 3333 생성 후, 7일 이내 10만원 이상 금액 4회 입금"

curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/3333/receive \
--data-urlencode "date=2018-05-24T09:00:00.000" \
--data-urlencode "beforeBalance=400000" \
--data-urlencode "fromAccount=4444" \
--data-urlencode "fromId=4" \
--data-urlencode "amount=100000"

curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/3333/receive \
--data-urlencode "date=2018-05-25T09:00:00.000" \
--data-urlencode "beforeBalance=2000000" \
--data-urlencode "fromAccount=5555" \
--data-urlencode "fromId=5" \
--data-urlencode "amount=110000"

curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/3333/receive \
--data-urlencode "date=2018-05-26T09:00:00.000" \
--data-urlencode "beforeBalance=300000" \
--data-urlencode "fromAccount=4444" \
--data-urlencode "fromId=4" \
--data-urlencode "amount=200000"

curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/3333/receive \
--data-urlencode "date=2018-05-26T10:00:00.000" \
--data-urlencode "beforeBalance=200000" \
--data-urlencode "fromAccount=4444" \
--data-urlencode "fromId=4" \
--data-urlencode "amount=100000"

printf "\n2.2 1번 3333 생성 후, 7일 1분 이후 10만원 이상 금액 1회 입금"
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/3333/receive \
--data-urlencode "date=2018-05-27T09:00:01.000" \
--data-urlencode "beforeBalance=200000" \
--data-urlencode "fromAccount=4444" \
--data-urlencode "fromId=4" \
--data-urlencode "amount=100000"

printf "\n2.3 1번 3333 계좌 status\n"
curl http://localhost:8080/v1/1/3333/status

printf "\n====================================="
printf "\n3.1 1번 3333 계좌 RuleB fraud 안나옴\n"
curl http://localhost:8080/v1/fraud/1

printf "\n3.2 1번 3333 생성 후, 7일 이내 10만원 이상 금액 1회 입금"
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/3333/receive \
--data-urlencode "date=2018-05-27T09:00:00.000" \
--data-urlencode "beforeBalance=200000" \
--data-urlencode "fromAccount=4444" \
--data-urlencode "fromId=4" \
--data-urlencode "amount=100000"

printf "\n3.3 1번 3333 계좌 RuleB fraud 나옴\n"
curl http://localhost:8080/v1/fraud/1