// File offset entry: 0x3f04c

undefined4 FUN_ffff0000b803f04c(undefined8 param_1,undefined8 param_2)

{
  long lVar1;
  undefined4 uVar2;
  
  if ((DAT_ffff0000b81282dc & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
    DAT_ffff0000b81282dc = 1;
  }
  if (DAT_ffff0000b81298b0 == 0) {
    lVar1 = FUN_ffff0000b803d960(param_1,param_2);
    if ((DAT_ffff0000b81282dc & 1) == 0) {
      FUN_ffff0000b804d070(&DAT_ffff0000b81298a0,0,0x38);
      DAT_ffff0000b81282dc = 1;
    }
    uVar2 = 3;
    DAT_ffff0000b81298b0 = lVar1;
    if (lVar1 != 0) {
      uVar2 = 0;
    }
  }
  else {
    uVar2 = 0;
  }
  return uVar2;
}


