# job-api

This project is a minimalist JSON HTTP API that exposes the following routes:
- `GET /jobs`: Returns a map of open positions in the job board.
- `POST /jobs`: Inserts a new open position in the job board and returns the updated map.
- `DELETE /jobs/:id`: Removes an open position from the job board and returns the updated map.


## Run
Start a server on port `8888`:
```sh
lein run 
```
