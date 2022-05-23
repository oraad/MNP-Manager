# MNP Manager

## Environment Setup

```bash
docker run --detach --name mysqldb \
    --env MYSQL_ROOT_PASSWORD='TZP398u$4PHtMyn#_nG83&hEa#6aDdyP' \
    --env MYSQL_USER='mnp_user' \
    --env MYSQL_PASSWORD='EVhX67XTt=@m3D8!5eHD' \
    --env MYSQL_DATABASE='mnp_manager' \
    -p 3306:3306 \
    mysql:8
```
