import 'source-map-support/register'

import {APIGatewayProxyEvent, APIGatewayProxyHandler, APIGatewayProxyResult} from 'aws-lambda'
import {createLogger} from "../../utils/logger";
import {GeneralUtil} from "../../utils/general";
import {getUserId} from "../utils";
import {createUser, getUserCountById} from "../../businessLogic/Twitter";
import {CreateUserRequest} from "../../requests/CreateUserRequest";

const generalUtil = new GeneralUtil()
const logger = createLogger('user')

export const handler: APIGatewayProxyHandler = async (event: APIGatewayProxyEvent): Promise<APIGatewayProxyResult> => {
    const newUser: CreateUserRequest = JSON.parse(event.body)

    const userId = getUserId(event)

    // before creating, check if the user already exists
    const existingUser = await getUserCountById(userId)

    if (existingUser.Count == 0) {
        const item = await createUser(userId, newUser)
        logger.info(`User create with ID: ${userId}`)
        return generalUtil.successResponse(201, 'result', item)
    } else {
        logger.info(`${userId} already exists`)
        return generalUtil.successResponse(200, 'result', existingUser.Items[0])
    }
}
