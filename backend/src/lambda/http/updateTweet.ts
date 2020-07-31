import 'source-map-support/register'

import {APIGatewayProxyEvent, APIGatewayProxyResult, APIGatewayProxyHandler} from 'aws-lambda'
import {getUserId} from "../utils";
import {createLogger} from '../../utils/logger'
import {GeneralUtil} from "../../utils/general";
import {getTweetById, updateTweet} from "../../businessLogic/Twitter";
import {LikeTweetRequest} from "../../requests/LikeTweetRequest";
import {Tweet} from "../../models/Tweet";

const generalUtil = new GeneralUtil()
const logger = createLogger('tweet')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {
    const userId = getUserId(event)
    const tweetId = event.pathParameters.tweetId
    const newRequest: LikeTweetRequest = JSON.parse(event.body)

    const foundTweet = await getTweetById(tweetId)

    if(foundTweet.Count == 0) {
        logger.info(`User ${userId} is trying to update a tweet with id: ${tweetId} which doesn't exist`)
        return generalUtil.errorResponse(400, `Tweet with id: ${tweetId} not found`)
    }

    logger.info(`Found Tweet: ${foundTweet.Items[0]}`)
    let tweetCast: Tweet = JSON.parse(JSON.stringify(foundTweet.Items[0]))

    await updateTweet(tweetId, tweetCast.createdAt, newRequest)

    logger.info(`User ${userId} has updated the tweet ${tweetId}`)
    return generalUtil.dubResponse(200)
}
