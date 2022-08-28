# MNP Manager

## Environment Setup

### Requirements

- Java 11
- NodeJS 16
- MySQL 8

### Docker Mysql with user and db

```bash
docker run --detach --name mysqldb \
    --env MYSQL_ROOT_PASSWORD='TZP398u$4PHtMyn#_nG83&hEa#6aDdyP' \
    --env MYSQL_USER='mnp_user' \
    --env MYSQL_PASSWORD='EVhX67XTt=@m3D8!5eHD' \
    --env MYSQL_DATABASE='mnp_manager' \
    --env TZ='Europe/Istanbul' \
    -p 3306:3306 \
    --restart 'unless-stopped' \
    mysql:8
```

### Development Database

From a mysql client login as root

```sql
    CREATE DATABASE mnp_manager_dev;
    GRANT ALL PRIVILEGES ON mnp_manager_dev.* TO 'mnp_user';
```

### Backend

Run the backend server application using `Maven` with `dev` profile

The first run will create the required tables.
Using `mysql` import the data into the db from `/data/import_data.sql`

### Frontend

Go to the frontend directory and run the following command

```bash
    npm install
    npm run dev
```

## Project

### Directory Structure

```tree
├── data                # Initial database data
├── docs                # Documentation
├── frontend            # Web application
└── src                 # Spring boot src directory
```

### Database

![Database Schema](/docs/DBSchema.png "Database schema")

### Backend

#### Environments

The application uses three environment configuration files:

- Production: application.properties (src/main/resources)
- Development: application-dev.properties (src/main/resources)
- Test: application.properties (src/test/resources)

#### Updates

To check for updates, run:

```bash
    ./mvnw.cmd versions:display-parent-updates versions:display-property-updates
```

#### Testing

```bash
    ./mvnw.cmd test
```

#### Directory Structure

### Frontend Structure

#### Environments

The frontend uses three environment configuration files:

- Production: .env.production
- Development: .env.development
- Test: .env.test

#### Updates

To check for updates, run:

```bash
    npm run update
```

#### Testing

```bash
    npm run test        # Unit tests
    npm run test:e2e    # E2E tests
    npm run test:all    # Run all tests
    npm run coverage    # Generate coverage report
```

## Packaging

1- In frontend, run

```bash
    npm run build
```

to generate the frontend assets

2- After running build in frontend, run in the root directory

```bash
    ./mvnw.cmd package
```

This command will package the spring boot application and bundle the frontend as well

## TODO

### Backend TODO

### Frontend TODO
