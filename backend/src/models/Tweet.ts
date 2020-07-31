import {LightWeightUser} from "./LightWeightUser";

// Global secondary index - `user_id`
export interface Tweet {
    id: string,
    userId: string,
    user: LightWeightUser
    tweet: string,
    createdAt: string,
    likeList: Array<LightWeightUser>,
    imageUrl?: string
}