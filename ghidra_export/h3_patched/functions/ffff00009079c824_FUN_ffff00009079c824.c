// ffff00009079c824  size=344  FUN_ffff00009079c824

undefined8 FUN_ffff00009079c824(undefined1 *param_1,long param_2)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  ushort *puVar5;
  undefined8 uVar6;
  ulong uVar7;
  ushort *puVar8;
  ushort *puVar9;
  uint uVar10;
  ushort *puVar11;
  ulong uVar12;
  ushort *puVar13;
  ulong uVar14;
  long lVar15;
  
  uVar3 = *(uint *)(param_1 + 8);
  uVar4 = *(uint *)(param_2 + 8);
  uVar1 = uVar4 + uVar3;
  uVar14 = (ulong)uVar1;
  if (uVar1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e0593,0x137,
               s_mp______void__0_____prec_>_0_ffff0000907ea1fc);
  }
  puVar5 = (ushort *)FUN_ffff00009077d2e0(uVar14,2);
  if (puVar5 == (ushort *)0x0) {
    uVar6 = 0xfffffffe;
  }
  else {
    if (uVar4 != 0) {
      uVar7 = 0;
      puVar8 = *(ushort **)(param_2 + 0x10);
      puVar9 = puVar5;
      do {
        if (*puVar8 != 0) {
          uVar10 = 0;
          if (uVar3 != 0) {
            puVar11 = *(ushort **)(param_1 + 0x10);
            uVar12 = (ulong)uVar3;
            puVar13 = puVar9;
            uVar10 = 0;
            do {
              uVar12 = uVar12 - 1;
              uVar2 = uVar10 + (uint)*puVar11 * (uint)*puVar8 + (uint)*puVar13;
              uVar10 = uVar2 >> 0x10;
              *puVar13 = (ushort)uVar2;
              puVar11 = puVar11 + 1;
              puVar13 = puVar13 + 1;
            } while (uVar12 != 0);
          }
          puVar5[uVar3 + (int)uVar7] = (ushort)uVar10;
        }
        uVar7 = uVar7 + 1;
        puVar8 = puVar8 + 1;
        puVar9 = puVar9 + 1;
      } while (uVar7 != uVar4);
    }
    if (1 < uVar1) {
      do {
        if (puVar5[uVar14 - 1] != 0) goto LAB_ffff00009079c904;
        uVar14 = uVar14 - 1;
      } while (1 < (uint)uVar14);
      uVar14 = 1;
    }
LAB_ffff00009079c904:
    uVar3 = *(uint *)(param_1 + 4);
    *param_1 = 0;
    lVar15 = *(long *)(param_1 + 0x10);
    *(undefined2 *)(param_1 + 1) = 0;
    param_1[3] = 0;
    *(uint *)(param_1 + 4) = uVar1;
    *(int *)(param_1 + 8) = (int)uVar14;
    *(undefined4 *)(param_1 + 0xc) = 0;
    *(ushort **)(param_1 + 0x10) = puVar5;
    if (lVar15 != 0) {
      FUN_ffff00009077eb88(lVar15,0,(ulong)uVar3 << 1);
      thunk_FUN_ffff0000907c2ad8(lVar15);
    }
    uVar6 = 0;
  }
  return uVar6;
}


