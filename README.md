<p align="center">
  <img src="docs/img/serverless_banner.jpg">
</p>

### Description
This repository contains code that helps you build a twitter clone and helps you deploy it to cloud as FaaS .i.e., Serverless Twitter. Take a look at [setup.md](https://github.com/SyamSundarKirubakaran/serverless-twitter/blob/master/docs/setup.md) to learn how to setup this project. You can also find two mobile clients consuming these serverless APIs .i.e., **Android**(v0.5) and **iOS**(WIP).

### Functions

| Purpose  | Method | Endpoint |
| ------------- | ------------- | ------------- |
| Create User   | POST  | https://{{apiId}}.execute-api.us-east-1.amazonaws.com/dev/user  |
| Create Tweet  | POST  | https://{{apiId}}.execute-api.us-east-1.amazonaws.com/dev/tweet  |
| Get All Tweets   | GET  | https://{{apiId}}.execute-api.us-east-1.amazonaws.com/dev/tweet  |
| Get Tweets by Specific User  | GET  |  https://{{apiId}}.execute-api.us-east-1.amazonaws.com/dev/tweet/specific/{authType}/{userId}  |
| Like / Unlike a Tweet   | PATCH  |  https://{{apiId}}.execute-api.us-east-1.amazonaws.com/dev/tweet/{tweetId}  |
| Follow / Unfollow an User   | PATCH  | https://{{apiId}}.execute-api.us-east-1.amazonaws.com/dev/user  |
| Get All Users   | GET  | https://{{apiId}}.execute-api.us-east-1.amazonaws.com/dev/user |
| Get Specific User   | GET  | https://{{apiId}}.execute-api.us-east-1.amazonaws.com/dev/user/specific/{authType}/{userId}  |
| Delete Tweet  | DELETE  | https://{{apiId}}.execute-api.us-east-1.amazonaws.com/dev/tweet/{tweetId}  |
| Get Signed URL for Image Upload   | GET  | https://{{apiId}}.execute-api.us-east-1.amazonaws.com/dev/tweet/signedUrl/{tweetId}  |

ps: replace `us-east-1` with your aws deployment region.

You can also import the provided Postman Collection to take a quick look at the exposed endpoints. Also make sure to fill up apiId and authToken environment variables to make endpoints intract with the deplpoyed serverless application.

**Screenshots**<br />
<img src="docs/img/one.png" height=520 width =270 />
<img src="docs/img/two.png" height=520 width =270 />
<img src="docs/img/three.png" height=520 width =270 />

### Architecture

<p align="center">
  <img src="docs/img/arch.jpg">
</p>
