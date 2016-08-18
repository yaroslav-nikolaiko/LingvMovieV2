import {Component, Input, Output, EventEmitter} from '@angular/core';
import {OnChanges} from '@angular/core';
import {PagingEntity} from "../hal.client/paging.entity";

@Component({
    selector: 'pagination',
    template: `
    <nav *ngIf="totalPages > 1">
        <ul class="pagination">
            <li [class.disabled]="currentPage == 1">
                <a (click)="previous()" aria-label="Previous">
                <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
            <li [class.active]="currentPage == page" *ngFor="let page of pages" (click)="changePage(page)">
                <a>{{ page }}</a>
            </li>
            <li [class.disabled]="currentPage == pages.length">
                <a (click)="next()" aria-label="Next">
                <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </ul>
    </nav>  
`
})
export class PaginationComponent implements OnChanges {
    @Input() pagingEntity: PagingEntity<any>;
    @Output('page-changed') pageChanged = new EventEmitter();
    @Output('loading') loading = new EventEmitter();
    totalPages: number = 1;
    pages: number[] = [];
    currentPage;

    ngOnChanges(){
        this.currentPage = 1;
        if(this.pagingEntity){
            this.totalPages = this.pagingEntity.page.totalPages;
            this.pages = [];
            for (var i = 1; i <= this.totalPages; i++)
                this.pages.push(i);
        }
    }

    changePage(page){
        if(this.currentPage == page) return;
        this.loading.emit(true);
        this.currentPage = page;
        this.pagingEntity.goToPage(page-1).subscribe(pagingEntity=>{
            this.pagingEntity = pagingEntity;
            this.pageChanged.emit(pagingEntity.list)
        });
    }

    previous(){
        if (this.currentPage == 1)
            return;
        this.loading.emit(true);

        this.currentPage--;
        this.pagingEntity.prev().subscribe(pagingEntity=>{
            this.pagingEntity = pagingEntity;
            this.pageChanged.emit(pagingEntity.list)
        });
    }

    next(){
        if (this.currentPage == this.pages.length)
            return;
        this.loading.emit(true);

        this.currentPage++;
        this.pagingEntity.next().subscribe(pagingEntity=>{
            this.pagingEntity = pagingEntity;
            this.pageChanged.emit(pagingEntity.list)
        });
    }
}