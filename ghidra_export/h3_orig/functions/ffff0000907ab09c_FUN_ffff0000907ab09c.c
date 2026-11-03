// ffff0000907ab09c  size=184  FUN_ffff0000907ab09c

undefined4 FUN_ffff0000907ab09c(long param_1,long param_2,long param_3)

{
  undefined4 uVar1;
  
  uVar1 = 3;
  if ((param_2 != 0) && (param_3 != 0)) {
    if (param_1 == 0) {
      uVar1 = 2;
    }
    else {
      FUN_ffff0000907b98b0(0xffff000018000000,1);
      FUN_ffff0000907b980c(&DAT_ffff000010000000,5);
      uVar1 = FUN_ffff0000907ba7ac(0xffff00001800a000,4,param_1,param_2,param_3);
      FUN_ffff0000907b980c(&DAT_ffff000010000000,0);
      FUN_ffff0000907b98b0(0xffff000018000000,0);
    }
  }
  return uVar1;
}


