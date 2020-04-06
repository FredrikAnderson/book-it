import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectsBookItemComponent } from './projects-book-item.component';

describe('ProjectsBookItemComponent', () => {
  let component: ProjectsBookItemComponent;
  let fixture: ComponentFixture<ProjectsBookItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectsBookItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectsBookItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
