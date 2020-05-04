import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Item } from '../../shared/item';
import { ItemsServiceService } from '../../services/items-service';
import { MatTableDataSource } from '@angular/material/table';
import { DataSource } from '@angular/cdk/table';

@Component({
  selector: 'app-items-list',
  templateUrl: './items-list.component.html',
  styleUrls: ['./items-list.component.css']
})
export class ItemsListComponent implements OnInit {

    // Possible values: list, select
    @Input('type') type : string = "list";
    
    displayedColumns: string[] = ['id', 'name', 'description', 'inventory'];

    items = new MatTableDataSource<Item>();

    // Only for type, select
    selectedItem : Item;

    constructor(private router: Router,
            private itemService : ItemsServiceService) {}
    
    ngOnInit(): void {
      this.getItems();
    }
    
    public resetForm() {
        this.selectedItem = null;
        
    }
    
    getItems() {
      this.items = new MatTableDataSource<Item>();
      this.itemService.getItems().subscribe((data : any) => {
          this.items = new MatTableDataSource(data);  
      });      
    }

    public onNewItem() {
        this.router.navigate( ['/item/new'] );
    }

    public onRowClicked(item : Item) {
    
      console.log("clicked id: " + item.id + " I am type: " + this.type);      
      
//      window.alert("Should show edit page for item with id: " + item.id);

      if (this.type.includes('select')) {
          if (this.selectedItem === item) {
              // Unselect
              this.selectedItem = null;
          } else {
              // Select
              this.selectedItem = item;              
          }          
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
