import { Component, OnInit } from '@angular/core';
import { AppService } from './app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [AppService],
})
export class AppComponent implements OnInit {
  
  showTemplates() : void {
 
  }
  
  title = 'angularclient';

  ngOnInit(){

  }

  substituteFormul() : void{
      
  }
}


