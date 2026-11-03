
void FUN_ffff0000be03535c(void)

{
  int iVar1;
  uint uVar2;
  uint uVar3;
  long lVar4;
  int iVar5;
  ulong uVar6;
  long lVar7;
  ulong uVar8;
  ulong uVar9;
  uint *puVar10;
  long lVar11;
  
  lVar4 = DAT_ffff0000be121818;
  if (DAT_ffff0000be121818 == 0) {
    return;
  }
  iVar1 = *(int *)(DAT_ffff0000be121818 + 0x18);
  if (iVar1 != 0) {
    iVar5 = 0;
    uVar2 = *(uint *)(DAT_ffff0000be121818 + 0x14);
    uVar6 = (ulong)uVar2;
    uVar3 = DAT_ffff0000be121828 | DAT_ffff0000be121848 | 0xff000000;
    uVar8 = uVar6 & 0xfffffffe;
    lVar7 = DAT_ffff0000be121840;
    uVar9 = uVar6;
    do {
      if (uVar2 != 0) {
        if (uVar2 == 1) {
          uVar9 = 0;
LAB_ffff0000be035400:
          lVar11 = uVar6 - uVar9;
          puVar10 = (uint *)(lVar7 + uVar9 * 4);
          do {
            lVar11 = lVar11 + -1;
            *puVar10 = uVar3;
            puVar10 = puVar10 + 1;
          } while (lVar11 != 0);
        }
        else {
          puVar10 = (uint *)(lVar7 + 4);
          uVar9 = uVar8;
          do {
            puVar10[-1] = uVar3;
            *puVar10 = uVar3;
            puVar10 = puVar10 + 2;
            uVar9 = uVar9 - 2;
          } while (uVar9 != 0);
          uVar9 = uVar8;
          if (uVar8 != uVar6) goto LAB_ffff0000be035400;
        }
        uVar9 = (ulong)*(uint *)(lVar4 + 0x14);
      }
      iVar5 = iVar5 + 1;
      lVar7 = lVar7 + (ulong)(uint)(*(int *)(lVar4 + 0x10) * (int)uVar9);
    } while (iVar5 != iVar1);
  }
  FUN_ffff0000be047cb0(0,0,0,0);
  return;
}


