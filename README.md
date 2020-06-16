Digital portfolio application
As a backend developer you have been assigned to design and implement the backend part of an application for a digital portfolio allowing payments between its users.
Functional requirements
The application will be an internet site that will allow payments to happen between the users of the site.
There are two types, or profiles of users:
A.	Consumers
B.	Merchants
Depending of the profile of the user are available different transactions. Basically, we need the consumers to be able to pay for services or merchandises to the merchants.
Consumers
We want the consumers to be able to create their own login account in the system. They must setup a username and a password during this step.
Then, the consumers must be able to deposit money to their portfolio using a bank card number. The aspects of the validation of this transaction remain out of scope of this project. In reality the system will call an external provider to authorize the transaction.
We want the consumers to be able to do money transfers to other consumers.
Consumers will want also to see the history of the transactions in their portfolio as well as the current balance.
Merchants
We want the merchants to be able to create their own accounts in the system the same way the consumers do.
They need to be able to add one or more bank accounts to their portfolio. Those accounts will be used for withdrawal of money.
When there is a purchase, the merchant needs to be able to initiate a funds transfer from the consumer’s portfolio into their own. This transaction will debit the consumer’s portfolio and debit the merchant’s one.
We need also to have the opposite transaction (reimbursement) when the merchant needs to return money to the consumer.
The merchants need to be able to withdraw money from their portfolio and transfer them to one of their registered bank accounts. You may consider that the wire transfer is just a call to external service.
Finally, merchants will also want to see the history of the transactions in their portfolio as well as the current balance.

Administrators
Yes, we need administrators who can lock/unlock accounts.
 
Technical requirements
Use the following technology stack:
•	Spring Boot as a whole.
•	Spring JDBC for persistence.
•	TestNG for testing.
•	SQL Server/In-memory database for data storage. You can use H2 database as a start. Depending of the progress and the time schedule we will try to migrate to Oracle later.
•	Maven
•	The code must be written in Java 8.
•	Logging - Logback or another framework
•	Assume that we can have lots of concurrent requests

NOTICE !
If you want to send email , change your google account less security to enable
  https://myaccount.google.com/lesssecureapps
