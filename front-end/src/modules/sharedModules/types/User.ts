export type LoginPayloadType = {
  username: string
  password: string
}
export type LoginResponseType = {
  accessToken: string
  expiresIn: number
  refreshExpiresIn: number
  refreshToken: string
  tokenType: string
}

export type TokenResponseType = {
  code: number
  message: string
  data: LoginResponseType
}