import * as AWS from "aws-sdk";

export class S3Util {
    constructor(
        private readonly s3: AWS.S3 = new AWS.S3({
            signatureVersion: 'v4',
            region: process.env.region,
            params: {Bucket: process.env.IMAGES_BUCKET}
        }),
        private readonly  signedUrlExpireSeconds = 60 * 5
    ) {}

    async getAttachmentUrl(tweetId: string): Promise<string> {
        try {
            await this.s3.headObject({
                Bucket: process.env.IMAGES_BUCKET,
                Key: tweetId
            }).promise();

            return this.s3.getSignedUrl('getObject', {
                Bucket: process.env.IMAGES_BUCKET,
                Key: tweetId,
                Expires: this.signedUrlExpireSeconds
            });
        } catch (err) {
            console.log(err)
        }
        return null
    }

    getPreSignedUrl(tweetId: string): string {
        return this.s3.getSignedUrl('putObject', {
            Bucket: process.env.IMAGES_BUCKET,
            Key: tweetId,
            Expires: this.signedUrlExpireSeconds
        }) as string;
    }
}