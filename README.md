# Cornershop test app

![Cornershop logo](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQRTlHsWXpc4SoMedkI5ZrZEKMmEczR_kk4sXHCOKR721mDJv--7a2i-EZl5XHZynsnz-Y&usqp=CAU)


This repository contains an Android Application with a challenge to demonstrate part of my knowledge like Android Developer.

### Challenge Description

Create an Android app for counting things. You'll need to meet high expectations for quality and functionality. It must meet at least the following:

### Design 
Build this app using the [following spec](https://www.figma.com/file/qBcG5Poxunyct1HEyvERXN/Counters-for-Android)

### Result

   ![video_1](https://i.imgur.com/0AVhSrM.gif)
   ![video_2](https://i.imgur.com/a4nzngi.gif)
   
   
### Installation

1. Clone the repository
2. Open the project in Android Studio
3. Go to file named local.properties and create a variable called `API_URL` like the following example:
![Cornershop logo](https://i.imgur.com/UwG5fzG.png)
4. Set the API URL of the API service
	* **From webserver** `https://cornershop-toy-backend-2021.herokuapp.com/api/v1/`
	* **From local server** `http://127.0.0.1:3000/api/v1`
5. Menu option build -> Clean Project. This step creates `BuildConfig.API_URL` value to use in the application.
6. Run the project and start to use it.

### Notes
1. Webserver deployed on Heroku is the same provided in the test folder
2. If you use a local server, avoid using `http://localhost:3000`. It's better to replace it with `127.0.0.1`. Application has problems using localhost. 


### Regards
