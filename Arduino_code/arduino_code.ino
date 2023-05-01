//Bibliothéque du cateur DHT11
#include <DHT.h>
//Bibliothéque de communication série entre arduino et NodeMCU
#include <SoftwareSerial.h>
//Bibliothéque pour crée l'objet JSON
#include <ArduinoJson.h>
//Initialisation des pin de communication Arduino à NodeMCU (5=Rx & 6=Tx)
SoftwareSerial nodemcu(5, 6);

#define DHTPIN 4 // Initialisation du pin du DHT11
int hs=A5; // Initialisation du pin du capteur d'humidité de sol
int nE=A1; //Initialisation du pin du capteur de niveau d'eau
int phR=A0; // Initialisation du pin de la photorésistance
DHT dht(DHTPIN, DHT11);//Configuration du capteur DHT11 
int nj; // état de lumiére
int respv; // état du reservoire d'eau
int moisture=0; // valeur finale de l'humidité de sol aprés le traitement des données
int  valPH =0; // valeur collecter par photorésistance
int valnE=0;// valeur collecter par capteur de niveau d'eau
int valsol;// valeur collecter par capteur d'humiduté de sol
float temp; // valeur de température collecter par capteur DHT11
float hum; // valeur d'humidité collecter par capteur DHT11


void setup() {
  pinMode(hs,INPUT);//Configuration du pin du capteur d'humidité de sol
  pinMode(nE,INPUT);//Configuration du pin du capteur de niveau d'eau
  pinMode(phR,INPUT);//Configuration du pin de la photorésistance 
  dht.begin(); //Initialisation du capteur DHT11 
  nodemcu.begin(1200);//Initialisation de la communication Arduino à NodeMCU
}

void loop() {
  //Création d'objet JSON
  StaticJsonBuffer<1000> jsonBuffer;
  JsonObject& data = jsonBuffer.createObject();
  // collecter les données de température et humidité
  hum = dht.readHumidity();
  temp = dht.readTemperature();
  // collection et traitement de la valeur de photorésistance
  valPH = analogRead(phR);
  if(valPH<50){
    nj=0; 
  }
else {
  Serial.println(" nj=1 ");
    nj=1; 
}

 // collection et traitement de la valeur de niveau d'eau
  valnE = analogRead(nE);
  if(valnE>470){
    respv=1;
  }
  else{
    respv=0;
  }
  // collecter la valeur d'humidité de sol
  valsol = analogRead(hs);
  moisture = map(valsol,669,159,0,100);
  //Affecter les données collecter à l'objet JSON
  data["humidity"] = hum;
  data["temperature"] = temp; 
  data["Lumiére"] = nj;
  data["humidité de sol"] = moisture;
  data["Niveau eau"] = respv;
  //Envoyer les données à NodeMCU
  data.printTo(nodemcu);
  jsonBuffer.clear();
  //Retard de 1 second
  delay(1000);
}
