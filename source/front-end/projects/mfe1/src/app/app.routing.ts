import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: '', loadChildren: () => import('./public/public.module').then(x => x.PublicModule) },
];

export const AppRoutes = RouterModule.forRoot(routes);
