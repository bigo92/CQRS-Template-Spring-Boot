import { Routes, RouterModule } from '@angular/router';
import { PublicComponent } from './public.component';

const routes: Routes = [
  {
    path: '', component: PublicComponent, children: [
      { path: 'home', loadChildren: () => import('./home/home.module').then(x => x.HomeModule) }
    ]
  },
];

export const PublicRoutes = RouterModule.forChild(routes);
