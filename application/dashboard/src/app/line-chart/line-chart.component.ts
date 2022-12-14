import { Component, OnInit } from '@angular/core';
import Chart from 'chart.js/auto';
import { Firestore, collectionData, collection, DocumentData, query, orderBy} from '@angular/fire/firestore';
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
    const statsCollection = collection(firestore, 'stats');
    const q = query(statsCollection, orderBy('date', 'asc'));
    this.stats = collectionData(q);


    // 10 - 8 - 2 

    this.stats.forEach(element => {

     for(let i = 0; i < element.length; i++){
        this.dates.push(element[i]['date']);
        this.avgs.push(element[i]['avg']);
        this.mins.push(element[i]['min']);
        this.maxs.push(element[i]['max']);
     }
    });
    console.log(this.dates);
  
   
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