import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UsersListComponent } from './users/users-list/users-list.component';
import { UserEditComponent } from './users/user-edit/user-edit.component';
import { ItemsListComponent } from './items/items-list/items-list.component';
import { ProjectsListComponent } from './projects/projects-list/projects-list.component';
import { ProjectsGanttComponent } from './projects/projects-gantt/projects-gantt.component';
import { ProjectEditComponent } from './projects/project-edit/project-edit.component';
import { ProjectsBookItemComponent } from './projects/projects-book-item/projects-book-item.component';
import { LoginComponent } from './users/login/login.component';

const routes: Routes = [
    {   path: 'login',              component: LoginComponent               },
    {   path: 'logout',             component: LoginComponent               },
    {   path: 'users',              component: UsersListComponent           },
    {   path: 'user/:id',           component: UserEditComponent            },
    {   path: 'items',              component: ItemsListComponent           },
    {   path: 'projects',           component: ProjectsListComponent        },
    {   path: 'projects-gantt',     component: ProjectsGanttComponent       },
    {   path: 'project/:id',        component: ProjectEditComponent         },
    {   path: 'projects-book-item', component: ProjectsBookItemComponent    },
    
    {   path: 'home',   redirectTo: '/projects-book-item', pathMatch: 'full'             },                        

    //    {
//        path: 'about',
//        component: AboutComponent
//    },
//    {
//        path: 'courses',
//        component: CoursesComponent
//    },
    {
        path: '',
        redirectTo: '/login',
        pathMatch: 'full'
    },
    {
        path: '**',
        redirectTo: '/',
        pathMatch: 'full'
    }                        
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
