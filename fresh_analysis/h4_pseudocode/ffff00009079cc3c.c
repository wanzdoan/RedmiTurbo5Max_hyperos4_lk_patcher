
undefined8 FUN_ffff00009079cc3c(undefined1 *param_1)

{
  ulong uVar1;
  int iVar2;
  long lVar3;
  uint uVar4;
  ushort uVar5;
  ushort uVar6;
  uint uVar7;
  long lVar8;
  undefined8 uVar9;
  long lVar10;
  long lVar11;
  ushort *puVar12;
  ulong uVar13;
  uint uVar14;
  uint uVar15;
  ushort *puVar16;
  long lVar17;
  ulong uVar18;
  ushort *puVar19;
  
  uVar4 = *(uint *)(param_1 + 8);
  uVar7 = uVar4 << 1;
  uVar18 = (ulong)uVar7;
  if (uVar7 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_sboot_c_ffff0000907e064d,0x137,
               s_mp______void__0_____prec_>_0_ffff0000907ea357);
  }
  lVar8 = FUN_ffff00009077d5a8(uVar18,2);
  if (lVar8 == 0) {
    uVar9 = 0xfffffffe;
  }
  else {
    puVar19 = *(ushort **)(param_1 + 0x10);
    if (uVar4 != 0) {
      uVar13 = 0;
      lVar11 = 1;
      puVar12 = puVar19;
      do {
        uVar5 = *puVar12;
        if (uVar5 != 0) {
          lVar17 = (ulong)(uint)((int)uVar13 << 1) * 2;
          uVar14 = (uint)*(ushort *)(lVar8 + lVar17) + (uint)uVar5 * (uint)uVar5;
          *(short *)(lVar8 + lVar17) = (short)uVar14;
          uVar14 = uVar14 >> 0x10;
          uVar1 = uVar13 + 1;
          if (uVar1 < uVar4) {
            lVar3 = lVar8 + uVar13 * 2;
            puVar16 = puVar19 + uVar1;
            lVar17 = lVar11;
            do {
              uVar5 = *puVar12;
              lVar10 = lVar17 * 2;
              uVar6 = *puVar16;
              lVar17 = lVar17 + 1;
              uVar15 = (uint)uVar6 * (uint)uVar5 * 2;
              uVar14 = uVar14 + *(ushort *)(lVar3 + lVar10);
              iVar2 = uVar15 + uVar14;
              *(short *)(lVar3 + lVar10) = (short)iVar2;
              uVar14 = (uint)(CONCAT44((uint)CARRY4(uVar14,uVar15) |
                                       (uint)uVar6 * (uint)uVar5 >> 0x1f,iVar2) >> 0x10);
              puVar16 = puVar16 + 1;
              uVar15 = uVar4;
            } while (uVar4 != (uint)lVar17);
          }
          else {
            uVar15 = (uint)uVar1;
          }
          uVar15 = uVar15 + (int)uVar13;
          uVar14 = uVar14 + *(ushort *)(lVar8 + (ulong)uVar15 * 2);
          *(short *)(lVar8 + (ulong)uVar15 * 2) = (short)uVar14;
          if (0xffff < uVar14) {
            do {
              uVar15 = uVar15 + 1;
              uVar14 = *(ushort *)(lVar8 + (ulong)uVar15 * 2) + 1;
              *(short *)(lVar8 + (ulong)uVar15 * 2) = (short)uVar14;
            } while (uVar14 >> 0x10 != 0);
          }
        }
        uVar13 = uVar13 + 1;
        puVar12 = puVar12 + 1;
        lVar11 = lVar11 + 1;
      } while (uVar13 != uVar4);
    }
    do {
      if (*(short *)(lVar8 + uVar18 * 2 + -2) != 0) goto LAB_ffff00009079cdbc;
      uVar18 = uVar18 - 1;
    } while (1 < (uint)uVar18);
    uVar18 = 1;
LAB_ffff00009079cdbc:
    uVar4 = *(uint *)(param_1 + 4);
    *param_1 = 0;
    *(undefined2 *)(param_1 + 1) = 0;
    param_1[3] = 0;
    *(uint *)(param_1 + 4) = uVar7;
    *(int *)(param_1 + 8) = (int)uVar18;
    *(undefined4 *)(param_1 + 0xc) = 0;
    *(long *)(param_1 + 0x10) = lVar8;
    if (puVar19 != (ushort *)0x0) {
      FUN_ffff00009077ee50(puVar19,0,(ulong)uVar4 << 1);
      thunk_FUN_ffff0000907c314c(puVar19);
    }
    uVar9 = 0;
  }
  return uVar9;
}


