import 'source-map-support/register'

import {APIGatewayProxyEvent, APIGatewayProxyHandler, APIGatewayProxyResult} from 'aws-lambda'
import {createLogger} from "../../utils/logger";
import {CreateTweetRequest} from "../../requests/CreateTweetRequest";
import {GeneralUtil} from "../../utils/general";
import {getUserId} from "../utils";
import {createTweet} from "../../businessLogic/Twitter";

const generalUtil = new GeneralUtil()
const logger = createLogger('tweet')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {
    const newTweet: CreateTweetRequest = JSON.parse(event.body)

    const userId = getUserId(event)
    const item = await createTweet(userId, newTweet)
    logger.info(`New Tweet posted: ${newTweet.tweet}, by user: ${userId}`)

    return generalUtil.successResponse(201, 'result', item)
}
