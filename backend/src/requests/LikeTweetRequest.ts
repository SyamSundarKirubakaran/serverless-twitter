import {LightWeightUser} from "../models/LightWeightUser";

// `id` belongs to tweet id - available as path parameter
export interface LikeTweetRequest {
    likesCount: number,
    likeList: Array<LightWeightUser>
}