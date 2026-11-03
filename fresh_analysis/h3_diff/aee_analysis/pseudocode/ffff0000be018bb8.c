
int FUN_ffff0000be018bb8(long param_1,uint param_2)

{
  uint uVar1;
  long *plVar2;
  undefined8 uVar3;
  int iVar4;
  int iVar5;
  uint uVar6;
  
  iVar4 = FUN_ffff0000be0133d4();
  if (iVar4 == 3) {
LAB_ffff0000be018bec:
    FUN_ffff0000be0133e8(3);
    iVar4 = -1;
  }
  else {
    iVar4 = 0;
    plVar2 = DAT_ffff0000be108818;
    for (; DAT_ffff0000be108818 = plVar2, param_2 != 0; param_2 = param_2 - uVar1) {
      *plVar2 = param_1;
      uVar3 = DAT_ffff0000be108820;
      uVar6 = param_2;
      if (0x3fff < param_2) {
        uVar6 = 0x4000;
      }
      plVar2[2] = (long)&LAB_ffff0000be018ddc;
      *(uint *)(plVar2 + 1) = uVar6;
      iVar5 = FUN_ffff0000be0694f4(uVar3);
      if (((iVar5 < 0) ||
          (iVar5 = FUN_ffff0000be01a590(&DAT_ffff0000be108828,0xffffffff), iVar5 != 0)) ||
         (DAT_ffff0000be108858 < 0)) goto LAB_ffff0000be018bec;
      uVar1 = *(uint *)(DAT_ffff0000be108818 + 1);
      iVar4 = uVar1 + iVar4;
      if (uVar1 != uVar6) {
        return iVar4;
      }
      param_1 = param_1 + (ulong)uVar1;
      plVar2 = DAT_ffff0000be108818;
    }
  }
  return iVar4;
}


