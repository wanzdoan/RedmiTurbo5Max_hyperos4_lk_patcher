// ffff00009075be20  size=32  FUN_ffff00009075be20

undefined8 FUN_ffff00009075be20(long *param_1)

{
  undefined8 uVar1;
  
  uVar1 = 0;
  if (param_1 != (long *)0x0) {
    if ((*param_1 == 0) || ((undefined8 *)param_1[1] == (undefined8 *)0x0)) {
      return 0;
    }
    uVar1 = *(undefined8 *)param_1[1];
  }
  return uVar1;
}


