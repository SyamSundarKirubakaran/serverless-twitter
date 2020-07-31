import 'source-map-support/register'

import {APIGatewayProxyEvent, APIGatewayProxyResult, APIGatewayProxyHandler} from 'aws-lambda'
import {getUserId} from "../utils";
import {createLogger} from '../../utils/logger'
import {GeneralUtil} from "../../utils/general";
import {getAllTweets} from "../../businessLogic/Twitter";
import {S3Util} from "../../utils/s3Util";

const s3Util = new S3Util()
const generalUtil = new GeneralUtil()
const logger = createLogger('tweet')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {
    const userId = getUserId(event)

    logger.info(`Get All Tweets invoked by user ${userId}`)
    const result = await getAllTweets()

    for (const record of result) {
        record.imageUrl = await s3Util.getAttachmentUrl(record.id)
    }

    return generalUtil.successResponse(200, 'result', result)
}
