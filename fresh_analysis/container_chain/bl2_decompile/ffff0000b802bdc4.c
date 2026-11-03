
void FUN_ffff0000b802bdc4(long param_1,int param_2,int param_3,uint param_4,int param_5)

{
  long lVar1;
  long lVar2;
  uint uVar3;
  int iVar4;
  uint *puVar5;
  int iVar6;
  uint uVar7;
  uint *puVar8;
  ulong uVar9;
  
  lVar2 = DAT_ffff0000b8125020;
  if (DAT_ffff0000b8125020 == 0) {
    iVar6 = 0;
    lVar1 = DAT_ffff0000b8125048;
  }
  else {
    iVar6 = *(int *)(DAT_ffff0000b8125020 + 0x10);
    lVar1 = DAT_ffff0000b8125048 +
            (ulong)(uint)(iVar6 * param_3 * *(int *)(DAT_ffff0000b8125020 + 0x14));
  }
  if (param_5 != 0) {
    uVar3 = 0;
    iVar4 = 0;
    puVar5 = (uint *)(lVar1 + (ulong)(uint)(iVar6 * param_2));
    do {
      puVar8 = puVar5;
      uVar9 = (ulong)param_4;
      uVar7 = uVar3;
      if (param_4 != 0) {
        do {
          uVar9 = uVar9 - 1;
          *puVar8 = *(uint *)(param_1 + (ulong)uVar7 * 4) | 0xff000000;
          puVar8 = puVar8 + 1;
          uVar7 = uVar7 + 1;
        } while (uVar9 != 0);
      }
      if (lVar2 == 0) {
        uVar7 = 0;
      }
      else {
        uVar7 = *(int *)(lVar2 + 0x14) * *(int *)(lVar2 + 0x10);
      }
      puVar5 = (uint *)((long)puVar5 + (ulong)uVar7);
      iVar4 = iVar4 + 1;
      uVar3 = uVar3 + param_4;
    } while (iVar4 != param_5);
  }
  FUN_ffff0000b803e8ec(0,0,0,0);
  return;
}


