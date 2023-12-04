if (localStorage.getItem('api-url') === null) {
  localStorage.setItem('api-url', 'http://192.168.196.174:8765/api');
}

export class ApiUrl {
  static baseUrl = localStorage.getItem('api-url');

  static getAllRoutes() {
    return this.baseUrl + '/routes';
  }

  static getRouteById(routeId: number) {
    return this.baseUrl + '/routes/' + routeId;
  }
}