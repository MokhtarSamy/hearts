import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { provideFirebaseApp, getApp, initializeApp } from '@angular/fire/app';
import { getFirestore, provideFirestore } from '@angular/fire/firestore';
import { AppComponent } from './app.component';
import { BarChartComponent } from './bar-chart/bar-chart.component';
import { LineChartComponent } from './line-chart/line-chart.component';
import { UserComponent } from './user/user.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

const firebaseConfig = {
  apiKey: "AIzaSyD4phyR2v8R-MyW0wLDUB2TdenPCyJ8P_o",
  authDomain: "smartwatchapp-92b44.firebaseapp.com",
  databaseURL: "https://smartwatchapp-92b44-default-rtdb.europe-west1.firebasedatabase.app",
  projectId: "smartwatchapp-92b44",
  storageBucket: "smartwatchapp-92b44.appspot.com",
  messagingSenderId: "516324080387",
  appId: "1:516324080387:web:aebaff936220290aac8736",
  measurementId: "G-K9KNZRCF73"
};

@NgModule({
  declarations: [
    AppComponent,
    BarChartComponent,
    LineChartComponent,
    UserComponent
  ],
  imports: [
    BrowserModule,
    provideFirebaseApp(() => initializeApp(firebaseConfig)),
    provideFirestore(() => getFirestore()),
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
