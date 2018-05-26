printf "1. 1번 5555 계좌 생성"
NOW=`date +%Y-%m-%dT%H:%M:%S.000`
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/create/5555 --data-urlencode "date=2018-05-20T09:00:00.000"

printf "\n====================================="
printf "\n2.1 1번 5555 2시간 이내 5만원 이상 받기 2회"

curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/5555/receive \
--data-urlencode "date=${NOW}" \
--data-urlencode "beforeBalance=400000" \
--data-urlencode "fromAccount=4444" \
--data-urlencode "fromId=4" \
--data-urlencode "amount=70000"

curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/5555/receive \
--data-urlencode "date=${NOW}" \
--data-urlencode "beforeBalance=2000000" \
--data-urlencode "fromAccount=5555" \
--data-urlencode "fromId=5" \
--data-urlencode "amount=50000"

printf "\n2.2 1번 5555 2시간 이후 5만원 이상 금액 1회 입금"
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/5555/receive \
--data-urlencode "date=2018-05-20T09:00:01.000" \
--data-urlencode "beforeBalance=200000" \
--data-urlencode "fromAccount=4444" \
--data-urlencode "fromId=4" \
--data-urlencode "amount=65000"

printf "\n2.3 1번 5555 계좌 status\n"
curl http://localhost:8080/v1/1/5555/status

printf "\n====================================="
printf "\n3.1 1번 5555 계좌 RuleC fraud 안나옴\n"
curl http://localhost:8080/v1/fraud/1

printf "\n3.2 1번 5555 2시간 이내 5만원 이상 금액 1회 입금"
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/5555/receive \
--data-urlencode "date=${NOW}" \
--data-urlencode "beforeBalance=200000" \
--data-urlencode "fromAccount=4444" \
--data-urlencode "fromId=4" \
--data-urlencode "amount=90000"

printf "\n3.3 1번 5555 계좌 RuleC fraud 나옴\n"
curl http://localhost:8080/v1/fraud/1