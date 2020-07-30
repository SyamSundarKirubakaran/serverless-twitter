import {LightWeightUser} from "./LightWeightUser";

export interface User {
    userId: string,
    name: string,
    email: string,
    imageUrl: string,
    isVerified: boolean,
    followerCount: number,
    followerList: Array<LightWeightUser>,
    followingCount: number,
    followingList: Array<LightWeightUser>
}