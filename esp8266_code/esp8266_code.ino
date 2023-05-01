//Bibliothéque de communication série entre arduino et NodeMCU
#include <SoftwareSerial.h>
//Bibliothéque pour travailler avec l'objet JSON
#include <ArduinoJson.h>
// Bibliothéque pour connecter à Firebase
#include "FirebaseESP8266.h"
//Bibliothéque pour travailler avec ESP8266 dans l'arduino IDE
#include <ESP8266WiFi.h>

// Lien de la base de données
#define FIREBASE_HOST "agrilogger-default-rtdb.firebaseio.com"
// Mot de passe de la base de données
#define FIREBASE_AUTH "iDdt1zeiH7Xwt0RKompgl4tABblwXp4oAGqa0uYR"
//Nom de Wifi
#define WIFI_SSID "V1rusafi"
// Mot de passe de wifi
#define WIFI_PASSWORD "s@fi-1997-v1rus"
//Initialisation des pin de communication NodeMCU à Arduino (D6=Rx & D5=Tx)
SoftwareSerial nodemcu(D6, D5);
// Initialisation des objet FirebaseData pour accéder à la base de données
FirebaseData firebaseData;
FirebaseData pompeStatus;
// Variable pour stocker la valeur du pompe
String pompe_status;
// initialisation du pin du pompe
int pompe=D1;
void setup() {
  //Configuration du pin de pompe
  pinMode(pompe,OUTPUT);
  Serial.begin(1200);
  //Connexion au wifi
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  // Vérification de la connexion de wifi jusqu'a le succés de l'operation
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println("wifi");
  //Connexion à la base de données
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);// Vérification de la connexion à Firebase
  Serial.println("firebase");
  //Initialisation de la communication série
  nodemcu.begin(1200);
  //Vérification de la communication jusqu'a le succés de l'opération
  while (!Serial) continue;
}

void loop() {
  //Réception de l'objet JSON
  StaticJsonBuffer<1000> jsonBuffer;
  JsonObject& data = jsonBuffer.parseObject(nodemcu);
  //Vérification si l'objet recus est valid
  if (data == JsonObject::invalid()) {
    jsonBuffer.clear();
    return;
  }
 delay(300);
  //Affecter chaque valeur recus à une variable
  float hum = data["humidity"];
  float temp = data["temperature"];
  float lum = data["Lumiére"];
  float niveauEu =  data["Niveau eau"];
  float humsol=data["humidité de sol"];
  // envoyer la valeur de la température
  delay(300);
  Firebase.pushFloat(firebaseData, "/temp3", temp);
  Serial.println("temp");
  // envoyer la valeur d'humidité
  Firebase.pushFloat(firebaseData, "/humid", hum);
  Serial.println("h");
  //envoyer la valeur de la lumiere 
  Firebase.setFloat(firebaseData, "/sunval" , lum );
  Serial.println("l");
  //envoyer la valeur de niveau d'eau 
  Firebase.setFloat(firebaseData, "/tank" , niveauEu);
  Serial.println("e");
// envoyer la valeur d'humidité de sol
 Firebase.pushFloat(firebaseData, "/humidsol" , humsol);
 Serial.println("sol");
  //Collecter l'état de pompe dans la base de données
  Firebase.getString(pompeStatus, "/pompe");
  Serial.println("pompe");
  pompe_status = pompeStatus.stringData();
  //Commander la pompe selon la valeur collecter
  if(pompe_status=="ON"){
    digitalWrite(pompe,LOW);
  }
  else{
    digitalWrite(pompe,HIGH);
  }
  yield();
  //fin de loop
  
}
