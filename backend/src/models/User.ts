import {LightWeightUser} from "./LightWeightUser";

export interface User {
    userId: string,
    name: string,
    email: string,
    imageUrl: string,
    isVerified: boolean,
    followerList: Array<LightWeightUser>,
    followingList: Array<LightWeightUser>
}