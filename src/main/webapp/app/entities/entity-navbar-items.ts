import NavbarItem from 'app/layouts/navbar/navbar-item.model';

export const EntityNavbarItems: NavbarItem[] = [
  {
    name: 'Manufacturer',
    route: '/manufacturer',
    translationKey: 'global.menu.entities.manufacturer',
  },
  {
    name: 'Client',
    route: '/client',
    translationKey: 'global.menu.entities.client',
  },
  {
    name: 'Car',
    route: '/car',
    translationKey: 'global.menu.entities.car',
  },
  {
    name: 'PurchasedCar',
    route: '/purchased-car',
    translationKey: 'global.menu.entities.purchasedCar',
  },
];
