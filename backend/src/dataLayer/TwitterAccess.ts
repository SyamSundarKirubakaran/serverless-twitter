import * as AWS from 'aws-sdk'
import {DocumentClient} from 'aws-sdk/clients/dynamodb'
import {Tweet} from "../models/Tweet";
import {User} from "../models/User";
import {LikeTweetRequest} from "../requests/LikeTweetRequest";
import {FollowUserRequest} from "../requests/FollowUserRequest";

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

    async getAllUsers(): Promise<User[]> {
        const result = await this.docClient.scan({
            TableName: this.userTable,
        }).promise()
        return result.Items as User[]
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

    async getUserById(userId: string): Promise<AWS.DynamoDB.QueryOutput> {
        return await this.docClient.query({
            TableName: this.userTable,
            KeyConditionExpression: 'userId = :userId',
            ExpressionAttributeValues: {
                ':userId': userId
            }
        }).promise()
    }

    async getTweetById(tweetId: string): Promise<AWS.DynamoDB.QueryOutput> {
        return await this.docClient.query({
            TableName: this.tweetTable,
            KeyConditionExpression: 'id = :id',
            ExpressionAttributeValues: {
                ':id': tweetId
            }
        }).promise()
    }

    async updateTweet(id: string, createdAt: string, updateRequest: LikeTweetRequest) {
        await this.docClient.update({
            TableName: this.tweetTable,
            Key: {
                'id': id,
                'createdAt': createdAt,
            },
            UpdateExpression: 'set #likeListHash = :ll',
            ExpressionAttributeValues: {
                ':ll': updateRequest.likeList
            },
            ExpressionAttributeNames: {
                "#likeListHash": "likeList"
            }
        }).promise()
    }

    async updateUser(id: string, updateRequest: FollowUserRequest) {
        await this.docClient.update({
            TableName: this.userTable,
            Key: {
                'userId': id
            },
            UpdateExpression: 'set #followingListHash = :f',
            ExpressionAttributeValues: {
                ':f': updateRequest.followingList
            },
            ExpressionAttributeNames: {
                "#followingListHash": "followingList"
            }
        }).promise()

        await this.docClient.update({
            TableName: this.userTable,
            Key: {
                'userId': updateRequest.targetUserId
            },
            UpdateExpression: 'set #followerListHash = :f',
            ExpressionAttributeValues: {
                ':f': updateRequest.followerList
            },
            ExpressionAttributeNames: {
                "#followerListHash": "followerList"
            }
        }).promise()
    }

}