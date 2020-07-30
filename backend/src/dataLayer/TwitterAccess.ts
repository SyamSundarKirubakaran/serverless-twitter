import * as AWS from 'aws-sdk'
import {DocumentClient} from 'aws-sdk/clients/dynamodb'
import {Tweet} from "../models/Tweet";
import {User} from "../models/User";

export class TwitterAccess {

    constructor(
        private readonly docClient: DocumentClient = new AWS.DynamoDB.DocumentClient(),
        private readonly tweetTable = process.env.TWEET_TABLE,
        private readonly userTable = process.env.USER_TABLE,
        private readonly userIndexTable = process.env.USER_ID_INDEX) {}

    async createTweet(item: Tweet): Promise<Tweet> {
        await this.docClient.put({
            TableName: this.tweetTable,
            Item: item
        }).promise()
        return item
    }

    async createUser(item: User): Promise<User> {
        await this.docClient.put({
            TableName: this.userTable,
            Item: item
        }).promise()
        return item
    }

    async getAllTweets(): Promise<Tweet[]> {
        const result = await this.docClient.scan({
            TableName: this.tweetTable,
        }).promise()
        return result.Items as Tweet[]
    }

    async getTweetsByUser(userId: string): Promise<Tweet[]> {
        const result = await this.docClient.query({
            TableName : this.tweetTable,
            IndexName : this.userIndexTable,
            KeyConditionExpression: 'userId = :userId',
            ExpressionAttributeValues: {
                ':userId': userId
            }
        }).promise()
        return result.Items as Tweet[]
    }

    async getUserCountById(userId: string): Promise<AWS.DynamoDB.QueryOutput> {
        return await this.docClient.query({
            TableName: this.userTable,
            KeyConditionExpression: 'userId = :userId',
            ExpressionAttributeValues: {
                ':userId': userId
            }
        }).promise()
    }

}