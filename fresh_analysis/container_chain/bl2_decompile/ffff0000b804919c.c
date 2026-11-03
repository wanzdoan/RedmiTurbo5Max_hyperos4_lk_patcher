
void FUN_ffff0000b804919c(long param_1,undefined1 *param_2,int param_3)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  uint uVar8;
  uint uVar9;
  uint uVar10;
  uint uVar11;
  uint uVar12;
  uint uVar13;
  ulong uVar14;
  long lVar15;
  uint uVar16;
  uint uVar17;
  uint uVar18;
  uint local_150 [64];
  
  uVar14 = *(ulong *)(param_1 + 8);
  *(ulong *)(param_1 + 8) = uVar14 + (long)param_3;
  if (param_3 != 0) {
    uVar14 = uVar14 & 0x3f;
    do {
      lVar15 = param_1 + uVar14;
      param_3 = param_3 + -1;
      uVar14 = uVar14 + 1;
      *(undefined1 *)(lVar15 + 0x10) = *param_2;
      if (uVar14 == 0x40) {
        lVar15 = 0;
        local_150[0x3c] = 0;
        local_150[0x3d] = 0;
        local_150[0x3e] = 0;
        local_150[0x3f] = 0;
        local_150[0x38] = 0;
        local_150[0x39] = 0;
        local_150[0x3a] = 0;
        local_150[0x3b] = 0;
        local_150[0x34] = 0;
        local_150[0x35] = 0;
        local_150[0x36] = 0;
        local_150[0x37] = 0;
        local_150[0x30] = 0;
        local_150[0x31] = 0;
        local_150[0x32] = 0;
        local_150[0x33] = 0;
        local_150[0x2c] = 0;
        local_150[0x2d] = 0;
        local_150[0x2e] = 0;
        local_150[0x2f] = 0;
        local_150[0x28] = 0;
        local_150[0x29] = 0;
        local_150[0x2a] = 0;
        local_150[0x2b] = 0;
        local_150[0x24] = 0;
        local_150[0x25] = 0;
        local_150[0x26] = 0;
        local_150[0x27] = 0;
        local_150[0x20] = 0;
        local_150[0x21] = 0;
        local_150[0x22] = 0;
        local_150[0x23] = 0;
        local_150[0x1c] = 0;
        local_150[0x1d] = 0;
        local_150[0x1e] = 0;
        local_150[0x1f] = 0;
        local_150[0x18] = 0;
        local_150[0x19] = 0;
        local_150[0x1a] = 0;
        local_150[0x1b] = 0;
        local_150[0x14] = 0;
        local_150[0x15] = 0;
        local_150[0x16] = 0;
        local_150[0x17] = 0;
        local_150[0x10] = 0;
        local_150[0x11] = 0;
        local_150[0x12] = 0;
        local_150[0x13] = 0;
        local_150[0xc] = 0;
        local_150[0xd] = 0;
        local_150[0xe] = 0;
        local_150[0xf] = 0;
        local_150[8] = 0;
        local_150[9] = 0;
        local_150[10] = 0;
        local_150[0xb] = 0;
        local_150[4] = 0;
        local_150[5] = 0;
        local_150[6] = 0;
        local_150[7] = 0;
        local_150[0] = 0;
        local_150[1] = 0;
        local_150[2] = 0;
        local_150[3] = 0;
        do {
          uVar4 = *(uint *)(param_1 + 0x10 + lVar15);
          uVar4 = (uVar4 & 0xff00ff00) >> 8 | (uVar4 & 0xff00ff) << 8;
          *(uint *)((long)local_150 + lVar15) = uVar4 >> 0x10 | uVar4 << 0x10;
          lVar15 = lVar15 + 4;
        } while (lVar15 != 0x40);
        lVar15 = 0;
        do {
          lVar1 = lVar15 + 4;
          uVar4 = *(uint *)((long)local_150 + lVar15 + 4);
          uVar3 = *(uint *)((long)local_150 + lVar15 + 0x38);
          *(uint *)((long)local_150 + lVar15 + 0x40) =
               *(int *)((long)local_150 + lVar15 + 0x24) + local_150[0] +
               ((uVar4 >> 7 | uVar4 << 0x19) ^ (uVar4 >> 0x12 | uVar4 << 0xe) ^ uVar4 >> 3) +
               ((uVar3 >> 0x11 | uVar3 << 0xf) ^ (uVar3 >> 0x13 | uVar3 << 0xd) ^ uVar3 >> 10);
          lVar15 = lVar1;
          local_150[0] = uVar4;
        } while (lVar1 != 0xc0);
        lVar15 = 0;
        uVar16 = *(uint *)(param_1 + 0x5c);
        uVar7 = *(uint *)(param_1 + 0x58);
        uVar8 = *(uint *)(param_1 + 0x54);
        uVar11 = *(uint *)(param_1 + 0x6c);
        uVar9 = *(uint *)(param_1 + 0x68);
        uVar10 = *(uint *)(param_1 + 100);
        uVar3 = *(uint *)(param_1 + 0x50);
        uVar4 = *(uint *)(param_1 + 0x60);
        do {
          uVar6 = uVar4;
          uVar5 = uVar3;
          uVar13 = uVar10;
          uVar12 = uVar9;
          uVar18 = uVar8;
          uVar17 = uVar7;
          iVar2 = uVar11 + (uVar13 & uVar6) + (uVar12 & (uVar6 ^ 0xffffffff)) +
                  *(int *)((long)&DAT_ffff0000b80a9508 + lVar15) +
                  ((uVar6 >> 6 | uVar6 << 0x1a) ^ (uVar6 >> 0xb | uVar6 << 0x15) ^
                  (uVar6 >> 0x19 | uVar6 << 7)) + *(int *)((long)local_150 + lVar15);
          uVar4 = iVar2 + uVar16;
          uVar3 = ((uVar17 ^ uVar18) & uVar5 ^ uVar17 & uVar18) +
                  ((uVar5 >> 2 | uVar5 << 0x1e) ^ (uVar5 >> 0xd | uVar5 << 0x13) ^
                  (uVar5 >> 0x16 | uVar5 << 10)) + iVar2;
          lVar15 = lVar15 + 4;
          uVar16 = uVar17;
          uVar7 = uVar18;
          uVar8 = uVar5;
          uVar11 = uVar12;
          uVar9 = uVar13;
          uVar10 = uVar6;
        } while (lVar15 != 0x100);
        uVar14 = 0;
        *(uint *)(param_1 + 0x50) = uVar3 + *(uint *)(param_1 + 0x50);
        *(uint *)(param_1 + 0x54) = uVar5 + *(uint *)(param_1 + 0x54);
        *(uint *)(param_1 + 0x58) = uVar18 + *(uint *)(param_1 + 0x58);
        *(uint *)(param_1 + 0x5c) = uVar17 + *(uint *)(param_1 + 0x5c);
        *(uint *)(param_1 + 0x60) = uVar4 + *(uint *)(param_1 + 0x60);
        *(uint *)(param_1 + 100) = uVar6 + *(uint *)(param_1 + 100);
        *(uint *)(param_1 + 0x68) = uVar13 + *(uint *)(param_1 + 0x68);
        *(uint *)(param_1 + 0x6c) = uVar12 + *(uint *)(param_1 + 0x6c);
      }
      param_2 = param_2 + 1;
    } while (param_3 != 0);
  }
  return;
}


