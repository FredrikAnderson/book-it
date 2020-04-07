import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'; 
import { UsersServiceService } from '../../services/users-service';
import { User } from '../../shared/User';
import { MatTableDataSource } from '@angular/material/table';
import { DataSource } from '@angular/cdk/table';

@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.css']
})
export class UsersListComponent implements OnInit {

    displayedColumns: string[] = ['userid', 'name', 'email'];

    users;
//  : User[] = [];

//            {"userid": "one", 
//             "name": "Kalle"
//             },
//             {"userid": "two",
//             "name": "Olle"    
//             }
//            ];
    
  constructor(private router: Router,
              private userService : UsersServiceService) { }

  ngOnInit(): void {
      this.getUsers();
  }

  getUsers() {
      this.users = [];
      this.userService.getUsers().subscribe((data : any) => {
//          console.log(data);
          this.users = new MatTableDataSource(data.items);  
      });      
  }

  public onNewUser() {
      this.router.navigate(['/user/new']);
  }

  public onRowClicked(user : User) {
//      console.log("clicked" + user.userid);      
      
      this.router.navigate(['/user/' + user.userid]);
  }

  applyFilter(event: Event) {
      const filterValue = (event.target as HTMLInputElement).value;
      this.users.filter = filterValue.trim().toLowerCase();
    }

}
