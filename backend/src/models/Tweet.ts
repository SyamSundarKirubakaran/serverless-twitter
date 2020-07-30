import {LightWeightUser} from "./LightWeightUser";

// Global secondary index - `user_id`
export interface Tweet {
    id: string,
    userId: string,
    tweet: string,
    createdAt: string,
    likeList: Array<LightWeightUser>,
    imageUrl?: string
}