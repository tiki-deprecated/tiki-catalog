/*
 * Copyright (c) TIKI Inc.
 * MIT license. See LICENSE file in root directory.
 */

import org.bouncycastle.util.encoders.Base64;

public class MockedBlock {
    public static final String titleHash = "XO99p1jVEfpQoSnGnLy42h0NXgoDXJ428CaEI9p9ZtY";
    public static final String licenseHash = "2jPH00bX27QFlbJDynobsTHMQ71_kVWJbKVbuJMGTYI";
    public static final String titleTxn = "4QlIHwMZYjcFv5ZmORsTxXyZLOOfmiSUUb5MgCjdrUE";
    public static final String licenseTxn = "mQyDnlU0LkUKiQnvIGUubEsxomWrTJ3CrKHqOPM13rc";
    public static final String titleSrc = "https://bucket.storage.l0.mytiki.com/2ab3efdb-8e91-4148-a43b-a7c198b4d3d7/FaUMg5tkhk898KgcWwK6MVbCAiYaE-v_qh0tJwSru5I/XO99p1jVEfpQoSnGnLy42h0NXgoDXJ428CaEI9p9ZtY.block?versionId=001678594781561323913-9y4mqyyn0o";
    public static final String licenseSrc = "https://bucket.storage.l0.mytiki.com/2ab3efdb-8e91-4148-a43b-a7c198b4d3d7/FaUMg5tkhk898KgcWwK6MVbCAiYaE-v_qh0tJwSru5I/2jPH00bX27QFlbJDynobsTHMQ71_kVWJbKVbuJMGTYI.block?versionId=001678594782480352129-oPNcEbD5Xl";
    public static final String appId = "2ab3efdb-8e91-4148-a43b-a7c198b4d3d7";
    public static final String address = "FaUMg5tkhk898KgcWwK6MVbCAiYaE-v_qh0tJwSru5I";

    public static byte[] title(){
        final String raw = "/QABI0sdmC02c/DFroY9lAqLnpvpw+gSeWzC2uzLiTSP5z0gXsk4C7Vu1hPgl2JQrkEVCbj50z4DpHnDJF5Rw4fuEpVO2Gqaipk9uparrCX75hfKV8naW/E8JctUMdCBcHSl/S2cFdGdSIC0SKdEMAGQyc91OLePATc4SG0guEMketOv2GG3Oq1z/afolOVcvj/vVt0kliqsC4VujuHpx29SfwrkkR7FyB6G3UMthad+Og/Wv5XzuXZtfJ22YnTfck+BtF8Nws6bBXJbbpRYRTE4RVQCuK4K0xSpWhJEcK6oxYCFPZsvdNr5QwBTBagLs03rhsA8/RYTqL4WgW8VDP1CyP2xAQEBBGQNUt0BACCe2OLaG4yD5FTyCoQwpBWt0Iu008+G0LQ3F6LjIOBeGgEB/YIBAQIgFaUMg5tkhk898KgcWwK6MVbCAiYaE+v/qh0tJwSru5IEZA1S3QD9AAE5BVqEPxCFh2TCA4Pkumatsmbrxb58G6ZQJxbVmgnKp4OZ4grlAiniSf8BAVB2z6bWHWEsL2W6pgOnik6y7Ojim6PSAoch/Q0CdAZ91Nct6mrF5QvVe7qmge1p9V26wC80IZeHUyu+dTPVRbag9h5BorOX4SqQnyPDLOYaoJn939jnPC2kAKXtIU/N+vfm2NVy2A0DLJ3XJ7eIMaKvSfQNMUZs60Zq3edj9x4khbaMQBymYfM3t8tf9MbX7lgfZV5Bm7k21QvgCqYIN7I9A0CnVe0D/AUNp3wPVTEJAMgLPB6RWg0UmFpozxygY3H5KfRciSbi1TY2x8coJPdmaIFYAFQBAiRmY2U1YzI2Ny1lOTBiLTQ0NmMtOGZkZC04NmUyYzZhZTA3MGIYY29tLm15dGlraS5zZGstZGFydC50ZXN0ABJbImN1c3RvbTp0ZXN0aW5nIl0=";
        return Base64.decode(raw);
    }

    public static byte[] license(){
        final String raw = "/QABXls2NTisiN4L/WatCwSBgdLJzy7Z0xF0IuyfT4VPo/cafXOdJGFCQruzpalO+QKNJoVOLt4SowvvsXTIeXTAO2xxiMJG5TEr1yUDodawd3sqzhD1Ske3k6aOHaeDnRZsCQocQr0jP9/8eTmusEmNRRkw1oIVvaEJseXgFbRk3iZ+8l9Iu/JO2lT84pHDlVZsP/Do/t/7jqk9o3/eOXNEhOhqNEWe6f/CgjwGnJHqELtqptRUQYjT7zsfVpIEqKBLsPtqVJlEWlRrqY5dEqLA/kdX6rnbadvz6on5HzUOTQWZRaN7jEWHW6gunhRTb9lV8xPcGCNjg7ie5BhHf176cP0kAgEBBGQNUt0gXO99p1jVEfpQoSnGnLy42h0NXgoDXJ428CaEI9p9ZtYg1CQMYKYtO9Bt2DnexUWNL3gSvs6kPKIxY84gYovUJM4BAf3WAQECIBWlDIObZIZPPfCoHFsCujFWwgImGhPr/6odLScEq7uSBGQNUt0xdHhuOi8vNFFsSUh3TVpZamNGdjVabU9Sc1R4WHlaTE9PZm1pU1VVYjVNZ0NqZHJVRf0AAUDusvurofCiXOLvtQAFjkuJrq9haVG+DMtZmA7BtznbeDovnyGcSTvzKmfQzQuv2NRXOvxZ2q4dOP+/uRypq/tbE/g/GkMhZ5uNS6Kd0wbR+LX7BWUhzf6zvmyeSAkcK3aicIQWm1vSt+X+MfK1ptdaASRQzQjVXfktQzbeAlZRPw0SO+hZ2tSU0atHaBvEz7d2OBVuV++2gfoWWcG2TnAwntr2FMbwmU9ny2BDpI0d0JHvv4gq11EjgzyfK5B1QNa4H9okWFaTfR9yl+kQE+CHEnTzmZbFqxe8oVw6ZSai0B6kY1ujmQUoN7P90yeHpG5K7sYWceyroFoburBQHvUAdwEDRFt7InVzZWNhc2VzIjpbImN1c3RvbTp0ZXN0aW5nIl0sImRlc3RpbmF0aW9ucyI6WyJcXC5teXRpa2lcXC5jb20iXX1dGGZvciB1c2UgaW4gdGVzdGluZyBvbmx5LhFyZWdpc3RyeSB0ZXN0aW5nLgRkDqRd";
        return Base64.decode(raw);
    }
}
