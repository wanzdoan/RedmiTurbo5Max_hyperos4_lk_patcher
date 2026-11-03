// ffff000090753c1c  size=216  FUN_ffff000090753c1c

undefined8 FUN_ffff000090753c1c(undefined4 param_1)

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
    FUN_ffff0000907582ac();
    break;
  case 10:
  case 0x55:
    FUN_ffff00009075afb8();
    break;
  case 0x17:
  case 0x1d:
    FUN_ffff00009075a6e8();
    break;
  case 0x18:
  case 0x1e:
    FUN_ffff00009074b2d0();
    break;
  case 0x2b:
  case 0x2c:
  case 0x2d:
    FUN_ffff00009074cb50();
    break;
  case 0x2f:
    FUN_ffff000090753cf4(0);
    FUN_ffff000090754058(0);
    break;
  case 0x30:
    FUN_ffff000090754360(0);
    uVar1 = 0;
    goto LAB_ffff000090753cdc;
  case 0x4c:
    FUN_ffff000090753cf4(1);
    FUN_ffff000090754058(1);
    break;
  case 0x4d:
    FUN_ffff000090754360(1);
    uVar1 = 1;
LAB_ffff000090753cdc:
    FUN_ffff0000907544fc(uVar1);
  }
  return 0;
}


