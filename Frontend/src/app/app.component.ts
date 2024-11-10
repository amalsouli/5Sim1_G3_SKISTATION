import { Component, OnInit } from '@angular/core';
import { NomDuServiceService } from './nom-du-service.service';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import { piste } from './Piste';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'gestionSkiFront';

  form: boolean = false;
  closeResult!: string;
  listpistes: any;
  piste!: any;

  constructor(private pisteService: NomDuServiceService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.getAllpiste();
    console.log(this.listpistes);
    this.piste = {
      numPiste: null,
      namePiste: null,
      color: null,
      length: null,
      slope: null
    };
  }

  getAllpiste() {
    return this.pisteService.getAllPiste().subscribe(res => {
      this.listpistes = res;
    });
  }
  addpiste(piste: any) {
    this.pisteService.addPiste(piste).subscribe(
      response => {
        console.log('Piste added successfully', response);
        this.getAllpiste();  // Refresh the list of pistes after adding
        this.form = false;  // Hide the form/modal
      },
      error => {
        console.error('Error adding piste:', error);  // Log any error
      }
    );
  }
  

  // Add the deletePiste method
  deletePiste(numPiste: number) {
    if (confirm('Are you sure you want to delete this piste?')) {
      this.pisteService.deletePiste(numPiste).subscribe(
        () => {
          console.log('Piste deleted successfully!');
          this.getAllpiste();  // Refresh the list after deletion
        },
        (error) => {
          console.error('Error deleting piste', error);
        }
      );
    }
  }

  open(content: any, action: any) {
    if (action != null)
      this.piste = action;  // For editing an existing piste
    else
      this.piste = { numPiste: null, namePiste: null, color: null, length: null, slope: null };  // For adding a new piste
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

  cancel() {
    this.form = false;
  }
}
