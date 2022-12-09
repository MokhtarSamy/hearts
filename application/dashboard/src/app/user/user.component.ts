import { Component } from '@angular/core';
import { Firestore, collectionData, collection, DocumentData } from '@angular/fire/firestore';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent {
  users: Observable<DocumentData[]>;

  constructor(firestore: Firestore) {
    const usersCollection = collection(firestore, 'users');
    this.users = collectionData(usersCollection);
  }
}
