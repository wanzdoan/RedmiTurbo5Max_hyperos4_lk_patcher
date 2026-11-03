
void FUN_ffff0000be096ad4(undefined8 *param_1,long param_2,int *param_3)

{
  int iVar1;
  long lVar2;
  long *plVar3;
  
  if ((param_1 != (undefined8 *)0x0) && (*(int *)*param_1 == 0x1000000)) {
    iVar1 = FUN_ffff0000be0977f8(param_1);
    if (iVar1 != 0) {
      *(int *)(param_2 + (long)*param_3 * 0x10) = iVar1;
      iVar1 = *param_3;
      *(undefined8 **)(param_2 + (long)iVar1 * 0x10 + 8) = param_1;
      *param_3 = iVar1 + 1;
    }
    if (*(int *)*param_1 == 0x1000000) {
      plVar3 = param_1 + 2;
      lVar2 = *plVar3;
      while (lVar2 != 0) {
        FUN_ffff0000be096ad4(lVar2,param_2,param_3);
        plVar3 = (long *)(*plVar3 + 8);
        lVar2 = *plVar3;
      }
    }
  }
  return;
}


