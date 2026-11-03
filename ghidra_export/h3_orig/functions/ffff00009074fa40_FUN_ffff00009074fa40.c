// ffff00009074fa40  size=2004  FUN_ffff00009074fa40

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009074fa40
               (int param_1,undefined8 param_2,uint param_3,byte param_4,byte *param_5,char param_6)

{
  uint uVar1;
  ulong uVar2;
  byte bVar3;
  uint uVar4;
  uint uVar5;
  ulong uVar6;
  long lVar7;
  ulong uVar8;
  ulong uVar9;
  uint *puVar10;
  uint uVar11;
  int iVar12;
  uint uVar13;
  byte *pbVar14;
  ulong uVar15;
  uint *puVar16;
  ulong uVar17;
  uint local_64;
  
  uVar2 = 1;
  if (param_1 != 0x2b) {
    uVar2 = 2;
  }
  uVar5 = 0;
  if (param_1 != 0x2b) {
    uVar5 = (uint)(param_1 != 0x2d);
  }
  uVar17 = (ulong)uVar5;
  uVar4 = (param_3 & 0xff) << 0x10;
  uVar5 = (uint)param_4 * 0x10000 + 0x12902;
  uVar1 = (uint)param_4 * 0x10000 + 0x10000 & 0xff0000;
  uVar6 = (ulong)param_4;
  uVar15 = uVar17;
  do {
    if ((*(byte *)((&DAT_ffff00009080cd68)[uVar15] + 0x34) & 3) == 0) {
      if (((*(byte *)((&DAT_ffff00009080cd68)[uVar17] + 0x34) & 3) == 0) &&
         (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar17 * 8 + 0x10) + 0xc) < 0)) {
        iVar12 = 0;
        do {
          iVar12 = iVar12 + 1;
          lVar7 = uVar2 - uVar17;
          if (((uint)(iVar12 * 0x26e978d5) >> 3 | iVar12 * -0x60000000) < 0x418938) {
            do {
              lVar7 = lVar7 + -1;
            } while (lVar7 != 0);
          }
        } while (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar17 * 8 + 0x10) + 0xc)
                 < 0);
      }
      if (param_3 < 0xb0) {
        if (param_4 < 2) {
          uVar11 = (param_3 & 0xff) << 0x10 | 0x500;
          if (param_4 != 0) {
            bVar3 = *param_5;
            uVar11 = 0x1500;
LAB_ffff00009074ff38:
            uVar11 = uVar4 | (uint)bVar3 << 0x18 | uVar11;
          }
          goto LAB_ffff00009074ff3c;
        }
        puVar10 = *(uint **)(&DAT_ffff000090804ac8 + uVar15 * 8);
        puVar16 = (uint *)((ulong)(puVar10 + 1) & 0xfffffffffffffffc);
        uVar13 = ((uint)(puVar10 + 1) & 3) << 3;
        uVar9 = (long)puVar10 + 5;
        *puVar10 = uVar5 | 0x1000;
        uVar11 = (int)puVar10 * 8 + 0x28;
        *puVar16 = *puVar16 & (0xff << (ulong)uVar13 ^ 0xffffffffU) | param_3 << (ulong)uVar13;
        uVar8 = uVar6;
        pbVar14 = param_5;
        do {
          puVar10 = (uint *)(uVar9 & 0xfffffffffffffffc);
          uVar13 = uVar11 & 0x18;
          uVar11 = uVar11 + 8;
          uVar9 = uVar9 + 1;
          uVar8 = uVar8 - 1;
          *puVar10 = *puVar10 & (0xff << (ulong)uVar13 ^ 0xffffffffU) |
                     (uint)*pbVar14 << (ulong)uVar13;
          pbVar14 = pbVar14 + 1;
        } while (uVar8 != 0);
LAB_ffff00009074fe68:
        lVar7 = *(long *)(s_0123456789abcdef_ffff000090804a48 + uVar15 * 8 + 0x10);
        iVar12 = (param_4 >> 2) + 0x8002;
      }
      else {
        if (1 < param_4) {
          puVar10 = *(uint **)(&DAT_ffff000090804ac8 + uVar15 * 8);
          puVar16 = (uint *)((ulong)(puVar10 + 1) & 0xfffffffffffffffc);
          uVar13 = ((uint)(puVar10 + 1) & 3) << 3;
          uVar9 = (long)puVar10 + 5;
          *puVar10 = uVar5;
          uVar11 = (int)puVar10 * 8 + 0x28;
          *puVar16 = *puVar16 & (0xff << (ulong)uVar13 ^ 0xffffffffU) | param_3 << (ulong)uVar13;
          uVar8 = uVar6;
          pbVar14 = param_5;
          do {
            puVar10 = (uint *)(uVar9 & 0xfffffffffffffffc);
            uVar13 = uVar11 & 0x18;
            uVar11 = uVar11 + 8;
            uVar9 = uVar9 + 1;
            uVar8 = uVar8 - 1;
            *puVar10 = *puVar10 & (0xff << (ulong)uVar13 ^ 0xffffffffU) |
                       (uint)*pbVar14 << (ulong)uVar13;
            pbVar14 = pbVar14 + 1;
          } while (uVar8 != 0);
          goto LAB_ffff00009074fe68;
        }
        uVar11 = (param_3 & 0xff) << 0x10 | 0x1300;
        if (param_4 != 0) {
          bVar3 = *param_5;
          uVar11 = 0x2300;
          goto LAB_ffff00009074ff38;
        }
LAB_ffff00009074ff3c:
        **(uint **)(&DAT_ffff000090804ac8 + uVar15 * 8) = uVar11;
        lVar7 = *(long *)(s_0123456789abcdef_ffff000090804a48 + uVar15 * 8 + 0x10);
        iVar12 = 0x8001;
      }
      *(int *)(lVar7 + 0x44) = iVar12;
    }
    else {
      local_64 = 0;
      FUN_ffff00009077eb88(&local_64,0,4);
      lVar7 = *(long *)(s_0123456789abcdef_ffff000090804a48 + uVar15 * 8 + 0x10);
      uVar13 = (*(uint *)(lVar7 + 0x110) >> 4 & 1) << 4 | (*(uint *)(lVar7 + 0x110) >> 3 & 1) << 3 |
               (*(uint *)(lVar7 + 0x110) >> 5 & 1) << 5;
      uVar11 = *(uint *)(lVar7 + 0x110) & 1 | (*(uint *)(lVar7 + 0x110) >> 2 & 1) << 2;
      if (param_3 < 0xb0) {
        if (param_4 < 2) {
          if (param_4 == 0) {
            uVar11 = local_64 & 0xc0 | uVar13 | uVar11 | uVar4;
            uVar13 = 0x500;
          }
          else {
            uVar11 = local_64 & 0xc0 | uVar13 | uVar11 | uVar4 | (uint)*param_5 << 0x18;
            uVar13 = 0x1500;
          }
LAB_ffff00009074fb48:
          *(uint *)(lVar7 + 0x110) = uVar11 | uVar13;
        }
        else {
          *(uint *)(lVar7 + 0x110) = uVar1 | 0x3902 | local_64 & 0xff0000c0 | uVar13 | uVar11;
          uVar8 = *(ulong *)(&DAT_ffff000090804af8 + uVar15 * 8);
          uVar11 = (int)uVar8 * 8;
          *(uint *)(uVar8 & 0xfffffffffffffffc) =
               *(uint *)(uVar8 & 0xfffffffffffffffc) &
               (0xff << (ulong)(uVar11 & 0x18) ^ 0xffffffffU) | param_3 << (ulong)(uVar11 & 0x18);
          uVar9 = uVar6;
          pbVar14 = param_5;
          do {
            uVar11 = uVar11 + 8;
            uVar8 = uVar8 + 1;
            uVar9 = uVar9 - 1;
            *(uint *)(uVar8 & 0xfffffffffffffffc) =
                 *(uint *)(uVar8 & 0xfffffffffffffffc) &
                 (0xff << (ulong)(uVar11 & 0x18) ^ 0xffffffffU) |
                 (uint)*pbVar14 << (ulong)(uVar11 & 0x18);
            pbVar14 = pbVar14 + 1;
          } while (uVar9 != 0);
        }
      }
      else {
        if (param_4 < 2) {
          if (param_4 == 0) {
            uVar11 = local_64 & 0xc0 | uVar13 | uVar11 | uVar4;
            uVar13 = 0x1300;
          }
          else {
            uVar11 = local_64 & 0xc0 | uVar13 | uVar11 | uVar4 | (uint)*param_5 << 0x18;
            uVar13 = 0x2300;
          }
          goto LAB_ffff00009074fb48;
        }
        *(uint *)(lVar7 + 0x110) = uVar1 | 0x2902 | local_64 & 0xff0000c0 | uVar13 | uVar11;
        uVar8 = *(ulong *)(&DAT_ffff000090804af8 + uVar15 * 8);
        uVar11 = (int)uVar8 * 8;
        *(uint *)(uVar8 & 0xfffffffffffffffc) =
             *(uint *)(uVar8 & 0xfffffffffffffffc) & (0xff << (ulong)(uVar11 & 0x18) ^ 0xffffffffU)
             | param_3 << (ulong)(uVar11 & 0x18);
        uVar9 = uVar6;
        pbVar14 = param_5;
        do {
          uVar11 = uVar11 + 8;
          uVar8 = uVar8 + 1;
          uVar9 = uVar9 - 1;
          *(uint *)(uVar8 & 0xfffffffffffffffc) =
               *(uint *)(uVar8 & 0xfffffffffffffffc) &
               (0xff << (ulong)(uVar11 & 0x18) ^ 0xffffffffU) |
               (uint)*pbVar14 << (ulong)(uVar11 & 0x18);
          pbVar14 = pbVar14 + 1;
        } while (uVar9 != 0);
      }
    }
    uVar15 = uVar15 + 1;
    if (uVar15 == uVar2) {
      bVar3 = DAT_ffff000032490034;
      if (param_1 == 0x2c) {
        bVar3 = DAT_ffff0000324a0034;
      }
      if ((bVar3 & 3) == 0) {
        if (param_6 != '\0') {
          if (param_1 == 0x2d) {
            _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe;
            if (((DAT_ffff000032490034 & 3) == 0) && (_DAT_ffff00003249000c < 0)) {
              iVar12 = 0;
              do {
                iVar12 = iVar12 + 1;
                uVar15 = uVar2;
                if (((uint)(iVar12 * 0x26e978d5) >> 3 | iVar12 * -0x60000000) < 0x418938) {
                  do {
                    uVar15 = uVar15 - 1;
                  } while (uVar15 != 0);
                }
              } while (_DAT_ffff00003249000c < 0);
            }
            _DAT_ffff0000324a0030 = _DAT_ffff0000324a0030 & 0xffffffef;
          }
          else {
            uVar15 = (ulong)(param_1 != 0x2b);
            puVar10 = *(uint **)(s_0123456789abcdef_ffff000090804a48 + uVar15 * 8 + 0x10);
            *puVar10 = *puVar10 & 0xfffffffe;
            *puVar10 = *puVar10 | 1;
            if (((*(byte *)((&DAT_ffff00009080cd68)[uVar15] + 0x34) & 3) == 0) &&
               ((int)puVar10[3] < 0)) {
              iVar12 = 0;
              do {
                iVar12 = iVar12 + 1;
                lVar7 = uVar2 - uVar15;
                if (((uint)(iVar12 * 0x26e978d5) >> 3 | iVar12 * -0x60000000) < 0x418938) {
                  do {
                    lVar7 = lVar7 + -1;
                  } while (lVar7 != 0);
                }
              } while ((int)puVar10[3] < 0);
            }
          }
        }
      }
      else if ((param_6 != '\0') && ((param_1 == 0x2d || (param_1 == 0x2b)))) {
        _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffeffff | 0x10000;
      }
      return;
    }
  } while( true );
}


