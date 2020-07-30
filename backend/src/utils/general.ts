import {APIGatewayProxyResult} from "aws-lambda";

export class GeneralUtil {
    successResponse(statusCode: number, key: string, items: any): APIGatewayProxyResult {
        return {
            statusCode: statusCode,
            headers: {
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify({
                [key]: items
            })
        }
    }

    dubResponse(statusCode: number): APIGatewayProxyResult {
        return {
            statusCode: statusCode,
            headers: {
                'Access-Control-Allow-Origin': '*'
            },
            body: null
        }
    }

    errorResponse(statusCode: number, message: string): APIGatewayProxyResult {
        return {
            statusCode: statusCode,
            headers: {
                'Access-Control-Allow-Origin': '*'
            },
            body: JSON.stringify({
                message
            })
        }
    }
}