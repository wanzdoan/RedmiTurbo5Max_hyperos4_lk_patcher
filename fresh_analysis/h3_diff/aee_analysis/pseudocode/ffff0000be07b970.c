
undefined8 FUN_ffff0000be07b970(undefined1 *param_1,long param_2,uint param_3)

{
  int iVar1;
  ushort uVar2;
  ushort uVar3;
  undefined8 uVar4;
  long lVar5;
  ulong uVar6;
  ushort uVar7;
  ulong uVar8;
  ushort *puVar9;
  long lVar10;
  ulong uVar11;
  ulong uVar12;
  uint uVar13;
  
  if (((param_1 == (undefined1 *)0x0) || (param_2 == 0)) || ((int)param_3 < 1)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_sboot_c_ffff0000be0af189,0x963,
               s_mp______void__0_____str______voi_ffff0000be0c14ab);
  }
  FUN_ffff0000be060ae0(*(undefined8 *)(param_1 + 0x10),0,(ulong)*(uint *)(param_1 + 4) << 1);
  uVar13 = 1;
  uVar12 = 0;
  *param_1 = 0;
  *(undefined4 *)(param_1 + 8) = 1;
  do {
    lVar5 = *(long *)(param_1 + 0x10);
    if (0xff < *(ushort *)(lVar5 + (ulong)(uVar13 - 1) * 2)) {
      uVar4 = FUN_ffff0000be07bc60(param_1,uVar13 + 1);
      if ((int)uVar4 != 0) {
        return uVar4;
      }
      lVar5 = *(long *)(param_1 + 0x10);
    }
    if (uVar13 == 0) {
LAB_ffff0000be07ba78:
      uVar13 = *(uint *)(param_1 + 8);
    }
    else {
      uVar6 = (ulong)uVar13;
      if (uVar13 == 1) {
        uVar8 = 0;
        uVar7 = 0;
LAB_ffff0000be07ba50:
        lVar10 = uVar6 - uVar8;
        puVar9 = (ushort *)(lVar5 + uVar8 * 2);
        do {
          uVar2 = *puVar9;
          lVar10 = lVar10 + -1;
          uVar3 = uVar7 | uVar2 << 8;
          uVar7 = uVar2 >> 8;
          *puVar9 = uVar3;
          puVar9 = puVar9 + 1;
        } while (lVar10 != 0);
      }
      else {
        uVar8 = uVar6 & 0xfffffffe;
        uVar7 = 0;
        puVar9 = (ushort *)(lVar5 + 2);
        uVar11 = uVar8;
        do {
          uVar3 = puVar9[-1];
          uVar11 = uVar11 - 2;
          uVar2 = *puVar9;
          puVar9[-1] = uVar7 | uVar3 << 8;
          uVar7 = uVar2 >> 8;
          *puVar9 = (ushort)(CONCAT22(uVar2,uVar3) >> 8);
          puVar9 = puVar9 + 2;
        } while (uVar11 != 0);
        if (uVar8 != uVar6) goto LAB_ffff0000be07ba50;
      }
      if (uVar2 < 0x100) goto LAB_ffff0000be07ba78;
      iVar1 = *(int *)(param_1 + 8);
      *(ushort *)(lVar5 + uVar6 * 2) = uVar7;
      uVar13 = iVar1 + 1;
    }
    uVar6 = (ulong)uVar13;
    if (1 < uVar13) {
      do {
        if (*(short *)(lVar5 + -2 + uVar6 * 2) != 0) goto LAB_ffff0000be07babc;
        uVar6 = uVar6 - 1;
      } while (1 < (uint)uVar6);
      uVar6 = 1;
    }
LAB_ffff0000be07babc:
    *(int *)(param_1 + 8) = (int)uVar6;
    uVar4 = FUN_ffff0000be078cc0(param_1,*(undefined1 *)(param_2 + uVar12),param_1);
    if ((int)uVar4 != 0) {
      return uVar4;
    }
    uVar12 = uVar12 + 1;
    if (uVar12 == param_3) {
      return 0;
    }
    uVar13 = *(uint *)(param_1 + 8);
  } while( true );
}


