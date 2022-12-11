import { Component, OnInit } from '@angular/core';
import Chart from 'chart.js/auto';
import { Firestore, collectionData, collection, DocumentData } from '@angular/fire/firestore';
import { Observable } from 'rxjs';
import { cO, M } from 'chart.js/dist/chunks/helpers.core';


@Component({
  selector: 'app-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.css']
})
export class LineChartComponent implements OnInit {

  stats: Observable<DocumentData[]>;
  dates:string[] = [];
  avgs:number[] =  [];
  mins:number[] =  [];
  maxs:number[] =  [];

  constructor(firestore: Firestore) {
    const usersCollection = collection(firestore, 'stats');
    this.stats = collectionData(usersCollection);

    // 10 - 8 - 2 

    this.stats.forEach(element => {

     for(let i = 0; i < element.length; i++){
        this.dates.push(element[i]['idSession'].substring(0, 10) + " " + element[i]['idSession'].substring(10, 18));
        this.avgs.push(element[i]['avg']);
        this.mins.push(element[i]['min']);
        this.maxs.push(element[i]['max']);
     }
    });
    console.log(this.dates);

    /*
    let newDates:string[] = [];
    let newAvgs:number[] =  [];
    let newMins:number[] =  [];
    let newMaxs:number[] =  [];

    for(let i = 0; i < this.dates.length; i++){
      
    }*/
   
  }

  ngOnInit(): void {
    this.createChart();
  }
  public chart: any;

  createChart(){
  
    this.chart = new Chart("MyChart", {
      type: 'line', //this denotes tha type of chart

      data: {// values on X-Axis
        labels: this.dates, 
	       datasets: [
          {
            label: "Average rates",
            data: this.avgs,
            backgroundColor: 'blue'
          },
          {
            label: "Minimum rates",
            data: this.mins,
            backgroundColor: 'red'
          },
          {
            label: "Maximum rates",
            data: this.maxs,
            backgroundColor: 'green'
          }
        ]
      },
      options: {
        aspectRatio:2.5
      }
      
    });
  }

}