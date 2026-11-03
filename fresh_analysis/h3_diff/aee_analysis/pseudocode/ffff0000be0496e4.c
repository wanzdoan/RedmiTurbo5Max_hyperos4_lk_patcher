
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be0496e4(code *param_1)

{
  int iVar1;
  int iVar2;
  undefined8 uVar3;
  ulong uVar4;
  int iVar5;
  int iVar6;
  
  _DAT_ffff00001441610c = 0x1ff0a;
  _DAT_ffff000014416118 = 1;
  _DAT_ffff00001441612c = 1;
  uVar3 = FUN_ffff0000be05d228(0xffff000014416400);
  iVar2 = FUN_ffff0000be05d228(0xffff000014416400);
  iVar6 = 0xffc;
  iVar5 = 0x100;
  do {
    _DAT_ffff000014416130 = iVar2;
    uVar4 = (*param_1)(uVar3,(long)iVar5);
    if ((uVar4 & 1) == 0) {
      FUN_ffff0000be05fd98(s__INFRA_BUS_HRE__AEE_EXPDB_SRAMdu_ffff0000be0bd733);
      return;
    }
    iVar6 = iVar6 - iVar5;
    iVar2 = iVar2 + 0x100;
    iVar1 = iVar6;
    if (iVar5 <= iVar6) {
      iVar1 = iVar5;
    }
    iVar5 = iVar1;
  } while (0 < iVar6);
  return;
}


