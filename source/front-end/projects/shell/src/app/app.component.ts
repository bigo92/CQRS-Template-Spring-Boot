import { Component } from '@angular/core';
import { SharedService } from '_shared';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent {
  title = 'shell';

  constructor(private service: SharedService) {
    this.service.login('Max', null);
  }
}

