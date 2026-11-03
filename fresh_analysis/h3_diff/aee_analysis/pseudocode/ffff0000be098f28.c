
long FUN_ffff0000be098f28(int *param_1,long param_2)

{
  uint uVar1;
  int iVar2;
  byte *pbVar3;
  uint uVar4;
  long lVar5;
  uint uVar6;
  byte *pbVar7;
  int iVar8;
  long lVar9;
  
  uVar6 = (*(uint *)(param_2 + 8) & 0xff00ff00) >> 8 | (*(uint *)(param_2 + 8) & 0xff00ff) << 8;
  pbVar3 = (byte *)FUN_ffff0000be055c70(*(undefined8 *)(param_1 + 2),uVar6 >> 0x10 | uVar6 << 0x10);
  if (pbVar3 != (byte *)0x0) {
    uVar6 = (uint)*pbVar3;
    if (*pbVar3 == 0) {
      uVar4 = 0;
    }
    else {
      uVar4 = 0;
      pbVar7 = pbVar3;
      do {
        pbVar7 = pbVar7 + 1;
        uVar4 = uVar6 + uVar4 * 0x334b;
        uVar6 = (uint)*pbVar7;
      } while (uVar6 != 0);
    }
    iVar8 = *param_1;
    uVar6 = iVar8 - 1;
    if (0 < iVar8) {
      do {
        uVar4 = uVar4 & uVar6;
        lVar9 = *(long *)(param_1 + 4);
        lVar5 = *(long *)(lVar9 + (long)(int)uVar4 * 8);
        if ((lVar5 == 0) ||
           ((uVar1 = *(uint *)(lVar5 + 8),
            uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8,
            lVar5 = FUN_ffff0000be055c70(*(undefined8 *)(param_1 + 2),uVar1 >> 0x10 | uVar1 << 0x10)
            , lVar5 != 0 && (iVar2 = thunk_FUN_ffff0000be060cb8(lVar5,pbVar3), iVar2 == 0)))) {
          return lVar9 + (long)(int)uVar4 * 8;
        }
        uVar4 = uVar4 + 1;
        iVar8 = iVar8 + -1;
      } while (iVar8 != 0);
    }
  }
  return 0;
}


