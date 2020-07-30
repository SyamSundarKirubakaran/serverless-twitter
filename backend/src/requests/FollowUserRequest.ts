/**
 * if you follow someone,
 * say,
 * you are -> `userId`
 * the person you followed -> `targetUserId`
 *
 * then the followingCount & followingList of `userId` will be altered
 * and the followerCount & followerList of `targetUserId` will be altered
 *
 * therefore, one change impacts 2 users
 */
import {LightWeightUser} from "../models/LightWeightUser";

export interface FollowUserRequest {
    // user_id -> from bearer token -> following
    followingCount: number,
    followingList: Array<LightWeightUser>
    targetUserId: string, // followers
    followerCount: number,
    followerList: Array<LightWeightUser>
}