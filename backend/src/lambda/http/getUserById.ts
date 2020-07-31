import 'source-map-support/register'

import {APIGatewayProxyEvent, APIGatewayProxyResult, APIGatewayProxyHandler} from 'aws-lambda'
import {getUserId} from "../utils";
import {createLogger} from '../../utils/logger'
import {GeneralUtil} from "../../utils/general";
import {getUserById} from "../../businessLogic/Twitter";

const generalUtil = new GeneralUtil()
const logger = createLogger('user')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {
    const userId = getUserId(event)
    const authType = event.pathParameters.authType
    const userIdPart = event.pathParameters.userId

    logger.info(`Get ${authType}|${userIdPart} User invoked by user ${userId}`)
    const result = await getUserById(`${authType}|${userIdPart}`)

    return generalUtil.successResponse(200, 'result', result.Items[0])
}
