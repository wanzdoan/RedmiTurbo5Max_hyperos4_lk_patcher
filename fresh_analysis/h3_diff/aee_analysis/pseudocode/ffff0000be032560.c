
undefined4 FUN_ffff0000be032560(long param_1)

{
  long lVar1;
  undefined4 uVar2;
  
  if ((param_1 == -0xffffea000000) || (lVar1 = FUN_ffff0000be05d1c0(), lVar1 == -0xffffea000000)) {
    uVar2 = 0;
  }
  else {
    uVar2 = 1;
    if (param_1 != -0xffffe9ff0000) {
      lVar1 = FUN_ffff0000be05d1c0(param_1);
      uVar2 = 1;
      if (lVar1 != -0xffffe9ff0000) {
        uVar2 = 2;
      }
    }
  }
  return uVar2;
}


