import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HttpClient } from '@angular/common/http';

// Angular Material
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { MatTableModule } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';

// Google Charts
import { GoogleChartsModule } from 'angular-google-charts';

// Apps own imports
import { LoginComponent } from './users/login/login.component';
import { UsersListComponent } from './users/users-list/users-list.component';
import { UserEditComponent } from './users/user-edit/user-edit.component';
import { ItemsListComponent } from './items/items-list/items-list.component';
import { ProjectsListComponent } from './projects/projects-list/projects-list.component';
import { ProjectsGanttComponent } from './projects/projects-gantt/projects-gantt.component';
import { ProjectEditComponent } from './projects/project-edit/project-edit.component';
import { ProjectsBookItemComponent } from './projects/projects-book-item/projects-book-item.component';
import { ProjectItemsListComponent } from './projects/project-items-list/project-items-list.component';
import { ItemEditComponent } from './items/item-edit/item-edit.component';


export const MY_DATE_FORMATS = {
        parse: {
            dateInput: 'YYYY-MM-DD'
        },
        display: {
            dateInput: 'YYYY-MM-DD',
            monthYearLabel: 'YYYY MM',
            dateA11yLabel: 'YYYY-MM-DD',
            monthYearA11yLabel: 'YYYY MM'
        }
    };

@NgModule({
  declarations: [
    AppComponent,
    UsersListComponent,
    UserEditComponent,
    ItemsListComponent,
    ProjectsListComponent,
    ProjectEditComponent,
    ProjectsGanttComponent,
    ProjectsBookItemComponent,
    LoginComponent,
    ProjectItemsListComponent,
    ItemEditComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    NoopAnimationsModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatAutocompleteModule,
    MatButtonModule,
    MatNativeDateModule,
    MatDatepickerModule,
    GoogleChartsModule.forRoot()
    ],
  providers: [
    { provide: MAT_DATE_FORMATS, useValue: MY_DATE_FORMATS }, 
    { provide: MAT_DATE_LOCALE, useValue: 'sv-SE' } 
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
