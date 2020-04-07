import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectItemsListComponent } from './project-items-list.component';

describe('ProjectItemsListComponent', () => {
  let component: ProjectItemsListComponent;
  let fixture: ComponentFixture<ProjectItemsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectItemsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectItemsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
