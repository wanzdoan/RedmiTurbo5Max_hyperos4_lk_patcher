// ffff0000907536dc  size=1172  FUN_ffff0000907536dc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907536dc(int param_1,long param_2,uint param_3,char param_4)

{
  long lVar1;
  byte bVar2;
  byte bVar3;
  bool bVar4;
  bool bVar5;
  int iVar6;
  ulong uVar7;
  uint uVar8;
  uint uVar9;
  byte *pbVar11;
  byte *pbVar12;
  long lVar13;
  ulong uVar14;
  uint uVar15;
  uint uVar16;
  uint *puVar17;
  uint *puVar18;
  long lVar19;
  uint uVar20;
  ulong uVar21;
  ulong uVar10;
  
  bVar4 = param_1 != 0x2b;
  lVar1 = 1;
  if (bVar4) {
    lVar1 = 2;
  }
  bVar5 = param_1 != 0x2d;
  uVar8 = (uint)bVar4;
  uVar15 = 0;
  if (bVar5) {
    uVar15 = uVar8;
  }
  if (!bVar5) {
    uVar8 = 1;
  }
  uVar20 = 0;
  uVar9 = 0;
  if (param_1 != 0x2b) {
    uVar9 = (uint)bVar5;
  }
  uVar10 = (ulong)uVar9;
  uVar21 = (ulong)uVar15;
  do {
    pbVar12 = (byte *)(param_2 + (ulong)uVar20 * 0x83);
    bVar2 = *pbVar12;
    bVar3 = pbVar12[2];
    uVar7 = (ulong)bVar3;
    uVar15 = (uint)bVar3;
    uVar9 = (uint)pbVar12[1];
    if ((bVar2 == 0) && (uVar9 == 0xff)) {
      FUN_ffff00009077cbd8(uVar15 * 1000);
    }
    else {
      pbVar11 = (byte *)(param_2 + 3 + (ulong)uVar20 * 0x83);
      if ((*(byte *)((&DAT_ffff00009080cd68)[uVar21] + 0x34) & 3) == 0) {
        lVar13 = (&DAT_ffff00009080cd68)[uVar10];
        if (((*(byte *)(lVar13 + 0x34) & 3) == 0) &&
           (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar10 * 8 + 0x10) + 0xc) < 0))
        {
          iVar6 = 0;
          do {
            iVar6 = iVar6 + 1;
            lVar19 = lVar1 - uVar10;
            if (((uint)(iVar6 * 0x26e978d5) >> 3 | iVar6 * -0x60000000) < 0x418938) {
              do {
                lVar19 = lVar19 + -1;
              } while (lVar19 != 0);
            }
          } while (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar10 * 8 + 0x10) + 0xc
                           ) < 0);
        }
        puVar17 = *(uint **)(&DAT_ffff000090804ac8 + uVar21 * 8);
        *puVar17 = 0;
        if (uVar15 < 2) {
          uVar16 = (uint)bVar2 << 8;
          if (uVar15 != 0) {
            uVar16 = uVar16 | (uint)pbVar12[3] << 0x18;
          }
          iVar6 = 0x8001;
          *puVar17 = uVar16 | uVar9 << 0x10;
        }
        else {
          puVar18 = (uint *)((ulong)(puVar17 + 1) & 0xfffffffc);
          uVar14 = (long)puVar17 + 5;
          *puVar17 = ((uint)bVar2 << 8 | uVar15 << 0x10) + 0x10002;
          uVar16 = ((uint)(puVar17 + 1) & 3) << 3;
          uVar15 = (int)puVar17 * 8 + 0x28;
          *puVar18 = *puVar18 & (0xff << (ulong)uVar16 ^ 0xffffffffU) | uVar9 << (ulong)uVar16;
          do {
            puVar17 = (uint *)(uVar14 & 0xfffffffc);
            uVar9 = uVar15 & 0x18;
            uVar15 = uVar15 + 8;
            uVar14 = uVar14 + 1;
            uVar7 = uVar7 - 1;
            *puVar17 = *puVar17 & (0xff << (ulong)uVar9 ^ 0xffffffffU) |
                       (uint)*pbVar11 << (ulong)uVar9;
            pbVar11 = pbVar11 + 1;
          } while (uVar7 != 0);
          iVar6 = (bVar3 >> 2) + 0x8002;
        }
        *(int *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar21 * 8 + 0x10) + 0x44) = iVar6;
        if (param_4 != '\0') {
          if (param_1 == 0x2d) {
            _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe | 1;
          }
          else {
            puVar17 = *(uint **)(s_0123456789abcdef_ffff000090804a48 + (ulong)bVar4 * 8 + 0x10);
            *puVar17 = *puVar17 & 0xfffffffe;
            *puVar17 = *puVar17 | 1;
          }
          if (((*(byte *)(lVar13 + 0x34) & 3) == 0) &&
             (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar10 * 8 + 0x10) + 0xc) < 0
             )) {
            iVar6 = 0;
            do {
              iVar6 = iVar6 + 1;
              lVar13 = lVar1 - uVar10;
              if (((uint)(iVar6 * 0x26e978d5) >> 3 | iVar6 * -0x60000000) < 0x418938) {
                do {
                  lVar13 = lVar13 + -1;
                } while (lVar13 != 0);
              }
            } while (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + uVar10 * 8 + 0x10) +
                             0xc) < 0);
          }
        }
      }
      else {
        lVar13 = *(long *)(s_0123456789abcdef_ffff000090804a48 + uVar21 * 8 + 0x10);
        if (bVar3 < 2) {
          uVar15 = *(uint *)(lVar13 + 0x110) & 0xfd | uVar9 << 0x10;
          if (bVar3 != 0) {
            uVar15 = uVar15 | (uint)pbVar12[3] << 0x18;
          }
          *(uint *)(lVar13 + 0x110) = uVar15 | (uint)bVar2 << 8;
        }
        else {
          *(uint *)(lVar13 + 0x110) =
               *(uint *)(lVar13 + 0x110) & 0xff0000ff | (uint)bVar2 << 8 |
               (uint)bVar3 * 0x10000 + 0x10000 & 0xff0000 | 2;
          uVar14 = *(ulong *)(&DAT_ffff000090804af8 + uVar21 * 8);
          uVar15 = (int)uVar14 * 8;
          *(uint *)(uVar14 & 0xfffffffffffffffc) =
               *(uint *)(uVar14 & 0xfffffffffffffffc) &
               (0xff << (ulong)(uVar15 & 0x18) ^ 0xffffffffU) | uVar9 << (ulong)(uVar15 & 0x18);
          do {
            uVar15 = uVar15 + 8;
            uVar14 = uVar14 + 1;
            uVar7 = uVar7 - 1;
            *(uint *)(uVar14 & 0xfffffffffffffffc) =
                 *(uint *)(uVar14 & 0xfffffffffffffffc) &
                 (0xff << (ulong)(uVar15 & 0x18) ^ 0xffffffffU) |
                 (uint)*pbVar11 << (ulong)(uVar15 & 0x18);
            pbVar11 = pbVar11 + 1;
          } while (uVar7 != 0);
        }
        if ((param_4 != '\0') && ((param_1 == 0x2d || (param_1 == 0x2b)))) {
          _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffeffff | 0x10000;
        }
      }
    }
    uVar20 = uVar20 + 1;
  } while ((uVar20 < param_3) || (bVar5 = uVar21 < uVar8, uVar21 = uVar21 + 1, bVar5));
  return;
}


