// ffff00009077099c  size=12  FUN_ffff00009077099c

void FUN_ffff00009077099c(undefined8 *param_1,undefined8 param_2)

{
  long lVar1;
  long lVar2;
  
  if (param_1 == (undefined8 *)0x0) {
    return;
  }
  if (*(int *)*param_1 == 0x1000000) {
    lVar1 = param_1[2];
    while (lVar1 != 0) {
      lVar2 = *(long *)(lVar1 + 8);
      FUN_ffff00009077099c(lVar1,param_2);
      lVar1 = lVar2;
    }
  }
  FUN_ffff000090771394(param_2,param_1);
  return;
}


