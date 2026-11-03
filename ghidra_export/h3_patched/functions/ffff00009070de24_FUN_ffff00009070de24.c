// ffff00009070de24  size=188  FUN_ffff00009070de24

undefined8 FUN_ffff00009070de24(undefined8 param_1,long param_2,long param_3)

{
  int iVar1;
  int iVar2;
  long lVar3;
  undefined8 uVar4;
  
  iVar1 = FUN_ffff00009077edac(param_1,0x20);
  lVar3 = FUN_ffff00009077f1f4(param_1,0x20);
  iVar1 = iVar1 - (int)param_1;
  iVar2 = FUN_ffff00009077ef88(param_1);
  uVar4 = 1;
  if (iVar1 < 0x14) {
    iVar2 = ~(uint)lVar3 + (int)param_1 + iVar2;
    if (iVar2 < 0x14) {
      FUN_ffff00009077efe4(param_2,param_1,(long)iVar1);
      *(undefined1 *)(param_2 + iVar1) = 0;
      FUN_ffff00009077efe4(param_3,lVar3 + 1,(long)iVar2);
      uVar4 = 0;
      *(undefined1 *)(param_3 + iVar2) = 0;
    }
  }
  return uVar4;
}


