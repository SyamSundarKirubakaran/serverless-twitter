import 'source-map-support/register'

import {APIGatewayProxyEvent, APIGatewayProxyResult, APIGatewayProxyHandler} from 'aws-lambda'
import {getUserId} from "../utils";
import {createLogger} from "../../utils/logger";
import {S3Util} from "../../utils/s3Util";
import {GeneralUtil} from "../../utils/general";
import {getTweetById} from "../../businessLogic/Twitter";
import {Tweet} from "../../models/Tweet";

const s3Util = new S3Util()
const generalUtil = new GeneralUtil()
const logger = createLogger('url')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {
    const userId = getUserId(event)

    const tweetId = event.pathParameters.tweetId

    const item = await getTweetById(tweetId)
    if (item.Count == 0) {
        logger.error(`no tweet found with the id ${tweetId}`)
        return generalUtil.errorResponse(400, "Tweet doesn't exists")
    }

    const resultCasted: Tweet = JSON.parse(JSON.stringify(item.Items[0]))
    if (resultCasted.userId !== userId) {
        logger.error(`unauthorized update event`)
        return generalUtil.errorResponse(400, `${userId} is not authorized to delete ${tweetId}`)
    }

    if(resultCasted.imageUrl != undefined){
        logger.error(`tweet ${tweetId} already has an image`)
        return generalUtil.errorResponse(400, `${tweetId} already has image`)
    }

    const url = s3Util.getPreSignedUrl(tweetId)
    return generalUtil.successResponse(200, "uploadUrl", url)
}
