import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { Item } from '../../shared/item';
import { ItemsServiceService } from '../../services/items-service';
//import { MatTableDataSource } from '@angular/material/table';
//import { DataSource } from '@angular/cdk/table';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { FormGroup, FormControl } from '@angular/forms';
import { MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';

@Component( {
    selector: 'app-item-edit',
    templateUrl: './item-edit.component.html',
    styleUrls: ['./item-edit.component.css']
} )
export class ItemEditComponent implements OnInit {


    itemForm = new FormGroup( {
        //        id: new FormControl( '' ),
        name: new FormControl( '' ),
        inventory: new FormControl( '' ),
        description: new FormControl( '' ),
        width: new FormControl( '' ),
        height: new FormControl( '' ),
        length: new FormControl( '' ),
        weight: new FormControl( '' ),
        price: new FormControl( '' ),
    } );

    showId: boolean = true;
    showSaveBtn: boolean = true;
    showDeleteBtn: boolean = true;

    item = new Item();

    items : Item[] = [];

    itemNameForm = new FormControl();
    options: string[] = ['One', 'Two', 'Three'];
    filteredOptions: Observable<string[]>;

    constructor( private route: ActivatedRoute,
        private router: Router,
        private itemService: ItemsServiceService ) { }

    ngOnInit(): void {
        const itemId = this.route.snapshot.paramMap.get( 'id' );
        console.log( "Item id: " + itemId );

        //        if (this.type.includes('nooperations')) {
        //            this.showId = false;
        //            this.showSaveBtn = false;
        //            this.showDeleteBtn = false;            
        //        }

        // New project
//        if ( itemId == null || itemId.includes( 'new' ) ) {
//
//            this.updateModelAndUpdateView( new Item() );
//
//        } else {
//            // Existing project
//            this.itemService.getItem( itemId ).subscribe( data => {
//
//                this.updateModelAndUpdateView( data );
//            } );
//        }

        this.itemService.getItems().subscribe( data => {

            console.log("got items: " + data);
            this.items = data;
            
            this.options = this.items.map( function(item) {
                return item['name'] + " Inv:" + item['inventory'];                
            } )
//            this.options =  ( data );
            
            console.log("options: " + this.options);

        } );

        this.filteredOptions = this.itemNameForm.valueChanges
            .pipe(
            startWith( '' ),
            map( value => this._filter( value ) )
            );
    }

    private _filter(value: string): string[] {
            const filterValue = value.toLowerCase();

            return this.options.filter(option => option.toLowerCase().includes(filterValue));
          }

    updateModelAndUpdateView( item: Item ) {
        this.item = item;
        this.updateViewFromModel();
    }

    updateViewFromModel() {
        //      console.log("updateView: " + this.project.id + ", " + this.project.startDate);

        //        this.itemForm.get( 'id' ).setValue( this.item.id );
        this.itemForm.get( 'name' ).setValue( this.item.name );
        this.itemForm.get( 'inventory' ).setValue( this.item.inventory );
        this.itemForm.get( 'description' ).setValue( this.item.description );
        this.itemForm.get( 'width' ).setValue( this.item.width );
        this.itemForm.get( 'height' ).setValue( this.item.height );
        this.itemForm.get( 'length' ).setValue( this.item.length );
        this.itemForm.get( 'weight' ).setValue( this.item.weight );
        this.itemForm.get( 'price' ).setValue( this.item.price );

        // TODO
        //        this.itemService.setCurrentProject(this.item);
    }

    updateModelFromView() {
        this.item = Object.assign( {}, this.itemForm.value );

        //        this.timesToUTC();        
    }
    
    public onItemNameChanged(event: MatAutocompleteSelectedEvent) {
        console.log("onSelectionChanged: " + event.option.value + " id: " + + event.option.id);
        
        
    }
    
    public onSaveItem() {
        this.updateModelFromView();

        this.router.navigate( ['/items'] );

        //        this.saveItem().subscribe(( data: Item) => {
        //            this.item = data;
        //            
        //            this.router.navigate( ['/items'] );
        //        } );
    }

    public saveItem(): Observable<Item> {
        //      console.warn(this.projectForm.value);
        this.updateModelFromView();

        // TODO
        return null; // this.itemService.saveItem( this.item );
    }

    public onDeleteItem() {
        this.router.navigate( ['/items'] );

        //        const id = this.itemForm.get( 'id' ).value;
        //    window.alert("Should delete project. " + id);


        //        this.itemService.deleteItem( id ).subscribe(( data: any ) => {
        //            this.router.navigate( ['/items'] );
        //        } );
    }

    debugData() {
        //        console.log("Component Type: ", this.type);
        console.log( "Model, Item: ", this.item );
    }


}
