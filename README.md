# Mini-Shopify

SYSC 4806 term project: a simplified Shopify built with Spring Boot, deployed on Azure.

Merchants create shops (name, description, categories/tags) and add products with a description, picture and inventory count. Customers search for shops by name or tag, browse a shop's catalog, add products to a cart and check out. Checkout is simulated, and a purchase can't exceed the available inventory.

**Live app:** 
https://mini-shopify-4806-fme9dbcfcmfjbuaw.norwayeast-01.azurewebsites.net/. Now shows that our controller is on and we have something on the webpage.
**Kanban board:** 
https://github.com/users/gabrielbugarija/projects/1/views/1

## Tech stack

- Java, Spring Boot
- Spring Data JPA with Hibernate
- Thymeleaf 
- Maven, GitHub Actions, Azure App Service

## Running locally

**Prerequisites:** JDK 17+ and Maven 3.9+.

```bash
git clone https://github.com/gabrielbugarija/mini-shopify-4806.git
cd mini-shopify-4806
mvn spring-boot:run
```

Then open http://localhost:8080.

- **Shopper view:** http://localhost:8080/ (browse and search shops, buy products)
- **Merchant view:** http://localhost:8080/merchant (create shops and add products)

**Run the tests:** `mvn test`

**Database:** data is saved to an H2 file database in `$HOME/mini-shopify-data/`, so it persists between runs. If `HOME` isn't set (common on Windows outside Git Bash), the folder is created in the project directory instead. Delete that folder to start fresh. To inspect it, go to http://localhost:8080/h2-console and connect with:

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:file:<that folder>/minishopify;AUTO_SERVER=TRUE` (e.g. `jdbc:h2:file:~/mini-shopify-data/minishopify;AUTO_SERVER=TRUE`) |
| User Name | `sa` |
| Password | *(blank)* |

**Troubleshooting:** shop names must be unique (ignoring case and surrounding spaces), and this is enforced by `src/main/resources/schema.sql` on every startup. If the app fails to start with an error mentioning `UX_SHOP_NAME`, your existing database has duplicate shop names from before this rule was added. Either delete the `mini-shopify-data` folder, or (with the app stopped, using a standalone H2 client) rename or delete the duplicate shops, then start the app again.

## Team

| Member | Current focus |
|---|---|
| James Noel | x |
| Gabriel Bugarija | x |
| Yuvraj Bains | x |
| Declan Koster | x |
| Pranav Gupta | x |

## Current state (Milestone 1)

- System contains a working front end for creating and viewing shops and products
- Working back end with persistence
- System tests creating and manipulating shops and persistence


## Plan for next sprint (Milestone 2)

- Increased system usability
- New useful features while maintaining clean code quality 

## Plan for next sprint (Milestone 3)

- Login authentication
- Allow for search with query typos (error distance)
- Add shopping carts for shoppers
- JavaScript frontend modifications
- Increased system usability
- New useful features while maintaining clean code quality

## High level - Domain model
<img width="1127" height="532" alt="image" src="https://github.com/user-attachments/assets/93dc0e40-affe-49b8-8453-7ee0e11d4fd4" />