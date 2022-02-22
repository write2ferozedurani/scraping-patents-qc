import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PatentListComponent } from './patent-list/patent-list.component';
import { ProcessPatentsComponent } from './process-patents/process-patents.component';

const routes: Routes = [
  { path: '', redirectTo: 'patents', pathMatch: 'full' },
  { path: 'patents', component: PatentListComponent },
  { path: 'process-patents', component: ProcessPatentsComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
