@echo off

set ADMIN_EMAIL=admin@examle.com
set ADMIN_PASSWORD=admin_password

set POSTGRES_USER=postgres
set POSTGRES_PASSWORD=postgres
set POSTGRES_USERINFO_PORT=5551
set POSTGRES_PET_PORT=5552
set POSTGRES_PERSON_PORT=5553
set POSTGRES_DB=PetProjectDB

set PGADMIN_DEFAULT_EMAIL=example@gmail.com
set PGADMIN_DEFAULT_PASSWORD=example_password

set JWT_SECRET=daf66e01593f61a15b857iuby24fb2g2iu9o397yf9r9v975nbh6a4sn761234e149036bcc8dee755dbb

docker compose up -d

pause