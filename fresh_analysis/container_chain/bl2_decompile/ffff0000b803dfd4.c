
undefined4 FUN_ffff0000b803dfd4(long *param_1)

{
  undefined4 uVar1;
  
  uVar1 = 0;
  if (param_1 != (long *)0x0) {
    if ((*param_1 == 0) || (param_1[1] == 0)) {
      return 0;
    }
    uVar1 = *(undefined4 *)(*param_1 + 0x14);
  }
  return uVar1;
}


