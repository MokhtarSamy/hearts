import { Component } from '@angular/core';
import Chart from 'chart.js/auto';
import { Firestore, collectionData, collection, DocumentData } from '@angular/fire/firestore';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-bar-chart',
  templateUrl: './bar-chart.component.html',
  styleUrls: ['./bar-chart.component.css']
})
export class BarChartComponent {
[x: string]: any;
chart: any;

ngOnInit(): void {
  this.createChart()
}

createChart(){
    this.chart = new Chart("MyChart", {
      type: 'bar', //this denotes tha type of chart

      data: {// values on X-Axis
        labels: ['2022-05-10', '2022-05-11', '2022-05-12'],
	       datasets: [
          {
            label: "Sales",
            data: ['467','576', '572'],
            backgroundColor: 'blue'
          },
          {
            label: "Profit",
            data: ['542', '542', '536'],
            backgroundColor: 'limegreen'
          }
        ]
      },
      options: {
        aspectRatio:2.5
      }
    });
  }

  constructor(firestore: Firestore) {
    const usersCollection = collection(firestore, 'users');
  }

}
