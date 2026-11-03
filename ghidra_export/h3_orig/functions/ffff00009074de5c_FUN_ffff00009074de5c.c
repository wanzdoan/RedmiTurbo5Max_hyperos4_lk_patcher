// ffff00009074de5c  size=864  FUN_ffff00009074de5c

void FUN_ffff00009074de5c(int param_1,undefined8 param_2,long param_3)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  long *plVar7;
  int iVar8;
  long lVar9;
  int iVar10;
  long lVar11;
  ulong uVar12;
  long lVar13;
  uint uVar14;
  uint *puVar15;
  
  uVar14 = *(uint *)(param_3 + 0xc4);
  if (uVar14 == 0) {
    uVar14 = *(int *)(param_3 + 0xc0) << 1;
  }
  uVar5 = 0;
  if (param_1 != 0x2b) {
    uVar5 = (uint)(param_1 != 0x2d);
  }
  uVar12 = (ulong)uVar5;
  lVar13 = 1;
  if (param_1 != 0x2b) {
    lVar13 = 2;
  }
  lVar13 = lVar13 - uVar12;
  plVar7 = &DAT_ffff00009080cd50 + uVar12;
  lVar9 = lVar13;
  do {
    lVar11 = *plVar7;
    *(undefined4 *)(lVar11 + 8) = 0x44441344;
    *(undefined4 *)(lVar11 + 0x120) = 0;
    *(undefined4 *)(lVar11 + 0x220) = 0;
    *(undefined4 *)(lVar11 + 800) = 0;
    *(undefined4 *)(lVar11 + 0x420) = 0;
    *(undefined4 *)(lVar11 + 0x520) = 0;
    *(undefined4 *)(lVar11 + 0x15c) = 0;
    *(undefined4 *)(lVar11 + 0x25c) = 0;
    *(undefined4 *)(lVar11 + 0x35c) = 0;
    *(undefined4 *)(lVar11 + 0x45c) = 0;
    *(undefined4 *)(lVar11 + 0x55c) = 0;
    *(undefined4 *)(lVar11 + 0x224) = 0;
    *(undefined4 *)(lVar11 + 0x424) = 0;
    *(undefined4 *)(lVar11 + 0x124) = 0;
    *(undefined4 *)(lVar11 + 0x524) = 0;
    *(undefined4 *)(lVar11 + 0x324) = 0;
    if (*(int *)(param_3 + 0x4ac) == 0) {
      *(undefined4 *)(lVar11 + 0x40) = 0x300501;
    }
    lVar9 = lVar9 + -1;
    plVar7 = plVar7 + 1;
  } while (lVar9 != 0);
  plVar7 = &DAT_ffff00009080cd50 + uVar12;
  lVar9 = lVar13;
  do {
    lVar11 = *plVar7;
    *(undefined4 *)(lVar11 + 0xc) = 0;
    *(undefined4 *)(lVar11 + 4) = 0x3fff0088;
    FUN_ffff00009077cbd8(1000);
    *(undefined4 *)(lVar11 + 4) = 0x3fff00c8;
    *(uint *)(lVar11 + 0x28) = *(uint *)(lVar11 + 0x28) | 1;
    FUN_ffff00009077cbd8(1000);
    *(uint *)(lVar11 + 0x28) = *(uint *)(lVar11 + 0x28) & 0xfffffffd;
    if (uVar14 != 0) {
      if (0x7c < uVar14 >> 6) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_mt6991_disp_dd_ffff0000907f2eb5,0xabc,&DAT_ffff0000907d24c9);
      }
      if (uVar14 >> 4 < 0x177) {
        if (uVar14 < 3000) {
          if (1999 < uVar14) {
            uVar5 = 0;
            iVar8 = 1;
LAB_ffff00009074e018:
            uVar6 = 0x10000000;
            iVar10 = 3;
            goto LAB_ffff00009074e020;
          }
          if (uVar14 < 0x5dc) {
            if (999 < uVar14) {
              iVar8 = 2;
              uVar5 = 0x100;
              goto LAB_ffff00009074e018;
            }
            if (uVar14 < 0x2ee) {
              if (499 < uVar14) {
                iVar8 = 4;
                uVar5 = 0x200;
                goto LAB_ffff00009074e018;
              }
              if (uVar14 < 0x1c2) {
                    /* WARNING: Subroutine does not return */
                FUN_ffff00009077cd04
                          (s_platform_mediatek_mt6991_disp_dd_ffff0000907f2eb5,0xaef,
                           &DAT_ffff0000907d24c9);
              }
              iVar8 = 0x10;
              uVar5 = 0x400;
            }
            else {
              iVar8 = 8;
              uVar5 = 0x300;
            }
          }
          else {
            iVar8 = 4;
            uVar5 = 0x200;
          }
        }
        else {
          iVar8 = 2;
          uVar5 = 0x100;
        }
        uVar6 = 0;
        iVar10 = 1;
      }
      else {
        uVar6 = 0;
        uVar5 = 0;
        iVar10 = 1;
        iVar8 = 1;
      }
LAB_ffff00009074e020:
      uVar14 = uVar14 >> 1;
      uVar4 = iVar8 * uVar14 * iVar10 >> (0xed7 < iVar8 * uVar14);
      uVar2 = (uVar4 % 0x1a) * 0x100;
      uVar3 = (uVar2 % 0x1a) * 0x100;
      uVar1 = uVar3 & 0xfe00;
      *(uint *)(lVar11 + 0x2c) =
           uVar1 * 0x4ec5 >> 0xb & 0xff00 | uVar4 / 0x1a << 0x18 | (uVar2 / 0x1a & 0xff) << 0x10 |
           ((uVar3 + (uVar1 / 0x1a) * -0x1a) * 0x100 & 0xfe00) / 0x1a;
      puVar15 = (uint *)(lVar11 + 0x30);
      *puVar15 = *puVar15 & 0xfffff8ff | uVar5;
      *puVar15 = *puVar15 & 0xefffffff | uVar6;
    }
    FUN_ffff00009077cbd8(1000);
    *(uint *)(lVar11 + 0x30) = *(uint *)(lVar11 + 0x30) | 1;
    FUN_ffff00009077cbd8(1000);
    lVar9 = lVar9 + -1;
    plVar7 = plVar7 + 1;
    if (lVar9 == 0) {
      plVar7 = (long *)(s_0123456789abcdef_ffff000090804a48 + uVar12 * 8 + 0x10);
      do {
        lVar9 = *plVar7;
        *(uint *)(lVar9 + 0x614) = *(uint *)(lVar9 + 0x614) | 1;
        if (*(int *)(param_3 + 0x4ac) == 0) {
          *(undefined4 *)(lVar9 + 0x61c) = 0x3210;
        }
        lVar13 = lVar13 + -1;
        plVar7 = plVar7 + 1;
      } while (lVar13 != 0);
      return;
    }
  } while( true );
}


