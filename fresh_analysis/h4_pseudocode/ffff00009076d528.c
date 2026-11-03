
int FUN_ffff00009076d528(undefined8 param_1,int *param_2)

{
  int iVar1;
  uint uVar2;
  uint uVar3;
  
  iVar1 = FUN_ffff00009076cf30(param_1,0xffffffff,0);
  if (iVar1 < 0) {
    if (iVar1 != -1) {
      return iVar1;
    }
    uVar2 = 0;
  }
  else {
    uVar3 = 0;
    do {
      uVar2 = FUN_ffff00009076d3e4(param_1,iVar1);
      if (uVar2 <= uVar3) {
        uVar2 = uVar3;
      }
      iVar1 = FUN_ffff00009076cf30(param_1,iVar1,0);
      uVar3 = uVar2;
    } while (-1 < iVar1);
    if (iVar1 != -1) {
      return iVar1;
    }
    if (uVar2 == 0xfffffffe) {
      return -0x11;
    }
  }
  if (param_2 != (int *)0x0) {
    *param_2 = uVar2 + 1;
  }
  return 0;
}


