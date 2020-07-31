
// `user-id` from bearer token
import {LightWeightUser} from "../models/LightWeightUser";

export interface CreateTweetRequest {
    user: LightWeightUser,
    tweet: string
}