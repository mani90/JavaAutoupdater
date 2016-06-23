
JavaAutoupdater
===============

Java Auto updater for Java Desktop Application Using SWT


Compile
=======

1. Create Java SWT project.
2. Import the package into the project.
3. Create update-config.xml into your server with below Format.
 
        <?xml version="1.0" encoding="UTF-8"?>
        <version>
            <avail-version>1.1</avail-version>
            <verion-date>24/05/201</verion-date>
            <size>2048</size>
            <auther>My Application</auther>
            <url>https://example.com</url>
            <check-url>https://example.com/update.zip</check-url>
            <status>completed</status>
        </version>
