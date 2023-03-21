/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import org.bouncycastle.util.encoders.Base64;

public class MockedBlock {

    public static byte[] get(){
        final String raw = "/QABAQH7EFmldms7QCfVchz6QqxGIE/F0b7AQuV1a0aNozp7Ng0B0BtNvFK4JbqHbLFCzoA77h3SnAQieEbwnDH5EEjTM0B1TBm+00BOa3fTeL6ykz7j9iH+t4P4UQvO8ZSt9ItOXAuH7+UgGJGXEyH019M/pAnsv8CdETCl/ThZvv3pVBDrVzTtbB3kR2iLUtaT5OhTf1ftJw6RC+Sj8PwoRivDQV0NUnXX5RHbULj1QxRa+wxz/v/1A+Xp7CLi4a/PbEe/wuqiAShzqA9F2zQfmiDlYwMdGqbHcHd1emdIM7iqdkZIGpku8BUgaU1qAvARHojTAkrLj8MnOnFcXriTIv2xAQEBBGQNVH0BACBqKSxNCb3AqbCytCvp5diQcIFFlP8M4N7TZPjfffH3CwEB/YIBAQIgxsKfWczVl24doWj/GWeJ//7EKOdrJ+Ryh64b/IrJlUMEZA1UfQD9AAFuN3VTWhwpRaqHc4AnPeLqq1CLUiQaYTehklNzSuWWnUElLpvpK18axq8qBbhMtOqmTUasWqGg8W5OpZkY2aetMSg1avVldN8yrzFkrOhB+6TkI/xtR9znB82Dtjd7wBVtT2IjG0sh3VsusprWmfeW5K74iJe//FkAA9fCke9lUfbQZ7uBDbn94+aLr7kCnKua1MnWmwYrERR0JcvNtgN1Ij1exdDYtfkBtst7uSroD2Nk2DodZ+OS9QxdLw3oHGnMoExwxOtjd+YNN0FuXm4W6dFN0FIYD41GvlBW4TTbam6iSFlVGJ0U/e4ALpKUw7/s5mQV9DObc1OqJHjatDl/AFQBAiRmY2U1YzI2Ny1lOTBiLTQ0NmMtOGZkZC04NmUyYzZhZTA3MGIYY29tLm15dGlraS5zZGstZGFydC50ZXN0ABJbImN1c3RvbTp0ZXN0aW5nIl0=";
        return Base64.decode(raw);
    }
}
