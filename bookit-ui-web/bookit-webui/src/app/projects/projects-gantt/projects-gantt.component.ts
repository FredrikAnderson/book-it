import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'; 
import { Project } from '../../shared/project';
import { ProjectsServiceService } from '../../services/projects-service';

//import { ViewChild } from '@angular/core';
//import { GoogleChartComponent } from 'angular-google-charts';

interface ChartEvent {
    column: number;
    row: number;
}

@Component({
  selector: 'app-projects-gantt',
  templateUrl: './projects-gantt.component.html',
  styleUrls: ['./projects-gantt.component.css']
})
export class ProjectsGanttComponent implements OnInit {

//    @ViewChild('chart')
//    chart: GoogleChartComponent;
    
    type = 'Timeline';
    title = "Fredrik testar";

    roles = [];
//    roles = [
//             { role: 'tooltip', type: 'string', index: 3, p: { html: true } }
//             ];
// 
    
//    columnNames = [
//                   ['string', 'Project'], 
//                   {type: 'string', role: 'tooltip', p: {html:true}}, 
//                   ['date', 'Start Date'], 
//                   ['date', 'End Date'],
//                   ];
    columnNames = ['Project', 'Start Date', 'End Date'
                   ];
    
    data = [
          ['Proj #1', new Date(2020, 4, 2), new Date(2020, 4, 10)],
          ['FAs project', new Date(2020, 4, 4), new Date(2020, 4, 7)],
          ['Testing åäö', new Date(2020, 4, 7), new Date(2020, 4, 15)],            
      ];

//    data = [
//            ['Proj #1', new Date(2020, 4, 2), new Date(2020, 4, 10), 'askfdjs'],
//            ['FAs project', new Date(2020, 4, 4), new Date(2020, 4, 7), 'tooltip åäö'],
//            ['Testing åäö', new Date(2020, 4, 7), new Date(2020, 4, 15), ''],            
//        ];

//    data = [];
//    data = [
//            ['Proj #1', 'abc', new Date(2020, 4, 2), new Date(2020, 4, 10)],
//            ['FAs project', 'åäö', new Date(2020, 4, 4), new Date(2020, 4, 7)],
//            ['Testing åäö', 'blah blah ', new Date(2020, 4, 7), new Date(2020, 4, 15)],            
//    ];

    
    options = {
//            title: 'My Daily Activities',
//            yAxis: 
//            timeline {
//                singleColor: '#8d8',
//            }
            tooltip: { isHtml: true },
            hAxis: { minValue : new Date(2020, 4, 1), maxValue: new Date(2020, 4, 25), format: 'dd MMM' },
            colors: ['green'],
            width: 1400,
            height: 500,
            fontSize: 20,
//            fontName: 'Courier',
    };

    projects : Project[];
    
    constructor(private router: Router,
                private projectservice : ProjectsServiceService) { }

    ngOnInit(): void {
        this.getProjects();
        
    }

    getProjects() {
        this.projects = [];
        this.projectservice.getProjects().subscribe((data : any) => {
            this.projects = data;  
            
            this.updateData();
            
//            console.log("Project" , this.projects);
//            console.log("Data" , this.data);
        });      
      }

    updateData() {
        this.data = [];

        this.projects.forEach((obj : Project) => 
            this.data.push([ obj.id + ": " + obj.name + ', ' + obj.startDate + ' -- ' + obj.endDate, 
                             new Date(obj.startDate), 
                             new Date(obj.endDate) 
                ] ) 
            );        
        
        console.log("Data" , this.data);
    }
    
  onSelect(event : ChartEvent[]) {      
      const row = event[0].row;
      console.log("Selected item, row: " + row + ", " + this.data[row][0]);

      
  }
  
}
