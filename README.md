# personal-finance
Personal Finance JavaB Project

## set up postgres

``` bash
sudo docker run --name postgres -e POSTGRES_DB=finance_mgmt -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15
```

go to http://localhost:8080/swagger-ui/index.html to access swagger documentation