import { Component, OnInit, Input } from '@angular/core';
import { Project } from '../../shared/project';
import { Item } from '../../shared/item';
import { ProjectsServiceService } from '../../services/projects-service';
import { MatTableDataSource } from '@angular/material/table';
import { DataSource } from '@angular/cdk/table';

@Component({
  selector: 'app-project-items-list',
  templateUrl: './project-items-list.component.html',
  styleUrls: ['./project-items-list.component.css']
})
export class ProjectItemsListComponent implements OnInit {
    
    displayedColumns: string[] = ['id', 'name', 'description', 'inventory'];

    items = new MatTableDataSource<Item>();

    // Only for type, select
    selectedProject : Project;

    name : string;

    
    selectedItem : Item = null;

    constructor(private projectService : ProjectsServiceService) { 
    }
    
    ngOnInit(): void {
//      this.getItems();
    }
        
    getItems() {
      this.items = new MatTableDataSource<Item>();
//      this.projectService.getItems().subscribe((data : any) => {
//          console.log(data);
//          this.items = new MatTableDataSource(data);  
//      });      
    }
    
    setProject(project : Project) {
        console.log("setProject: " + project.id);

//        let item2 : Item;
//        
//        let item1 : Item = new Item();
//        item1.id = "1";
//        item1.name = "Test";
//        item2 = new Item();
//        item2.id = "2";
//        item2.name = "Test 22";
//        item2.description = "Desc.";
//        
//        project.bookedItems = 
//            [ 
//            item1,
//            item2
//        ];        
        
        this.selectedProject = project;        
        this.name = project.name;
        
        this.items = new MatTableDataSource(project.bookedItems);
    }

    public onCancelItemBooking() {
        if (this.selectedItem) {
            console.log("Should cancel booking for projId" + this.selectedProject.id + 
                    " and itemId: " + this.selectedItem.id);      
            
            this.projectService.cancelItemForProject(this.selectedItem.id, this.selectedProject.id).subscribe(
                    
            );
            
            window.location.reload();
            
//            this.router.navigate( ['/project/' + this.selectedProject.id ] );            
        }        
    }
    
    public onRowClicked(item : Item) {
    
      console.log("clicked Item id: " + item.id );      
      
//      window.alert("Should show edit page for item with id: " + item.id);

      if (this.selectedItem === item) {
          // Unselect
          this.selectedItem = null;
      } else {
          // Select
          this.selectedItem = item;              
      }          
    }

    public onRowBackgroundColor(item : Item) {
        
        if (this.selectedItem === item) {
//            console.log("selected item: ", item);      
            return 'lightblue';
        } 
      }

    applyFilter(event: Event) {
      const filterValue = (event.target as HTMLInputElement).value;
      this.items.filter = filterValue.trim().toLowerCase();
    }
    
}
