# trustly-integration
Bambora Code Test Integrating Trustly

## Task
Create a Spring Boot Web application, which displays a simple web page where the end-user can enter an amount and complete a Trustly deposit. Once the flow is completed a success or failure page should be displayed. Check in the code in Github and deploy a working web app on a cloud provider (use Heroku or similar free service).

### Challenges
1. Create Github repo
2. Get a Spring Boot Web application up and running
3. Create a HTML page + controller for initiating a Trustly deposit
4. Handle JSON request / response to Trustly deposit API
5. Handle client-side redirect
6. Receive and parse callback notification from Trustly
7. Create and verify Trustly signatures
8. Show success or failure page
9. Deploy solution to a cloud provider

### Documentation
* Deposit API: https://trustly.com/en/developer/api#/deposit 
* Notification: https://trustly.com/en/developer/api#/notifications 
* Signature: https://trustly.com/en/developer/api#/signature

## Solution
* Deployed in Heroku: https://trustly-integration.herokuapp.com/trustly-integration/deposit
* Step by step:
1. A simple web page was created with a Deposit form filled with test data.
2. After submitting the form, the operation id is registered in Redis along with a "PENDING" status and a new view is shown with the url received by the Trustly api to make the payment.
3. An AJAX call remains checking the status of the payment in Trustly every 5 seconds.
4. When the notification with the payment confirmation arrives at the backend, the status of the operation is changed to "OK" in Redis.
5. The AJAX call from step 3 will notice the state change and display the positive or negative response in the view.
