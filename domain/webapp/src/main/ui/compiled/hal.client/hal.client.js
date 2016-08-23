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
var http_1 = require("@angular/http");
require('rxjs/add/operator/map');
var core_1 = require("@angular/core");
var Rx_1 = require("rxjs/Rx");
var paging_entity_1 = require("./paging.entity");
var HalClient = (function () {
    function HalClient(http) {
        this.http = http;
        this.searchKey = "search";
        this.entryURL = "api";
        this.headers = new http_1.Headers();
        this.headers.append('X-Forwarded-Host', location.host);
    }
    HalClient.prototype.getList = function (resource, options) {
        var _this = this;
        return this.httpGet(resource, options).map(function (res) {
            var json = res.json();
            if (json.page) {
                var pagingEntity = _this.pagingEntity(json, resource, options);
                pagingEntity.list.forEach(function (i) { return _this.resolveLinks(i); });
                return pagingEntity;
            }
            else {
                if (json._embedded) {
                    var list = json._embedded[resource];
                    list.forEach(function (i) { return _this.resolveLinks(i); });
                    return list;
                }
                else
                    return json;
            }
        });
    };
    HalClient.prototype.save = function (link, entity) {
        return this.httpPOST(link, entity).map(function (res) { return res.json(); });
    };
    HalClient.prototype.update = function (entity) {
        return this.http.put(entity._links.self.href, entity, { headers: this.headers }).map(function (res) { return res.json(); });
    };
    HalClient.prototype.get = function (href, link) {
        var _this = this;
        return this.http.get(href, { headers: this.headers }).map(function (res) {
            var entity = res.json();
            if (entity['_embedded'] && link) {
                if (entity['_embedded'][link])
                    return _this.resolveLinks(entity['_embedded'][link]);
                return [];
            }
            return _this.resolveLinks(entity);
        });
    };
    HalClient.prototype.delete = function (entity) {
        return this.http.delete(entity._links.self.href, { headers: this.headers });
    };
    HalClient.prototype.pagingEntity = function (json, resource, options) {
        var _this = this;
        var list = json._embedded[resource];
        if (!list)
            list = [];
        var pagingEntity = new paging_entity_1.PagingEntity(list, json.page);
        var goToPageLink = function () {
            pagingEntity['goToPage'] = function (page) {
                options.href = null;
                options.params['page'] = page;
                return _this.getList(resource, options);
            };
        };
        var pagingLink = function (pLink) {
            if (json._links[pLink])
                pagingEntity[pLink] = function () { return _this.getList(resource, { href: json._links[pLink].href }); };
        };
        pagingLink('first');
        pagingLink('next');
        pagingLink('last');
        pagingLink('prev');
        goToPageLink();
        return pagingEntity;
    };
    HalClient.prototype.httpGet = function (resource, options) {
        var _this = this;
        var search = false;
        var entry = function () { return _this.getEntryPoint(); };
        if (options && options.search) {
            search = true;
            entry = function () { return _this.searchPoint(resource, options); };
        }
        return entry().flatMap(function (entity) {
            var urlSearchParams = null;
            var href = options ? options.href : null;
            if (!href) {
                var link = resource;
                if (search) {
                    link = options.search;
                }
                href = entity._links[link].href;
                if (options) {
                    options.href = href;
                    urlSearchParams = _this.resolveOptions(options);
                    href = options.href;
                }
            }
            return _this.http.get(href, {
                headers: _this.headers,
                search: urlSearchParams });
        });
    };
    HalClient.prototype.searchPoint = function (resource, options) {
        var _this = this;
        return this.getEntryPoint().flatMap(function (entity) {
            var href = entity._links[resource].href;
            href = _this.resolveSearchUri(href, options);
            return _this.http.get(href, {
                headers: _this.headers
            }).map(function (res) { return res.json(); });
        });
    };
    HalClient.prototype.resolveSearchUri = function (uri, options) {
        if (options.search) {
            uri = uri.replace(/{\?(.*)}/, "");
            var searchKey = this.searchKey;
            if (options.searchKey) {
                searchKey = options.searchKey;
            }
            if (!uri.endsWith("/"))
                uri += "/";
            uri += searchKey;
        }
        return uri;
    };
    ;
    HalClient.prototype.resolveOptions = function (options) {
        var _this = this;
        var resolveSearchUri = function (uri) {
            if (options.search) {
                var searchKey = _this.searchKey;
                if (options.searchKey) {
                    searchKey = options.searchKey;
                }
                if (!uri.endsWith("/"))
                    uri += "/";
                uri += searchKey;
            }
            //options.search = null;
            return uri;
        };
        options.href = resolveSearchUri(options.href);
        options.href = resolveUriVariables(options.href, options.params);
        return resolveUriParameters();
        function resolveUriVariables(uri, params) {
            for (var p in params)
                uri = uri.split("{" + p + "}").join(params[p]);
            return uri;
        }
        function resolveUriParameters() {
            var matcher = options.href.match(/{\?(.*)}/);
            if (!matcher)
                return null;
            options.href = options.href.split(matcher[0])[0];
            var searchParams = new http_1.URLSearchParams();
            for (var _i = 0, _a = matcher[1].split(','); _i < _a.length; _i++) {
                var p = _a[_i];
                if (options.params[p])
                    searchParams.append(p, options.params[p]);
            }
            return searchParams;
        }
    };
    HalClient.prototype.resolveLinks = function (entity) {
        var _this = this;
        if (entity instanceof Array)
            for (var i = 0; i < entity.length; i++)
                entity[i] = this.resolveLinks(entity[i]);
        var _loop_1 = function(link) {
            if (link == "self")
                return "continue";
            entity[link] = function () { return _this.get(entity._links[link].href, link); };
        };
        for (var link in entity._links) {
            var state_1 = _loop_1(link);
            if (state_1 === "continue") continue;
        }
        return entity;
    };
    HalClient.prototype.getEntryPoint = function () {
        var _this = this;
        if (this.entryPoint)
            return Rx_1.Observable.of(this.entryPoint);
        return this.http.get(this.entryURL, { headers: this.headers })
            .map(function (res) { return _this.entryPoint = res.json(); });
    };
    HalClient.prototype.httpPOST = function (link, body) {
        var _this = this;
        return this.getEntryPoint().flatMap(function (entity) {
            return _this.http.post(entity._links[link].href, body, { headers: _this.headers });
        });
    };
    HalClient = __decorate([
        core_1.Injectable(), 
        __metadata('design:paramtypes', [http_1.Http])
    ], HalClient);
    return HalClient;
}());
exports.HalClient = HalClient;
//# sourceMappingURL=hal.client.js.map