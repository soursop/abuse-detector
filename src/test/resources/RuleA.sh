printf "1. 1번 1111 계좌 생성"
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/create/1111 --data-urlencode "date=2018-05-26T09:00:00.000"

printf "\n====================================="
printf "\n2.1 1번 1111 20만원 계좌 생성 1시간 1분 뒤 입금"
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/charge/1111 \
--data-urlencode "date=2018-05-26T10:00:01.000" \
--data-urlencode "amount=200000" \
--data-urlencode "bankAccount=1010"

printf "\n2.2 1번 1111 계좌 status => 20만원 rule 안나옴\n"
curl http://localhost:8080/v1/1/1111/status

printf "\n2.3 1번 1111 계좌 생성 1시간 뒤 20만원 입금"
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/charge/1111 \
--data-urlencode "date=2018-05-26T10:00:00.000" \
--data-urlencode "amount=200000" \
--data-urlencode "bankAccount=1010"

printf "\n2.4 1번 1111 계좌 status => Rule(AMOUNT:EQUALS:200000|1|3600000) 확인 가능\n"
curl http://localhost:8080/v1/1/1111/status

printf "\n====================================="

printf "\n3.1 1번 1111 계좌 잔액이 계좌 생성 59분 후, 1000원 이하"
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/1111/send \
--data-urlencode "date=2018-05-26T09:00:59.000" \
--data-urlencode "fromBalance=1010" \
--data-urlencode "toAccount=2222" \
--data-urlencode "toId=2" \
--data-urlencode "amount=10"

printf "\n3.2 1번 1111 계좌 fraud 안나옴\n"
curl http://localhost:8080/v1/fraud/1

printf "\n3.3 1번 1111 계좌 잔액이 계좌 생성 1시간 후, 1000원 이하"
curl -X PUT -H "charset=utf-8" http://localhost:8080/v1/1/1111/send \
--data-urlencode "date=2018-05-26T10:00:00.000" \
--data-urlencode "fromBalance=200000" \
--data-urlencode "toAccount=2222" \
--data-urlencode "toId=2" \
--data-urlencode "amount=199000"

printf "\n3.4 1번 1111 계좌 fraud 나옴\n"
curl http://localhost:8080/v1/fraud/1