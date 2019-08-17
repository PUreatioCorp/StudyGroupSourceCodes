import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DemoEditComponent } from './demo-edit/demo-edit.component';
import { DemoListComponent } from './demo-list/demo-list.component';
import { DemoAddComponent } from './demo-add/demo-add.component';

// 初期URLは /list にリダイレクトする。
// /listでDemoListComponentを表示する。
// /edit/:idで対象IDを編集するDemoEditComponentを表示する。
const routes: Routes = [
  { path: '', redirectTo: '/list', pathMatch: 'full' },
  { path: 'list', component: DemoListComponent },
  { path: 'add', component: DemoAddComponent },
  { path: 'edit/:id', component: DemoEditComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
