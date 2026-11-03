
undefined8 FUN_ffff0000b8035d24(undefined4 param_1)

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
    FUN_ffff0000b803a15c();
    break;
  case 10:
  case 0x55:
    FUN_ffff0000b803cf98();
    break;
  case 0x17:
  case 0x1d:
    FUN_ffff0000b803c598();
    break;
  case 0x18:
  case 0x1e:
    FUN_ffff0000b802d450();
    break;
  case 0x2b:
  case 0x2c:
  case 0x2d:
    FUN_ffff0000b802ec58();
    break;
  case 0x2f:
    FUN_ffff0000b8035dfc(0);
    FUN_ffff0000b8036160(0);
    break;
  case 0x30:
    FUN_ffff0000b8036468(0);
    uVar1 = 0;
    goto LAB_ffff0000b8035de4;
  case 0x4c:
    FUN_ffff0000b8035dfc(1);
    FUN_ffff0000b8036160(1);
    break;
  case 0x4d:
    FUN_ffff0000b8036468(1);
    uVar1 = 1;
LAB_ffff0000b8035de4:
    FUN_ffff0000b8036604(uVar1);
  }
  return 0;
}


