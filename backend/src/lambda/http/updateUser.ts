import 'source-map-support/register'

import {APIGatewayProxyEvent, APIGatewayProxyResult, APIGatewayProxyHandler} from 'aws-lambda'
import {getUserId} from "../utils";
import {createLogger} from '../../utils/logger'
import {GeneralUtil} from "../../utils/general";
import {getUserById, updateUser} from "../../businessLogic/Twitter";
import {FollowUserRequest} from "../../requests/FollowUserRequest";

const generalUtil = new GeneralUtil()
const logger = createLogger('user')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {
    const userId = getUserId(event)
    const newRequest: FollowUserRequest = JSON.parse(event.body)

    const foundUser = await getUserById(userId)
    const foundTargetUser = await getUserById(newRequest.targetUserId)

    if(foundUser.Count == 0 || foundTargetUser.Count == 0) {
        logger.info(`Either User: ${userId} or Target User: ${newRequest.targetUserId} doesn't exist`)
        return generalUtil.errorResponse(400, `Unable to perform this action`)
    }

    await updateUser(userId, newRequest)

    logger.info(`User: ${userId} & ${newRequest.targetUserId} Updated`)
    return generalUtil.dubResponse(200)
}
