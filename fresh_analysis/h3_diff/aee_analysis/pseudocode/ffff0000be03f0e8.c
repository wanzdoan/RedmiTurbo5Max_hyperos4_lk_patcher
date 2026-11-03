
undefined8 FUN_ffff0000be03f0e8(undefined4 param_1)

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
    FUN_ffff0000be043520();
    break;
  case 10:
  case 0x55:
    FUN_ffff0000be04635c();
    break;
  case 0x17:
  case 0x1d:
    FUN_ffff0000be04595c();
    break;
  case 0x18:
  case 0x1e:
    FUN_ffff0000be036814();
    break;
  case 0x2b:
  case 0x2c:
  case 0x2d:
    FUN_ffff0000be03801c();
    break;
  case 0x2f:
    FUN_ffff0000be03f1c0(0);
    FUN_ffff0000be03f524(0);
    break;
  case 0x30:
    FUN_ffff0000be03f82c(0);
    uVar1 = 0;
    goto LAB_ffff0000be03f1a8;
  case 0x4c:
    FUN_ffff0000be03f1c0(1);
    FUN_ffff0000be03f524(1);
    break;
  case 0x4d:
    FUN_ffff0000be03f82c(1);
    uVar1 = 1;
LAB_ffff0000be03f1a8:
    FUN_ffff0000be03f9c8(uVar1);
  }
  return 0;
}


