# Java-Data-Access
* The project use Maven in order to set up environment, Docker actually run PostgreSQL database and use DAO pattern when dealing with database.

## Prerequisites
1. Docker is installed
2. psql client is installed

## Actions

### Running PostgreSQL
1. Pull Docker Image
`docker pull postgres:9`

2. Build data directory
`mkdir -p ~/srv/postgres`

3. Run docker image
`docker run --name server-postgres --restart always -e POSTGRES_PASSWORD=password -d -v $HOME/srv/postgres:/var/lib/postgresql/data -p 5432:5432 postgres:9`

### Stopping PostgreSQL
`docker stop server-postgres`

### Logging into Database
* `psql -h localhost -U postgres -d hplussport`

### Creating starter data
1. `psql -h localhost -U postgres -f database.sql`
2. `psql -h localhost -U postgres -d hplussport -f customer.sql`
3. `psql -h localhost -U postgres -d hplussport -f product.sql`
4. `psql -h localhost -U postgres -d hplussport -f salesperson.sql`
5. `psql -h localhost -U postgres -d hplussport -f orders.sql`


### Creating stored procedure
1. `psql -h localhost -U postgres -f stored_proc.sql`
