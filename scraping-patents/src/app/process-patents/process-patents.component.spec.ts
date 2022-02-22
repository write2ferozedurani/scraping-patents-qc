import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessPatentsComponent } from './process-patents.component';

describe('ProcessPatentsComponent', () => {
  let component: ProcessPatentsComponent;
  let fixture: ComponentFixture<ProcessPatentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProcessPatentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcessPatentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
