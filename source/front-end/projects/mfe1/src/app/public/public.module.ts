import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PublicComponent } from './public.component';
import { PublicRoutes } from './public.routing';

@NgModule({
  imports: [
    CommonModule,
    PublicRoutes
  ],
  declarations: [PublicComponent]
})
export class PublicModule { }
