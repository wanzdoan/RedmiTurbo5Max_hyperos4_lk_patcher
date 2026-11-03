
uint FUN_ffff0000be096a44(undefined8 *param_1)

{
  int iVar1;
  long lVar2;
  long *plVar3;
  uint uVar4;
  
  if ((param_1 == (undefined8 *)0x0) || (*(int *)*param_1 != 0x1000000)) {
    uVar4 = 0;
  }
  else {
    iVar1 = FUN_ffff0000be0977f8(param_1);
    uVar4 = (uint)(iVar1 != 0);
    if (*(int *)*param_1 == 0x1000000) {
      plVar3 = param_1 + 2;
      lVar2 = *plVar3;
      while (lVar2 != 0) {
        iVar1 = FUN_ffff0000be096a44();
        uVar4 = iVar1 + uVar4;
        plVar3 = (long *)(*plVar3 + 8);
        lVar2 = *plVar3;
      }
    }
  }
  return uVar4;
}


