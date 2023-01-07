# l0-index

**Purpose**: A searchable index of chains, blocks, and transactions on the TIKI network.

**Why?**: Performant, API-driven lookups, network-wide without the need for a local tiki client.

**How**: l0-storage will submit a report (block id, transaction id, etc.) on each block write. l0-index will normalize the report data into searchable tables accessible via simple REST APIs. 

*Important Note: The l0-index does not care about/index against the transaction content. The l0-index is an index of the network (a list of blockchains)*

## Requirements:

### Infrastructure:
A stateless spring-boot microservice, a hosted Postgres database,  and a round-robin load balancer. 

Deployed as a Docker container via GH actions calling terraform scripts. Performance and error monitoring handled by Sentry. 

See [l0-auth](https://github.com/tiki/l0-auth) for an example. 

### Data Structure: 
*Note: skeleton framework only, not to be taken as final table structure*

<img width="674" alt="image" src="https://user-images.githubusercontent.com/3769672/211174385-f8dd0926-6e7d-4421-810f-276606fcdc3d.png">

### APIs

`GET /app/{id}`  
Parameters: `aid`   
Returns: List of `addresses`

`GET /app/address/{address}`  
Parameters: `address`  
Returns: List of block hashes

`GET /app/address/block/{hash}`  
Parameters: block `hash` aka block_id  
Returns: Deserialized block
  
`GET /app/address/block/transaction/{hash}`  
Parameters: transaction `hash` aka transaction_id   
Returns: Deserialized transaction

`POST /report`  
Parameters: `aid`, `address`, `block_hash` and a list of `transaction_hashes`  
Returns `200 OK`

### Security
- GET request(s) require a valid JWT with the index.l0.mytiki.com audience.
- POST /report request to require a secret key injected at runtime via Cloudflare Worker secrets —the request will be sent from the serverless l0-storage worker, and temporary credential grants would add significant overhead.
