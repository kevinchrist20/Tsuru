export enum ToolRoute {
  Base64 = "base64",
  Base64Image = "base64Image",
  Slugs = "slug",
  UUID = "uuid"
}

export enum ToolCategory {
  EncodeDecode = "Encoders & Decoders",
  Converters = "Converters",
  Cryptography = "Cryptography & Security",
}

export interface Tool {
  name: string;
  sub: string;
  description: string;
  route: ToolRoute;
  category: ToolCategory;
  icon?: string;
}

export function getTools(): Tool[] {
  return [
    {
      name: "Base64 Encoder/Decoder",
      sub: "Base64 Encoder/Decoder is a utility for converting text to Base64 or decoding Base64 to text. It comprises two modes: Base64 Encoder and Base64 Decoder.",
      description: `
        <p><a href="https://en.wikipedia.org/wiki/Base64"><b>Base64</b></a> is a binary-to-text encoding representing binary data in an ASCII string format. Base64 encoding uses different 64 ASCII characters including uppercase letters A-Z, lowercase letters a-z, 
        digits 0-9, and the special symbols + and / to represent binary data in a text format.</p><br/>
                
        <p><b>Base64 Encoder</b> - Converts plain text or binary data into a Base64 encoded format, increasing readability and ensuring compatibility with systems that only support ASCII characters.</p>
        <p>Enter text in the input field, and the output Base64 will be displayed accordingly.</p><br/>
 
        <p><b>Base64 Decoder</b> - Reverses the process, converting Base64 encoded data back into its original form, facilitating data transfer and interpretation across different platforms.</p>
        <p>Paste Base64 in the input field, and the output string will be displayed accordingly.</p>
      `,
      route: ToolRoute.Base64,
      category: ToolCategory.EncodeDecode
    },
    {
      name: "Base64 Image Encoder/Decoder",
      sub: "Base64 Image encoding is a versatile tool for converting images to Base64 strings or decoding Base64-encoded images. A Base64 encoded image is the result of converting a binary image to a Base64 string.",
      description: `
        <p><a href="https://en.wikipedia.org/wiki/Base64"><b>Base64 Image</b></a> encoding simplifies the process of encoding images into Base64 strings and decoding Base64-encoded images. 
        Base64 encoding represents binary data in ASCII format, allowing images to be embedded directly into HTML, CSS, or XML files.</p><br/>
                
        <p><b>Base64 Image Encoder</b> - Converts an image to Base64 by either browsing an image file locally or fetching an image from a URL. 
        When the process is done, the input image and Base64 output will be displayed accordingly.</p>
        <p>Maximum file size supported is 8 MB.</p><br/>
                
        <p><b>Base64 Image Decoder</b> - Converts a Base64 string to an image by pasting a Base64 string in the input field. The output image will show up instantly.</p>
        <p>For a large image, this process may take a moment.</p>
      `,
      route: ToolRoute.Base64Image,
      category: ToolCategory.EncodeDecode
    },
    {
      name: "Slugs",
      sub: "Slugs Generator",
      description: "It slugify words to an SEO-friendly and human-readable URL slug for SEO optimization. A URL slug is the part located at the very end of a URL that refers to a page or post on a website.",
      route: ToolRoute.Slugs,
      category: ToolCategory.EncodeDecode
    },
    {
      name: "UUID",
      sub: "UUID Generator",
      description: "UUID is an abbreviation standing for Universally Unique Identifier. A UUID is a 128-bit value used to identify a computer resource to ensure its uniqueness.",
      route: ToolRoute.UUID,
      category: ToolCategory.EncodeDecode
    }
  ];
}
