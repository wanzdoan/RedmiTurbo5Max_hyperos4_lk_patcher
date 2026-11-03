
undefined4 FUN_ffff0000907ab034(long param_1,undefined8 param_2,long param_3,long param_4)

{
  undefined4 uVar1;
  
  uVar1 = 2;
  if ((param_1 != 0) && (param_3 != 0)) {
    if (param_4 == 0) {
      uVar1 = 3;
    }
    else {
      FUN_ffff0000907b9f24(0xffff000018000000,1);
      FUN_ffff0000907b9e80(&DAT_ffff000010000000,5);
      uVar1 = FUN_ffff0000907bacd0(0xffff00001800a000,1,param_1,param_2,param_3,param_4);
      FUN_ffff0000907b9e80(&DAT_ffff000010000000,0);
      FUN_ffff0000907b9f24(0xffff000018000000,0);
    }
  }
  return uVar1;
}


