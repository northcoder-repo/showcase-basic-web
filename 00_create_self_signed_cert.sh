#! /bin/bash

# When prompted for 'first and last name', enter your 
# domain name - for example 'localhost', if you are
# just running everything locally.
#
# For both passwords (see below), make sure that 
# what you enter matches what's in the webdemo.properties 
# file for the 'demo.keystore.pass' entry. Use the same
# password for the keystore and the key.
# 
# For the other prompts, enter sensible values.

# a keystore using 'localhost' has already been created, so...
exit 0

$JAVA_HOME/bin/keytool \
  -genkey \
  -keyalg RSA \
  -alias selfsigned \
  -keystore keystore.jks \
  -storepass your_keystore_password \
  -keypass your_keystore_password \
  -validity 365 \
  -keysize 2048

