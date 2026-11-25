export type CodecMode = 'encode' | 'decode'

export interface CodecLabelSet {
  tab: string
  input: string
  output: string
}

export interface CodecLabels {
  encode: CodecLabelSet
  decode: CodecLabelSet
}
