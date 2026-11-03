
long FUN_ffff0000b80824bc(long param_1,long param_2)

{
  long lVar1;
  
  if (param_1 != 0) {
    if (param_2 == 0) {
      *(undefined8 *)(*(long *)(param_1 + -0x10) + 0x10) = *(undefined8 *)(param_1 + -8);
      FUN_ffff0000b808257c();
      lVar1 = 0;
    }
    else {
      lVar1 = FUN_ffff0000b8082280(param_2,0);
      if (lVar1 != 0) {
        FUN_ffff0000b804cd40(lVar1,param_1,param_2);
        *(undefined8 *)(*(long *)(param_1 + -0x10) + 0x10) = *(undefined8 *)(param_1 + -8);
        FUN_ffff0000b808257c();
      }
    }
    return lVar1;
  }
  lVar1 = FUN_ffff0000b8082280(param_2,0);
  return lVar1;
}


