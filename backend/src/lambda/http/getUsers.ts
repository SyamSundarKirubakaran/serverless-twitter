import 'source-map-support/register'

import {APIGatewayProxyEvent, APIGatewayProxyResult, APIGatewayProxyHandler} from 'aws-lambda'
import {getUserId} from "../utils";
import {createLogger} from '../../utils/logger'
import {GeneralUtil} from "../../utils/general";
import {getAllUsers} from "../../businessLogic/Twitter";

const generalUtil = new GeneralUtil()
const logger = createLogger('user')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {
    const userId = getUserId(event)

    logger.info(`Get All Users invoked by user ${userId}`)
    const result = await getAllUsers()

    return generalUtil.successResponse(200, 'result', result)
}
