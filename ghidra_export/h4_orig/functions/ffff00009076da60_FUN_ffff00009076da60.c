// ffff00009076da60  size=168  FUN_ffff00009076da60

long FUN_ffff00009076da60(long param_1,int param_2,int *param_3)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  
  uVar1 = *(uint *)(param_1 + 8);
  iVar2 = FUN_ffff00009076ca9c();
  if ((iVar2 < 0) || (iVar2 = FUN_ffff00009076cea0(param_1,param_2), iVar2 < 0)) {
LAB_ffff00009076dae4:
    lVar3 = 0;
    if (param_3 == (int *)0x0) {
      return 0;
    }
  }
  else {
    uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
    lVar3 = param_1 + (ulong)(uVar1 >> 0x10 | uVar1 << 0x10) + (long)param_2 + 4;
    uVar1 = (*(uint *)(param_1 + 0x14) & 0xff00ff00) >> 8 |
            (*(uint *)(param_1 + 0x14) & 0xff00ff) << 8;
    if ((uVar1 >> 0x10 | uVar1 << 0x10) < 0x10) {
      lVar3 = FUN_ffff00009077f4bc(lVar3,0x2f);
      if (lVar3 == 0) {
        iVar2 = -0xb;
        goto LAB_ffff00009076dae4;
      }
      lVar3 = lVar3 + 1;
    }
    if (param_3 == (int *)0x0) {
      return lVar3;
    }
    iVar2 = FUN_ffff00009077f250(lVar3);
  }
  *param_3 = iVar2;
  return lVar3;
}


