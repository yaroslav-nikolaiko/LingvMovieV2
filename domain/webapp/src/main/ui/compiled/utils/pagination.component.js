"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require('@angular/core');
var paging_entity_1 = require("../hal.client/paging.entity");
var PaginationComponent = (function () {
    function PaginationComponent() {
        this.pageChanged = new core_1.EventEmitter();
        this.loading = new core_1.EventEmitter();
        this.totalPages = 1;
        this.pages = [];
    }
    PaginationComponent.prototype.ngOnChanges = function () {
        this.currentPage = 1;
        if (this.pagingEntity) {
            this.totalPages = this.pagingEntity.page.totalPages;
            this.pages = [];
            for (var i = 1; i <= this.totalPages; i++)
                this.pages.push(i);
        }
    };
    PaginationComponent.prototype.changePage = function (page) {
        var _this = this;
        if (this.currentPage == page)
            return;
        this.loading.emit(true);
        this.currentPage = page;
        this.pagingEntity.goToPage(page - 1).subscribe(function (pagingEntity) {
            _this.pagingEntity = pagingEntity;
            _this.pageChanged.emit(pagingEntity.list);
        });
    };
    PaginationComponent.prototype.previous = function () {
        var _this = this;
        if (this.currentPage == 1)
            return;
        this.loading.emit(true);
        this.currentPage--;
        this.pagingEntity.prev().subscribe(function (pagingEntity) {
            _this.pagingEntity = pagingEntity;
            _this.pageChanged.emit(pagingEntity.list);
        });
    };
    PaginationComponent.prototype.next = function () {
        var _this = this;
        if (this.currentPage == this.pages.length)
            return;
        this.loading.emit(true);
        this.currentPage++;
        this.pagingEntity.next().subscribe(function (pagingEntity) {
            _this.pagingEntity = pagingEntity;
            _this.pageChanged.emit(pagingEntity.list);
        });
    };
    __decorate([
        core_1.Input(), 
        __metadata('design:type', paging_entity_1.PagingEntity)
    ], PaginationComponent.prototype, "pagingEntity", void 0);
    __decorate([
        core_1.Output('page-changed'), 
        __metadata('design:type', Object)
    ], PaginationComponent.prototype, "pageChanged", void 0);
    __decorate([
        core_1.Output('loading'), 
        __metadata('design:type', Object)
    ], PaginationComponent.prototype, "loading", void 0);
    PaginationComponent = __decorate([
        core_1.Component({
            selector: 'pagination',
            template: "\n    <nav *ngIf=\"totalPages > 1\">\n        <ul class=\"pagination\">\n            <li [class.disabled]=\"currentPage == 1\">\n                <a (click)=\"previous()\" aria-label=\"Previous\">\n                <span aria-hidden=\"true\">&laquo;</span>\n                </a>\n            </li>\n            <li [class.active]=\"currentPage == page\" *ngFor=\"let page of pages\" (click)=\"changePage(page)\">\n                <a>{{ page }}</a>\n            </li>\n            <li [class.disabled]=\"currentPage == pages.length\">\n                <a (click)=\"next()\" aria-label=\"Next\">\n                <span aria-hidden=\"true\">&raquo;</span>\n                </a>\n            </li>\n        </ul>\n    </nav>  \n"
        }), 
        __metadata('design:paramtypes', [])
    ], PaginationComponent);
    return PaginationComponent;
}());
exports.PaginationComponent = PaginationComponent;
//# sourceMappingURL=pagination.component.js.map