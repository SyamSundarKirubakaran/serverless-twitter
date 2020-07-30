
// `user-id` from bearer token
export interface CreateUserRequest {
    name: string,
    email: string,
    imageUrl: string
    isVerified: boolean,
}