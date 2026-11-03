// File offset entry: 0x3ef08

void FUN_ffff0000b803ef08(undefined8 param_1)

{
  long lVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  DAT_ffff0000b81298a8 = 0;
  lVar1 = FUN_ffff0000b8036e98(0,DAT_ffff0000b81298b8);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  DAT_ffff0000b81298c8 = lVar1;
  if (lVar1 == 0) {
    return;
  }
  if ((DAT_ffff0000b81298b0 == (undefined8 *)0x0) || (*(int *)*DAT_ffff0000b81298b0 != 2)) {
    uVar2 = 0x102;
  }
  else {
    uVar2 = 0x2d;
    if (((int *)*DAT_ffff0000b81298b0)[0x66] != 2) {
      uVar3 = 0x2b;
      if (*(int *)(DAT_ffff0000b81298b0 + 2) == 2) {
        uVar3 = 0x2c;
      }
      uVar2 = 0x2d;
      if (*(int *)(DAT_ffff0000b81298b0 + 2) != 3) {
        uVar2 = uVar3;
      }
    }
  }
  FUN_ffff0000b80374ac(lVar1,uVar2);
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  FUN_ffff0000b8038460(DAT_ffff0000b81298c8,DAT_ffff0000b81298b0[1],param_1);
  return;
}


