// ffff000090793b8c  size=440  FUN_ffff000090793b8c

undefined8 FUN_ffff000090793b8c(long *param_1,undefined4 param_2,undefined4 param_3)

{
  byte bVar1;
  uint uVar2;
  ulong uVar3;
  int iVar4;
  long lVar5;
  long lVar6;
  undefined8 uVar7;
  undefined8 uVar8;
  uint uVar9;
  ulong uVar10;
  
  lVar5 = FUN_ffff00009071676c();
  do {
    uVar8 = daif;
    uVar9 = (uint)uVar8;
    if ((uVar9 >> 7 & 1) == 0) {
      uVar10 = daif;
      daif = uVar10 | 0x80;
    }
    bVar1 = *(byte *)(param_1 + 0x2f);
    uVar2 = 1 << (ulong)(bVar1 & 0x1f);
    uVar10 = (ulong)(int)uVar2;
    if ((param_1[0x10] & uVar10) == 0) {
      param_1[9] = 0;
      param_1[0x10] = param_1[0x10] | uVar10;
      if ((uVar9 >> 7 & 1) == 0) {
        uVar3 = daif;
        daif = uVar3 & 0xffffffffffffff7f;
      }
      lVar5 = param_1[0xf];
      *(undefined8 *)(lVar5 + 0x20) = 0;
      *(undefined2 *)(lVar5 + 0x40) = 0x100;
      *(undefined4 *)(lVar5 + 0x38) = 2;
      *(uint *)(lVar5 + 0x3c) = (uint)bVar1;
      *(undefined4 *)(param_1 + 0x37) = param_2;
      iVar4 = FUN_ffff000090790e44(param_1,lVar5);
      if (iVar4 == 0) {
        uVar8 = *(undefined8 *)(param_1[0xf] + 8);
        param_1[0x30] = param_1[0x30] | uVar10;
        FUN_ffff000090707cc4(uVar8,0x20);
        FUN_ffff000090707d04(*(undefined8 *)(param_1[0xf] + 0x10),0x34);
        FUN_ffff000090707cc4(*(undefined8 *)(param_1[0xf] + 0x18),0x800);
        FUN_ffff000090707ce4(*(undefined8 *)param_1[0xf],0x20);
        lVar6 = *param_1;
        if (*(int *)(lVar6 + 0x20) != 0) {
          *(int *)(lVar6 + 0x20) = *(int *)(lVar6 + 0x20);
        }
        *(uint *)(lVar6 + 0x58) = uVar2;
        uVar7 = FUN_ffff000090790720(param_1,lVar5,param_3);
        uVar8 = daif;
        uVar9 = (uint)uVar8;
      }
      else {
        uVar7 = 0xffffffff;
        uVar8 = daif;
        uVar9 = (uint)uVar8;
      }
      if ((uVar9 >> 7 & 1) == 0) {
        uVar10 = daif;
        daif = uVar10 | 0x80;
        param_1[9] = 0;
        param_1[0x10] = param_1[0x10] & (long)(int)~uVar2;
        uVar10 = daif;
        daif = uVar10 & 0xffffffffffffff7f;
      }
      else {
        param_1[9] = 0;
        param_1[0x10] = param_1[0x10] & (long)(int)~uVar2;
      }
      return uVar7;
    }
    param_1[9] = 0;
    if ((uVar9 >> 7 & 1) == 0) {
      uVar10 = daif;
      daif = uVar10 & 0xffffffffffffff7f;
    }
    lVar6 = FUN_ffff00009071676c();
  } while ((ulong)(lVar6 - lVar5) < 0x4c4b41);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_common_ufs_ufs_ffff0000907d6da1,0x353,&DAT_ffff0000907d24c9);
}


