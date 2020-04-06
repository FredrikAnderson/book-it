import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { FormGroup, FormControl } from '@angular/forms';
import { User } from '../../shared/user';
import { UsersServiceService } from '../../services/users-service';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit {

    userForm = new FormGroup({
        userid: new FormControl(''),
        name: new FormControl(''),
        email: new FormControl('')
    });

    user = new User();

    public useridReadonly = false;
    
    constructor(private route: ActivatedRoute,
                private router: Router,
                private userService: UsersServiceService) {        
    }
    
  ngOnInit(): void {
      const userId = this.route.snapshot.paramMap.get('id');
//      console.log("User id: " + userId );

      // New project
      if (userId.includes('new')) {

          this.updateModelAndUpdateView(new User());
          
      } else {
          // Existing project
          this.userService.getUser(userId).subscribe(data => {
              
              this.updateModelAndUpdateView(data);
              this.useridReadonly = true;
          });
      }      
  }

  updateModelAndUpdateView(aUser : User) {
      this.user = aUser; 
      this.updateViewFromModel();
  }
  
  updateViewFromModel() {
//      console.log("updateView: " + this.project.id + ", " + this.project.startDate);
      
      this.userForm.get('userid').setValue(this.user.userid);
      this.userForm.get('name').setValue(this.user.name);
      this.userForm.get('email').setValue(this.user.email);
  }
    
  public onSubmit() {
      console.warn("onSubmit");
  }      
  
  public onSaveUser() {
//      console.warn(this.projectForm.value);
 
      this.user = Object.assign({}, this.userForm.value);
     
      this.userService.saveUser(this.user).subscribe((data : any) => {
      
          this.router.navigate(['/users']);
      });
  }
  
  public onDeleteUser() {
      const id = this.userForm.get('userid').value;
//    window.alert("Should delete project. " + id);
      
      this.userService.deleteUser(id).subscribe((data : any) => {
          this.router.navigate(['/users']);
      });
  }
}
