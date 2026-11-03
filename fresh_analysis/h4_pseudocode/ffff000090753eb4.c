
undefined8 FUN_ffff000090753eb4(undefined4 param_1)

{
  undefined8 uVar1;
  
  switch(param_1) {
  case 0:
  case 1:
  case 6:
  case 0x37:
  case 0x38:
  case 0x51:
  case 0x52:
  case 0x5e:
  case 0x5f:
  case 0x60:
    FUN_ffff000090758544();
    break;
  case 10:
  case 0x55:
    FUN_ffff00009075b250();
    break;
  case 0x17:
  case 0x1d:
    FUN_ffff00009075a980();
    break;
  case 0x18:
  case 0x1e:
    FUN_ffff00009074b568();
    break;
  case 0x2b:
  case 0x2c:
  case 0x2d:
    FUN_ffff00009074cde8();
    break;
  case 0x2f:
    FUN_ffff000090753f8c(0);
    FUN_ffff0000907542f0(0);
    break;
  case 0x30:
    FUN_ffff0000907545f8(0);
    uVar1 = 0;
    goto LAB_ffff000090753f74;
  case 0x4c:
    FUN_ffff000090753f8c(1);
    FUN_ffff0000907542f0(1);
    break;
  case 0x4d:
    FUN_ffff0000907545f8(1);
    uVar1 = 1;
LAB_ffff000090753f74:
    FUN_ffff000090754794(uVar1);
  }
  return 0;
}


