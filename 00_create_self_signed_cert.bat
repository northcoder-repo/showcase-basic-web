@echo off

REM When prompted for 'first and last name', enter your 
REM domain name - for example 'localhost', if you are
REM just running everything locally.
REM
REM For both passwords (see below), make sure that 
REM what you enter matches what's in the webdemo.properties 
REM file for the 'demo.keystore.pass' entry. Use the same
REM password for the keystore and the key.
REM 
REM For the other prompts, enter sensible values.

REM a keystore using 'localhost' has already been created, so...
exit 0

"%JAVA_HOME%/bin/keytool" ^
  -genkey ^
  -keyalg RSA ^
  -alias selfsigned ^
  -keystore keystore.jks ^
  -storepass your_keystore_password ^
  -keypass your_keystore_password ^
  -validity 365 ^
  -keysize 2048

pause
