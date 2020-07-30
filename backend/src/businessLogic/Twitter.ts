import {CreateTweetRequest} from "../requests/CreateTweetRequest";
import {Tweet} from "../models/Tweet";
import * as uuid from 'uuid'
import {TwitterAccess} from "../dataLayer/TwitterAccess";
import {CreateUserRequest} from "../requests/CreateUserRequest";
import {User} from "../models/User";

const twitterAccess = new TwitterAccess()

export async function createTweet(userId: string, newTodo: CreateTweetRequest): Promise<Tweet> {
    const newId = uuid.v4()
    return twitterAccess.createTweet({
        id: newId,
        userId: userId,
        tweet: newTodo.tweet,
        createdAt: new Date().toISOString(),
        likeList: []
    })
}

export async function createUser(userId: string, newUser: CreateUserRequest): Promise<User> {
    return twitterAccess.createUser({
        userId: userId,
        name: newUser.name,
        email: newUser.email,
        imageUrl: newUser.imageUrl,
        isVerified: newUser.isVerified,
        followerList: [],
        followingList: []
    })
}

export async function getAllTweets(): Promise<Tweet[]> {
    return twitterAccess.getAllTweets()
}

export async function getTweetsByUser(userId: string): Promise<Tweet[]> {
    return twitterAccess.getTweetsByUser(userId)
}

export async function getUserCountById(userId: string): Promise<AWS.DynamoDB.QueryOutput> {
    return twitterAccess.getUserCountById(userId)
}