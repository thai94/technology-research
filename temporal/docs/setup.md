### Installation

Using docker compose to install temporal:
https://github.com/temporalio/docker-compose

```sh
git clone https://github.com/temporalio/temporal.git
cd temporal
docker-compose -f docker-compose-mysql.yml up -d
```
Exposed endpoints:
- http://localhost:8080 - Temporal Web UI