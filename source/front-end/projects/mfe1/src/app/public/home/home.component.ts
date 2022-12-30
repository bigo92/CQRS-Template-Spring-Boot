import { Component, OnInit } from '@angular/core';
import { SharedService } from '_shared';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  user = this.service.user;
  constructor(private service: SharedService) {
    this.service.login('Max', null);
  }

  ngOnInit() {
  }

}
