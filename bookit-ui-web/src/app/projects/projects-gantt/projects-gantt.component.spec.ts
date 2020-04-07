import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectsGanttComponent } from './projects-gantt.component';

describe('ProjectsGanttComponent', () => {
  let component: ProjectsGanttComponent;
  let fixture: ComponentFixture<ProjectsGanttComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProjectsGanttComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProjectsGanttComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
